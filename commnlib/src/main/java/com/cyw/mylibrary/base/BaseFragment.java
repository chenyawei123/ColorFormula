package com.cyw.mylibrary.base;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cyw.mylibrary.R;
import com.cyw.mylibrary.util.MyTimerTask;

import java.util.Timer;

import me.jessyan.autosize.AutoSizeCompat;
import me.jessyan.autosize.internal.CustomAdapt;

public abstract class BaseFragment extends Fragment implements CustomAdapt {
    protected View rootView;
    private Thread loadDataThread, requestDataThread;
    private LinearLayout mLoadingLayout;
    public String toastText = "加载中...请稍候！";
    private Timer timer = null;
    private MyTimerTask task;
    private Activity mActivity;

    @Override
    public boolean isBaseOnWidth() {
        return false;
    }

    @Override
    public float getSizeInDp() {
        return 857;
    }

    public boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            return networkInfo.isAvailable();
        }
        return false;
    }

    public View findViewById(int resId) {
        return rootView.findViewById(resId);
    }

    protected void initBaseView() {
//		mLoadingLayout = (LinearLayout) rootView
//				.findViewById(R.id.fullscreen_loading_indicator);
    }

    protected void goback() {
        getActivity().setResult(10);
        getActivity().finish();
    }

    public void showToast() {
        Toast.makeText(getActivity(), toastText, Toast.LENGTH_LONG).show();
    }

    public void showToast(String _toastText) {
        Toast.makeText(getActivity(), _toastText, Toast.LENGTH_LONG).show();
    }

    public void showToastShort(String _toastText) {
        Toast.makeText(getActivity(), _toastText, Toast.LENGTH_SHORT).show();
    }

    public void showToast(String _toastText, int time) {
        Toast.makeText(getActivity(), _toastText, time).show();
    }

    public void showLoadingView() {
        mLoadingLayout.setVisibility(View.VISIBLE);
    }

    public void dismissLoadingView() {
        mLoadingLayout.setVisibility(View.GONE);
    }

    protected final int KEY_LOAD_VIEW = 1;
    protected final int KEY_RELOAD_VIEW = 2;
    protected final int KEY_SHOW_NET_ERR_MSG = 3;
    protected final int KEY_SHOW_NET_ERR_MSG_2 = 4;
    protected final int KEY_GET_VIEW = 5;
    protected final int KEY_GET_VIEW_DELAY = 3000;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
//			case KEY_LOAD_VIEW:
//				loadView();
//				break;
                case KEY_GET_VIEW:
                    //getViews();
                    break;
                case KEY_GET_VIEW_DELAY:
                    //getViewDelay();
                    break;
//			case KEY_RELOAD_VIEW:
//				reLoadView();
//				break;
                case KEY_SHOW_NET_ERR_MSG:
                    showToast("请检查是否打开wifi");
                    break;
                case KEY_SHOW_NET_ERR_MSG_2:
                    showToast("请检查是否打开wifi");
                    break;
            }
        }
    };

//	protected abstract void loadData();
//
//	protected abstract void loadView();

//    protected abstract void getData();
//
//    protected abstract void getViews();
//
//    protected abstract void getViewDelay();

//	protected abstract void reLoadData();
//
//	protected abstract void reLoadView();

    //	protected void initCurrentView() {
//		//initBaseView();
//		loadDataThread = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					if (!BaseFragment.this.isNetworkConnected())
//						handler.sendEmptyMessageDelayed(KEY_SHOW_NET_ERR_MSG,
//								100);
//					loadData();
//				} catch (Exception e) {
//					Log.e("BaseFragment",
//							"load data faild, Exception:  " + e.getMessage());
//					handler.sendEmptyMessageDelayed(KEY_SHOW_NET_ERR_MSG_2, 100);
//					return;
//				}
//				handler.sendEmptyMessageDelayed(KEY_LOAD_VIEW, 300);
//			}
//		});
//		loadDataThread.start();
//	}
//	protected void reLoadCurrentView() {
//		showLoadingView();
//		loadDataThread = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					if (!BaseFragment.this.isNetworkConnected())
//						handler.sendEmptyMessageDelayed(KEY_SHOW_NET_ERR_MSG,
//								100);
//					reLoadData();
//				} catch (Exception e) {
//					Log.e("BaseFragment",
//							"load data faild, Exception:  " + e.getMessage());
//					handler.sendEmptyMessageDelayed(KEY_SHOW_NET_ERR_MSG_2, 100);
//					return;
//				}
//				handler.sendEmptyMessageDelayed(KEY_RELOAD_VIEW, 100);
//			}
//		});
//		loadDataThread.start();
//	}
//
//	protected void reLoadCurrentViewNoLoading() {
//		loadDataThread = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					if (!BaseFragment.this.isNetworkConnected())
//						handler.sendEmptyMessageDelayed(KEY_SHOW_NET_ERR_MSG,
//								100);
//					reLoadData();
//				} catch (Exception e) {
//					Log.e("BaseFragment",
//							"load data faild, Exception:  " + e.getMessage());
//					handler.sendEmptyMessageDelayed(KEY_SHOW_NET_ERR_MSG_2, 100);
//					return;
//				}
//				handler.sendEmptyMessageDelayed(KEY_RELOAD_VIEW, 100);
//			}
//		});
//		loadDataThread.start();
//	}
    public void requestData() {
        // final Timer timer = new Timer();
        //timer = new Timer();
        requestDataThread = new Thread(new Runnable() {

            @Override
            public void run() {
                if (!BaseFragment.this.isNetworkConnected()) {
                    handler.sendEmptyMessageDelayed(KEY_SHOW_NET_ERR_MSG,
                            100);
                    return;
                }

                checkTimeOut();
                //getData();

            }
        });
        requestDataThread.start();

    }

    private void checkTimeOut() {
        try {
            timer = new Timer();
            task = new MyTimerTask(handler);
            timer.schedule(task, KEY_GET_VIEW_DELAY);
        } catch (Exception e) {
            Log.i("timer", e.getMessage());
        }
    }

    public void getViewData() {
        timer.cancel();
        handler.sendEmptyMessageDelayed(KEY_GET_VIEW, 100);
    }

//	public void getDelayData() {
//		TimerTask task = new TimerTask() {
//			@Override
//			public void run() {
//				// 需要做的事:发送消息
//				// Message message = new Message();
//				// message.what = KEY_GET_VIEW_DELAY;
//				// handler.sendMessage(message);
//				handler.sendEmptyMessageDelayed(KEY_GET_VIEW_DELAY, 100);
//
//			}
//		};
//		// 5秒后发送消息
//		timer.schedule(task, 3000);
//
//	}

    protected void cancelThread() {
        loadDataThread.destroy();
    }

    protected void startActivityAni(Intent it) {
        getActivity().startActivity(it);
        getActivity().overridePendingTransition(R.anim.entry_from_right,
                R.anim.leave_from_left);
    }

    public void startActivityForResultAni(Intent it, int requestCode) {
        this.startActivityForResult(it, requestCode);
        getActivity().overridePendingTransition(R.anim.entry_from_right,
                R.anim.leave_from_left);
    }

    protected void loadDataCallBack() {
        dismissLoadingView();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AutoSizeCompat.autoConvertDensityOfGlobal((super.getResources()));
    }

    //得到可靠地Activity
    public Activity getMyActivity() {
        return mActivity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);//上面方法替代品，部分小米机型不支持

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
