package com.nimo.namefate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

public class ResultView extends Activity {
	ShowView sv;
	Canvas c;
	int i=0;
	Timer t;
	String maleName,femaleName;
	int maleStroke,femaleStroke;
	int finalScore=0;
	RelativeLayout rl;
	ScrollView m_ScrollView;
	Weibo mWeibo;
	public static Oauth2AccessToken accessToken;
	private TextView mText;
	public static final String TAG = "sinasdk";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
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
		setContentView(sv);
		
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
	                	   }
   
	                   }
                                     });}
	          }, 0, 100); 
		
		//addCaseeViewAD(this);
        
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
	     "姓名测缘 v1.1 \n" +
	     "Copyright by Scott Wu 2011. \n\n" +
	     
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
	   
	  case R.id.share:
	   //启动分享
	   
	   mWeibo=Weibo.getInstance("3200369891", "http://www.weibo.com");
	   mWeibo.authorize(ResultView.this, new AuthDialogListener());
	   StatusesAPI api = new StatusesAPI(ResultView.accessToken);
	   api.update("测试微博", "90.00", "90.00", new RequestListener(){
	    
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
	   
	   System.out.println("here 1！！！！！！！！！！！！！！！！！！！");
	   saveToSD();
	   System.out.println("here 2！！！！！！！！！！！！！！！！！！！");
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
	      int statusBarHeights = frames.top; 
	      Display display = getWindowManager().getDefaultDisplay(); 
	      int widths = display.getWidth(); 
	      int heights = display.getHeight(); 
	      //第一种方式      
	      views.layout(0, statusBarHeights,widths, heights - statusBarHeights); 
	      views.setDrawingCacheEnabled(true);//允许当前窗口保存缓存信息，两种方式都需要加上 
	      Bitmap bmp = Bitmap.createBitmap(views.getDrawingCache()); 
	      //第二种方式      
	      // 1、source 位图  2、X x坐标的第一个像素  3、Y y坐标的第一个像素  4、宽度的像素在每一行  5、高度的行数 
	      //Bitmap bmp = Bitmap.createBitmap(views.getDrawingCache(), 0, statusBarHeights,widths, heights - statusBarHeights); 
	      System.out.println("here 3！！！！！！！！！！！！！！！！！！！");
	      return bmp;   
	      
	  } 
	
	 private void saveToSD(){
		   try { 
		       String status = Environment.getExternalStorageState(); 
		       // 判SD卡是否存在 
		       System.out.println("status is : "+Environment.MEDIA_MOUNTED);
		       if (status.equals(Environment.MEDIA_MOUNTED)) { 
		        
		    
		        
		         
		           File file = new File(Environment.getExternalStorageDirectory(),"testfile.png"); 
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
	 
   
}
