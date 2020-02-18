package com.paladin.common.service.syst;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.paladin.common.mapper.syst.SysAttachmentMapper;
import com.paladin.common.model.syst.SysAttachment;
import com.paladin.common.service.syst.dto.PictureSaveConfig;
import com.paladin.framework.common.QueryType;
import com.paladin.framework.common.Condition;
import com.paladin.framework.common.OffsetPage;
import com.paladin.framework.common.PageResult;
import com.paladin.framework.core.ServiceSupport;
import com.paladin.framework.core.configuration.web.WebProperties;
import com.paladin.framework.core.exception.BusinessException;
import com.paladin.framework.core.exception.SystemException;
import com.paladin.framework.utils.Base64Util;
import com.paladin.framework.utils.uuid.UUIDUtil;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;

@Service
public class SysAttachmentService extends ServiceSupport<SysAttachment> {

	@Autowired
	private SysAttachmentMapper sysAttachmentMapper;

	@Autowired
	private WebProperties webProperties;

	private long maxFileByteSize;

	private int maxFileNameSize = 50;

	private String attachmentPath;

	private int maxFileSize;

	@PostConstruct
	protected void initialize() {
		attachmentPath = webProperties.getFilePath();
		if (attachmentPath.startsWith("file:")) {
			attachmentPath = attachmentPath.substring(5);
		}

		attachmentPath = attachmentPath.replaceAll("\\\\", "/");

		if (!attachmentPath.endsWith("/")) {
			attachmentPath += "/";
		}

		maxFileSize = webProperties.getFileMaxSize();
		if (maxFileSize <= 0) {
			maxFileSize = 10;
		}

		maxFileByteSize = maxFileSize * 1024 * 1024;

		// 创建目录
		Path root = Paths.get(attachmentPath);
		try {
			Files.createDirectories(root);
		} catch (IOException e) {
			throw new RuntimeException("创建附件存放目录异常", e);
		}
	}

	/**
	 * 创建附件（MultipartFile格式）
	 * 
	 * @param file
	 * @param attachmentName
	 * @return
	 */
	public SysAttachment createAttachment(MultipartFile file, String attachmentName) {
		return createAttachment(file, attachmentName, null);
	}

	/**
	 * 创建附件（MultipartFile格式）
	 * 
	 * @param file
	 * @param attachmentName
	 * @param userType
	 * @return
	 */
	public SysAttachment createAttachment(MultipartFile file, String attachmentName, Integer userType) {
		String id = UUIDUtil.createUUID();
		String name = file.getOriginalFilename();
		long size = file.getSize();
		if (size > maxFileByteSize) {
			throw new BusinessException("上传附件不能大于" + maxFileSize + "MB");
		}

		SysAttachment attachment = new SysAttachment();
		attachment.setId(id);
		attachment.setSize(size);
		attachment.setType(file.getContentType());

		if (name != null && name.length() > 0) {
			int i = name.lastIndexOf(".");
			if (i >= 0) {
				String suffix = name.substring(i);
				attachment.setSuffix(suffix);
			}

			if (attachmentName == null || attachmentName.length() == 0) {
				attachmentName = i >= 0 ? name.substring(0, i) : name;
			}
		}

		attachment.setName(attachmentName);

		if (userType == null || (userType != SysAttachment.USE_TYPE_COLUMN_RELATION && userType != SysAttachment.USE_TYPE_RESOURCE)) {
			userType = SysAttachment.USE_TYPE_COLUMN_RELATION;
		}

		attachment.setUseType(userType);

		try {
			saveFile(file.getBytes(), attachment);
		} catch (IOException e) {
			throw new SystemException("保存附件文件失败", e);
		}

		save(attachment);
		return attachment;
	}

	/**
	 * 创建附件（base64格式）
	 * 
	 * @param base64String
	 * @param filename
	 * @return
	 */
	public SysAttachment createAttachment(String base64String, String filename, String type) {
		return createAttachment(base64String, filename, null);
	}

