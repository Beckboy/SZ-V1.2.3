package com.junhsue.ksee.utils;


import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.entity.ResultEntity;
import com.junhsue.ksee.entity.UserDefinedStatisticModel;
import com.junhsue.ksee.file.FileUtil;
import com.junhsue.ksee.interfaces.IDefinedStatisticsManager;
import com.junhsue.ksee.net.api.OKHttpStatistics;
import com.junhsue.ksee.net.callback.RequestCallback;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义统计
 * Created by Sugar on 17/8/9.
 */

public class DefinedStatisticsManager implements IDefinedStatisticsManager {

    //以后考虑一下debug模式

    private static DefinedStatisticsManager instance = null;

    public static final String HEARD_DIVIDER = "+";//头部和内容分隔符
    public static final String MIDDLE_DIVIDER = ",";//内容的中间分隔符
    public static final String END_DIVIDER = ";";//内容的尾部分隔符


    private Object lock = new Object();
    public static final long MAX_TXT_LENGTH = 1024000L;//暂时设置该值

    public static final long spanTime = 300000;//相隔时间差

    public static final String APP_VERSION_NAME = "app_version_name";//app当前版本号
    public static final String UPDATE_DESIGN = "update_design";//文件的更新标识
    public static final String PLATFORM_ID = "2";//客服端平台标识,ios 为1,android 为2
    public static final String OS_DEVICE_VERSION_INFO = "os_device_version_info";//设备的Android系统版本


    public static final String page_statistics_save_path = "page_statistics";
    public static final String dot_statistics_save_path = "dot_statistics";


    private DefinedStatisticsManager() {

    }

    public static DefinedStatisticsManager getInstance() {

        if (instance == null) {
            synchronized (DefinedStatisticsManager.class) {
                if (instance == null) {
                    instance = new DefinedStatisticsManager();
                }
            }
        }
        return instance;

    }


    @Override
    public void countActionNum(UserDefinedStatisticModel statisticModel) {
        String strPath = FileUtil.ROOTPATH + dot_statistics_save_path + ".txt";

//        CountExecutor.execute(new WriteTxtFile(statisticModel, strPath));
        writeTxtToFile(getContent(statisticModel), strPath, statisticModel.type);
    }

    @Override
    public void pageViewWithName(UserDefinedStatisticModel statisticModel) {
        String strPath = FileUtil.ROOTPATH + page_statistics_save_path + ".txt";
//        CountExecutor.execute(new WriteTxtFile(statisticModel, strPath));
        writeTxtToFile(getContent(statisticModel), strPath, statisticModel.type);
    }


    /**
     * 上传统计文件成功后，删除该文件
     *
     * @param file
     */
    private void delete(File file) {
        synchronized (lock) {
            if (file.exists()) {
                file.delete();
            }
        }

    }

    /**
     * 上传统计文件成功后，删除该文件
     */
    private void delete(String pagePath, String dotPath) {
//        synchronized (lock) {
        File pageFile = new File(pagePath);
        File dotFile = new File(dotPath);
        if (pageFile.exists()) {//删除页面路径文件
            pageFile.delete();
        }
        if (dotFile.exists()) {//删除埋点文件
            dotFile.delete();
        }
//        }

    }

    /**
     * 满足文件大小后，上传统计文件,设计目的是在后台切换到前台
     */
    public void uploadStatisticsFile() {
//        Trace.d("uploadTxt length = " + length);
//
//        if (length > MAX_TXT_LENGTH) {
////            uploadCountTxtFile(file);
//        }

        uploadTxt();

//        synchronized (lock) {
//
//            uploadTxt();
//
//        }

    }


