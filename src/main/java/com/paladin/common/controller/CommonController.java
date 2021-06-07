package com.paladin.common.controller;

import com.paladin.common.core.ConstantsContainer;
import com.paladin.common.core.ConstantsContainer.KeyValue;
import com.paladin.common.core.FileResourceContainer;
import com.paladin.common.core.cache.DataCacheHelper;
import com.paladin.common.core.security.NeedAdmin;
import com.paladin.common.model.sys.SysAttachment;
import com.paladin.common.service.sys.SysAttachmentService;
import com.paladin.common.service.sys.vo.FileResource;
import com.paladin.framework.api.R;
import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.service.UserSession;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Api("通用操作管理")
@Controller
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private SysAttachmentService attachmentService;

    @ApiOperation(value = "通过code获取常量")
    @GetMapping("/constant")
    @ResponseBody
    public Map<String, List<KeyValue>> enumConstants(@RequestParam("code") String[] code) {
        return DataCacheHelper.getData(ConstantsContainer.class).getTypeConstants(code);
    }

    @ApiOperation(value = "通过ID获取附件")
    @GetMapping("/attachment/{id}")
    @ResponseBody
    public FileResource getAttachment(@PathVariable("id") String id) {
        return FileResourceContainer.getFileResource(id);
    }

    @ApiOperation(value = "通过ID获取多个附件")
    @GetMapping("/attachment")
    @ResponseBody
    public List<FileResource> getAttachments(@RequestParam("id[]") String[] ids) {
        return FileResourceContainer.getFileResources(ids);
    }

    @ApiOperation(value = "上传附件文件")
    @PostMapping("/upload/file")
    @ResponseBody
    public FileResource uploadFile(@RequestParam("file") MultipartFile file) {
        return FileResourceContainer.convert(attachmentService.createFile(file).get(0));
    }

    @ApiOperation(value = "上传多个附件文件")
    @PostMapping("/upload/files")
    @ResponseBody
    public FileResource[] uploadFiles(@RequestParam("files") MultipartFile[] files) {
        FileResource[] result = new FileResource[files.length];
        List<SysAttachment> attachments = attachmentService.createFile(files);
        int i = 0;
        for (SysAttachment attachment : attachments) {
            result[i] = FileResourceContainer.convert(attachment);
        }
        return result;
    }

    @ApiOperation(value = "清理文件")
    @GetMapping("/clean/attachment")
    @ResponseBody
    public Object cleanAttachment() {
        if (UserSession.getCurrentUserSession().isSystemAdmin()) {
            int count = attachmentService.cleanAttachmentFile();
            return R.success("成功清理附件文件" + count + "个");
        }
        throw new BusinessException("您没有权限执行该操作");
    }

    @ApiOperation(value = "重新加载容器数据缓存")
    @GetMapping("/container/restart")
    @ResponseBody
    @NeedAdmin
    public R restartContainer(@RequestParam String container) {
        if (UserSession.getCurrentUserSession().isSystemAdmin()) {
            long t1 = System.currentTimeMillis();
            DataCacheHelper.reloadCache(container);
            long t2 = System.currentTimeMillis();
            return R.success(t2 - t1);
        }
        throw new BusinessException("您没有权限执行该操作");
    }


    @ApiOperation(value = "异常测试")
    @GetMapping("/test/exception")
    public Object getException() {
        throw new BusinessException("异常测试，返回页面");
    }

    @ApiOperation(value = "异常测试")
    @PostMapping("/test/exception")
    @ResponseBody
    public Object postException() {
        throw new BusinessException("异常测试，返回json");
    }


}
