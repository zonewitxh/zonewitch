package main;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.text.ParseException;
import java.util.*;
import java.sql.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.util.*;
import java.io.UnsupportedEncodingException;
import preprocess.*;
import features.*;
class SInterface extends JFrame implements ActionListener
{
	JFrame pi,ci;
	JPanel mainPanel,bkim,northPanel,ncpanel,cpanel,spanel;
	JButton opdata,nex,jump;
	int right;
	String type;
	JLabel userdict;
	JTextField dictpath;
	JScrollPane jsp;
	JTextArea tt;
	public SInterface()
	{
	    setTitle("���ı����������Ǩ�Ʒ���ƽ̨");
	    setSize(700, 700);
	    setLocation(150,0);
	    setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		PanelInit();
		setVisible(true);
	}
	public void PanelInit()
	{
		mainPanel = (JPanel)this.getContentPane();
    	mainPanel.setLayout(new BorderLayout());
    	northPanel=new JPanel();
    	northPanel.setBorder(BorderFactory.createLineBorder(Color.black,2));
    	ncpanel=new JPanel();
    	ncpanel.setLayout(new GridLayout(4,4));
    	userdict=new JLabel("�����û��ʵ�·��",JLabel.CENTER);
    	dictpath=new JTextField(15);
    	opdata=new JButton("��ʼ�ִ�");
    	opdata.addActionListener(this);
    	ncpanel.add(new JLabel(""));
    	ncpanel.add(new JLabel(""));
    	ncpanel.add(new JLabel(""));
    	ncpanel.add(new JLabel(""));
    	ncpanel.add(new JLabel(""));
    	ncpanel.add(userdict);
    	ncpanel.add(dictpath);
    	ncpanel.add(new JLabel(""));
    	ncpanel.add(new JLabel(""));
    	ncpanel.add(new JLabel(""));
    	ncpanel.add(new JLabel(""));
    	ncpanel.add(new JLabel(""));
    	ncpanel.add(new JLabel(""));
    	ncpanel.add(new JLabel(""));
    	ncpanel.add(opdata);
    	ncpanel.add(new JLabel(""));
    	northPanel.add(ncpanel,"Center");
    	mainPanel.add(northPanel,"North");
    	bkim=new JPanel();
    	bkim.setBorder(BorderFactory.createLineBorder(Color.black,2));
    	bkim.setLayout(null);
    	tt=new JTextArea();
    	tt.setEditable(true);
    	tt.setLineWrap(true);
    	tt.setWrapStyleWord(true);
    	jsp = new JScrollPane(tt);
    	//jsp.setViewportView(tt);
    	jsp.setAutoscrolls(true);
    	jsp.setBounds(10, 20, 670, 470);
    	jsp.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    	nex=new JButton("��һ��");
    	nex.setBounds(220, 500, 110, 30);
    	nex.addActionListener(this);
    	nex.setEnabled(false);
    	jump=new JButton("����");
    	jump.setBounds(360, 500, 110, 30);
    	bkim.add(jsp);
    	bkim.add(nex);
    	//bkim.add(jump);
    	mainPanel.add(bkim,"Center");
}		
	public void actionPerformed(ActionEvent event)
	{
		if(event.getSource()==nex)
		{
			this.dispose();
			pi = new ProInterface();
			Thread t = new Thread() //�̴߳���������JTextArea��̬��������
			{
			@Override
				public void run() 
				{
							// TODO Auto-generated method stub
			                //����д����Ҫ��������
					ICTCLASwords ins;	
					WeightCal a=new WeightCal();
					a.onlyword(WeightCal.segfile);
					a.onlyfile();
					pi.dispose();
					try 
					{
						ci=new CInterface();
					} catch (ParseException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}  
			};  
			t.start(); 		
		}
		if(event.getSource()==opdata)
		{
			opdata.setEnabled(false);
			if(!dictpath.getText().toString().isEmpty())
				Parameters.setDictsource(dictpath.getText().toString());
			Thread t = new Thread() //�̴߳���������JTextArea��̬��������
			{
			@Override
				public void run() 
				{
							// TODO Auto-generated method stub
			                //����д����Ҫ��������
					ICTCLASwords ins;	
					try 
					{
						ins=new ICTCLASwords();
						ins.writeseg(tt);
						ins.exit();
						nex.setEnabled(true);
					} catch (UnsupportedEncodingException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}  
			};  
			t.start(); 			
		}
	}	
}
class ProInterface extends JFrame 
{
	JPanel mp,userPanel;
	JLabel ul;
	public  ProInterface()
	{
		this.setTitle("���ı����������Ǩ�Ʒ���ƽ̨");
		this.setSize(250,160); 
		this.setLocation(350,300);
		this.setResizable(false);
		this.PanelInit();	
		this.setVisible(true);
	} 
	public void PanelInit()
	{
		mp = (JPanel)this.getContentPane();
		mp.setLayout(new GridLayout(3,1));  
		ul=new JLabel("���ڴ����ļ������Ժ󡭡� ",JLabel.CENTER);
		mp.add(new JLabel(""));
		mp.add(ul);
	}
}
public class SegInterface
{
	public static void main(String[] args)
	{
		JFrame frame=new SInterface();
    }
}