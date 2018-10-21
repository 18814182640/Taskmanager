package com.TMC.frame.Dialog;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.TMC.Cache.DataCache;
import com.TMS.entity.ItemInfoPojo;
import com.TMS.entity.UserInfoPojo;
import com.TMS.model.TaskModel;
import com.configpath.ConfigPath;
import com.sun.org.apache.xml.internal.security.Init;

public class EditUserDialog extends Dialog {

	private Shell shlservice;
	private TaskModel taskModel;
	private Combo combo1,combo2,combo3;
    private UserInfoPojo userInfoPojo;
    private Text text1;
    private Text text2;
    private Text text3;
    private Text text4;
    private Text text5;
    private Text text6;
	/**
	 * @wbp.parser.constructor
	 */
	public EditUserDialog(Shell parent,UserInfoPojo userInfoPojo) {
		super(parent);
		this.userInfoPojo = userInfoPojo;
	}
  
	public TaskModel open() {
		shlservice = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shlservice.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				taskModel = null;
			}
		});
		shlservice.setImage(new Image(shlservice.getDisplay(), ConfigPath.AddImagePath));
		shlservice.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		shlservice.setSize(486, 501);
		shlservice.setText("编辑人员:");
		shlservice.setLocation(getParent().getLocation().x + getParent().getSize().x / 2 - shlservice.getSize().x / 2,
				getParent().getLocation().y + getParent().getSize().y / 2 - shlservice.getSize().y / 2);
		shlservice.setLayout(new FillLayout());

		Composite composite = new Composite(shlservice, SWT.BORDER);
		composite.setFont(SWTResourceManager.getFont("楷体", 11, SWT.NORMAL));
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		composite.setLayout(null);

		Button okButton = new Button(composite, SWT.CENTER);
		okButton.setBounds(153, 432, 80, 27);
		okButton.setImage(new Image(shlservice.getDisplay(), ConfigPath.OKImagePath));
		okButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newTask();
			}
		});
		okButton.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		okButton.setText("OK");

		Button cancelButton = new Button(composite, SWT.CENTER);
		cancelButton.setBounds(243, 432, 75, 27);
		cancelButton.setImage(new Image(shlservice.getDisplay(), ConfigPath.RemoveImagePath));
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				taskModel = null;
				shlservice.dispose();
			}
		});
		cancelButton.setText("Cancel");
		cancelButton.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		
		Label label = new Label(composite, SWT.NONE);
		label.setBounds(10, 309, 77, 15);
		label.setText("权限类型:");
		label.setFont(SWTResourceManager.getFont("楷体", 11, SWT.BOLD));
		label.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		
		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setBounds(10, 356, 77, 15);
		label_1.setText("所属部门:");
		label_1.setFont(SWTResourceManager.getFont("楷体", 11, SWT.BOLD));
		label_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		
		combo2 = new Combo(composite, SWT.READ_ONLY);
		combo2.setBounds(88, 351, 378, 25);
		combo2.setItems(DataCache.getDepartmentNames());
		
		Label lblId = new Label(composite, SWT.NONE);
		lblId.setBounds(10, 18, 61, 15);
		lblId.setText("ID:");
		lblId.setFont(SWTResourceManager.getFont("楷体", 11, SWT.BOLD));
		lblId.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		
		text1 = new Text(composite, SWT.BORDER);
		text1.setBounds(88, 14, 378, 23);
		text1.setEditable(false);
		
		Label label_4 = new Label(composite, SWT.NONE);
		label_4.setBounds(10, 55, 61, 15);
		label_4.setText("工号:");
		label_4.setFont(SWTResourceManager.getFont("楷体", 11, SWT.BOLD));
		label_4.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		
		text2 = new Text(composite, SWT.BORDER);
		text2.setBounds(88, 55, 378, 23);
		
		Label label_5 = new Label(composite, SWT.NONE);
		label_5.setText("登陆密码:");
		label_5.setFont(SWTResourceManager.getFont("楷体", 11, SWT.BOLD));
		label_5.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		label_5.setBounds(10, 103, 77, 15);
		
		text3 = new Text(composite, SWT.BORDER);
		text3.setBounds(88, 99, 378, 23);
		
		Label label_6 = new Label(composite, SWT.NONE);
		label_6.setText("姓名:");
		label_6.setFont(SWTResourceManager.getFont("楷体", 11, SWT.BOLD));
		label_6.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		label_6.setBounds(10, 154, 61, 15);
		
		text4 = new Text(composite, SWT.BORDER);
		text4.setBounds(88, 150, 378, 23);
		
		Label lblEmail = new Label(composite, SWT.NONE);
		lblEmail.setText("E-mail");
		lblEmail.setFont(SWTResourceManager.getFont("楷体", 11, SWT.BOLD));
		lblEmail.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblEmail.setBounds(10, 207, 61, 15);
		
		text5 = new Text(composite, SWT.BORDER);
		text5.setBounds(88, 203, 378, 23);
		
		Label label_3 = new Label(composite, SWT.NONE);
		label_3.setText("电话:");
		label_3.setFont(SWTResourceManager.getFont("楷体", 11, SWT.BOLD));
		label_3.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		label_3.setBounds(10, 258, 61, 15);
		
		text6 = new Text(composite, SWT.BORDER);
		text6.setBounds(88, 254, 378, 23);
		
		combo1 = new Combo(composite, SWT.READ_ONLY);
		combo1.setItems(new String[] {"用户", "管理员", "超级管理员"});
		combo1.setBounds(88, 304, 378, 25);
		
		Label label_2 = new Label(composite, SWT.NONE);
		label_2.setText("用户状态:");
		label_2.setFont(SWTResourceManager.getFont("楷体", 11, SWT.BOLD));
		label_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		label_2.setBounds(10, 406, 77, 15);
		
		combo3 = new Combo(composite, SWT.READ_ONLY);
		combo3.setItems(new String[] {"无效", "有效"});
		combo3.setBounds(88, 401, 378, 25);
		
		
		init();

		shlservice.layout();
		shlservice.open();
		Display display = shlservice.getDisplay();
		while (!shlservice.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return taskModel;
	}

	private void init() {
		if(userInfoPojo!=null){
			text1.setText(userInfoPojo.getId()+"");
			text2.setText(userInfoPojo.getUser_name()+"");
			text3.setText(userInfoPojo.getUser_password()+"");
			text4.setText(userInfoPojo.getReal_name()+"");
			text5.setText(userInfoPojo.getUser_mail()+"");
			text6.setText(userInfoPojo.getPhone_number()+"");
			combo1.select(userInfoPojo.getUser_role()-1);
			combo2.select(userInfoPojo.getDepartment()-1);
			combo3.select(userInfoPojo.getUser_status());
		}
		
	}

	private void newTask() {
		String userName = text2.getText();
		if(!StringUtils.isNotEmpty(userName)){
			CommonDialog.showMessage(shlservice, "提示", "工号不能为空!", SWT.ICON_INFORMATION);
			return;
		}
		String password = text3.getText();
		if(!StringUtils.isNotEmpty(password)){
			CommonDialog.showMessage(shlservice, "提示", "密码不能为空!", SWT.ICON_INFORMATION);
			return;
		}
		String realName = text4.getText();
		if(!StringUtils.isNotEmpty(realName)){
			CommonDialog.showMessage(shlservice, "提示", "姓名不能为空!", SWT.ICON_INFORMATION);
			return;
		}
		String email = text5.getText();
		String phone = text6.getText();
		
		int user_role  = combo1.getSelectionIndex();
		if(user_role<0){
			CommonDialog.showMessage(shlservice, "提示", "请选择权限类型!", SWT.ICON_INFORMATION);
			return;
		}
		int select = combo2.getSelectionIndex();
		if(select<0){
			CommonDialog.showMessage(shlservice, "提示", "请选择所属部门!", SWT.ICON_INFORMATION);
			return;
		}
		int id = DataCache.getDepartmentID(select);
		int status = combo3.getSelectionIndex();
		if(status<0){
			CommonDialog.showMessage(shlservice, "提示", "请选择用户状态!", SWT.ICON_INFORMATION);
			return;
		}
		userInfoPojo.setUser_name(userName);
		userInfoPojo.setUser_password(password);
		userInfoPojo.setReal_name(realName);
		userInfoPojo.setUser_mail(email);
		userInfoPojo.setPhone_number(phone);
		userInfoPojo.setUser_role(user_role+1);
		userInfoPojo.setDepartment(id);
		userInfoPojo.setUser_status(status);
		taskModel = new TaskModel();
		taskModel.setAssigner(userInfoPojo);
		shlservice.dispose();
	}
	

}
