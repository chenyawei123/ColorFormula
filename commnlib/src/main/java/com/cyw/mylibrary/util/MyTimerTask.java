package com.cyw.mylibrary.util;

import android.os.Handler;

import java.util.TimerTask;

public class MyTimerTask extends TimerTask{
	private final Handler mHandler ;
	public MyTimerTask(Handler mHandler){
		this.mHandler = mHandler;
	}
	@Override
	public void run() {
		mHandler.sendEmptyMessage(3000);
		
	}

}
