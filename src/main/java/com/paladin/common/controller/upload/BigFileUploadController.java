package com.paladin.common.controller.upload;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.paladin.common.service.upload.BigFileUploader;
import com.paladin.common.service.upload.BigFileUploaderContainer;
import com.paladin.framework.web.response.CommonResponse;

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
			return new CommonResponse(status, uploader.getUploadedChunk(), "");
		} catch (Exception e) {
			return new CommonResponse(BigFileUploader.UPLOAD_ERROR);
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
		return CommonResponse.getSuccessResponse("", result);
	}

	
	@GetMapping(value = "/find/file")
	@ResponseBody
	public Object findVideos() {
		return CommonResponse.getSuccessResponse(bigFileUploaderContainer.findAllFiles());
	}

	@GetMapping(value = "/delete/file")
	@ResponseBody
	public Object removeVideos(String pelativePath) {
		return CommonResponse.getSuccessResponse(bigFileUploaderContainer.deleteFile(pelativePath));
	}
}
