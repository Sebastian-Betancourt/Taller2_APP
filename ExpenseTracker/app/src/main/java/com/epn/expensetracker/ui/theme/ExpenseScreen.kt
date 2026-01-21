@file:OptIn(ExperimentalMaterial3Api::class)
package com.epn.expensetracker.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.epn.expensetracker.data.local.ExpenseEntity
import java.text.SimpleDateFormat
import java.util.*

/* ===================== PANTALLA PRINCIPAL ===================== */

@Composable
fun ExpenseScreen(
    viewModel: ExpenseViewModel,
    onRecordatorioChange: (Boolean, Int, Int) -> Unit
) {
    val monto by viewModel.monto.collectAsState()
    val descripcion by viewModel.descripcion.collectAsState()
    val categoriaSeleccionada by viewModel.categoriaSeleccionada.collectAsState()
    val gastos by viewModel.gastos.collectAsState(initial = emptyList())
    val total by viewModel.total.collectAsState(initial = 0.0)

    val recordatorioActivo by viewModel.recordatorioActivo.collectAsState()
    val horaRecordatorio by viewModel.horaRecordatorio.collectAsState()
    val minutoRecordatorio by viewModel.minutoRecordatorio.collectAsState()

    var gastoEnEdicion by remember { mutableStateOf<ExpenseEntity?>(null) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mis Gastos") }) }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            FormularioGasto(
                monto = monto,
                descripcion = descripcion,
                categoria = categoriaSeleccionada,
                categorias = viewModel.categorias,
                onMontoChange = viewModel::actualizarMonto,
                onDescripcionChange = viewModel::actualizarDescripcion,
                onCategoriaChange = viewModel::seleccionarCategoria,
                onGuardar = viewModel::guardarGasto
            )

            Spacer(Modifier.height(16.dp))

            ConfiguracionRecordatorio(
                activo = recordatorioActivo,
                hora = horaRecordatorio,
                minuto = minutoRecordatorio,
                onActivoChange = {
                    viewModel.cambiarEstadoRecordatorio(it)
                    onRecordatorioChange(it, horaRecordatorio, minutoRecordatorio)
                },
                onHoraChange = { h, m ->
                    viewModel.actualizarHoraRecordatorio(h, m)
                    if (recordatorioActivo) {
                        onRecordatorioChange(true, h, m)
                    }
                }
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Total: $${String.format("%.2f", total ?: 0.0)}",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(Modifier.height(16.dp))

            gastos.forEach { gasto ->
                GastoItem(
                    gasto = gasto,
                    onEditar = { gastoEnEdicion = it },
                    onEliminar = { viewModel.eliminarGasto(gasto) }
                )
            }
        }
    }

    gastoEnEdicion?.let {
        EditExpenseDialog(
            gasto = it,
            categorias = viewModel.categorias,
            onDismiss = { gastoEnEdicion = null },
            onSave = { actualizado ->
                viewModel.actualizarGasto(actualizado)
                gastoEnEdicion = null
            }
        )
    }
}

/* ===================== FORMULARIO ===================== */

@Composable
fun FormularioGasto(
    monto: String,
    descripcion: String,
    categoria: String,
    categorias: List<String>,
    onMontoChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onCategoriaChange: (String) -> Unit,
    onGuardar: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card {
        Column(Modifier.padding(16.dp)) {

            OutlinedTextField(
                value = monto,
                onValueChange = onMontoChange,
                label = { Text("Monto") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = onDescripcionChange,
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                OutlinedTextField(
                    value = categoria,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría") },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categorias.forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                onCategoriaChange(it)
                                expanded = false
                            }
                        )
                    }
                }
            }

            Button(
                onClick = onGuardar,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar")
            }
        }
    }
}

/* ===================== RECORDATORIO ===================== */

@Composable
fun ConfiguracionRecordatorio(
    activo: Boolean,
    hora: Int,
    minuto: Int,
    onActivoChange: (Boolean) -> Unit,
    onHoraChange: (Int, Int) -> Unit
) {
    var mostrarPicker by remember { mutableStateOf(false) }

    Card {
        Column(Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Recordatorio")
                Switch(checked = activo, onCheckedChange = onActivoChange)
            }

            if (activo) {
                Text(
                    text = String.format("%02d:%02d", hora, minuto),
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .clickable { mostrarPicker = true },
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }

    if (mostrarPicker) {
        TimePickerDialog(
            hora = hora,
            minuto = minuto,
            onConfirm = { h, m ->
                onHoraChange(h, m)
                mostrarPicker = false
            },
            onDismiss = { mostrarPicker = false }
        )
    }
}

/* ===================== TIME PICKER ===================== */

@Composable
fun TimePickerDialog(
    hora: Int,
    minuto: Int,
    onConfirm: (Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    val state = rememberTimePickerState(hora, minuto, true)

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onConfirm(state.hour, state.minute)
            }) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        text = { TimePicker(state = state) }
    )
}

/* ===================== ITEM ===================== */

@Composable
fun GastoItem(
    gasto: ExpenseEntity,
    onEditar: (ExpenseEntity) -> Unit,
    onEliminar: () -> Unit
) {
    val fecha = remember(gasto.fecha) {
        SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            .format(Date(gasto.fecha))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(gasto.descripcion, style = MaterialTheme.typography.bodyLarge)
                Text("${gasto.categoria} • $fecha", style = MaterialTheme.typography.bodySmall)
            }

            Text(
                "$${String.format("%.2f", gasto.monto)}",
                color = MaterialTheme.colorScheme.primary
            )

            IconButton(onClick = { onEditar(gasto) }) {
                Icon(Icons.Default.Edit, contentDescription = "Editar")
            }

            IconButton(onClick = onEliminar) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

/* ===================== DIÁLOGO EDITAR ===================== */

@Composable
fun EditExpenseDialog(
    gasto: ExpenseEntity,
    categorias: List<String>,
    onDismiss: () -> Unit,
    onSave: (ExpenseEntity) -> Unit
) {
    var montoText by remember { mutableStateOf(gasto.monto.toString()) }
    var descripcionText by remember { mutableStateOf(gasto.descripcion) }
    var categoriaText by remember { mutableStateOf(gasto.categoria) }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar gasto") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                OutlinedTextField(
                    value = montoText,
                    onValueChange = { if (it.matches(Regex("^\\d*\\.?\\d*$"))) montoText = it },
                    label = { Text("Monto") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = descripcionText,
                    onValueChange = { descripcionText = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedTextField(
                        value = categoriaText,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Categoría") },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        categorias.forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = {
                                    categoriaText = it
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onSave(
                    gasto.copy(
                        monto = montoText.toDoubleOrNull() ?: gasto.monto,
                        descripcion = descripcionText.trim(),
                        categoria = categoriaText
                    )
                )
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
