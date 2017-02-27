package com.syz.magicbox.magicbox.activity;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.syz.magicbox.magicbox.HomeActivity;
import com.syz.magicbox.magicbox.R;
import com.syz.magicbox.magicbox.utils.ToastUtil;

/**
 * @author chx
 *
 */
public class RegisterActivity extends Activity implements View.OnClickListener {

	
	/**
	 * 从MOB官网获取APPKEY 和 APPSECRET
	 */
	private final static String APPKEY = "18efe99fa3226";
	private final static String APPSECRET = "c54d8ff252945bf36f05f129a80f3cdb";
	private EditText edtTxt_name;
	private EditText edtTxt_phone;
	private EditText edtTxt_code;
	private EditText edtTxt_pwd;
	private EditText edtTxt_repwd;
	private Button btn_getcode;
	private Button btn_register;
	private ImageButton ib_back;
	private boolean isPhone =false;
	private TimeCount mTimeCount;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		SMSSDK.initSDK(this, APPKEY, APPSECRET);
		initUI();
		initEvent();
	}
	private void initEvent() {
         //注册短信回调
		 SMSSDK.registerEventHandler(mEvent);
	     btn_getcode.setOnClickListener(this);
	     btn_register.setOnClickListener(this);
	     ib_back.setOnClickListener(this);
	}
	private void initUI() {
		edtTxt_code = (EditText) findViewById(R.id.edttxt_register_code);
		edtTxt_name = (EditText) findViewById(R.id.edttxt_register_name);
		edtTxt_phone = (EditText) findViewById(R.id.edttxt_register_phone);
		edtTxt_pwd = (EditText) findViewById(R.id.edttxt_register_pwd);
		edtTxt_repwd = (EditText) findViewById(R.id.edttxt_register_repeat);
		btn_getcode = (Button) findViewById(R.id.btn_register_getcode);
		btn_register = (Button) findViewById(R.id.btn_register_register);
		ib_back = (ImageButton) findViewById(R.id.ib_register_back);
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_register_getcode:
			checkPhone(); 
			break;
		case R.id.btn_register_register:
			String code = edtTxt_code.getText().toString().trim();
			String phone = edtTxt_phone.getText().toString().trim();
			if (!TextUtils.isEmpty(code) && !TextUtils.isEmpty(phone) && !isPhone) {
				 SMSSDK.submitVerificationCode("86", phone, code);
			}else {
				ToastUtil.showToast(RegisterActivity.this, "验证码错误，请重新获取");
				isPhone = false;
			}
			break;
		case R.id.ib_register_back:
			onBackPressed();
			break;
		default:
			break;
		}
	}

	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			if (isPhone) {
				handleRegister();
				ToastUtil.showToast(RegisterActivity.this, "验证成功！");
			}
			if (msg.what == 0x10) {
				//初始化计时
		        mTimeCount = new TimeCount(60000, 1000);
		        //开始计时
		        mTimeCount.start();
			}
		};
	};
	
		
/*	public void onRegister();        //注册时回调
	public void beforeEvent(int event, Object data);  //操作执行前调用，event表示操作类型，data表示传入数据
	
	//afterEvent在操作结束时被触发，
           同样具备event和data参数，但是data是事件操作结果，其具体取值根据参数result而定。
    sult是操作结果，为SMSSDK.RESULT_COMPLETE表示操作成功，为SMSSDK.RESULT_ERROR表示操作失败。
          当result=SMSSDK.RESULT_ERROR，则data的类型为Throwable；
          如果服务器有返回错误码，那么这个Throwable的message就存放着服务器返回的json数据，你可以从中读取相关信息。
	public void afterEvent(int event, int result, Object data); 
	//反注册时触发
	public void onUnregister();*/
    /**
     * 短信验证回调
     */
    private EventHandler mEvent = new EventHandler(){
		/* (non-Javadoc)
		 * @see cn.smssdk.EventHandler#afterEvent(int, int, java.lang.Object)
		 * 时间
		 */
		@Override
		public void afterEvent(int event, int result, Object data) {
			if (result == SMSSDK.RESULT_COMPLETE) {
				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
					//提交验证码成功
					isPhone = true;
					mHandler.sendEmptyMessage(0);
				}else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
					//获取短信验证码成功后
					//更新按钮状态
				    Message msg = Message.obtain();
				    msg.what = 0x10;
				    mHandler.sendMessage(msg);
				}else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
					//返回支持发送验证码的国家列表
				}
			}else {
				((Throwable)data).printStackTrace();
				Log.i("REGISTER", "回调失败");
			}
		}
	};
	/**
	 *验证电话,获取验证码 
	 */
	private void checkPhone() {
		String phone = edtTxt_phone.getText().toString().trim();
		if (!TextUtils.isEmpty(phone)) {
			//获取短信验证码
			SMSSDK.getVerificationCode("86", phone);
		}
	}
	
	private void handleRegister() {
		String name = edtTxt_name.getText().toString().trim();
		String pwd = edtTxt_pwd.getText().toString().trim();
		String repwd = edtTxt_repwd.getText().toString().trim();
		if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(repwd)) {
			if (pwd.equals(repwd)) {
				boolean isSuccess = saveNewUser();
				if (isSuccess) {
					startActivity(new Intent(RegisterActivity.this,HomeActivity.class));
					ToastUtil.showToast(getApplicationContext(), "注册成功");
				}else {
					ToastUtil.showToast(getApplicationContext(), "系统忙，请稍后再试");
				}
			}else {
				ToastUtil.showToast(getApplicationContext(), "两次密码输入不一致");
			}
		}else {
			ToastUtil.showToast(getApplicationContext(), "注册内容不能为空");
		}
	}
	/**
	 * 把新注册的用户数据发送到服务器端进行存储
	 * @return
	 */
	private boolean saveNewUser() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	protected void onDestroy() {
		//在注册页面销毁后，注销短信验证回调事件
		SMSSDK.unregisterEventHandler(mEvent);
		super.onDestroy();
	}
	
	/**
	 * 定义一个计时类继承倒计时类
	 * @author chx
	 *  millisInFuture:倒计时总时间;countDownInterval:倒计时间隔时间
	 */
	class TimeCount extends CountDownTimer{

		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		/* (non-Javadoc)
		 * @see android.os.CountDownTimer#onTick(long)
		 * 
		 * 计时过程中
		 */
		@Override
		public void onTick(long millisUntilFinished) {
			btn_getcode.setClickable(false);
			btn_getcode.setText(millisUntilFinished / 1000 + "秒后重新获取");
		}

		/* (non-Javadoc)
		 * @see android.os.CountDownTimer#onFinish()
		 *计时完成
		 */
		@Override
		public void onFinish() {
			btn_getcode.setText("获取验证码");
			btn_getcode.setClickable(true);
		}
		
	}
	
}
