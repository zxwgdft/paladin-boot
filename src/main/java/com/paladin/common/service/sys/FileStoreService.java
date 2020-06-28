package com.paladin.common.service.sys;

import java.io.InputStream;

/**
 * @author TontoZhou
 * @since 2020/6/28
 */
public interface FileStoreService {

    /**
     * 存储文件
     *
     * @param input    输入流
     * @param filePath 文件地址（可以是相对地址）
     * @param fileName 文件名称
     */
    void storeFile(InputStream input, String filePath, String fileName);

    /**
     * 检查文件目录是否存在，不存在则创建
     *
     * @param filePath 文件目录地址
     */
    void checkAndMakeDirectory(String filePath);

}
