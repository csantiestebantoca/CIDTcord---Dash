/**
 *  CIDTcor_OpenDoor
 *
 *  Copyright 2017 Cosme Ernesto Santiesteban Toca
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "CIDTcor_OpenDoor",
    namespace: "CORDOVA INDUSTRIAL",
    author: "Cosme Ernesto Santiesteban Toca",
    description: "Activar las luces de la sala principal al abrir la puerta.\r\n\r\n(C) 2017 Centro de Investigaci\u00F3n Tecnolog\u00EDas\r\n(C) 2017 C\u00F3rdova Industrial e Integradores S.A. de C.V.",
    category: "",
    iconUrl: "http://www.cordovaindustrial.com/wp-content/uploads/thegem-logos/logo_28059605826e0bf6c5f6b84ed897e0fa_1x.png",
    iconX2Url: "http://www.cordovaindustrial.com/wp-content/uploads/thegem-logos/logo_28059605826e0bf6c5f6b84ed897e0fa_1x.png",
    iconX3Url: "http://www.cordovaindustrial.com/wp-content/uploads/thegem-logos/logo_28059605826e0bf6c5f6b84ed897e0fa_1x.png")


preferences {
    section ("Dispositivos disponibles:") {
        paragraph "Contactos y sensores"
        input "sensor_puerta", "capability.contactSensor", title: "Cuándo?"
        input "luz_sala", "capability.switch", title: "Luz de la sala"
        input "cerradura_puerta", "capability.lock", title: "Cierre de la puerta"
        input "sensor_movimiento", "capability.motionSensor", title: "Sensor de movimiento", multiple: true
        //input "sensor_humedad", "capability.moistureSensor", title: "Sensor de humedad"
    }
}

def installed() {
	log.debug "Instalar configuraciones: ${settings}"

	initialize()
}

def updated() {
	log.debug "Actualizar configuraciones: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
	subscribe sensor_puerta, "contact.open", openHandler
	subscribe sensor_puerta, "contact.closed", closedHandler
	subscribe sensor_movimiento, "motion.active", alarmHandler
}

// TODO: implement event handlers


def openHandler(evt) {
    luz_sala.on();
    cerradura_puerta.unlock()
}

def closedHandler(evt) {
    luz_sala.off();
    cerradura_puerta.lock()
}

def alarmHandler(evt) {
    luz_sala.on();
    cerradura_puerta.lock()
}

def moistureHandler(evt) {
    log.debug = "El clima está seco"
}
