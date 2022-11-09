// Copyright (C) 2022 SpyroSoft Synergy S.A.
// SPDX-License-Identifier: MIT

package com.spyro_soft.qtaidl

import org.apache.commons.io.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import org.gradle.process.ExecResult
import java.io.File
import java.net.URL
import javax.inject.Inject

abstract class CondaSetupTask @Inject constructor(
    private val execOperations: ExecOperations,
) : DefaultTask() {

    private val qtAidlPluginExtension: QtAidlPluginExtension = project.qtaidlPlugin

    init {
        group = "qtaidl"
        description = "Install conda binaries"
        this.onlyIf {
            !qtAidlPluginExtension.condaBinDir.get().asFile.exists()
        }
    }

    @TaskAction
    fun setup(): ExecResult = with(qtAidlPluginExtension) {
        val condaDirFile = condaDir.get().asFile
        condaDirFile.mkdirs()
        val condaInstaller =
            condaDirFile.resolve("${condaInstaller.get()}-${condaVersion.get()}-$os-${systemArch.get()}.$exec")
        downloadConda(condaInstaller)
        allowInstallerExecution(condaInstaller)
        logger.lifecycle("Installing ${this.condaInstaller.get()}...")
        execOperations.exec {
            this.environment("PYTHONNOUSERSITE", "1")
            this.executable = condaInstaller.canonicalPath
            val execArgs = if (isWindows)
                listOf(
                    "/InstallationType=JustMe",
                    "/RegisterPython=0",
                    "/AddToPath=0",
                    "/S",
                    "/D=${condaDirFile.canonicalPath}"
                )
            else
                listOf("-b", "-u", "-p", condaDirFile.canonicalPath)
            this.args(execArgs)
        }
    }

    private fun allowInstallerExecution(condaInstaller: File) {
        if (!isWindows) {
            logger.lifecycle("Allowing user to run installer...")
            execOperations.exec {
                this.executable = "chmod"
                this.args("u+x", condaInstaller.canonicalPath)
            }
        }
    }

    private fun downloadConda(condaFile: File) {
        val condaRepoUrl = qtAidlPluginExtension.condaRepoUrl.get().dropLastWhile { it == '/' }
        val condaInstaller = qtAidlPluginExtension.condaInstaller.get()
        logger.lifecycle("Downloading $condaInstaller to: ${condaFile.canonicalPath} from: $condaRepoUrl (please wait, it can take a while)")
        val connection = URL("${condaRepoUrl}/${condaFile.name}").openConnection()
        val condaInputStream = connection.getInputStream()
        FileUtils.copyInputStreamToFile(condaInputStream, condaFile)
    }
}