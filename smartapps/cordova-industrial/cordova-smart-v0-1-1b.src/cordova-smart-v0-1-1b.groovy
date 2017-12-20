/**
 *  Nuevo
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
    name: "CORDOVA Smart v0.1.1b",
    namespace: "CORDOVA INDUSTRIAL",
    author: "Cosme Ernesto Santiesteban Toca",
    description: "Prueba",
    category: "My Apps",
    iconUrl: "http://www.cordovaindustrial.com/wp-content/uploads/thegem-logos/logo_28059605826e0bf6c5f6b84ed897e0fa_1x.png",
    iconX2Url: "http://www.cordovaindustrial.com/wp-content/uploads/thegem-logos/logo_28059605826e0bf6c5f6b84ed897e0fa_1x.png",
    iconX3Url: "http://www.cordovaindustrial.com/wp-content/uploads/thegem-logos/logo_28059605826e0bf6c5f6b84ed897e0fa_1x.png")


preferences {
	section("Luces e interruptores") {
		input "switches", "capability.switch", multiple: true, required: true
	}
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Actualizado con la confirugación: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
	getURL(null)
}

// Mi implementación

mappings {
    path("/ui") {
		action: [
			GET: "html",
		]
	}
    path("/switches") {
        action: [
            GET: "listSwitches"
        ]
    }
    path("/switches/:command") {
        action: [
            PUT: "updateSwitches"
        ]
    }
}

def listSwitches() {
    def resp = []
    switches.each {
        resp << [name: it.displayName, value: it.currentValue("switch")]
    }
    return resp
}

void updateSwitches() {
    // use the built-in request object to get the command parameter
    def command = params.command

    // all switches have the command
    // execute the command on all switches
    // (note we can do this on the array - the command will be invoked on every element
    switch(command) {
        case "on":
        	switches.on()
        	break
        case "off":
            switches.off()
            break
        default:
            httpError(400, "$command no es un comando válido para los switches especificados")
    }
}

def getURL(e) {
    if (resetOauth) {
    	log.debug "Reiniciar el Token de acceso"
    	state.accessToken = null
    }    
	if (!state.accessToken) {
    	try {
			createAccessToken()
			log.debug "Creando nuevo Token de acceso: $state.accessToken"
		} catch (ex) {
			log.error "Olvidó habilitar la opción OAuth en la configuración de la SmartApp?"
			log.error ex
		}
    }
	if (state.accessToken) {
		def url1 = "https://graph.api.smartthings.com/api/smartapps/installations/${app.id}/ui"
		def url2 = "?access_token=${state.accessToken}"
		log.info "${title ?: location.name} ActiON Dashboard URL: $url1$url2"
		if (phone) {
			sendSmsMessage(phone, url1)
			sendSmsMessage(phone, url2)
		}
	}
}

def html() {
	render contentType: "text/html", data: """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="utf-8"/>
            <meta name="description" content=""/>
            <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0 minimum-scale=1.0"/>
            <meta name="apple-mobile-web-app-capable" content="yes" />
            <meta name="apple-mobile-web-app-status-bar-style" content="black" />
            <meta name="language" content="es">
            <meta name="content-language" content="es">

            <link rel="icon" sizes="192x192" href="http://www.cordovaidt.com/CIDT-Dash/logo.png">
            <link rel="apple-touch-icon" href="http://www.cordovaidt.com/CIDT-Dash/logo.png">
            <meta name="mobile-web-app-capable" content="yes">
            <title>${app.label ?: location.name}</title>
        </head>
        <body>
            <h1>Saludos desde ${app.label ?: location.name}!!!</h1>
            
            ${renderTiles()}
        </body>
        </html>
        """
}

def renderTiles() {
"""
<div class="tiles">
	${allDeviceData()?.collect{renderTile(it)}.join("\n")}
</div>
"""
}

def allDeviceData() {
	def data = []
	
	if (showClock == "Small Analog") data << [tile: "clock", size: 1, type: "a", date: getDate(), dow: getDOW()]
	else if (showClock == "Large Analog") data << [tile: "clock", size: 2, type: "a", date: getDate(), dow: getDOW()]
    else if (showClock == "Small Digital") data << [tile: "clock", size: 1, type: "d", date: getDate(), dow: getDOW()]
	else if (showClock == "Large Digital") data << [tile: "clock", size: 2, type: "d", date: getDate(), dow: getDOW()]
	
	if (showMode && location.modes) data << [tile: "mode", mode: "$location.mode", isStandardMode: ("$location.mode" == "Home" || "$location.mode" == "Away" || "$location.mode" == "Night"), modes: location.modes.name]
	
	def phrases = location?.helloHome?.getPhrases()*.label?.sort()
	if (showHelloHome && phrases) data << [tile: "helloHome", phrases: phrases]
	
	weather?.each{data << getWeatherData(it)}
	
	holiday?.each{data << getDeviceData(it, "holiday")}
	locks?.each{data << getDeviceData(it, "lock")}
	switches?.each{data << getDeviceData(it, "switch")}
	dimmers?.each{data << getDeviceData(it, "dimmer")}
	momentaries?.each{data << getDeviceData(it, "momentary")}
	contacts?.each{data << getDeviceData(it, "contact")}
	presence?.each{data << getDeviceData(it, "presence")}
	motion?.each{data << getDeviceData(it, "motion")}
	camera?.each{data << getDeviceData(it, "camera")}
	(1..3).each{if (settings["dropcamStreamUrl$it"]) {data << [tile: "video", link: settings["dropcamStreamUrl$it"], title: settings["dropcamStreamT$it"] ?: "Stream $it", i: it]}}
	temperature?.each{data << getDeviceData(it, "temperature")}
	humidity?.each{data << getDeviceData(it, "humidity")}
	water?.each{data << getDeviceData(it, "water")}
	energy?.each{data << getDeviceData(it, "energy")}
	power?.each{data << getDeviceData(it, "power")}
	battery?.each{data << getDeviceData(it, "battery")}
	carbon_monoxide_detector?.each{data << getDeviceData(it, "carbonMonoxide")}
	smoke?.each{data << getDeviceData(it, "smoke")}
	valves?.each{data << getDeviceData(it, "valve")}
	
	(1..3).each{if (settings["linkUrl$it"]) {data << [tile: "link", link: settings["linkUrl$it"], title: settings["linkTitle$it"] ?: "Link $it", i: it]}}
	
	data << [tile: "refresh", ts: getTS()]
	
	data
}

def getDeviceData(device, type) {
	[tile: "device", 
    active: isActive(device, type), 
    type: type, 
    device: device.id, 
    name: device.displayName,
    value: getDeviceValue(device, type), 
    level: getDeviceLevel(device, type), 
    isValue: isValue(device, type)]
}

def isActive(device, type) {
	def field = getDeviceFieldMap()[type]
	def value = device.respondsTo("currentValue") ? device.currentValue(field) : device.value
	def active = value == getActiveDeviceMap()[type] ? "active" : "inactive"
	active
}

def getDeviceValue(device, type) {
	def unitMap = [temperature: "°", humidity: "%", battery: "%", power: "W", energy: "kWh"]
	def field = getDeviceFieldMap()[type]
	def value = device.respondsTo("currentValue") ? device.currentValue(field) : device.value
	if (!isValue(device, type)) return value
	else return "${roundNumber(value)}${unitMap[type] ?: ""}"
}

def getDeviceLevel(device, type) {
    if (type == "dimmer" ||  type == "music") 
    return "${device.currentValue("level") / 10.0}".toDouble().round()
}

def isValue(device, type) {
	!(["momentary", "camera"] << getActiveDeviceMap().keySet()).flatten().contains(type)
}

def getDeviceFieldMap() {
    [lock: "lock", 
    holiday: "switch", 
    "switch": "switch", 
    dimmer: "switch", 
    contact: "contact", 
    presence: "presence", 
    temperature: "temperature", 
    humidity: "humidity", 
    motion: "motion", 
    water: "water", 
    power: "power", 
    energy: "energy", 
    battery: "battery", 
    carbon_monoxide_detector: "carbonMonoxide", 
    smoke: "smoke", 
    valve:"contact"]
}

def getActiveDeviceMap() {
    [lock: "unlocked", 
    holiday: "on", 
    "switch": "on", 
    dimmer: "on", 
    contact: "open", 
    presence: "present", 
    motion: "active", 
    water: "wet", 
    carbon_monoxide_detector: "clear", 
    smoke: "clear", 
    valve: "open"]
}

def getTS() {
	def tf = new java.text.SimpleDateFormat("h:mm a")
    if (location?.timeZone) tf.setTimeZone(location.timeZone)
    "${tf.format(new Date())}"
}

def renderTile(data) {
	if (data.type == "weather"){
		return """<div class="weather tile w2" data-type="weather" data-device="$data.device" data-weather='${data.encodeAsJSON()}'></div>"""
	} else if (data.tile == "device") {
		return """<div class="$data.type tile $data.active" data-active="$data.active" data-type="$data.type" data-device="$data.device" data-value="$data.value" data-level="$data.level" data-is-value="$data.isValue"><div class="title">$data.name</div></div>"""
	} else if (data.tile == "link") {
		return """<div class="link tile" data-link-i="$data.i"><div class="title">$data.title</div><div class="icon"><a href="$data.link" data-ajax="false" style="color:white"><i class="fa fa-link"></i></a></div></div>"""
	} else if (data.tile == "video") {
		return """<div class="video tile h2 w2" data-link-i="$data.i"><div class="title">$data.title</div><div class="icon" style="margin-top:-82px;"><object width="240" height="164"><param name="movie" value="$data.link"></param><param name="allowFullScreen" value="true"></param><param name="allowscriptaccess" value="always"></param><param name="wmode" value="opaque"></param><embed src="$data.link" type="application/x-shockwave-flash" allowscriptaccess="always" allowfullscreen="true" width="240" height="164" wmode="opaque"></embed></object></div></div>"""
	} else if (data.tile == "refresh") {
		return """<div class="refresh tile clickable"><div class="title">Actualizar</div><div class="footer">Actualizado $data.ts</div></div>"""
	} else if (data.tile == "mode") {
		return renderModeTile(data)
	} else if (data.tile == "clock") {
		if (data.type == "a") {
			return """<div id="analog-clock" class="clock tile clickable h$data.size w$data.size"><div class="title">$data.date</div><div class="icon" style="margin-top:-${data.size * 45}px;"><canvas id="clockid" class="CoolClock:st:${45 * data.size}"></canvas></div><div class="footer">$data.dow</div></div>"""
		} else {
			return """<div id="digital-clock" class="clock tile clickable w$data.size"><div class="title">$data.date</div><div class="icon ${data.size == 2 ? "" : "text"}" id="clock">*</div><div class="footer">$data.dow</div></div>"""
		}
	} else if (data.tile == "helloHome") {
		return renderHelloHomeTile(data)
	}
	
	return ""
}

