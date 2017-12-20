/**
 *  Córdova Dashboard v0.2b
 *
 *  Córdova Dashboard is a web application to managing of your smart devices. 
 *  The dashboard is responsive and do not need to install any SmartThings Mobile application to run.
 *
 *  Copyright © 2017 Córdova Industrial
 *
 *  Licensed under the Apache License, Version 2.0 (http://www.apache.org/licenses/LICENSE-2.0)
 *
 */
 
definition(
    name: "CIDTcor_Dashboard_v0.2b",
    namespace: "CORDOVA INDUSTRIAL",
    author: "Cosme E. Santiesteban Toca",
    description: "A web application to managing of your smart devices (version 0.2b)",
    category: "Convenience",
    iconUrl: "http://www.cordovaindustrial.com/wp-content/uploads/thegem-logos/logo_28059605826e0bf6c5f6b84ed897e0fa_1x.png",
    iconX2Url: "http://www.cordovaindustrial.com/wp-content/uploads/thegem-logos/logo_28059605826e0bf6c5f6b84ed897e0fa_1x.png",
    iconX3Url: "http://www.cordovaindustrial.com/wp-content/uploads/thegem-logos/logo_28059605826e0bf6c5f6b84ed897e0fa_1x.png",
    oauth: true)


preferences {
	page(name: "selectDevices", title: "Dispositivos", install: false, uninstall: true, nextPage: "selectPhrases") {
    
        section("Panel de control") {
            paragraph "Panel de control Córdova. \n" +
            "Panel de control personalizable... acceda a cada uno de sus dispositivos de forma sencilla en una única interfaz. \n" +
            "Copyright © 2017 Córdova Industrial"
        }
        
    	section("Dispositivos a controlar...") {
            input "holiday", "capability.switch", title: "Luces para fiesta?", multiple: true, required: false
            input "switches", "capability.switch", title: "Interruptores?", multiple: true, required: false
            input "dimmers", "capability.switchLevel", title: "Variadores?", multiple: true, required: false
            input "momentaries", "capability.momentary", title: "Interruptores momentaneos?", multiple: true, required: false
            input "locks", "capability.lock", title: "Cerraduras?", multiple: true, required: false
			input "camera", "capability.imageCapture", title: "Cámaras?", multiple: true, required: false
			input "valves", "capability.valve", title: "Válvulas?", multiple: true, required: false 
        }

        section("Dispositivos para monitorear...") {
            input "contacts", "capability.contactSensor", title: "Contactos?", multiple: true, required: false
            input "presence", "capability.presenceSensor", title: "Presencia?", multiple: true, required: false
            input "temperature", "capability.temperatureMeasurement", title: "Temperatura?", multiple: true, required: false
            input "humidity", "capability.relativeHumidityMeasurement", title: "Humedad Relativa?", multiple: true, required: false
            input "motion", "capability.motionSensor", title: "Moviemiento?", multiple: true, required: false
            input "water", "capability.waterSensor", title: "Agua?", multiple: true, required: false
            input "battery", "capability.battery", title: "Batería?", multiple: true, required: false
            input "energy", "capability.energyMeter", title: "Energía?", multiple: true, required: false
            input "power", "capability.powerMeter", title: "Potencia?", multiple: true, required: false
            input "weather", "device.smartweatherStationTile", title: "Clima?", multiple: true, required: false
            input "carbon_monoxide_detector", "device.carbonMonoxideDetector", title: "Detector monóxido de carbono?", multiple: true, required: false
            input "smoke", "device.smokeDetector", title: "Humo?", multiple: true, required: false
        }
    }
    
	page(name: "selectPhrases", title: "Accesos directos", install: false, uninstall: true, nextPage: "selectPreferences") {
		section("Show...") {
        	input "showMode", title: "Modo de visulaización", "bool", required: true, defaultValue: true
			input "showHelloHome", title: "Mostrar acciones", "bool", required: true, defaultValue: true
            input "showClock", title: "Mostrar reloj", "enum", multiple: false, required: true, defaultValue: "Large Analog", options: ["Small Analog", "Small Digital", "Large Analog", "Large Digital", "None"]
			input "roundNumbers", title: "Redondeo de decimales", "bool", required: true, defaultValue:true
        }
        
        section("Cámaras de video vigilancia") {
			paragraph "Enter absolute URL starting with http..."
        	input "dropcamStreamT1", "text", title:"Cámara 1", required: false
            input "dropcamStreamUrl1", "text", title:"URL 1", required: false
			input "dropcamStreamT2", "text", title:"Cámara 2", required: false
            input "dropcamStreamUrl2", "text", title:"URL 2", required: false
			input "dropcamStreamT3", "text", title:"Cámara 3 ", required: false
            input "dropcamStreamUrl3", "text", title:"URL 3", required: false
        }
        
        section("Mostrar enlaces") {
			paragraph "Enter absolute URL starting with http..."
        	input "linkTitle1", "text", title:"Nombre enlace 1", required: false
            input "linkUrl1", "text", title:"URL 1", required: false
			input "linkTitle2", "text", title:"Nombre enlace 2", required: false
            input "linkUrl2", "text", title:"URL 2", required: false
			input "linkTitle3", "text", title:"Nombre enlace 3", required: false
            input "linkUrl3", "text", title:"URL 3", required: false
        }
	}
	
    page(name: "selectPreferences", title: "Preferencias", install: true, uninstall: true) {
        section("Panel de preferencias...") {
        	label title: "Title", required: false, defaultValue: "CIDTcor_Dashboard_v0.2b"
        }
		
        section("Resetear el token de acceso al panel...") {
        	paragraph "Activar esta opción invalida el token de acceso actual. La nueva URL de acceso al panel de control aparecerá en los logs. El token de acceso se mantendrá reseteado mientras la opción no sea puesta a OFF."
        	input "resetOauth", "bool", title: "Resetear el token de acceso al panel de control?", defaultValue: false
        }
        
        section("Enviar mensaje de texto a:") {
        	paragraph "Enviar mensaje de texto con el token de acceso al panel de control <<opcional>> (Recibirá dos mensajes con la URL)."
            input "phone", "phone", title: "Teléfono?", required: false
        }
    }
}

