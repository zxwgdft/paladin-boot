package com.paladin.common.controller;

import com.paladin.common.core.ConstantsContainer;
import com.paladin.common.core.ConstantsContainer.KeyValue;
import com.paladin.common.core.FileResourceContainer;
import com.paladin.common.model.sys.SysAttachment;
import com.paladin.common.service.sys.SysAttachmentService;
import com.paladin.common.service.sys.vo.FileResource;
import com.paladin.framework.common.HttpCode;
import com.paladin.framework.common.R;
import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.service.UserSession;
import com.paladin.framework.service.VersionContainerManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
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
    public R<Map<String, List<KeyValue>>> enumConstants(@RequestParam("code") String[] code) {
        return R.success(ConstantsContainer.getTypeConstants(code));
    }

    @ApiOperation(value = "通过ID获取附件")
    @GetMapping("/attachment/{id}")
    @ResponseBody
    public R<FileResource> getAttachment(@PathVariable("id") String id) {
        return R.success(FileResourceContainer.getFileResource(id));
    }

    @ApiOperation(value = "通过ID获取多个附件")
    @GetMapping("/attachment")
    @ResponseBody
    public R<List<FileResource>> getAttachments(@RequestParam("id[]") String[] ids) {
        return R.success(FileResourceContainer.getFileResources(ids));
    }

    @ApiOperation(value = "上传附件文件")
    @PostMapping("/upload/file")
    @ResponseBody
    public R<FileResource> uploadFile(@RequestParam("file") MultipartFile file) {
        return R.success(FileResourceContainer.convert(attachmentService.createAttachment(file)));
    }

    @ApiOperation(value = "上传多个附件文件")
    @PostMapping("/upload/files")
    @ResponseBody
    public R<FileResource[]> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        FileResource[] result = new FileResource[files.length];
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            result[i] = FileResourceContainer.convert(attachmentService.createAttachment(file));
        }
        return R.success(result);
    }

    @ApiOperation(value = "上传base64格式的附件文件")
    @PostMapping("/upload/file/base64")
    @ResponseBody
    public R<FileResource> uploadFileByBase64(@RequestParam String fileStr, @RequestParam(required = false) String filename) {
        if (fileStr == null || fileStr.length() == 0) {
            return R.fail("上传文件为空");
        }
        SysAttachment result = attachmentService.createAttachment(fileStr, filename == null || filename.length() == 0 ? "附件" : filename);
        return R.success(FileResourceContainer.convert(result));
    }

    @ApiOperation(value = "上传图片，图片过大会被压缩")
    @PostMapping("/upload/picture")
    @ResponseBody
    public R<FileResource> uploadPicture(@RequestParam("file") MultipartFile file,
                                         @RequestParam(required = false) Integer thumbnailWidth,
                                         @RequestParam(required = false) Integer thumbnailHeight) {
        return R.success(FileResourceContainer.convert(attachmentService.createPictureAndThumbnail(file, null, thumbnailWidth, thumbnailHeight)));
    }

    @ApiOperation(value = "上传base64格式图片，图片过大会被压缩")
    @PostMapping("/upload/picture/base64")
    @ResponseBody
    public R<FileResource> uploadPictureBase64(@RequestParam String base64Str, @RequestParam(required = false) String filename,
                                               @RequestParam(required = false) Integer thumbnailWidth, @RequestParam(required = false) Integer thumbnailHeight) {
        return R.success(FileResourceContainer.convert(attachmentService.createPictureAndThumbnail(base64Str, filename, thumbnailWidth, thumbnailHeight)));
    }

    @GetMapping("/container/index")
    public String containerIndex() {
        if (UserSession.getCurrentUserSession().isSystemAdmin()) {
            return "/common/container/index";
        }
        return "/common/error/error";
    }

    @ApiOperation(value = "获取所有容器")
    @GetMapping("/container/find/all")
    @ResponseBody
    public R findContainers() {
        if (UserSession.getCurrentUserSession().isSystemAdmin()) {
            List<VersionContainerManager.VersionObject> versionObjects = VersionContainerManager.getVersionObjects();
            List<Map<String, Object>> result = new ArrayList<>();

            for (VersionContainerManager.VersionObject versionObject : versionObjects) {
                Map<String, Object> objectMap = new HashMap<>();
                objectMap.put("id", versionObject.getId());
                objectMap.put("updateTime", versionObject.getUpdateTime());
                objectMap.put("version", versionObject.getVersion());
                result.add(objectMap);
            }
            return R.success(result);
        }
        return R.fail(HttpCode.UNAUTHORIZED);
    }

    @ApiOperation(value = "重启容器")
    @GetMapping("/container/restart")
    @ResponseBody
    public R restartContainer(@RequestParam String container) {
        if (UserSession.getCurrentUserSession().isSystemAdmin()) {
            long t1 = System.currentTimeMillis();
            VersionContainerManager.versionChanged(container);
            long t2 = System.currentTimeMillis();
            return R.success(t2 - t1);
        }
        return R.fail(HttpCode.UNAUTHORIZED);
    }

    @ApiOperation(value = "重启所有容器")
    @GetMapping("/container/restart/all")
    @ResponseBody
    public R restartAllContainer() {
        if (UserSession.getCurrentUserSession().isSystemAdmin()) {
            long t1 = System.currentTimeMillis();
            VersionContainerManager.versionChanged();
            long t2 = System.currentTimeMillis();
            return R.success(t2 - t1);
        }
        return R.fail(HttpCode.UNAUTHORIZED);
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
