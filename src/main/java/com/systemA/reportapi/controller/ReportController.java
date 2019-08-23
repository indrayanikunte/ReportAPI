package com.systemA.reportapi.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.systemA.reportapi.dto.ClientDTO;
import com.systemA.reportapi.dto.ProductDTO;
import com.systemA.reportapi.exception.ReportInputException;
import com.systemA.reportapi.exception.ReportOutputException;
import com.systemA.reportapi.service.ReportService;

@RestController
public class ReportController {

	@Autowired
	private ReportService reportService;

	Logger logger = LoggerFactory.getLogger(ReportController.class);
	
	@RequestMapping(value = "/getSummaryReport", method = RequestMethod.GET, produces = "text/csv")
	public void getSummaryReport(HttpServletResponse response) throws ReportInputException, ReportOutputException {
		logger.info("Received a request to generate summary report.");
		Map<ClientDTO, List<ProductDTO>> reportData = reportService.getSummaryReportData();
		reportService.generateReport(response, reportData);
	}
}