	/**
	 * 创建附件（base64格式）
	 * 
	 * @param base64String
	 * @param filename
	 * @param userType
	 * @return
	 */
	public SysAttachment createAttachment(String base64String, String filename, String type, Integer userType) {
		String id = UUIDUtil.createUUID();
		long size = Base64Util.getFileSize(base64String);
		if (size > maxFileByteSize) {
			throw new BusinessException("上传附件不能大于" + maxFileSize + "MB");
		}

		SysAttachment attachment = new SysAttachment();
		attachment.setId(id);
		attachment.setSize(size);
		attachment.setType(type);

		if (filename != null && filename.length() > 0) {
			int i = filename.lastIndexOf(".");
			if (i >= 0) {
				String suffix = filename.substring(i);
				attachment.setSuffix(suffix);
				attachment.setName(filename.substring(0, i));
			} else {
				attachment.setName(filename);
			}
		} else {
			throw new BusinessException("文件名不能为空");
		}

		if (userType == null || (userType != SysAttachment.USE_TYPE_COLUMN_RELATION && userType != SysAttachment.USE_TYPE_RESOURCE)) {
			userType = SysAttachment.USE_TYPE_COLUMN_RELATION;
		}

		attachment.setUseType(userType);

		try {
			saveFile(Base64Util.decode(base64String), attachment);
		} catch (IOException e) {
			throw new SystemException("保存附件文件失败", e);
		}
		save(attachment);
		return attachment;
	}

	/**
	 * 创建附件（字节格式）
	 * 
	 * @param data
	 * @param filename
	 * @param type
	 * @return
	 */
	public SysAttachment createAttachment(byte[] data, String filename, String type) {
		return createAttachment(data, filename, type, null, null);
	}

	/**
	 * 创建附件（字节格式）
	 * 
	 * @param base64String
	 * @param filename
	 * @param subFilePath
	 *            子路径
	 * @return
	 */
	public SysAttachment createAttachment(byte[] data, String filename, String type, String subFilePath, Integer userType) {
		String id = UUIDUtil.createUUID();
		long size = data.length;
		if (size > maxFileByteSize) {
			throw new BusinessException("上传附件不能大于" + maxFileSize + "MB");
		}

		SysAttachment attachment = new SysAttachment();
		attachment.setId(id);
		attachment.setSize(size);
		attachment.setType(type);

		if (filename != null && filename.length() > 0) {
			int i = filename.lastIndexOf(".");
			if (i >= 0) {
				String suffix = filename.substring(i);
				attachment.setSuffix(suffix);
				attachment.setName(filename.substring(0, i));
			} else {
				attachment.setName(filename);
			}
		} else {
			throw new BusinessException("文件名不能为空");
		}

		if (userType == null || (userType != SysAttachment.USE_TYPE_COLUMN_RELATION && userType != SysAttachment.USE_TYPE_RESOURCE)) {
			userType = SysAttachment.USE_TYPE_COLUMN_RELATION;
		}
		attachment.setUseType(userType);

		try {
			saveFile(data, attachment, subFilePath);
		} catch (IOException e) {
			throw new SystemException("保存附件文件失败", e);
		}
		save(attachment);
		return attachment;
	}

	private static double baseSize = 1 * 1024 * 1024;

	/**
	 * 创建图片，过大图片会被压缩，压缩策略为根据图片大小与基准大小比例作为缩放大小进行缩放
	 * 
	 * @param file
	 * @param pictureName
	 * @param userType
	 * @return
	 */
	public SysAttachment createPictureAndCompress(MultipartFile file, String pictureName, Integer userType) {
		return createPictureAndCompress(file, pictureName, userType, null, null);
	}

	/**
	 * 创建图片，过大图片会被压缩，压缩策略为根据图片大小与基准大小比例作为缩放大小进行缩放
	 * 
	 * @param file
	 * @param pictureName
	 * @param userType
	 * @param thumbnailWidth
	 *            缩略图宽度，如果不需要则设置为null
	 * @param thumbnailHeight
	 *            缩略图高度，如果不需要则设置为null
	 * @return
	 */
	public SysAttachment createPictureAndCompress(MultipartFile file, String pictureName, Integer userType, Integer thumbnailWidth, Integer thumbnailHeight) {

		PictureSaveConfig config = null;

		long size = file.getSize();
		if (size > baseSize) {
			double scale = baseSize / size;
			scale = Math.sqrt(scale) * 0.8;
			config = new PictureSaveConfig();
			config.setScale(scale);
		}

		if (thumbnailHeight != null && thumbnailWidth != null) {
			if (config == null) {
				config = new PictureSaveConfig();
			}

			config.setThumbnailHeight(thumbnailHeight);
			config.setThumbnailWidth(thumbnailWidth);
		}

		return createPicture(file, pictureName, null, config);
	}

