package com.songzi.service;

import com.songzi.TikuApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TikuApp.class)
@ActiveProfiles("test")
public class ExamineServiceTest {

    @Autowired private ExamineService examineService;

    @Test
    public void answer() {
        examineService.getOne(90L);
    }
}
