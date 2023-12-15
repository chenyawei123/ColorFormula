package com.cyw.mylibrary.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.SDCardUtils;
import com.cyw.mylibrary.bean.ColorBean;
import com.cyw.mylibrary.bean.FormulaData;
import com.cyw.mylibrary.services.ColorBeanService;
import com.cyw.mylibrary.services.FormulaDataService;

import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.arnaudguyon.xmltojsonlib.JsonToXml;
import fr.arnaudguyon.xmltojsonlib.XmlToJson;

/**
 *
 */

public class SaveJsonFile {
    /**
     * 保存json到本地
     *
     * @param mActivity
     * @param filename
     * @param content
     */
    private String dbDir;
    private static Context context;
    private static final String datasJson = "CalibrationDatas.json";
    private static final String planJson = "CalibrationPlan.json";
    private static final String configJson = "PumpConfig.json";
    private final String settingJson = "PumpSetting.json";
    public static File dir = new File(SDCardUtils.getSDCardPathByEnvironment() + "/colorformula" + "/correctmilk");
    public static File dir2 = new File(SDCardUtils.getSDCardPathByEnvironment() + "/colorlink");
    public static File dirBusy = new File(SDCardUtils.getSDCardPathByEnvironment() + "/colorlinkbusy");
    public static File dirResult = new File(SDCardUtils.getSDCardPathByEnvironment() + "/colorlinkresult");


