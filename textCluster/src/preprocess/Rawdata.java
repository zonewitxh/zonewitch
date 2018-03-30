package preprocess;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.*;
/**
 * @author �����
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
//���췽��
	public Rawdata() 
	{
		super();
		// TODO Auto-generated constructor stub
	}
	// ����΢������ʱ������ݿ��е���������Ψһid����
	public static void setid()
	{
		try
		{	
			int i=1;
			Class.forName("net.sourceforge.jtds.jdbc.Driver"); 
			//Ҫ��JVM���Ҳ�����java����sqlserver����
			Connection con=DriverManager.getConnection("jdbc:jtds:sqlserver://localhost:1433;DatabaseName="+Parameters.dbname,Parameters.username,Parameters.password);
			//�������ݿ�����
			Statement stmt=con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			ResultSet rs=stmt.executeQuery("select "+Parameters.id+" from newweibo order by "+Parameters.time);
			//���Ͳ�ѯ���
			while(rs.next()) 
			{	
				//���շ���ʱ��Ϊÿ��΢�����id
				System.out.println(rs.getInt(1));
				rs.updateInt(1, i);
				rs.updateRow();
				i++;
			}
			con.close();
			//�ر����ݿ�����
			
		}catch(Exception e){
			//�쳣����
		}
	}
	
	/**
	 * ��ȡ���ݿ��е�΢�����ϵ�id�ͷ���ʱ�䣬�����շ���ʱ�����򣬽����ϵ�id�Ͷ�Ӧʱ��д��txt
	 */
	public static void idandtime()
	{
		try
		{	
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			//Ҫ��JVM���Ҳ�����java����sqlserver����
			Connection con=DriverManager.getConnection("jdbc:jtds:sqlserver://localhost:1433;DatabaseName="+Parameters.dbname,Parameters.username,Parameters.password);
			//�������ݿ�����
			Statement stmt=con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			ResultSet rs=stmt.executeQuery("select "+Parameters.time+","+Parameters.id+" from newweibo order by "+Parameters.time);
			//���Ͳ�ѯ���
			BufferedWriter bw = new BufferedWriter(new FileWriter("result/idtime.txt"));
			//�����ļ������
			while(rs.next()) 
			{	
				//����ѯ΢������д��txt�ļ���
				bw.write(rs.getString(1)+"&"+rs.getInt(2)+"\r\n");	
			}
			bw.close();
			//�ر��ļ������
			con.close();
			//�ر����ݿ�����
			
		}catch(Exception e)
		{
			//�쳣����
			System.out.print(e);
		}
	}
	/**
	 * ��������ִ��setid()��idandtime()
	 */
	public static void main(String args[])
	{
		Rawdata.setid();
		Rawdata.idandtime();
	}	
}
