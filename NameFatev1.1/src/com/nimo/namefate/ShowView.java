package com.nimo.namefate;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.view.View;

public class ShowView extends View {
    
	int w;
	int h;
	int key=0;
	String maleName="";
	String femaleName="";
	String c_title="";
	String c_content="";
	Paint p=new Paint();

	public ShowView(Context con,int wid,int hei){
		super(con);
		w=wid;
		h=hei;
		
	}
    
	public void setKey(int k){
		key=k;
	}
	
	public void setMaleName(String m)
	{
		maleName=m;
	}
	
	public void setFemaleName(String f)
	{
		femaleName=f;
	}
	
	public void setCtitle(String t)
	{
		c_title=t;
	}
	
	public void setCcontent(String c)
	{
		c_content=c;
	}
	
	public void onDraw(Canvas c) {
		
			p.setColor(Color.TRANSPARENT);
			p.setTextSize(10f);
			c.drawRect(0,0,w,h,p);
			
			p.setFlags(Paint.ANTI_ALIAS_FLAG);//消除字体锯齿
			p.setColor(Color.BLACK);
			p.setTextSize(12f);
			p.setTextAlign(Align.LEFT);
			c.drawText(maleName+" 和 "+femaleName+ "," ,w/20,h/2-40, p);
			c.drawText("你们的缘份值是...",w/20,h/2-25, p);	
			c.drawText("（满分为100分）",w/20,h/2-10, p);	

            p.setColor(Color.RED);
            p.setTextAlign(Align.CENTER);
            p.setTextSize(50f);
			c.drawText(Integer.toString(key),w/2,h/2+15,p);
			
			p.setColor(Color.BLUE);
			c.drawLine(0, h/2+25, w, h/2+25, p);
			
            p.setColor(Color.BLACK);
            p.setTextAlign(Align.CENTER);
            p.setStyle(Paint.Style.STROKE);
            p.setTextSize(20f);
			c.drawText(c_title,w/2,h/2+50,p);
			
            
			
			p.setColor(Color.BLACK);
            p.setTextAlign(Align.LEFT);
            p.setTextSize(15f);
                        
            //StringBuffer lines = new StringBuffer();
            int line_width_temp= 0;
            String s_temp = "  "+c_content;
            
            for (int i=0;s_temp.length()>0;i++)
            {
            	line_width_temp = p.breakText(s_temp, true, w, null); 
            	System.out.println("line_width_temp is : "+line_width_temp);
                //lines.add(s_temp.substring(0, line_width_temp-1));
            	
            	if (line_width_temp<s_temp.length())
            	{
            		c.drawText(s_temp.substring(0, line_width_temp-1),5,h/2+50+25+25*i,p);
            		s_temp=s_temp.substring(line_width_temp-1, s_temp.length());
               	}
            	else
            	{
            		c.drawText(s_temp.substring(0, line_width_temp),5,h/2+50+25+25*i,p);
            		s_temp="";
            	}
                System.out.println("s_temp is : "+s_temp);
                
                
            }
 		
			
			System.out.println("w1 is : "+w);
			System.out.println("h1 is : "+h);
			
			
			
			
		
	}
	
	

}
