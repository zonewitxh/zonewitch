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
	public static String a="导出数据  ";	
	public MyFrame()
	{
	    setTitle("短文本主题聚类与迁移分析平台");
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
    	db=new JLabel("数据库名称",JLabel.CENTER);
    	dbname=new JTextField(15); 
    	user=new JLabel("用    户    名",JLabel.CENTER);
    	usr=new JTextField(15);
    	password=new JLabel("密             码",JLabel.CENTER);
    	pswd=new JPasswordField(15);
    	id=new JLabel("主             键",JLabel.CENTER);
    	key=new JTextField(15); 
    	time=new JLabel("时             间",JLabel.CENTER);
    	tm=new JTextField(15);
    	text=new JLabel("文             本",JLabel.CENTER);
    	txt=new JTextField(15);
    	tb=new JLabel("表             名",JLabel.CENTER);
    	table=new JTextField(15);
    	opdata=new JButton("导出数据");
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
    	//tt.setText("#2014年商务工作主要任务#【推进“一带一路”建设，形成全方位开放新格局】①推进丝绸之路经济带建设；②推进21世纪海上丝绸之路建设；③扩大内陆沿边开放。\r\n"
    			//+ "【商务部布置明年8大任务】1.全面深化商务领域改革；2.积极参与国际经贸规则制定，以开放促改革促转型；3.完善内贸流通总体布局；4.整顿和规范市场秩序，建设法治化营商环境5.加快进出口结构调整；6.进一步优化投资环境；7.深化多双边经贸合作；8.推进“一带一路”建设。详戳长微博：\r\n");
    	jsp = new JScrollPane(tt);
    	//jsp.setViewportView(tt);
    	jsp.setAutoscrolls(true);
    	jsp.setBounds(10, 20, 670, 470);
    	jsp.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    	nex=new JButton("下一步");
    	nex.setBounds(220, 500, 110, 30);
    	nex.addActionListener(this);
    	nex.setEnabled(false);
    	jump=new JButton("跳过");
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
			Thread t = new Thread() //线程处理，可以让JTextArea动态更新内容
			{
			@Override
				public void run() 
				{
							// TODO Auto-generated method stub
			                //这里写下你要做的事情
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