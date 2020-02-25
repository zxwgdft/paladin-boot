package com.paladin.common.config;

import com.paladin.common.core.upload.BigFileUploaderContainer;
import com.paladin.framework.io.TemporaryFileHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务配置
 *
 * @author TontoZhou
 * @since 2020/2/25
 */
@Slf4j
@Component
public class ScheduleConfiguration {

    @Autowired(required = false)
    private BigFileUploaderContainer bigFileUploaderContainer;

    @Autowired(required = false)
    private TemporaryFileHelper temporaryFileHelper;

    /**
     * 大文件上传清理任务
     */
    @Scheduled(cron = "0 0 */2 * * ?")
    public void scheduledCleanUploader() {
        if (bigFileUploaderContainer != null) {
            bigFileUploaderContainer.cleanUploader(60);
        }
    }

    /**
     * 临时文件定时清理
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void scheduledClean() {
        if (temporaryFileHelper != null) {
            temporaryFileHelper.clearTemporaryFile(30);
        }
    }

}
