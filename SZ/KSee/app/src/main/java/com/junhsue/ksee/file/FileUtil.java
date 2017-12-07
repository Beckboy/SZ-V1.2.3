package com.junhsue.ksee.file;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.junhsue.ksee.frame.MyApplication;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.UUID;

/**
 * @author Ralken Liao
 * @version create time：Jan 5, 2013 9:32:24 PM
 */

public class FileUtil {

    public static final String PROJECT_NAME = "KSee";
    public static final String ROOTPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + PROJECT_NAME;
    private static final String DB_NAME = "KSee.db";

    public static boolean isDirExist(String dirPath) {
        File dir = new File(dirPath);
        return dir.exists() && dir.isDirectory();
    }

    /**
     * 判断SD卡上的文件是否存在
     *
     * @return
     */
    public static boolean isFileExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    public static void checkRoot() {
        if (!isDirExist(ROOTPATH)) {
            createDir(ROOTPATH);
        }
    }

    public static void initFolders() {
        createDir(ROOTPATH);
    }

    public static String getBaseFilePath() {
        return ROOTPATH;
    }

    public static String getDBFilePath(String userId) {
        checkRoot();
        return getUserRootFolder(userId) + File.separator + DB_NAME;
    }

    public static String getImageFolder() {
        createDir(ROOTPATH + File.separator + "image");
        return ROOTPATH + File.separator + "image";
    }


    public static String getTmpFolder() {
        createDir(ROOTPATH + File.separator + "tmp");
        return ROOTPATH + File.separator + "tmp";
    }

    public static String createTmpFile(String name) {
        return getTmpFolder() + File.separator + name;
    }

    public static String getSerializableImageFolder() {
        createDir(ROOTPATH + File.separator + "serializable");
        return ROOTPATH + File.separator + "serializable";
    }

    public static String getUserRootFolder(String userId) {
        String absDir = ROOTPATH + File.separator + "profile" + File.separator + userId;
        createDir(absDir);
        return absDir;
    }

    public static String getCompressedImageCacheFolder() {
        createDir(ROOTPATH + File.separator + "compressed");
        return ROOTPATH + File.separator + "compressed";
    }

    /**
     * 在SD卡上创建文件
     *
     * @return
     * @throws IOException
     */
    public static File createSDFile(String path) throws IOException {
        File file = new File(path);
        if (!file.exists())
            file.createNewFile();

        return file;
    }

    public static void createDir(String... dirPath) {
        File dir = null;
        for (int i = 0; i < dirPath.length; i++) {
            dir = new File(dirPath[i]);
            if (!dir.exists() && !dir.isDirectory()) {
                dir.mkdirs();
            }
        }
    }

    /**
     * 在SD卡上创建目录
     *
     * @param dirName
     * @return
     */
    public static File createSDDir(String dirName) {
        File file = new File(dirName);
        if (!file.exists())
            file.mkdir();
        return file;
    }

