package com.example.shared

enum class BleCommand(val payload: ByteArray) {
    RELAY_ONE_ON(byteArrayOf(0x01)),
    RELAY_TWO_ON(byteArrayOf(0x02)),
    RELAY_ONE_OFF(byteArrayOf(0x11)),
    RELAY_TWO_OFF(byteArrayOf(0x12))
}