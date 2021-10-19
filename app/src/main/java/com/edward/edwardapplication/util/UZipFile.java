package com.edward.edwardapplication.util;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class UZipFile {
    /**
     * 解压到指定目录
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void unZipFiles(String zipPath, String descDir) throws IOException {
        unZipFiles(new File(zipPath), descDir);
    }

    /**
     * 解压文件到指定目录
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressWarnings("rawtypes")
    public static void unZipFiles(File zipFile, String descDir) throws IOException {
        File pathFile = new File(descDir);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        //解决zip文件中有中文目录或者中文文件
        ZipFile zip = new ZipFile(zipFile, Charset.forName("GBK"));
        for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            String zipEntryName = entry.getName();
            InputStream in = zip.getInputStream(entry);
            String outPath = (descDir + zipEntryName).replaceAll("\\*", "/");
            ;
            //判断路径是否存在,不存在则创建文件路径
            File file = new File(outPath.substring(0, outPath.lastIndexOf('\\')));
            if (!file.exists()) {
                file.mkdirs();
            }
            //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
            if (new File(outPath).isDirectory()) {
                continue;
            }
            //输出文件路径信息
            System.out.println(outPath);
            OutputStream out = new FileOutputStream(outPath);
            byte[] buf1 = new byte[1024];
            int len;
            while ((len = in.read(buf1)) > 0) {
                out.write(buf1, 0, len);
            }
            in.close();
            out.close();
        }
        System.out.println("******************解压完毕********************");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void main(String[] args) throws IOException {
        /**
         * 解压文件
         */
        File zipFile = new File("C:/Users/Administrator/Desktop/update.zip");
        String path = "C:\\Users\\Administrator\\Desktop\\zipfile\\";
        unZipFiles(zipFile, path);
    }


    public static void unZipFile(String archive, String decompressDir, boolean isDeleteZip) throws IOException {
        BufferedInputStream bi;
        ZipFile zf = new ZipFile(archive);
        Enumeration e = zf.entries();
        while (e.hasMoreElements()) {
            ZipEntry ze2 = (ZipEntry) e.nextElement();
            String entryName = ze2.getName();
            String path = decompressDir + "/" + entryName;
            if (ze2.isDirectory()) {
                File decompressDirFile = new File(path);
                if (!decompressDirFile.exists()) {
                    decompressDirFile.mkdirs();
                }
            } else {
                String fileDir = path.substring(0, path.lastIndexOf("/"));
                File fileDirFile = new File(decompressDir);
                if (!fileDirFile.exists()) {
                    fileDirFile.mkdirs();
                }
                String substring = entryName.substring(entryName.lastIndexOf("/") + 1, entryName.length());
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(decompressDir + "/" + substring));
                bi = new BufferedInputStream(zf.getInputStream(ze2));
                byte[] readContent = new byte[1024];
                int readCount = bi.read(readContent);
                while (readCount != -1) {
                    bos.write(readContent, 0, readCount);
                    readCount = bi.read(readContent);
                }
                bos.close();
            }
        }
        zf.close();
        if (isDeleteZip) {
            File zipFile = new File(archive);
            if (zipFile.exists() && zipFile.getName().endsWith(".zip")) {
                zipFile.delete();
            }
        }
    }
}