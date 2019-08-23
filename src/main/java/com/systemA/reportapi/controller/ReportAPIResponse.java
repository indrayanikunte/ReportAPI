package com.systemA.reportapi.controller;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ReportAPIResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5545089582586405690L;
	
	
	private int statusCode;
	private String message;
	
}
