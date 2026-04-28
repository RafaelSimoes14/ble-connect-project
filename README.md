# BLE Connect Project

Projeto completo de integração entre **Android (Kotlin)** e **ESP32 (C++)** utilizando **Bluetooth Low Energy (BLE)** para controle de hardware em tempo real.

---

## Android App

Aplicativo Android responsável por:

- Conectar via BLE ao ESP32
- Enviar comandos para controle de relés
- Gerenciar estado da conexão
- Exibir feedback para o usuário

### Tecnologias
- Kotlin
- MVVM + Clean Architecture
- Kotlin Multiplatform (KMP) para compartilhamento de lógica e abstração de BLE
- Jetpack Compose (UI moderna e declarativa)
- Bluetooth Low Energy (BLE)
- Android SDK

---

## Firmware ESP32

Firmware desenvolvido em C++ utilizando Arduino Framework:

- Criação de servidor BLE
- Recebimento de comandos do app Android
- Controle de relés físicos (GPIO)
- Reconexão automática BLE

### Tecnologias
- C++
- ESP32
- Arduino Framework
- BLE (Bluetooth Low Energy)

---

## Comunicação BLE

O app envia comandos para o ESP32 via BLE:

| Comando | Ação |
|--------|------|
| `0x01` | Liga relé 1 |
| `0x11` | Desliga relé 1 |
| `0x02` | Liga relé 2 |
| `0x12` | Desliga relé 2 |

---

## Como executar

### Android

1. Abrir o projeto no Android Studio
2. Conectar em um dispositivo físico (BLE não funciona bem em emulador)
3. Rodar o app

---

### ESP32

1. Abrir o código em PlatformIO ou Arduino IDE
2. Conectar o ESP32 via USB
3. Fazer upload do firmware
4. Abrir o monitor serial (115200)

---

## Objetivo do Projeto

Demonstrar integração entre:

- Aplicações mobile
- Comunicação BLE
- Sistemas embarcados
- Controle de hardware

---

## Possíveis melhorias

- Segurança na comunicação BLE
- Suporte a múltiplos dispositivos
- Modularização do firmware
- Testes unitários e instrumentados do app

---

## Autor

Rafael Rosa  
Desenvolvedor Android | Engenheiro Eletrônico
