package com.squareStudio.glitter.action.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.Notifications
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.ui.MessageType
import com.siyeh.ig.portability.mediatype.ModelMediaType
import com.squareStudio.glitter.action.ZipUtil
import com.squareStudio.glitter.action.toolWindow.MyToolWindow
import java.io.File

object Initial {
    var project: Project? = null
    var filePath: String? = null
    //Create or replace your glitter project
    fun initial(path: String, appName: String, icon: File?,android:Boolean=true) {
        val notificationGroup = NotificationGroup("testid", NotificationDisplayType.BALLOON, false)
        val rout = "$filePath"
        if (!File(rout).exists()) {
            File(rout).mkdir()
        }
        val logFile = File(rout, "web.zip")
        if (ZipUtil.getRequest(
                path,
                1000 * 120,
                file = logFile
            )
        ) {
            ZipUtil.unzip(
                logFile,
                rout
            )
            val application = File("$rout/Application/glitterBundle/Application.html")
            application.writeText(application.readText().replace("App name", appName))
            if (icon != null && icon.exists()) {
                File("$rout/Application/glitterBundle/src/Square_Studio.png").writeBytes(icon.readBytes())
            }
            project!!.projectFile!!.fileSystem.refresh(true)
            val notification = notificationGroup.createNotification("Glitter loaded successfully", MessageType.INFO)
            Notifications.Bus.notify(notification)
        } else {
            val notification = notificationGroup.createNotification("Glitter loaded Error", MessageType.ERROR)
            Notifications.Bus.notify(notification)
        }
    }
    //Get the glitter version
    fun getVersion(): MutableMap<String, String> {
        val json = Http.getRequest("https://github.com/sam38124/GlitterBundle/raw/master/json.txt", 10 * 1000)
        val jsonMap: MutableMap<String, String> = Gson().fromJson<MutableMap<String, String>>(json!!, object :
            TypeToken<MutableMap<String, String>>() {}.type)
        return jsonMap
    }
}