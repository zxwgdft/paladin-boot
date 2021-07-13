package com.paladin.common.service.core;

import com.paladin.common.service.sys.SysAttachmentService;
import com.paladin.framework.io.TemporaryFileHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author TontoZhou
 * @since 2020/6/30
 */
@Slf4j
@Service
public class FileCleanService {

    @Autowired(required = false)
    private TemporaryFileHelper temporaryFileHelper;

    @Autowired
    private SysAttachmentService sysAttachmentService;

    @Scheduled(cron = "0 0 2 * * ?")
    public void scheduledClean() {

        log.info("开始清理无效文件");

        // 临时文件定时清理
        if (temporaryFileHelper != null) {
            temporaryFileHelper.clearTemporaryFile(30);
        }

        // 清理附件
        sysAttachmentService.cleanAttachmentFile();
    }

}
