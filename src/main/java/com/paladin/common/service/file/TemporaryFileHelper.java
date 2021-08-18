package com.paladin.common.service.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class TemporaryFileHelper {

    private static TemporaryFileService staticTemporaryFileService;

    @Autowired(required = false)
    private TemporaryFileService temporaryFileService;

    @PostConstruct
    public void init() {
        staticTemporaryFileService = temporaryFileService;
    }

    public static TemporaryFileOutputStream getTemporaryFileOutputStream(String fileName, String suffix) {
        return staticTemporaryFileService.getTemporaryFileOutputStream(fileName, suffix);
    }


}
