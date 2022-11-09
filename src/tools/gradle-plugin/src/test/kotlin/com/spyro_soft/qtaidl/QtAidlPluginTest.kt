// Copyright (C) 2022 SpyroSoft Synergy S.A.
// SPDX-License-Identifier: MIT

package com.spyro_soft.qtaidl

import org.assertj.core.api.Assertions.assertThat
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Test

internal class QtAidlPluginTest {

    @Test
    fun `check if plugin is loaded`() {
        val project: Project = ProjectBuilder.builder().build()
        project.apply(mapOf("plugin" to "com.spyro_soft.qtaidl"))

        assertThat(project.plugins).hasSize(1)
    }
}