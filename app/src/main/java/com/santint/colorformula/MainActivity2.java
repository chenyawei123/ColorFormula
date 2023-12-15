package com.santint.colorformula;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SDCardUtils;
import com.cyw.mylibrary.base.BaseAppCompatActivity;
import com.cyw.mylibrary.daos.CannedResultDao;
import com.cyw.mylibrary.services.CannedResultService;
import com.santint.colorformula.utils.UtilsWithPermission;

import org.xmlpull.v1.XmlSerializer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * author： cyw
 */
public class MainActivity2 extends BaseAppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        writeXml();
        setContentView(R.layout.activity_main2);
        //moveTaskToBack(true);
        readWritePermission();
        Button button = findViewById(R.id.btn_click);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CannedResultDao dao = CannedResultService.getInstance().getDao();
                Intent intent = new Intent(MainActivity2.this, CorrectionActivity.class);
                startActivity(intent);
            }
        });
        EditText editText = findViewById(R.id.edittext);
        editText.setFilters(new InputFilter[]{lendFilter});
        int numOfDecimals = 3;
//        String s = numOfDecimals > 0 ? ("{0," + numOfDecimals + "}$") : "*";
//        Log.i("TAG",s);
    }
    /**
     * 设置小数后位数控制
     */
    private InputFilter lendFilter = new InputFilter() {
        //start == 0  end =1   dstart dend 相等，从0 开始变化  ，source 为新输入的值   dest 为已经输入的值
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            // 删除等特殊字符，直接返回
            if ("".equals(source.toString())) {
                return null;
            }
            String dValue = dest.toString();
            String[] splitArray = dValue.split("\\.");
            if (splitArray.length > 1) {
                String dotValue = splitArray[1];
                int diff = dotValue.length() + 1 - 3;//4表示输入框的小数位数
                if (diff > 0) {
                    return source.subSequence(start, end - diff);
                }
            }
            return null;
        }
    };

    private boolean isRefuse = false;
    private final int REQ_WRITE_STORAGE = 11;


    private void writeXml() {
        OutputStream output = null;
        try {
            output = new FileOutputStream(SDCardUtils.getSDCardPathByEnvironment() + "/colorlink"+"/12.txt");

            XmlSerializer xmlSerializer = Xml.newSerializer();
            Map<String, String> map = new HashMap<>();
            map.put("k1", "v1");
            map.put("k2", "v2");
            map.put("k3", "v3");
            xmlSerializer.setOutput(output, "UTF-8");
            xmlSerializer.startDocument("UTF-8", true);

            xmlSerializer.startTag(null, "ROOT");

            for (Map.Entry<String, String> entry : map.entrySet()) {
               // xmlSerializer.startTag(null, "K");
                // xmlSerializer.attribute(null, TAG_ID, Integer.toString(people.id));

                writeTextTag(xmlSerializer, entry.getKey(), entry.getValue());
//            writeTextTag(xmlSerializer, TAG_ADDR, people.addr);
//            writeTextTag(xmlSerializer, TAG_AGE, Integer.toString(people.age));

               // xmlSerializer.endTag(null, "K");

            }


//        for (People people : peopleList) {
//            xmlSerializer.startTag(null, TAG_PEOPLE);
//            xmlSerializer.attribute(null, TAG_ID, Integer.toString(people.id));
//
//            writeTextTag(xmlSerializer, TAG_NAME, people.name);
//            writeTextTag(xmlSerializer, TAG_ADDR, people.addr);
//            writeTextTag(xmlSerializer, TAG_AGE, Integer.toString(people.age));
//
//            xmlSerializer.endTag(null, TAG_PEOPLE);
//        }

            xmlSerializer.endTag(null, "ROOT");
            xmlSerializer.endDocument();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeTextTag(XmlSerializer xmlSerializer, String tag, String text)
            throws IOException {
        xmlSerializer.startTag(null, tag);
        xmlSerializer.text(text);
        xmlSerializer.endTag(null, tag);
    }

    private void readWritePermission() {
        //需要注意
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            UtilsWithPermission.readWriteStorage(MainActivity2.this, REQ_WRITE_STORAGE);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !isRefuse) {// android 11  且 不是已经被拒绝
            // 先判断有没有权限
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1024);
            }
        }
    }

    // 带回授权结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1024 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 检查是否有权限
            if (Environment.isExternalStorageManager()) {
                isRefuse = false;
                CannedResultService.getInstance().initDao(MainActivity2.this);
                // 授权成功
            } else {
                isRefuse = true;
                // 授权失败
            }
        }
    }
}
