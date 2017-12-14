jQuery(function() {
	//jQuery(".switch, .dimmer, .lock, .menu, .momentary, .link, .holiday, .camera, .music, .thermostat, .refresh").append("<i class='spinner fa fa-refresh fa-spin'></i>");
	jQuery(".tile").append("<i class='spinner fa fa-refresh fa-spin'></i>");
	
	setIcons();
	
	jQuery(".switch, .dimmer, .momentary, .clock, .lock, .link, .holiday, .camera").click(function() {
		animateClick(jQuery(this));
	});
	
	jQuery(".switch, .lock, .momentary, .music .play, .music .pause, .holiday, .camera").click(function() {
		jQuery(this).closest(".tile").toggleClass("active");
        sendCommand(jQuery(this).attr("data-type"), jQuery(this).attr("data-device"), "toggle");
	});
                
    jQuery(".dimmer").on('slidestop', function(e) {
    	var level = jQuery(this).find("input").val()
		if (jQuery(this).hasClass("active")) {
			animateClick(jQuery(this));
			sendCommand("dimmer", jQuery(this).attr("data-device"), "level", level);
		};
		jQuery(this).attr("data-level", level);
    });
    
    jQuery(".dimmer").click(function() {
		jQuery(this).toggleClass("active");
    	sendCommand("dimmer", jQuery(this).attr("data-device"), "toggle", jQuery(this).attr("data-level"));
    });
	
	jQuery(".mode, .hello-home, .thermostat").click(function() {
		jQuery("#" + jQuery(this).attr("data-popup")).popup("open");
    });
    
    jQuery(".refresh, .clock").click(function() {
        refresh();
	});
    
    jQuery("#mode-popup li").click(function() {
    	jQuery("#mode-popup").popup("close");
		var tile = jQuery(".mode");
    	animateClick(tile);
        var newMode = jQuery(this).text();
		sendCommand("mode", "mode", newMode);
        
		var oldMode = jQuery(".mode").attr("data-mode");
		tile.removeClass(oldMode);
		tile.attr("data-mode", newMode);
		if (["Home", "Away", "Night"].indexOf(newMode) >= 0) {
			jQuery("#mode-name").hide();
			tile.addClass(newMode);
		} else {
			jQuery("#mode-name").html(newMode).show();
		}
    });
    
    jQuery("#hello-home-popup li").on("click", function() {
    	jQuery("#hello-home-popup").popup("close");
		animateClick(jQuery(".hello-home"));
		sendCommand("helloHome", "helloHome", jQuery(this).text());
    });
	
	startTime();
	
	jQuery(".wtfcloud").click(function(){
		jQuery("#wtfcloud-popup").popup("open");
	});
});

var fadeOn = 100;
var fadeOff = 200;

function animateClick(element, toggle) {
	spinner(element);
	element.animate({opacity: 0.3}, fadeOff, "swing").delay(fadeOn).animate({opacity: 1}, fadeOn, "swing");
}

function spinner(element) {
	element.closest(".tile").find(".spinner").fadeIn("slow").delay(2000).fadeOut("slow");
}

function setIcons() {
	jQuery(".switch, .dimmer").append("<div class='icon inactive'><i class='fa fa-toggle-off'></i></div>").append("<div class='icon active'><i class='fa fa-toggle-on'></i></div>");
	
	jQuery(".lock").append("<div class='icon inactive'><i class='fa fa-lock'></i></div>").append("<div class='icon active'><i class='fa fa-unlock-alt'></i></div>");
	
	jQuery(".motion").append("<div class='icon inactive'><i class='fa opaque fa-exchange'></i></div>").append("<div class='icon active'><i class='fa fa-exchange'></i></div>");
	
	jQuery(".presence").append("<div class='icon inactive'><i class='fa opaque fa-map-marker'></i></div>").append("<div class='icon active'><i class='fa fa-map-marker'></i></div>");
	
	jQuery(".contact").append("<div class='icon inactive'><i class='r45 fa fa-compress'></i></div>").append("<div class='icon active'><i class='r45 fa fa-expand'></i></div>");
	jQuery(".water").append("<div class='icon inactive'><i class='fa opaque fa-tint'></i></div>").append("<div class='icon active'><i class='fa fa-tint'></i></div>");
	
	jQuery(".momentary").append("<div class='icon'><i class='fa fa-circle-o'></i></div>");
	jQuery(".camera").append("<div class='icon'><i class='fa fa-camera'></i></div>");
	jQuery(".holiday").append("<div class='icon'><i class='fa fa-tree'></i></div>");
	jQuery(".refresh").append("<div class='icon'><i class='fa fa-refresh'></i></div>");

	jQuery(".dimmer").each(function(){renderSlider(jQuery(this), 1)});
	jQuery(".music").each(function(){renderSlider(jQuery(this), 0)});
	jQuery(".weather").each(function(){renderWeather(jQuery(this))});
	
	jQuery(".humidity").append("<div class='footer'><i class='fa fa-fw wi wi-sprinkles'></i></div>");
	jQuery(".temperature").append("<div class='footer'><i class='fa fa-fw wi wi-thermometer'></i></div>");
	jQuery(".energy").append("<div class='footer'><i class='fa fa-fw wi wi-lightning'></i></div>");
	jQuery(".power").append("<div class='footer'><i class='fa fa-fw fa-bolt'></i></div>");
	jQuery(".battery").append("<div class='footer'><span class='batt'></span></div>");
	
	jQuery(".tile[data-is-value=true]").each(function(){renderValue(jQuery(this))});
}

