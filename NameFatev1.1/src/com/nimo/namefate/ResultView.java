package com.nimo.namefate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.net.RequestListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

import com.google.ads.*;

public class ResultView extends Activity {
	RelativeLayout mainRL,centerRL, topRL;
	TextView title;
	private Button btn_shareApp;
	private Button btn_shareScore;
	ShowView sv;
	Canvas c;
	int i=0;
	Timer t;
	String maleName,femaleName;
	int maleStroke,femaleStroke;
	int finalScore=0;
	RelativeLayout rl;
	private final int FP = ViewGroup.LayoutParams.FILL_PARENT;
	private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
	ScrollView m_ScrollView;
	Weibo mWeibo;
	public static Oauth2AccessToken accessToken;
	private TextView mText;
	public static final String TAG = "sinasdk";
	private AdView adView;
	private int AdHeight;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		//处理按钮事件。
        OnClickListener listener = new OnClickListener() {
			public void onClick(View v) {
				if(v.getId() == 111){//“分享应用”按钮
					  Intent intent=new Intent(Intent.ACTION_SEND);
					  intent.setType("text/plain");
					  intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
					  intent.putExtra(Intent.EXTRA_TEXT, "最近下了一个应用可以通过姓名测试两个人的缘分，很有意思，下载地址：http://www.blogjava.net/Files/easywu/NameFate_v2.0.apk");
	                  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					  startActivity(Intent.createChooser(intent, "分享给好友："));
				}
				else 	if(v.getId() == 112){ //“分享结果”按钮
					 saveToSD();
					 Intent intent=new Intent(Intent.ACTION_SEND);
					 File file = new File("mnt/sdcard/namefate.png");
					 intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file)); 
					 intent.setType("image/*");
					 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					 startActivity(Intent.createChooser(intent, "将结果分享给好友："));
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
		
		savedInstanceState = getIntent().getExtras();
		maleName = savedInstanceState.getString("maleName");
		femaleName = savedInstanceState.getString("femaleName");
		System.out.println("maleName is : "+ maleName);
		System.out.println("femaleName is : "+ femaleName);
		
		CnToStroke _maleStroke=new CnToStroke(maleName);
		CnToStroke _femaleStroke=new CnToStroke(femaleName);
		
		maleStroke=_maleStroke.getStroke();
		femaleStroke=_femaleStroke.getStroke();
		
		System.out.println(maleName+ "'s stroke is : "+ maleStroke);
		System.out.println(femaleName+"'s stroke is : "+ femaleStroke);
		
        finalScore=match(maleStroke,femaleStroke);
		
		sv=new ShowView(this,w,h);
		sv.setMaleName(maleName);
		sv.setFemaleName(femaleName);
		
		c=new Canvas();
		sv.setBackgroundResource(R.drawable.mainback);
		
		btn_shareApp = new Button(this);
		btn_shareScore = new Button(this);

		t=new Timer();
		t.schedule(new TimerTask() {
	          @Override
	          public void run() {
	               ResultView.this.runOnUiThread(new Runnable() {
	                   public void run() {
                    	   if (i<=finalScore)
	    	                {
	                		   sv.setKey(i);
	                		   //sv.onDraw(c);
	                		   System.out.println("i:"+i);
	                		   sv.invalidate();//执行该命令后，会自动调用VIEW的onDraw函数，
	                		                   //所以就不必再手工调用一次onDraw函数了。
	    	                i++; 
	    	                }
	                	   if (i>finalScore)
	                	   {
	                			sv.setCtitle(Comment.c_title[(100-finalScore)/2]);
	                			sv.setCcontent(Comment.c_content[(100-finalScore)/2]);
	                			btn_shareApp.setClickable(true);
	                			btn_shareScore.setClickable(true);
	                	   }
   
	                   }
                                     });}
	          }, 0, 100); 
		
		mainRL = new RelativeLayout(this);
		
		topRL = new RelativeLayout(this);//topRL用来顶部的导航栏及按钮。
		topRL.setBackgroundColor(Color.parseColor("#FFF2CC"));
		topRL.setId(110);
		RelativeLayout.LayoutParams topRL_param = new RelativeLayout.LayoutParams(FP, WC);
		topRL_param.addRule(RelativeLayout.ALIGN_TOP);
		mainRL.addView(topRL, topRL_param);
		
		//Button btn_shareApp = new Button(this);
		btn_shareApp.setBackgroundColor(Color.parseColor("#FFE9AB"));
		btn_shareApp.setText("分享应用");
		btn_shareApp.setId(111);
		btn_shareApp.setOnClickListener(listener);
		btn_shareApp.setClickable(false);
		
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
		
		//Button btn_shareScore = new Button(this);
		btn_shareScore.setBackgroundColor(Color.parseColor("#FFE9AB"));
		btn_shareScore.setText("分享结果");
		btn_shareScore.setId(112);
		btn_shareScore.setOnClickListener(listener);
		btn_shareScore.setClickable(false);
		RelativeLayout.LayoutParams btn_shareScore_param = new RelativeLayout.LayoutParams(WC, WC);
		btn_shareScore_param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		topRL.addView(btn_shareScore,btn_shareScore_param);
		
		centerRL = new RelativeLayout(this);
		centerRL.setBackgroundResource(R.drawable.mainback);
		RelativeLayout.LayoutParams centerRL_param = new RelativeLayout.LayoutParams(FP, FP);
		centerRL_param.addRule(RelativeLayout.BELOW,110);
		mainRL.addView(centerRL,centerRL_param);
		
		centerRL.addView(sv);
		
		/*添加Google Admob*/
		adView = new AdView(this, AdSize.BANNER, "a1526fb98491f2e");
		AdHeight=AdSize.BANNER.getHeight();//获取广告条的高度。
		System.out.println("AdHeight is :"+AdHeight);
		LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		centerRL.addView(adView, params);
		adView.loadAd(new AdRequest());
		
		setContentView(mainRL);
		
   }
    
