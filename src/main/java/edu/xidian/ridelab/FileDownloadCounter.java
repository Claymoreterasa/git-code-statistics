package edu.xidian.ridelab;

import edu.xidian.ridelab.counters.Counter;
import edu.xidian.ridelab.counters.Statistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Author: cwz
 * Time: 2018/4/29
 * Description: 以下载压缩包的形式统计git仓库代码行数，压缩包下载到repository目录
 */
public class FileDownloadCounter {
    private String url;
    private String repository;
    private String repositoryDir = "repository/";
    private Logger LOGGER = LoggerFactory.getLogger(FileDownloadCounter.class);
    private List<Counter> counters = new ArrayList<Counter>();

    /**
     * 初始化
     * @param url url格式为.*.git
     */
    public FileDownloadCounter(String url){
        repository = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
        this.url = url.replace(".git", "/archive/master.zip");
    }

    /**
     * 添加指定文件格式过滤器
     * @param counter
     */
    public void addCounter(Counter counter){
        counters.add(counter);
    }

    /**
     * 进行统计
     */
    public void statistics(){
        String zipPath = downloadFile(url);
        Statistics statistics = readZipFile(zipPath);
        LOGGER.info("result: {}", statistics);
    }

    /**
     * 下载压缩包
     * @param urlString
     * @return
     */
    private String downloadFile(String urlString) {
        LOGGER.info("start download code zip from {}", urlString);
        long startTime = System.currentTimeMillis();
        String outputFile = repositoryDir + repository + ".zip";

        try {
            URL url = new URL(urlString);
            InputStream is = url.openStream();
            BufferedInputStream bin = new BufferedInputStream(is);

            OutputStream out = new FileOutputStream(outputFile);

            byte[] b = new byte[1024];
            int byteread = 0;
            while ((byteread = bin.read(b))!= -1) {
                out.write(b, 0, byteread);
            }

            bin.close();
            out.flush();
            out.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        LOGGER.info("downloaded into {} -> cost {} ms", outputFile ,(endTime - startTime));
        return outputFile;
    }


    /**
     * 流形式读取压缩包文件，过滤并计数
     * @param file
     * @return
     */
    public Statistics readZipFile(String file){
        long startTime = System.currentTimeMillis();
        Statistics totalStatistics = new Statistics(0, 0, 0);
        InputStream in = null;
        ZipInputStream zin = null;
        try {
            ZipFile zf = new ZipFile(file);
            in = new BufferedInputStream(new FileInputStream(file));
            zin = new ZipInputStream(in);
            ZipEntry ze;
            while ((ze = zin.getNextEntry()) != null) {
                if (ze.isDirectory()) {
                } else {
                    String fileName = ze.getName();
                    for(Counter counter : counters){
                        if(counter.filter(fileName)){
                            totalStatistics.add(counter.count(fileName, zf.getInputStream(ze)));
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                zin.closeEntry();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        long endTime = System.currentTimeMillis();
        LOGGER.info("statistics cost {} ms",(endTime - startTime));
        return totalStatistics;
    }

}
