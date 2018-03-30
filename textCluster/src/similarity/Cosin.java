package similarity;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import dbconnect.Connectdb;
public class Cosin 
{
	public static Map<String,String> weightkeyvalue(String s1)
	{
		Map<String,String> map1=new HashMap<String,String>();
		String tempkey,tempvalue;
		while(s1.length()>1)
		{
			tempkey=s1.substring(1, s1.indexOf(" "));
			s1=s1.substring(s1.indexOf(" ")+1);
			tempvalue=s1.substring(0, s1.indexOf(" "));
			map1.put(tempkey, tempvalue);
			s1=s1.substring(s1.indexOf(","));
		}
		return map1;
	}
	public static ArrayList word=new ArrayList();
	public static double cal(String s1,String s2)
	{
		Map<String,String> map1,map2;
		map1=weightkeyvalue(s1);
		map2=weightkeyvalue(s2);
		double xy=0,x=0,y=0,d=0;
		for(Map.Entry<String, String> entry:map1.entrySet())
		{
			double d1=Double.parseDouble(entry.getValue());
			x+=Math.pow(d1, 2);
		    if(map2.containsKey(entry.getKey()))
		    {
		    	double d2=Double.parseDouble(map2.get(entry.getKey()));
		    	//System.out.print(entry.getKey()+d2);
		    	y+=Math.pow(d2, 2);
				xy+=d1*d2;
		    }
		}
		//System.out.println("");
		for(Map.Entry<String, String> entry:map2.entrySet())
		{
			if(!map1.containsKey(entry.getKey()))
			{
				double d2=Double.parseDouble(entry.getValue());
				y+=Math.pow(d2, 2);
			}
		}
		x=Math.sqrt(x);
		y=Math.sqrt(y);
		if(x==0||y==0)
			return d;
		d=xy/(x*y);
		return d;
	}
}
