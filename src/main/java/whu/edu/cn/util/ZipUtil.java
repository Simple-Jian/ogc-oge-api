package whu.edu.cn.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 一个工具类
 * 压缩多个文件(文件夹中的多个文件或文件夹),放入指定目录
 * 2023/9/9
 * By JQL
 */
public class ZipUtil {
    private ZipUtil() {
    }

    public static void myZip(File src, File dest) throws IOException {
        //1.压缩包的路径
        File destZip = new File(dest, src.getName() + ".zip");
        //2.创建压缩流关联文件包
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(destZip));
        //3.获取src中的每一个文件,变成ZipEntry对象,由于是多层的,所以需要递归
        zip(zos, src, src.getName());
        //4.关闭压缩流
        zos.close();
    }

    /**
     * 重载
     * @param src
     * @param dest
     * @throws IOException
     */
    public static void myZip(String src, String dest) throws IOException {
        myZip(new File(src), new File(dest));
    }

    private static void zip(ZipOutputStream zos, File src, String base) throws IOException {

        File[] files = src.listFiles();   //列出文件夹下所有文件
        //文件夹为空时,创建该空文件夹
        if (files.length == 0) {
            zos.putNextEntry(new ZipEntry(base + "/"));    //注意加"/"
        } else {
            for (File file : files) {
                if (file.isDirectory()) {      //如果是文件夹,则递归
                    zip(zos, file, base + "\\" + file.getName());
                } else {//如果是文件,直接写入
                    //将Entry对象放到压缩包中,注意带上其父文件夹
                    zos.putNextEntry(new ZipEntry(base + "\\" + file.getName().toString()));
                    //获取当前文件的输入流
                    FileInputStream fis = new FileInputStream(file);
                    //将文件写入压缩流中
                    byte bytes[] = new byte[1024*1024*5];
                    int len;
                    while ((len = fis.read(bytes)) != -1) {
                        zos.write(bytes, 0, len);
                    }
                    fis.close();
                    zos.closeEntry(); //表示当前文件(一个ZipEntry对象)处理完毕
                }
            }
        }
    }
}

