package com.systemA.reportapi.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
public class ReportOutputException extends Exception {
	private String message;
	
	public ReportOutputException(String messageEx, Exception e) {
		this.message = messageEx;
		this.setStackTrace(e.getStackTrace());
	}


}
