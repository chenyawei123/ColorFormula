package com.cyw.mylibrary.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.os.Environment;
import android.util.Log;

import com.cyw.mylibrary.dao.dbhelper.SantintDBHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class DatabaseContext extends ContextWrapper {
    private static final String ROOT_SDCARD = Environment
            .getExternalStorageDirectory().getAbsolutePath();
    private String dbDir;
    private Context context;
    private SantintDBHelper helper;
//    String dbPath;
//    File dbFile;

    public DatabaseContext(Context base, String path) {
        super(base);
        context = base;
        dbDir = path;
        //getPermission();
    }
//	//必须有此权限才能读取上传图片
//	void getPermission() {
//		int permissionCheck1 = ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
//		int permissionCheck2 = ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
//		if (permissionCheck1 != PackageManager.PERMISSION_GRANTED || permissionCheck2 != PackageManager.PERMISSION_GRANTED) {
//			ActivityCompat.requestPermissions((MainActivity)context,
//					new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
//					124);
//		}
//	}
//
//	@Override
//	public void onRequestPermissionsResult(int requestCode,
//										   String[] permissions,
//										   int[] grantResults) {
//		if (requestCode == 124) {
//			if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//				Log.d("heihei", "获取到权限了！");
//
//			} else {
//				Log.d("heihei", "搞不定啊！");
//			}
//		}da
//	}

    @Override
    public File getDatabasePath(String name) {

        // 判断是否存在sd卡
            /*boolean sdExist = android.os.Environment.MEDIA_MOUNTED
                    .equals(android.os.Environment.getExternalStorageState());
	        if (!sdExist) {// 如果不存在,
	            Log.e("Database error", "SD卡不存在");
	            return null;
	        }*/


        // 判断目录是否存在，不存在则创建该目录
        File dirFile = new File(dbDir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }


        // 标记数据库文件是否创建成功
        boolean isFileCreateSuccess = false;
        // String dbPath = dbDir +"/"+"userIdList"+"/"+ String.valueOf(UserService.getUserId())+"/" + name;// 数据库路径
        String dbPath = dbDir + "/" + name;
//        String dbPath2 = dbDir + "/" + "sqlitedb3.s3db-shm";
//        String dbPath3 = dbDir + "/" + "sqlitedb3.s3db-wal";
        File dbFile = new File(dbPath);//dbDir, name
//        File dbFile2 = new File(dbPath2);
//        File dbFile3 = new File(dbPath3);
//        boolean isFirstRun = SharedPreferencesHelper.getBoolean(context, "isFirstRun", true);
//        if (isFirstRun) {
//            Log.i("TAGVVFILESS", "66666666666666666666");
//            SharedPreferencesHelper.putBoolean(context, "isFirstRun", false);
//            if (dbFile.exists()) {
//                dbFile.delete();
//            }
//            if (dbFile2.exists()) {
//                dbFile2.delete();
//            }
//            if (dbFile3.exists()) {
//                dbFile3.delete();
//            }
//        }
//        if (!dbFile.exists()) {
//            try {
//                isFileCreateSuccess = dbFile.createNewFile();// 创建文件
//                Log.i("TAGVVFILE", "66666666666666666666");
//                loadFile(context, dbFile, R.raw.sqlitedb3 );
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        } else {
//            isFileCreateSuccess = true;
//
////            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbPath,null);
////            if(db!=null){
//            Log.i("TAGVVFILE22", "66666666666666666666");
////                db.close();
////            }
//
////            helper.onUpgrade(db,db.getVersion(),SantintDBHelper.dataVersion);
//
//        }
//        if (dbFile2.exists()) {
//            dbFile2.delete();
//            boolean dd = dbFile2.exists();
//            Log.i("TAGVVFILEDD", dd + "jjjjjjj");
//        }
//        if (dbFile3.exists()) {
//            dbFile3.delete();
//        }
//        //db.close();
//        //返回数据库文件对象
//        if (isFileCreateSuccess) {
//            Log.i("TAGVVFILE88", "66666666666666666666");
//            return dbFile;
//        } else {
//            Log.i("TAGVVFILE99", "66666666666666666666");
//            return null;
//        }
//
//        //super.getDatabasePath(dbPath);
//        //return dbFile;
        return dbFile;

    }



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
        int count = 0;
        while ((count = is.read(buffer)) > 0) {
            fos.write(buffer, 0, count);
        }
        fos.close();
        is.close();
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode,
                                               CursorFactory factory) {
        Log.i("TAG7", "66666666666666666666");
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(
                getDatabasePath(name), null);
        return result;
    }

    /**
     * Android 4.0会调用此方法获取数据库。
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode,
                                               CursorFactory factory, DatabaseErrorHandler errorHandler) {
        Log.i("TAG6", "66666666666666666666");
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(
                getDatabasePath(name), null);
//	        SQLiteDatabase result = SQLiteDatabase.openDatabase(dbFile.getPath(), null,
//	                SQLiteDatabase.OPEN_READWRITE);
        return result;
    }
}
