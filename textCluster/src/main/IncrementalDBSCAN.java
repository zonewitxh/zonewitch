package main;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Stack;
import javax.swing.JTextArea;
import clusters.Density;
import features.WeightCal;
class IncrementalDBSCAN 
{
	public void fileclean(String start,int interval,int clusterid) throws IOException, ParseException
	{
		BufferedWriter bw=new BufferedWriter(new FileWriter("result/weight.txt"));
		bw.write("");
		bw.close();
		bw=new BufferedWriter(new FileWriter("result/result.txt"));
		bw.write("");
		bw.close();
		bw=new BufferedWriter(new FileWriter("result/cluster.txt"));
		bw.write("");
		bw.close();
		bw=new BufferedWriter(new FileWriter("result/addwinandclusterid.txt"));
		String temp = start;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		 
		Date d = sdf.parse(start+" 00:00:00");
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(cal.MONTH, interval);
		temp +=sdf.format(cal.getTime()).substring(0,10)+","+String.valueOf(clusterid);
		bw.write(temp);
		bw.close();
	}
	public void clusterperiod(String stop,double Eps,int MinPts,JTextArea tt,JTextArea tt1,JTextArea tt2)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Density densityinstance=new Density();
		WeightCal weightinstance=new WeightCal();
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(densityinstance.addwin); //起始时间
        //cal.add(cal.DATE, daynum); 
        //Date stopadd=cal.getTime();
		Date stopadd;//结束时间
		try
		{
			stopadd=sdf.parse(stop);
			//System.out.println(stop);
		}catch(Exception e)
		{
			System.out.println(e);
			return;
		}
        Stack<Integer> id;
        while(!densityinstance.addwin.equals(stopadd))
        {
       	//System.out.println(densityinstance.addwin);
        	id=weightinstance.Weightdelete(densityinstance.deletewin);
        	while(!id.isEmpty())
        	{
       		//System.out.println(id.size());
        		densityinstance.delete(id.pop(), MinPts,sdf.format(densityinstance.addwin));//代码需要调试
       	}
       	id=weightinstance.Weight(densityinstance.addwin);
        	while(!id.isEmpty())
        	{
        		densityinstance.add(weightinstance.wm, id.peek(), Eps, MinPts,sdf.format(densityinstance.addwin));
        		tt.append(String.valueOf(id.peek())+":"+weightinstance.wm.get(id.peek())[0]+weightinstance.wm.get(id.peek())[1]+"\r\n");
        		tt.setCaretPosition(tt.getDocument().getLength());
				if(tt.getLineCount()>500)
				{
					int tint = tt.getText().length()/2;
					//System.out.println(tint);
					String temp = tt.getText().substring(tint);
					//System.out.println(temp.length());
					tt.setText(temp);
					tt.setCaretPosition(tt.getDocument().getLength());
					tt.paintImmediately(tt.getBounds());
				}
				tt1.append(id.peek()+":"+densityinstance.cm.get(id.peek())[0]+"&"+densityinstance.cm.get(id.peek())[1]+densityinstance.cm.get(id.peek())[2]+"\r\n");
        		tt1.setCaretPosition(tt1.getDocument().getLength());
				if(tt1.getLineCount()>500)
				{
					int tint = tt1.getText().length()/2;
					//System.out.println(tint);
					String temp = tt1.getText().substring(tint);
					//System.out.println(temp.length());
					tt1.setText(temp);
					tt1.setCaretPosition(tt1.getDocument().getLength());
					tt1.paintImmediately(tt1.getBounds());
				}
				tt2.setText(densityinstance.readresult()+densityinstance.result.toString());
				tt2.setCaretPosition(tt2.getDocument().getLength());
				if(tt1.getLineCount()>500)
				{
					int tint = tt2.getText().length()/2;
					//System.out.println(tint);
					String temp = tt2.getText().substring(tint);
					//System.out.println(temp.length());
					tt2.setText(temp);
					tt2.setCaretPosition(tt2.getDocument().getLength());
					tt2.paintImmediately(tt2.getBounds());
				}
        		id.pop();
        	}
        	cal.setTime(densityinstance.addwin); 
            cal.add(cal.DATE, 1);
            densityinstance.addwin=cal.getTime();
            cal.setTime(densityinstance.deletewin); 
            cal.add(cal.DATE, 1);
            densityinstance.deletewin=cal.getTime();
        }
        for(Integer key:densityinstance.cm.keySet())
        {//输出core
	        if(densityinstance.cm.get(key)[0].equals("noise")&&
					Integer.parseInt(densityinstance.cm.get(key)[1])>=MinPts)
				System.out.println(key);
        }
        densityinstance.end();
        weightinstance.end();
	}
	/*public static void main(String args[]){
		IncrementalDBSCAN i=new IncrementalDBSCAN();
		i.clusterperiod("2014-01-10",0.75,6);
	}*/
}
