package com.squareStudio.glitter.action

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.intellij.compiler.ModuleSourceSet.getModules
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.vfs.VirtualFile
import java.io.File
import com.intellij.psi.PsiDirectory
import com.intellij.psi.util.ClassUtil
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.util.xml.ModuleContextProvider.getModules
import com.intellij.webcore.ModuleHelper.getModules
import com.sun.jna.platform.win32.Kernel32Util.getModules
import com.intellij.psi.PsiPackage

import com.intellij.ide.projectView.impl.nodes.ProjectViewDirectoryHelper

import com.intellij.psi.JavaDirectoryService

import com.intellij.psi.PsiManager

import com.intellij.util.containers.ContainerUtil

import com.intellij.openapi.roots.ProjectRootManager

import com.intellij.ide.projectView.ProjectViewSettings
import com.intellij.ide.projectView.impl.nodes.PackageUtil
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.testFramework.propertyBased.PsiIndexConsistencyTester
import com.intellij.util.indexing.diagnostic.dump.paths.PortableFilePath
import com.squareStudio.glitter.action.android.AndroidFile
import com.squareStudio.glitter.action.util.Http
import java.util.*


fun main() {

//    map["Official_1.0"]="https://github.com/sam38124/GlitterBundle/raw/master/Official%20Verision/1.0.zip"
    val packageName = "com.orange.first"
    val appName = "MyAppName"
    var root = ""
    val file = File("AndroidProject/app/src/main/AndroidManifest.xml")
    file.writeText(
        AndroidFile.mainfeist.replace(
            "packageName",
            packageName
        )
    )
    val rout = File("AndroidProject/app/src/main/java/${packageName.replace(".", "/")}")
    if (!rout.exists()) {
        rout.mkdirs()
    }
    File("AndroidProject/app/src/main/java/${packageName.replace(".", "/")}/MyApp.kt").writeText(
        AndroidFile.myApp.replace(
            "packageName",
            packageName
        )
    )
    File("AndroidProject/app/build.gradle").writeText(AndroidFile.gradle.replace("com.jianzhi.glitter", packageName))
    File("AndroidProject/app/src/main/res/values/strings.xml").writeText(
        """<resources>
    <string name="app_name">${appName}</string>
    </resources>"""
    )
//    println(file.readText().replace("com.jianzhi.glitter",packageName))
//    var json=Http.getRequest("https://github.com/sam38124/GlitterBundle/raw/master/json.txt",10*1000)
//    val jsonMap:MutableMap<String,String> = Gson().fromJson<MutableMap<String,String>>(json!!,object :TypeToken<MutableMap<String,String>>(){}.type)
//    println(jsonMap["Official_1.4"])
//    ResourceBundle.getBundle()
//    PsiIndexConsistencyTester.Action.ReloadFromDisk
//    val rout="app/src/main/assets"
//    if(!File(rout).exists()){
//        File(rout).mkdir()
//    }
//    Thread {
//        val logFile = File(rout, "web.zip")
//        if (ZipUtil.getRequest("https://github.com/sam38124/Glitter_Android/raw/master/glitterBundle.zip", 1000 * 120, file = logFile)) {
//            ZipUtil.unzip(
//                logFile,
//                rout
//            )
//            print("success")
//        }
//    }.start()
//"app/src/AndroidManifest.xml"
//    println(PortableFilePath.ProjectRoot)
}