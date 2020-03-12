package com.songzi.service;

import cc.kebei.expands.template.Template;
import com.alibaba.fastjson.JSONArray;
import com.songzi.domain.*;
import com.songzi.domain.enumeration.*;
import com.songzi.repository.ExaminerRepository;
import com.songzi.repository.SubjectRepository;
import com.songzi.repository.UserRepository;
import com.songzi.security.SecurityUtils;
import com.songzi.service.dto.MultipleChoice;
import com.songzi.service.dto.UserDTO;
import com.songzi.web.rest.errors.BadRequestAlertException;
import com.songzi.web.rest.util.ChineseToEnglishUtil;
import com.songzi.web.rest.vm.SubjectVM;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@Transactional
public class ExportImportService {

    private final Logger log = LoggerFactory.getLogger(ExportImportService.class);

    @Autowired
    private ExaminerRepository examinerRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private LogBackupService logBackupService;

    public ServletOutputStream exportExaminerExcel(HttpServletResponse response) throws IOException {
        try {
            SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
            String date = dft.format(new Date());
            Workbook workbook = new HSSFWorkbook();
            Sheet sheet = workbook.createSheet("用户");
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
            headers = new String[]{"姓名", "机构id", "手机号", "电子邮箱", "性别", "生日", "地址", "座机", "街道"};
            for (int i = 0; i < headers.length; i++) {
                Cell celli = row.createCell(i);
                celli.setCellValue(headers[i]);
                celli.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 4000);

            }
            List<Object[]> examinerList = examinerRepository.exprotExaminerAndUserMessage();
            for (int i = 0; i < examinerList.size(); i++) {
                Object[] obj = examinerList.get(i);
                Row dataRow = sheet.createRow(i + 1);
                dataRow.setHeight((short) 400);
//                dataRow.createCell(0).setCellValue(obj[0] == null?"":obj[0]+"");
//                dataRow.createCell(1).setCellValue(obj[1] == null?"":obj[1]+"");
//                dataRow.createCell(2).setCellValue(obj[2] == null?"":obj[2]+"");
//                dataRow.createCell(3).setCellValue(obj[3] == null?"":obj[3]+"");
                dataRow.createCell(0).setCellValue(obj[4] == null ? "" : obj[4] + "");
                dataRow.createCell(1).setCellValue(obj[5] == null ? "" : obj[5] + "");
                dataRow.createCell(2).setCellValue(obj[6] == null ? "" : obj[6] + "");
                dataRow.createCell(3).setCellValue(obj[7] == null ? "" : obj[7] + "");
                dataRow.createCell(4).setCellValue(obj[8] == null ? "" : obj[8] + "");
                dataRow.createCell(5).setCellValue(obj[9] == null ? "" : obj[9] + "");
                dataRow.createCell(6).setCellValue(obj[10] == null ? "" : obj[10] + "");
                dataRow.createCell(7).setCellValue(obj[11] == null ? "" : obj[11] + "");
                dataRow.createCell(8).setCellValue(obj[12] == null ? "" : obj[12] + "");
            }
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment;filename= 用户_" + date + ".xls");
            response.flushBuffer();
            workbook.write(response.getOutputStream());

            LogBackup logBackup = new LogBackup();
            logBackup.setCreatedTime(Instant.now());
            logBackup.setCreatedBy(SecurityUtils.getCurrentUserLogin().get());
            logBackup.setDescription("考评员数据导出");
            logBackup.setSize(0);
            logBackup.setLevel(Level.INFO);
            logBackup.setAuthority("ROLE_ADMIN");
            logBackup.setLogType(LogType.IEMPORT);
            logBackup.setBakType(BakType.EXAMINER);
            logBackupService.insert(logBackup);
        } catch (Exception e) {
            log.debug("error{}" + e.getMessage());

        }
        return response.getOutputStream();
    }

    /**
     * 批量导入连接变量V-SimpleVar
     *
     * @param file
     * @return
     */
    public void importExaminerXml(MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        log.debug(suffix);
        if (!".xls".equals(suffix) && !".xlsx".equals(suffix)) {
            log.debug("模板不匹配,只支持Excel文件,后缀为xls或xlsx格式的文件！");
            throw new BadRequestAlertException("模板不匹配,只支持Excel文件,后缀为xls或xlsx格式的文件！", this.getClass().getName(), "格式不匹配");
        }
        //读取file
        Workbook workbook = null;
        if ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(file.getContentType())) {
            workbook = new XSSFWorkbook(file.getInputStream());
        } else {
            workbook = new HSSFWorkbook(new POIFSFileSystem(file.getInputStream()));
        }
        Sheet sheet = workbook.getSheetAt(0);
        Integer totalRow = sheet.getLastRowNum();
        Row row;
        if (totalRow == 0) {
            throw new BadRequestAlertException("导入文件里面是空数据", this.getClass().getName(), "空数据");
        }
        List<String[]> list = new ArrayList<>();
        int i = 0;
        while (true) {
            //第一行  标题列不取
            row = sheet.getRow(i + 1);
            String[] rowCell = new String[13];
            if (row.getCell(0) == null && row.getCell(1) == null && row.getCell(2) == null && row.getCell(3) == null && row.getCell(4) == null) {
                break;
            }
            rowCell[0] = row.getCell(0) == null ? "" : row.getCell(0).getStringCellValue();
            rowCell[1] = row.getCell(1) == null ? "" : row.getCell(1).getStringCellValue();
            rowCell[2] = row.getCell(2) == null ? "" : row.getCell(2).getStringCellValue();
            rowCell[3] = row.getCell(3) == null ? "" : row.getCell(3).getStringCellValue();
            rowCell[4] = row.getCell(4) == null ? "" : row.getCell(4).getStringCellValue();
            rowCell[5] = row.getCell(5) == null ? "" : row.getCell(5).getStringCellValue();
            rowCell[6] = row.getCell(6) == null ? "" : row.getCell(6).getStringCellValue();
            rowCell[7] = row.getCell(7) == null ? "" : row.getCell(7).getStringCellValue();
            rowCell[8] = row.getCell(8) == null ? "" : row.getCell(8).getStringCellValue();
//            rowCell[9] = row.getCell(9).getStringCellValue() == null?"":row.getCell(9).getStringCellValue();
//            rowCell[10] = row.getCell(10).getStringCellValue() == null?"":row.getCell(10).getStringCellValue();
//            rowCell[11] = row.getCell(11).getStringCellValue() == null?"":row.getCell(11).getStringCellValue();
//            rowCell[12] = row.getCell(12).getStringCellValue() == null?"":row.getCell(12).getStringCellValue();
            list.add(rowCell);
            i++;
        }
        this.importExaminerXmlSave(list);

        LogBackup logBackup = new LogBackup();
        logBackup.setCreatedTime(Instant.now());
        logBackup.setCreatedBy(SecurityUtils.getCurrentUserLogin().get());
        logBackup.setDescription("考评员数据导入");
        logBackup.setSize(0);
        logBackup.setLevel(Level.INFO);
        logBackup.setAuthority("ROLE_ADMIN");
        logBackup.setLogType(LogType.IEMPORT);
        logBackup.setBakType(BakType.EXAMINER);
        logBackupService.insert(logBackup);
    }

    public void importExaminerXmlSave(List<String[]> rowList) {
        for (String[] str : rowList) {
            UserDTO userDTO = new UserDTO();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            String now = LocalDateTime.now().format(formatter);
            userDTO.setLogin(ChineseToEnglishUtil.getPingYin(str[0] + now));
            userDTO.setEmail(str[3]);
            if (userRepository.findOneWithAuthoritiesByEmail(str[3]).isPresent()) {
                throw new BadRequestAlertException("邮箱" + str[3] + "已存在", this.getClass().getName(), "邮箱重复");
            }

//                Set<String> authoritiesSet = new HashSet<>();
//                if(examinerVM.getAuthoritiesType() != null){
//                    for(AuthoritiesType authoritiesType : examinerVM.getAuthoritiesType()){
//                        authoritiesSet.add(authoritiesType.name());
//                    }
//                    userDTO.setAuthorities(authoritiesSet);
//                }
            userDTO.setLastName(str[0]);
            userDTO.setFirstName(str[0]);
            User user = userService.createUserByExaminer(userDTO);


            Examiner examiner = new Examiner();
            examiner.setUserId(user.getId());
            examiner.setTime(0);
            examiner.setName(str[0]);
            examiner.setDepartmentId(Long.parseLong(str[1]));
            examiner.setCellPhone(str[2]);
            examiner.setEmail(str[3]);
            examiner.setSex(Sex.valueOf(str[4]));
            examiner.setBirth(str[5]);
            examiner.setLocation(str[6]);
            examiner.setPhone(str[7]);
            examiner.setAddress(str[8]);
            examinerRepository.save(examiner);
        }
    }


    public ServletOutputStream exportSubjectExcel(HttpServletResponse response) throws IOException {
        try {
            SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
            String date = dft.format(new Date());
            Workbook workbook = new HSSFWorkbook();
            Sheet sheet = workbook.createSheet("考题");
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
            headers = new String[]{"题目名称", "题目标题", "题目描述", "题目答案（正确填1，错误填0）"};
            for (int i = 0; i < headers.length; i++) {
                Cell celli = row.createCell(i);
                celli.setCellValue(headers[i]);
                celli.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 4000);

            }
            List<Subject> subjectList = subjectRepository.findAll();
            for (int i = 0; i < subjectList.size(); i++) {
                Subject subject = subjectList.get(i);
                Row dataRow = sheet.createRow(i + 1);
                dataRow.setHeight((short) 400);
                dataRow.createCell(0).setCellValue(subject.getName());
                dataRow.createCell(1).setCellValue(subject.getTitle());
                dataRow.createCell(2).setCellValue(subject.getDescription());
                dataRow.createCell(3).setCellValue(subject.getRight());
            }
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment;filename= 题目_" + date + ".xls");
            response.flushBuffer();
            workbook.write(response.getOutputStream());

            LogBackup logBackup = new LogBackup();
            logBackup.setCreatedTime(Instant.now());
            logBackup.setCreatedBy(SecurityUtils.getCurrentUserLogin().get());
            logBackup.setDescription("考评项数据导出");
            logBackup.setSize(0);
            logBackup.setLevel(Level.INFO);
            logBackup.setAuthority("ROLE_ADMIN");
            logBackup.setLogType(LogType.IEMPORT);
            logBackup.setBakType(BakType.SUBJECT);
            logBackupService.insert(logBackup);
        } catch (Exception e) {
            log.debug("error{}" + e.getMessage());

        }
        return response.getOutputStream();
    }


    /**
     * 批量导入连接变量V-SimpleVar
     *
     * @param file
     * @return
     */
    public void importSubjectXml(MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        log.debug(suffix);
        if (!".xls".equals(suffix) && !".xlsx".equals(suffix)) {
            log.debug("模板不匹配,只支持Excel文件,后缀为xls或xlsx格式的文件！");
            throw new BadRequestAlertException("模板不匹配,只支持Excel文件,后缀为xls或xlsx格式的文件！", this.getClass().getName(), "格式不匹配");
        }
        //读取file
        Workbook workbook = null;
        if ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(file.getContentType())) {
            workbook = new XSSFWorkbook(file.getInputStream());
        } else {
            workbook = new HSSFWorkbook(new POIFSFileSystem(file.getInputStream()));
        }
        Sheet sheet = workbook.getSheetAt(0);
        Integer totalRow = sheet.getLastRowNum(); // 总行数
        Integer totalColumnNum = sheet.getRow(0).getPhysicalNumberOfCells(); // 标题行的列数
        Row row;
        if (totalRow == 0) {
            throw new BadRequestAlertException("导入文件里面是空数据", this.getClass().getName(), "空数据");
        }
        if (totalColumnNum > 13) { // 多选题
            List<SubjectVM> subjects = assembleMultipleChoice(sheet);
            subjects.stream().forEach(subjectVM -> {
                subjectService.insert(subjectVM);
            });
        } else { // 判断题
            List<String[]> list = new ArrayList<>();
            int i = 1; // 不读标题，所以从1开始
            while (true) {
                //第一行  标题列不取
                row = sheet.getRow(i);

                String[] rowCell = new String[13];
                if (row.getCell(0) == null && row.getCell(1) == null
                    && row.getCell(2) == null && row.getCell(3) == null
                    && row.getCell(4) == null)
                    break;

                rowCell[0] = row.getCell(0) == null ? "" : row.getCell(0).getStringCellValue();
                rowCell[1] = row.getCell(1) == null ? "" : row.getCell(1).getStringCellValue();
                rowCell[2] = row.getCell(2) == null ? "" : row.getCell(2).getStringCellValue();
                rowCell[3] = row.getCell(3) == null ? "" : row.getCell(3).getStringCellValue();
                list.add(rowCell);


                i++;
            }
            this.importSubjectXmlSave(list);
        }

        LogBackup logBackup = new LogBackup();
        logBackup.setCreatedTime(Instant.now());
        logBackup.setCreatedBy(SecurityUtils.getCurrentUserLogin().get());
        logBackup.setDescription("考评项数据导入");
        logBackup.setSize(0);
        logBackup.setLevel(Level.INFO);
        logBackup.setAuthority("ROLE_ADMIN");
        logBackup.setLogType(LogType.IEMPORT);
        logBackup.setBakType(BakType.SUBJECT);
        logBackupService.insert(logBackup);
    }

    private List<SubjectVM> assembleMultipleChoice(Sheet sheet) {
        List<SubjectVM> subjects = new ArrayList<>(sheet.getLastRowNum());
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            SubjectVM subject = new SubjectVM();
            int cellIndex = 0;
            // 考评项题目
            subject.setName(row.getCell(cellIndex) == null ? "" : row.getCell(cellIndex++).getStringCellValue());
            // 考评项标题
            subject.setTitle(row.getCell(cellIndex) == null ? "" : row.getCell(cellIndex++).getStringCellValue());
            // 如果题目和标题是空则跳出循环，入库结束
            if (StringUtils.isEmpty(subject.getName()) && StringUtils.isEmpty(subject.getTitle()))
                break;
            // 考评项描述
            subject.setDescription(row.getCell(cellIndex) == null ? "" : row.getCell(cellIndex++).getStringCellValue());
            // 答案 默认0
            subject.setRight(0L);
            // type 默认NORMAL
            subject.setType("NORMAL");
            // 考评项分值
            subject.setTotalPoint(row.getCell(cellIndex) == null ? "" : String.valueOf(row.getCell(cellIndex++).getStringCellValue()));
            // 考评选项
            List<MultipleChoice> multipleChoiceList = new ArrayList<>();
            // 选项1
            MultipleChoice multipleChoice1 = new MultipleChoice();
            multipleChoice1.setNo("1");
            multipleChoice1.setContent(row.getCell(cellIndex) == null ? "" : row.getCell(cellIndex++).getStringCellValue());
            multipleChoice1.setPoint(row.getCell(cellIndex) == null ? "" : String.valueOf(row.getCell(cellIndex++).getStringCellValue()));
            if (!"".equals(multipleChoice1.getContent()) && !"".equals(multipleChoice1.getPoint())) {
                multipleChoiceList.add(multipleChoice1);
            }
            // 选项2
            MultipleChoice multipleChoice2 = new MultipleChoice();
            multipleChoice2.setNo("2");
            multipleChoice2.setContent(row.getCell(cellIndex) == null ? "" : row.getCell(cellIndex++).getStringCellValue());
            multipleChoice2.setPoint(row.getCell(cellIndex) == null ? "" : String.valueOf(row.getCell(cellIndex++).getStringCellValue()));
            if (!"".equals(multipleChoice2.getContent()) && !"".equals(multipleChoice2.getPoint()))
                multipleChoiceList.add(multipleChoice2);
            // 选项3
            MultipleChoice multipleChoice3 = new MultipleChoice();
            multipleChoice3.setNo("3");
            multipleChoice3.setContent(row.getCell(cellIndex) == null ? "" : row.getCell(cellIndex++).getStringCellValue());
            multipleChoice3.setPoint(row.getCell(cellIndex) == null ? "" : String.valueOf(row.getCell(cellIndex++).getStringCellValue()));
            if (!"".equals(multipleChoice3.getContent()) && !"".equals(multipleChoice3.getPoint()))
                multipleChoiceList.add(multipleChoice3);
            // 选项4
            MultipleChoice multipleChoice4 = new MultipleChoice();
            multipleChoice4.setNo("4");
            multipleChoice4.setContent(row.getCell(cellIndex) == null ? "" : row.getCell(cellIndex++).getStringCellValue());
            multipleChoice4.setPoint(row.getCell(cellIndex) == null ? "" : String.valueOf(row.getCell(cellIndex++).getStringCellValue()));
            if (!"".equals(multipleChoice4.getContent()) && !"".equals(multipleChoice4.getPoint()))
                multipleChoiceList.add(multipleChoice4);
            // 选项5
            MultipleChoice multipleChoice5 = new MultipleChoice();
            multipleChoice5.setNo("5");
            multipleChoice5.setContent(row.getCell(cellIndex) == null ? "" : row.getCell(cellIndex++).getStringCellValue());
            multipleChoice5.setPoint(row.getCell(cellIndex) == null ? "" : String.valueOf(row.getCell(cellIndex++).getStringCellValue()));
            if (!"".equals(multipleChoice5.getContent()) && !"".equals(multipleChoice5.getPoint()))
                multipleChoiceList.add(multipleChoice5);

            subject.setOptions(JSONArray.toJSONString(multipleChoiceList));

            subjects.add(subject);

        }
        return subjects;
    }

    public void importSubjectXmlSave(List<String[]> rowList) {
        for (String[] str : rowList) {
            SubjectVM subjectVM = new SubjectVM();
            subjectVM.setName(str[0]);
            subjectVM.setTitle(str[1]);
            subjectVM.setDescription(str[2]);
            subjectVM.setRight(Long.parseLong(str[3] + ""));
            subjectVM.setType("NORMAL");
            subjectService.insert(subjectVM);
        }
    }

    @Autowired
    private ResourceLoader resourceLoader;

    public void exportModleXls(String modelType, HttpServletResponse response) throws IOException {
        String fileName;
        if ("EXAMINER".equals(modelType)) {
            fileName = "classpath:templates/考员.xls";
        } else if ("SUBJECT".equals(modelType)) {
            fileName = "classpath:templates/考评项.xls";
        } else {
            throw new BadRequestAlertException("不支持的类型", this.getClass().getName(), "不支持的类型");
        }
        Resource resource = resourceLoader.getResource(fileName);
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName.getBytes());
        ServletOutputStream out = response.getOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(resource.getInputStream());
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (final IOException e) {
            throw e;
        } finally {
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
        }
    }

    @Autowired
    private ReportService reportService;
    @Autowired
    private ReportItemsService reportItemsService;

    @Transactional
    public void importReport(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf("."));
        if (!".doc".equals(extension)) {
            log.debug("模板不匹配,只支持Word2003文件,后缀为doc");
            throw new BadRequestAlertException("模板不匹配,只支持Word2003文件,后缀为doc", this.getClass().getName(), "格式不匹配");
        }
        // 处理doc格式 即office2003版本
        POIFSFileSystem pfs = null;
        HWPFDocument document = null;
        try {
            pfs = new POIFSFileSystem(file.getInputStream());
            document = new HWPFDocument(pfs);
            TableIterator iterator = new TableIterator(document.getRange());
            Map<Integer, String> answerMap = new HashMap();
            while (iterator.hasNext()) {
                Table table = iterator.next();
                // 遍历行，从第1行开始
                int position = 1;
                for (int i = 1; i < table.numRows(); i++) {
                    TableRow row = table.getRow(i);
                    if (row.numCells() == 5) {
                        answerMap.put(position, row.getCell(4).text());
                    }
                    position++;
                }
            }
            // 生成报告
            Report report = new Report();
            // 保存报告名称
            report.setReportName("自查自纠");
            ZonedDateTime now = Instant.now().atZone(ZoneOffset.ofHours(8));
            // 报告创建时间
            report.setCreatedTime(now);
            // 报告时间
            report.setReportTime(now);
            // 保存报告人
            User user = SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin).get();
            report.setReportUser(user.getLogin());
            report.setUser(user);
            // 报告状态
            report.setReportStatus(ReportStatus.FINISH);
            // 保存报告
            Report report1 = reportService.save(report);
            // 报告内容
            for (int i = 1; i < 60; i++) {
                String level = answerMap.get(i);
                if (level != null && level.length() > 0) {
                    CheckItem checkItem = new CheckItem();
                    checkItem.setId((long) i);
                    ReportItems items = new ReportItems();
                    items.setLevel(level.trim());
                    items.setCheckItem(checkItem);
                    items.setReport(report1);
                    // 保存提报信息
                    reportItemsService.save(items);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pfs != null) {
                    pfs.close();
                }
                if (document != null) {
                    document.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 导出报告
     *
     * @param id report id
     */
    public void exportReport(String id, HttpServletRequest request, HttpServletResponse response) {
        ByteArrayInputStream bis = null;
        try {
            Report report = reportService.findOne(Long.parseLong(id));
            User user = SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin).get();
            Resource resource = resourceLoader.getResource("classpath:templates/report-template.ftl");
            Map<String, Object> dataMap = new HashMap<>();
            // 设置报告部门
            String department = "";
            if (user.getDepartment() != null) {
                department = user.getDepartment().getName();
            }
            dataMap.put("department", department);
            // 设置报告日期
            ZonedDateTime reportDate = DateTime.now().toGregorianCalendar().toZonedDateTime();
            if (report.getReportTime() != null) {
                reportDate = report.getReportTime();
            }
            dataMap.put("year", reportDate.getYear() + "");
            dataMap.put("month", reportDate.getMonthValue() + "");
            dataMap.put("day", reportDate.getDayOfMonth() + "");
            // 设置报告答案
            for (ReportItems item : report.getReportItems()) {
                Long checkItemId = item.getCheckItem().getId();
                String answer = item.getLevel();
                dataMap.put("X" + checkItemId, answer.trim());
            }
            // Freemarker
            String template = FileCopyUtils.copyToString(new InputStreamReader(resource.getInputStream(), UTF_8));
            String result = Template.freemarker.compile(template).render(dataMap);
            bis = new ByteArrayInputStream(result.getBytes(UTF_8));
            // 编码文件名
            String filename = Timestamp.from(reportDate.toInstant()).getTime() +".doc";
            // 设置Response信息
            response.reset();
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/msword");
            response.setHeader("Content-disposition", "attachment;filename=" + filename);

            org.apache.commons.io.IOUtils.copy(bis, response.getOutputStream());
            response.flushBuffer();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void readAnswerFromTable(Range range) {
        TableIterator it = new TableIterator(range);
        while (it.hasNext()) {
            Table tb = it.next();
            //迭代行，默认从0开始,可以依据需要设置i的值,改变起始行数，也可设置读取到那行，只需修改循环的判断条件即可
            for (int i = 0; i < tb.numRows(); i++) {
                TableRow tr = tb.getRow(i);
                //迭代列，默认从0开始
                for (int j = 0; j < tr.numCells(); j++) {
                    //取得单元格
                    TableCell td = tr.getCell(j);
                    //取得单元格的内容
                    for (int k = 0; k < td.numParagraphs(); k++) {
                        Paragraph para = td.getParagraph(k);
                        String s = para.text();
                        //去除后面的特殊符号
                        if (null != s && !"".equals(s)) {
                            s = s.substring(0, s.length() - 1);
                        }
                        System.out.print(s + "\t");
                    }
                }
                System.out.println();
            }
        }
    }

    public static Map<Integer, String> readReportAnswerMapFromRange(Range range) {
        TableIterator iterator = new TableIterator(range);
        Map<Integer, String> map = new HashMap();
        while (iterator.hasNext()) {
            Table table = iterator.next();
            // 遍历行，从第1行开始
            int position = 1;
            for (int i = 1; i < table.numRows(); i++) {
                TableRow row = table.getRow(i);
                if (row.numCells() == 5) {
                    map.put(position, row.getCell(4).text());
                }
                position++;
            }
        }
        return map;
    }

    public static void main(String[] args) throws Exception {
        String filepath = "/Users/kebei/Desktop/自查自纠/自查自评结果.doc";
        File file = new File(filepath);
        FileInputStream fileInputStream = new FileInputStream(file);
        POIFSFileSystem pfs = new POIFSFileSystem(fileInputStream);
        HWPFDocument document = new HWPFDocument(pfs);
        readReportAnswerMapFromRange(document.getRange());
    }

}
