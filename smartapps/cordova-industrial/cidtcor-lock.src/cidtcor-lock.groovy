/**
 *  CIDTcor_Lock
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
    name: "CIDTcor_Lock",
    namespace: "CORDOVA INDUSTRIAL",
    author: "Cosme Ernesto Santiesteban Toca",
    description: "Prueba con la cerradura electr\u00F3nica",
    category: "Safety & Security",
    iconUrl: "http://www.cordovaindustrial.com/wp-content/uploads/thegem-logos/logo_28059605826e0bf6c5f6b84ed897e0fa_1x.png",
    iconX2Url: "http://www.cordovaindustrial.com/wp-content/uploads/thegem-logos/logo_28059605826e0bf6c5f6b84ed897e0fa_1x.png",
    iconX3Url: "http://www.cordovaindustrial.com/wp-content/uploads/thegem-logos/logo_28059605826e0bf6c5f6b84ed897e0fa_1x.png")


preferences {
	section("Title") {
		// TODO: put inputs here
	}
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
	// TODO: subscribe to attributes, devices, locations, etc.
}

// TODO: implement event handlers