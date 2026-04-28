package com.example.bleconnect.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bleconnect.ui.preview.DarkModePreview
import com.example.bleconnect.ui.preview.LightModePreview
import com.example.bleconnect.ui.theme.BleConnectTheme
import com.example.shared.BleConnectionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BleConnectScreen(
    viewModel: BleConnectViewModel,
    onConnectClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.message) {
        uiState.message?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearMessage()
        }
    }

    BleConnectTheme {
        Surface {
            BleConnectContent(
                uiState = uiState,
                onDeviceNameChanged = viewModel::onDeviceNameChanged,
                onConnectClick = onConnectClick,
                onDisconnectClick = viewModel::disconnect,
                onRelay1Click = viewModel::onRelay1Clicked,
                onRelay2Click = viewModel::onRelay2Clicked,
                snackbarHostState = snackbarHostState
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BleConnectContent(
    uiState: BleConnectUiState,
    onDeviceNameChanged: (String) -> Unit,
    onConnectClick: () -> Unit,
    onDisconnectClick: () -> Unit,
    onRelay1Click: () -> Unit,
    onRelay2Click: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("BLE Connect") }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                DeviceSection(
                    deviceName = uiState.deviceName,
                    connectionState = uiState.connectionState,
                    isLoading = uiState.isLoading,
                    onDeviceNameChanged = onDeviceNameChanged,
                    onConnectClick = onConnectClick,
                    onDisconnectClick = onDisconnectClick
                )

                RelaySection(
                    title = "Relé 1",
                    checked = uiState.isRelay1On,
                    enabled = uiState.connectionState is BleConnectionState.Connected,
                    onToggleClick = onRelay1Click
                )

                RelaySection(
                    title = "Relé 2",
                    checked = uiState.isRelay2On,
                    enabled = uiState.connectionState is BleConnectionState.Connected,
                    onToggleClick = onRelay2Click
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun DeviceSection(
    deviceName: String,
    connectionState: BleConnectionState,
    isLoading: Boolean,
    onDeviceNameChanged: (String) -> Unit,
    onConnectClick: () -> Unit,
    onDisconnectClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Conexão BLE",
                style = MaterialTheme.typography.titleMedium
            )

            OutlinedTextField(
                value = deviceName,
                onValueChange = onDeviceNameChanged,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nome do dispositivo") },
                singleLine = true
            )

            Text(
                text = "Status: ${connectionState.toReadableText()}",
                style = MaterialTheme.typography.bodyLarge
            )

            if (isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Button(
                onClick = onConnectClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                Text("Conectar")
            }

            OutlinedButton(
                onClick = onDisconnectClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Desconectar")
            }
        }
    }
}

@Composable
private fun RelaySection(
    title: String,
    checked: Boolean,
    enabled: Boolean,
    onToggleClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        ListItem(
            headlineContent = {
                Text(title)
            },
            supportingContent = {
                Text(
                    if (checked) "Ligado" else "Desligado"
                )
            },
            trailingContent = {
                Switch(
                    checked = checked,
                    onCheckedChange = { onToggleClick() },
                    enabled = enabled
                )
            }
        )

        TextButton(
            onClick = onToggleClick,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = if (checked) "Desligar" else "Ligar"
            )
        }
    }
}

private fun BleConnectionState.toReadableText(): String {
    return when (this) {
        is BleConnectionState.Idle -> "Aguardando"
        is BleConnectionState.Scanning -> "Procurando dispositivo"
        is BleConnectionState.Connecting -> "Conectando"
        is BleConnectionState.Connected -> "Conectado em ${this.deviceName}"
        is BleConnectionState.Disconnected -> "Desconectado"
        is BleConnectionState.Error -> "Erro: ${this.message}"
    }
}

@LightModePreview
@DarkModePreview
@Composable
fun BleConnectContentPreview() {
    val fakeState = BleConnectUiState(
        deviceName = "ESP32",
        connectionState = BleConnectionState.Connected("ESP32"),
        isLoading = false,
        isRelay1On = true,
        isRelay2On = false,
        message = null
    )
    BleConnectTheme {
        BleConnectContent(
            uiState = fakeState,
            onDeviceNameChanged = {},
            onConnectClick = {},
            onDisconnectClick = {},
            onRelay1Click = {},
            onRelay2Click = {},
            snackbarHostState = SnackbarHostState()
        )
    }
}

@LightModePreview
@DarkModePreview
@Composable
fun DeviceSectionPreview() {
    BleConnectTheme {
        DeviceSection(
            deviceName = "",
            connectionState = BleConnectionState.Connecting,
            isLoading = false,
            onDeviceNameChanged = {},
            onConnectClick = {},
            onDisconnectClick = {}
        )
    }
}

@LightModePreview
@DarkModePreview
@Composable
fun RelaySectionPreview() {
    BleConnectTheme {
        RelaySection(
            title = "",
            checked = true,
            enabled = true,
            onToggleClick = {}
        )
    }
}