function renderSlider(tile, min) {
	tile.find(".slider-container").remove();
	tile.append("<div class='slider-container'><div class='full-width-slider'><input value='" + tile.attr("data-level") + "' min='" + min + "' max='10' type='range' step='1' data-mini='true' data-popup-enabled='true' data-disabled='false' data-highlight='true'></div></div>").find("input").slider()
	jQuery(".full-width-slider").click(function(e) {e.stopImmediatePropagation();});
}

function renderValue(tile) {
	tile.find(".icon").remove();
	tile.append("<div class='icon text'>" + tile.attr("data-value") + "</div>");
}

function renderWeather(tile) {
	var data = JSON.parse(tile.attr("data-weather"));
	tile.empty();
	var content = "<div class='title'>" + data.city + "<br/><span class='title2'>" + data.weather + ", feels like " + data.feelsLike + "&deg;</span></div>\n\
<div class='icon'><span class='text'>" + data.temperature + "&deg;</span><i class='wi " + data.icon + "'></i></span></div>\n\
<div class='footer'>" + data.localSunrise + " <i class='fa fa-fw wi wi-horizon-alt'></i> " + data.localSunset + "</div>\n\
<div class='footer right'>" + data.percentPrecip + "%<i class='fa fa-fw fa-umbrella'></i><br>" + data.humidity + "%<i class='fa fa-fw wi wi-sprinkles'></i></div>";
	tile.html(content);
}

function sendCommand(type, device, command, value) {
	//alert("&type=" + type + "&device=" + device + "&command=" + command + "&value=" + value);
	
	var access_token = getUrlParameter("access_token");
	var request = { type: type, device: device, command: command, value: value};
	if (access_token) request["access_token"] = access_token;
	
	jQuery.get("command", request).done(function( data ) {
		//alert( "Data Loaded: " + data);
		if (data.status == "ok") {
			nextPoll(5);
		}
	}).fail(function() {
		setWTFCloud();
		nextPoll(10);
	});
}

function doPoll(func) {
	nextPoll(20);
	if (!func) spinner(jQuery(".refresh"));
	var access_token = getUrlParameter("access_token");
	var request = {ts:stateTS};
	if (access_token) request["access_token"] = access_token;
	
	jQuery.get("ping", request).done(function( data ) {
		clearWTFCloud();
		if (func) {
			func();
		} else {
			stateTS = data.ts;
			jQuery(".refresh .footer").html("Updated " + data.updated);
			if (data.status == "update") updateTiles(data.data);
		}
	}).fail(function() {
		setWTFCloud();
	});
}

function updateTiles(data) {for (i in data) updateTile(data[i]);}

function updateTile(data) {
	if (data.tile == "device") {
		var tile = jQuery("." + data.type + "[data-device=" + data.device + "]");
	
		if (data.tile == "weather") {
			tile.attr("data-weather", JSON.stringify(d));
			renderWeather(tile);
		} else {
			if (data.value != tile.attr("data-value")) spinner(tile);
			tile.attr("data-value", data.value);
			
			if (data.isValue){
				renderValue(tile);
			} else {
				tile.removeClass("inactive active").addClass(data.active);
				tile.attr("data-active", data.active);
			}
			
			if (data.type == "dimmer") {
				if (data.level != tile.attr("data-level")) spinner(tile);
				tile.attr("data-level", data.level);
				renderSlider(tile, 1);
			} else if  (data.type == "music") {
				renderSlider(tile, 0);
			}
		}
	} else if (data.tile == "mode") {
		var tile = jQuery(".mode");
		if (data.mode != tile.attr("data-mode")) spinner(tile);
		tile.removeClass(tile.attr("data-mode"));
		tile.attr("data-mode", data.mode);
		if (data.isStandardMode) tile.addClass(data.mode);
		jQuery(".mode-name").html(data.mode);
	}
}

