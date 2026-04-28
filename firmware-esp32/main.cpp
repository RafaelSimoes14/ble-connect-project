#include <Arduino.h>
#include <BLEDevice.h>
#include <BLEUtils.h>
#include <BLEServer.h>

#define RELAY_1 23
#define RELAY_2 22

#define SERVICE_UUID        "0000FFE0-0000-1000-8000-00805F9B34FB"
#define CHARACTERISTIC_UUID "0000FFE1-0000-1000-8000-00805F9B34FB"

bool relay1State = false;
bool relay2State = false;

class MyServerCallbacks : public BLEServerCallbacks {

    void onConnect(BLEServer* pServer) {
        Serial.println("Dispositivo conectado");
    }

    void onDisconnect(BLEServer* pServer) {
        Serial.println("Dispositivo desconectado");

        delay(300);
        BLEDevice::startAdvertising();

        Serial.println("BLE anunciando novamente...");
    }
};

class MyCallbacks : public BLECharacteristicCallbacks {

    void onWrite(BLECharacteristic *pCharacteristic) {
        std::string value = pCharacteristic->getValue();

        if (value.length() == 0) return;

        uint8_t command = value[0];

        Serial.print("Comando recebido: ");
        Serial.println(command, HEX);

        switch (command) {

            case 0x01:
                digitalWrite(RELAY_1, LOW);
                relay1State = true;
                Serial.println("Rele 1 LIGADO");
                break;

            case 0x11:
                digitalWrite(RELAY_1, HIGH);
                relay1State = false;
                Serial.println("Rele 1 DESLIGADO");
                break;

            case 0x02:
                digitalWrite(RELAY_2, LOW);
                relay2State = true;
                Serial.println("Rele 2 LIGADO");
                break;

            case 0x12:
                digitalWrite(RELAY_2, HIGH);
                relay2State = false;
                Serial.println("Rele 2 DESLIGADO");
                break;

            default:
                Serial.println("Comando desconhecido");
                break;
        }
    }
};

void setup() {
    Serial.begin(115200);

    pinMode(RELAY_1, OUTPUT);
    pinMode(RELAY_2, OUTPUT);

    digitalWrite(RELAY_1, HIGH);
    digitalWrite(RELAY_2, HIGH);

    delay(1000);

    Serial.println("Iniciando BLE...");

    BLEDevice::init("ESP32_BLE");

    BLEServer *pServer = BLEDevice::createServer();
    pServer->setCallbacks(new MyServerCallbacks());

    BLEService *pService = pServer->createService(SERVICE_UUID);

    BLECharacteristic *pCharacteristic = pService->createCharacteristic(
        CHARACTERISTIC_UUID,
        BLECharacteristic::PROPERTY_WRITE |
        BLECharacteristic::PROPERTY_READ |
        BLECharacteristic::PROPERTY_NOTIFY
    );

    pCharacteristic->setCallbacks(new MyCallbacks());
    pCharacteristic->setValue("Ready");

    pService->start();

    BLEAdvertising *pAdvertising = BLEDevice::getAdvertising();
    pAdvertising->addServiceUUID(SERVICE_UUID);
    pAdvertising->setScanResponse(true);
    pAdvertising->start();

    Serial.println("BLE pronto. Aguardando conexao...");
}

void loop() {
    delay(1000);
}