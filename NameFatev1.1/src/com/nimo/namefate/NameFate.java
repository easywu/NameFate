package com.nimo.namefate;
//这是Namefate的第二个版本。
//测试git push
//测试git push 2

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
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;




public class NameFate extends Activity {
	/** Called when the activity is first created. */
	RelativeLayout mainRL, rl1, rl2;
	TextView male, female;
	EditText maleName, femaleName;
	Button btn;
	private final int FP = ViewGroup.LayoutParams.FILL_PARENT;
	private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//处理按钮事件。
		OnClickListener listener = new OnClickListener() {
			public void onClick(View v) {
				if(v.getId() == 14){//“全部”按钮
					String temp1=maleName.getText().toString();
					String temp2=femaleName.getText().toString();
					System.out.println("From NameFate: maleName is : "+maleName);
					System.out.println("From NameFate: femaleName is : "+femaleName);
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
		mainRL.setBackgroundResource(R.drawable.mainback);

		rl1 = new RelativeLayout(this);
		RelativeLayout.LayoutParams rl1_param = new RelativeLayout.LayoutParams(
				FP, h / 5 * 4);
		// rl1_param.addRule(RelativeLayout.);
		rl1_param.addRule(RelativeLayout.CENTER_IN_PARENT);
		

		rl2 = new RelativeLayout(this);
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
		
		mainRL.addView(rl1, rl1_param);
		setContentView(mainRL);

		//加入广告加载方法
		//addCaseeViewAD(this);

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
					"姓名测缘 v1.1 \n" +
					"Copyright by Scott Wu 2011. \n\n" +
					
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
