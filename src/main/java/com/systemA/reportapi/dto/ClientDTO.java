package com.systemA.reportapi.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class ClientDTO {
	
	private String clientType;
	private String clientNumber;
	private String accountNumber;
	private String subAccountNumber;

	
}