var polling;
var wtfCloud = false;

function setWTFCloud() {
	wtfCloud = true;
	jQuery("#wtfcloud-popup").popup("open");
}

function clearWTFCloud() {
	wtfCloud = false;
	jQuery("#wtfcloud-popup").popup("close");
}

function nextPoll(timeout) {
	if (polling) clearInterval(polling)
	polling = setInterval(function () {doPoll()}, timeout * 1000);
}

nextPoll(30);

function refresh(timeout) {
	if (!timeout) {
		setTimeout(function() { doRefresh() }, 100);
	} else {
		setTimeout(function() { doRefresh() }, timeout * 1000);
	}
}

function doRefresh() {
	doPoll(function func(){
		jQuery(".refresh .icon").addClass("fa-spin");
		location.reload();
	});
}

refresh(60 * 60); // hard refresh every hour

function getUrlParameter(sParam)
{
    var sPageURL = window.location.search.substring(1);
    var sURLVariables = sPageURL.split('&');
    for (var i = 0; i < sURLVariables.length; i++) 
    {
        var sParameterName = sURLVariables[i].split('=');
        if (sParameterName[0] == sParam) 
        {
            return sParameterName[1];
        }
    }
}

CoolClock.config.skins = { 
st: {
	outerBorder:      { lineWidth: 12, radius: 100, color: "yellow", alpha: 0 },
		smallIndicator:   { lineWidth: 16, startAt: 80, endAt: 85, color: "white", alpha: 1 },
		largeIndicator:   { lineWidth: 2, startAt: 80, endAt: 85, color: "white", alpha: 1 },
		hourHand:         { lineWidth: 8, startAt: 0, endAt: 60, color: "white", alpha: 1 },
		minuteHand:       { lineWidth: 6, startAt: 0, endAt: 75, color: "white", alpha: 1 },
		secondHand:       { lineWidth: 5, startAt: 80, endAt: 85, color: "red", alpha: 0 },
		secondDecoration: { lineWidth: 3, startAt: 96, radius: 4, fillColor: "white", color: "black", alpha: 1 }
},
st1: {
outerBorder: { lineWidth: 2, radius: 80, color: "white", alpha: 0 },
smallIndicator: { lineWidth: 5, startAt: 88, endAt: 94, color: "yellow", alpha: 0 },
largeIndicator: { lineWidth: 5, startAt: 90, endAt: 94, color: "white", alpha: 1 },
hourHand: { lineWidth: 8, startAt: 0, endAt: 60, color: "white", alpha: 1 },
minuteHand: { lineWidth: 8, startAt: 0, endAt: 80, color: "white", alpha: 1 },
secondHand: { lineWidth: 5, startAt: 89, endAt: 94, color: "white", alpha: 1 },
secondDecoration: { lineWidth: 3, startAt: 0, radius: 4, fillColor: "black", color: "black", alpha: 0 }
}
}

function startTime() {
    if (!document.getElementById('clock')) return;
    var today=new Date();
    var h=today.getHours();
    if (h > 12) {
    	h = h - 12;
    }
    var m=today.getMinutes();
    var s=today.getSeconds();
    m = checkTime(m);
    s = checkTime(s);
    document.getElementById('clock').innerHTML = h+":"+m;
    setTimeout(function(){startTime()},500);
}

function checkTime(i) {
    if (i<10) {i = "0" + i};  // add zero in front of numbers < 10
    return i;
}

var cellSize = getUrlParameter("t") || 120;
var cellGutter = getUrlParameter("g") || 4;

jQuery(function() {
  var wall = new freewall(".tiles");
  wall.fitWidth();
  
  wall.reset({
			draggable: false,
			selector: '.tile',
		animate: true,
		gutterX:cellGutter,
		gutterY:cellGutter,
		cellW:cellSize,
		cellH:cellSize,
		fixSize:null,
		onResize: function() {
			wall.fitWidth();
			wall.refresh();
		}
	});
	wall.fitWidth();
	// for scroll bar appear;
	jQuery(window).trigger("resize");
});
