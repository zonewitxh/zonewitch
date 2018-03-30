package features;
import java.io.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import preprocess.Parameters;
import similarity.Cosin;
import dbconnect.Connectdb;
public class WeightCal 
{
	public static String weightfile="result/weight.txt";
	public static String segfile="result/target/";
	public WeightCal()
	{
		readmap();
	}
	public void end()
	{
		writemap();
	}
	//去除词性并按词性筛选词语，让一个文件里每种词只出现一次
	public void onlyword(String sr) 
	{
		File src=new File(sr);
		File[] filesrc = src.listFiles();
		for(int i=0;i<filesrc.length;i++){
			File read=new File(sr+filesrc[i].getName());
			Reader reader = null;
	        try 
	        {
	            reader = new InputStreamReader(new FileInputStream(read));
	            int tempchar;
	            String word="";
	            String text="";
	            String vo="";
	            ArrayList list=new ArrayList();
	            boolean flag=false;
	            while ((tempchar=reader.read())!= -1) 
	            {
	            	if((char)tempchar!=' ')
	            	{
	            		if((char)tempchar=='/')
	            			flag=true;
	            		if(flag==false)
	            			word+=(char)tempchar;
	            		else
	            			vo+=(char)tempchar;
	            	}
	            	else
	            	{
	            		if(list.contains(word))
	            		{
	            			word="";
	            			vo="";
	            			flag=false;
	            		}
	            		else
	            		{
	            			if(accept(vo))
	            			{
			            		list.add(word);
			            		text=text+word+" ";
	            			}
	            			//System.out.println(word+vo);
		            		word="";
		            		vo="";
		            		flag=false;
	            		}
	            	}
	            }
	            reader.close();
	            read.delete();
	            FileWriter fw = new FileWriter(read);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(text);
				bw.close();
				fw.close();
	        } 
	        catch (Exception e) 
	        {
	            e.printStackTrace();
	        }
		}
	}
	//同一天的文件放在一起
	public void onlyfile()
	{
		try
		{
			File src=new File(WeightCal.segfile);
			File[] li=src.listFiles();
			ArrayList<File> filetgt = new ArrayList<File>();
			for(int i=0;i<li.length;i++){
				if(filetgt.contains(li[i]))
					continue;
				BufferedReader reader=new BufferedReader(new FileReader(li[i]));
				String tempf = li[i].getName().substring(0, 10);
				BufferedWriter writer=new BufferedWriter(new FileWriter(segfile+tempf+".txt",true));
				if(tempf.compareTo(Parameters.datefrom)<=0)
				{
					
					Parameters.setDatefrom(tempf);
				}
				if(tempf.compareTo(Parameters.dateto)>=0)
				{
					Parameters.setDateto(tempf);
				}
				String temp="";
				while((temp=reader.readLine())!=null)
				{
					writer.write(li[i].getName().substring(li[i].getName().indexOf("&")+1,li[i].getName().indexOf("."))+" "+temp+"\r\n");
				}
				reader.close();
				for(int j=i+1;j<li.length;j++)
				{
					if(li[i].getName().substring(0, 10).equals(li[j].getName().substring(0, 10)))
					{
						BufferedReader reader1=new BufferedReader(new FileReader(li[j]));
						String temp1="";
						while((temp1=reader1.readLine())!=null)
						{
							writer.write(li[j].getName().substring(li[j].getName().indexOf("&")+1,li[j].getName().indexOf("."))+" "+temp1+"\r\n");
						}
						reader1.close();
						filetgt.add(li[j]);
					}
				}
				writer.close();
			}
		}catch(Exception e){}
	}	
	public Stack<String> getwordlist(String sr)
	{
		Stack<String> s=new Stack<String>();
		String word="";
		try
		{
			for(int i=0;i<sr.length();i++) 
			{
				String tempchar=" ";
				if(i!=sr.length()-1)
					tempchar=sr.substring(i,i+1);
	        	if(!tempchar.equals(" "))
	        	{
	        		word+=tempchar;
	        	}
	        	else
	        	{	
	        		s.push(word);
	        		word="";
	        	}
	        }
		}catch(Exception e)
		{
			System.out.println("getwordlist"+e+sr);
		}
		return s;
	}	
	public Map<Integer,String[]> wm=new HashMap<Integer,String[]>();
	public void writemap()
	{
		try
		{
			BufferedWriter writer=new BufferedWriter(new FileWriter("result/weight.txt"));
			for(Integer key:wm.keySet()){
				writer.write(String.valueOf(key)+":"+wm.get(key)[0]+wm.get(key)[1]+"\r\n");
				//System.out.println(String.valueOf(key)+":"+wm.get(key)[0]+wm.get(key)[1]+"\r\n");
			}
			writer.close();
		}catch(Exception e)
		{
			System.out.println("writemap"+e);
		}
	}
	public void readmap()
	{
		try
		{
			BufferedReader reader=new BufferedReader(new FileReader("result/weight.txt"));
			wm=new HashMap<Integer,String[]>();
			String temp;
			while((temp=reader.readLine())!=null)
			{
				String[] s={temp.substring(temp.indexOf(":")+1, temp.indexOf(",")),temp.substring(temp.indexOf(","))};
				wm.put(Integer.valueOf(temp.substring(0, temp.indexOf(":"))), s);
			}
			reader.close();
		}catch(Exception e)
		{
			System.out.println("readmap"+e);
		}
	}
	//移除过期的权重
	public Stack<Integer> Weightdelete(Date d)
	{
		Stack<Integer> id=new Stack<Integer>();
		List<Integer> a=new ArrayList<Integer>();
		try
		{
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			for(Integer key:wm.keySet())
			{
				if(!sdf.format(d).equals(wm.get(key)[0]))
					continue;
				id.push(key);
				a.add(key);
			}
			for(Integer tmp:a)
			{
		        wm.remove(tmp);
		    }
			return id;
		}
		catch(Exception e)
		{
			System.out.println("DELE"+e);
			return id;
		}
	}
	//计算某时刻所有词的权重
	public Stack<Integer> Weight(Date d) 
	{
		Stack<Integer> id=new Stack<Integer>();
        try
        {
       	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
       		double Ear=0,BS=0,ar=0,Earprevious=0,TOP=0,weight=0;
        	Date atcurrent=d,atprevious=sdf.parse("1970-01-01");
        	File src=new File(WeightCal.segfile+sdf.format(d)+".txt");
        	if(!src.exists())
        		return id;
			String tempstring="";
			BufferedReader reader=new BufferedReader(new FileReader(src));
			while((tempstring=reader.readLine())!=null)
			{
				id.push(Integer.valueOf(tempstring.substring(0, tempstring.indexOf(" "))));
				tempstring=tempstring.substring(tempstring.indexOf(" ")+1);
				Stack<String> s=getwordlist(tempstring);
				String text=",";
				while(!s.empty())
				{
					String word=s.pop();
					double n=1, total=1;;
					int atpre=-1;
					for(Integer key:wm.keySet())
					{
						total++;
						int i;
						if((i=wm.get(key)[1].indexOf(","+word+" "))!=-1)
						{
							n++;
							if(key>atpre)
							{
								atpre=key;
								atprevious=sdf.parse(wm.get(key)[0]);
								String temp=wm.get(key)[1].substring(i+2+word.length());
								temp=temp.substring(temp.indexOf(" ")+1);
								Earprevious=Double.parseDouble(temp.substring(0,temp.indexOf(",")));
							}
						}
					}
					TOP=n/total;
				    if(!atprevious.equals(sdf.parse("1970-01-01")))
				    {
				    	Long l=atcurrent.getTime()-atprevious.getTime();
				    	Double d1=Double.parseDouble(l.toString());
				    	ar=1.0/(d1/(24*60*60*1000)+1.0);
				    	
				    }
				    else
				    {
				    	ar=1.0/90.0;
				    }
				    Ear=Earprevious+(1/n)*(ar-Earprevious);
			    	BS=(ar-Ear)/Ear;
			    	if(BS<0)
			    		BS=0;
			    	//System.out.println(BS+"BS");
			    	weight=BS*TOP;
			    	text+=(word+" "+String.valueOf(weight)+" "+String.valueOf(Ear)+",");
				}
				String[] value={sdf.format(d),text};
				if(!text.equals(","))
					wm.put(id.peek(), value);
				//writer.write(text+"\r\n");
			}
			reader.close();
			//writer.close();
			return id;
		}
		catch(Exception e)
        {
			System.out.println("ADD"+e+id.peek());
			return id;
		}  
	}	
	private boolean accept(String word) 
	{
		boolean accept = false;
		int x=word.indexOf("/");
		String type=word.substring(x+1);
		if (type.startsWith("n") // 名词
				|| type.startsWith("t") // 时间词
				|| type.startsWith("s") // 处所词
				|| type.startsWith("f") // 方位词
				|| type.startsWith("a") // 形容词
				|| type.startsWith("v") // 动词
				|| type.startsWith("b") // 区别词
				|| type.startsWith("z") // 状态词
				// ||type.startsWith("r") //代词
				// ||type.startsWith("m") //数词
				|| type.startsWith("q") // 量词
				// ||type.startsWith("d") //副词
				//|| type.startsWith("p") // 介词
				//|| type.startsWith("c") // 连词
				// ||type.startsWith("u") //助词
				// ||type.startsWith("e") //叹词
				// ||type.startsWith("y") //语气词
				|| type.startsWith("o") // 拟声词
				|| type.startsWith("h") // 前缀
				|| type.startsWith("k") // 后缀
				//|| type.startsWith("x") // 网址URL
				// ||type.startsWith("w") //标点符号
		) 
		{
			return true;
		}
		return accept;
	}
	//动态计算对象之间的距离
	public void distance(Date d)
	{
		Calendar cal = Calendar.getInstance(); 
		Date addwin=d,remwin,calda,stop;
		cal.setTime(d); 
        cal.add(cal.MONTH, -3);
        remwin=cal.getTime();
        cal.add(cal.MONTH, 9);
        calda=cal.getTime();
        cal.add(cal.MONTH, 4);
        stop=cal.getTime();
        while(!addwin.equals(stop))
        {
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        	Weightdelete(remwin);//删掉过去权重
        	Stack<Integer> id=Weight(addwin);//增加现在权重并返回新增加微博的id
        	try
        	{
	        	while(!id.empty())
	        	{
	        		for(Integer key:wm.keySet())
	        		{
	        			if(key==id.peek())
	        				continue;//自己和自己不计算距离
	        			if(sdf.parse(wm.get(id.peek())[0]).after(calda)){//
	        				double distance=Cosin.cal(wm.get(id.peek())[1], wm.get(key)[1]);
	        				if(distance>=0.5)
		        				System.out.println(distance);
	        				else
	        					continue;
	        				BufferedWriter writer=new BufferedWriter(new FileWriter("result/distance/"+String.valueOf(id.peek())+".txt",true));
	        				writer.write(String.valueOf(distance)+"\r\n");
	        				writer.close();
	        				if(sdf.parse(wm.get(key)[0]).after(calda)){
		        				BufferedWriter writer1=new BufferedWriter(new FileWriter("result/distance/"+String.valueOf(key)+".txt",true));
			        			writer1.write(String.valueOf(distance)+"\r\n");
			        			writer1.close();
		        			}
	        			}
	    			}
	        		id.pop();
	        	}
        	}catch(Exception e)
        	{}
        	cal.setTime(addwin); 
            cal.add(cal.DATE, 1);
            addwin=cal.getTime();
            cal.setTime(remwin); 
            cal.add(cal.DATE, 1);
            remwin=cal.getTime();
        }
	}	
	public static void main(String args[]) throws ParseException, IOException 
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance(); 
		WeightCal a=new WeightCal();
		//a.onlyword(WeightCal.segfile);
		a.onlyfile();
		//Date d=sdf.parse("2016-03-10");
		/*while(!d.equals(sdf.parse("2016-03-13"))){
			System.out.println(sdf.format(d));
			a.Weight(d);
			cal.setTime(d); 
	        cal.add(cal.DATE, 1); 
			d=cal.getTime();
			//System.out.println(a.wm);
		}
		a.writemap();*/
		//a.distance(d);
		
		
		/*while(!d.equals(sdf.parse("2014-05-09 12:25"))){
			Calendar cal = Calendar.getInstance(); 
			cal.setTime(d); 
	        cal.add(cal.MINUTE, 1); 
			a.Weight(d);
			d=cal.getTime();
		}
		a.writemap();
		a.Weightdelete(sdf.parse("2014-05-08 19:10"));*/
		//a.readmap();	
	}
}
