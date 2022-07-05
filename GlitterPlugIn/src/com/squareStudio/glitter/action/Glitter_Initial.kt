package com.squareStudio.glitter.action

import com.intellij.codeInsight.hints.settings.Diff
import com.intellij.compiler.CompilerConfiguration
import com.intellij.compiler.actions.BuildArtifactAction
import com.intellij.compiler.actions.CompileAction
import com.intellij.debugger.DebuggerManagerEx
import com.intellij.debugger.actions.ReloadFileAction
import com.intellij.debugger.ui.HotSwapUI
import com.intellij.internal.InspectStubIndexAction.actionPerformed
import com.intellij.jarRepository.settings.reloadAllRepositoryLibraries
import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.compiler.CompilerManager
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.ui.MessageType
import com.intellij.openapi.util.IconLoader.activate
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.openapi.wm.impl.X11UiUtil.activate
import com.intellij.psi.JavaDirectoryService
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.testFramework.configurationStore.copyFilesAndReloadProject
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import com.intellij.testFramework.propertyBased.PsiIndexConsistencyTester
import com.intellij.ui.IconManager.activate
import com.intellij.util.ResourceUtil.getResourceAsStream
import com.intellij.util.indexing.diagnostic.dump.paths.PortableFilePath
import com.intellij.util.ui.JBUI
import com.squareStudio.glitter.action.toolWindow.MyToolWindowFactory
import com.squareStudio.glitter.action.util.Initial
import java.io.File
import java.util.ArrayList

class Glitter_Initial : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val toolWindow=ToolWindowManager.getInstance(e.project!!).getToolWindow("Sample Calendar")!!
        toolWindow.activate(null);
        toolWindow.show()
    }

    protected fun getCompilableFiles(project: Project?, files: Array<VirtualFile>?): Array<VirtualFile?>? {
        return if (files != null && files.size != 0) {
            val psiManager = PsiManager.getInstance(project!!)
            val compilerConfiguration = CompilerConfiguration.getInstance(
                project
            )
            val fileIndex = ProjectRootManager.getInstance(
                project
            ).fileIndex
            val compilerManager = CompilerManager.getInstance(
                project
            )
            val filesToCompile: MutableList<VirtualFile> = ArrayList<VirtualFile>()
            val var7: Array<VirtualFile> = files
            val var8 = files.size
            for (var9 in 0 until var8) {
                val file = var7[var9]
                if (fileIndex.isInSourceContent(file) && file.isInLocalFileSystem) {
                    if (file.isDirectory) {
                        val directory = psiManager.findDirectory(file)
                        if (directory == null || JavaDirectoryService.getInstance().getPackage(directory) == null) {
                            continue
                        }
                    } else {
                        val fileType = file.fileType
                        if (!compilerManager.isCompilableFileType(fileType) && !compilerConfiguration.isCompilableResourceFile(
                                project,
                                file
                            )
                        ) {
                            continue
                        }
                    }
                    filesToCompile.add(file)
                }
            }
            VfsUtilCore.toVirtualFileArray(filesToCompile)
        } else {
            VirtualFile.EMPTY_ARRAY
        }
    }
}