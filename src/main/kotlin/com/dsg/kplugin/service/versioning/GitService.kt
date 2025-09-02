package com.dsg.kplugin.service.versioning

import USER_NAME_TEMPLATE
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
                GitConfigUtil.getValue(project, repoRoot, USER_NAME_TEMPLATE) ?: System.getProperty("user.name")
            } else {
                System.getProperty(USER_NAME_TEMPLATE)
            }
            ApplicationManager.getApplication().invokeLater {
                callback(name)
            }
        }
    }
}
