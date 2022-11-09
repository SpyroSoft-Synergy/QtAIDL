// Copyright (C) 2022 SpyroSoft Synergy S.A.
// SPDX-License-Identifier: MIT

package com.spyro_soft.qtaidl

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileCollection
import org.gradle.api.file.ProjectLayout
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.*
import org.gradle.process.ExecOperations
import javax.inject.Inject

abstract class GenerateAidlSourcesTask @Inject constructor(
    private val execOperations: ExecOperations,
    objects: ObjectFactory,
    projectLayout: ProjectLayout
) : DefaultTask() {

    private val qtAidlPluginExtension: QtAidlPluginExtension = project.qtaidlPlugin

    @SkipWhenEmpty
    @PathSensitive(PathSensitivity.RELATIVE)
    @IgnoreEmptyDirectories
    @InputFiles
    val sourceFiles = objects.fileCollection()

    fun addSourceFiles(files: FileCollection) {
        sourceFiles.from(files)
    }

    @Optional
    @OutputDirectory
    val outputDir: DirectoryProperty =
        objects.directoryProperty().convention(projectLayout.buildDirectory.dir("generated"))

    @TaskAction
    fun taskAction(): Unit = with(qtAidlPluginExtension) {
        outputDir.get().asFile.mkdirs()
        sourceFiles.asFileTree.forEach {
            val allArgs = if (isWindows)
                listOf(
                    "cmd",
                    "/c",
                    condaBinDir.get().asFile.resolve("activate.bat").absolutePath,
                    pythonEnvName.get(),
                    ">nul",
                    "2>&1",
                    "&&",
                    "qface",
                    "--rules",
                    "${qfaceTemplatesDir.get()}/aidl.yaml",
                    "--target",
                    outputDir.get(),
                    it.absoluteFile.path
                )
            else
                listOf(
                    "sh",
                    "-c",
                    ". ${condaActivatePath.get()}" +
                            "&& conda activate ${pythonEnvName.get()}" +
                            "&& qface --rules ${qfaceTemplatesDir.get()}/aidl.yaml --target ${outputDir.get()} ${it.absoluteFile.path}"
                )
            logger.lifecycle("Executing command: '${allArgs.joinToString(" ")}'")
            execOperations.exec {
                this.commandLine(allArgs)
            }
        }
    }
}