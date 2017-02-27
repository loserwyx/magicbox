package com.syz.magicbox.magicbox.activity;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.syz.magicbox.magicbox.HomeActivity;
import com.syz.magicbox.magicbox.R;
import com.syz.magicbox.magicbox.utils.StreamUtils;
import com.syz.magicbox.magicbox.utils.ToastUtil;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

public class SplashActivity extends Activity{

	private final static String APPINFO_URL = "";
	//16进制数表示消息码
	protected static final int UPDATE_APP = 0x1;
	protected static final int ENTER_APP = 0x2;
	protected static final int ERROR = 0x3;
	private String mDownloadURL = null;
	private int mLocalVersion;
	private int newestVersion;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		initApp();
	}
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_APP:
				popUpdateDialog();
				break;
			case ENTER_APP:
				enterHome();
				break;
			case ERROR:
				 ToastUtil.showToast(getApplicationContext(), "网络异常");
				 enterHome();
			     break;
			default:
				break;
			}
		};
	};
	private String mDownloadUrl;

	private void initApp() {

		//初始化应用数据
		initData();
		//检查更新
		checkUpdate();

	}

	private void enterHome() {
		startActivity(new Intent(SplashActivity.this, HomeActivity.class));
		this.finish();
	}

	/**
	 * 检查更新
	 */
	private void checkUpdate() {
		checkVersion();
	}

	private void checkVersion() {
		// TODO Auto-generated method stub
		getLocalVersion();
		new Thread(){
			public void run() {
				Message msg = Message.obtain();
				long startTime = System.currentTimeMillis();
				try {
					URL url = new URL(APPINFO_URL);
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					//设置访问方式,默认为GET
					connection.setRequestMethod("GET");
					//设置连接超时时间
					connection.setConnectTimeout(5000);
					//获取响应码
					int code = connection.getResponseCode();
					//访问成功
					if (code == 200) {
						InputStream is = connection.getInputStream();
						String versionInfo = StreamUtils.streamToString(is);
						JSONObject jsonInfo = new JSONObject(versionInfo);
						//获取最新版本的版本号
						 newestVersion = jsonInfo.getInt("versionCode");
					}
					if (mLocalVersion < newestVersion) {
						msg.what = UPDATE_APP;
					}else {
						msg.what = ENTER_APP;
					}
				} catch (Exception e) {
					msg.what = ERROR;
					e.printStackTrace();
				}finally{
					long endTime = System.currentTimeMillis();
					if (endTime - startTime < 4000) {
						SystemClock.sleep(4000-(endTime - startTime));
					}
					mHandler.sendMessage(msg);
				}
			};
		}.start();
	}

	protected void popUpdateDialog() {
		Builder builder = new Builder(this);
		builder.setTitle("更新提示");
		builder.setIcon(R.drawable.splash_dialog_icon);
		builder.setMessage(newestVersion);
		builder.setNegativeButton("稍后更新", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				enterHome();
			}
		});
		builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				downloadApk();
			}
		});
		//点击取消事件监听
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				//即使用户点击取消,也需要让其进入应用程序主界面
				enterHome();
				dialog.dismiss();
			}
		});
		
		builder.show();
		
	}

	protected void downloadApk() {
		//apk下载链接地址,放置apk的所在路径
		//1,判断sd卡是否可用,是否挂在上
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			//2,获取sd路径
			String path = Environment.getExternalStorageDirectory().getAbsolutePath()
					+File.separator+"MobileSafe.apk";
			//3,发送请求,获取apk,并且放置到指定路径
			HttpUtils httpUtils = new HttpUtils();
			//4,发送请求,传递参数(下载地址,下载应用放置位置)
			httpUtils.download(mDownloadUrl, path, new RequestCallBack<File>() {
				@Override
				public void onSuccess(ResponseInfo<File> responseInfo) {
					//下载成功(下载过后的放置在sd卡中apk)
					File file = responseInfo.result;
					//提示用户安装
					installApk(file);
				}
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					//下载失败
				}
				//刚刚开始下载方法
				@Override
				public void onStart() {
					super.onStart();
				}
				//下载过程中的方法(下载apk总大小,当前的下载位置,是否正在下载)
				@Override
				public void onLoading(long total, long current,boolean isUploading) {
					super.onLoading(total, current, isUploading);
				}
			});
			
		}
		
	}

	protected void installApk(File file) {
		//系统应用界面,源码,安装apk入口
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		/*//文件作为数据源
		intent.setData(Uri.fromFile(file));
		//设置安装的类型
		intent.setType("application/vnd.android.package-archive");*/
		intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
//		startActivity(intent);
		startActivityForResult(intent, 0);
	}

	private void getLocalVersion() {
		//获取当前版本
		PackageManager pm = getPackageManager();
		try {
			PackageInfo pInfo = pm.getPackageInfo(getPackageName(), 0);
			//获取版本号
			mLocalVersion = pInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		
	}
}
