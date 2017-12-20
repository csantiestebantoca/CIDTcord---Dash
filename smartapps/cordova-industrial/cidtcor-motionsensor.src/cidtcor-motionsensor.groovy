/**
 *  CIDTcor_MotionSensor
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
    name: "CIDTcor_MotionSensor",
    namespace: "CORDOVA INDUSTRIAL",
    author: "Cosme Ernesto Santiesteban Toca",
    description: "Prueba con sensor de movimiento",
    category: "Safety & Security",
    iconUrl: "http://www.cordovaindustrial.com/wp-content/uploads/thegem-logos/logo_28059605826e0bf6c5f6b84ed897e0fa_1x.png",
    iconX2Url: "http://www.cordovaindustrial.com/wp-content/uploads/thegem-logos/logo_28059605826e0bf6c5f6b84ed897e0fa_1x.png",
    iconX3Url: "http://www.cordovaindustrial.com/wp-content/uploads/thegem-logos/logo_28059605826e0bf6c5f6b84ed897e0fa_1x.png")

preferences {
    section("Sensor de movimiento:") {
        input "sensor_movimiento", "capability.motionSensor", required: true, title: "Movimiento?"
        input "minutos", "number", required: true, title: "minutos?"
        input "interruptor", "capability.switch", required: true
    }
}

def installed() {
    initialize()
}

def updated() {
    unsubscribe()
    initialize()
}

def initialize() {
    subscribe(sensor_movimiento, "motion.active", motionDetectedHandler)
    subscribe(sensor_movimiento, "motion.inactive", motionStoppedHandler)
}

def motionDetectedHandler(evt) {
    interruptor.on()
}

def motionStoppedHandler(evt) {
    runIn(60 * minutos, checkMotion)
}

def checkMotion() {

    def motionState = sensor_movimiento.currentState("motion")

    if (motionState.value == "inactive") {
        def elapsed = now() - motionState.date.time

        def threshold = 1000 * 60 * minutos

        if (elapsed >= threshold) {
            log.debug "El movimiento se ha mantenido inactivo el tiempo suficiente desde la última comprobación ($elapsed ms):  Apagando el interruptor"
            interruptor.off()
        } else {
            log.debug "El movimiento no se ha mantenido inactivo el tiempo suficiente desde la última comprobación ($elapsed ms):  No hacer nada"
        }
    } else {
        log.debug "Hay movimiento, esperando inactividad"
    }
}