mappings {
    path("/ui") {
		action: [
			GET: "html",
		]
	}
    path("/command") {
    	action: [
			GET: "command",
		]
    }
	path("/data") {
    	action: [
			GET: "allDeviceData",
		]
    }
	path("/ping") {
    	action: [
			GET: "ping",
		]
    }
}

def command() {
	log.debug "command received with params $params"
    
    def id = params.device
    def type = params.type
    def command = params.command
	def value = params.value

	def device
    
	if (type == "switch") {
		device = switches?.find{it.id == id}
		if (device) {
			if(device.currentValue('switch') == "on") {
				device.off()
			} else {
				device.on()
			}
		}
	} else if (type == "holiday") {
		device = holiday?.find{it.id == id}
		if (device) {
			if(device.currentValue('switch') == "on") {
				device.off()
			} else {
				device.on()
			}
		}
	} else if (type == "lock") {
		device = locks?.find{it.id == id}
		if (device) {
			if(device.currentValue('lock') == "locked") {
                device.unlock()
            } else {
                device.lock()
            }
		}
	} else if (type == "dimmer") {
		device = dimmers?.find{it.id == id}
		if (device) {
			if (command == "toggle") {
				if(device.currentValue('switch') == "on") {
					device.off()
				} else {
					device.setLevel(Math.min((value as Integer) * 10, 99))
				}
			} else if (command == "level") {
				device.setLevel(Math.min((value as Integer) * 10, 99))
			}
		}
    } else if (type == "mode") {
		setLocationMode(command)
	} else if (type == "helloHome") {
        log.debug "executing Hello Home '$value'"
    	location.helloHome.execute(command)
    } else if (type == "momentary") {
    	momentaries?.find{it.id == id}?.push()
    } else if (type == "camera") {
    	camera?.find{it.id == id}.take()
    } else if (type == "valve") {
		device = valves?.find{it.id == id}
		if (device) {
			if(device.currentValue('contact') == "open") {
				device.closed()
			} else {
				device.open()
			}
		}
	}
    
	[status:"ok"]
}

