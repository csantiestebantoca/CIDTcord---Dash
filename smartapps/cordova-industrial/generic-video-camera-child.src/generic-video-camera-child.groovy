/**
*  Generic Video Camera Child
*
*  Copyright 2016 Patrick Stuart
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
    name: "Generic Video Camera Child",
    namespace: "CORDOVA INDUSTRIAL",
    author: "Patrick Stuart",
    description: "Child Video Camera SmartApp",
    category: "Safety & Security",
    iconUrl: "http://www.cordovaindustrial.com/wp-content/uploads/thegem-logos/logo_28059605826e0bf6c5f6b84ed897e0fa_1x.png",
    iconX2Url: "http://www.cordovaindustrial.com/wp-content/uploads/thegem-logos/logo_28059605826e0bf6c5f6b84ed897e0fa_1x.png",
    iconX3Url: "http://www.cordovaindustrial.com/wp-content/uploads/thegem-logos/logo_28059605826e0bf6c5f6b84ed897e0fa_1x.png")


preferences {
    page(name: "mainPage", title: "Install Video Camera", install: true, uninstall:true) {
        section("Camera Name") {
            label(name: "label", title: "Name This Camera", required: true, multiple: false, submitOnChange: true)
        }
        section("Add a Camera") {
        	input("CameraStreamPathList","enum", title: "Camera Stream Path", description: "Please enter your camera's streaming path", required:false, submitOnChange: true,
            options: [ //add your camera urls here
            ["http://192.168.1.93/cgi-bin/sp.cgi?chn=0&quality=0&rate=30&u=admin&p":"Camera 1"], //Córdova
            ["http://http://124.154.243.230/SnapshotJPEG?Resolution=640x480&amp;amp;Quality=Clarity&amp;amp;1511811712":"Camera Fukuoka"], //Fukuoka
            ["http://http://46.195.18.64:8000/mjpg/video.mjpg?COUNTER":"Camera Zoo"] //Zoo
            ], displayDuringSetup: true)
        
            
            	input("CameraStreamPathCustom","string", title: "Camera Stream Path", description: "Please enter your camera's streaming path", defaultValue: settings?.CameraStreamPathList, required:false, displayDuringSetup: true)
        
            }
    }
    
}

def installed() {
    log.debug "Installed"

    initialize()
}

def updated() {
    log.debug "Updated"

    unsubscribe()
    initialize()
}

def initialize() {
	log.debug "CameraStreamPathList is $CameraStreamPathList"
    log.debug "CameraStreamPathCustom is $CameraStreamPathCustom"
	if(CameraStreamPathList) { state.CameraStreamPath = CameraStreamPathList }
    if(CameraStreamPathCustom) { state.CameraStreamPath = CameraStreamPathCustom }
    try {
        def DNI = (Math.abs(new Random().nextInt()) % 99999 + 1).toString()
        def cameras = getChildDevices()
        if (cameras) {
            removeChildDevices(getChildDevices())
        }
        def childDevice = addChildDevice("pstuart", "Generic Video Camera", DNI, null, [name: app.label, label: app.label, completedSetup: true])
    } catch (e) {
    	log.error "Error creating device: ${e}"
    }
}

private removeChildDevices(delete) {
    delete.each {
        deleteChildDevice(it.deviceNetworkId)
    }
}