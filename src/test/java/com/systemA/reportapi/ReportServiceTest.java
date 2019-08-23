package com.systemA.reportapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import com.systemA.reportapi.dto.ClientDTO;
import com.systemA.reportapi.dto.ProductDTO;
import com.systemA.reportapi.exception.ReportInputException;
import com.systemA.reportapi.service.ReportService;
import com.systemA.reportapi.service.ReportServiceImpl;

public class ReportServiceTest {

	@Mock
	private ReportService reportService = new ReportServiceImpl();
	
	@Test
	public void test_should_return_map_of_size_2() {
		try {
			ReflectionTestUtils.setField(reportService, "inputFileName", "InputTest.txt");
			Map<ClientDTO, List<ProductDTO>> reportData = reportService.getSummaryReportData();
			assertNotNull(reportData);
			assertEquals(2,reportData.size());
		} catch (ReportInputException e) {
			assertFalse("unexpected error",true);
		}
	}

	@Test
	public void test_check_map_content_one_client_one_product() {
		try {
			ReflectionTestUtils.setField(reportService, "inputFileName", "InputTest_OneClientOneProduct.txt");
			Map<ClientDTO, List<ProductDTO>> reportData = reportService.getSummaryReportData();
			assertNotNull(reportData);
			assertEquals(1,reportData.size());
			
			//check client details 
			ClientDTO clientDTO = reportData.keySet().stream().findFirst().get();
			
			assertNotNull(clientDTO);
			assertEquals("client type not matching","TYPA",clientDTO.getClientType());
			assertEquals("client number not matching","6666",clientDTO.getClientNumber());
			assertEquals("account number not matching","9999",clientDTO.getAccountNumber());
			assertEquals("sub account number not matching","8888",clientDTO.getSubAccountNumber());
			
			//check product details
			assertNotNull(reportData.get(clientDTO));
			assertEquals("prodcut list size should be 1",1, reportData.get(clientDTO).size());
			ProductDTO productDTO = reportData.get(clientDTO).stream().findFirst().get();
			
			assertEquals("product group code not matching", "PR",productDTO.getProductGroupCode());
			assertEquals("exchange code not matching", "exch",productDTO.getExchangeCode());
			assertEquals("symbol not matching", "SYMBOL",productDTO.getSymbol());
			assertEquals("expiration date not matching", "20100910",productDTO.getExpirationDate());
			
			assertEquals("total transaction amount is not as per expectation",new Long(34) ,productDTO.getTotalTransactionAmount());
		} catch (ReportInputException e) {
			assertFalse("unexpected error",true);
		}
	}
	
	@Test
	public void test_check_map_content_one_client_two_products() {
		try {
			ReflectionTestUtils.setField(reportService, "inputFileName", "InputTest_OneClientTwoProducts.txt");
			Map<ClientDTO, List<ProductDTO>> reportData = reportService.getSummaryReportData();
			assertNotNull(reportData);
			assertEquals(1,reportData.size());
			
			//check client details 
			ClientDTO clientDTO = reportData.keySet().stream().findFirst().get();
			
			assertNotNull(clientDTO);
			assertEquals("client type not matching","TYPA",clientDTO.getClientType());
			assertEquals("client number not matching","6666",clientDTO.getClientNumber());
			assertEquals("account number not matching","9999",clientDTO.getAccountNumber());
			assertEquals("sub account number not matching","8888",clientDTO.getSubAccountNumber());
			
			//check product details
			assertNotNull(reportData.get(clientDTO));
			assertEquals("prodcut list size should be 2",2, reportData.get(clientDTO).size());
			
			ProductDTO productDTO = reportData.get(clientDTO).get(0);
			assertEquals("product group code not matching", "PR",productDTO.getProductGroupCode());
			assertEquals("exchange code not matching", "exch",productDTO.getExchangeCode());
			assertEquals("symbol not matching", "SYMBOL",productDTO.getSymbol());
			assertEquals("expiration date not matching", "20100910",productDTO.getExpirationDate());
			assertEquals("total transaction amount is not as per expectation",new Long(6) ,productDTO.getTotalTransactionAmount());
			
			productDTO = reportData.get(clientDTO).get(1);
			assertEquals("product group code not matching", "ZR",productDTO.getProductGroupCode());
			assertEquals("exchange code not matching", "zxch",productDTO.getExchangeCode());
			assertEquals("symbol not matching", "ZYMBOL",productDTO.getSymbol());
			assertEquals("expiration date not matching", "20100910",productDTO.getExpirationDate());
			assertEquals("total transaction amount is not as per expectation",new Long(28) ,productDTO.getTotalTransactionAmount());
			
		} catch (ReportInputException e) {
			assertFalse("unexpected error",true);
		}
	}
	
	@Test
	public void test_check_map_content_two_clients_same_product() {
		try {
			ReflectionTestUtils.setField(reportService, "inputFileName", "InputTest_TwoClientsSameProduct.txt");
			Map<ClientDTO, List<ProductDTO>> reportData = reportService.getSummaryReportData();
			assertNotNull(reportData);
			assertEquals("report data map size not matching",2,reportData.size());
			
			//check client details 
			for (ClientDTO clientDTO : reportData.keySet()) {

				assertNotNull(clientDTO);
				
				if(clientDTO.getClientType().equals("TYPA")) {
					checkClientA(clientDTO, reportData);
				}else if(clientDTO.getClientType().equals("TYPB")) {
					checkClientB(clientDTO, reportData);
				}else {
					assertFalse("unexpected client type",true);
				}
				
			}
		} catch (ReportInputException e) {
			assertFalse("unexpected error",true);
		}
	}
	
