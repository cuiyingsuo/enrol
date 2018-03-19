package com.itcast.enrol.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author liuzs
 *         Created by liuzhongshuai on 2017/11/30.
 */
public class FileUtils {


    /**
     * 文件目录正则
     */
    private static String matches = "[A-Za-z]:\\\\[^:?\"><*]*";


    private static BusLogUtil busLogUtil = new BusLogUtil(FileUtils.class);


    /**
     * 删除单个文件
     *
     * @param fileName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static void deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (!(file.exists() && file.isFile())) {
            throw new RuntimeException("不能找到要删除的文件!");
        }
        if (!file.delete()) {
            throw new RuntimeException("删除文件失败!");
        }
    }


    /**
     * 在指定文件夹创建文件
     *
     * @param dirPath  文件所在目录
     * @param fileName 文件名称 含扩展名
     * @param bytes    文件字节流
     */
    public static void createFile(String dirPath, String fileName, byte[] bytes) throws IOException {

        if (null == bytes || bytes.length <= 0) {
            throw new RuntimeException("不能创建空文件!!");
        }
        File fileDir = new File(dirPath);
        // 判断目录或文件是否存在 不存在则创建
        if (!fileDir.exists()) {
            boolean flag = fileDir.mkdirs();
            if (!flag) {
                throw new RuntimeException("创建文件夹失败!,名称:" + dirPath);
            }
        }
        //创建文件
        File file = new File(dirPath + fileName);
        if (file.exists()) {
            file.delete();
            file = new File(dirPath + fileName);
        }
        file.createNewFile();
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException("创建文件失败!,文件名称:" + dirPath + fileName);
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
