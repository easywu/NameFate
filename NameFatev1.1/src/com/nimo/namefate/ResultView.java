package com.nimo.namefate;

import java.util.Timer;
import java.util.TimerTask;

import com.casee.adsdk.CaseeAdView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
	CaseeAdView cav;
	

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
	                		   sv.invalidate();//ִ�и�����󣬻��Զ�����VIEW��onDraw������
	                		                   //���ԾͲ������ֹ�����һ��onDraw�����ˡ�
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
		
		addCaseeViewAD(this);
        
    }
    
    /*
     * ������������Ե��ֵ�ķ���
     * ����Ĳ���Ϊ��Ů�����������ܱʻ�����
     * ������Ů����������Ե��ֵ�����Ϊ100�֣����Ϊ56��
     * ������������Ϊ0�֡�
     * (non-Javadoc)
     * @see android.app.Activity#onStop()
     */
    public int match(int maleS,int femaleS)//
    {
    	int score=0;//Ե��ֵ�����Ϊ100�����Ϊ56�֡�
    	int _score=0;
    	int _tempM=maleS;
    	int _tempFe=femaleS;
    	if (_tempM==0 || _tempFe==0)//������㲻���ʻ�������GB2312�ַ������ô˷�������֤��ͬ�ַ�Ե����ߡ�
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
    public void onStop(){//���·��ؼ�ʱ�ͻ�ִ�иûص�������ֹͣTimer��
    	
    	super.onStop();
    	t.cancel();
    	if (cav != null) {
			cav.onUnshown();
		}
    	//t.purge();
    	
    }
    
	public void onStart() {
		
		if (cav != null) {
			cav.onShown();
		}
		super.onStart();
	}

	public void onResume() {
		
		if (cav != null) {
			cav.onShown();
		}
		super.onResume();
	}
    
    @Override
    public void onPause(){//���·��ݼ�ʱ��ִ�иûص�������ֹͣTimer��
    	
    	super.onStop();
    	t.cancel();
    	if (cav != null) {
			cav.onUnshown();
		}
    	//t.purge();
    	
    }
    
	//��������ط���
	private CaseeAdView addCaseeViewAD(Activity activity) { 

    	//��������������� 
    	RelativeLayout relativeLayout = new RelativeLayout(this); 
    	LayoutParams params = new LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, 
    	ViewGroup.LayoutParams.FILL_PARENT); 
    	activity.addContentView(relativeLayout, params);
    	
    	cav = new CaseeAdView(activity, "7C9E4ED0A7E99152FD8E5DCE3B203273", 
    			false, 10 * 1000,//��ʽ����Ϊfalse 
    			Color.BLACK, Color.WHITE, false); 
    	params = new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 
    			                  ViewGroup.LayoutParams.WRAP_CONTENT); 
    	// params.addRule(RelativeLayout.ALIGN_PARENT_TOP); 
    	params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM); 
    	relativeLayout.addView(cav, params); 
    	return cav; 
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
			//����About
			AlertDialog dialog = new AlertDialog.Builder(ResultView.this).create();
			dialog.setTitle("������Ե");//���ñ���
			dialog.setMessage("\n\n" +
					"������Ե v1.1 \n" +
					"Copyright by Scott Wu 2011. \n\n" +
					
					"ʹ�������������ϵeasywu@126.com \n\n");//��������
			dialog.setButton("ȷ��",//����ȷ����ť
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
