# Copyright (C) 2022 SpyroSoft S.A.
# SPDX-License-Identifier: MIT

import inspect
import json

from qface.idl.domain import Module, Interface, Property, Parameter, Field, Struct
from qface.helper.generic import lower_first, upper_first
from qface.helper.qtcpp import Filters

def aidl_type(symbol):
    if symbol.type.is_bool:
        return 'boolean'
    elif symbol.type.is_real:
        return 'float'
    elif symbol.type.is_string:
        return 'String'
    else: 
        return symbol.type.name

def append_unique_structure(list, type):
    if type.is_struct and not any(type.name == t.name for t in list):
        list.append(type)

def structures(interface):
    types = []
    for o in interface.operations:
        for p in o.parameters:
            append_unique_structure(types, p.type)
        append_unique_structure(types, o.type)

    for s in interface.signals:
        for p in s.parameters:
            append_unique_structure(types, p.type)

    for p in interface.properties:
        append_unique_structure(types, p.type)

    return types

def module_path(module, joint = '/', prefix = ""):
    name = module.name
    parts = name.split(".")
    path = joint.join(parts)
    if prefix:
        path = prefix + joint + path

    return path

def module_ns(module, prefix = ""):
    name = module.name
    parts = name.split(".")
    ns = '::'.join(parts)
    if prefix:
        ns = prefix + "::" + ns

    return ns

filters['module_path'] = module_path
filters['module_ns'] = module_ns
filters['aidl_type'] = aidl_type
filters['structures'] = structures
