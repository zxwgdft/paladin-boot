package com.paladin.common.service.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 简单操作FTP工具类 ,此工具类支持中文文件名，不支持中文目录
 * 如果需要支持中文目录，需要 new String(path.getBytes("UTF-8"),"ISO-8859-1") 对目录进行转码
 *
 * @author WZH
 */
@Slf4j
public class FtpHelper {


    /**
     * 获取FTPClient对象
     *
     * @param ftpHost     服务器IP
     * @param ftpPort     服务器端口号
     * @param ftpUserName 用户名
     * @param ftpPassword 密码
     * @return FTPClient
     */
    public static FTPClient getFTPClient(String ftpHost, int ftpPort,
                                         String ftpUserName, String ftpPassword) {
        FTPClient ftp = null;
        try {
            ftp = new FTPClient();
            // 设置连接超时时间,5000毫秒
            ftp.setConnectTimeout(50000);
            // 设置中文编码集，防止中文乱码
            ftp.setControlEncoding("UTF-8");
            // 连接FPT服务器,设置IP及端口
            ftp.connect(ftpHost, ftpPort);
            // 设置用户名和密码
            ftp.login(ftpUserName, ftpPassword);
            if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                log.error("未连接到FTP，用户名或密码错误");
                ftp.disconnect();
            }
        } catch (Exception e) {
            log.error("连接FTP服务器失败", e);
        }

        return ftp;
    }

    /**
     * 关闭FTP方法
     *
     * @param ftp
     * @return
     */
    public static boolean closeFTP(FTPClient ftp) {

        try {
            ftp.logout();
        } catch (Exception e) {
            log.error("FTP关闭失败");
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    log.error("FTP关闭失败");
                }
            }
        }

        return false;

    }


    /**
     * 下载FTP下指定文件
     *
     * @param ftp      FTPClient对象
     * @param filePath FTP文件路径
     * @param fileName 文件名
     * @param output   输出流
     * @return
     */
    public static boolean downLoad(FTPClient ftp, OutputStream output, String filePath, String fileName) {
        // 默认失败
        boolean flag = false;

        try {
            // 跳转到文件目录
            ftp.changeWorkingDirectory(filePath);
            // 获取目录下文件集合
            ftp.enterLocalPassiveMode();
            FTPFile[] files = ftp.listFiles();
            for (FTPFile file : files) {
                // 取得指定文件并下载
                if (file.getName().equals(fileName)) {
                    // 绑定输出流下载文件,需要设置编码集，不然可能出现文件为空的情况
                    flag = ftp.retrieveFile(fileName, output);
                    // 下载成功删除文件,看项目需求
                    output.flush();
                    if (!flag) {
                        log.error("下载文件[" + fileName + "]失败");
                    }
                }
            }
        } catch (Exception e) {
            log.error("下载文件[" + fileName + "]失败", e);
        }

        return flag;
    }

    /**
     * FTP文件上传工具类
     */
    public static boolean uploadFile(FTPClient ftp, InputStream input, String ftpPath, String fileName) {
        boolean flag = false;
        try {
            // 设置PassiveMode传输
            ftp.enterLocalPassiveMode();
            //设置二进制传输，使用BINARY_FILE_TYPE，ASC容易造成文件损坏
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            //判断FPT目标文件夹时候存在不存在则创建
            if (!ftp.changeWorkingDirectory(ftpPath)) {
                ftp.makeDirectory(ftpPath);
            }
            //跳转目标目录
            ftp.changeWorkingDirectory(ftpPath);

            //上传文件
            flag = ftp.storeFile(fileName, input);
            if (!flag) {
                log.error("上传文件[" + fileName + "]失败");
            }
        } catch (Exception e) {
            log.error("上传文件[" + fileName + "]失败", e);
        }

        return flag;
    }


    /**
     * 删除FTP上指定文件夹下文件及其子文件方法，添加了对中文目录的支持
     *
     * @param ftp       FTPClient对象
     * @param ftpFolder 需要删除的文件夹
     * @return
     */
    public static boolean deleteByFolder(FTPClient ftp, String ftpFolder) {
        boolean flag = false;
        try {
            ftp.changeWorkingDirectory(ftpFolder);
            ftp.enterLocalPassiveMode();
            FTPFile[] files = ftp.listFiles();
            for (FTPFile file : files) {
                //判断为文件则删除
                if (file.isFile()) {
                    ftp.deleteFile(new String(file.getName().getBytes("UTF-8"), "ISO-8859-1"));
                }
                //判断是文件夹
                if (file.isDirectory()) {
                    String childPath = ftpFolder + File.separator + file.getName();
                    //递归删除子文件夹
                    deleteByFolder(ftp, childPath);
                }
            }
            //循环完成后删除文件夹
            flag = ftp.removeDirectory(ftpFolder);
            if (!flag) {
                log.error("删除文件夹[" + ftpFolder + "]失败");
            }
        } catch (Exception e) {
            log.error("删除文件夹[" + ftpFolder + "]失败", e);
        }

        return flag;
    }

    /**
     * 删除文件
     *
     * @param ftp      FTPClient对象
     * @param filePath 需要删除的文件路径
     * @param fileName 需要删除的文件名
     * @return
     */
    public static boolean deleteFile(FTPClient ftp, String filePath, String fileName) {
        boolean flag = false;
        try {
            ftp.changeWorkingDirectory(filePath);
            ftp.enterLocalPassiveMode();
            flag = ftp.deleteFile(fileName);
            if (!flag) {
                log.error("删除文件[" + fileName + "]失败");
            }
        } catch (Exception e) {
            log.error("删除文件[" + fileName + "]失败", e);
        }

        return flag;
    }
    

}
