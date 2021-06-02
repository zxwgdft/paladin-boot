package com.paladin.common.controller.upload;


import com.paladin.common.core.upload.BigFileUploader;
import com.paladin.common.core.upload.BigFileUploaderContainer;
import com.paladin.framework.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@Controller
@RequestMapping("/common/upload")
public class BigFileUploadController {

    @Autowired
    private BigFileUploaderContainer bigFileUploaderContainer;

    @GetMapping(value = "/index")
    public Object index() {
        return "/common/upload/index";
    }

    @PostMapping("/check")
    @ResponseBody
    public Object uploadCheck(WebUploadParam param) {
        try {
            BigFileUploader uploader = bigFileUploaderContainer.getOrCreateUploader(param.getMd5(), param.getChunks(), param.getName());
            int status = uploader.isCompleted() ? BigFileUploader.UPLOAD_COMPLETED : BigFileUploader.UPLOAD_SUCCESS;
            HashMap<String, Object> result = new HashMap<>();
            result.put("status", status);
            result.put("result", uploader.getUploadedChunk());
            return R.success(result);
        } catch (Exception e) {
            return R.success(BigFileUploader.UPLOAD_ERROR);
        }
    }

    @PostMapping("/chunk")
    @ResponseBody
    public Object uploadChunk(WebUploadParam param) {
        int result;
        try {
            result = bigFileUploaderContainer.uploadFileChunk(param.getMd5(), param.getChunk(), param.getFile().getBytes());
        } catch (Exception e) {
            result = BigFileUploader.UPLOAD_ERROR;
        }
        return R.success(result);
    }

    @GetMapping(value = "/find/file")
    @ResponseBody
    public Object findVideos() {
        return R.success(bigFileUploaderContainer.findAllFiles());
    }

    @GetMapping(value = "/delete/file")
    @ResponseBody
    public Object removeVideos(String pelativePath) {
        return R.success(bigFileUploaderContainer.deleteFile(pelativePath));
    }
}
