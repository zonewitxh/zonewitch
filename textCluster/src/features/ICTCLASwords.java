package features;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import preprocess.Parameters;
import ICTCLAS.I3S.AC.*;
import dbconnect.*;
public class ICTCLASwords 
{	
	Connectdb c; 
	//数据库连接
	ICTCLAS50 test;
	//分词模块
	String path;
	//路径
	/**
	 * 成员变量初始化
	 * @throws UnsupportedEncodingException
	 * 
	 */
	public ICTCLASwords() throws UnsupportedEncodingException
	{
		 test = new ICTCLAS50();
		 path=".";
		 if(test.ICTCLAS_Init(path.getBytes("GB2312"))==false)
			 System.out.println("Init Fail!");
		 test.ICTCLAS_SetPOSmap(2);
		 String usrdir = Parameters.dictsource; 
		 //建立用户词典
		 byte[] usrdirb = usrdir.getBytes(); 
		 test.ICTCLAS_ImportUserDictFile(usrdirb, 0);
	}
	/**
	 * 将数据库中的每个微博语料都写入一个txt文档中，并将文档命名为时间+id
	 * @param s 数据库连接字符串
	 * 
	 */
	public void writetxt(String s,JTextArea jta)
	{	
		File src=new File("result/source");
		File[] files = src.listFiles();
        for (File file : files) 
        {
        	file.delete();
        }
        //删除文件路径现有的文件
        String data="";
        String name="";
		try
		{
			//建立数据库连接，并将数据库中的每个微博语料都写入一个txt文档中，并将文档命名为时间+id
			c=new Connectdb(s);
			while(c.getRs().next())
			{
				data=c.getRs().getString(1);
				name=c.getRs().getString(2).replace(":", "a")+"&"+c.getRs().getString(3);
				FileWriter fw = new FileWriter("result/source/"+name+".txt");
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(data);
				//System.out.println(jta.getLineCount());//(name+" "+data+"\r\n");
				jta.append(name+" "+data+"\r\n");
				jta.setCaretPosition(jta.getDocument().getLength());
				//jta.paintImmediately(jta.getBounds());
				if(jta.getLineCount()>500)
				{
					int tint = jta.getText().length()/2;
					//System.out.println(tint);
					String temp = jta.getText().substring(tint);
					//System.out.println(temp.length());
					jta.setText(temp);
					jta.setCaretPosition(jta.getDocument().getLength());
					jta.paintImmediately(jta.getBounds());
				}
				bw.close();
				fw.close();	
			}
			c.closedb();
		}
		catch(Exception e)
		{
			//异常处理
			System.out.println("失败"+e+name+data);
		}
	}
	/**
	 * 对每个txt文本中的微博语料分词，并将结果储存在新的txt文档中，并以输入txt文档名命名
	 * @throws UnsupportedEncodingException 
	 */
	public void writeseg(JTextArea jta) 
	{
		File tgt=new File("result/target");
		File[] files = tgt.listFiles();
        for (File file : files) 
        {
            file.delete();
        }
        //删除目标文件路径现有的文件
        File src=new File(Parameters.writesegsource);
		File[] filesrc = src.listFiles();
		System.out.println(Parameters.writesegsource);
		for(int i=0;i<filesrc.length;i++)
		{
			String in=Parameters.writesegsource+"/"+filesrc[i].getName();
			String ou="result/target/"+filesrc[i].getName();
			jta.append("正在分词文件："+in+"\r\n");
			jta.setCaretPosition(jta.getDocument().getLength());
			//System.out.println(filesrc[i].getName());
			byte[] in1=in.getBytes(),ou1=ou.getBytes();
			test.ICTCLAS_FileProcess(in1,0, 1,ou1);
			//中科院分词系统对软件分词
			jta.append("已保存文件至："+ou+"\r\n");
			jta.setCaretPosition(jta.getDocument().getLength());
			if(jta.getLineCount()>500)
			{
				int tint = jta.getText().length()/2;
				//System.out.println(tint);
				String temp = jta.getText().substring(tint);
				//System.out.println(temp.length());
				jta.setText(temp);
				jta.setCaretPosition(jta.getDocument().getLength());
				jta.paintImmediately(jta.getBounds());
			}
		}			
	}
	public void exit()
	{
		test.ICTCLAS_Exit();
	}
	/**
	 * @param args
	 * @throws UnsupportedEncodingException
	 * 执行writetxt()和writeseg()
	 */
	/*public static void main(String args[]) throws UnsupportedEncodingException
	{
		ICTCLASwords ins=new ICTCLASwords();
		ins.writetxt("select 博文内容,发布时间 ,id from newweibo order by id");
		ins.writeseg();
		ins.exit();
		
	}*/
	public Connectdb getC() 
	{
		return c;
	}
	public void setC(Connectdb c) 
	{
		this.c = c;
	}
	public ICTCLAS50 getTest() 
	{
		return test;
	}
	public void setTest(ICTCLAS50 test) 
	{
		this.test = test;
	}
	public String getPath() 
	{
		return path;
	}
	public void setPath(String path) 
	{
		this.path = path;
	}
	//成员变量get与set函数
}
