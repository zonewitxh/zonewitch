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
class DSInterface extends JFrame implements ActionListener
{
	JFrame pi,ci;
	JPanel mainPanel,bkim,northPanel,ncpanel,cpanel,spanel;
	JButton opdata,nex,jump;
	int right;
	String type;
	JLabel userdict,writeseg;
	JTextField dictpath,writesegsource;
	JScrollPane jsp;
	JTextArea tt;
	public DSInterface()
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
    	writeseg=new JLabel("输入数据所在路径",JLabel.CENTER);
    	writesegsource=new JTextField(15);
    	userdict=new JLabel("输入用户词典路径",JLabel.CENTER);
    	dictpath=new JTextField(15);
    	opdata=new JButton("开始分词");
    	opdata.addActionListener(this);
    	ncpanel.add(new JLabel(""));
    	ncpanel.add(writeseg);
    	ncpanel.add(writesegsource);
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
    	nex=new JButton("下一步");
    	nex.setBounds(220, 500, 110, 30);
    	nex.addActionListener(this);
    	nex.setEnabled(false);
    	jump=new JButton("跳过");
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
			Thread t = new Thread() //线程处理，可以让JTextArea动态更新内容
			{
			@Override
				public void run() 
				{
							// TODO Auto-generated method stub
			                //这里写下你要做的事情
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
			if(!writesegsource.getText().toString().isEmpty())
				Parameters.setWritesegsource(writesegsource.getText().toString());
			if(!dictpath.getText().toString().isEmpty())
				Parameters.setDictsource(dictpath.getText().toString());
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
public class DSegInterface
{
	public static void main(String[] args){
		JFrame frame=new DSInterface();
    }
}