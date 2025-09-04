package com.dsg.kplugin.components

import com.dsg.kplugin.common.USER_CUSTOM_CHECKBOX_TEXT
import com.dsg.kplugin.common.USER_NAME_TEXT
import com.dsg.kplugin.service.versioning.GitService
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBTextField
import java.awt.Dimension
import java.awt.FlowLayout
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JLabel
import javax.swing.JPanel

class MigrationSettingsComponent(
    project: Project,
    initialUser: String,
) {
    companion object {
        private val USER_FIELD_DIMENSION = Dimension(150, 30)
        private const val VERTICAL_STRUT = 2
    }

    private val userTextField = JBTextField(initialUser).apply {
        isEditable = false
        isEnabled = false
        preferredSize = USER_FIELD_DIMENSION
        minimumSize = USER_FIELD_DIMENSION
        maximumSize = USER_FIELD_DIMENSION
    }
    private val useCustomCheckBox = JBCheckBox(USER_CUSTOM_CHECKBOX_TEXT, false)
    private var gitUserName: String = initialUser

    private val usernameRow: JPanel
        get() = createUsernameRow()

    private val checkBoxPanel: JPanel
        get() = createCheckBoxPanel()

    private val panel: JPanel = JPanel().apply {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        add(usernameRow)
        add(Box.createVerticalStrut(VERTICAL_STRUT))
        add(checkBoxPanel)
    }

    init {
        useCustomCheckBox.addActionListener {
            val useCustom = useCustomCheckBox.isSelected
            userTextField.isEditable = useCustom
            userTextField.isEnabled = useCustom
            if (!useCustom) {
                userTextField.text = gitUserName
            }
        }
        loadGitUserName(project)
    }

    private fun createUsernameRow(): JPanel =
        JPanel(FlowLayout(FlowLayout.LEFT, 0, 0)).apply {
            add(JLabel(USER_NAME_TEXT))
            add(userTextField)
            maximumSize = Dimension(Int.MAX_VALUE, USER_FIELD_DIMENSION.height)
        }

    private fun createCheckBoxPanel(): JPanel =
        JPanel(FlowLayout(FlowLayout.LEFT, 0, 0)).apply {
            add(useCustomCheckBox)
            maximumSize = Dimension(Int.MAX_VALUE, USER_FIELD_DIMENSION.height)
        }

    private fun loadGitUserName(project: Project) {
        GitService().getGitUserName(project) { gitUser ->
            gitUserName = gitUser
            if (!useCustomCheckBox.isSelected) {
                ApplicationManager.getApplication().invokeLater {
                    userTextField.text = gitUserName
                }
            }
        }
    }

    fun getPanel(): JPanel = panel
    fun getUserName(): String = userTextField.text.trim()
    fun isUseCustomUser(): Boolean = useCustomCheckBox.isSelected
    fun setUserName(name: String) {
        userTextField.text = name
    }
    fun setCustomUserEnabled(useCustom: Boolean) {
        useCustomCheckBox.isSelected = useCustom
        userTextField.isEditable = useCustom
        userTextField.isEnabled = useCustom
    }
}
