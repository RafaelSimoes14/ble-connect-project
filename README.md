# BLE Connect Project

Projeto completo de integração entre **Android (Kotlin)** e **ESP32 (C++)** utilizando **Bluetooth Low Energy (BLE)** para controle de hardware em tempo real.


## Objetivo do Projeto

Demonstrar integração entre:

- Aplicações mobile
- Comunicação BLE
- Sistemas embarcados
- Controle de hardware


## Android App

Aplicativo Android responsável por:

- Conectar via BLE ao ESP32
- Enviar comandos para controle de relés
- Gerenciar estado da conexão
- Exibir feedback para o usuário

### Demonstração do App

<p align="center">
  <img src="https://github.com/user-attachments/assets/463d43ab-15f6-475c-9188-8b24d49d6e24" width="300"/>
  <img src="https://github.com/user-attachments/assets/897d1327-e515-47d4-98ae-bb91b63dd0f0" width="300"/>
  <img src="https://github.com/user-attachments/assets/7c8457a4-a5c6-4a5a-bd31-8b00b6fb464e" width="300"/>
</p>


### Tecnologias
- Kotlin
- MVVM + Clean Architecture
- Kotlin Multiplatform (KMP) para compartilhamento de lógica e abstração de BLE
- Jetpack Compose (UI moderna e declarativa)
- Bluetooth Low Energy (BLE)
- Android SDK


## Firmware ESP32

Firmware desenvolvido em C++ utilizando Arduino Framework:

- Recebimento de comandos do app Android
- Controle de relés físicos (GPIO)
- Reconexão automática BLE

### Exemplo de ESP32 utilizado

<p align="center">
  <img src="https://github.com/user-attachments/assets/78123b69-0d5a-41b4-849f-58a4c12b2803" width="1243" />
</p>


### Tecnologias
- C++
- ESP32
- Arduino Framework
- BLE (Bluetooth Low Energy)


## Comunicação BLE

O app envia comandos para o ESP32 via BLE:

| Comando | Ação |
|--------|------|
| `0x01` | Liga relé 1 |
| `0x11` | Desliga relé 1 |
| `0x02` | Liga relé 2 |
| `0x12` | Desliga relé 2 |


## Como executar

### Android

1. Abrir o projeto no Android Studio
2. Conectar em um dispositivo físico (BLE não funciona bem em emulador)
3. Rodar o app


### ESP32

1. Abrir o código em PlatformIO ou Arduino IDE
2. Conectar o ESP32 via USB
3. Fazer upload do firmware
4. Abrir o monitor serial (115200)


## Possíveis melhorias

- Segurança na comunicação BLE
- Suporte a múltiplos dispositivos
- Modularização do firmware
- Testes unitários e instrumentados do app


## Autor

Rafael Rosa  
Desenvolvedor Android | Engenheiro Eletrônico