	/**
	 * 创建图片，过大图片会被压缩，压缩策略为根据图片大小与基准大小比例作为缩放大小进行缩放
	 * 
	 * @param base64str
	 * @param pictureName
	 * @param type
	 * @param userType
	 * @return
	 */
	public SysAttachment createPictureAndCompress(String base64str, String pictureName, String type, Integer userType) {
		return createPictureAndCompress(base64str, pictureName, type, userType, null, null);
	}

	/**
	 * 创建图片，过大图片会被压缩，压缩策略为根据图片大小与基准大小比例作为缩放大小进行缩放
	 * 
	 * @param base64str
	 * @param pictureName
	 * @param type
	 * @param userType
	 * @param thumbnailWidth
	 *            缩略图宽度，如果不需要则设置为null
	 * @param thumbnailHeight
	 *            缩略图高度，如果不需要则设置为null
	 * @return
	 */
	public SysAttachment createPictureAndCompress(String base64str, String pictureName, String type, Integer userType, Integer thumbnailWidth,
			Integer thumbnailHeight) {

		PictureSaveConfig config = null;

		long size = Base64Util.getFileSize(base64str);
		if (size > baseSize) {
			double scale = baseSize / size;
			scale = Math.sqrt(scale) * 0.8;
			config = new PictureSaveConfig();
			config.setScale(scale);
		}

		if (thumbnailHeight != null && thumbnailWidth != null) {
			if (config == null) {
				config = new PictureSaveConfig();
			}

			config.setThumbnailHeight(thumbnailHeight);
			config.setThumbnailWidth(thumbnailWidth);
		}

		return createPicture(base64str, pictureName, type, null, config);
	}

	/**
	 * 创建图片
	 * 
	 * @param file
	 * @param pictureName
	 * @param userType
	 * @param config
	 *            图片压缩配置，null则不压缩
	 * @return
	 */
	public SysAttachment createPicture(MultipartFile file, String pictureName, Integer userType, PictureSaveConfig config) {
		String id = UUIDUtil.createUUID();
		String name = file.getOriginalFilename();
		long size = file.getSize();
		if (size > maxFileByteSize) {
			throw new BusinessException("上传图片不能大于" + maxFileSize + "MB");
		}

		SysAttachment attachment = new SysAttachment();
		attachment.setId(id);
		attachment.setSize(size);
		attachment.setType(file.getContentType());

		if (name != null && name.length() > 0) {
			int i = name.lastIndexOf(".");
			if (i >= 0) {
				String suffix = name.substring(i);
				attachment.setSuffix(suffix);
			}

			if (pictureName == null || pictureName.length() == 0) {
				pictureName = i >= 0 ? name.substring(0, i) : name;
			}
		}

		attachment.setName(pictureName);

		if (userType == null || (userType != SysAttachment.USE_TYPE_COLUMN_RELATION && userType != SysAttachment.USE_TYPE_RESOURCE)) {
			userType = SysAttachment.USE_TYPE_COLUMN_RELATION;
		}

		attachment.setUseType(userType);

		try {
			savePicture(file.getInputStream(), attachment, null, config);
		} catch (IOException e) {
			throw new SystemException("保存图片文件失败", e);
		}

		save(attachment);
		return attachment;
	}

