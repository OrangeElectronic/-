package com.squareStudio.glitter.action.android

class AndroidFile {
    companion object{
        var mainfeist="""<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    package="packageName">
    <application
        android:networkSecurityConfig="@xml/network_security_config"
        android:allowBackup="true"
        android:name=".MyApp"
        android:icon="@mipmap/icon"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/icon"
        android:supportsRtl="true"
        android:largeHeap="true"
        android:theme="@style/Theme.AppCompat.NoActionBar"
        tools:ignore="UnusedAttribute">
        <activity android:name="com.jianzhi.glitter.GlitterActivity"
            android:configChanges="orientation|keyboardHidden|screenSize">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>
</manifest>"""

        val myApp="""package packageName;
import android.app.Application
import com.jianzhi.glitter.GlitterActivity
import com.jianzhi.glitter.JsInterFace

class MyApp :Application(){
    override fun onCreate() {
        super.onCreate()
        GlitterActivity.setUp("file:///android_asset/appData",appName = "appData")
    }
}"""

        val gradle="""apply plugin: 'com.android.application'
apply plugin: 'kotlin-android'
apply plugin: 'kotlin-android-extensions'

android {
    compileSdkVersion 29
    buildToolsVersion "29.0.3"

    defaultConfig {
        applicationId "com.jianzhi.glitter"
        minSdkVersion 21
        targetSdkVersion 29
        versionCode 1
        versionName "1.0"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }


    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation fileTree(dir: "libs", include: ["*.jar"])
    implementation "org.jetbrains.kotlin:kotlin-stdlib:${'$'}kotlin_version"
    implementation 'androidx.core:core-ktx:1.3.2'
    implementation 'androidx.appcompat:appcompat:1.2.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.0.4'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'androidx.test.ext:junit:1.1.2'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.3.0'
    implementation 'com.github.sam38124:Glitter_Android:2.1.8'
}"""
    }
}