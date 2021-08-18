package com.paladin.common.service.file;

/**
 * 临时文件服务
 */
public interface TemporaryFileService {


    /**
     * 获取一个临时文件输出流
     *
     * @param fileName 可为null，实现方法应该在该文件名基础上增加额外部分来让文件生成不冲突
     * @param suffix   可为null，文件类型，文件后缀名
     * @return
     */
    TemporaryFileOutputStream getTemporaryFileOutputStream(String fileName, String suffix);


    /**
     * 清除临时文件，临时文件应该被定期清理
     */
    void clearTemporaryFile();

}
