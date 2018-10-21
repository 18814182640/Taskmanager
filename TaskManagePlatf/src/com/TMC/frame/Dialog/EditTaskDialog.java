package com.TMC.frame.Dialog;

import java.awt.Toolkit;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.TMC.Cache.DataCache;
import com.TMS.model.TaskModel;
import com.config.Config.TASK_STATUS;
import com.configpath.ConfigPath;
import com.sun.beans.util.Cache;

public class EditTaskDialog extends Dialog {

	private Shell shlservice;
	private TaskModel taskModel;
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;
	private Text text_4;
	private Text text_5;
	private Text text_6;
	private Text text_7;
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private int model;
    private Text text_8;
	
	public EditTaskDialog(Shell parent,TaskModel taskModel,int model) {
		super(parent);
		this.taskModel =taskModel;
		this.model = model;
	}

	public void open() {
		shlservice = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shlservice.setImage(new Image(shlservice.getDisplay(), ConfigPath.UPDATE));
		shlservice.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND));
		shlservice.setSize(474, 652);
		shlservice.setText("任务详情");
		/*shlservice.setLocation(getParent().getLocation().x + getParent().getSize().x / 2 - shlservice.getSize().x / 2,
				getParent().getLocation().y + getParent().getSize().y / 2 - shlservice.getSize().y / 2);*/
		shlservice.setLayout(new FillLayout());

		int screenH = Toolkit.getDefaultToolkit().getScreenSize().height;
		int screenW = Toolkit.getDefaultToolkit().getScreenSize().width;
		OS.SetWindowPos(shlservice.handle,OS.HWND_TOPMOST,(screenW - shlservice.getSize().x) / 2, (screenH - shlservice.getSize().y) / 2,
				480, 615, SWT.NULL);
		Composite composite = new Composite(shlservice, SWT.BORDER);
		composite.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		composite.setLayout(null);

		Button cancelButton = new Button(composite, SWT.NONE);
		cancelButton.setBounds(192, 583, 80, 27);
		cancelButton.setImage(new Image(shlservice.getDisplay(), ConfigPath.OKImagePath));
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlservice.dispose();
			}
		});
		cancelButton.setText("确认");
		cancelButton.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setText("任务执行人");
		lblNewLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblNewLabel.setBounds(10, 10, 72, 12);
		lblNewLabel.setFont(SWTResourceManager.getFont("楷体", 10, SWT.BOLD));
		if (model==0) {
			lblNewLabel.setText("任务发布人:");
		}else{
			lblNewLabel.setText("任务执行人:");
		}
		
		text = new Text(composite, SWT.BORDER);
		text.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
		text.setEditable(false);
		text.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		text.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		text.setBounds(10, 28, 446, 23);
		
		Label label = new Label(composite, SWT.NONE);
		label.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		label.setBounds(10, 57, 85, 12);
		label.setText("任务发布时间:");
		label.setFont(SWTResourceManager.getFont("楷体", 10, SWT.BOLD));
		
		text_1 = new Text(composite, SWT.BORDER);
		text_1.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
		text_1.setEditable(false);
		text_1.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		text_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		text_1.setBounds(10, 78, 446, 23);
		
		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		label_1.setText("任务详情:");
		label_1.setFont(SWTResourceManager.getFont("楷体", 10, SWT.BOLD));
		label_1.setBounds(10, 107, 72, 12);
		
		text_2 = new Text(composite, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		text_2.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
		text_2.setEditable(false);
		text_2.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		text_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		text_2.setBounds(10, 125, 446, 131);
		
		Label label_2 = new Label(composite, SWT.NONE);
		label_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		label_2.setText("任务状态:");
		label_2.setFont(SWTResourceManager.getFont("楷体", 10, SWT.BOLD));
		label_2.setBounds(10, 395, 85, 12);
		
		text_3 = new Text(composite, SWT.BORDER);
		text_3.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
		text_3.setEditable(false);
		text_3.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		text_3.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		text_3.setBounds(10, 413, 446, 23);
		
		Label label_3 = new Label(composite, SWT.NONE);
		label_3.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		label_3.setText("任务计划开始时间:");
		label_3.setFont(SWTResourceManager.getFont("楷体", 10, SWT.BOLD));
		label_3.setBounds(10, 442, 122, 12);
		
		text_4 = new Text(composite, SWT.BORDER);
		text_4.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
		text_4.setEditable(false);
		text_4.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		text_4.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		text_4.setBounds(10, 460, 446, 23);
		
		Label label_4 = new Label(composite, SWT.NONE);
		label_4.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		label_4.setText("任务计划结束时间:");
		label_4.setFont(SWTResourceManager.getFont("楷体", 10, SWT.BOLD));
		label_4.setBounds(10, 489, 122, 12);
		
		text_5 = new Text(composite, SWT.BORDER);
		text_5.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
		text_5.setEditable(false);
		text_5.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		text_5.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		text_5.setBounds(10, 507, 446, 23);
		
		Label label_5 = new Label(composite, SWT.NONE);
		label_5.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		label_5.setText("任务完成剩余时间:");
		label_5.setFont(SWTResourceManager.getFont("楷体", 10, SWT.BOLD));
		label_5.setBounds(10, 536, 122, 12);
		
		text_6 = new Text(composite, SWT.BORDER);
		text_6.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
		text_6.setEditable(false);
		text_6.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		text_6.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		text_6.setBounds(10, 554, 446, 23);
		
		Label lblSvn = new Label(composite, SWT.NONE);
		lblSvn.setText("SVN路径");
		lblSvn.setFont(SWTResourceManager.getFont("楷体", 10, SWT.BOLD));
		lblSvn.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblSvn.setBounds(10, 262, 85, 13);
		
		text_7 = new Text(composite, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		text_7.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		text_7.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
		text_7.setEditable(false);
		text_7.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		text_7.setBounds(10, 279, 446, 63);
		
		Label label_6 = new Label(composite, SWT.NONE);
		label_6.setText("所属项目:");
		label_6.setFont(SWTResourceManager.getFont("楷体", 10, SWT.BOLD));
		label_6.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		label_6.setBounds(10, 348, 85, 12);
		
		text_8 = new Text(composite, SWT.BORDER);
		text_8.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		text_8.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
		text_8.setEditable(false);
		text_8.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		text_8.setBounds(10, 366, 446, 23);

		show();
		shlservice.layout();
		shlservice.open();
		Display display = shlservice.getDisplay();
		while (!shlservice.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	private void show(){
		if(model==0){
			text.setText(getDefult(taskModel.getAuthorizer().getReal_name()));
		}else{
			text.setText(getDefult(taskModel.getAssigner().getReal_name()));
		}
		text_1.setText(getDefult(format.format(new Timestamp(taskModel.getTask().getSave_time()))));
		text_2.setText(getDefult(taskModel.getTask().getTask_info()));
		text_3.setText(getDefult(TASK_STATUS.getEelenment(taskModel.getTask().getStatus_id()).getInfo()));
		Timestamp start = new Timestamp(taskModel.getTask().getStart_time());
		text_4.setText(getDefult(format.format(start)));
		Timestamp end = new Timestamp(taskModel.getTask().getEnd_time());
		text_5.setText(getDefult(format.format(end)));
		long temp =end.getTime() -  System.currentTimeMillis();  //ms
		String tempStr = temp/(1000*60*60*24) +"天" + (temp/(1000*60*60))%24 +"小时" + temp/(1000*60*24)%60 +"分钟" ;
		text_6.setText(getDefult(tempStr));
		text_7.setText(getDefult(taskModel.getTask().getSvn_path()));
		text_8.setText(getDefult(DataCache.getItemName(taskModel.getTask().getItem_id())));
	}
	
	
	
	private String getDefult(String str){
		if(StringUtils.isNotEmpty(str)){
			return str;
		}else{
			return "-";
		}
	}
	
}