	@Test
	public void test_check_map_content_three_clients() {
		try {
			ReflectionTestUtils.setField(reportService, "inputFileName", "InputTest_TwoClientsSameProduct.txt");
			Map<ClientDTO, List<ProductDTO>> reportData = reportService.getSummaryReportData();
			assertNotNull(reportData);
			assertEquals("report data map size not matching",2,reportData.size());
			
			//check client details 
			for (ClientDTO clientDTO : reportData.keySet()) {

				assertNotNull(clientDTO);
				
				if(clientDTO.getClientType().equals("TYPA")) {
					checkClientA(clientDTO, reportData);
				}else if(clientDTO.getClientType().equals("TYPB")) {
					checkClientB(clientDTO, reportData);
				}else if(clientDTO.getClientType().equals("TYPC")) {
					checkClientC(clientDTO, reportData);
				}
				else {
					assertFalse("unexpected client type",true);
				}
				
			}
		} catch (ReportInputException e) {
			assertFalse("unexpected error",true);
		}
	}
	
	@Test
	public void test_file_not_foundException() {
		try {
			ReflectionTestUtils.setField(reportService, "inputFileName", "InputTest123.txt");
			reportService.getSummaryReportData();
			assertFalse("control should not reach this line,it should have gone to exception block",true);
		} catch (ReportInputException e) {
			assertEquals("exception message not matching","Input file is not available",e.getMessage());
		} catch (Exception e) {
			assertFalse("expecting exception of type ReportInputException",true);
		}
	}
	
	@Test
	public void test_file_reading_error() {
		try {
			ReflectionTestUtils.setField(reportService, "inputFileName", "InputTest_InvalidData.txt");
			reportService.getSummaryReportData();
			assertFalse("control should not reach this line,it should have gone to exception block",true);
		} catch (ReportInputException e) {
			assertEquals("exception message not matching","Error while reading input file",e.getMessage());
		} catch (Exception e) {
			assertFalse("expecting exception of type ReportInputException",true);
		}
	}

	/**-----------------private methods  ----------------------------------------**/
	
	private void checkClientA(ClientDTO clientDTO,Map<ClientDTO, List<ProductDTO>> reportData) {
		assertEquals("client type not matching", "TYPA", clientDTO.getClientType());
		assertEquals("client number not matching", "6666", clientDTO.getClientNumber());
		assertEquals("account number not matching", "9999", clientDTO.getAccountNumber());
		assertEquals("sub account number not matching", "8888", clientDTO.getSubAccountNumber());

		// check product details
		assertNotNull(reportData.get(clientDTO));
		assertEquals("prodcut list size should be 1", 1, reportData.get(clientDTO).size());
		ProductDTO productDTO = reportData.get(clientDTO).stream().findFirst().get();

		assertEquals("product group code not matching", "PR", productDTO.getProductGroupCode());
		assertEquals("exchange code not matching", "exch", productDTO.getExchangeCode());
		assertEquals("symbol not matching", "SYMBOL", productDTO.getSymbol());
		assertEquals("expiration date not matching", "20100910", productDTO.getExpirationDate());

		assertEquals("total transaction amount is not as per expectation", new Long(34),productDTO.getTotalTransactionAmount());
	}
	
	private void checkClientB(ClientDTO clientDTO,Map<ClientDTO, List<ProductDTO>> reportData) {
		assertEquals("client type not matching", "TYPB", clientDTO.getClientType());
		assertEquals("client number not matching", "6666", clientDTO.getClientNumber());
		assertEquals("account number not matching", "9999", clientDTO.getAccountNumber());
		assertEquals("sub account number not matching", "8888", clientDTO.getSubAccountNumber());

		// check product details
		assertNotNull(reportData.get(clientDTO));
		assertEquals("prodcut list size should be 1", 1, reportData.get(clientDTO).size());
		ProductDTO productDTO = reportData.get(clientDTO).stream().findFirst().get();

		assertEquals("product group code not matching", "PR", productDTO.getProductGroupCode());
		assertEquals("exchange code not matching", "exch", productDTO.getExchangeCode());
		assertEquals("symbol not matching", "SYMBOL", productDTO.getSymbol());
		assertEquals("expiration date not matching", "20100910", productDTO.getExpirationDate());

		assertEquals("total transaction amount is not as per expectation", new Long(5),productDTO.getTotalTransactionAmount());
	}
	
	private void checkClientC(ClientDTO clientDTO,Map<ClientDTO, List<ProductDTO>> reportData) {
		assertEquals("client type not matching", "TYPC", clientDTO.getClientType());
		assertEquals("client number not matching", "6666", clientDTO.getClientNumber());
		assertEquals("account number not matching", "7777", clientDTO.getAccountNumber());
		assertEquals("sub account number not matching", "8888", clientDTO.getSubAccountNumber());

		// check product details
		assertNotNull(reportData.get(clientDTO));
		assertEquals("prodcut list size should be 1", 1, reportData.get(clientDTO).size());
		ProductDTO productDTO = reportData.get(clientDTO).stream().findFirst().get();

		assertEquals("product group code not matching", "ZR", productDTO.getProductGroupCode());
		assertEquals("exchange code not matching", "exch", productDTO.getExchangeCode());
		assertEquals("symbol not matching", "SYMBOL", productDTO.getSymbol());
		assertEquals("expiration date not matching", "20100910", productDTO.getExpirationDate());

		assertEquals("total transaction amount is not as per expectation", new Long(4),productDTO.getTotalTransactionAmount());
	}
}

