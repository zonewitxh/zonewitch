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
	//���ݿ�����
	ICTCLAS50 test;
	//�ִ�ģ��
	String path;
	//·��
	/**
	 * ��Ա������ʼ��
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
		 //�����û��ʵ�
		 byte[] usrdirb = usrdir.getBytes(); 
		 test.ICTCLAS_ImportUserDictFile(usrdirb, 0);
	}
	/**
	 * �����ݿ��е�ÿ��΢�����϶�д��һ��txt�ĵ��У������ĵ�����Ϊʱ��+id
	 * @param s ���ݿ������ַ���
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
        //ɾ���ļ�·�����е��ļ�
        String data="";
        String name="";
		try
		{
			//�������ݿ����ӣ��������ݿ��е�ÿ��΢�����϶�д��һ��txt�ĵ��У������ĵ�����Ϊʱ��+id
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
			//�쳣����
			System.out.println("ʧ��"+e+name+data);
		}
	}
	/**
	 * ��ÿ��txt�ı��е�΢�����Ϸִʣ���������������µ�txt�ĵ��У���������txt�ĵ�������
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
        //ɾ��Ŀ���ļ�·�����е��ļ�
        File src=new File(Parameters.writesegsource);
		File[] filesrc = src.listFiles();
		System.out.println(Parameters.writesegsource);
		for(int i=0;i<filesrc.length;i++)
		{
			String in=Parameters.writesegsource+"/"+filesrc[i].getName();
			String ou="result/target/"+filesrc[i].getName();
			jta.append("���ڷִ��ļ���"+in+"\r\n");
			jta.setCaretPosition(jta.getDocument().getLength());
			//System.out.println(filesrc[i].getName());
			byte[] in1=in.getBytes(),ou1=ou.getBytes();
			test.ICTCLAS_FileProcess(in1,0, 1,ou1);
			//�п�Ժ�ִ�ϵͳ������ִ�
			jta.append("�ѱ����ļ�����"+ou+"\r\n");
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
	 * ִ��writetxt()��writeseg()
	 */
	/*public static void main(String args[]) throws UnsupportedEncodingException
	{
		ICTCLASwords ins=new ICTCLASwords();
		ins.writetxt("select ��������,����ʱ�� ,id from newweibo order by id");
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
	//��Ա����get��set����
}
