// Copyright (C) 2022 SpyroSoft Synergy S.A.
// SPDX-License-Identifier: MIT

package com.spyro_soft.qtaidl

import org.gradle.api.Project
import org.gradle.internal.os.OperatingSystem
import org.gradle.kotlin.dsl.getByType

internal val Project.qtaidlPlugin: QtAidlPluginExtension
    get() = extensions.getByType()

internal val isWindows: Boolean
    get() = OperatingSystem.current().isWindows

/**
 * Returns simplified operating system name
 */
internal val os: String
    get() {
        return when {
            OperatingSystem.current().isMacOsX -> "MacOSX"
            isWindows -> "Windows"
            else -> "Linux"
        }
    }


/**
 * Returns system architecture name
 */
internal val arch: String
    get() {
        val arch = System.getProperty("os.arch")
        return when {
            OperatingSystem.current().isMacOsX -> "x86_64"
            isWindows -> when (arch) {
                "x86_64", "amd64" -> "x86_64"
                else -> "x86"
            }
            else -> "x86_64"
        }
    }

/**
 * Returns exec extensions
 */
internal val exec: String
    get() {
        return when {
            OperatingSystem.current().isLinux -> "sh"
            isWindows -> "exe"
            else -> "sh"
        }
    }