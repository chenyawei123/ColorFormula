package com.example.datacorrects.utils;

import android.content.Context;
import android.os.Environment;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.SDCardUtils;
import com.cyw.mylibrary.util.WriteLog;

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
import java.util.Arrays;
import java.util.List;

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
    public static File dir = new File(SDCardUtils.getSDCardPathByEnvironment() + "/colorformula"+ "/correctmilk");


    public static void saveToSDCard(Context mActivity, String filename, String content) {
        String en = Environment.getExternalStorageState();
        //获取SDCard状态,如果SDCard插入了手机且为非写保护状态
        if (en.equals(Environment.MEDIA_MOUNTED)) {
            BufferedWriter writer = null;
            OutputStream out = null;
            try {
                FileUtils.createOrExistsDir(dir);
                String filePath = dir.getAbsolutePath()+File.separator+filename;
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
            }finally {
                try{
                    if(writer!=null){
                        writer.close();
                    }
                    if(out!=null){
                        out.close();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
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
            String filePath = dir.getAbsolutePath()+File.separator+fileName;
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
                if(bufferedReader!=null){
                    bufferedReader.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if(in!=null){
                    in.close();
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
            File sdFile = FileUtils.getFileByPath(dir+fileName);
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