    /*
     * 计算两个姓名缘分值的方法
     * 传入的参数为男女两个姓名的总笔画数。
     * 返回男女两个姓名的缘分值，最高为100分，最低为56分
     * 作弊情况，最低为0分。
     * (non-Javadoc)
     * @see android.app.Activity#onStop()
     */
    public int match(int maleS,int femaleS)//
    {
    	int score=0;//缘分值，最高为100，最低为56分。
    	int _score=0;
    	int _tempM=maleS;
    	int _tempFe=femaleS;
    	if (_tempM==0 || _tempFe==0)//如果计算不出笔画，即非GB2312字符，则用此方法。保证相同字符缘分最高。
    	{
    		for(int i=0;i<(maleName).getBytes().length;i++)
    	          _score+=(maleName).getBytes()[i];
    		score=_score;
    		for(int i=0;i<(femaleName).getBytes().length;i++)
  	          _score+=(femaleName).getBytes()[i];
    		score=100-Math.abs(score-_score);
    	    if (score<0) score=0-score;   
    	    while (score<56||score>100)
    	         {
    	    	  score/=4;
    	    	  score+=56;
    	         }
    	}
    	else
    	{
    		score=100-Math.abs(_tempM-_tempFe);
    		while (score<56||score>100)
	         {
	    	  score/=4;
	    	  score+=56;
	         }
    	}
    	if (score==99)
    		score=98;
    	return score;
    }
    
    @Override
    public void onStop(){//按下返回键时就会执行该回调函数，停止Timer。
    	super.onStop();
    	t.cancel();
    	//t.purge();
     }
    
	public void onStart() {
		super.onStart();
	}

	public void onResume() {
		super.onResume();
	}
    
