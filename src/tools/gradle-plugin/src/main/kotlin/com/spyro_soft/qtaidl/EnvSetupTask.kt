// Copyright (C) 2022 SpyroSoft Synergy S.A.
// SPDX-License-Identifier: MIT

package com.spyro_soft.qtaidl

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import org.gradle.process.ExecResult
import javax.inject.Inject

abstract class EnvSetupTask @Inject constructor(
    private val execOperations: ExecOperations,
) : DefaultTask() {

    private val qtAidlPluginExtension: QtAidlPluginExtension = project.qtaidlPlugin

    init {
        group = "qtaidl"
        description = "Setup python environment"
        this.onlyIf {
            !qtAidlPluginExtension.pythonEnvDir.get().asFile.exists()
        }
    }

    @TaskAction
    fun setup(): ExecResult = with(qtAidlPluginExtension) {
        execOperations.exec {
            this.workingDir = pluginDataDir.get().asFile
            this.environment("PYTHONNOUSERSITE", "1")
            this.executable = condaExec.get().asFile.canonicalPath
            this.args(listOf("env", "create", "-f", "environment.yaml"))
        }
    }
}