def installed() {
	log.debug "CIDTcor_Dashboard_v0.2b instalado: ${settings}"
	initialize()
}

def updated() {
	log.debug "CIDTcor_Dashboard_v0.2b actualizado: ${settings}"
	unsubscribe()
	initialize()
}

def initialize() {
	subscribe(app, getURL)
    scheduledWeatherRefresh()
    getURL(null)
	
	updateStateTS()
	
	subscribe(location, handler)
    subscribe(app, handler)
	subscribe(holiday, "switch.on", handler, [filterEvents: false])
	subscribe(holiday, "switch.off", handler, [filterEvents: false])
	subscribe(holiday, "switch", handler, [filterEvents: false])
	subscribe(holiday, "level", handler, [filterEvents: false])
    subscribe(switches, "switch", handler, [filterEvents: false])
    subscribe(dimmers, "level", handler, [filterEvents: false])
	subscribe(dimmers, "switch", handler, [filterEvents: false])
    subscribe(locks, "lock", handler, [filterEvents: false])
    subscribe(contacts, "contact", handler, [filterEvents: false])
    subscribe(presence, "presence", handler, [filterEvents: false])
    subscribe(temperature, "temperature", handler, [filterEvents: false])
    subscribe(humidity, "humidity", handler, [filterEvents: false])
    subscribe(motion, "motion", handler, [filterEvents: false])
    subscribe(water, "water", handler, [filterEvents: false])
    subscribe(battery, "battery", handler, [filterEvents: false])
    subscribe(energy, "energy", handler, [filterEvents: false])
    subscribe(power, "power", handler, [filterEvents: false])
    subscribe(carbon_dioxide_detector, "carbonMonoxide", handler, [filterEvents: false])
    subscribe(smoke, "smoke", handler, [filterEvents: false])
    subscribe(valves, "valve", handler, [filterEvents: false])
}

