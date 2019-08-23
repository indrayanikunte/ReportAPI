package com.systemA.reportapi.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class ProductDTO {

	private String productGroupCode;
	private String exchangeCode;
	private String symbol;
	private String expirationDate;
	
	@EqualsAndHashCode.Exclude
	private Long totalTransactionAmount;
	
	public void setTotalTransactionAmount(Long totalTransactionAmount) {
		this.totalTransactionAmount = totalTransactionAmount;
	}
	
	
}
