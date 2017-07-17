package io.rebelsouls.integration.email;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import io.rebelsouls.core.users.User;
import io.rebelsouls.template.TemplateApp;
import io.rebelsouls.template.services.StorageService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes=TemplateApp.class)
public class StorageTest {

    @Autowired StorageService storageService;
    
    @Test
    public void testUploading() {
        User user = new User();
        storageService.store(user, "/testfile.txt", new File("/home/user/testfile.txt"));
    }
    
}
