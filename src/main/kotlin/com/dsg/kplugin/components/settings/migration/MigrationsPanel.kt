package com.dsg.kplugin.ui

import com.dsg.kplugin.common.constants.EMPTY_STRING
import com.dsg.kplugin.common.constants.Migration
import com.dsg.kplugin.common.constants.UI
import com.dsg.kplugin.model.enums.BumpType
import com.dsg.kplugin.service.migration.MigrationService
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
        versionsPanel.add(JLabel(UI.LAST_VERSION))
        versionsPanel.add(lastVersionField)
        versionsPanel.add(JLabel(UI.NEW_VERSION))
        versionsPanel.add(newVersionField)
        return versionsPanel
    }

    private fun createCreateButton(): JButton {
        return JButton(Migration.CREATE_MIGRATION_TITLE).apply {
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
        val newVersion = newVersionField.text

        migrationService.createMigrationFiles(project, selectedBump, EMPTY_STRING, EMPTY_STRING)
        lastVersionField.text = newVersion
        JOptionPane.showMessageDialog(this, Migration.MIGRATION_CREATED.format(newVersion))

        selectedBump = BumpType.PATCH
        patchButton.isSelected = true
        updateNewVersion(BumpType.PATCH)
    }
}
