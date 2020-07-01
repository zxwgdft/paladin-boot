package com.paladin.common.service.sys;

import com.paladin.common.model.sys.SysAttachment;
import com.paladin.common.service.core.FileStoreService;
import com.paladin.common.service.sys.dto.FileCreateParam;
import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.service.Condition;
import com.paladin.framework.service.QueryType;
import com.paladin.framework.service.ServiceSupport;
import com.paladin.framework.utils.StringUtil;
import com.paladin.framework.utils.UUIDUtil;
import com.paladin.framework.utils.convert.DateFormatUtil;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class SysAttachmentService extends ServiceSupport<SysAttachment> {

    // 单位M
    @Value("${paladin.file.max-file-size:10}")
    private int maxFileSize;
    private int maxFileNameSize = 100;
    private int maxFileByteSize;

    @Autowired
    private FileStoreService fileStoreService;

    @PostConstruct
    protected void initialize() {
        if (maxFileSize <= 0) {
            maxFileSize = 10;
        }
        maxFileByteSize = maxFileSize * 1024 * 1024;
    }

    /**
     * 创建附件
     */
    public List<SysAttachment> createAttachments(MultipartFile[] files) {
        List<SysAttachment> attachments = new ArrayList<>(files.length);
        for (MultipartFile file : files) {
            attachments.add(createAttachment(file, null));
        }
        return attachments;
    }

    /**
     * 创建附件
     */
    public SysAttachment createAttachment(MultipartFile file) {
        return createAttachment(file, null);
    }

    /**
     * 创建附件
     */
    public SysAttachment createAttachment(MultipartFile file, String filename) {
        FileCreateParam param = new FileCreateParam();
        param.setFileContent(file);
        param.setType(SysAttachment.TYPE_FILE);
        if (filename != null && filename.length() > 0) {
            param.setFilename(filename);
        }
        return createFile(param);
    }


    /**
     * 创建附件
     */
    public SysAttachment createAttachment(String base64str, String filename) {
        FileCreateParam param = new FileCreateParam();
        param.setFileContent(base64str);
        param.setType(SysAttachment.TYPE_FILE);
        if (filename != null && filename.length() > 0) {
            param.setFilename(filename);
        }
        return createFile(param);
    }


    /**
     * 图片限制参数,如果不想限制可以设置大点
     */
    private double max_picture_size = 2 * 1024 * 1024;
    private int max_thumbnail_width = 600;
    private int max_thumbnail_height = 600;
    private int min_thumbnail_width = 200;
    private int min_thumbnail_height = 200;

    /**
     * 创建图片与缩略图
     */
    public SysAttachment createPictureAndThumbnail(MultipartFile file) {
        return createPictureAndThumbnail(file, null, min_thumbnail_width, min_thumbnail_height);
    }

    /**
     * 创建图片与缩略图
     */
    public SysAttachment createPictureAndThumbnail(MultipartFile file, String filename) {
        return createPictureAndThumbnail(file, filename, min_thumbnail_width, min_thumbnail_height);
    }

    /**
     * 创建图片与缩略图
     */
    public SysAttachment createPictureAndThumbnail(MultipartFile file, String filename, Integer thumbnailWidth,
                                                   Integer thumbnailHeight) {
        FileCreateParam param = new FileCreateParam();
        param.setFileContent(file);
        if (filename != null && filename.length() > 0) {
            param.setFilename(filename);
        }
        return createPictureAndThumbnail(param, thumbnailWidth, thumbnailHeight);
    }

    /**
     * 创建图片与缩略图
     */
    public SysAttachment createPictureAndThumbnail(String base64str, String filename) {
        return createPictureAndThumbnail(base64str, filename, min_thumbnail_width, min_thumbnail_height);
    }

    /**
     * 创建图片与缩略图
     *
     * @param base64str
     * @param filename
     * @param thumbnailWidth  缩略图宽度，如果不需要则设置为null
     * @param thumbnailHeight 缩略图高度，如果不需要则设置为null
     * @return
     */
    public SysAttachment createPictureAndThumbnail(String base64str, String filename, Integer thumbnailWidth, Integer thumbnailHeight) {
        FileCreateParam param = new FileCreateParam();
        param.setFileContent(base64str);
        return createFile(param);
    }

    /**
     * 创建图片与缩略图，如果开启限制图片，过大图片会被压缩，压缩策略为根据图片大小与基准大小比例作为缩放大小进行缩放
     *
     * @param param
     * @param thumbnailWidth  缩略图宽度，如果不需要则设置为null
     * @param thumbnailHeight 缩略图高度，如果不需要则设置为null
     * @return
     */
    private SysAttachment createPictureAndThumbnail(FileCreateParam param, Integer thumbnailWidth, Integer thumbnailHeight) {
        param.setType(SysAttachment.TYPE_IMAGE);
        long size = param.getSize();

        if (size > max_picture_size) {
            double scale = max_picture_size / size;
            scale = Math.sqrt(scale) * 0.8;
            param.setScale(scale);
        }

        if (thumbnailHeight == null) {
            thumbnailHeight = min_thumbnail_height;
        }

        if (thumbnailWidth == null) {
            thumbnailWidth = min_thumbnail_width;
        }

        param.setThumbnailHeight(Math.min(thumbnailHeight, max_thumbnail_height));
        param.setThumbnailWidth(Math.min(thumbnailWidth, max_thumbnail_width));

        return createFile(param);
    }


    /**
     * 创建文件附件
     *
     * @param param
     * @return
     */
    public SysAttachment createFile(FileCreateParam param) {
        if (param.getSize() > maxFileByteSize) {
            throw new BusinessException("上传文件不能大于" + maxFileSize + "MB");
        }
        SysAttachment attachment = new SysAttachment();
        attachment.setId(UUIDUtil.createUUID());
        attachment.setSize(param.getSize());
        attachment.setType(param.getType());
        attachment.setCreateTime(new Date());
        attachment.setDeleted(false);

        String filename = param.getFilename();
        if (filename != null && filename.length() > 0) {
            int i = filename.lastIndexOf(".");
            if (i >= 0) {
                attachment.setSuffix(filename.substring(i));
                attachment.setName(filename.substring(0, i));
            } else {
                attachment.setName(filename);
            }
        } else {
            throw new BusinessException("文件名不能为空");
        }

        filename = attachment.getName();
        if (filename.length() > maxFileNameSize) {
            attachment.setName(filename.substring(0, maxFileNameSize));
        }

        try {
            saveFile(param, attachment, null);
        } catch (IOException e) {
            throw new BusinessException("保存图片文件失败", e);
        }

        attachment.setStoreType(fileStoreService.getStoreType());
        save(attachment);
        return attachment;
    }

    /**
     * 保存文件附件
     *
     * @param param
     * @param attachment
     * @param subPath
     * @return
     * @throws IOException
     */
    private SysAttachment saveFile(FileCreateParam param, SysAttachment attachment, String subPath) throws IOException {
        String filename = attachment.getId();
        String suffix = attachment.getSuffix();
        if (suffix != null) {
            filename += suffix;
        }

        if (subPath == null || subPath.length() == 0) {
            subPath = DateFormatUtil.getThreadSafeFormat("yyyyMMdd").format(new Date());
        }

        fileStoreService.checkAndMakeDirectory(subPath);

        String relativePath = subPath + "/" + filename;
        attachment.setRelativePath(relativePath);

        boolean created = false;
        InputStream input = param.getInput();

        // 如果是图片类型，需要查看图片相关处理参数
        if (param.getType() == SysAttachment.TYPE_IMAGE) {
            Integer width = param.getWidth();
            Integer height = param.getHeight();
            Double scale = param.getScale();
            Double quality = param.getQuality();
            Integer thumbnailWidth = param.getThumbnailWidth();
            Integer thumbnailHeight = param.getThumbnailHeight();

            // 根据以下两个字段创建缩略图，并且缩略图是建立在原图基础上的，而不是改变过质量的原图
            // 如需要修改缩略图质量和规模，可加入参数
            if (thumbnailWidth != null && thumbnailHeight != null) {
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                byte[] buffer = new byte[2048];
                int len;
                while ((len = input.read(buffer)) > -1) {
                    output.write(buffer, 0, len);
                }

                // 创建缩略图
                input = new ByteArrayInputStream(output.toByteArray());


                output = new ByteArrayOutputStream();
                Thumbnails.of(input).size(thumbnailWidth, thumbnailHeight).toOutputStream(output);

                input = new ByteArrayInputStream(output.toByteArray());

                String thumbnailName = "t_" + filename;
                String thumbnailRelativePath = subPath + "/" + thumbnailName;

                fileStoreService.storeFile(input, subPath, thumbnailName);

                attachment.setThumbnailRelativePath(thumbnailRelativePath);
                input.reset();
            }

            // 根据以下字段修改原图
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


                ByteArrayOutputStream output = new ByteArrayOutputStream();
                builder.toOutputStream(output);

                input = new ByteArrayInputStream(output.toByteArray());
                fileStoreService.storeFile(input, subPath, filename);
                created = true;
            }
        }

        if (!created) {
            fileStoreService.storeFile(input, subPath, filename);
        }

        return attachment;
    }


    /**
     * 获取文件附件记录
     *
     * @param ids
     * @returns
     */
    public List<SysAttachment> getAttachments(String... ids) {
        return searchAll(new Condition(SysAttachment.FIELD_ID, QueryType.IN, Arrays.asList(ids)));
    }

    /**
     * 删除附件
     *
     * @param ids
     * @return
     */
    public int deleteAttachments(String... ids) {
        if (ids != null && ids.length > 0) {
            Example example = buildOrCreateExample(new Condition(SysAttachment.FIELD_ID, QueryType.IN, Arrays.asList(ids)), modelType, false);
            SysAttachment attachment = new SysAttachment();
            attachment.setDeleted(true);
            attachment.setDeleteTime(new Date());
            return getSqlMapper().updateByExampleSelective(attachment, example);
        }
        return 0;
    }


    /**
     * 合并附件
     *
     * @param newIds
     * @param attachmentFiles
     * @return
     */
    public List<SysAttachment> mergeAttachments(String newIds, MultipartFile... attachmentFiles) {
        return replaceAndMergeAttachments(null, newIds, attachmentFiles);
    }

    /**
     * 替换和合并附件
     * <p>
     * 会删除附件，所以需要调用该方法时需要考虑是否要添加事务
     *
     * @param originIds
     * @param newIds
     * @param attachmentFiles
     * @return
     */
    public List<SysAttachment> replaceAndMergeAttachments(String originIds, String newIds, MultipartFile... attachmentFiles) {
        List<SysAttachment> newAttList = null;
        if (newIds != null && newIds.length() != 0) {
            newAttList = getAttachments(newIds.split(","));
        }

        if (newAttList == null) {
            newAttList = new ArrayList<>(attachmentFiles != null ? attachmentFiles.length : 0);
        }

        ArrayList<String> deleteIdList = new ArrayList<>();
        if (originIds != null && originIds.length() > 0) {
            String[] originIdArray = originIds.split(",");
            for (String oid : originIdArray) {
                boolean del = true;
                for (SysAttachment att : newAttList) {
                    if (att.getId().equals(oid)) {
                        del = false;
                        break;
                    }
                }
                if (del) {
                    deleteIdList.add(oid);
                }
            }

            if (deleteIdList.size() > 0) {
                deleteAttachments(deleteIdList.toArray(new String[deleteIdList.size()]));
            }
        }

        if (attachmentFiles != null) {
            for (MultipartFile file : attachmentFiles) {
                if (file != null) {
                    SysAttachment a = createAttachment(file);
                    newAttList.add(a);
                }
            }
        }

        return newAttList;
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

    /**
     * 清理删除的附件文件
     */
    public int cleanAttachmentFile() {
        int count = 0;
        List<SysAttachment> list = searchAll(
                new Condition[]{
                        new Condition(SysAttachment.FIELD_DELETED, QueryType.EQUAL, true),
                        new Condition(SysAttachment.FIELD_DELETE_TIME, QueryType.LESS_THAN, new Date(System.currentTimeMillis() - 10 * 60 * 1000)),
                },
                SysAttachment.class, true);

        for (SysAttachment att : list) {
            String id = att.getId();
            if (deleteFile(id, att.getRelativePath()) &&
                    deleteFile(id, att.getThumbnailRelativePath())) {
                getSqlMapper().deleteByPrimaryKey(id);
                count++;
            }
        }

        return count;
    }

    private boolean deleteFile(String id, String filePath) {
        try {
            if (StringUtil.isNotEmpty(filePath)) {
                int i = filePath.lastIndexOf('/');
                if (i >= 0) {
                    fileStoreService.deleteFile(filePath.substring(0, i), filePath.substring(i + 1));
                } else {
                    fileStoreService.deleteFile("", filePath);
                }
            }
            return true;
        } catch (Exception e) {
            log.error("删除附件失败[id:" + id + ",path:" + filePath + "]");
        }
        return false;
    }

}