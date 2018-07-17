package com.songzi.web.rest;

import com.alibaba.fastjson.JSONObject;
import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ExportImportResource {

    private final Logger log = LoggerFactory.getLogger(ExportImportResource.class);

    /**
     * 连接模型连接变量批量导入
     * @param importType
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping("/importVariables")
    @Timed
    @ApiOperation(value = "批量导入",httpMethod = "POST",response = Map.class ,notes = "导入")
    public ResponseEntity<?> importVariable(@RequestParam String importType,
                                            @RequestParam MultipartFile file) throws Exception {
        log.debug("批量导入 : {}", importType);
        Map result = new HashMap();
        result.put("flag","success");
        result.put("message","成功导入");
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }

    @GetMapping("/exportVariables")
    @Timed
    @ApiOperation(value = "导出变量",httpMethod = "GET",response = Void.class ,notes = "导出变量")
    public ResponseEntity<?> exportVariable(@RequestParam String importType, HttpServletRequest request, HttpServletResponse response) throws IOException {

        ServletOutputStream result = this.exportExcel(importType,response);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }



    private ServletOutputStream exportExcel(String importType, HttpServletResponse response) throws IOException {
        try {
            SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
            String date = dft.format(new Date());
            Workbook workbook = new HSSFWorkbook();
            Sheet sheet = workbook.createSheet(importType);
            sheet.setColumnWidth(0, 4000);
            sheet.setColumnWidth(1, 4000);
            sheet.setColumnWidth(2, 4000);
            sheet.setColumnWidth(3, 4000);
            sheet.setColumnWidth(4, 5000);
            sheet.setColumnWidth(5, 4000);
            sheet.setColumnWidth(6, 5000);
            sheet.setColumnWidth(7, 4000);
            sheet.setColumnWidth(8, 4000);
            sheet.setDefaultRowHeightInPoints(18);

            HSSFCellStyle headerStyle = (HSSFCellStyle) workbook.createCellStyle();// 创建标题样式
            HSSFFont headerFont = (HSSFFont) workbook.createFont(); //创建字体样式
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 11);    //设置字体大小
            headerStyle.setFont(headerFont);    //为标题样式设置字体样式

            HSSFCellStyle cellStyle = (HSSFCellStyle) workbook.createCellStyle();
            HSSFFont cellFont = (HSSFFont) workbook.createFont(); //创建字体样式
            cellFont.setFontHeightInPoints((short) 11);    //设置字体大小
            cellStyle.setFont(cellFont);
            Row row = sheet.createRow(0);

            String headers[];
            if("examiner".equals(importType)){
                headers = new String[]{"id","名称", "机构id", "手机号", "电子邮箱", "性别", "生日","地址","座机","街道"};
            }else{
                headers = new String[]{"id","机构名称", "机构编码", "类型", "上级机构id"};
            }
            for (int i = 0; i < headers.length; i++) {
                Cell celli = row.createCell(i);
                celli.setCellValue(headers[i]);
                celli.setCellStyle(headerStyle);

            }

            Row dataRow= sheet.createRow(1);
            dataRow.setHeight((short) 400);
            if("examiner".equals(importType)){
                dataRow.createCell(0).setCellValue("1");
                dataRow.createCell(1).setCellValue("admin");
                dataRow.createCell(2).setCellValue("1");
                dataRow.createCell(3).setCellValue("15810987865");
                dataRow.createCell(4).setCellValue("111@111.com");
                dataRow.createCell(5).setCellValue("MAN");
                dataRow.createCell(6).setCellValue("2018-09-11");
                dataRow.createCell(7).setCellValue("大街");
                dataRow.createCell(8).setCellValue("8976846");
                dataRow.createCell(9).setCellValue("草地上");
            }else{
                dataRow.createCell(0).setCellValue("1");
                dataRow.createCell(1).setCellValue("部门1");
                dataRow.createCell(2).setCellValue("1");
                dataRow.createCell(3).setCellValue("部门");
                dataRow.createCell(4).setCellValue("");
            }
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment;filename="+importType+"_"+date+".xls");
            response.flushBuffer();
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            log.debug("error{}" + e.getMessage());

        }
        return response.getOutputStream();

    }
}
