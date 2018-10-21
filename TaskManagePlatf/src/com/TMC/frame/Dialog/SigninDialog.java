package com.TMC.frame.Dialog;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
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
import org.w3c.dom.ls.LSInput;

import com.TMC.Cache.DataCache;
import com.TMC.frame.MainFrame;
import com.TMS.entity.DepartmentInfoPojo;
import com.TMS.entity.UserInfoPojo;
import com.TMS.model.TaskModel;
import com.configpath.ConfigPath;

import sun.applet.Main;

import org.eclipse.swt.widgets.Combo;

public class SigninDialog extends Dialog {

	private Shell shlservice;
	private UserInfoPojo assiger = null;
	private Text text_1;
	private Text text_2;
	private Text text_3;
	private Combo combo;
	
	public SigninDialog(Shell parent,UserInfoPojo assiger) {
		super(parent);
		this.assiger = assiger;
	}

	public UserInfoPojo open() {
		shlservice = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shlservice.setImage(new Image(shlservice.getDisplay(), ConfigPath.LoginImagePath));
		shlservice.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND));
		shlservice.setSize(474, 251);
		shlservice.setText("注册");
		shlservice.setLayout(new FillLayout());
		shlservice.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				assiger = null;
			}
		});

		int screenH = Toolkit.getDefaultToolkit().getScreenSize().height;
		int screenW = Toolkit.getDefaultToolkit().getScreenSize().width;
		OS.SetWindowPos(shlservice.handle,OS.HWND_TOPMOST,(screenW - shlservice.getSize().x) / 2, (screenH - shlservice.getSize().y) / 2,
				474, 227, SWT.NULL);
		Composite composite = new Composite(shlservice, SWT.BORDER);
		composite.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		composite.setLayout(null);
		
		Label label = new Label(composite, SWT.NONE);
		label.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		label.setBounds(87, 51, 48, 23);
		label.setText("工号:");
		label.setFont(SWTResourceManager.getFont("楷体", 13, SWT.BOLD));
		
		text_1 = new Text(composite, SWT.BORDER);
		text_1.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
		text_1.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		text_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		text_1.setBounds(141, 50, 234, 23);
		
		Label label_3 = new Label(composite, SWT.NONE);
		label_3.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		label_3.setText("姓名:");
		label_3.setFont(SWTResourceManager.getFont("楷体", 13, SWT.BOLD));
		label_3.setBounds(87, 81, 48, 23);
		
		text_2 = new Text(composite, SWT.BORDER);
		text_2.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
		text_2.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		text_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		text_2.setBounds(142, 80, 234, 23);
		
		Button button = new Button(composite, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			    	if(newUser()!=null){
			    		shlservice.dispose();
			    	}
			}
		});
		button.setText("注册");
		button.setImage(SWTResourceManager.getImage(ConfigPath.LoginImagePath));
		button.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		button.setBounds(190, 169, 80, 27);
		
		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setText("部门:");
		label_1.setFont(SWTResourceManager.getFont("楷体", 13, SWT.BOLD));
		label_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		label_1.setBounds(86, 111, 48, 23);
		
		combo = new Combo(composite, SWT.READ_ONLY);
		combo.setBounds(141, 109, 234, 25);
		
		Label label_2 = new Label(composite, SWT.NONE);
		label_2.setText("密码:");
		label_2.setFont(SWTResourceManager.getFont("楷体", 13, SWT.BOLD));
		label_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		label_2.setBounds(86, 141, 48, 23);
		
		text_3 = new Text(composite, SWT.BORDER);
		text_3.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		text_3.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
		text_3.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		text_3.setBounds(141, 140, 234, 23);
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
		lblNewLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		lblNewLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblNewLabel.setBounds(10, 10, 444, 17);
		lblNewLabel.setText("已从电脑获取到你的信息，请确认是否正确！");

		show();
		shlservice.layout();
		shlservice.open();
		Display display = shlservice.getDisplay();
		while (!shlservice.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return assiger;
	}

	private void show() {
		if(assiger!=null){
			text_1.setText(assiger.getUser_name()+"");
			text_2.setText(assiger.getReal_name()+"");
			List<Object> list = DataCache.departmentList;
			if(list!=null){
				ArrayList<String> strings = new ArrayList<>();
				for (int i = 0; i < list.size(); i++) {
					TaskModel temp = (TaskModel) list.get(i);
					strings.add(temp.getDepartment().getDepartment());
				}
				String [] de = new String[strings.size()];
				strings.toArray(de);
				combo.setItems(de);
			}
		}
		
	}

	private UserInfoPojo newUser() {
		String number = text_1.getText();
		String name = text_2.getText();
		String password = text_3.getText();
		if(!StringUtils.isNotEmpty(number.trim())){
			CommonDialog.showMessage(shlservice, "提示", "请输入工号!", SWT.ICON_INFORMATION);
			return null;
		}
		if(!StringUtils.isNotEmpty(name.trim())){
			CommonDialog.showMessage(shlservice, "提示", "请输入姓名!", SWT.ICON_INFORMATION);
			return null;
		}
		if(!StringUtils.isNotEmpty(password.trim())){
			CommonDialog.showMessage(shlservice, "提示", "请输入密码!", SWT.ICON_INFORMATION);
			return null;
		}else if(password.length()<6){
			CommonDialog.showMessage(shlservice, "提示", "密码长度应该大于6!", SWT.ICON_INFORMATION);
			return null;
		}
		int select = combo.getSelectionIndex();
		if(select<0){
			CommonDialog.showMessage(shlservice, "提示", "请选择部门!", SWT.ICON_INFORMATION);
			return null;
		}
		assiger = new UserInfoPojo();
		assiger.setUser_name(number);
		assiger.setReal_name(name);
		assiger.setUser_password(password);
//		DepartmentInfoPojo department = (DepartmentInfoPojo) MainFrame.departmentList.get(select);
		assiger.setDepartment(select+1);
		return assiger;
	}
}
