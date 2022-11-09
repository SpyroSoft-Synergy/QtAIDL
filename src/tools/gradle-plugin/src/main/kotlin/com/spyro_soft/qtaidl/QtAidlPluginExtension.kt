// Copyright (C) 2022 SpyroSoft Synergy S.A.
// SPDX-License-Identifier: MIT

package com.spyro_soft.qtaidl


import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.internal.file.FileFactory
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.ProviderFactory
import org.gradle.internal.os.OperatingSystem
import org.gradle.kotlin.dsl.property
import javax.inject.Inject

abstract class QtAidlPluginExtension @Inject constructor(
    providerFactory: ProviderFactory,
    project: Project,
    objects: ObjectFactory,
    fileFactory: FileFactory
) {
    val pluginDataDir: DirectoryProperty = objects.directoryProperty().convention(
        providerFactory.provider {
            fileFactory.dir(project.rootDir).dir(GRADLE_FILES_DIR).dir(QTAIDL_MAIN_DIR)
        }
    )

    val condaRepoUrl: Property<String> =
        objects.property<String>().convention(DEFAULT_CONDA_REPO_URL)

    val qfaceTemplatesDir: DirectoryProperty = objects.directoryProperty().convention(
        providerFactory.provider { pluginDataDir.get().dir(QTAIDL_QFACE_TEMPLATES_DIR) }
    )

    val systemArch: Property<String> = objects.property<String>().convention(arch)

    val condaInstaller: Property<String> =
        objects.property<String>().convention(DEFAULT_CONDA_INSTALLER)
    val condaVersion: Property<String> =
        objects.property<String>().convention(DEFAULT_CONDA_VERSION)

    internal val pythonEnvName: Property<String> = objects.property<String>().convention(
        providerFactory.provider { "qtaidl_env" }
    )

    internal val pythonEnvDir: DirectoryProperty = objects.directoryProperty().convention(
        providerFactory.provider { condaDir.get().dir("envs").dir(pythonEnvName.get()) }
    )

    internal val condaDir: DirectoryProperty = objects.directoryProperty().convention(
        providerFactory.provider {
            pluginDataDir.get()
                .dir(os)
                .dir("${condaInstaller.get()}-${condaVersion.get()}")
        }
    )

    internal val condaBinDir: DirectoryProperty = objects.directoryProperty().convention(
        providerFactory.provider { condaDir.get().dir("condabin") }
    )

    internal val condaExec: RegularFileProperty = objects.fileProperty().convention(
        providerFactory.provider {
            if (OperatingSystem.current().isWindows)
                this.condaBinDir.get().file("conda.bat")
            else
                this.condaBinDir.get().file("conda")
        }
    )

    internal val condaActivatePath: RegularFileProperty = objects.fileProperty().convention(
        providerFactory.provider {
            if (OperatingSystem.current().isWindows)
                condaBinDir.get().file("activate.bat")
            else
                condaDir.get().dir("bin").file("activate")
        }
    )
}
