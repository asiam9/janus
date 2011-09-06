package com.em.janus.model.output;

public enum OutputType {
	SMART,		//ajax, web 2.0, modern browsers
	MOBILE,		//intended for mobile browsers
	XML,		//pure xml feed
	FEED, 		//opds compliant feed
	HTML,		//pure, simple, html (simple readers)
	JSON;		//pure json return (restful integration)
}
