package main;
import java.awt.BorderLayout;  
import java.awt.Color;  
import java.awt.Dimension;  
import java.awt.FlowLayout;  
import java.awt.GridLayout;  
import java.awt.Image;  
import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;  
import java.awt.event.ItemEvent;  
import java.awt.event.ItemListener;  
import java.awt.event.WindowAdapter;  
import java.awt.event.WindowEvent;  
import java.awt.event.WindowFocusListener;  
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;  
import java.util.Date;  
import javax.swing.BorderFactory;  
import javax.swing.ButtonGroup;  
import javax.swing.ImageIcon;  
import javax.swing.JButton;  
import javax.swing.JComboBox;  
import javax.swing.JDialog;  
import javax.swing.JFrame;
import javax.swing.JLabel;  
import javax.swing.JPanel;  
import javax.swing.JTextField;  
import javax.swing.JToggleButton;  
import javax.swing.SwingConstants;  
import javax.swing.border.Border;  
import javax.swing.plaf.ButtonUI;  
import javax.swing.plaf.ComboBoxUI;  
import preprocess.Parameters;
public class DateField extends JPanel  
{  
    private JTextField tfDate;  
    private JButton button;  
    private JDialog dlg;  
    private JPanel paCalendar;  
    private boolean show;  
    private JComboBox cboYear;  
    private JComboBox cboMonth;  
    private JLabel[] lbls = new JLabel[7];  
    private JToggleButton[] toggles = new JToggleButton[42];  
    private Border border;  
    private Color bgColor;  
    private int width = 150, height = 25;  
    private int year,yeare;  
    private int month,monthe;  
    private int date, datee;  
    private int dayOfWeek;  
    private String[] week = { "日", "一", "二", "三", "四", "五", "六" };  
    public DateField(int y,int m,int d,int ye,int me,int de) throws ParseException  
    {  
        year = y;  
        month = m;  
        date = d; 
        yeare = ye;  
        monthe = me;  
        datee = de;
        dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);  
        initUI();  
        initDateDialog();  
        updateField();  
    }  
   private void initUI()  
    {  
        tfDate = new JTextField();  
        tfDate.setEditable(false);  
        tfDate.setBackground(Color.WHITE);  
        border = tfDate.getBorder();  
        tfDate.setBorder(null);  
        button = new JButton();  
        button.setPreferredSize(new Dimension(height, height));  
        button.addActionListener(new ActionListener()  
        {  
            public void actionPerformed(ActionEvent e)  
            {  
                if (show == false)  
                {  
                    dlg.setLocation(DateField.this.getLocationOnScreen().x, DateField.this.getLocationOnScreen().y + DateField.this.height);  
                    dlg.setAlwaysOnTop(true);  
                    dlg.setVisible(true);  
                } else  
                {  
                    dlg.dispose();  
                }  
                updateField();  
                show = !show;  
            }  
        });  
       this.bgColor = this.getBackground();  
       this.setOpaque(false);  
        this.setLayout(new BorderLayout(0, 0));  
        this.setBorder(border);  
        this.add(tfDate, BorderLayout.CENTER);  
        this.add(button, BorderLayout.EAST);  
    }  
   private void initDateDialog() throws ParseException  
    {  
        dlg = new JDialog();  
        dlg.setUndecorated(true);  
        paCalendar = new JPanel(new BorderLayout());  
        paCalendar.setBorder(this.border);  
        paCalendar.setBackground(this.bgColor);  
       int borderWidth = 3;  
        // 初始化两个下拉组件用于年和月的选择  
        JPanel paHeader = new JPanel(new GridLayout(1, 2, borderWidth, borderWidth));  
        paHeader.setOpaque(false);  
        paHeader.setPreferredSize(new Dimension(this.width, this.height + borderWidth));  
        paHeader.setBorder(BorderFactory.createEmptyBorder(borderWidth, borderWidth, borderWidth, borderWidth));  
        paHeader.add(cboYear = new JComboBox());  
        paHeader.add(cboMonth = new JComboBox());  
        initYearModel();  
        initMonthModel();  
        paCalendar.add(paHeader, BorderLayout.NORTH);  
       // 初始化日期组件  
        JPanel paDay = new JPanel();  
        paDay.setOpaque(false);  
        paDay.setPreferredSize(new Dimension(this.height * 7, this.height * 7));  
        initDate(paDay);  
        paCalendar.add(paDay, BorderLayout.CENTER);  
       dlg.setContentPane(paCalendar);  
        dlg.pack();  
        dlg.addWindowFocusListener(new WindowAdapter()  
        {  
            public void windowLostFocus(WindowEvent e)  
            {  
                dlg.dispose();  
            }  
        });  
    }  
    private void initYearModel()  
    {  
        for (int y = year; y <= yeare; y++)  
            cboYear.addItem(y);  
        cboYear.setSelectedItem(year);  
        cboYear.addItemListener(new ItemListener()  
        {  
            public void itemStateChanged(ItemEvent e)  
            {  
                year = Integer.parseInt(cboYear.getSelectedItem().toString());
                try {
					updateComponent();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}  
            }  
       });  
    }  
   private void initMonthModel()  
    {  
        for (int m = 1; m <= 12; m++)  
            cboMonth.addItem(m);  
        cboMonth.setSelectedItem(month);  
        cboMonth.addItemListener(new ItemListener()  
        {  
           public void itemStateChanged(ItemEvent e)  
            {  
                month = Integer.parseInt(cboMonth.getSelectedItem().toString());  
                try 
                {
					updateComponent();
				} catch (ParseException e1) 
                {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}  
            }  
        });  
    }  
   private void initDate(JPanel pa) throws ParseException  
    {  
        pa.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));  
        // 显示日历的标签栏  
        for (int i = 0; i < week.length; i++)  
        {  
            lbls[i] = new JLabel(week[i]);  
            lbls[i].setHorizontalAlignment(SwingConstants.CENTER);  
            lbls[i].setOpaque(true);  
            lbls[i].setBackground(Color.WHITE);  
            lbls[i].setPreferredSize(new Dimension(this.height, this.height));  
            pa.add(lbls[i]);  
        }  
        // 加载日历按钮  
        ButtonGroup group = new ButtonGroup();  
        for (int i = 0; i < 42; i++)  
        {  
            toggles[i] = new JToggleButton();  
            toggles[i].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));  
            toggles[i].setPreferredSize(new Dimension(this.height, this.height));  
            toggles[i].addActionListener(new ActionListener()  
            {  
                public void actionPerformed(ActionEvent e)  
                {  
                    date = Integer.parseInt(((JToggleButton) e.getSource()).getText().toString());  
                    dlg.dispose();  
                    updateField();  
                }  
            });  
            group.add(toggles[i]);  
            pa.add(toggles[i]);  
        }  
        updateComponent();  
    }  
   private void updateComponent() throws ParseException  
    {  
        if (cboYear == null || cboMonth == null)  
            return;  
        Calendar cal = Calendar.getInstance();  
        cal.set(year, month - 1, 1);  
        // 根据当月的第一天是星期几来判断日历按钮的数字该从第几个按钮开始显示  
        int off = cal.get(Calendar.DAY_OF_WEEK) - 1;  
        // 计算当月总共有几天  
        int end = 30;  
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12)  
            end = 31;  
        if (month == 2)  
        {  
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)  
                end = 29;  
            else  
                end = 28;  
        }  
        for (int i = 0; i < 42; i++)  
        {  
            if (i >= off && i <= end + off - 1)  
            {  
                int day = i - off + 1;  
                toggles[i].setText(day + "");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date d1=sdf.parse(Parameters.datefrom+" 00:00:00"); 
                Date d2=sdf.parse(Parameters.dateto+" 00:00:00");
                Calendar ca = Calendar.getInstance();
                ca.setTime(d2);
                d2=ca.getTime();
                ca.set(year, month-1, day,0,0,0);
                Date now = ca.getTime();
                if(now.before(d1)||now.after(d2))
                {	
                	toggles[i].setEnabled(false);
                }
                else 
                {
                	toggles[i].setEnabled(true);  
                }
            } else  
            {  
                toggles[i].setText("");  
                toggles[i].setEnabled(false);  
            }  
        }  
        // 使当天的按钮呈现被按下的效果  
        int day = date + off - 1;  
        toggles[day].setSelected(true);  
    }  
   public JTextField getTfDate() 
   {
		return tfDate;
	}
	public void setTfDate(JTextField tfDate) 
	{
		this.tfDate = tfDate;
	}
	public JButton getButton() 
	{
		return button;
	}
	public void setButton(JButton button) 
	{
		this.button = button;
	}
	public JDialog getDlg() 
	{
		return dlg;
	}
	public void setDlg(JDialog dlg) 
	{
		this.dlg = dlg;
	}
	public JPanel getPaCalendar() {
		return paCalendar;
	}

	public void setPaCalendar(JPanel paCalendar) {
		this.paCalendar = paCalendar;
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public JComboBox getCboYear() {
		return cboYear;
	}

	public void setCboYear(JComboBox cboYear) {
		this.cboYear = cboYear;
	}

	public JComboBox getCboMonth() {
		return cboMonth;
	}

	public void setCboMonth(JComboBox cboMonth) {
		this.cboMonth = cboMonth;
	}

	public JLabel[] getLbls() {
		return lbls;
	}

	public void setLbls(JLabel[] lbls) {
		this.lbls = lbls;
	}

	public JToggleButton[] getToggles() {
		return toggles;
	}

	public void setToggles(JToggleButton[] toggles) {
		this.toggles = toggles;
	}

	public Color getBgColor() {
		return bgColor;
	}

	public void setBgColor(Color bgColor) {
		this.bgColor = bgColor;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getYeare() {
		return yeare;
	}

	public void setYeare(int yeare) {
		this.yeare = yeare;
	}

	public int getMonthe() {
		return monthe;
	}

	public void setMonthe(int monthe) {
		this.monthe = monthe;
	}

	public int getDatee() {
		return datee;
	}

	public void setDatee(int datee) {
		this.datee = datee;
	}

	public int getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(int dayOfWeek) 
	{
		this.dayOfWeek = dayOfWeek;
	}
	public String[] getWeek() 
	{
		return week;
	}
	public void setWeek(String[] week) 
	{
		this.week = week;
	}
	public Border getBorder() 
	{
		return border;
	}
	public void setYear(int year) 
	{
		this.year = year;
	}
	public void setMonth(int month) 
	{
		this.month = month;
	}
	public void setDate(int date) 
	{
		this.date = date;
	}
	// 更新文本框内的文字  
    private void updateField()  
    {  
        StringBuilder builder = new StringBuilder();  
        builder.append(this.year + "年");  
        builder.append(this.month + "月");  
        builder.append(this.date + "日");  
        Calendar cal = Calendar.getInstance();  
        cal.set(this.year, this.month - 1, this.date);  
        builder.append(" 星期" + week[cal.get(Calendar.DAY_OF_WEEK) - 1]);  
        tfDate.setText(builder.toString());  
    }  
   public Dimension getPreferredSize()  
    {  
        return new Dimension(this.width, this.height);  
    }  
   public void setPreferredSize(Dimension preferredSize)  
    {  
        super.setPreferredSize(preferredSize);  
        this.width = (int) preferredSize.getWidth();  
        this.height = (int) preferredSize.getHeight();  
    }  
   public void setBackground(Color bg)  
    {  
        super.setBackground(bg);  
        this.bgColor = bg;  
    }  
   public void setBorder(Border border)  
    {  
        super.setBorder(border);  
        if (paCalendar != null)  
            paCalendar.setBorder(border);  
    }  
   public void setButtonUI(String clzUIName)  
    {  
        try  
        {  
            button.setUI((ButtonUI) Class.forName(clzUIName).newInstance());  
        }  
        catch (Exception e)  
        {  
            e.printStackTrace();  
        }  
    }  
   public void setDateButtonUI(String clzUIName)  
    {  
        try  
        {  
            for (int i = 0; i < 42; i++)  
                toggles[i].setUI((ButtonUI) Class.forName(clzUIName).newInstance());  
        }  
        catch (Exception e)  
        {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
    }  
   public void setComboBoxUI(String clzUIName)  
    {  
        try  
        {  
            cboYear.setUI((ComboBoxUI) Class.forName(clzUIName).newInstance());  
            cboMonth.setUI((ComboBoxUI) Class.forName(clzUIName).newInstance());  
        }  
        catch (Exception e)  
        {  
            e.printStackTrace();  
        }  
   }  
   public void setLableColor(Color fg, Color bg)  
    {  
        for (int i = 0; i < lbls.length; i++)  
        {  
            lbls[i].setForeground(fg);  
            lbls[i].setBackground(bg);  
        }  
    }  
   public void setIcon(Image icon)  
    {  
        button.setIcon(new ImageIcon(icon));  
    }  
   public int getYear()  
    {  
        return this.year;  
    }  
   public int getMonth()  
    {  
        return this.month;  
    }  
   public int getDateOfWeek()  
    {  
        return this.date;  
    }  
   public Date getDate()  
    {  
        Calendar cal = Calendar.getInstance();  
        cal.set(this.year, this.month, this.date);  
        return cal.getTime();  
    }    
}  