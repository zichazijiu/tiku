package com.songzi.service;

import com.songzi.domain.Examiner;
import com.songzi.domain.User;
import com.songzi.domain.enumeration.AuthoritiesType;
import com.songzi.domain.enumeration.Sex;
import com.songzi.repository.ExaminerRepository;
import com.songzi.repository.UserRepository;
import com.songzi.service.dto.ExaminerDTO;
import com.songzi.service.dto.UserDTO;
import com.songzi.web.rest.errors.BadRequestAlertException;
import com.songzi.web.rest.util.ChineseToEnglishUtil;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    private ExaminerService examinerService;

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
            headers = new String[]{"用户ID","登录名","密码","考评员ID","名称", "机构id", "手机号", "电子邮箱", "性别", "生日","地址","座机","街道"};
            for (int i = 0; i < headers.length; i++) {
                Cell celli = row.createCell(i);
                celli.setCellValue(headers[i]);
                celli.setCellStyle(headerStyle);
                sheet.setColumnWidth(i,4000);

            }
            List<Object[]> examinerList = examinerRepository.exprotExaminerAndUserMessage();
            for(int i = 0; i < examinerList.size();i++){
                Object[] obj = examinerList.get(i);
                Row dataRow= sheet.createRow(i+1);
                dataRow.setHeight((short) 400);
                dataRow.createCell(0).setCellValue(obj[0] == null?"":obj[0]+"");
                dataRow.createCell(1).setCellValue(obj[1] == null?"":obj[1]+"");
                dataRow.createCell(2).setCellValue(obj[2] == null?"":obj[2]+"");
                dataRow.createCell(3).setCellValue(obj[3] == null?"":obj[3]+"");
                dataRow.createCell(4).setCellValue(obj[4] == null?"":obj[4]+"");
                dataRow.createCell(5).setCellValue(obj[5] == null?"":obj[5]+"");
                dataRow.createCell(6).setCellValue(obj[6] == null?"":obj[6]+"");
                dataRow.createCell(7).setCellValue(obj[7] == null?"":obj[7]+"");
                dataRow.createCell(8).setCellValue(obj[8] == null?"":obj[8]+"");
                dataRow.createCell(9).setCellValue(obj[9] == null?"":obj[9]+"");
                dataRow.createCell(10).setCellValue(obj[10] == null?"":obj[10]+"");
                dataRow.createCell(11).setCellValue(obj[11] == null?"":obj[11]+"");
                dataRow.createCell(12).setCellValue(obj[12] == null?"":obj[12]+"");
            }
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment;filename= 用户_"+date+".xls");
            response.flushBuffer();
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            log.debug("error{}" + e.getMessage());

        }
        return response.getOutputStream();
    }

    /**
     * 批量导入连接变量V-SimpleVar
     * @param file
     * @return
     */
    public void importExaminerXml(MultipartFile file)throws Exception {
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        log.debug(suffix);
        if (!".xls".equals(suffix) && !".xlsx".equals(suffix)) {
            log.debug("模板不匹配,只支持Excel文件,后缀为xls或xlsx格式的文件！");
            throw new BadRequestAlertException("模板不匹配,只支持Excel文件,后缀为xls或xlsx格式的文件！", this.getClass().getName(),"格式不匹配");
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
            throw new BadRequestAlertException("导入文件里面是空数据", this.getClass().getName(),"空数据");
        }
        List<String[]> list = new ArrayList<>();
        for(int i = 0; i < totalRow; i++){
            //第一行  标题列不取
            row = sheet.getRow(i+1);
            String[] rowCell = new String[13];
            rowCell[0] = row.getCell(0).getStringCellValue() == null?"":row.getCell(0).getStringCellValue();
            rowCell[1] = row.getCell(1).getStringCellValue() == null?"":row.getCell(1).getStringCellValue();
            rowCell[2] = row.getCell(2).getStringCellValue() == null?"":row.getCell(2).getStringCellValue();
            rowCell[3] = row.getCell(3).getStringCellValue() == null?"":row.getCell(3).getStringCellValue();
            rowCell[4] = row.getCell(4).getStringCellValue() == null?"":row.getCell(4).getStringCellValue();
            rowCell[5] = row.getCell(5).getStringCellValue() == null?"":row.getCell(5).getStringCellValue();
            rowCell[6] = row.getCell(6).getStringCellValue() == null?"":row.getCell(6).getStringCellValue();
            rowCell[7] = row.getCell(7).getStringCellValue() == null?"":row.getCell(7).getStringCellValue();
            rowCell[8] = row.getCell(8).getStringCellValue() == null?"":row.getCell(8).getStringCellValue();
            rowCell[9] = row.getCell(9).getStringCellValue() == null?"":row.getCell(9).getStringCellValue();
            rowCell[10] = row.getCell(10).getStringCellValue() == null?"":row.getCell(10).getStringCellValue();
            rowCell[11] = row.getCell(11).getStringCellValue() == null?"":row.getCell(11).getStringCellValue();
            rowCell[12] = row.getCell(12).getStringCellValue() == null?"":row.getCell(12).getStringCellValue();
            list.add(rowCell);
        }
        this.importExaminerXmlSave(list);
    }

    public void importExaminerXmlSave(List<String[]> rowList){
        for(String[] str : rowList){
            if("".equals(str[0])){
                UserDTO userDTO = new UserDTO();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                String now = LocalDateTime.now().format(formatter);
                userDTO.setLogin(ChineseToEnglishUtil.getPingYin(str[4]+ now));
                userDTO.setEmail(str[7]);
                if(userRepository.findOneWithAuthoritiesByEmail(str[7]).isPresent()){
                    throw new BadRequestAlertException("邮箱"+str[7]+"已存在",this.getClass().getName(),"邮箱重复");
                }

//                Set<String> authoritiesSet = new HashSet<>();
//                if(examinerVM.getAuthoritiesType() != null){
//                    for(AuthoritiesType authoritiesType : examinerVM.getAuthoritiesType()){
//                        authoritiesSet.add(authoritiesType.name());
//                    }
//                    userDTO.setAuthorities(authoritiesSet);
//                }
                userDTO.setLastName(str[4]);
                userDTO.setFirstName(str[4]);
                User user = userService.createUserByExaminer(userDTO);


                Examiner examiner = new Examiner();
                examiner.setUserId(user.getId());
                examiner.setTime(0);
                examiner.setName(str[4]);
                examiner.setDepartmentId(Long.parseLong(str[5]));
                examiner.setCellPhone(str[6]);
                examiner.setEmail(str[7]);
                examiner.setSex(Sex.valueOf(str[8]));
                examiner.setBirth(str[9]);
                examiner.setLocation(str[10]);
                examiner.setPhone(str[11]);
                examiner.setAddress(str[12]);
                examinerRepository.save(examiner);
            }else{
                User user = userRepository.findOne(Long.parseLong(str[0]));
                Optional<User> user_email = userRepository.findOneWithAuthoritiesByEmail(str[7]);
                if(user_email.isPresent() && user_email.get().getId() != user.getId()){
                    throw new BadRequestAlertException("邮箱"+str[7]+"已存在",this.getClass().getName(),"邮箱重复");
                }
                user.setLogin(str[1]);
                user.setPassword(str[2]);
                user.setFirstName(str[4]);
                user.setLastName(str[4]);
                user.setEmail(str[7]);
                userRepository.save(user);

                Examiner examiner = examinerRepository.findOne(Long.parseLong(str[3]));
                examiner.setName(str[4]);
                examiner.setDepartmentId(Long.parseLong(str[5]));
                examiner.setCellPhone(str[6]);
                examiner.setEmail(str[7]);
                examiner.setSex(Sex.valueOf(str[8]));
                examiner.setBirth(str[9]);
                examiner.setLocation(str[10]);
                examiner.setPhone(str[11]);
                examiner.setAddress(str[12]);
                examinerRepository.save(examiner);
            }
        }

    }
}
