package com.cyw.mylibrary.dao.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cyw.mylibrary.bean.CannedBean;
import com.cyw.mylibrary.bean.ColorBean;
import com.cyw.mylibrary.bean.DDParamsBean;
import com.cyw.mylibrary.bean.FmParamsBean;
import com.cyw.mylibrary.bean.FormulaData;
import com.cyw.mylibrary.bean.PumpConfigBean;
import com.cyw.mylibrary.bean.ResultBreak;
import com.cyw.mylibrary.bean.ResultData;
import com.cyw.mylibrary.util.DatabaseContext;
import com.cyw.mylibrary.util.GlobalConstants;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SantintDBHelper extends OrmLiteSqliteOpenHelper {
    private static final String DB_NAME = "sqlite.db";
    private Map<String, Dao> daos = new HashMap<String, Dao>();
    public static int dataVersion = 1;
    private Context mContext;

    public SantintDBHelper(Context context) {
        super(context, DB_NAME, null, dataVersion);
        Log.i("TAGVVFILECONSTRUCT", "JJJ" + dataVersion);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database,
                         ConnectionSource connectionSource) {
        Log.i("TAGVVFILEONCREATE", "==========ONCREATE==============");
        try {
            TableUtils.createTableIfNotExists(connectionSource, CannedBean.class);
            TableUtils.createTableIfNotExists(connectionSource, ResultBreak.class);
            TableUtils.createTableIfNotExists(connectionSource, ResultData.class);
            TableUtils.createTableIfNotExists(connectionSource, FormulaData.class);
            TableUtils.createTableIfNotExists(connectionSource, ColorBean.class);
            TableUtils.createTableIfNotExists(connectionSource, PumpConfigBean.class);
            TableUtils.createTableIfNotExists(connectionSource, FmParamsBean.class);
            TableUtils.createTableIfNotExists(connectionSource, DDParamsBean.class);

//            RouterBean routerbean = RouterManager.getInstance().build("/jgchat/bean/getFriendInfo").getRrouterBean(mContext);
//            Method getFriendInfo = null;
//            try {
//
//                getFriendInfo = routerbean.getMyClass().getMethod("getFriendInfo", new Class[0]);
//                Class<?> myClass = routerbean.getMyClass();
//                String value = myClass.getName();
//                Type friendInfoType = getFriendInfo.getGenericReturnType();
//                if (getFriendInfo != null) {
//                    TableUtils.createTableIfNotExists(connectionSource, getFriendInfo.getDeclaringClass());
//                }
//
//            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
//            }
//            TableUtils.createTableIfNotExists(connectionSource, FriendInfo.class);
//            TableUtils.createTableIfNotExists(connectionSource, FriendVersion.class);
//            TableUtils.createTableIfNotExists(connectionSource, CollectionInfo.class);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // try
        // {
        // //TableUtils.createTable(connectionSource, TestInfo.class);
        // // TableUtils.createTable(connectionSource, Article.class);
        // // TableUtils.createTable(connectionSource, Student.class);
        // } catch (SQLException e)
        // {
        // e.printStackTrace();
        // }
    }


    @Override
    public void onUpgrade(SQLiteDatabase database,
                          ConnectionSource connectionSource, int oldVersion, int newVersion) {
//		// try
//		// {
//		// TableUtils.dropTable(connectionSource, TestInfo.class, true);
//		// // TableUtils.dropTable(connectionSource, Article.class, true);
//		// // TableUtils.dropTable(connectionSource, Student.class, true);
//		// onCreate(database, connectionSource);
//		// } catch (SQLException e)
//		// {
//		// e.printStackTrace();
//		// }
        Log.i("TAGVVFILE33", "66666666666666666666" + oldVersion);


        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onCreate(database, connectionSource);

    }

    private static SantintDBHelper instance;

    /**
     * 单例获取该Helper
     *
     * @param context
     * @return
     */
    public static synchronized SantintDBHelper getHelper(Context context) {
        // context = context.getApplicationContext();
        if (instance == null) {
            synchronized (SantintDBHelper.class) {
                if (instance == null)
                    // instance = new DatabaseHelper(context);
                    instance = new SantintDBHelper(new DatabaseContext(context,
                            GlobalConstants.path));
            }
        }

        return instance;
    }

    public synchronized Dao getDao(Class clazz) throws SQLException {
        Dao dao = null;
        String className = clazz.getSimpleName();

        if (daos.containsKey(className)) {
            dao = daos.get(className);
        }
        if (dao == null) {
            dao = (Dao) super.getDao(clazz);//有的时候dao =super.getDao(clazz);报红，是因为JDK高版本可以不显示强制转化，低版本不行，加上强转语法就行
            daos.put(className, dao);
        }
        return dao;
    }

    /**
     * 释放资源
     */
    @Override
    public void close() {
        super.close();

        for (String key : daos.keySet()) {
            Dao dao = daos.get(key);
            dao = null;
        }
    }

//}
// public static final String DB_DIR = Environment
// .getExternalStorageDirectory().getAbsolutePath() + "/UserID/";
// //public static final String DB_DIR = "data/data/"+ "com.example.mysantint" +
// "/databases/";
// public static final String DB_NAME = "sqlitedb3.s3db";
// private AndroidConnectionSource connectionSource;
// private static SantintDBHelper dbHelper;
//
// public static SantintDBHelper getInstance(Context context) {
// if (dbHelper == null) {
// //dbHelper = new SantintDBHelper(context.getApplicationContext());
// dbHelper = new SantintDBHelper(context);
// }
// return dbHelper;
// }
//
// public SantintDBHelper(Context context) {
// File dir = new File(DB_DIR);
// if (!dir.exists()) {
// dir.mkdirs();
// }
// File file = new File(dir, DB_NAME);
// if (!file.exists()) {
// try {
// loadFile(context, file, R.raw.sqlitedb3);
// } catch (IOException e) {
// e.printStackTrace();
// }
// }
// // 标记数据库文件是否创建成功
// boolean isFileCreateSuccess = false;
// // if (!file.exists()) {
// // try {
// //
// // isFileCreateSuccess = file.createNewFile();
// //
// // // if(file.exists()){
// // // file.delete();
// // // }
// // // loadFile(mContext, file, R.raw.sqlitedb4);
// // } catch (IOException e) {
// // e.printStackTrace();
// // }
// // } else
// // isFileCreateSuccess = true;
// SQLiteDatabase db = SQLiteDatabase.openDatabase(file.getPath(), null,
// SQLiteDatabase.OPEN_READWRITE);
// connectionSource = new AndroidConnectionSource(db);
// }

    /**
     * 下在文件到指定目录
     *
     * @param context
     * @param file    sd卡中的文件
     * @param id      raw中的文件id
     * @throws IOException
     */
    public static void loadFile(Context context, File file, int id)
            throws IOException {
        InputStream is = context.getResources().openRawResource(id);
        FileOutputStream fos = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        //buffer = Encoding.UTF8.GetBytes(fileName);//fileName是string类型
        int count = 0;
        while ((count = is.read(buffer)) > 0) {
            fos.write(buffer, 0, count);
        }
        fos.close();
        is.close();
    }
}

// /**
// * 获取dao
// *
// * @param clazz
// * @return
// * @throws SQLException
// */
// public <D extends Dao<T, ?>, T> D getDao(Class<T> clazz) throws Exception {
// if (connectionSource != null) {
// return DaoManager.createDao(connectionSource, clazz);
// }
// return null;
// }
// }
