package clusters;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.SQLException;
import java.util.Stack;
import similarity.Cosin;
public class ChoseMinPts 
{
	public static void randomten(File tgt) throws SQLException
	{
		int[] r=new int[10];
		for(int i=0;i<10;i++)
		{
			r[i]=1+(int)(Math.random()*56401);
			for(int j=0;j<i;j++)
			{
				if(r[i]==r[j])
				{
					i=i-1;
					break;
				}		
			}
		}
		for(int i=0;i<10;i++)
		{
			File f=new File("distance/"+String.valueOf(r[i])+".txt");
			if(f.exists())
				continue;
			caldistance(String.valueOf(r[i]), tgt, f);
		}
	}
	private static void caldistance(String id,File tgt,File save) 
	{
		//计算某对象与所有对象距离
		double distance=0;
		String tempstring="",tempid="";
		try
		{
			save.createNewFile();
			BufferedWriter writer=new BufferedWriter(new FileWriter(save,true));
			BufferedReader reader=new BufferedReader(new FileReader(tgt));
			while((tempstring=reader.readLine())!=null)
			{
				tempid=tempstring.substring(17);
				tempstring=tempid.substring(tempid.indexOf("$"));
				tempid=tempid.substring(0,tempid.indexOf("$"));
				if(!id.equals(tempid))
					continue;

				String tempstring1="";
				BufferedReader reader1=new BufferedReader(new FileReader(tgt));
				Cosin c=new Cosin();
				while((tempstring1=reader1.readLine())!=null)
				{
					String id1="";
					id1=tempstring1.substring(17);
					tempstring1=id1.substring(id1.indexOf("$"));
					id1=id1.substring(0,id1.indexOf("$"));
					if(id1.equals(id))
						continue;
					distance=Cosin.cal(tempstring, tempstring1);
					writer.write(String.valueOf(distance)+"\r\n");
					
				}
				reader1.close();
				break;
			}
			reader.close();
			writer.close();
			System.out.println(id);
		}
		catch(Exception e)
		{
			System.out.println("calNeps"+e);
		}
	}
	public static void main(String[] args) throws SQLException
	{
		File f=new File("weight325.txt");
		randomten(f);
	}
}