    @Override
    public void onPause(){//按下房屋键时会执行该回调函数。停止Timer。
    	super.onStop();
    	t.cancel();
    	//t.purge();
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
	   AlertDialog dialog = new AlertDialog.Builder(ResultView.this).create();
	   dialog.setTitle("姓名测缘");//设置标题
	   dialog.setMessage("\n\n" +
	     "姓名测缘 v2.0 \n" +
	     "Copyright by Scott Wu 2013. \n\n" +
	     
	     "使用意见或建议请联系easywu@126.com \n\n");//设置内容
	   dialog.setButton("确定",//设置确定按钮
	     new DialogInterface.OnClickListener() 
	         {
	    @Override
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

	private Bitmap shot() {//截屏的方法   
	      View views = getWindow().getDecorView(); 
	      views.buildDrawingCache(); 
	      // 获取状态栏高度 
	      Rect frames = new Rect(); 
	      views.getWindowVisibleDisplayFrame(frames); 
	     // int statusBarHeights = frames.top-topRL.getHeight(); 
	      Display display = getWindowManager().getDefaultDisplay(); 
	      int widths = display.getWidth(); 
	      int heights = display.getHeight(); 
	      //第一种方式      
	      //views.layout(0, statusBarHeights,widths, heights - statusBarHeights-AdHeight); 
	      //views.layout(0,40,widths, heights-AdHeight+topRL.getHeight()); 
	      //System.out.println("topRL is: "+topRL.getHeight());
	      views.setDrawingCacheEnabled(true);//允许当前窗口保存缓存信息，两种方式都需要加上 
	      //Bitmap bmp = Bitmap.createBitmap(views.getDrawingCache()); 
	      //第二种方式      
	      // 1、source 位图  2、X x坐标的第一个像素  3、Y y坐标的第一个像素  4、宽度的像素在每一行  5、高度的行数 
	      Bitmap bmp = Bitmap.createBitmap(views.getDrawingCache(), 0, topRL.getHeight(),widths, heights - topRL.getHeight()-AdHeight); 
	      System.out.println("here 3！！！！！！！！！！！！！！！！！！！");
	      return bmp;   
	      
	  } 
	
	 private void saveToSD(){
		   try { 
		       String status = Environment.getExternalStorageState(); 
		       // 判SD卡是否存在 
		       System.out.println("status is : "+Environment.MEDIA_MOUNTED);
		       if (status.equals(Environment.MEDIA_MOUNTED)) { 
                   File file = new File(Environment.getExternalStorageDirectory(),"namefate.png"); 
		           System.out.println("Environment is : "+ Environment.getExternalStorageDirectory());
		           // 判断文件A是否存在 
		           //if (file.exists()) { 
		               //String pic_path ="文件夹名" +"图片名"+".png"; 
		           // String pic_path ="/sdcard/testdir/testfile"+".png";
		            //System.out.println("pic_path is : "+pic_path);
		               FileOutputStream out = new FileOutputStream(file); 
		               shot().compress(Bitmap.CompressFormat.PNG,100, out); 
		               out.flush(); 
		               out.close(); 
		               System.out.println("here 4！！！！！！！！！！！！！！！！！！！");
		           //} 
		           
		        
		               
		       } 
		   } catch (FileNotFoundException e) { 
		       e.printStackTrace(); 
		   } catch (IOException e) { 
		       e.printStackTrace(); 
		   } 
		  }  
	 
	 public boolean hasApplication(Intent intent){  //判断系统中是否有可以用于分享的应用如微博。
	        PackageManager packageManager = getPackageManager();  
	        //查询是否有该Intent的Activity  
	        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);  
	        //activities里面不为空就有，否则就没有  
	        return activities.size() > 0 ? true : false;  	 
	 
	 
	 
/*	
	 class AuthDialogListener implements WeiboAuthListener {
	        @Override
	        public void onComplete(Bundle values) {
	            String token = values.getString("access_token");
	            System.out.println("token is : "+token);
	            String expires_in = values.getString("expires_in");
	            System.out.println("expires_in is : "+expires_in);
	            ResultView.accessToken = new Oauth2AccessToken(token, expires_in);
	            System.out.println("accessToken is : "+accessToken);
	            if (ResultView.accessToken.isSessionValid()) {
	                String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
	                        .format(new java.util.Date(ResultView.accessToken
	                                .getExpiresTime()));
	                
	                mText.setText("认证成功: \r\n access_token: " 
	                  + token + "\r\n"
	                        + "expires_in: " + expires_in 
	                        + "\r\n有效期：" + date);
	                try {
	                    Class sso = Class
	                            .forName("com.weibo.sdk.android.api.WeiboAPI");// 如果支持weiboapi的话，显示api功能演示入口按钮
	                   // apiBtn.setVisibility(View.VISIBLE);
	                } catch (ClassNotFoundException e) {
	                    // e.printStackTrace();
	                    Log.i(TAG, "com.weibo.sdk.android.api.WeiboAPI not found");
	                }
	               // cancelBtn.setVisibility(View.VISIBLE);
	               // AccessTokenKeeper.keepAccessToken(ResultView.this,
	               //         accessToken);
	                Toast.makeText(ResultView.this, "认证成功", Toast.LENGTH_SHORT)
	                        .show();
	                StatusesAPI api = new StatusesAPI(ResultView.accessToken);
	       //api.update("测试微博", "90.00", "90.00", new RequestListener(){
	                api.upload("测试微博@瓜妈Gennie", "/sdcard/testfile.png", "90.00", "90.00", new RequestListener(){
	        
	        @Override
	        public void onIOException(IOException arg0){
	         
	        }
	        
	        @Override
	        public void onError(WeiboException arg0){
	         
	        }
	        
	        @Override
	        public void onComplete(String arg0){
	         
	        }
	       });
	            }
	        }


	 


	        @Override
	        public void onError(WeiboDialogError e) {
	            Toast.makeText(getApplicationContext(),
	                    "Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
	        }


	 


	        @Override
	        public void onCancel() {
	            Toast.makeText(getApplicationContext(), "Auth cancel",
	                    Toast.LENGTH_LONG).show();
	        }


	 


	        @Override
	        public void onWeiboException(WeiboException e) {
	            Toast.makeText(getApplicationContext(),
	                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
	                    .show();
	        }

	    }
	 */
   
}
}
