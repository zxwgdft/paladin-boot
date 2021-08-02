package com.paladin.common.service.file;

import java.io.OutputStream;

public abstract class TemporaryFileOutputStream extends OutputStream {

    /**
     * 获取文件url
     *
     * @return
     */
    public abstract String getFileUrl();

}
