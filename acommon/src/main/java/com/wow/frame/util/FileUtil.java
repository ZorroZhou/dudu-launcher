package com.wow.carlauncher.common.util;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileUtil {
    public static byte[] file2bytes(File file) {
        byte[] ret = null;
        try {
            if (file == null) {
                return null;
            }
            FileInputStream in = new FileInputStream(file);
            ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
            byte[] b = new byte[4096];
            int n;
            while ((n = in.read(b)) != -1) {
                out.write(b, 0, n);
            }
            in.close();
            out.close();
            ret = out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static String file2string(File file) {
        try {
            byte[] ret = file2bytes(file);
            if (ret != null) {
                return Base64.encodeToString(ret, Base64.DEFAULT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除目录（文件夹）以及目录下的文�?
     *
     * @param sPath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String sPath) {
        // 如果sPath不以文件分隔符结尾，自动添加文件分隔�?
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        // 如果dir对应的文件不存在，或者不是一个目录，则�??�?
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        // 删除文件夹下的所有文�?(包括子目�?)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文�?
            if (files[i].isFile()) {
                deleteFile(files[i].getAbsolutePath());
            } // 删除子目�?
            else {
                deleteDirectory(files[i].getAbsolutePath());
            }
        }
        // 删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除目录下的文件
     *
     * @param sPath 被删除目录的文件路径
     * @return 文件删除成功返回true，否则返回false
     */
    public static boolean deleteFiles(String sPath) {
        // 如果sPath不以文件分隔符结尾，自动添加文件分隔�?
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        // 如果dir对应的文件不存在，或者不是一个目录，则�??�?
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        // 删除文件夹下的所有文�?(包括子目�?)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文�?
            if (files[i].isFile()) {
                deleteFile(files[i].getAbsolutePath());
            } // 删除子目�?
            else {
                deleteDirectory(files[i].getAbsolutePath());
            }
        }
        // 判断目录下的文件是否清除
        if (dirFile.listFiles().length > 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 根据路径删除指定的目录或文件，无论存在与�?
     *
     * @param sPath 要删除的目录或文�?
     * @return 删除成功返回 true，否则返�? false�?
     */
    public static boolean deleteFolder(String sPath) {
        File file = new File(sPath);
        // 判断目录或文件是否存�?
        if (!file.exists()) { // 不存在返�? false
            return false;
        } else {
            // 判断是否为文�?
            if (file.isFile()) { // 为文件时调用删除文件方法
                return deleteFile(sPath);
            } else { // 为目录时调用删除目录方法
                return deleteDirectory(sPath);
            }
        }
    }

    /**
     * 删除单个文件
     *
     * @param sPath 被删除文件的文件�?
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String sPath) {
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
        }
        return true;
    }

    /**
     * 创建文件�?
     *
     * @param file
     */
    public static void mkdirs(File file) {
        if (!file.exists()) {
            if (file.mkdirs()) {
                MLogUtil.e(file.getAbsoluteFile() + "文件夹创建成功?");
            } else {
                MLogUtil.e(file.getAbsoluteFile() + "文件夹创建失败?");
            }
        } else {
            MLogUtil.e(file.getAbsoluteFile() + "文件夹已存在");
        }
    }

    /**
     * 判断路径是否存在，不存在则创建
     *
     * @param file
     */
    public static void pathIsExists(File file) {
        if (!file.exists()) {
            if (file.mkdirs()) {
                MLogUtil.e(file.getAbsoluteFile() + "文件夹创建成功");
            } else {
                MLogUtil.e(file.getAbsoluteFile() + "文件夹创建失败");
            }
        }
    }

    /**
     * 下载文件
     *
     * @param urlPath
     * @param file
     * @return
     */
    public static File downloadFile(String urlPath, File file) {
        if (file.exists()) {
            return file;
        } else {
            String path = file.getPath();
            mkdirs(new File(path.substring(0, path.lastIndexOf("/"))));
        }
        try {
            String str = java.net.URLEncoder.encode(urlPath, "UTF-8");
            str = str.replaceAll("%2F", "/");// �?要把网址的特殊字符转过来
            str = str.replaceAll("%3A", ":");
            URL url = new URL(str);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.getResponseCode();
            if (conn.getResponseCode() == 200) {
                InputStream inSream = conn.getInputStream();
                file.createNewFile();
                FileOutputStream outStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int len = -1;
                while ((len = inSream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }
                outStream.close();
                inSream.close();
                return file;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取文本文件
     *
     * @param strFilePath,要读取的文件
     * @return 文件内容
     */

    public static String ReadTxtFile(String strFilePath) {
        MLogUtil.e("打开txt文件:" + strFilePath);
        String path = strFilePath;
        String content = ""; // 文件内容字符�?
        // 打开文件
        File file = new File(path);
        // 如果path是传递过来的参数，可以做�?个非目录的判�?
        System.out.println(file.exists());
        if (file.exists()) {
            try {
                InputStream instream = new FileInputStream(file);
                if (instream != null) {
                    InputStreamReader inputreader = new InputStreamReader(instream, "UTF-8");
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    // 分行读取
                    while ((line = buffreader.readLine()) != null) {
                        content += line + "\n";
                    }
                    instream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    public static File byte2file(byte[] bfile, String filePath) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }

    // 保存文件
    public static void saveFile(String newsRootPath, String filename, File picFile) {
        try {
            File newsFileRoot = new File(newsRootPath);
            if (!newsFileRoot.exists()) {
                newsFileRoot.mkdirs();
            }

            FileOutputStream fos = new FileOutputStream(newsRootPath + filename);
            FileInputStream fis = new FileInputStream(picFile);
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = fis.read(buf)) > 0) {
                fos.write(buf, 0, len);
            }
            if (fis != null)
                fis.close();
            if (fos != null)
                fos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
