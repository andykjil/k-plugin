package com.dsg.kplugin.components.settings.migration

import com.dsg.kplugin.common.constants.Migration
import com.dsg.kplugin.common.constants.MigrationMapping
import com.intellij.ui.JBColor
import java.awt.BorderLayout
import java.awt.FlowLayout
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTable
import javax.swing.event.TableModelEvent
import javax.swing.event.TableModelListener
import javax.swing.table.DefaultTableModel

class MigrationSettingsMappingComponent {

    private val settings = MigrationMappingSettings.getInstance()

    private val tableModel: DefaultTableModel = createTableModel()
    private val table: JTable = createTable(tableModel)
    private val panel: JPanel = JPanel(BorderLayout(5, 5)).apply {
        add(createScrollPane(table), BorderLayout.CENTER)
        add(createButtonsPanel(), BorderLayout.SOUTH)
    }

    private fun createTableModel(): DefaultTableModel {
        val data = if (settings.getMappings().isNotEmpty()) {
            settings.getMappings().map { arrayOf(it.first, it.second) }.toTypedArray()
        } else {
            MigrationMapping.DEFAULT_DATA
        }
        val model = object : DefaultTableModel(data, MigrationMapping.COLUMN_NAMES) {
            override fun isCellEditable(row: Int, column: Int): Boolean = true
        }
        model.addTableModelListener(object : TableModelListener {
            override fun tableChanged(e: TableModelEvent) {
                if (e.type == TableModelEvent.UPDATE || e.type == TableModelEvent.INSERT || e.type == TableModelEvent.DELETE) {
                    updateSettingsFromModel()
                }
            }
        })
        return model
    }

    private fun updateSettingsFromModel() {
        val mappings = mutableListOf<Pair<String, String>>()
        for (row in 0 until tableModel.rowCount) {
            val kotlinType = tableModel.getValueAt(row, 0)?.toString()?.trim() ?: ""
            val postgresType = tableModel.getValueAt(row, 1)?.toString()?.trim() ?: ""
            if (kotlinType.isNotEmpty() || postgresType.isNotEmpty()) {
                mappings.add(kotlinType to postgresType)
            }
        }
        settings.setMappings(mappings)
    }

    private fun createTable(tableModel: DefaultTableModel): JTable {
        val table = JTable(tableModel)
        val header = table.tableHeader
        header.reorderingAllowed = false
        header.resizingAllowed = false

        val headerRenderer = createHeaderRenderer()
        for (i in 0 until table.columnModel.columnCount) {
            table.columnModel.getColumn(i).headerRenderer = headerRenderer
        }

        table.border = BorderFactory.createLineBorder(JBColor.LIGHT_GRAY)
        table.rowHeight = MigrationMapping.TABLE_ROW_HEIGHT
        table.intercellSpacing = MigrationMapping.TABLE_INTERCELL_SPACING
        table.setShowHorizontalLines(true)
        table.setShowVerticalLines(true)
        table.gridColor = JBColor.LIGHT_GRAY

        val centerRenderer = createCenterRenderer()
        for (i in 0 until table.columnCount) {
            table.columnModel.getColumn(i).cellRenderer = centerRenderer
        }
        return table
    }

    private fun createHeaderRenderer(): javax.swing.table.DefaultTableCellRenderer {
        return javax.swing.table.DefaultTableCellRenderer().apply {
            horizontalAlignment = javax.swing.SwingConstants.CENTER
            border = BorderFactory.createMatteBorder(0, 0, 0, 0, JBColor.LIGHT_GRAY)
        }
    }

    private fun createCenterRenderer(): javax.swing.table.DefaultTableCellRenderer {
        return javax.swing.table.DefaultTableCellRenderer().apply {
            horizontalAlignment = javax.swing.SwingConstants.CENTER
        }
    }

    private fun createScrollPane(table: JTable): JScrollPane {
        return JScrollPane(table).apply {
            border = BorderFactory.createEmptyBorder(
                MigrationMapping.SCROLLPANE_BORDER, MigrationMapping.SCROLLPANE_BORDER,
                MigrationMapping.SCROLLPANE_BORDER, MigrationMapping.SCROLLPANE_BORDER,
            )
        }
    }

    private fun createButtonsPanel(): JPanel {
        val addButton = JButton(Migration.MAPPING_ADD_NEW).apply {
            addActionListener {
                tableModel.addRow(arrayOf("", ""))
            }
        }
        val removeButton = JButton(Migration.MAPPING_DELETE).apply {
            addActionListener {
                val selectedRow = table.selectedRow
                if (selectedRow != -1) {
                    tableModel.removeRow(selectedRow)
                }
            }
        }
        return JPanel(FlowLayout(FlowLayout.RIGHT, MigrationMapping.BUTTONS_HGAP, MigrationMapping.BUTTONS_VGAP)).apply {
            add(addButton)
            add(removeButton)
        }
    }

    fun getPanel(): JComponent = panel
}