	/**
	 * 创建图片（base64）
	 * 
	 * @param base64str
	 * @param pictureName
	 * @param type
	 * @param userType
	 * @param config
	 *            图片压缩配置，null则不压缩
	 * @return
	 */
	public SysAttachment createPicture(String base64str, String pictureName, String type, Integer userType, PictureSaveConfig config) {
		String id = UUIDUtil.createUUID();
		long size = Base64Util.getFileSize(base64str);
		if (size > maxFileByteSize) {
			throw new BusinessException("上传附件不能大于" + maxFileSize + "MB");
		}

		SysAttachment attachment = new SysAttachment();
		attachment.setId(id);
		attachment.setSize(size);

		if (type == null || type.length() == 0) {
			type = "image/jpeg";
		}
		attachment.setType(type);

		if (pictureName != null && pictureName.length() > 0) {
			int i = pictureName.lastIndexOf(".");
			if (i >= 0) {
				String suffix = pictureName.substring(i);
				attachment.setSuffix(suffix);
				attachment.setName(pictureName.substring(0, i));
			} else {
				attachment.setName(pictureName);
			}
		} else {
			throw new BusinessException("文件名不能为空");
		}

		attachment.setName(pictureName);

		if (userType == null || (userType != SysAttachment.USE_TYPE_COLUMN_RELATION && userType != SysAttachment.USE_TYPE_RESOURCE)) {
			userType = SysAttachment.USE_TYPE_COLUMN_RELATION;
		}

		attachment.setUseType(userType);

		try {
			byte[] data = Base64Util.decode(base64str);
			savePicture(new ByteArrayInputStream(data), attachment, null, config);
		} catch (IOException e) {
			throw new SystemException("保存图片文件失败", e);
		}

		save(attachment);
		return attachment;
	}

	/**
	 * 创建图片，并根据条件缩放
	 * 
	 * @param input
	 * @param attachment
	 * @param subPath
	 * @param config
	 * @return
	 * @throws IOException
	 */
	private SysAttachment savePicture(InputStream input, SysAttachment attachment, String subPath, PictureSaveConfig config) throws IOException {

		Integer width = config == null ? null : config.getWidth();
		Integer height = config == null ? null : config.getHeight();
		Double scale = config == null ? null : config.getScale();
		Double quality = config == null ? null : config.getQuality();
		Integer thumbnailWidth = config == null ? null : config.getThumbnailWidth();
		Integer thumbnailHeight = config == null ? null : config.getThumbnailHeight();

		String filename = attachment.getId();
		String suffix = attachment.getSuffix();
		if (suffix != null) {
			filename += suffix;
		}

		if (subPath == null || subPath.length() == 0) {
			subPath = format.format(new Date());
		}

		Path path = Paths.get(attachmentPath, subPath);
		if (!Files.exists(path)) {
			try {
				Files.createDirectory(path);
			} catch (FileAlreadyExistsException e1) {
				// 继续
			}
		}

		String pelativePath = subPath + "/" + filename;
		attachment.setPelativePath(pelativePath);
		attachment.setCreateTime(new Date());

		if (thumbnailWidth != null && thumbnailHeight != null) {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			byte[] buffer = new byte[2048];
			int len;
			while ((len = input.read(buffer)) > -1) {
				output.write(buffer, 0, len);
			}

			// 创建缩略图
			input = new ByteArrayInputStream(output.toByteArray());

			String thumbnailPelativePath = subPath + "/t_" + filename;
			try (OutputStream out = Files.newOutputStream(Paths.get(attachmentPath + thumbnailPelativePath))) {
				Thumbnails.of(input).size(thumbnailWidth, thumbnailHeight).toOutputStream(out);
			}

			attachment.setThumbnailPelativePath(thumbnailPelativePath);
			input.reset();
		}

		if ((width != null && height != null) || scale != null || quality != null) {
			// 有修改图片则使用缩放图片工具
			boolean changed = false;
			Builder<? extends InputStream> builder = Thumbnails.of(input);
			if (width != null && height != null) {
				builder.size(width, height);
				changed = true;
			}

			if (scale != null) {
				builder.scale(scale, scale);
				changed = true;
			}

			if (quality != null) {
				if (!changed) {
					// 如果没有设置size或缩放但是要压缩质量，则缩放原大小
					builder.scale(1f);
				}
				builder.outputQuality(quality);
			}

			try (OutputStream out = Files.newOutputStream(Paths.get(attachmentPath + pelativePath))) {
				builder.toOutputStream(out);
			}
		} else {
			// 直接保存原图片
			try (OutputStream out = Files.newOutputStream(Paths.get(attachmentPath + pelativePath))) {
				byte[] buffer = new byte[2048];
				int len;
				while ((len = input.read(buffer)) > -1) {
					out.write(buffer, 0, len);
				}
			}
		}

		// 附件名称太长则截取
		String attachmentName = attachment.getName();
		if (attachmentName != null && attachmentName.length() > maxFileNameSize) {
			attachmentName = attachmentName.substring(0, maxFileNameSize);
		}

		return attachment;
	}

