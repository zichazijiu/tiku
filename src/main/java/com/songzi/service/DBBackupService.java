package com.songzi.service;

import cc.kebei.utils.DateTimeUtils;
import com.songzi.TikuConstants;
import com.songzi.config.Constants;
import com.songzi.domain.DBBackup;
import com.songzi.domain.LogBackup;
import com.songzi.domain.enumeration.LogType;
import com.songzi.repository.DBBackupRepository;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.time.Instant;
import java.util.Date;

/**
 * @author Ke Bei
 * @date 2020/3/21
 */
@Service
public class DBBackupService {
    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Autowired private DBBackupRepository dbBackupRepository;

    private static final Logger LOG = LoggerFactory.getLogger(DBBackupService.class);

    /**
     * Java代码实现MySQL数据库导出
     * @param hostIP MySQL数据库所在server地址IP
     * @param userName 进入数据库所须要的username
     * @param password 进入数据库所须要的密码
     * @param savePath 数据库导出文件保存路径
     * @param fileName 数据库导出文件文件名称
     * @param databaseName 要导出的数据库名
     * @return 返回true表示导出成功，否则返回false。
     */
    public boolean exportDatabaseTool(String hostIP, String port, String userName, String password,
                                             String savePath, String fileName,	String databaseName) {
        File saveFile = new File(savePath);
        if (!saveFile.exists()) {// 文件夹不存在
            saveFile.mkdirs();// 创建文件夹
        }
        if (!savePath.endsWith(File.separator)) {
            savePath = savePath + File.separator;
        }
        String sqlfile=savePath+fileName;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("mysqldump").append(" -h").append(hostIP).append(" --port=").append(port);
        stringBuilder.append(" --user=").append(userName).append(" --password=").append(password)
            .append(" --lock-all-tables=true");
        stringBuilder.append(" --result-file=").append(sqlfile)
            .append(" --default-character-set=utf8mb4 ").append(databaseName);
        try {
            LOG.info(stringBuilder.toString());
            Process process = Runtime.getRuntime().exec(stringBuilder.toString());
            if (process.waitFor() == 0) {// 0 表示线程正常终止。
                return true;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            LOG.error(e.getLocalizedMessage());
        }
        return false;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insert(DBBackup dbBackup){
        dbBackupRepository.save(dbBackup);
    }

    @Scheduled(cron = "0 0 1 * * MON-FRI")
    public void backup(){
        LOG.warn("---------数据备份定时任务开始---------");
        DateTime date = DateTime.now();
        String ipAndPort = dbUrl.substring(dbUrl.indexOf("/")+2,dbUrl.lastIndexOf("/"));
        String[] szIPAndPort = ipAndPort.split(":");
        String dbHostIP = szIPAndPort[0];
        String port = szIPAndPort[1];
        String savePath = TikuConstants.CFSMS_HOME+File.separator+ DateTimeUtils.format(date.toDate(),DateTimeUtils.YEAR_MONTH_DAY);
        boolean ret = this.exportDatabaseTool(dbHostIP,port,dbUsername,dbPassword,savePath,"dump.sql","tiku");
        if (ret) {
            LOG.warn("数据备份成功");
            DBBackup dbBackup = new DBBackup();
            dbBackup.setCreatedBy("system");
            dbBackup.setCreatedTime(date.toDate().toInstant());
            dbBackup.setAuthority("ROLE_ADMIN");
            String filepath = savePath+File.separator+"dump.sql";
            File file = new File(filepath);
            dbBackup.setFilepath(filepath);
            dbBackup.setSize((int)(file.length()/1024));
            dbBackup.setDescription("system在"+DateTimeUtils.format(date.toDate(), DateTimeUtils.YEAR_MONTH_DAY_HOUR_CN)+"备份数据");
            this.insert(dbBackup);
        } else {
            LOG.warn("数据备份失败，备份路径：{}",savePath);
        }
        LOG.warn("---------数据备份定时任务结束---------");
    }
}
