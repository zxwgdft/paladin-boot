package com.paladin.common.core;

import com.paladin.common.model.sys.SysAttachment;
import com.paladin.common.service.sys.SysAttachmentService;
import com.paladin.framework.spring.SpringContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AttachmentContainer implements SpringContainer {
    //TODO 在这里可以做缓存处理，从内存或redis获取
    @Autowired
    private SysAttachmentService attachmentService;

    private static AttachmentContainer container;

    public boolean initialize() {
        container = this;
        return true;
    }


    public static List<SysAttachment> getAttachments(String... ids) {
        return container.attachmentService.getAttachment(ids);
    }

    public static SysAttachment getAttachment(String id) {
        return container.attachmentService.get(id);
    }

}
