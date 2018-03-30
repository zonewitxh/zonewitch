package analyze;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JTextArea;
public class Topic 
{
	public Topic(JTextArea tt)
	{
		tt.setText("");
		try
		{
			HashMap<String,ArrayList<String>> d=new HashMap<String,ArrayList<String>>();
			ArrayList<String> a=new ArrayList<String>();
			HashMap<String,HashMap<String,Double>> g=new HashMap<String,HashMap<String,Double>>();
			BufferedReader reader=new BufferedReader(new FileReader("result/cluster.txt"));
			String temp;
			while((temp=reader.readLine())!=null)
			{
				if(d.containsKey(temp.substring(temp.indexOf(":")+1,temp.indexOf("&"))))
				{
					ArrayList<String> t=d.get(temp.substring(temp.indexOf(":")+1,temp.indexOf("&")));
					t.add(temp.substring(0,temp.indexOf(":")));
					d.put(temp.substring(temp.indexOf(":")+1,temp.indexOf("&")), t);
				}
				else
				{
					ArrayList<String> t=new ArrayList<String>();
					t.add(temp.substring(0,temp.indexOf(":")));
					d.put(temp.substring(temp.indexOf(":")+1,temp.indexOf("&")), t);
				}
			}
			reader.close();
			for(String k:d.keySet())
			{
				HashMap<String,Double> b=new HashMap<String,Double>();
				reader=new BufferedReader(new FileReader("result/weight.txt"));
				while((temp=reader.readLine())!=null){
					if(d.get(k).contains(temp.substring(0,temp.indexOf(":"))))
					{
						temp=temp.substring(temp.indexOf(","));
						while(temp.length()>1){
							temp=temp.substring(temp.indexOf(",")+1);
							if(b.containsKey(temp.substring(0,temp.indexOf(" "))))
							{
								String key=temp.substring(0,temp.indexOf(" "));
								temp=temp.substring(temp.indexOf(" ")+1);
								b.put(key, b.get(key)+Double.parseDouble(temp.substring(0,temp.indexOf(" "))));
								temp=temp.substring(temp.indexOf(","));
							}
							else
							{
								String key=temp.substring(0,temp.indexOf(" "));
								temp=temp.substring(temp.indexOf(" ")+1);
								b.put(key, Double.parseDouble(temp.substring(0,temp.indexOf(" "))));
								temp=temp.substring(temp.indexOf(","));
							}
						}
					}
				}
				reader.close();
				g.put(k, b);
			}
				
			HashMap<String,HashMap<String,Double>> c=new HashMap<String,HashMap<String,Double>>();
			for(String k:g.keySet())
			{
				HashMap<String,Double> h=new HashMap<String,Double>();
				for(String key:g.get(k).keySet())
				{
					if(g.get(k).get(key)!=0)
						h.put(key, g.get(k).get(key));
				}
				c.put(k, h);
			}
			BufferedWriter writer=new BufferedWriter(new FileWriter("result/topic.txt"));
			StringBuffer ts=new StringBuffer("");
			for(String k:c.keySet())
			{
				if(k.equals("noise"))
					continue;
				ts.append(k+" ");
				tt.append(k+" ");
        		tt.setCaretPosition(tt.getDocument().getLength());
				while(!c.get(k).isEmpty())
				{
					String key="";
					double value=0;
					for(String kk:c.get(k).keySet())
					{
						if(c.get(k).get(kk)>value){
							key=kk;
							value=c.get(k).get(kk);
						}
					}
					ts.append(key+":"+value+" ");
					tt.append(key+":"+value+" ");
	        		tt.setCaretPosition(tt.getDocument().getLength());
					c.get(k).remove(key);
				}
				ts.append("\r\n");
				tt.append("\r\n");
        		tt.setCaretPosition(tt.getDocument().getLength());
			}
			writer.write(ts.toString());	
			writer.close();
			if(tt.getLineCount()>500)
			{
				int tint = tt.getText().length()/2;
				//System.out.println(tint);
				String temps = tt.getText().substring(tint);
				//System.out.println(temp.length());
				tt.setText(temps);
				tt.setCaretPosition(tt.getDocument().getLength());
				tt.paintImmediately(tt.getBounds());
			}
		}catch(Exception e)
		{
			System.out.println("readmap"+e);
		}
	}	
}
