package com.dsg.kplugin.components.settings.migration

import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTable
import javax.swing.table.DefaultTableModel

class MigrationSettingsMappingComponent {

    private val panel: JPanel = JPanel().apply {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)

        val columnNames = arrayOf("Kotlin Type", "PostgreSQL Type")
        val data = arrayOf(
            arrayOf("UUID", "uuid"),
            arrayOf("String", "text"),
            arrayOf("Long", "numeric"),
            arrayOf("Double", "numeric"),
            arrayOf("Boolean", "boolean"),
            arrayOf("Int", "numeric"),
            arrayOf("LocalDate", "timestamp"),
            arrayOf("LocalDateTime", "timestamp"),
            arrayOf("BigDecimal", "numeric"),
            arrayOf("OffsetDateTime", "timestamp with time zone"),
        )

        val tableModel = object : DefaultTableModel(data, columnNames) {
            override fun isCellEditable(row: Int, column: Int): Boolean {
                return true
            }
        }

        val table = JTable(tableModel)
        val scrollPane = JScrollPane(table)
        add(scrollPane)

        val addButton = JButton("Add Mapping").apply {
            addActionListener {
                tableModel.addRow(arrayOf("", ""))
            }
        }
        val removeButton = JButton("Remove Mapping").apply {
            addActionListener {
                val selectedRow = table.selectedRow
                if (selectedRow != -1) {
                    tableModel.removeRow(selectedRow)
                }
            }
        }

        val buttonsPanel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            add(addButton)
            add(removeButton)
        }

        add(buttonsPanel)
    }

    fun getPanel(): JComponent = panel
}