    /**
     * 创建文件目录
     *
     * @param filePath
     */
    public static void createFileDir(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            parentFile = null;
        }
        file = null;
    }

    /**
     * 删除文件（不是目录）
     * <p>
     * add by wjz 040318
     * <p>
     *
     * @param fileName Description of the Parameter
     * @return Description of the Return Value
     */
    public static boolean deleteFile(String fileName) {
        try {
            if (fileName == null) {
                return false;
            }
            File f = new File(fileName);

            if (f == null || !f.exists()) {
                return false;
            }

            if (f.isDirectory()) {
                return false;
            }
            return f.delete();
        } catch (Exception e) {
            // Log.d(FILE_TAG, e.getMessage());
            return false;
        }
    }

    /**
     * 删除文件夹下的文件(不含当前文件夹)
     *
     * @param dirName   源文件或目录
     * @param isRecurse 如果是目录,是否删除其下的子目录
     * @return true-成功,false-失败
     */
    public static boolean deleteFileOfDir(String dirName, boolean isRecurse) {
        boolean blret = false;
        try {
            File f = new File(dirName);
            if (f == null || !f.exists()) {
                // Log.d(FILE_TAG, "file" + dirName + "not isExist");
                return false;
            }

            if (f.isFile()) {
                blret = f.delete();
                return blret;
            } else {
                File[] flst = f.listFiles();
                if (flst == null || flst.length <= 0) {
                    return true;
                }

                int filenumber = flst.length;
                File[] fchilda = f.listFiles();
                for (int i = 0; i < filenumber; i++) {
                    File fchild = fchilda[i];
                    if (fchild.isFile()) {
                        blret = fchild.delete();
                        if (!blret) {
                            break;
                        }
                    } else if (isRecurse) {
                        blret = deleteFileDir(fchild.getAbsolutePath(), true);
                    }
                }
            }
        } catch (Exception e) {
            // Log.d(FILE_TAG, e.getMessage());
            blret = false;
        }

        return blret;
    }

    /**
     * 删除文件文件夹(含当前文件夹)
     *
     * @param dirName   源文件或目录
     * @param isRecurse 如果是目录,是否删除其下的子目录
     * @return true-成功,false-失败
     */
    public static boolean deleteFileDir(String dirName, boolean isRecurse) {
        boolean blret = false;
        try {
            File f = new File(dirName);
            if (f == null || !f.exists()) {
                // Log.d(FILE_TAG, "file" + dirName + "not isExist");
                return false;
            }
            if (f.isFile()) {
                blret = f.delete();
                return blret;
            } else {
                File[] flst = f.listFiles();
                if (flst == null || flst.length <= 0) {
                    f.delete();
                    return true;
                }
                int filenumber = flst.length;
                File[] fchilda = f.listFiles();
                for (int i = 0; i < filenumber; i++) {
                    File fchild = fchilda[i];
                    if (fchild.isFile()) {
                        blret = fchild.delete();
                        if (!blret) {
                            break;
                        }
                    } else if (isRecurse) {
                        blret = deleteFileDir(fchild.getAbsolutePath(), true);
                    }
                }

                // 删除当前文件夹
                blret = new File(dirName).delete();
            }
        } catch (Exception e) {
            // Log.d(FILE_TAG, e.getMessage());
            blret = false;
        }

        return blret;
    }

    /**
     * 移动文件
     *
     * @param filePath
     */
    public static void removeToDir(String filePath, String toFilePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }
        file.renameTo(new File(toFilePath));
    }

    /**
     * 本方法主要用于将一个文件拷贝到另一个文件件
     *
     * @param from 源文件
     * @param to   目的文件
     * @return 返回字节数,－2：文件未找到，-3： IO错误
     * @throws FileNotFoundException Description of the Exception
     * @throws FileNotFoundException ,抛出文件没有发现异常
     * @throws IOException           抛出IO异常
     */
    public final static int copyFile(String from, String to) throws FileNotFoundException, IOException {
        FileInputStream is = null;
        FileOutputStream os = null;

        int n = 0;
        try {
            is = new FileInputStream(from);

            byte[] buffer = new byte[1024];

            // 删除原文件
            deleteFile(to);

            os = new FileOutputStream(to);
            int i = 0;
            while ((i = is.read(buffer)) != -1) {
                os.write(buffer, 0, i);
            }
            os.flush();

            buffer = null;
            n = 0;
        } catch (FileNotFoundException fe) {
            n = -2;
        } catch (IOException ie) {
            n = -3;
        } finally {
            try {
                is.close();
            } catch (Exception ex) {
            }
            try {
                os.close();
            } catch (Exception ex) {
            }
        }
        return n;
    }

    /**
     * 根据文件路径取得该文件路径下的所有文件名称：
     *
     * @param filePath
     * @return 存放文件名称的数组
     */
    public static String[] getFileNames(String filePath) {
        // modify by wjz 0225,考虑到.-当前目录
        if (filePath == null || filePath.length() <= 0) {
            return null;
        }
        File f = new File(filePath);
        if (f.isFile()) {
            return null;
        }
        String[] flName = f.list();
        f = null;
        return flName;
    }

    /**
     * 读取指定路径的文件
     *
     * @param filePath
     * @return
     */
    public static File readFile(String filePath) {
        File file = new File(filePath);
        return file;
    }

    /**
     * 将一个InputStream里面的数据写入到SD卡中
     *
     * @param inputStream
     * @return
     */
    public static File writeFile2SDCard(File file, InputStream inputStream) {
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = inputStream.read(temp)) > 0) {
                outputStream.write(temp, 0, i);
            }
            // byte buffer[] = new byte[1024];
            // while ((inputStream.read(buffer) != -1)) {
            // outputStream.write(buffer);
            // }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return file;
    }

    /**
     * 新建目录
     *
     * @param folderPath 目录
     * @return 返回目录创建后的路径
     */
    public static String createFolder(String folderPath) {
        // 构建文件夹路径
        String path = getStoragePath() + folderPath;
        try {
            File myFilePath = new File(path);
            if (!myFilePath.exists()) {
                myFilePath.mkdir();
            }
        } catch (Exception e) {
            // Log.d(FILE_TAG, "---createFolder Error");
        }
        return path;
    }

    /**
     * 检查SDCard是否存在
     *
     * @return
     */
    public static boolean isExistsStorage() {
        // 是否存在外存储器(优先判断)
        // Environment.MEDIA_MOUNTED // sd卡在手机上正常使用状态
        // Environment.MEDIA_UNMOUNTED // 用户手工到手机设置中卸载sd卡之后的状态
        // Environment.MEDIA_REMOVED // 用户手动卸载，然后将sd卡从手机取出之后的状态
        // Environment.MEDIA_BAD_REMOVAL // 用户未到手机设置中手动卸载sd卡，直接拨出之后的状态
        // Environment.MEDIA_SHARED // 手机直接连接到电脑作为u盘使用之后的状态
        // Environment.MEDIA_CHECKINGS // 手机正在扫描sd卡过程中的状态

        boolean res = false;
        String exStorageState = Environment.getExternalStorageState();
        if (exStorageState != null && Environment.MEDIA_SHARED.equals(exStorageState)) {
            // 有SD卡，手机直接连接到电脑作为u盘使用之后的状态
            return false;
        }

        if (exStorageState != null && Environment.MEDIA_MOUNTED.equals(exStorageState)) {
            // sd卡在手机上正常使用状态
            res = true;
        } else {
            // 如果外存储器不存在，则判断内存储器
            res = isExitInternalStorage();
        }

        return res;
    }

    // 是否存在内存储器
    private static boolean isExitInternalStorage() {
        File file = MyApplication.getApplication().getFilesDir();

        if (file != null) {
            return true;
        }

        return false;
    }

    /**
     * 得到SDCard的路径
     *
     * @return
     */
    public static String getStoragePath() {
        /* 检测是否存在SD卡 */
        File filePath = null;
        String exStorageState = Environment.getExternalStorageState();
        if (exStorageState != null && Environment.MEDIA_SHARED.equals(exStorageState)) {
            // 有SD卡，手机直接连接到电脑作为u盘使用之后的状态
            return null;
        }

        if (exStorageState != null && Environment.MEDIA_MOUNTED.equals(exStorageState)) {
            // sd卡在手机上正常使用状态
            // 是否存在外存储器(优先判断)
            filePath = Environment.getExternalStorageDirectory();
        } else if (isExitInternalStorage()) {
            // 如果外存储器不存在，则判断内存储器
            filePath = MyApplication.getApplication().getFilesDir();
        }

        if (filePath != null) {
            // System.out.println("*******************system path:"
            // + filePath.getParent() + "/");
            return filePath.getPath() + "/";
        } else {
            return null;
        }
    }

    /**
     * 是否还有可使用的储蓄空间，低于10M则认为没有满足程序使用的储蓄空间
     *
     * @return
     */
    public static boolean isAvailableMermory() {
        boolean res = false;
        String exStorageState = Environment.getExternalStorageState();
        if (exStorageState != null && Environment.MEDIA_SHARED.equals(exStorageState)) {
            // 有SD卡，手机直接连接到电脑作为u盘使用之后的状态
            return false;
        }

        if (exStorageState != null && Environment.MEDIA_MOUNTED.equals(exStorageState)) {
            // sd卡在手机上正常使用状态
            // 是否存在外存储器(优先判断)
            File file = Environment.getExternalStorageDirectory();
            res = isAvailableMermoryOfFile(file);
        } else if (isExitInternalStorage()) {
            // 如果外存储器不存在，则判断内存储器
            File file = MyApplication.getApplication().getFilesDir();
            res = isAvailableMermoryOfFile(file);
        }

        return res;
    }

    // 指定的文件是否有可用的空间
    private static boolean isAvailableMermoryOfFile(File file) {
        if (file == null || file.getPath() == null) {
            return false;
        }

        StatFs statfs = new StatFs(file.getPath());
        long blockSize = statfs.getBlockSize();
        long availableBlocks = statfs.getAvailableBlocks();
        int availableMermory = (int) ((availableBlocks * blockSize) / 1024 / 1024);
        file = null;

        return availableMermory >= 10;
    }

    /**
     * 查看空间不否可用
     *
     * @return
     */
    public static boolean checkAvailableMermory() {
        if (!isAvailableMermory()) {
            Toast.makeText(MyApplication.getApplication(), "存储空间不足,请删除部分文件,然后再试", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 读取文本文件内容
     * <p>
     * 文件输入流
     * 文本文件打开的编码方式
     *
     * @return 返回文本文件的内容
     */
    public static byte[] readFileOfByte(String path) throws IOException {
        if (path == null || path.trim().length() <= 0) {
            return null;
        }

        byte[] res = null;
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(path);
            res = new byte[fs.available()];
            fs.read(res, 0, res.length);
        } catch (IOException es) {
            res = null;
        } finally {
            if (fs != null) {
                fs.close();
                fs = null;
            }
        }
        return res;
    }

    /**
     * 读取assets的文件
     *
     * @return
     */
    public static String readAssetsFile(String fileName) {
        String res = null;

        InputStream inputStream = null;
        try {
            inputStream = MyApplication.getApplication().getAssets().open(fileName);
            int len;
            len = inputStream.available();
            byte[] arrayOfByte = new byte[len];
            inputStream.read(arrayOfByte);
            res = new String(arrayOfByte, "UTF-8");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                inputStream = null;
            }
        }

        return res;
    }

    /**
     * 读取文本文件内容
     * <p>
     * 文件输入流
     *
     * @param encoding 文本文件打开的编码方式
     * @return 返回文本文件的内容
     */
    public static String readFile(String path, String encoding) {
        encoding = encoding.trim();
        File file = new File(path);
        StringBuffer str = new StringBuffer("");
        String st = "";
        String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data"; // 内容类型
        String encode = "UTF-8";
        str.append(PREFIX);
        str.append(BOUNDARY);
        str.append(LINE_END);
        /**
         * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
         * filename是文件的名字，包含后缀名的 比如:abc.png
         */

        str.append("Content-Disposition: form-data; name=\"upload_file\"; filename=\"" + file.getName() + "\"" + LINE_END);
        str.append("Content-Type: multipart/form-data; charset=" + encode + LINE_END);
        str.append(LINE_END);

        try {
            FileInputStream fs = new FileInputStream(path);
            InputStreamReader isr = null;
            if (encoding.equals("") || encoding == null) {
                isr = new InputStreamReader(fs);
            } else {
                isr = new InputStreamReader(fs, encoding);
            }
            BufferedReader br = new BufferedReader(isr);
            try {
                String data = "";
                while ((data = br.readLine()) != null) {
                    str.append(data + "\r\n");
                }
            } catch (Exception e) {
                str.append(e.toString());
            }
            st = str.toString();
            str = null;
        } catch (IOException es) {
            st = "";
        }
        return st;
    }

    /**
     * 读取文本文件内容
     *
     * @param fs       文件输入流
     * @param encoding 文本文件打开的编码方式
     * @return 返回文本文件的内容
     */
    public static String readFile(InputStream fs, String encoding) throws IOException {
        encoding = encoding.trim();
        StringBuffer str = new StringBuffer("");
        String st = "";
        try {
            // FileInputStream fs = new FileInputStream(filePathAndName);
            InputStreamReader isr = null;
            if (encoding.equals("") || encoding == null) {
                isr = new InputStreamReader(fs);
            } else {
                isr = new InputStreamReader(fs, encoding);
            }
            BufferedReader br = new BufferedReader(isr);
            try {
                String data = "";
                while ((data = br.readLine()) != null) {
                    str.append(data + "\r\n");
                }
            } catch (Exception e) {
                str.append(e.toString());
            }
            st = str.toString();
            str = null;
        } catch (IOException es) {
            st = "";
        }
        return st;
    }

    /**
     * 写入文件
     *
     * @param path 文件名
     *             内容
     */
    public static void writeFileOfDelExists(String path, byte[] in) {
        if (in == null || in.length <= 0) {
            return;
        }

        FileOutputStream ios = null;
        File f = null;
        try {
            f = new File(path);
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();

            ios = new FileOutputStream(f);
            ios.write(in, 0, in.length);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ios != null) {
                try {
                    ios.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                ios = null;
            }
            if (f != null) {
                f = null;
            }
        }
    }

    /**
     * 写入文件
     *
     * @param path 文件名
     *             内容
     * @throws Exception
     */
    public static void writeFileOfNODelExists(String path, byte[] in) throws Exception {
        if (in == null || in.length <= 0) {
            return;
        }

        // 创建文件目录
        createFileDir(path);

        FileOutputStream ios = null;
        File f = null;
        try {
            f = new File(path);
            if (f.exists()) {
                return;
            }
            f.createNewFile();

            ios = new FileOutputStream(f);
            ios.write(in, 0, in.length);
            ios.flush();
        } catch (Exception e) {
            e.printStackTrace();
            // 若写文件失败，则删除脏文件
            deleteFile(path);
            throw e;
        } finally {
            if (ios != null) {
                try {
                    ios.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                ios = null;
            }
            if (f != null) {
                f = null;
            }
        }
    }

    /**
     * 写入文件
     *
     * @param path    文件名
     * @param content 内容
     */
    public static void writeFile(String path, String content) {
        createFileDir(path);

        String s = new String();
        String s1 = new String();
        // 构建文件路径
        // String filePath = getSDCardPath() + path;
        try {
            File f = new File(path);
            if (f.exists()) {
            } else {
                f.createNewFile();
            }

            FileInputStream ios = new FileInputStream(f);
            InputStreamReader isr = new InputStreamReader(ios, "UTF-8");
            BufferedReader input = new BufferedReader(isr);
            while ((s = input.readLine()) != null) {
                s1 += s + "\r\n";
            }
            input.close();
            s1 += content;

            FileOutputStream fos = new FileOutputStream(f);
            Writer out = new OutputStreamWriter(fos, "UTF-8");
            out.write(s1);
            out.close();
            fos.close();
            // BufferedWriter output = new BufferedWriter(new FileWriter(f));
            // s1 = URLEncoder.encode(s1, "utf-8");
            // output.write(s1, "UTF-8");
            // output.close();
        } catch (Exception e) {
            // Log.d(FILE_TAG, "---writeFile Error = " + e.getMessage());
        }
    }

    /**
     * 写入文件
     *
     * @param path    文件名
     * @param content 内容
     */
    public static void writeFileOfDelExists(String path, String content) {
        createFileDir(path);

        String s = new String();
        String s1 = new String();
        // 构建文件路径
        // String filePath = getSDCardPath() + path;
        try {
            File f = new File(path);
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();

            FileInputStream ios = new FileInputStream(f);
            InputStreamReader isr = new InputStreamReader(ios, "UTF-8");
            BufferedReader input = new BufferedReader(isr);
            while ((s = input.readLine()) != null) {
                s1 += s + "\r\n";
            }
            input.close();
            s1 += content;

            FileOutputStream fos = new FileOutputStream(f);
            Writer out = new OutputStreamWriter(fos, "UTF-8");
            out.write(s1);
            out.close();
            fos.close();
            // BufferedWriter output = new BufferedWriter(new FileWriter(f));
            // s1 = URLEncoder.encode(s1, "utf-8");
            // output.write(s1, "UTF-8");
            // output.close();
        } catch (Exception e) {
            // Log.d(FILE_TAG, "---writeFile Error = " + e.getMessage());
        }
    }

    /**
     * Writes content to internal storage making the content private to the
     * application. The method can be easily changed to take the MODE as
     * argument and let the caller dictate the visibility: MODE_PRIVATE,
     * MODE_WORLD_WRITEABLE, MODE_WORLD_READABLE, etc.
     *
     * @param filename - the name of the file to create
     * @param content  - the content to write
     */
    public static void writeInternalStoragePrivate(String filename, byte[] content) {
        FileOutputStream fos = null;
        try {
            // MODE_PRIVATE creates/replaces a file and makes
            // it private to your application. Other modes:
            // MODE_WORLD_WRITEABLE
            // MODE_WORLD_READABLE
            // MODE_APPEND
            fos = MyApplication.getApplication().openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(content);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                fos = null;
            }
        }
    }

    /**
     * Reads a file from internal storage
     *
     * @param filename the file to read from
     * @return the file content
     */
    public static byte[] readInternalStoragePrivate(String filename) {
        int len = 1024;
        FileInputStream fis = null;
        byte[] buffer = new byte[len];
        try {
            fis = MyApplication.getApplication().openFileInput(filename);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int nrb = fis.read(buffer, 0, len); // read up to len bytes
            while (nrb != -1) {
                baos.write(buffer, 0, nrb);
                nrb = fis.read(buffer, 0, len);
            }
            buffer = baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                fis = null;
            }
        }
        return buffer;
    }

    // 删除应用程序目录下的文件
    public static void delInternalStoragePrivate(String fileName) {
        deleteFile(fileName);
    }

    /**
     * 重命名文件
     *
     * @param sourcePath
     * @param objPath
     */
    public static void renameFile(String sourcePath, String objPath) {
        File sourceFile = new File(sourcePath);
        File objFile = new File(objPath);

        if (sourceFile != null && !sourceFile.exists()) {// 源文件不存在，则返回
            return;
        }

        if (objFile != null && objFile.exists()) {// 目标文件存在，则删除
            objFile.delete();
        }

        sourceFile.renameTo(objFile);

        sourceFile = null;
        objFile = null;
    }

    public static String getFilePathByUri(Activity activity, Uri uri) {
        String path = null;
        Cursor cursor = activity.managedQuery(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
        if (cursor.moveToFirst()) {
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        }
        // cursor will be automatically closed above 4.0
        // cursor.close();
        cursor = null;
        return path;
    }


    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小", "获取失败!");
        }
        return FormatFileSize(blockSize);
    }

    public static String getAutoFileOrFilesSizeFormatMB(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小", "获取失败!");
        }
        return FormatFileSizeToMB(blockSize);
    }


    /**
     * 获取指定文件大小
     *
     * @param
     * @return
     * @throws Exception
     */
    private static long getFileSize(File file) throws Exception {
        long size = 0;
        FileInputStream fis = null;
        if (file.exists()) {
            try {
                fis = new FileInputStream(file);
                size = fis.available();

            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(null!=fis){
                    fis.close();
                }
            }
        } else {
            file.createNewFile();
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }


    /**
     * 获取指定文件夹
     *
     * @param f
     * @return
     * @throws Exception
     */
    private static long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }


    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */

    private static String FormatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * @param fileS
     * @return
     */
    private static String FormatFileSizeToMB(long fileS) {
        DecimalFormat df = new DecimalFormat("0.00");
        String fileSizeString = "";
        String wrongSize = "0.00M";
        if (fileS == 0) {
            return wrongSize;
        }
        fileSizeString = df.format((double) fileS / 1048576) + "M";
        return fileSizeString;
    }


    /**
     * 判断文件夹是否清空
     * @param filePath
     * @return
     */
    public static  boolean isFileClear(String filePath){
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小", "获取失败!");
        }
        return blockSize==0?true:false;
    }

    /**
     * 清空指定路径的所有缓存
     *
     * @param context
     * @param filePath
     */
    public static boolean clearAllCacheInDir(Context context, String filePath) {
        boolean isClearAll = false;
        File file = new File(filePath);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            isClearAll = deleteDir(file);
        }
        return isClearAll;
    }

    /**
     * 清空指定路径的所有缓存
     *
     * @param dir
     * @return
     */
    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

}
