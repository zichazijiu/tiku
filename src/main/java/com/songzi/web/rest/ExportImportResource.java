package com.songzi.web.rest;

import com.alibaba.fastjson.JSONObject;
import com.codahale.metrics.annotation.Timed;
import com.songzi.service.ExportImportService;
import com.songzi.web.rest.errors.BadRequestAlertException;
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
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ExportImportService exportImportService;

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
        if("EXAMINER".equals(importType)){
            exportImportService.importExaminerXml(file);
        }else if("SUBJECT".equals(importType)){
            exportImportService.importSubjectXml(file);
        }else{
            throw new BadRequestAlertException("不支持的导入类型",this.getClass().getName(),"不支持");
        }
        Map result = new HashMap();
        result.put("flag","success");
        result.put("message","成功导入");
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }

    @GetMapping("/exportVariables")
    @Timed
    @ApiOperation(value = "导出变量",httpMethod = "GET",response = Void.class ,notes = "导出变量")
    public ResponseEntity<?> exportVariable(@RequestParam String importType, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if("EXAMINER".equals(importType)){
            exportImportService.exportExaminerExcel(response);
        }else if("SUBJECT".equals(importType)){
            exportImportService.exportSubjectExcel(response);
        }else{
            throw new BadRequestAlertException("不支持的导出类型",this.getClass().getName(),"不支持");
            //this.exportExcel(importType,response);
        }
        Map result = new HashMap();
        result.put("flag","success");
        result.put("message","成功导出");
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }

    @GetMapping("/exportModelExcel")
    @Timed
    @ApiOperation(value = "导出模板",httpMethod = "GET",response = Void.class ,notes = "导入模板")
    public void exportModelExcel(@RequestParam String modelType, HttpServletRequest request, HttpServletResponse response) throws IOException {
        exportImportService.exportModleXls(modelType,response);
    }
}
