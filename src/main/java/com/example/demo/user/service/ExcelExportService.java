package com.example.demo.user.service;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;
import java.util.Map;

public interface ExcelExportService {
    XSSFWorkbook exportExcel(String fileName, Map<String, Object> map, String sheetName);
}
