package com.edward.edwardapplication.util;

import android.util.Log;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by suicheng on 2016/6/3.
 */
public class ZipUtils {
    private static final int BUFFER = 2048;

    public static boolean decompress(String zipFile, String dirLocation) {
        boolean isSuccess = true;
        ZipInputStream zin = null;
        FileOutputStream fout = null;
        try {
            ZipEntry ze;
            zin = new ZipInputStream(new FileInputStream(zipFile));
            byte data[] = new byte[BUFFER];
            while ((ze = zin.getNextEntry()) != null) {
                Log.i("Decompress", "Unzipping:" + ze.getName());
                if (ze.isDirectory()) {
                    dirChecker(dirLocation, ze.getName());
                } else {
                    File file = new File(dirLocation + ze.getName());
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    fout = new FileOutputStream(dirLocation + ze.getName());
                    int count;
                    while ((count = zin.read(data, 0, BUFFER)) != -1) {
                        fout.write(data, 0, count);
                    }
                    zin.closeEntry();
                    closeQuietly(fout);
                }
            }
            zin.close();
        } catch (Exception e) {
            System.out.println(e);
            Log.e("zip Decompress", "fail", e);
            isSuccess = false;
        } finally {
            closeQuietly(zin);
            closeQuietly(fout);
            try {
                zin.closeEntry();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }

    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void dirChecker(String location, String dirName) {
        File f = new File(location + dirName);
        if (!f.isDirectory()) {
            f.mkdirs();
        }
    }


//    public static void main(String[] args) {
//        decompress("C:\\Users\\Administrator\\Desktop\\update.zip", "C:\\Users\\Administrator\\Desktop\\");
//    }
}
