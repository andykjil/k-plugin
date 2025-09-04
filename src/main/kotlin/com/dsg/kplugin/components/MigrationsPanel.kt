package com.dsg.kplugin.ui

import com.dsg.kplugin.common.CREATE_MIGRATION_TITLE
import com.dsg.kplugin.common.LAST_VERSION
import com.dsg.kplugin.common.MIGRATION_CREATED
import com.dsg.kplugin.common.NEW_VERSION
import com.dsg.kplugin.model.enums.BumpType
import com.dsg.kplugin.service.MigrationService
import com.dsg.kplugin.service.versioning.VersionService
import com.intellij.openapi.project.Project
import java.awt.BorderLayout
import java.awt.GridLayout
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.ButtonGroup
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JRadioButton
import javax.swing.JTextField

class MigrationsPanel(
    private val project: Project,
    private val migrationService: MigrationService,
    private val versionService: VersionService,
) : JPanel(BorderLayout()) {

    private val lastVersionField = JTextField().apply {
        isEditable = false
    }

    private val newVersionField = JTextField().apply {
        isEditable = false
    }

    private var selectedBump: BumpType = BumpType.PATCH

    private val majorButton = JRadioButton(BumpType.MAJOR.text)
    private val minorButton = JRadioButton(BumpType.MINOR.text)
    private val patchButton = JRadioButton(BumpType.PATCH.text, true)
    private val buttonGroup = ButtonGroup()

    init {
        initializeButtonGroup()
        addListeners()

        val mainPanel = JPanel()
        mainPanel.layout = BoxLayout(mainPanel, BoxLayout.Y_AXIS)
        mainPanel.add(createBumpPanel())
        mainPanel.add(Box.createVerticalStrut(10))
        mainPanel.add(createVersionsPanel())
        mainPanel.add(Box.createVerticalStrut(10))
        mainPanel.add(createCreateButton())

        add(mainPanel, BorderLayout.NORTH)

        loadLastVersion()
    }

    private fun initializeButtonGroup() {
        buttonGroup.add(majorButton)
        buttonGroup.add(minorButton)
        buttonGroup.add(patchButton)
    }

    private fun addListeners() {
        majorButton.addActionListener { updateNewVersion(BumpType.MAJOR) }
        minorButton.addActionListener { updateNewVersion(BumpType.MINOR) }
        patchButton.addActionListener { updateNewVersion(BumpType.PATCH) }
    }

    private fun createBumpPanel(): JPanel {
        val bumpPanel = JPanel(GridLayout(1, 3))
        bumpPanel.add(majorButton)
        bumpPanel.add(minorButton)
        bumpPanel.add(patchButton)
        return bumpPanel
    }

    private fun createVersionsPanel(): JPanel {
        val versionsPanel = JPanel(GridLayout(2, 2, 5, 5))
        versionsPanel.add(JLabel(LAST_VERSION))
        versionsPanel.add(lastVersionField)
        versionsPanel.add(JLabel(NEW_VERSION))
        versionsPanel.add(newVersionField)
        return versionsPanel
    }

    private fun createCreateButton(): JButton {
        return JButton(CREATE_MIGRATION_TITLE).apply {
            addActionListener { onCreateMigration() }
        }
    }

    fun loadLastVersion() {
        val changelogDir = migrationService.findChangelogDir(project) ?: return
        val lastVersion = versionService.findLastChangelog(changelogDir)
        lastVersionField.text = lastVersion
        updateNewVersion(selectedBump)
    }

    fun updateNewVersion(bumpType: BumpType) {
        selectedBump = bumpType
        val lastVersion = lastVersionField.text
        val newVersion = versionService.bumpVersion(lastVersion, bumpType)
        newVersionField.text = newVersion
    }

    private fun onCreateMigration() {
        val changelogDir = migrationService.findChangelogDir(project) ?: return
        val newVersion = newVersionField.text

        migrationService.createMigrationFiles(project, changelogDir, newVersion)
        lastVersionField.text = newVersion
        JOptionPane.showMessageDialog(this, MIGRATION_CREATED.format(newVersion))

        selectedBump = BumpType.PATCH
        patchButton.isSelected = true
        updateNewVersion(BumpType.PATCH)
    }
}
