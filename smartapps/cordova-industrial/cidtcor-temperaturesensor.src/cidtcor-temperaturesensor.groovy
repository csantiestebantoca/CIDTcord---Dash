/**
 *  CIDTcor_TemperatureSensor
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
    name: "CIDTcor_TemperatureSensor",
    namespace: "CORDOVA INDUSTRIAL",
    author: "Cosme Ernesto Santiesteban Toca",
    description: "Prueba con sensor de temperatura",
    category: "Safety & Security",
    iconUrl: "http://www.cordovaindustrial.com/wp-content/uploads/thegem-logos/logo_28059605826e0bf6c5f6b84ed897e0fa_1x.png",
    iconX2Url: "http://www.cordovaindustrial.com/wp-content/uploads/thegem-logos/logo_28059605826e0bf6c5f6b84ed897e0fa_1x.png",
    iconX3Url: "http://www.cordovaindustrial.com/wp-content/uploads/thegem-logos/logo_28059605826e0bf6c5f6b84ed897e0fa_1x.png")

preferences {
    section ("Dispositivos disponibles:") {
        paragraph "Sensores y actuadores"
        input "sensor_temperatura", "capability.temperatureMeasurement", title: "Temperatura actual?"
        input "manipulador_ducha", "capability.switchLevel", title: "Manipulador de la ducha"
    }
}

def installed() {
	log.debug "Instalado con: ${settings}"

	initialize()
}

def updated() {
	log.debug "Actualizado con: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
	subscribe sensor_temperatura, "temperature", tempHandler
}

// TODO: implement event handlers

def tempHandler(evt) {
	if (evt.value != null) {
    	def temp = getTemp(evt)
        controlTemp(temp)
    } 
}

def getTemp(evt) {
    def offSet = 0.01f
    def temp = Float.valueOf(evt.value) + offSet
    log.debug "Temperatura actual ${temp}ºC"
}

def controlTemp(temp) {
	def min = 3
    def max = 32
	def reg = 100 - (temp - min ) * 100 / (max - min)
    manipulador_ducha.setLevel(reg)
    log.debug "Regulación: ${reg}%"
}