    /**
     * 上传
     */
    private void uploadTxt() {

        String pageFilePath = FileUtil.ROOTPATH + page_statistics_save_path + ".txt";
        String pageContent = "";

        String dotFilePath = FileUtil.ROOTPATH + dot_statistics_save_path + ".txt";
        String dotContent = "";

        long currentTime = System.currentTimeMillis();
        long lastTime = SharedPreferencesUtils.getInstance().getLong(Constants.STATISTICS_LAST_TIME, 0);

        File tempPathFile = new File(pageFilePath);
        File tempDotFile = new File(dotFilePath);
        Trace.d("===currentTime - lastTime:" + (currentTime - lastTime) + "==spanTime:" + spanTime + "tempPathFile.length() < MAX_TXT_LENGTH:" + (tempPathFile.length() < MAX_TXT_LENGTH) + "tempDotFile.length() < MAX_TXT_LENGTH:" + (tempDotFile.length() < MAX_TXT_LENGTH));
        if ((currentTime - lastTime) < spanTime && (tempPathFile.length() < MAX_TXT_LENGTH && tempDotFile.length() < MAX_TXT_LENGTH)) {
            return;
        }

        SharedPreferencesUtils.getInstance().putLong(Constants.STATISTICS_LAST_TIME, currentTime);

        try {
            dotContent = readFileContent(dotFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            pageContent = readFileContent(pageFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (StringUtils.isBlank(dotContent) && StringUtils.isBlank(pageContent)) {
            return;
        }

        //去尾部的分号,后台处理不了
        OKHttpStatistics.getInstance().uploadStatisticsFile(pageContent, dotContent, new RequestCallback<ResultEntity>() {

            @Override
            public void onError(int errorCode, String errorMsg) {

                Trace.d("===" + errorCode + "==" + errorMsg);

            }

            @Override
            public void onSuccess(ResultEntity response) {
                String dotPath = FileUtil.ROOTPATH + dot_statistics_save_path + ".txt";
                String pagePath = FileUtil.ROOTPATH + page_statistics_save_path + ".txt";
                delete(pagePath, dotPath);
            }
        });

    }


    /**
     * 上传统计文件到服务器
     */
    public void uploadCountTxtFile(final File pageFile, final File dotFile) {
        if (!pageFile.exists() || dotFile.length() <= 0) {
            return;
        }
    }


    private void writeTxtToFile(String strContent, String strFilePath, int type) {

        synchronized (lock) {
            try {
//                strFilePath = FileUtil.ROOTPATH + page_statistics_save_path + ".txt";
                String content = "";
                if (!FileUtil.isFileExist(strFilePath)) {

                    if (type == 1) {
                        strFilePath = FileUtil.ROOTPATH + page_statistics_save_path + ".txt";
                    }

                    if (type == 2) {
                        strFilePath = FileUtil.ROOTPATH + dot_statistics_save_path + ".txt";
                    }

                    content = getHeardContent(type) + strContent;
                }

                content = content + strContent;
                File loadTxtFile = new File(strFilePath);

                if (!loadTxtFile.exists()) {
                    loadTxtFile.createNewFile();
                }
                RandomAccessFile raf = new RandomAccessFile(loadTxtFile, "rwd");
                raf.seek(loadTxtFile.length());
                raf.write(content.getBytes());
                raf.close();

                Trace.d("write to txt file content = " + strContent);

                uploadTxt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


    /**
     * 读取文章内容
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    private String readFileContent(String fileName) throws IOException {

        File file = new File(fileName);
        if (file.exists()) {
            BufferedReader bf = new BufferedReader(new FileReader(file));

            String content = "";
            StringBuilder sb = new StringBuilder();

            while (content != null) {
                content = bf.readLine();

                if (content == null) {
                    break;
                }

                sb.append(content.trim());
            }

            bf.close();
            return sb.toString();
        } else {
            return "";
        }

    }


    /**
     * 把统计数据写入Txt文件类
     */
    private class WriteTxtFile implements Runnable {

        String path;

        UserDefinedStatisticModel definedStatisticModel;

        public WriteTxtFile(UserDefinedStatisticModel definedStatisticModel, String path) {
            this.definedStatisticModel = definedStatisticModel;
            this.path = path;
        }

        @Override
        public void run() {
            writeTxtToFile(getContent(definedStatisticModel), path, definedStatisticModel.type);
        }

    }

    /**
     * 用于统计的线程池类
     */
    public static class CountExecutor {
        private static final int CORE_POOL_SIZE = 2;
        private static final int MAXIMUM_POOL_SIZE = 5;
        private static final int KEEP_ALIVE = 1;
        private static final ThreadFactory sThreadFactory = new ThreadFactory() {
            private final AtomicInteger mCount = new AtomicInteger(1);

            public Thread newThread(Runnable r) {
                return new Thread(r, "CountExecutor #" + mCount.getAndIncrement());
            }
        };

        private static final BlockingQueue<Runnable> sPoolWorkQueue =
                new LinkedBlockingQueue<>(128);

        public static final Executor THREAD_POOL_EXECUTOR
                = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
                TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);

        /**
         * 执行统计的线程
         *
         * @param r
         */
        public static void execute(Runnable r) {
            try {
                THREAD_POOL_EXECUTOR.execute(r);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 实体内容
     *
     * @param definedStatisticModel
     * @return
     */
    private String getContent(UserDefinedStatisticModel definedStatisticModel) {

        StringBuffer sb = new StringBuffer();
        sb.append(definedStatisticModel.time).append(MIDDLE_DIVIDER)
                .append(definedStatisticModel.path).append(END_DIVIDER);

        Trace.d("save to txt file content = " + sb.toString());
        return sb.toString();
    }


    /**
     * 设置统计的头部配置信息
     *
     * @param type
     * @return
     */
    private String getHeardContent(int type) {

        StringBuffer sb = new StringBuffer();
        sb.append(getSystemContent().get(APP_VERSION_NAME)).append(END_DIVIDER)
                .append(String.valueOf(type)).append(END_DIVIDER)
                .append((System.currentTimeMillis() / 1000)).append(END_DIVIDER)
                .append(PLATFORM_ID).append(END_DIVIDER)
                .append(getSystemContent().get(OS_DEVICE_VERSION_INFO)).append(HEARD_DIVIDER);

        return sb.toString();

    }

    /**
     * 获取系统中或全局的一些统计字段
     *
     * @return
     */
    private HashMap<String, String> getSystemContent() {
        HashMap<String, String> system = new HashMap<>();
        system.put(APP_VERSION_NAME, CommonUtils.getInstance().getAppVersionName());
        system.put(OS_DEVICE_VERSION_INFO, CommonUtils.getInstance().getDeviceVersion());
        return system;
    }

}
