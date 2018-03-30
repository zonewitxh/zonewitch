package dbconnect;
import java.sql.*;
import preprocess.*;
public class Connectdb 
{
	public Connection getCon() 
	{
		return con;
	}
	public void setCon(Connection con) 
	{
		this.con = con;
	}
	public Statement getStmt() 
	{
		return stmt;
	}
	public void setStmt(Statement stmt) 
	{
		this.stmt = stmt;
	}
	public ResultSet getRs() 
	{
		return rs;
	}
	public void setRs(ResultSet rs) 
	{
		this.rs = rs;
	}
	Connection con;
	Statement stmt;
	ResultSet rs;
	public Connectdb()
	{
		try
		{
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			con=DriverManager.getConnection("jdbc:jtds:sqlserver://localhost:1433;DatabaseName="+Parameters.dbname,Parameters.username,Parameters.password);
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
		}
		catch(Exception e)
		{
			System.out.println("Ê§°Ü"+e);
		}
	}
	public Connectdb(String s)
	{
		try
		{
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			con=DriverManager.getConnection("jdbc:jtds:sqlserver://localhost:1433;DatabaseName="+Parameters.dbname,Parameters.username,Parameters.password);
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			rs=stmt.executeQuery(s);
		}
		catch(Exception e)
		{
			System.out.println("Ê§°Ü"+e);
			System.out.println("Ê§°Ü"+e.getMessage());
		}
	}
	public void Connectdb2(String s)
	{
		try
		{
			rs=stmt.executeQuery(s);
		}
		catch(Exception e)
		{
			System.out.println("Ê§°Ü"+e);
		}
	}	
	 public void closedb()
	 {
		try
		{
			rs.close();
			stmt.close();
			con.close();
		}
		catch(Exception e)
		{
			System.out.println("Ê§°Ü"+e);
		}
	} 
	 /*public static void main(String args[]) throws Exception{
		 Connectdb a=new Connectdb("select * from cluster where cluster!='noise' and clusternum is null");
		 Connectdb b=new Connectdb();
		 while(a.getRs().next()){
			 if(a.getRs().getString("cluster").equals("noise"))
				 continue;
			 b.Connectdb2("select count(*) from cluster "
			 		+ "where cluster='"+a.getRs().getString("cluster")+
			 		"' and [time]='"+a.getRs().getString("time")+"'");
			 b.getRs().next();
			 a.getRs().updateFloat("clusternum",b.getRs().getFloat(1));
			 b.Connectdb2("select count(*) from cluster "
				 		+ "where [time]='"+a.getRs().getString("time")+"'");
			 b.getRs().next();
			 a.getRs().updateFloat("windownum",b.getRs().getFloat(1));
			 a.getRs().updateRow();
		 }
		 a.closedb();b.closedb();
	 }*/
	 
}
