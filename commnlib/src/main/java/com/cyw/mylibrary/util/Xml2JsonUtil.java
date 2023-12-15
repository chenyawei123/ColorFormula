//package com.cyw.mylibrary.util;
//
//import android.util.Xml;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.PrintStream;
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//
//
//public class Xml2JsonUtil {
//    /**
//     * 转换一个xml格式的字符串到json格式
//     * @param xml    xml格式的字符串
//     * @return 成功返回json 格式的字符串;失败反回null
//     */
//    public static  String xml2JSON(String xml) {
//        new GsonBuilder().registerTypeAdapter(Xml.class,new Xmla)
//    }
//
//    /**
//     * 转换一个xml格式的字符串到json格式
//     * @param file     java.io.File实例是一个有效的xml文件
//     * @return 成功反回json 格式的字符串;失败反回null
//     */
//    public static String xml2JSON(File file) {
//        JSONObject obj = new JSONObject();
//        try {
//            SAXBuilder sb = new SAXBuilder();
//            Document doc = sb.build(file);
//            Element root = doc.getRootElement();
//            obj.put(root.getName(), iterateElement(root));
//            return obj.toString();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    /**
//     * 一个迭代方法
//     * @param element
//     *  org.jdom.Element
//     * @return java.util.Map 实例
//     */
//    @SuppressWarnings("unchecked")
//    private static Map  iterateElement(Element element) {
//        List jiedian = element.getChildren();
//        Element et = null;
//        Map obj = new HashMap();
//        List list = null;
//        for (int i = 0; i < jiedian.size(); i++) {
//            list = new LinkedList();
//            et = (Element) jiedian.get(i);
//            if (et.getTextTrim().equals("")) {
//    /*            if (et.getChildren().size() == 0)
//                    continue;  */
//                if (obj.containsKey(et.getName())) {
//                    list = (List) obj.get(et.getName());
//                }
//                list.add(iterateElement(et));
//                obj.put(et.getName(), list);
//            } else {
//                if (obj.containsKey(et.getName())) {
//                    list = (List) obj.get(et.getName());
//                }
//                list.add(et.getTextTrim());
//                obj.put(et.getName(), list);
//            }
//        }
//        return obj;
//    }
//
//    public static void readToBuffer(StringBuffer buffer, String filePath) throws IOException {
//        InputStream is = new FileInputStream(filePath);
//        String line; // 用来保存每行读取的内容
//        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//        line = reader.readLine(); // 读取第一行
//        while (line != null) { // 如果 line 为空说明读完了
//            buffer.append(line); // 将读到的内容添加到 buffer 中
//            buffer.append("\n"); // 添加换行符
//            line = reader.readLine(); // 读取下一行
//        }
//        reader.close();
//        is.close();
//    }
//
//    /**
//     * 读取文本文件内容
//     * @param filePath 文件所在路径
//     * @return 文本内容
//     * @throws IOException 异常
//     */
//    public static String readFile(String filePath) throws IOException {
//        StringBuffer sb = new StringBuffer();
//        Xml2JsonUtil.readToBuffer(sb, filePath);
//        return sb.toString();
//    }
//
//    //写字符串到文件
//    public static void WriteStringToFile(String filePath,String s) {
//        try {
//            File file = new File(filePath);
//            PrintStream ps = new PrintStream(new FileOutputStream(file));
//            ps.println(s);// 往文件里写入字符串
//            ps.close();
//            //ps.append("http://www.docin.com/p-315288370.html");// 在已有的基础上添加字符串
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public static void getFile(String path,int item) throws IOException{
//        File file = new File(path);
//        File[] array = file.listFiles();
//        System.out.println("一共"+array.length+"个文件");
//        for(int i=0;i<array.length;i++){
//            if(array[i].isFile()){
//                System.out.println("搜索到文件:" + array[i].getName());
//                String newName=array[i].getName().substring(0, array[i].getName().indexOf("."))+".json";
//                String json=Xml2JsonUtil.xml2JSON(Xml2JsonUtil.readFile(array[i]+""));
//                Xml2JsonUtil.WriteStringToFile("d:/wuSir/"+item+"/"+newName,json);
//            }else if(array[i].isDirectory()){
//                getFile(array[i].getPath(),item);
//            }
//        }
//    }
//
//    public static boolean createDir(String destDirName) {
//        File dir = new File(destDirName);
//        if (dir.exists()) {
//            System.out.println("创建目录" + destDirName + "失败，目标目录已经存在");
//            return false;
//        }
//        if (!destDirName.endsWith(File.separator)) {
//            destDirName = destDirName + File.separator;
//        }
//        //创建目录
//        if (dir.mkdirs()) {
//            System.out.println("创建目录" + destDirName + "成功！");
//            return true;
//        } else {
//            System.out.println("创建目录" + destDirName + "失败！");
//            return false;
//        }
//    }
//
//    // 测试
//    public static void main(String[] args) throws IOException {
//        for(int i=2007;i<2018;i++){
//            Xml2JsonUtil.createDir("d:/wuSir/"+i);
//            Xml2JsonUtil.getFile("d:/xmls/"+i+"/",i);
//        }
//    }
//}