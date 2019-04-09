package com.songzi.service;

import com.songzi.domain.LogBackup;
import com.songzi.domain.enumeration.BakType;
import com.songzi.domain.enumeration.Level;
import com.songzi.domain.enumeration.LogType;
import com.songzi.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class DatabaseService {

    @Autowired
    private LogBackupService logBackupService;

    /**
     * linux 命令  在linux上可以直接执行  放在程序中就不行  问题待排查
     * @return
     */
    public String doDatabaseBackup() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime now = LocalDateTime.now();
        String nowString = now.format(formatter);
        String path = "/root/server/databasesbackup/";
        String filename = "tiku"+nowString+".sql";
        String cmds =  "cmd /c mysqldump -uroot -p'Q!w2e3r4' tiku>"+path+filename;
        try {
            Process pro = Runtime.getRuntime().exec(cmds);

            LogBackup logBackup = new LogBackup();
            logBackup.setCreatedTime(Instant.now());
            logBackup.setCreatedBy(SecurityUtils.getCurrentUserLogin().get());
            logBackup.setDescription("数据库备份"+path+filename);
            logBackup.setSize(27);
            logBackup.setLevel(Level.INFO);
            logBackup.setAuthority("ROLE_ADMIN");
            logBackup.setLogType(LogType.DATABASE);
            logBackup.setBakType(BakType.DABABASE);
            logBackupService.insert(logBackup);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
}