def getURL(e) {
    if (resetOauth) {
    	log.debug "Reseting Access Token"
    	state.accessToken = null
    }    
	if (!state.accessToken) {
    	try {
			createAccessToken()
			log.debug "Creating new Access Token: $state.accessToken"
		} catch (ex) {
			log.error "Did you forget to enable OAuth in SmartApp settings for ActiON Dashboard?"
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

def scheduledWeatherRefresh() {
    runIn(3600, scheduledWeatherRefresh, [overwrite: false])
	weather?.refresh()
    state.lastWeatherRefresh = getTS()
	updateStateTS()
}

def getTS() {
	def tf = new java.text.SimpleDateFormat("h:mm a")
    if (location?.timeZone) tf.setTimeZone(location.timeZone)
    "${tf.format(new Date())}"
}

def getDate() {
	def tf = new java.text.SimpleDateFormat("MMMMM d")
    if (location?.timeZone) tf.setTimeZone(location.timeZone)
    "${tf.format(new Date())}"
}

def getDOW() {
	def tf = new java.text.SimpleDateFormat("EEEE")
    if (location?.timeZone) tf.setTimeZone(location.timeZone)
    "${tf.format(new Date())}"
}

def renderModeTile(data) {
"""
<div class="mode tile w2 menu ${data.isStandardMode ? data.mode : ""}" data-mode="$data.mode" data-popup="mode-popup">
	<div class="title">Modo de trabajo</div>
	<div data-role="popup" id="mode-popup" data-overlay-theme="b">
		<ul data-role="listview" data-inset="true" style="min-width:210px;">
			${data.modes.collect{"""<li data-icon="false">$it</li>"""}.join("\n")}
		</ul>
    </div>
	<div class="icon Home"><i class="fa fa-home"></i></div>
	<div class="icon Night"><i class="fa fa-moon-o"></i></div>
	<div class="icon Away"><i class="fa fa-sign-out"></i></div>
	<div class="icon small text mode-name" id="mode-name">$data.mode</div>
</div>
"""
}

def renderHelloHomeTile(data) {
"""
<div class="hello-home tile menu" data-rel="popup" data-popup="hello-home-popup">
	<div class="title">Automatizaciones</div>
	<div class="icon"><i class="fa fa-comment-o"></i></div>
	<div data-role="popup" id="hello-home-popup" data-overlay-theme="b">
		<ul data-role="listview" data-inset="true" style="min-width:210px;">
			${data.phrases.collect{"""<li data-icon="false">$it</li>"""}.join("\n")}
		</ul>
	</div>
</div>
"""
}

def roundNumber(num) {
	if (!roundNumbers || !"$num".isNumber()) return num
	if (num == null || num == "") return "n/a"
	else {
    	try {
            return "$num".toDouble().round()
        } catch (e) {return num}
    }
}

def getWeatherData(device) {
	def data = [tile:"device", active:"inactive", type: "weather", device: device.id]
    ["city", "weather", "feelsLike", "temperature", "localSunrise", "localSunset", "percentPrecip", "humidity", "weatherIcon"].each{data["$it"] = device?.currentValue("$it")}
    data.icon = ["chanceflurries":"wi-snow","chancerain":"wi-rain","chancesleet":"wi-rain-mix","chancesnow":"wi-snow","chancetstorms":"wi-storm-showers","clear":"wi-day-sunny","cloudy":"wi-cloudy","flurries":"wi-snow","fog":"wi-fog","hazy":"wi-dust","mostlycloudy":"wi-cloudy","mostlysunny":"wi-day-sunny","partlycloudy":"wi-day-cloudy","partlysunny":"wi-day-cloudy","rain":"wi-rai","sleet":"wi-rain-mix","snow":"wi-snow","sunny":"wi-day-sunny","tstorms":"wi-storm-showers","nt_chanceflurries":"wi-snow","nt_chancerain":"wi-rain","nt_chancesleet":"wi-rain-mix","nt_chancesnow":"wi-snow","nt_chancetstorms":"wi-storm-showers","nt_clear":"wi-stars","nt_cloudy":"wi-cloudy","nt_flurries":"wi-snow","nt_fog":"wi-fog","nt_hazy":"wi-dust","nt_mostlycloudy":"wi-night-cloudy","nt_mostlysunny":"wi-night-cloudy","nt_partlycloudy":"wi-night-cloudy","nt_partlysunny":"wi-night-cloudy","nt_sleet":"wi-rain-mix","nt_rain":"wi-rain","nt_snow":"wi-snow","nt_sunny":"wi-night-clear","nt_tstorms":"wi-storm-showers","wi-horizon":"wi-horizon"][data.weatherIcon]
	data
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

def getDeviceFieldMap() {[lock: "lock", holiday: "switch", "switch": "switch", dimmer: "switch", contact: "contact", presence: "presence", temperature: "temperature", humidity: "humidity", motion: "motion", water: "water", power: "power", energy: "energy", battery: "battery", carbon_monoxide_detector: "carbonMonoxide", smoke: "smoke", valve:"contact"]}

def getActiveDeviceMap() {[lock: "unlocked", holiday: "on", "switch": "on", dimmer: "on", contact: "open", presence: "present", motion: "active", water: "wet", carbon_monoxide_detector: "clear", smoke: "clear", valve: "open"]}

def isValue(device, type) {!(["momentary", "camera"] << getActiveDeviceMap().keySet()).flatten().contains(type)}

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

def getDeviceLevel(device, type) {if (type == "dimmer" ||  type == "music") return "${device.currentValue("level") / 10.0}".toDouble().round()}

def handler(e) {
	log.debug "event happened $e.description"
	updateStateTS()
}

def updateStateTS() {state.ts = now()}

def getStateTS() {state.ts}

def ping() {
	if ("$params.ts" == "${getStateTS()}") [status: "noop", updated: getTS(), ts: getStateTS()]
	else [status: "update", updated: getTS(), ts: getStateTS(), data: allDeviceData()]
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
	valve?.each{data << getDeviceData(it, "valve")}
	
	(1..3).each{if (settings["linkUrl$it"]) {data << [tile: "link", link: settings["linkUrl$it"], title: settings["linkTitle$it"] ?: "Link $it", i: it]}}
	
	data << [tile: "refresh", ts: getTS()]
	
	data
}

def html() {
	render contentType: "text/html", data: """
        <!DOCTYPE html>
        <html>
        <head>
            ${head()}
            <style> 
                ${style()}
            </style>
        </head>
        <body>
            ${renderMain()}
            ${renderWTFCloud()}
        </body>
        </html>
        <script>
        	${script()}
        </script>
        """
}

def head() {
"""
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
	<!--title>${app.label ?: location.name}</title-->
	<title>CORDOVA Smart v0.2b</title>

    <link rel="stylesheet" href="https://code.jquery.com/mobile/1.4.4/jquery.mobile-1.4.4.min.css" />
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/weather-icons/1.3.2/css/weather-icons.min.css" />

	<script src="https://code.jquery.com/jquery-2.1.1.min.js" type="text/javascript"></script>
	<script src="https://code.jquery.com/mobile/1.4.4/jquery.mobile-1.4.4.min.js" type="text/javascript"></script>
	<script src="https://cdn.jsdelivr.net/coolclock/2.1.4/coolclock.min.js" type="text/javascript"></script>
	<script src="https://625alex.github.io/ActiON-Dashboard/script.min.js" type="text/javascript"></script>

	<script>var stateTS = ${getStateTS()};</script>
"""
}                                                              

def style() {
"""
* {
	margin: 0;
	padding: 0;
	box-sizing: border-box;
}

.contenedor {
	background: black;
	
	display: -webkit-box;
	display: -moz-box;
	display: -ms-flexbox;
	display: -webkit-flex;
	display: flex;
	
	flex-flow: row wrap;

}

header {
	width: 100%;
	padding: 20px;
	
	display: flex;
	justify-content: flex-start;
	align-items: center;
	flex-flow: row wrap;
}

header .titulo {
	font-size: 30px;
	margin-left:10px;
}

.izquierda{
	margin: 10px;
	overflow-y: auto;
	
	flex: 1 1 15%;
}

.main {
	padding: 10px;
	border: 1px solid gray;
	text-align: center;
	
	flex: 1 1 60%;
}

.derecha{
	margin: 10px;
	overflow-y: auto;
	
	flex: 1 1 15%;
}

footer {
	background: black;
	width: 100%;
	padding: 20px;
	
	display: flex;
	align-items: center;
}

footer img {
	margin-right: 10px;
}

@media screen and (max-width: 800px){
	.contenedor {
		flex-direction: column;
	}
	
	header .titulo {
		font-size: 22px;
	}
	
	.main      { order: 1; }
	.derecha   { order: 2; }
	.izquierda { order: 3; } 
	.footer    { order: 4; }

}

.modal {
    display: none; 
    position: fixed; 
    z-index: 1;
    padding-top: 50px; 
    left: 0;
    top: 0;
    width: 100%; 
    height: 100%; 
    overflow: auto; 
    background-color: rgb(0,0,0); 
    background-color: rgba(0,0,0,0.4); 
}

.modal-content {
    background-color: #fefefe;
    margin: auto;
    padding: 5px;
    border: 1px solid gray;
    width: 90%;
}

.close {
    color: #aaaaaa;
    float: right;
    font-size: 20px;
    font-weight: bold;
	text-align: right;
}

.close:hover, 
.close:focus {
    color: #000;
    text-decoration: none;
    cursor: pointer;
}

.modal footer {
	background: #fefefe;
	font-size: 14px;
}

#cam1, #cam2, #cam3, #cam4 {
    display: none; 
}	

.tile{background-color:grey}

.weather{background-color:#a20025}

.clock{background-color:#607d8b}

.presence{background-color:#3f51b5}

.lock{background-color:#f44336}

.hello-home{background-color:#9c27b0}

.dimmer,.momentary,.switch{background-color:#4caf50}

.contact{background-color:#e91e63}

.refresh{background-color:#607d8b}

.temperature{background-color:#ff6f00}

.mode{background-color:#673ab7}

.motion{background-color:#2196f3}

.humidity{background-color:#795548}

.link{background-color:#263B44}

.thermostat{background-color:#009688}

.energy{background-color:#558b2f}

.power{background-color:#33691e}

.water{background-color:#006064}

.music{background-color:#a0f}

.battery{background-color:#954469}

.camera{background-color:#cddc39}

.video{background-color:#8E8E8E}

.dimmer .ui-slider-bg.ui-btn-active,.music .ui-slider-bg.ui-btn-active{background-color:grey}

.dimmer.active .ui-slider-bg.ui-btn-active{background-color:#81c784}

.music.active .ui-slider-bg.ui-btn-active{background-color:#ab47bc}

body{background-color:#1d1d1d!important}

.tile{border-radius:2px;color:#fff}

.tiles{margin:2px}

.camera,.clock,.dimmer,.holiday,.link,.lock,.menu,.momentary,.music i,.refresh,.switch,.thermostat{cursor:pointer}

.tiles{overflow:hidden}

.tile{width:120px;height:120px}

.w2{width:240px}.h2{height:240px}

.ui-page,body{background-color:#000!important}

*{text-shadow:none;font-family:Mallanna,Verdana,Arial,Helvetica,sans-serif}

.ui-overlay-a{text-shadow:none}

.ui-li-static.ui-body-inherit{text-align:center;cursor:pointer}

.ui-slider-track.ui-corner-all{border-radius:1em}

.ui-shadow-inset{box-shadow:none!important}

.ui-controlgroup,fieldset.ui-controlgroup{margin:0}

.ui-controlgroup-controls{margin-bottom:10px}

.ui-page-theme-a .ui-btn,html body .spinHeat .ui-group-theme-a .ui-btn{border:0;background-color:orange}

.ui-page-theme-a .ui-btn,html body .spinCold .ui-group-theme-a .ui-btn{border:0;background-color:#add8e6}

.popup{padding:15px;text-align:center}

.spinbox{height:36px!important}

.wtfcloud{background:0 0}

.wtfcloud .icon.cloud{margin-left:-100px;font-size:200px;color:#fff}

.wtfcloud .icon.message{margin-left:-50px;margin-top:-65px;color:grey}

.title{font-size:.9em;line-height:1em;height:2em;overflow:ellipsis;position:absolute;top:5px;left:5px}

.title2{font-size:12px}

.icon{font-size:3em;line-height:3em;width:100%;margin-top:-1.5em;position:absolute;text-align:center;top:50%;white-space:nowrap}

.icon.text{font-size:1.8em;line-height:2em;width:100%;margin-top:-1em}

.icon span.text{font-size:.75em;margin-right:15px}

.footer{bottom:5px;color:#fff;font-size:11px;left:5px;position:absolute;line-height:10px}

.footer.right{right:5px;text-align:right}

.footer i{font-size:16px}

.spinner{bottom:5px;right:5px;color:#fff;font-size:10px;line-height:10px;position:absolute;display:none}

.opaque{opacity:.3}

.mode.Away .icon.text,.mode.Home .icon.text,.mode.Night .icon.text,.mode:not(.Away) .icon.Away,.mode:not(.Home) .icon.Home,.mode:not(.Night) .icon.Night,.tile.active .icon.inactive,.tile:not(.active) .icon.active{display:none}

.icon,.icon *{text-shadow:0 4px 3px rgba(0,0,0,.4),0 8px 13px rgba(0,0,0,.1),0 18px 23px rgba(0,0,0,.1)}

.r45{-moz-transform:rotate(45deg);-webkit-transform:rotate(45deg);-o-transform:rotate(45deg);-ms-transform:rotate(45deg);transform:rotate(45deg)}

.ui-slider-track.ui-mini{height:3px}

.ui-slider-popup{height:12px;width:18px;padding-top:0;padding-bottom:2px;font-size:12px}

div.ui-slider{height:21px}

.ui-slider-track{margin:0!important}

.full-width-slider{position:absolute;bottom:5px;width:100%}

.full-width-slider input{display:none}

.ui-slider-track.ui-mini .ui-slider-handle{height:20px;width:20px;margin:-11px 0 0 -11px}

.ui-slider.ui-mini{padding:0 10px 0 12px}

.ui-slider-handle.ui-btn.ui-shadow{background-color:#fff;box-shadow:0 4px 3px rgba(0,0,0,.4),0 8px 13px rgba(0,0,0,.1),0 18px 23px rgba(0,0,0,.1);-webkit-box-shadow:0 4px 3px rgba(0,0,0,.4),0 8px 13px rgba(0,0,0,.1),0 18px 23px rgba(0,0,0,.1);-moz-box-shadow:0 4px 3px rgba(0,0,0,.4),0 8px 13px rgba(0,0,0,.1),0 18px 23px rgba(0,0,0,.1)}

.batt{font-size:.8px;display:inline-block;width:4.5em;height:10em;border:1em solid #fff;border-width:.5em 1em;box-shadow:0 0 0 .5em #fff;margin:4em 1.5em 1.5em 4px;position:relative;background-color:#fff;background-size:100%}

.batt:before{position:absolute;top:-4.5em;left:0;display:block;background:#fff;width:4.5em;height:1.5em;content:"";border-radius:1em 1em 0 0}

.batt:after{position:absolute;top:0;left:0;display:block;background:0 0;width:4.5em;content:"";height:9.5em}

.music .ui-slider.ui-mini{padding:0 10px 0 40px}

.music .footer{bottom:14px;z-index:100}

.music .full-width-slider{z-inndex:99}

.music.active .play,.music.muted .unmuted,.music:not(.active) .pause,.music:not(.muted) .muted{display:none}

.music i{cursor:pointer}

.holiday{background-color:#11772D}

.holiday.active{background-color:#AB0F0B}

.holiday.active .icon i{color:#11772D}

"""
}

def renderMain() {
"""

	<div class="contenedor">
	
		<header>
			<div class="logo">
				<img src="http://www.cordovaindustrial.com/wp-content/uploads/thegem-logos/logo_28059605826e0bf6c5f6b84ed897e0fa_1x.png" height="42" width="42"> 
			</div>
			<div class="titulo">
				<p id="p1">Panel de control CORDOVA Smart v0.2b</p>
			</div>
		</header>
		
		<aside class="izquierda">
			<h2>Cámaras</h2>
			<button id="Btn_cam0">No mostrar</button>
			<button id="Btn_cam1">Cámara 1</button>
			<button id="Btn_cam2">Cámara 2</button>
			<button id="Btn_cam3">Cámara 3</button>
			<button id="Btn_cam4">Cámara 4</button>
		</aside>

		<article class="main">
			<img id="cam0" src="https://www.geekstips.com/wp-content/uploads/2016/12/sm4.jpg" alt="Error de recepción de la imagen" width="98%">
            <img id="cam1" src="http://192.168.1.93/cgi-bin/sp.cgi?chn=0&quality=0&rate=30&u=admin&p" alt="Error de recepción de la imagen" width="98%">
			<img id="cam2" src="http://192.168.1.93/cgi-bin/sp.cgi?chn=0&quality=1&rate=30&u=admin&p" alt="Error de recepción de la imagen" width="98%">
			<img id="cam3" src="http://192.168.1.93/cgi-bin/sp.cgi?chn=0&quality=2&rate=30&u=admin&p" alt="Error de recepción de la imagen" width="98%">
			<img id="cam4" src="http://192.168.1.93/cgi-bin/sp.cgi?chn=0&quality=3&rate=30&u=admin&p" alt="Error de recepción de la imagen" width="98%">
		</article>
		
		<aside class="derecha">
			<h2>Habitaciones</h2>
			<button id="Btn1">Panel Central</button>
			<button id="Btn2" onclick="hola()">Salón Principal</button>
			<button id="Btn3">Habitación Principal</button>
			<button id="Btn4">Habitación Niña</button>
			<button id="Btn5">Habitación Huéspedes</button>
			<button id="Btn6">Baño 1</button>
			<button id="Btn7">Baño 2</button>
		</aside>

		<footer class="footer">
			 <img src="http://www.cordovaidt.com/CIDT-Dash/logo.png" height="21" width="21">             
			 Copyright &copy; 2017 Centro de Innovación y Desarrollo Tecnológico Córdova | &nbsp;
			 <a href="www.cordovaidt.com">www.cordovaidt.com</a>
		</footer>
		
	</div>
	
	<div id="Modal1" class="modal">
		<div class="modal-content">
			<span class="close">X</span>
			<div>
				Controles inteligentes
			<div>
			<div>
				${renderTiles()}
			<div>
		</div>
		<footer>
			<img src="http://www.cordovaidt.com/wp-content/uploads/thegem-logos/logo_251873f060a7cc482f3e9d7c7e491c1a_1x.png" height="21" width="21">             
			Copyright &copy; 2017 Centro de Innovación y Desarrollo Tecnológico Córdova |  &nbsp;
            <a href="www.cordovaidt.com">www.cordovaidt.com</a>
		</footer>
	</div>
    
"""
}

def script() {
"""

	// Panel Central
	var modal1 = document.getElementById('Modal1');
	var btn1 = document.getElementById("Btn1");
	var span1 = document.getElementsByClassName("close")[0];
	btn1.onclick = function() {
		modal1.style.display = "block";
	}
	span1.onclick = function() {
		modal1.style.display = "none";
	}
	window.onclick = function(event) {
		if (event.target == modal1) {
			modal1.style.display = "none";
		}
	}
	
	// Cámaras
	document.getElementById("Btn_cam0").onclick = function() {
		document.getElementById("cam0").style.display = "block";
		document.getElementById("cam1").style.display = "none";
		document.getElementById("cam2").style.display = "none";
		document.getElementById("cam3").style.display = "none";
		document.getElementById("cam4").style.display = "none";		
	}
	
	document.getElementById("Btn_cam1").onclick = function() {
		document.getElementById("cam0").style.display = "none";
		document.getElementById("cam1").style.display = "block";
		document.getElementById("cam2").style.display = "none";
		document.getElementById("cam3").style.display = "none";
		document.getElementById("cam4").style.display = "none";		
	}
	
	document.getElementById("Btn_cam2").onclick = function() {
		document.getElementById("cam0").style.display = "none";
		document.getElementById("cam1").style.display = "none";
		document.getElementById("cam2").style.display = "block";
		document.getElementById("cam3").style.display = "none";
		document.getElementById("cam4").style.display = "none";		
	}
	
	document.getElementById("Btn_cam3").onclick = function() {
		document.getElementById("cam0").style.display = "none";
		document.getElementById("cam1").style.display = "none";
		document.getElementById("cam2").style.display = "none";
		document.getElementById("cam3").style.display = "block";
		document.getElementById("cam4").style.display = "none";		
	}
	
	document.getElementById("Btn_cam4").onclick = function() {
		document.getElementById("cam0").style.display = "none";
		document.getElementById("cam1").style.display = "none";
		document.getElementById("cam2").style.display = "none";
		document.getElementById("cam3").style.display = "none";
		document.getElementById("cam4").style.display = "block";		
	}
    
    var v = document.getElementsByClassName("valve");
    
    for (i = 0; i < v.length; i++) {
        v[i].onclick = function() {
        	animateClick(v[i]);
            cercano(v[i], ".tile").classList.toggle("active");
            sendCommand(v[i].attr("data-type"), v[i].attr("data-device"), "toggle")
        }
        setIconos(v[i]);
    }
    
    function setIconos(elemento) {
    	elemento.innerHTML += "<div class='icon inactive'><i class='fa fa-toggle-off'></i></div>";
    	elemento.innerHTML += "<div class='icon active'><i class='fa fa-toggle-on'></i></div>";
    }
    
    function cercano(elemento, arg) {
      return elemento.match(arg) ? elemento : elemento.up(arg);
    }
    
"""
}

def renderTiles() {
"""
<div class="tiles">
	${allDeviceData()?.collect{renderTile(it)}.join("\n")}
</div>
"""
}

def renderWTFCloud() {
"""
<div data-role="popup" id="wtfcloud-popup" data-overlay-theme="b" class="wtfcloud">
    <div class="icon cloud" onclick="clearWTFCloud()">
    	<i class="fa fa-cloud"></i>
    </div>
    <div class="icon message" onclick="clearWTFCloud()">
        <i class="fa fa-question"></i>
        <i class="fa fa-exclamation"></i>
        <i class='fa fa-refresh'></i>
    </div>
</div>
"""
}