	/**
	 * 保存文件，并生成相对路径到附件数据中，子路径为当前日期
	 * 
	 * @param data
	 * @param attachment
	 * @throws IOException
	 */
	private void saveFile(byte[] data, SysAttachment attachment) throws IOException {
		saveFile(data, attachment, null);
	}

	/*
	 * TODO 缓存当前已经判断过存在的日期文件夹，防止重复判断，如果有人为删除文件夹则会出现问题
	 * 
	 */
	@SuppressWarnings("unused")
	private Path currentPath = null;
	private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

	/**
	 * 保存文件，并生成相对路径到附件数据中
	 * 
	 * @param data
	 * @param attachment
	 * @param subPath
	 * @throws IOException
	 */
	private void saveFile(byte[] data, SysAttachment attachment, String subPath) throws IOException {
		if (data == null || data.length == 0) {
			throw new SystemException("文件为空");
		}

		String filename = attachment.getId();
		String suffix = attachment.getSuffix();
		if (suffix != null) {
			filename += suffix;
		}

		if (subPath == null || subPath.length() == 0) {
			subPath = format.format(new Date());
		}

		Path path = Paths.get(attachmentPath, subPath);
		if (!Files.exists(path)) {
			try {
				Files.createDirectory(path);
			} catch (FileAlreadyExistsException e1) {
				// 继续
			}
		}

		String pelativePath = subPath + "/" + filename;
		Files.write(Paths.get(attachmentPath + pelativePath), data);

		attachment.setPelativePath(pelativePath);
		attachment.setCreateTime(new Date());

		// 附件名称太长则截取
		String attachmentName = attachment.getName();
		if (attachmentName != null && attachmentName.length() > maxFileNameSize) {
			attachmentName = attachmentName.substring(0, maxFileNameSize);
		}
	}

	/**
	 * 获取文件附件记录
	 * 
	 * @param ids
	 * @return
	 */
	public List<SysAttachment> getAttachment(String... ids) {
		return searchAll(new Condition(SysAttachment.COLUMN_FIELD_ID, QueryType.IN, Arrays.asList(ids)));
	}

	/**
	 * 获取文件附件记录
	 * 
	 * @param ids
	 * @return
	 */
	public PageResult<SysAttachment> getResourceImagePage(OffsetPage query) {
		Page<SysAttachment> page = PageHelper.offsetPage(query.getOffset(), query.getLimit());
		sysAttachmentMapper.findImageResource();
		return new PageResult<>(page);
	}

	/**
	 * 通过拼接字符串查出附件或者创建新的附件
	 * 
	 * @param idString
	 * @param attachmentFiles
	 * @return
	 */
	public List<SysAttachment> checkOrCreateAttachment(String idString, MultipartFile... attachmentFiles) {

		List<SysAttachment> attIds = null;

		if (idString != null && idString.length() != 0) {
			attIds = getAttachment(idString.split(","));

		}

		if (attachmentFiles != null) {
			if (attIds == null) {
				attIds = new ArrayList<>(attachmentFiles.length);
			}
			for (MultipartFile file : attachmentFiles) {
				if(file != null) {
					SysAttachment a = createAttachment(file, null);
					attIds.add(a);
				}
			}
		}

		return attIds;
	}

	/**
	 * 拼接附件ID字符串
	 * 
	 * @param attachments
	 * @return
	 */
	public String splicingAttachmentId(List<SysAttachment> attachments) {
		if (attachments != null && attachments.size() > 0) {
			String str = "";
			for (SysAttachment attachment : attachments) {
				str += attachment.getId() + ",";
			}
			return str.substring(0, str.length() - 1);
		}
		return null;
	}

}