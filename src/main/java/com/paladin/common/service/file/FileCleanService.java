package com.paladin.common.service.file;

import com.paladin.common.service.sys.SysAttachmentService;
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
    private TemporaryFileService temporaryFileService;

    @Autowired
    private SysAttachmentService sysAttachmentService;

    @Scheduled(cron = "0 0 2 * * ?")
    public void scheduledClean() {
        log.info("开始清理无效文件");

        // 临时文件定时清理
        if (temporaryFileService != null) {
            temporaryFileService.clearTemporaryFile();
        }

        // 清理附件
        sysAttachmentService.cleanAttachmentFile();
    }

}
