package com.xatu.gmall;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 *
 *
 */
public class UrlCrawBoke {

    static String userId = "";
    static InputStream is;
    static String pageStr;
    public static void main(String urlstr[]) throws IOException, InterruptedException {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Set<String> urls = new HashSet<String>();

                // ----------------------------------------------遍历每一页 获取文章链接----------------------------------------------
                final String homeUrl = "https://blog.csdn.net/" + userId + "/article/list/";// 后面加pageNum即可
                int totalPage = 0;

                StringBuilder curUrl = null;
                for (int i = 1; i < 100; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("finding page " + i);
                    curUrl = new StringBuilder(homeUrl);
                    curUrl.append(i);
                    System.out.println(curUrl);
                    try {
                        is = doGet(curUrl.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        pageStr = inputStreamToString(is, "UTF-8");// 一整页的html源码
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    List<String> list = getMatherSubstrs(pageStr, "(?<=href=\")https://blog.csdn.net/" + userId + "/article/details/[0-9]{8,9}(?=\")");
                    urls.addAll(list);

                    if (pageStr.lastIndexOf("空空如也") != -1) {
                        System.out.println("No This Page!");
                        break;
                    } else {
                        System.out.println("Success~");
                    }
                    totalPage = i;
                }
                System.out.println("总页数为: " + totalPage);

                // ---------------------------------------------------打印每个链接---------------------------------------------------
                System.out.println("打印每个链接");
                for (String s : urls) {
                    System.out.println(s);
                }
                System.out.println("打印每个链接完毕");

                // ---------------------------------------------------访问每个链接---------------------------------------------------
                int i = 0;
                for (String s : urls) {

                    try {
                        doGet(s);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("成功访问第" + (++i) + "个链接,共" + urls.size() + "个:" + s);
                }

                // ---------------------------------------------------程序结束---------------------------------------------------
                System.out.println("运行完毕，成功增加访问数：" + urls.size());
            }
        },1000,60*1000);


    }

    public static InputStream doGet(String urlstr) throws IOException {
        URL url = new URL(urlstr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        InputStream inputStream = conn.getInputStream();
        return inputStream;
    }

    public static String inputStreamToString(InputStream is, String charset) throws IOException {
        byte[] bytes = new byte[1024];
        int byteLength = 0;
        StringBuffer sb = new StringBuffer();
        while ((byteLength = is.read(bytes)) != -1) {
            sb.append(new String(bytes, 0, byteLength, charset));
        }
        return sb.toString();
    }

    // 正则匹配
    public static List<String> getMatherSubstrs(String str, String regex) {
        List<String> list = new ArrayList<String>();
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        while (m.find()) {
            list.add(m.group());
        }
        return list;
    }
}