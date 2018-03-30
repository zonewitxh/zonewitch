package main;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.sql.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.util.*;
import java.io.UnsupportedEncodingException;
import preprocess.*;
import features.*;
class MyFrame extends JFrame implements ActionListener
{
	JPanel mainPanel,bkim,northPanel,ncpanel,cpanel,spanel;
	JButton opdata,nex,jump;
	int right;
	String type;
	JLabel db,user,password,id,time,text,tb;
	JTextField dbname, usr,key,tm,txt,table;
	JPasswordField pswd;
	JScrollPane jsp;
	JTextArea tt;
	public static String a="��������  ";	
	public MyFrame()
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
    	db=new JLabel("���ݿ�����",JLabel.CENTER);
    	dbname=new JTextField(15); 
    	user=new JLabel("��    ��    ��",JLabel.CENTER);
    	usr=new JTextField(15);
    	password=new JLabel("��             ��",JLabel.CENTER);
    	pswd=new JPasswordField(15);
    	id=new JLabel("��             ��",JLabel.CENTER);
    	key=new JTextField(15); 
    	time=new JLabel("ʱ             ��",JLabel.CENTER);
    	tm=new JTextField(15);
    	text=new JLabel("��             ��",JLabel.CENTER);
    	txt=new JTextField(15);
    	tb=new JLabel("��             ��",JLabel.CENTER);
    	table=new JTextField(15);
    	opdata=new JButton("��������");
    	opdata.addActionListener(this);
    	ncpanel.add(db);
    	ncpanel.add(dbname);
    	ncpanel.add(user);
    	ncpanel.add(usr);
    	ncpanel.add(password);
    	ncpanel.add(pswd);
    	ncpanel.add(tb);
    	ncpanel.add(table);
    	ncpanel.add(id);
    	ncpanel.add(key);
    	ncpanel.add(time);
    	ncpanel.add(tm);
    	ncpanel.add(text);
    	ncpanel.add(txt);
    	ncpanel.add(new JLabel(""));
    	ncpanel.add(opdata);
    	northPanel.add(ncpanel,"Center");
    	mainPanel.add(northPanel,"North");
    	bkim=new JPanel();
    	bkim.setBorder(BorderFactory.createLineBorder(Color.black,2));
    	bkim.setLayout(null);
    	tt=new JTextArea();
    	tt.setEditable(true);
    	tt.setLineWrap(true);
    	tt.setWrapStyleWord(true);
    	//tt.setText("#2014����������Ҫ����#���ƽ���һ��һ·�����裬�γ�ȫ��λ�����¸�֡����ƽ�˿��֮·���ô����裻���ƽ�21���ͺ���˿��֮·���裻��������½�ر߿��š�\r\n"
    			//+ "�����񲿲�������8������1.ȫ�����������ĸ2.����������ʾ�ó�����ƶ����Կ��Ŵٸĸ��ת�ͣ�3.������ó��ͨ���岼�֣�4.���ٺ͹淶�г����򣬽��跨�λ�Ӫ�̻���5.�ӿ�����ڽṹ������6.��һ���Ż�Ͷ�ʻ�����7.���˫�߾�ó������8.�ƽ���һ��һ·�����衣�����΢����\r\n");
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
        jump.addActionListener(this);
    	jump.setBounds(360, 500, 110, 30);
    	bkim.add(jsp);
    	bkim.add(nex);
    	bkim.add(jump);
    	mainPanel.add(bkim,"Center");
}		
	public void actionPerformed(ActionEvent event)
	{
		if(event.getSource()==nex)
		{
			this.dispose();
			JFrame si = new SInterface();
		}
		if(event.getSource()==jump)
		{
			this.dispose();
			JFrame si = new DSInterface();
		}
		if(event.getSource()==opdata)
		{
			if(!dbname.getText().toString().isEmpty())
				Parameters.setDbname(dbname.getText().toString());
			if(!key.getText().toString().isEmpty())
				Parameters.setId(key.getText().toString());
			if(!pswd.getPassword().toString().isEmpty())
				Parameters.setPassword(pswd.getPassword().toString());
			if(!txt.getText().toString().isEmpty())
				Parameters.setText(txt.getText().toString());
			if(!tm.getText().toString().isEmpty())
				Parameters.setTime(tm.getText().toString());
			if(!usr.getText().toString().isEmpty())
				Parameters.setUsername(usr.getText().toString());
			if(!table.getText().toString().isEmpty())
				Parameters.setTable(table.getText().toString());
			jump.setEnabled(false);
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
						ins = new ICTCLASwords();
						ins.writetxt("select "+Parameters.text+","+Parameters.time+" ,"+Parameters.id+" from "+Parameters.table+ " order by "+Parameters.id,tt);
						jump.setEnabled(true);
						nex.setEnabled(true);
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
				}  
			};  
			t.start(); 					
		}
	}
} 
public class MyInterface{
	public static void main(String[] args){
		JFrame frame=new MyFrame();
    }
}