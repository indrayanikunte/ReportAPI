package com.systemA.reportapi.exception;

import lombok.Getter;
import lombok.Setter;


@Getter
public class ReportInputException extends Exception {

	private String message;
	public ReportInputException(String messageEx, Exception e) {
		this.message = messageEx;
		this.setStackTrace(e.getStackTrace());
	}

	
}
