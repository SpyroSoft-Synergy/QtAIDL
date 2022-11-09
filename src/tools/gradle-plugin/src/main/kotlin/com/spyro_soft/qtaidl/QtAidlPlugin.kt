// Copyright (C) 2022 SpyroSoft Synergy S.A.
// SPDX-License-Identifier: MIT

package com.spyro_soft.qtaidl

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.register
import java.util.*

class QtAidlPlugin : Plugin<Project> {

    private lateinit var project: Project
    private var wasApplied = false
    private var PRE_REQ_PLUGINS = listOf("com.android.application", "com.android.library")

    override fun apply(project: Project) {
        project.extensions.create(QT_AIDL_PLUGIN_EXTENSION_NAME, QtAidlPluginExtension::class.java)
        this.project = project

        PRE_REQ_PLUGINS.forEach { preReqPlugin -> 
            project.pluginManager.withPlugin(preReqPlugin) {
                wasApplied = true
                doApply()
            }
        }

        project.afterEvaluate {
            if (!wasApplied) {
                throw GradleException(
                    "The com.spyro_soft.qtaidl plugin could not be applied during project evaluation.'\n" +
                            "                + ' The com.android.application or com.android.library plugin must be applied to the project first.'"
                )
            }
        }

    }

    private fun doApply() {
        val android = project.extensions.getByType(CommonExtension::class.java)
        
        if (!(android is ApplicationExtension || android is LibraryExtension)) {
            throw GradleException(
                    "The com.spyro_soft.qtaidl plugin could not be applied, can't find `android` in dsl."
                )
        }

        android.sourceSets.configureEach {
            val name = this.name
            val sds = project.objects.sourceDirectorySet(name, "$name QFace Files")
            (this as ExtensionAware).extensions.add("qface", sds)
            sds.srcDir("src/${name}/qface")
            sds.include("**/*.qface")
            this.aidl.srcDir(project.buildDir.path + "/generated")
        }

        val dataSetupTask = project.tasks.register<DataSetupTask>("extractData")
        val condaSetupTask = project.tasks.register<CondaSetupTask>("condaSetup") {
            dependsOn(dataSetupTask)
        }
        val envSetupTask = project.tasks.register<EnvSetupTask>("envSetup") {
            dependsOn(condaSetupTask)
        }

        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
        androidComponents.onVariants { variant ->
            println("Variant ${variant.name}")
            val capitalizedVariantName =
                variant.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            val compileTask = project.tasks.create(
                "compile${capitalizedVariantName}QtAidlSources",
                GenerateAidlSourcesTask::class.java
            ) {
                dependsOn(envSetupTask)
                android.sourceSets.forEach {
                    it.qface {
                        addSourceFiles(this)
                    }
                }
            }

            project.afterEvaluate {
                this.tasks.getByName("compile${capitalizedVariantName}Aidl").dependsOn(compileTask)
            }
        }
    }
}
