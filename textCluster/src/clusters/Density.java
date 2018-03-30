package clusters;
import similarity.*;
import java.io.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import preprocess.Parameters;
public class Density 
{
	public Map<Integer,String[]> cm=new HashMap<Integer,String[]>();
	public int clusterid;
	public Date addwin,deletewin;
	public StringBuffer result=new StringBuffer("");
	public Density()
	{
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			BufferedReader reader=new BufferedReader(new FileReader("result/addwinandclusterid.txt"));
			String temp=reader.readLine();
			reader.close();
			addwin=sdf.parse(temp.substring(0,10));
			deletewin=sdf.parse(temp.substring(10,20));
			clusterid=Integer.parseInt(temp.substring(temp.indexOf(",")+1));
			readcluster();
		}catch(Exception e)
		{
			System.out.println("density"+e);
		}
	}
	public void end()
	{
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			BufferedWriter bw=new BufferedWriter(new FileWriter("result/addwinandclusterid.txt"));			 
	        bw.write(sdf.format(addwin)+sdf.format(deletewin)+","+String.valueOf(clusterid));
	        bw.close();
	        writecluster();
	        writeresult();
		}catch(Exception e)
		{
			System.out.println("end"+e);
		}
	}
	public void writeresult()
	{
		try
		{
			BufferedWriter bw=new BufferedWriter(new FileWriter("result/result.txt",true));
			bw.write(result.toString());
			bw.close();
		}catch(Exception e)
		{
			System.out.println("rr"+e);
		}
	}
	public String readresult()
	{
		String rtemp="";
		try
		{
			BufferedReader bw=new BufferedReader(new FileReader("result/result.txt"));
			String temp;
			while((temp=bw.readLine())!=null)
			{
				rtemp+=temp+"\r\n";
			}
			bw.close();	
		}catch(Exception e)
		{
			System.out.println("rr"+e);
		}
		return rtemp;
	}
	public void writecluster()
	{
		try
		{
			BufferedWriter writer=new BufferedWriter(new FileWriter("result/cluster.txt"));
			for(Integer key:cm.keySet()){
				writer.write(key+":"+cm.get(key)[0]+"&"+cm.get(key)[1]+cm.get(key)[2]+"\r\n");
			}
			writer.close();
		}catch(Exception e)
		{
			System.out.println("writecluster"+e);
		}
	}
	public void readcluster()
	{
		try
		{
			BufferedReader reader=new BufferedReader(new FileReader("result/cluster.txt"));
			cm=new HashMap<Integer,String[]>();
			String temp;
			while((temp=reader.readLine())!=null)
			{
				String[] s={temp.substring(temp.indexOf(":")+1,temp.indexOf("&")),
						temp.substring(temp.indexOf("&")+1,temp.indexOf(",")),temp.substring(temp.indexOf(","))};
				cm.put(Integer.valueOf(temp.substring(0, temp.indexOf(":"))), s);
				//System.out.println(Integer.valueOf(temp.substring(0, temp.indexOf(":")))+" "+ temp.substring(temp.indexOf(":")+1));
			}
			reader.close();
		}catch(Exception e)
		{
			System.out.println("readmap"+e);
		}
	}
	public Stack<Integer> getNeps(String s)
	{
		Stack<Integer> id=new Stack<Integer>();
		while(!s.equals(","))
		{
			s=s.substring(1);
			id.push(Integer.valueOf(s.substring(0, s.indexOf(","))));
			s=s.substring(s.indexOf(","));
		}
		return id;
	}
	public int getCore(String s,int MinPts)
	{
		while(!s.equals(","))
		{
			s=s.substring(1);
			int idtemp=Integer.valueOf(s.substring(0, s.indexOf(",")));
			if(Integer.valueOf(cm.get(idtemp)[1])>=MinPts)
				return idtemp;
			s=s.substring(s.indexOf(","));
		}
		return -1;
	}
	public Stack<Integer> getAllCore(String s,int MinPts)
	{
		Stack<Integer> id=new Stack<Integer>();
		while(!s.equals(","))
		{
			s=s.substring(1);
			int idtemp=Integer.valueOf(s.substring(0, s.indexOf(",")));
			if(Integer.valueOf(cm.get(idtemp)[1])>=MinPts)
				id.push(idtemp);
			s=s.substring(s.indexOf(","));
		}
		return id;
	}
	public Stack<Integer> findsamecluster(Queue<Integer> newnoncore)
	{
		Stack<Integer> id=new Stack<Integer>();
		Queue<Integer> temp=new LinkedList<Integer>();
		temp.addAll(newnoncore);
		while(!newnoncore.isEmpty())
			newnoncore.poll();
		String cluster=cm.get(temp.peek())[0];
		for(int i=0;i<temp.size();i++)
		{
			if(cm.get(temp.peek())[0].equals(cluster))
				id.push(temp.peek());
			else
				newnoncore.offer(temp.peek());
			int tem=temp.poll();
			temp.offer(tem);
		}
		return id;
	}
	public void delete(int deleteid,int MinPts,String date)
	{
		Queue<Integer> newnoncore=new LinkedList<Integer>();
		if(Integer.parseInt(cm.get(deleteid)[1])>=MinPts)
		{
			newnoncore.offer(deleteid);
			cm.get(deleteid)[1]="0";
		}
		Stack<Integer> id=getNeps(cm.get(deleteid)[2]);
		while(!id.empty())
		{
			cm.get(id.peek())[1]=String.valueOf(Integer.parseInt(cm.get(id.peek())[1])-1);
			if(Integer.parseInt(cm.get(id.peek())[1])==MinPts-1)
				newnoncore.offer(id.peek());
			cm.get(id.peek())[2]=cm.get(id.peek())[2].replace(","+String.valueOf(deleteid)+",", ",");
			id.pop();
		}
		while(!newnoncore.isEmpty())
		{
			Queue<Integer> influid=new LinkedList<Integer>();
			Stack<Integer> influ=findsamecluster(newnoncore);//get same class newnoncore
			//System.out.println(newnoncore.size());
			String cluster=cm.get(influ.peek())[0];//cluster handling
			while(!influ.empty()){
				Stack<Integer> s;
				influid.offer(influ.peek());
				s=getNeps(cm.get(influ.pop())[2]);
				while(!s.empty()){
					if(!influid.contains(s.peek()))
						influid.offer(s.peek());//get all affected id
					s.pop();
				}
			}
			Queue<Integer> noncore=new LinkedList<Integer>();
			ArrayList<Integer> core=new ArrayList<Integer>();
			while(!influid.isEmpty()){//find core id and noncore id remain
				if(Integer.parseInt(cm.get(influid.peek())[1])<MinPts)
				{//
					//if(!noncore.contains(influid.peek()))
						noncore.offer(influid.peek());
				}
				else
				{
					//if(!core.contains(influid.peek()))
						core.add(influid.peek());
				}
				influid.poll();
			}
			if(core.isEmpty())
			{
				result.append(date+":"+cluster+"->\r\n");
				while(!noncore.isEmpty())
				{	
					int i;
					if(cm.get(noncore.peek())[0].equals(cluster)&&
							(i=getCore(cm.get(noncore.peek())[2], MinPts))!=-1)
						cm.get(noncore.peek())[0]=cm.get(i)[0];
					else if(cm.get(noncore.peek())[0].equals(cluster))
						cm.get(noncore.peek())[0]="noise";
					noncore.poll();
				}
			}
			else
			{
				while(!noncore.isEmpty())
				{	
					int i;
					if(cm.get(noncore.peek())[0].equals(cluster)&&
							(i=getCore(cm.get(noncore.peek())[2], MinPts))!=-1)
						cm.get(noncore.peek())[0]=cm.get(i)[0];
					else if(cm.get(noncore.peek())[0].equals(cluster))
						cm.get(noncore.peek())[0]="noise";
					noncore.poll();
				}
				while(core.size()>1)
				{
					int split=reachable(core,MinPts);
					if(split!=0)
					{
						splitcluster(split,MinPts);
						result.append(date+":"+cluster+"->"+cluster+"&cluster"+String.valueOf(clusterid)+"\r\n");
					}
				}
			}
		}		
		cm.remove(deleteid);
	}
	public void splitcluster(int splitid,int MinPts)
	{
		clusterid++;
		Stack<Integer> s;
		Queue<Integer> q=new LinkedList<Integer>();
		ArrayList<Integer> hasmapped=new ArrayList<Integer>();
		hasmapped.add(splitid);
		s=getNeps(cm.get(splitid)[2]);
		cm.get(splitid)[0]="cluster"+String.valueOf(clusterid);
		while(!s.isEmpty())
		{
			cm.get(s.peek())[0]="cluster"+String.valueOf(clusterid);
			if(Integer.parseInt(cm.get(s.peek())[1])<MinPts){
				s.pop();
				continue;
			}
			q.offer(s.pop());
		}
		while(!q.isEmpty())
		{
			hasmapped.add(q.peek());
			s=getNeps(cm.get(q.poll())[2]);
			while(!s.isEmpty()){
				if(q.contains(s.peek())||hasmapped.contains(s.peek()))
				{
					s.pop();
					continue;
				}
				cm.get(s.peek())[0]="cluster"+String.valueOf(clusterid);
				if(Integer.parseInt(cm.get(s.peek())[1])<MinPts)
				{
					s.pop();
					continue;
				}
				q.offer(s.pop());
			}
		}
	}	
	public int reachable(ArrayList<Integer> core,int MinPts)
	{
		Stack<Integer> s;
		Queue<Integer> q=new LinkedList<Integer>();
		int splitpoint=core.get(0);
		q.offer(core.get(0));
		core.remove(0);
		ArrayList<Integer> hasmapped=new ArrayList<Integer>();
		while(!q.isEmpty())
		{
			hasmapped.add(q.peek());
			s=getAllCore(cm.get(q.poll())[2], MinPts);
			while(!s.isEmpty())
			{
				if(core.contains(s.peek()))
					core.remove(s.peek());
				if(core.isEmpty())
					return 0;
				if(!q.contains(s.peek())&&!hasmapped.contains(s.peek()))
					q.offer(s.pop());
				else
					s.pop();
			}
		}
		return splitpoint;
	}
	public void add(Map<Integer,String[]> weightmap,int insertid,double Eps,int MinPts,String date)
	{
		String[] s={"noise","0",","};
		Queue<Integer> newcore=new LinkedList<Integer>();
		for(Integer key:cm.keySet())
		{
			if(Parameters.getSimilarcal().indexOf("”‡œ“æ‡¿Î")!=-1)
			{
				double distance=Cosin.cal(weightmap.get(key)[1], weightmap.get(insertid)[1]);
				if(distance<Eps)
					continue;
			}
			else
			{
				double distance=Euclid.cal(weightmap.get(key)[1], weightmap.get(insertid)[1]);
				if(distance>Eps)
					continue;
			}
			cm.get(key)[1]=String.valueOf(Integer.parseInt(cm.get(key)[1])+1);
			s[1]=String.valueOf(Integer.parseInt(s[1])+1);
			cm.get(key)[2]+=(String.valueOf(insertid)+",");
			s[2]+=(String.valueOf(key)+",");
			if(Integer.parseInt(cm.get(key)[1])>=MinPts&&(!cm.get(key)[0].equals("noise")))
				s[0]=cm.get(key)[0];
			if(Integer.parseInt(cm.get(key)[1])==MinPts)
				newcore.offer(key);			
		}
		cm.put(insertid, s);
		if(Integer.parseInt(s[1])>=MinPts)
			newcore.offer(insertid);		
		while(!newcore.isEmpty())
		{
			int travelid=findhascluster(newcore);
			if(travelid!=-1)
			{
				Stack<Integer> id=getNeps(cm.get(travelid)[2]);
				while(!id.empty())
				{
					if(cm.get(id.peek())[0].equals("noise"))
					{//absoption
						cm.get(id.peek())[0]=cm.get(travelid)[0];
					}
					else if((!cm.get(id.peek())[0].equals(cm.get(travelid)[0]))//merge
							&&(Integer.parseInt(cm.get(id.peek())[1])>=MinPts))
					{
						result.append(date+":"+cm.get(id.peek())[0]+"&"+cm.get(travelid)[0]
									+"->"+cm.get(travelid)[0]+"\r\n");
						mergecluster(cm.get(id.peek())[0],cm.get(travelid)[0]);
					}
					id.pop();
				}
			}
			else
			{//create
				createcluster(newcore.poll());
				result.append(date+":->cluster"+String.valueOf(clusterid)+"\r\n");
			}			
		}
	}	
	public void createcluster(int coreid)
	{
		Stack<Integer> id=getNeps(cm.get(coreid)[2]);
		id.push(coreid);
		clusterid++;
		while(!id.empty()){
			cm.get(id.pop())[0]="cluster"+String.valueOf(clusterid);
		}
	}
	public void mergecluster(String changedid,String changeid)
	{
		for(Integer key:cm.keySet())
		{
			if(cm.get(key)[0].equals(changedid))
				cm.get(key)[0]=changeid;
		}
	}
	public int findhascluster(Queue<Integer> newcore)
	{
		int si=newcore.size();
		for(int i=0;i<si;i++)
		{
			if(!cm.get(newcore.peek())[0].equals("noise"))
			{
				return newcore.poll();
			}
			int tem=newcore.poll();
			newcore.offer(tem);			
		}
		return -1;
	}
}
