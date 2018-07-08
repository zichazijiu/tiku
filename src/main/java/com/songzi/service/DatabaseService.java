package com.songzi.service;

import com.songzi.domain.LogBackup;
import com.songzi.domain.enumeration.BakType;
import com.songzi.domain.enumeration.Level;
import com.songzi.domain.enumeration.LogType;
import com.songzi.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class DatabaseService {

    @Autowired
    private LogBackupSerivce logBackupSerivce;

    /**
     * linux 命令  在linux上可以直接执行  放在程序中就不行  问题待排查
     * @throws IOException
     * @throws InterruptedException
     */
    public Map doDatabaseBackup() throws IOException, InterruptedException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime now = LocalDateTime.now();
        String nowString = now.format(formatter);
        String path = "/root/server/databasesbackup/";
        String filename = "tiku"+nowString+".sql";
//        String[] cmds = {"mysqldump -h127.0.0.1 -P3306 -u'root' -p'Q!w2e3r4' tiku>"+path+filename};
//        Process pro =  Runtime.getRuntime().exec(cmds);
//        pro.waitFor();
//        InputStream in = pro.getInputStream();
//        BufferedReader read = new BufferedReader(new InputStreamReader(in));
//        String line = null;
//        while ((line = read.readLine()) != null) {
//            System.out.println("-------------"+line);
//
//        }
        Map map = new HashMap();
        map.put("path",path);
        map.put("filename",filename);
        map.put("status","success");

        LogBackup logBackup = new LogBackup();
        logBackup.setCreatedTime(Instant.now());
        logBackup.setCreatedBy(SecurityUtils.getCurrentUserLogin().get());
        logBackup.setDescription("数据库备份"+path+filename);
        logBackup.setSize(27);
        logBackup.setLevel(Level.INFO);
        logBackup.setAuthority("ROLE_ADMIN");
        logBackup.setLogType(LogType.DATABASE);
        logBackup.setBakType(BakType.DABABASE);
        logBackupSerivce.insert(logBackup);
        return map;
    }
}
