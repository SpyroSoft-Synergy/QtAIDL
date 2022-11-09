// Copyright (C) 2022 SpyroSoft Synergy S.A.
// SPDX-License-Identifier: MIT

package com.spyro_soft.qtaidl

import com.android.build.api.dsl.AndroidSourceSet
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.plugins.ExtensionAware

fun AndroidSourceSet.qface(action: SourceDirectorySet.() -> Unit) {
    (this as? ExtensionAware)
        ?.extensions
        ?.getByName("qface")
        ?.let { it as? SourceDirectorySet }
        ?.apply(action)
}