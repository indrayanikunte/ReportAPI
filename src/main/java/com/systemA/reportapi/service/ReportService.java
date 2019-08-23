package com.systemA.reportapi.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.systemA.reportapi.dto.ClientDTO;
import com.systemA.reportapi.dto.ProductDTO;
import com.systemA.reportapi.exception.ReportInputException;
import com.systemA.reportapi.exception.ReportOutputException;


public interface ReportService {

	Map<ClientDTO,List<ProductDTO>> getSummaryReportData() throws ReportInputException ;
	void generateReport(HttpServletResponse response,Map<ClientDTO,List<ProductDTO>> reportData) throws ReportOutputException;
}
