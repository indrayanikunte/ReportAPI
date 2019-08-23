package com.systemA.reportapi.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.systemA.reportapi.dto.ClientDTO;
import com.systemA.reportapi.dto.ProductDTO;
import com.systemA.reportapi.exception.ReportInputException;
import com.systemA.reportapi.exception.ReportOutputException;

@Service
public class ReportServiceImpl implements ReportService {

	@Value( "${input.file.name}" )
	private String inputFileName;
	
	@Value( "${output.file.name}" )
	private String outputFileName;
	
	Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);
	
	@Override
	public Map<ClientDTO,List<ProductDTO>> getSummaryReportData() throws ReportInputException {

		logger.info("Creating summary report data");
		Map<ClientDTO,List<ProductDTO>> reportData = new HashMap<ClientDTO,List<ProductDTO>>();

		/**read file*/
		logger.debug("Reading data for summary report from file:"+inputFileName);
		Resource resource = new ClassPathResource(inputFileName);
		try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {

			String line = "";
			while ((line = br.readLine()) != null) 
			{
				/**get client and product details*/
				 ClientDTO clientDTO = getClientDTO(line);
				 ProductDTO productDTO = getProductDTO(line);
				 
				 /** check if the client details already present in the report data map, 
				  * if yes, then update product list accordingly with transaction amount 
				  * if no, then create a new product list
				  */
				 if(reportData.containsKey(clientDTO)) {
					 
					 List<ProductDTO> productDTOs = reportData.get(clientDTO);
					 updateProductDTOList(productDTOs, productDTO);
					 
				 }else {
					 List<ProductDTO> productDTOs = new ArrayList<ProductDTO>();
					 productDTOs.add(productDTO);
					 reportData.put(clientDTO,productDTOs);
				 }
			}
			
			logger.info("Summary report data created successfully.");
		} catch (FileNotFoundException e) {
			logger.error("Error while creating summary report data,error message:"+e.getMessage()+",error stack trace:"+e.getStackTrace());
			throw new ReportInputException("Input file is not available", e);

		} catch (Exception e) {
			logger.error("Error while creating summary report data,error message:"+e.getMessage()+",error stack trace:"+e.getStackTrace());
			throw new ReportInputException("Error while reading input file", e);
		}
		
		return reportData;
	}

	@Override
	public void generateReport(HttpServletResponse response, Map<ClientDTO,List<ProductDTO>> reportData) throws ReportOutputException {
		
		logger.info("Generating report with output file name:"+outputFileName);
		
		/**check if report data map is not empty*/
		if(reportData==null || reportData.size()==0) {
			logger.error("Error while generating report,error message:Data is not available to generate report");
			throw new ReportOutputException("Data is not available to generate report",new Exception("Data is not available to generate report"));
		}
		
		/***create response header*/
		response.setContentType("text/csv");
		response.setHeader("Content-disposition", "attachment;filename="+ outputFileName);
		
		/**write csv header*/
		String header = "Client Information,Product Information,Total Transaction Amount";
		writeCSVRecord(header, response);
		
		/**write csv data**/
		for(Entry<ClientDTO, List<ProductDTO>> data : reportData.entrySet()) {
			
			StringBuilder clientInfoSB = new StringBuilder();
			
			/**
			 * adding client information, each detail separated by space
			 */
			ClientDTO clientInfo = data.getKey();
			clientInfoSB.append(clientInfo.getClientType()).append(" ")
					.append(clientInfo.getClientNumber()).append(" ")
					.append(clientInfo.getAccountNumber()).append(" ")
					.append(clientInfo.getSubAccountNumber()).append(",");
			
			/*** product information*/
			for(ProductDTO productInfo : data.getValue()) {
				
				StringBuilder record = new StringBuilder();
				record.append(clientInfoSB.toString());
				
				/**append product information and transaction amount to client information*/
				record.append(productInfo.getExchangeCode()).append(" ")
						.append(productInfo.getProductGroupCode()).append(" ")
						.append(productInfo.getSymbol()).append(" ")
						.append(productInfo.getExpirationDate()).append(",")
						.append(productInfo.getTotalTransactionAmount());
				
				/**write record to csv file*/
				writeCSVRecord(record.toString(), response);
			}
		}
		
		try {
			response.getOutputStream().flush();
		} catch (IOException e) {
			logger.error("Error while generating report,error message:"+e.getMessage()+",error stack trace:"+e.getStackTrace());
			throw new ReportOutputException("Error while generating report.",e);
		}	

		logger.info("Report generated successfully with file name:"+outputFileName);
	}

	//------------private methods---------------------------
	
	
	private void writeCSVRecord(String record,HttpServletResponse response) throws ReportOutputException {
		try {
			response.getOutputStream().print(record);
			response.getOutputStream().print("\n");
		} catch (IOException e) {
			logger.error("Error while generating report,error message:"+e.getMessage()+",error stack trace:"+e.getStackTrace());
			throw new ReportOutputException("Error while generating report.",e);
		}
	}
	
	private ClientDTO getClientDTO(String line) {
		
		String clientType = StringUtils.trimWhitespace(line.substring(3, 7));
		String clientNumber = StringUtils.trimWhitespace(line.substring(7, 11));
		String accountNumber = StringUtils.trimWhitespace(line.substring(11, 15));
		String subAccountNumber = StringUtils.trimWhitespace(line.substring(15, 19));
		
		ClientDTO clientDTO = new ClientDTO(clientType, clientNumber, accountNumber, subAccountNumber);
		return clientDTO;
	}
	
	private ProductDTO getProductDTO(String line) {
	
		String productGroupCode = StringUtils.trimWhitespace(line.substring(25, 27));
		String exchangeCode = StringUtils.trimWhitespace(line.substring(27, 31));
		String symbol = StringUtils.trimWhitespace(line.substring(31, 37));
		String expirationDate = StringUtils.trimWhitespace(line.substring(37, 45));
		Long totalTransactionAmount = getTrasactionAmount(line);

		ProductDTO productDTO = new ProductDTO(productGroupCode, exchangeCode, symbol, expirationDate, totalTransactionAmount);
		return productDTO;
	}
	
	private Long getTrasactionAmount(String line) {
		String quantityLong = StringUtils.trimWhitespace(line.substring(52, 62));
		String quantityShort =  StringUtils.trimWhitespace(line.substring(63, 73));
		
		quantityLong = StringUtils.isEmpty(quantityLong) ? "0" : quantityLong;
		quantityShort = StringUtils.isEmpty(quantityShort) ? "0" : quantityShort;
		
		Long transactionAmount = new Long(quantityLong) - new Long(quantityShort);
		return transactionAmount;
	}
	
	private void updateProductDTOList(List<ProductDTO> productDTOs,ProductDTO newProductDTO) {
		
		ProductDTO existingProductDTO = productDTOs.stream()
										.filter(productDTO -> productDTO.equals(newProductDTO))
										.findAny()
										.orElse(null);
		
		if(existingProductDTO==null) {
			productDTOs.add(newProductDTO);
		}else {
			Long totalTransactionAmount = existingProductDTO.getTotalTransactionAmount() + newProductDTO.getTotalTransactionAmount();
			existingProductDTO.setTotalTransactionAmount(totalTransactionAmount);
		}
	}
	
}
