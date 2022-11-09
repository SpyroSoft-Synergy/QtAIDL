// Copyright (C) 2022 SpyroSoft Synergy S.A.
// SPDX-License-Identifier: MIT

package com.spyro_soft.qtaidl

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.util.jar.JarFile


abstract class DataSetupTask : DefaultTask() {

    private val qtAidlPluginExtension: QtAidlPluginExtension = project.qtaidlPlugin

    init {
        group = "qtaidl"
        description = "Setup generator files"
        this.onlyIf {
            !qtAidlPluginExtension.pluginDataDir.get().asFile.exists()
        }
    }

    @TaskAction
    fun setup(): Unit = with(qtAidlPluginExtension) {
        val pluginDataDirFile = pluginDataDir.get().asFile
        pluginDataDirFile.mkdirs()
        val dataUrl = object {}.javaClass.getResource("/data")
        logger.lifecycle("Installing ${dataUrl}...")
        if (dataUrl != null && dataUrl.protocol.equals("jar")) {
            val path = dataUrl.path
            val jarPath = path.substring(5, path.indexOf('!'))
            val jarFile = JarFile(URLDecoder.decode(jarPath, StandardCharsets.UTF_8.name()))
            val entries = jarFile.entries()
            for (entry in entries) {
                val entryName = entry.name
                if (entryName.startsWith("data") && !entryName.endsWith('/')) {
                    val targetFileName = entryName.substring(entryName.indexOf("/") + 1)
                    val inputFileName = "/$entryName"
                    val envFile = pluginDataDir.get().file(targetFileName).asFile
                    envFile.parentFile.mkdirs()
                    logger.lifecycle("Installing $envFile ${inputFileName}...")
                    this.javaClass.getResource(inputFileName)
                        ?.let { envFile.writeText(it.readText()) }
                }
            }
        }
    }
}