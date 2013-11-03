package com.nimo.namefate;
//这是Namefate的第二个版本。
//V2.0增加了应用分享和缘分测试结果分享功能，同时去掉了架势广告，改成了谷歌广告。
//V2.1增加了cnzz应用统计的功能。

import java.io.File;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;
import com.google.ads.*;


public class NameFate extends Activity {
	/** Called when the activity is first created. */
	RelativeLayout mainRL, centerRL, topRL, rl1, rl2;
	TextView male, female, title;
	EditText maleName, femaleName;
	Button btn, btn_shareApp;
	private final int FP = ViewGroup.LayoutParams.FILL_PARENT;
	private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
	private AdView adView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//处理按钮事件。
		OnClickListener listener = new OnClickListener() {
			public void onClick(View v) {
				if(v.getId() == 14){//“全部”按钮
					String temp1=maleName.getText().toString();
					String temp2=femaleName.getText().toString();

					if(temp1.equals(null)|| temp1.equals("")
							||temp2.equals(null)||temp2.equals(""))
					{
						AlertDialog dialog = new AlertDialog.Builder(NameFate.this).create();
						dialog.setTitle("注意！");// 设置标题
						dialog.setMessage("请输入姓名一和姓名二");// 设置内容
						dialog.setButton("确定",// 设置确定按钮
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										dialog.cancel();
									}
								});
						dialog.show(); 
					}
					else
					{
						Intent intent = new Intent(NameFate.this,ResultView.class); 
						Bundle bundle = new Bundle(); 
						bundle.putString("maleName", temp1); 
						bundle.putString("femaleName", temp2); 
					    intent.putExtras(bundle); 
						startActivity(intent);
					}
    				 //finish(); 
				}
				else 	if(v.getId() == 111){ 
					  Intent intent=new Intent(Intent.ACTION_SEND);
					  intent.setType("text/plain");
					  intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
					  intent.putExtra(Intent.EXTRA_TEXT, "最近下了一个应用可以通过姓名测试两个人的缘分，很有意思，下载地址：http://www.blogjava.net/Files/easywu/NameFate_v2.0.apk");
	                  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					  startActivity(Intent.createChooser(intent, "分享给好友："));
				}
			}
		};
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		DisplayMetrics displayMetrics = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int h = displayMetrics.heightPixels;
		int w = displayMetrics.widthPixels;

		mainRL = new RelativeLayout(this);
		
		topRL = new RelativeLayout(this);//topRL用来顶部的导航栏及按钮。
		topRL.setBackgroundColor(Color.parseColor("#FFF2CC"));
		topRL.setId(110);
		RelativeLayout.LayoutParams topRL_param = new RelativeLayout.LayoutParams(FP, WC);
		topRL_param.addRule(RelativeLayout.ALIGN_TOP);
		mainRL.addView(topRL, topRL_param);
		
		Button btn_shareApp = new Button(this);
		btn_shareApp.setBackgroundColor(Color.parseColor("#FFE9AB"));
		btn_shareApp.setText("分享应用");
		btn_shareApp.setId(111);
		btn_shareApp.setOnClickListener(listener);
		RelativeLayout.LayoutParams btn_shareApp_param = new RelativeLayout.LayoutParams(WC, WC);
		btn_shareApp_param.addRule(RelativeLayout.ALIGN_LEFT);
		topRL.addView(btn_shareApp,btn_shareApp_param);
		
		TextView title = new TextView(this);
		title.setText("姓名测缘");
		title.setTextSize(20f);
		title.setTextColor(Color.BLACK);
		title.setId(112);
		RelativeLayout.LayoutParams title_param = new RelativeLayout.LayoutParams(
				WC, WC);
		title_param.addRule(RelativeLayout.CENTER_IN_PARENT);
		topRL.addView(title, title_param);
	
		centerRL = new RelativeLayout(this);
		centerRL.setBackgroundResource(R.drawable.mainback);
		RelativeLayout.LayoutParams centerRL_param = new RelativeLayout.LayoutParams(FP, FP);
		centerRL_param.addRule(RelativeLayout.BELOW,110);
		mainRL.addView(centerRL,centerRL_param);
		
		rl1 = new RelativeLayout(this);//截一个同屏宽，4/5高的区域放在屏幕中间。
		RelativeLayout.LayoutParams rl1_param = new RelativeLayout.LayoutParams(
				FP, h / 5 * 4);
		// rl1_param.addRule(RelativeLayout.);
		rl1_param.addRule(RelativeLayout.CENTER_IN_PARENT);

		rl2 = new RelativeLayout(this);//在rl1里面在截一个2/3宽，和rl1同高的区域出来放在中间，这个区域用来放TextView、EditView、Button部件。
		RelativeLayout.LayoutParams rl2_param = new RelativeLayout.LayoutParams(
				w / 3 * 2, FP);
		rl2_param.addRule(RelativeLayout.CENTER_IN_PARENT);
		rl1.addView(rl2, rl2_param);

		male = new TextView(this);
		male.setText(R.string.name1);
		male.setTextSize(20f);
		male.setTextColor(Color.BLACK);
		male.setId(10);
		RelativeLayout.LayoutParams male_param = new RelativeLayout.LayoutParams(
				WC, WC);
		male_param.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		male_param.addRule(RelativeLayout.CENTER_VERTICAL);
		rl2.addView(male, male_param);

		maleName = new EditText(this);
		maleName.setId(11);
		RelativeLayout.LayoutParams maleName_param = new RelativeLayout.LayoutParams(
				FP - male.getMeasuredWidth(), WC);
		maleName_param.addRule(RelativeLayout.RIGHT_OF, 10);
		maleName_param.addRule(RelativeLayout.CENTER_VERTICAL);
		rl2.addView(maleName, maleName_param);

		female = new TextView(this);
		female.setText(R.string.name2);
		female.setTextSize(20f);
		female.setTextColor(Color.BLACK);
		female.setId(12);
		RelativeLayout.LayoutParams female_param = new RelativeLayout.LayoutParams(
				WC, WC);
		female_param.addRule(RelativeLayout.BELOW, 11);
		female_param.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rl2.addView(female, female_param);

		femaleName = new EditText(this);
		femaleName.setId(13);
		RelativeLayout.LayoutParams femaleName_param = new RelativeLayout.LayoutParams(
				FP - female.getMeasuredWidth(), WC);
		femaleName_param.addRule(RelativeLayout.RIGHT_OF, 12);
		femaleName_param.addRule(RelativeLayout.BELOW, 11);
		rl2.addView(femaleName, femaleName_param);

		btn = new Button(this);
		btn.setId(14);
		btn.setOnClickListener(listener);
		btn.setText(R.string.cacu);
		RelativeLayout.LayoutParams btn_param = new RelativeLayout.LayoutParams(
				FP, WC);
		btn_param.addRule(RelativeLayout.BELOW, 13);
		rl2.addView(btn, btn_param);
		
		
		
		centerRL.addView(rl1, rl1_param);


		/*添加Google Admob*/
		adView = new AdView(this, AdSize.BANNER, "a1526fb98491f2e");
		LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		mainRL.addView(adView, params);
		adView.loadAd(new AdRequest());
		
		setContentView(mainRL);

	}


	
	public void onStart() {
		

		super.onStart();
	}

	public void onResume() {
		
	
		super.onResume();
	}

	public void onPause() {
		
	
		super.onPause();
	}

	public void onStop() {
		
	
		super.onStop();
	}

	
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int item_id = item.getItemId();
		
		switch (item_id)
		{
		case R.id.about:
			//启动About
			AlertDialog dialog = new AlertDialog.Builder(NameFate.this).create();
			dialog.setTitle("姓名测缘");//设置标题
			dialog.setMessage("\n\n" +
					"姓名测缘 v2.0 \n" +
					"Copyright by Scott Wu 2013. \n\n" +
					
					"使用意见或建议请联系easywu@126.com \n\n");//设置内容
			dialog.setButton("确定",//设置确定按钮
					new DialogInterface.OnClickListener() 
	        {
				public void onClick(DialogInterface dialog, int whichButton)
				{
		            dialog.cancel();
	        }
	        });
		    dialog.show();
		    break;

		}
		return true;
	}
	
}
