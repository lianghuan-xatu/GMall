package com.xatu.gmall.util;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class PmsUploadUtil
{
    public static String uploadImage(MultipartFile multipartFile){
        String imgUrl = "http://192.168.0.106";
        //上传图片到服务器
        //配置fdfs全局链接地址
      //  String tracker = PmsUploadUtil.class.getResource("/tracker.conf").getPath();//获取配置文件路径
        try {
            ClientGlobal.init("tracker.conf");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
        TrackerClient trackerClient=new TrackerClient();
        //获取一个trackerServer的实例
        TrackerServer trackerServer=null;
        try {
           trackerServer = trackerClient.getTrackerServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //通过tracker获得一个storage、连接客户端
        StorageClient storageClient=new StorageClient(trackerServer,null);
        try {
            byte[] bytes = multipartFile.getBytes();//获取上传的二进制文件
            // 获取文件后缀名
            String originalFilename = multipartFile.getOriginalFilename();
            System.out.println(originalFilename);
            int i = originalFilename.lastIndexOf(".");
            String extName = originalFilename.substring(i + 1);

            String[] uploadInfos = storageClient.upload_file(bytes, extName, null);

            for(String uploadInfo : uploadInfos){
                imgUrl += "/"+uploadInfo;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }

            return imgUrl;
    }

}
