package com.jawa83.domotica.dobiss.core.domotica.model;

enum ActionType {
	OFF("00"),
	ON("01"),
	TOGGLE("02");
	
	String hexPresentation;
	
	ActionType(String hexPresentation) {
		this.hexPresentation = hexPresentation;
	}
}
