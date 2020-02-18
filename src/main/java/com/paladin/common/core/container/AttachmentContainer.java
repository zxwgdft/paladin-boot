package com.paladin.common.core.container;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.paladin.common.model.syst.SysAttachment;
import com.paladin.common.service.syst.SysAttachmentService;
import com.paladin.framework.spring.SpringContainer;

@Component
public class AttachmentContainer  implements SpringContainer{
	//TODO 在这里可以做缓存处理，从内存或redis获取
	@Autowired
	private SysAttachmentService attachmentService;

	private static AttachmentContainer container;
	
	public boolean initialize() {
		container = this;
		return true;
	}
	
	
	public static List<SysAttachment> getAttachments(String... ids){
		return container.attachmentService.getAttachment(ids);
	}
	
	public static SysAttachment getAttachment(String id){
		return container.attachmentService.get(id);
	}
	
}
