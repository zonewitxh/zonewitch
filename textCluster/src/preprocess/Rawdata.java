package preprocess;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.*;
/**
 * @author 张炜婷
 * @version 1.0
 * @Time 2016-01-20
 * 
 */
public class Rawdata 
{
	public static String dbname;
	public static String username;
	public static String password;
	public static String time;
	public static String id;
	public static String getDbname() 
	{
		return dbname;
	}
	public static void setDbname(String dbname) 
	{
		Rawdata.dbname = dbname;
	}
	public static String getUsername() 
	{
		return username;
	}
	public static void setUsername(String username) 
	{
		Rawdata.username = username;
	}
	public static String getPassword() 
	{
		return password;
	}
	public static void setPassword(String password) 
	{
		Rawdata.password = password;
	}
	public static String getTime() 
	{
		return time;
	}
	public static void setTime(String time) 
	{
		Rawdata.time = time;
	}
	public static String getId() 
	{
		return id;
	}
	public static void setId(String id) 
	{
		Rawdata.id = id;
	}	
//构造方法
	public Rawdata() 
	{
		super();
		// TODO Auto-generated constructor stub
	}
	// 根据微博发布时间给数据库中的语料设置唯一id索引
	public static void setid()
	{
		try
		{	
			int i=1;
			Class.forName("net.sourceforge.jtds.jdbc.Driver"); 
			//要求JVM查找并加载java连接sqlserver驱动
			Connection con=DriverManager.getConnection("jdbc:jtds:sqlserver://localhost:1433;DatabaseName="+Parameters.dbname,Parameters.username,Parameters.password);
			//建立数据库连接
			Statement stmt=con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			ResultSet rs=stmt.executeQuery("select "+Parameters.id+" from newweibo order by "+Parameters.time);
			//发送查询语句
			while(rs.next()) 
			{	
				//按照发表时间为每个微博添加id
				System.out.println(rs.getInt(1));
				rs.updateInt(1, i);
				rs.updateRow();
				i++;
			}
			con.close();
			//关闭数据库连接
			
		}catch(Exception e){
			//异常处理
		}
	}
	
	/**
	 * 读取数据库中的微博语料的id和发布时间，并按照发布时间排序，将语料的id和对应时间写入txt
	 */
	public static void idandtime()
	{
		try
		{	
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			//要求JVM查找并加载java连接sqlserver驱动
			Connection con=DriverManager.getConnection("jdbc:jtds:sqlserver://localhost:1433;DatabaseName="+Parameters.dbname,Parameters.username,Parameters.password);
			//建立数据库连接
			Statement stmt=con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			ResultSet rs=stmt.executeQuery("select "+Parameters.time+","+Parameters.id+" from newweibo order by "+Parameters.time);
			//发送查询语句
			BufferedWriter bw = new BufferedWriter(new FileWriter("result/idtime.txt"));
			//建立文件输出流
			while(rs.next()) 
			{	
				//将查询微博语料写入txt文件中
				bw.write(rs.getString(1)+"&"+rs.getInt(2)+"\r\n");	
			}
			bw.close();
			//关闭文件输出流
			con.close();
			//关闭数据库连接
			
		}catch(Exception e)
		{
			//异常处理
			System.out.print(e);
		}
	}
	/**
	 * 主函数，执行setid()与idandtime()
	 */
	public static void main(String args[])
	{
		Rawdata.setid();
		Rawdata.idandtime();
	}	
}
