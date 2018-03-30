package preprocess;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import clusters.Density;
import features.WeightCal;
/** 
 * @author 张炜婷 E-mail: 
 * @version 1.0
 * @time 创建时间：2018年3月24日 上午10:56:57 
 * 全部重要参数
 */
public class Parameters 
{
	public static String dbname="weibo";
	public static String username="zonewitch";
	public static String password="zonewitch";
	public static String time="发布时间";
	public static String id="id";
	public static String text="博文内容";
	public static String table="newweibo";
	public static String writesegsource="result/source";
	public static String dictsource="userdict.txt";
	//public static String datefrom="9999-99-99";
	//public static String dateto="0000-00-00";
	public static String datefrom="2013-12-27";
	public static String dateto="2015-10-27";
	public static String datenow="0000-00-00";
	public static String similarcal="余弦距离";
	public static Date getdn() throws ParseException 
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		 
		return sdf.parse(datenow+" 00:00:00");
	}
	public static Date getdf() throws ParseException 
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		 
		return sdf.parse(datefrom+" 00:00:00");
	}
	public static Date getdt() throws ParseException 
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.parse(dateto+" 00:00:00");
	}
	public static String getSimilarcal() 
	{
		return similarcal;
	}
	public static void setSimilarcal(String similarcal) 
	{
		Parameters.similarcal = similarcal;
	}
	public static String getDatefrom() 
	{
		return datefrom;
	}
	public static void setDatefrom(String datefrom) 
	{
		Parameters.datefrom = datefrom;
	}
	public static String getDateto() 
	{
		return dateto;
	}
	public static void setDateto(String dateto) 
	{
		Parameters.dateto = dateto;
	}
	public static String getDatenow() 
	{
		return datenow;
	}
	public static void setDatenow(String datenow) 
	{
		Parameters.datenow = datenow;
	}
	public static String getDictsource() 
	{
		return dictsource;
	}
	public static void setDictsource(String dictsource) 
	{
		Parameters.dictsource = dictsource;
	}
	public static String getWritesegsource() 
	{
		return writesegsource;
	}
	public static void setWritesegsource(String writesegsource) 
	{
		Parameters.writesegsource = writesegsource;
	}
	public static String getTable() 
	{
		return table;
	}
	public static void setTable(String table) 
	{
		Parameters.table = table;
	}
	public static String getDbname() 
	{
		return dbname;
	}
	public static String getText() 
	{
		return text;
	}
	public static void setText(String text) 
	{
		Parameters.text = text;
	}
	public static void setDbname(String dbname) 
	{
		Parameters.dbname = dbname;
	}
	public static String getUsername() 
	{
		return username;
	}
	public static void setUsername(String username) 
	{
		Parameters.username = username;
	}
	public static String getPassword() 
	{
		return password;
	}
	public static void setPassword(String password) 
	{
		Parameters.password = password;
	}
	public static String getTime() 
	{
		return time;
	}
	public static void setTime(String time) 
	{
		Parameters.time = time;
	}
	public static String getId() 
	{
		return id;
	}
	public static void setId(String id) 
	{
		Parameters.id = id;
	}
}
