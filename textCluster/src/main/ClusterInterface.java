package main;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.sql.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.util.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import preprocess.*;
import features.*;
import analyze.Topic;
class CInterface extends JFrame implements ActionListener
{
	JComboBox similar,timewindow,interval;
	DateField datef,datet;
	JPanel mainPanel,bkim,northPanel,ncpanel,cpanel,spanel;
	JButton wecal,clus,drif,topi,start,restart;
	int y,m,d,ye,me,de;
	String type;
	JLabel from,to,simi,timew,interv,ep,minp;
	JScrollPane jsp;
	JTextArea tt,tt1,tt2,tt3;
	WeightCal a;
	JTextField eps,minpts;
	int right=0;
	IncrementalDBSCAN in;
	Topic too;
	public CInterface() throws ParseException
	{
	    setTitle("短文本主题聚类与迁移分析平台");
	    setSize(700, 700);
	    setLocation(150,0);
	    setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		PanelInit();
		setVisible(true);
	}
	public void PanelInit() throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		Date da=sdf.parse(Parameters.datefrom);
		cal.setTime(da);
		y = cal.get(Calendar.YEAR);
		m = cal.get(Calendar.MONTH)+1;
		d = cal.get(Calendar.DATE);
		da=sdf.parse(Parameters.dateto);
		cal.setTime(da);
		ye = cal.get(Calendar.YEAR);
		me = cal.get(Calendar.MONTH)+1;
		de = cal.get(Calendar.DATE);
		mainPanel = (JPanel)this.getContentPane();
    	mainPanel.setLayout(new BorderLayout());
    	northPanel=new JPanel();
    	northPanel.setBorder(BorderFactory.createLineBorder(Color.black,2));
    	ncpanel=new JPanel();
    	ncpanel.setLayout(new GridLayout(5,4));
    	from=new JLabel("起 始 时 间",JLabel.CENTER);
    	to=new JLabel("结 束 时 间",JLabel.CENTER);
    	simi=new JLabel("相似度方法",JLabel.CENTER);
    	timew=new JLabel("时 间 窗 口",JLabel.CENTER);
    	interv=new JLabel("时 间 间 隔",JLabel.CENTER);
    	ep=new JLabel("Eps",JLabel.CENTER);
    	eps=new JTextField(5); 
    	minp=new JLabel("MinPts",JLabel.CENTER);
    	minpts=new JTextField(5); 
    	wecal=new JButton("权重计算");
    	wecal.addActionListener(this);
    	wecal.setEnabled(false);
    	clus=new JButton("聚类分析");
    	clus.addActionListener(this);
    	clus.setEnabled(false);
    	drif=new JButton("迁移分析");
    	drif.addActionListener(this);
    	drif.setEnabled(false);
    	topi=new JButton("主题分析");
    	topi.addActionListener(this);
    	topi.setEnabled(false);
    	start=new JButton("开始");
    	start.addActionListener(this);
    	restart=new JButton("重新开始");
    	restart.setEnabled(false);
    	restart.addActionListener(this);
    	restart.setBounds(360, 472, 110, 30);
    	datef = new DateField(y,m,d,ye,me,de);
    	datet = new DateField(y,m,d,ye,me,de);
    	similar = new JComboBox();
    	similar.addItem("           余弦距离          ");
    	similar.addItem("           欧式距离          ");
    	timewindow = new JComboBox();
    	timewindow.addItem("           3个月          ");
    	timewindow.addItem("           4个月          ");
    	timewindow.addItem("           5个月          ");
    	timewindow.addItem("           6个月          ");
    	timewindow.setSelectedItem("           3个月          ");
    	interval = new JComboBox();
    	interval.addItem("           1个月          ");
    	interval.addItem("           2个月          ");
    	interval.addItem("           3个月          ");
    	interval.setSelectedItem("           1个月          ");
    	ncpanel.add(from);
    	ncpanel.add(datef);
    	ncpanel.add(to);
    	ncpanel.add(datet);
    	ncpanel.add(timew);
    	ncpanel.add(timewindow);
    	ncpanel.add(interv);
    	ncpanel.add(interval);
    	ncpanel.add(ep);
    	ncpanel.add(eps);
    	ncpanel.add(minp);
    	ncpanel.add(minpts);
    	ncpanel.add(simi);
    	ncpanel.add(similar);
    	ncpanel.add(new JLabel(""));
    	ncpanel.add(start);
    	ncpanel.add(wecal);
    	ncpanel.add(clus);
    	ncpanel.add(drif);
    	ncpanel.add(topi);
    	northPanel.add(ncpanel,"Center");
    	mainPanel.add(northPanel,"North");
    	bkim=new JPanel();
    	bkim.setBorder(BorderFactory.createLineBorder(Color.black,2));
    	bkim.setLayout(null);
    	tt=new JTextArea();
    	tt.setEditable(false);
    	tt.setLineWrap(true);
    	tt.setWrapStyleWord(true);
    	tt1=new JTextArea();
    	tt1.setEditable(false);
    	tt1.setLineWrap(true);
    	tt1.setWrapStyleWord(true);
    	tt2=new JTextArea();
    	tt2.setEditable(false);
    	tt2.setLineWrap(true);
    	tt2.setWrapStyleWord(true);
    	tt3=new JTextArea();
    	tt3.setEditable(false);
    	tt3.setLineWrap(true);
    	tt3.setWrapStyleWord(true);
    	jsp = new JScrollPane();
    	jsp.setViewportView(tt);
    	jsp.setAutoscrolls(true);
    	jsp.setBounds(10, 20, 670, 442);
    	jsp.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    	bkim.add(jsp);
    	bkim.add(restart);
    	mainPanel.add(bkim,"Center");
	}
	public void actionPerformed(ActionEvent event)
	{
		if(event.getSource()==wecal)
		{
			jsp.setViewportView(tt);
		}
		if(event.getSource()==clus)
		{
			jsp.setViewportView(tt1);
		}
		if(event.getSource()==drif)
		{
			jsp.setViewportView(tt2);
		}
		if(event.getSource()==topi)
		{
			jsp.setViewportView(tt3);	
		}
		if(event.getSource()==restart)
		{
			right=0;
			datef.getButton().setEnabled(true);
			datet.getButton().setEnabled(true);
			timewindow.setEnabled(true);
			interval.setEnabled(true);
			similar.setEnabled(true);
			eps.setEnabled(true);
			minpts.setEnabled(true);
			start.setText("开始");
			start.setEnabled(true);
			wecal.setEnabled(false);
        	clus.setEnabled(false);
        	drif.setEnabled(false);
        	topi.setEnabled(false);
        	tt.setText("");
        	tt1.setText("");
        	tt2.setText("");
        	tt3.setText("");
		}
		if(event.getSource()==start)
		{
			Thread t = new Thread() //线程处理，可以让JTextArea动态更新内容
			{
			@Override
				public void run() 
				{
							// TODO Auto-generated method stub
			                //这里写下你要做的事情
				Calendar ca = Calendar.getInstance();
	            ca.set(datef.getYear(), datef.getMonth()-1, datef.getDateOfWeek(),0,0,0);
	            Date fr = ca.getTime();
	            ca.set(datet.getYear(), datet.getMonth()-1, datet.getDateOfWeek(),0,0,0);
	            Date ft = ca.getTime();
	            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	            int window = Integer.valueOf(timewindow.getSelectedItem().toString().replace(" ", "").substring(0,1));
	            int inter = Integer.valueOf(interval.getSelectedItem().toString().replace(" ", "").substring(0,1));
				if(right==0)
				{	
		            try {
		            	wecal.setEnabled(true);
		            	clus.setEnabled(true);
		            	drif.setEnabled(true);
		            	Parameters.setDatenow(sdf.format(fr).substring(0, 10));
						if(fr.before(ft))
						{
							in = new IncrementalDBSCAN();	
							in.fileclean(sdf.format(fr).substring(0, 10),0-window,0);
							
							ca.setTime(Parameters.getdn());
							ca.add(ca.MONTH, inter);
							//System.out.println(sdf.format(ca.getTime()));
							if(sdf.format(ca.getTime()).compareTo(sdf.format(ft).substring(0, 10))<0&&!eps.getText().replace(" ", "").equals("")&&!minpts.getText().replace(" ", "").equals(""))
							{
								start.setEnabled(false);
								right=1;
								datef.getButton().setEnabled(false);
								datet.getButton().setEnabled(false);
								timewindow.setEnabled(false);
								interval.setEnabled(false);
								similar.setEnabled(false);
								eps.setEnabled(false);
								minpts.setEnabled(false);
								in.clusterperiod(sdf.format(ca.getTime()).substring(0, 10), Double.valueOf(eps.getText().replace(" ", "")), Integer.valueOf(minpts.getText().replace(" ", "")),tt,tt1,tt2);
								Parameters.setDatenow(sdf.format(ca.getTime()).substring(0, 10));
								too=new Topic(tt3);
								topi.setEnabled(true);
								start.setText("结果:"+sdf.format(ca.getTime()).substring(0, 10)+" >>");
								start.setEnabled(true);
								restart.setEnabled(true);
							}
							else 
							{
								start.setEnabled(false);
								right=1;
								datef.getButton().setEnabled(false);
								datet.getButton().setEnabled(false);
								timewindow.setEnabled(false);
								interval.setEnabled(false);
								similar.setEnabled(false);
								eps.setEnabled(false);
								minpts.setEnabled(false);
								in.clusterperiod(sdf.format(ft).substring(0, 10), Double.valueOf(eps.getText().replace(" ", "")), Integer.valueOf(minpts.getText().replace(" ", "")),tt,tt1,tt2);
								Parameters.setDatenow(sdf.format(ft).substring(0, 10));
								too=new Topic(tt3);
								topi.setEnabled(true);
								start.setText("结果:"+sdf.format(ft).substring(0, 10));
								restart.setEnabled(true);
							}
						}
					} catch (ParseException e) 
		            {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) 
		            {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(right==1)
				{
					try 
					{
						ca.setTime(Parameters.getdn());
						ca.add(ca.MONTH, inter);
						if(sdf.format(ca.getTime()).compareTo(sdf.format(ft).substring(0, 10))<0){
							start.setEnabled(false);
							restart.setEnabled(false);
							in.clusterperiod(sdf.format(ca.getTime()).substring(0, 10), Double.valueOf(eps.getText().replace(" ", "")), Integer.valueOf(minpts.getText().replace(" ", "")),tt,tt1,tt2);
							Parameters.setDatenow(sdf.format(ca.getTime()).substring(0, 10));
							too=new Topic(tt3);
							topi.setEnabled(true);
							start.setText("结果:"+sdf.format(ca.getTime()).substring(0, 10)+" >>");
							start.setEnabled(true);
							restart.setEnabled(true);
						}
						else
						{
							start.setEnabled(false);
							restart.setEnabled(false);
							in.clusterperiod(sdf.format(ft).substring(0, 10), Double.valueOf(eps.getText().replace(" ", "")), Integer.valueOf(minpts.getText().replace(" ", "")),tt,tt1,tt2);
							Parameters.setDatenow(sdf.format(ft).substring(0, 10));
							too=new Topic(tt3);
							topi.setEnabled(true);
							start.setText("结果:"+sdf.format(ft).substring(0, 10));
							restart.setEnabled(true);
						}
					} catch (ParseException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}			
				}
				}  
			};  
			t.start(); 				
		}
	}
}
public class ClusterInterface 
{
	public static void main(String[] args) throws ParseException
	{
		JFrame frame=new CInterface();
    }
}
