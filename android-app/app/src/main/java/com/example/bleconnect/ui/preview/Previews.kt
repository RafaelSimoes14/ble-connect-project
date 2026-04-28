package com.example.bleconnect.ui.preview


import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.bleconnect.ui.theme.BleConnectTheme

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
annotation class DarkModePreview

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL)
annotation class LightModePreview

@DarkModePreview
@Composable
private fun DarkMode() {
    BleConnectTheme {
        Sample()
    }
}

@LightModePreview
@Composable
private fun LightMode() {
    BleConnectTheme {
        Sample()
    }
}

@Composable
private fun Sample() {
    Surface {
        Row {
            Text("test")
        }
    }
}
