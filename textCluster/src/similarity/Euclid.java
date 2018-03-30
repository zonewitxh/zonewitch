package similarity;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import dbconnect.Connectdb;
import features.WeightCal;
public class Euclid 
{
	public Euclid() throws SQLException
	{
		Connectdb con=new Connectdb("select word from wordlist where n>=10");
		while(con.getRs().next())
		{		
			word.add(con.getRs().getString(1));
		}
		con.closedb();
	}
	public static Map<String,String> weightkeyvalue(String s1)
	{
		Map<String,String> map1=new HashMap<String,String>();
		String tempkey,tempvalue;
		while(s1.length()>1)
		{
			s1=s1.substring(1);
			tempkey=s1.substring(0, s1.indexOf("$"));
			s1=s1.substring(s1.indexOf("$")+1);
			tempvalue=s1.substring(0, s1.indexOf("$"));
			if(word.contains(tempkey))
				map1.put(tempkey, tempvalue);
			s1=s1.substring(s1.indexOf("$"));
		}
		return map1;
	}
	public static ArrayList word=new ArrayList();
	public static double cal(String s1,String s2)
	{
		Map<String,String> map1,map2;
		map1=Euclid.weightkeyvalue(s1);
		map2=Euclid.weightkeyvalue(s2);
		double d=0;
		int n=0;
		for(Map.Entry<String, String> entry:map1.entrySet())
		{
			double d1=Double.parseDouble(entry.getValue());
		    if(map2.containsKey(entry.getKey()))
		    {		    	 
				double d2=Double.parseDouble(map2.get(entry.getKey()));
				d+=Math.pow((d1-d2), 2);
				n++;
		    }
		    else
		    {
		    	d+=Math.pow(d1, 2);
		    	n++;
		    }
		}
		for(Map.Entry<String, String> entry:map2.entrySet())
		{
			if(!map1.containsKey(entry.getKey()))
			{
				double d1=Double.parseDouble(entry.getValue());
				d+=Math.pow(d1, 2);
				n++;
			}
		}
		d=Math.sqrt(d/n);
		System.out.println(d);
		return d;
	}
}
