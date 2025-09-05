package com.dsg.kplugin.service.versioning

import com.dsg.kplugin.common.constants.Migration
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import git4idea.GitUtil
import git4idea.branch.GitBranchUtil
import git4idea.config.GitConfigUtil
import git4idea.repo.GitRepositoryManager

class GitService {

    fun getCurrentBranch(project: Project): String? {
        val repoManager = GitRepositoryManager.getInstance(project)
        val repo = repoManager.repositories.firstOrNull() ?: return null
        return GitBranchUtil.getBranchNameOrRev(repo)
    }

    fun getGitUserName(project: Project, callback: (String) -> Unit) {
        ApplicationManager.getApplication().executeOnPooledThread {
            val repoRoot = GitUtil.getRepositoryManager(project).repositories.firstOrNull()?.root
            val name = if (repoRoot != null) {
                GitConfigUtil.getValue(project, repoRoot, Migration.USER_NAME_TEMPLATE) ?: System.getProperty(Migration.USER_NAME_TEMPLATE)
            } else {
                System.getProperty(Migration.USER_NAME_TEMPLATE)
            }
            ApplicationManager.getApplication().invokeLater {
                callback(name)
            }
        }
    }

    fun getDefaultUserNameSync(project: Project): String {
        val repoRoot = GitRepositoryManager.getInstance(project).repositories.firstOrNull()?.root
        return if (repoRoot != null) {
            GitConfigUtil.getValue(project, repoRoot, Migration.USER_NAME_TEMPLATE) ?: System.getProperty(Migration.USER_NAME_TEMPLATE)
        } else {
            System.getProperty(Migration.USER_NAME_TEMPLATE)
        }
    }
}