    public static void saveToSDCard(Context mActivity, String filename, String content) {
        String en = Environment.getExternalStorageState();
        //获取SDCard状态,如果SDCard插入了手机且为非写保护状态
        if (SDCardUtils.isSDCardEnableByEnvironment()) {
            Log.i("TAG", "HHHHH");
        } else {
            if (SDCardUtils.isSDCardEnable()) {
                Log.i("TAG", "HHHHH");
            }
        }
        if (en.equals(Environment.MEDIA_MOUNTED)) {
            BufferedWriter writer = null;
            OutputStream out = null;
            try {
                FileUtils.createOrExistsDir(dir);
                String filePath = dir.getAbsolutePath() + File.separator + filename;
                File file = FileUtils.getFileByPath(filePath);
                FileUtils.createOrExistsFile(file);
                out = new FileOutputStream(file);
                //out.write(content.getBytes());
                writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                writer.write(content);
                // writer.write(content,0,len);//使用这种形式不需要flush，有bufferWriter，bufferOutputStream封装都需要flush
                //out.close();
                // out.flush();///////////////////////看源码什么都没有做
                writer.flush();///////////////////////新增
                // writer.close();//将缓冲区数据强制写入到文件，不管缓冲区是否满

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (writer != null) {
                        writer.close();
                    }
                    if (out != null) {
                        out.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void writeXml(String code, List<String> list, List<String> list2,String innerCode) {
        OutputStream output = null;
        try {
            FileUtils.createOrExistsDir(dirResult);
            String filePath2 = dirResult.getAbsolutePath() + File.separator + formulaFileName + "return.xml";
           File file = new File(filePath2);
           if(file.exists()){
               file.delete();
           }
           file.createNewFile();
            output = new FileOutputStream(file);
            XmlSerializer xmlSerializer = Xml.newSerializer();

//            map.put("k1", "v1");
//            map.put("k2", "v2");
//            map.put("k3", "v3");
            xmlSerializer.setOutput(output, "UTF-8");
            xmlSerializer.startDocument("UTF-8", true);
            xmlSerializer.startTag(null, "AcutalQuantity");
            writeTextTag(xmlSerializer, "ControlCode", code);
            writeTextTag(xmlSerializer, "InnerColorCode", innerCode);
//            for (Map.Entry<String, String> entry : map.entrySet()) {
//                // xmlSerializer.startTag(null, "K");
//                // xmlSerializer.attribute(null, TAG_ID, Integer.toString(people.id));
//                writeTextTag(xmlSerializer, entry.getKey(), entry.getValue());
//            }
            for (int i = 0; i < list.size(); i++) {
                String key = list.get(i);
                String value = list2.get(i);
                writeTextTag(xmlSerializer, "ColorantCode", key);
                writeTextTag(xmlSerializer, "ActualQua", value);
            }


            xmlSerializer.endTag(null, "AcutalQuantity");
            xmlSerializer.endDocument();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void writeTextTag(XmlSerializer xmlSerializer, String tag, String text)
            throws IOException {
        xmlSerializer.startTag(null, tag);
        xmlSerializer.text(text);
        xmlSerializer.endTag(null, tag);
    }
    //BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
//   writer.write(content);//content.getBytes()
    //DataOutputStream dataOutputStream = new DataOutputStream(out);
// dataOutputStream.writeBytes(new String(content.getBytes(),"UTF-8"));
//  writer.close();
// dataOutputStream.close();

//    /**
//     * 从本地读取json
//     */
//    public static String readTextFile(String filePath, int type) {
//        StringBuffer sb = new StringBuffer();
//        try {
//            File file = new File(dir + "/" + filePath);
//            InputStream in = null;
//            if (file.exists()) {
//                in = new FileInputStream(file);
//            }
//            if (in != null) {
//                byte[] buffer = new byte[1024];
//                int len = -1;
//                while ((len = in.read(buffer)) != -1) {
//                    sb.append(new String(buffer, 0, len, "UTF-8"));
//                }
//                in.close();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return sb.toString();
//    }

    /**
     * 从本地读取json
     */
    public static String readTextFile(String fileName, int type) {
        StringBuffer sb = new StringBuffer();
        BufferedReader bufferedReader = null;
        InputStream in = null;
        InputStreamReader inputStreamReader = null;
        try {
            String filePath = dir.getAbsolutePath();
            FileUtils.createOrExistsDir(filePath);
            filePath = filePath + File.separator + fileName;
            File file = new File(filePath);
            if (file.exists()) {
                in = new FileInputStream(file);
            }
            if (in != null) {
                inputStreamReader = new InputStreamReader(in, "UTF-8");
                char[] buffer = new char[4 * 1024];//245760
                int len = -1;
                bufferedReader = new BufferedReader(inputStreamReader);
                while ((len = bufferedReader.read(buffer)) != -1) {
                    sb.append(new String(buffer, 0, len));
                    // content+= new String(buffer);
                }
                // bufferedReader.close();
                //in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (in != null) {
                    in.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return sb.toString();
    }

    /**
     * 从本地删除配方文件
     */
    public static void deleteFormulaFile(String fileName) {

        try {

            ///////////////读xml
            String filePath = dir2.getAbsolutePath() + File.separator + formulaFileName + ".xml";
            // FileUtils.createOrExistsDir(filePath);
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    /**
     * 从本地删除忙文件
     */
    public static void deleteBusyFile(String fileName) {

        try {

            ///////////////读xml
            String filePath = dirBusy.getAbsolutePath() + File.separator + fileName;
            // FileUtils.createOrExistsDir(filePath);
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    /**
     * 从本地读取忙文件
     */
    public static boolean readBusyFile(String fileName) {
        try {

            ///////////////读xml
            // String filePath = dir2.getAbsolutePath() + File.separator + fileName;
            String filePath = dirBusy.getAbsolutePath();
            FileUtils.createOrExistsDir(filePath);
            filePath = filePath + File.separator + fileName;
            File file = new File(filePath);
            if (file.exists()) {
                return true;
            } else {
                file.createNewFile();
            }
            return false;


        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return false;
    }

    /**
     * 从本地读取xml  并生成json文件
     */
    public static String readXmlFile(String fileName, int type) {
        StringBuffer sb = new StringBuffer();
        BufferedReader bufferedReader = null;
        InputStream in = null;
        InputStreamReader inputStreamReader = null;
        BufferedWriter bufferedWriter = null;
        OutputStream outputStream = null;
        String xmlString = "";
        String jsonString = "";
        try {

            ///////////////读xml
            String filePath = dir2.getAbsolutePath();
            FileUtils.createOrExistsDir(filePath);
            filePath = filePath + File.separator + fileName;
            //
            File file = new File(filePath);
            if (file.exists()) {
                in = new FileInputStream(file);//文件操作___32__32文件被打开
            }
            ///////////////////////写json
//            String filePath2 = dir2.getAbsolutePath() + File.separator + formulaFileName+".json";
//            File file2 = new File(filePath2);
//            outputStream = new FileOutputStream(file2);// 文件操作___2__2文件被修改 文件操作___32__32文件被打开
//            bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            if (in != null) {
                inputStreamReader = new InputStreamReader(in, "UTF-8");
                char[] buffer = new char[4 * 1024];//245760
                int len = -1;
                bufferedReader = new BufferedReader(inputStreamReader);
                while ((len = bufferedReader.read(buffer)) != -1) {//文件操作___1__1打开文件后读取文件的操作
                    sb.append(new String(buffer, 0, len));
                    // content+= new String(buffer);
                }
                xmlString = sb.toString();
                // String jsonstring4 = com.cyw.mylibrary.util.XmlToJson.xml2JSON(string);
                XmlToJson xmlToJson = new XmlToJson.Builder(xmlString).build();
                String jsonString3 = xmlToJson.toJson().toString();
                jsonString = xmlToJson.toString();
                String jsonString2 = xmlToJson.toFormattedString();
                Log.i("TAG", jsonString + jsonString2 + jsonString3 + "jj");

////                int length = in.available();
////                byte[] bytes = new byte[length];
////                in.read(bytes);
////                XmlToJson xmlToJson = new XmlToJson.Builder(new String(bytes)).build();
////                String jsonString = xmlToJson.toString();
////                String jsonString2 = xmlToJson.toFormattedString();
////                //String jsonString3 = xmlToJson.toFormattedString("UTF-8");
////                System.out.println("正常json数据:\n" + jsonString+"jj"+jsonString2);
////                System.out.println("格式化json数据:\n" + xmlToJson.toFormattedString());
////                System.out.println("\n" + xmlToJson.toFormattedString("\t"));//要使用的缩进，例如“”或“\ t”。
//
//                bufferedWriter.write(jsonString);
//                bufferedWriter.flush();//  文件操作___2__2文件被修改  文件操作___16__16只读文件被关闭   文件操作___8__8文件写入或编辑后关闭
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (in != null) {
                    in.close();
                }
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return jsonString;
    }

    /**
     * 拿到本地json字符串，写入xml
     */
    public static String readJsonFile(String json, int type) {
        StringBuffer sb = new StringBuffer();
        BufferedReader bufferedReader = null;
        InputStream in = null;
        InputStreamReader inputStreamReader = null;
        BufferedWriter bufferedWriter = null;
        OutputStream outputStream = null;
        String string = "";
        try {
            ///////////////读json
//            String filePath = dir2.getAbsolutePath() + File.separator + fileName;
//            File file = new File(filePath);
//            if (file.exists()) {
//                in = new FileInputStream(file);
//            }
            ///////////////////////写xml
            FileUtils.createOrExistsDir(dirResult);
            String filePath2 = dirResult.getAbsolutePath() + File.separator + formulaFileName + "return.xml";//改为别人xml名+return
            File file2 = new File(filePath2);
            outputStream = new FileOutputStream(file2);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
//            if (in != null) {
//                inputStreamReader = new InputStreamReader(in, "UTF-8");
//                char[] buffer = new char[4 * 1024];//245760
//                int len = -1;
//                bufferedReader = new BufferedReader(inputStreamReader);
//                while ((len = bufferedReader.read(buffer)) != -1) {
//                    sb.append(new String(buffer, 0, len));
//                    // content+= new String(buffer);
//                }
//                string = sb.toString();
//                // String jsonstring4 = com.cyw.mylibrary.util.XmlToJson.xml2JSON(string);
            JsonToXml jsonToXml = new JsonToXml.Builder(json).build();
            String xmlString = jsonToXml.toString();
            String xmlString2 = jsonToXml.toFormattedString();
            //jsonToXml.toFormattedString()
            Log.i("TAG", xmlString + xmlString2);
            // formulaFileName = "";//开始准备获取下一个配方文件名

//                int length = in.available();
//                byte[] bytes = new byte[length];
//                in.read(bytes);
//                XmlToJson xmlToJson = new XmlToJson.Builder(new String(bytes)).build();
//                String jsonString = xmlToJson.toString();
//                String jsonString2 = xmlToJson.toFormattedString();
//                //String jsonString3 = xmlToJson.toFormattedString("UTF-8");
//                System.out.println("正常json数据:\n" + jsonString+"jj"+jsonString2);
//                System.out.println("格式化json数据:\n" + xmlToJson.toFormattedString());
//                System.out.println("\n" + xmlToJson.toFormattedString("\t"));//要使用的缩进，例如“”或“\ t”。

            bufferedWriter.write(xmlString2);
            bufferedWriter.flush();
            //}

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (in != null) {
                    in.close();
                }
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return string;
    }

    static List<String> list = new ArrayList<>();

    //json字符串集合
    public static List<String> readAllFile(String fileName, int type, Context context) {
        traverse(dir2);
        return list;
    }

    public static void saveFileNameId(FormulaData formulaData) {
        if (formulaFileName != null && formulaFileName.length() > 0 && !FormulaDataService.getInstance().isExist(formulaFileName)) {
            formulaData.setFileName(formulaFileName);
            FormulaDataService.getInstance().save(formulaData);
        }

    }

    public static void saveFormulaItem(List<ColorBean> colorBeans) {
        ColorBeanService.getInstance().saveMulti(colorBeans);

    }

    public static void updateFormulaItem() {
        // ColorBeanService.getInstance().update
    }

    private static String formulaFileName = "";

    private static void traverse(File directory) {
        if (!directory.exists()) {
            return;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isFile()) {
                // 处理文件
                String fileName = file.getName();
                if (fileName.endsWith(".xml") && !fileName.equals("busy.xml")) {
                    formulaFileName = fileName.substring(0, fileName.length() - 4);
                    String jsonString = readXmlFile(fileName, 0);
                    list.add(jsonString);
                }

                System.out.println("文件名：" + file.getName());
            } else if (file.isDirectory()) {
                // 处理文件夹
                traverse(file);
            }
        }
    }


    /**
     * 从本地读取json
     */
    public static String readAssetFile(String fileName, int type, Context context) {
        StringBuffer sb = new StringBuffer();
        BufferedReader bufferedReader = null;
        InputStream in = null;
        InputStreamReader inputStreamReader = null;
//        FileOutputStream fos = null;
//        BufferedOutputStream bos = null;
        BufferedWriter bufferedWriter = null;
        OutputStream outputStream = null;
        try {
            String filePath = dir.getAbsolutePath() + File.separator + fileName;
            File file = new File(filePath);
            outputStream = new FileOutputStream(file);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
//            if (file.exists()) {
//                in = new FileInputStream(file);
//
//            }
            //if(!file.exists()) {
            in = context.getAssets().open(fileName);
            if (in != null) {
                inputStreamReader = new InputStreamReader(in, "UTF-8");
                char[] buffer = new char[4 * 1024];//245760
                int len = -1;
                bufferedReader = new BufferedReader(inputStreamReader);
                while ((len = bufferedReader.read(buffer)) != -1) {
                    sb.append(new String(buffer, 0, len));
                    // content+= new String(buffer);
                }
                bufferedWriter.write(sb.toString());
                bufferedWriter.flush();
                // bufferedReader.close();
                //in.close();
            }
            // }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (in != null) {
                    in.close();
                }
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return sb.toString();
    }
//    private void writeMilk(Context mActivity, String filename, List<MilkFormula> list){
//        String en = Environment.getExternalStorageState();
//        //获取SDCard状态,如果SDCard插入了手机且为非写保护状态
//        if (en.equals(Environment.MEDIA_MOUNTED)) {
//            try {
//                if (!dir.exists()) {
//                    dir.mkdirs(); //create folders where write files
//                }
//
//                File file = new File(dir, filename);
//                if (!file.exists()) {
//                    file.createNewFile();
//                }
//                OutputStream out = new FileOutputStream(file);
//                //创建JsonWrite对象
//                JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "utf-8"));
//                writer.setIndent("    ");
//                    writer.beginArray();
//                    for (MilkFormula milkFormula :list){
//                        writer.beginObject();
//                        writer.name("milkName").value(milkFormula.id);
//                        writer.name("milkSort").value(milkFormula.name);
//                        writer.name("foodPic").value(milkFormula.name);
//                        writer.name("milkId").value(milkFormula.name);
//                        writer.name("url").value(milkFormula.name);
//                        writer.name("titleName").value(milkFormula.name);
//                        writer.name("tag").value(milkFormula.name);
//                        writer.name("isTitle").value(milkFormula.name);
//                        writer.name("name").value(milkFormula.name);
//                        writer.name("price").value(milkFormula.name);
//                        writer.name("foodCount").value(milkFormula.name);
//                        writer.name("buzhou").value(milkFormula.name);
//                        writer.name("nameDataBeanCounts").value(milkFormula.getNameDataBeanCounts());
//                        writer.name("bigStep").value(milkFormula.getBigStep());
//                        writer.name("side").value(milkFormula.getSide());
//                        writer.endObject();
//                    }
//                    writer.endArray();
//                    writer.close();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                //ToastUtil.showToast(mActivity, "保存失败");
//            }
//        }

    //   }

    /**
     * 将集合写入sd卡
     *
     * @param fileName 文件名
     * @param list     集合
     * @return true 保存成功
     */
    public static <T> boolean writeListIntoSDcard(String fileName, List<T> list) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //File sdCardDir = Environment.getExternalStorageDirectory();//获取sd卡目录
            File sdFile = new File(dir, fileName);
            try {
                FileOutputStream fos = new FileOutputStream(sdFile);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(list);//写入
                fos.close();
                oos.close();


                FileInputStream fis = new FileInputStream(sdFile);
                ObjectInputStream ois = new ObjectInputStream(fis);
                list = (List<T>) ois.readObject();
                String value = Arrays.toString(list.toArray());
                WriteLog.writeTxtToFile(value);
                fis.close();
                ois.close();
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 读取sd卡对象
     *
     * @param fileName 文件名
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> readListFromSdCard(String fileName) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {  //检测sd卡是否存在
            List<T> list;
            //File sdCardDir = Environment.getExternalStorageDirectory();
            File sdFile = FileUtils.getFileByPath(dir + fileName);
            try {
                FileInputStream fis = new FileInputStream(sdFile);
                ObjectInputStream ois = new ObjectInputStream(fis);
                list = (List<T>) ois.readObject();
                fis.close();
                ois.close();
                return list;
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
                return null;
            } catch (OptionalDataException e) {
                e.printStackTrace();
                return null;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

}
