package com.TMC.frame.Dialog;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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

import com.TMC.Unit.MD5;
import com.TMC.frame.MainFrame;
import com.TMS.entity.UserInfoPojo;
import com.configpath.ConfigPath;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.events.VerifyEvent;

public class LoginDialog extends Dialog {

	private Shell shlservice;
	private UserInfoPojo assiger = null;
	private Text text_1;
	private Text txtAdmin;
	private Button btnCheckButton;
	private File file;
	private Properties properties;
	private HashMap<String, String> map;
	private String pathName;
	private String fileName = "user.propertie";
	private String isSelect = "---isselect";
	
	public LoginDialog(Shell parent) {
		super(parent);
	}

	public UserInfoPojo open() {
		shlservice = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shlservice.setImage(new Image(shlservice.getDisplay(), ConfigPath.LandImagePath));
		shlservice.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND));
		shlservice.setSize(474, 261);
		shlservice.setText("请登录");
		/*shlservice.setLocation(getParent().getLocation().x + getParent().getSize().x / 2 - shlservice.getSize().x / 2,
				getParent().getLocation().y + getParent().getSize().y / 2 - shlservice.getSize().y / 2);*/
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
				474, 261, SWT.NULL);
		Composite composite = new Composite(shlservice, SWT.BORDER);
		composite.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		composite.setLayout(null);

		Button cancelButton = new Button(composite, SWT.NONE);
		cancelButton.setBounds(162, 120, 80, 27);
		cancelButton.setImage(new Image(shlservice.getDisplay(), ConfigPath.LandImagePath));
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (newUser()!=null) {
					sendInfo();
					shlservice.dispose();
				}
			}
		});
		cancelButton.setText("登陆");
		cancelButton.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		
		Label label = new Label(composite, SWT.NONE);
		label.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		label.setBounds(79, 59, 48, 23);
		label.setText("工号:");
		label.setFont(SWTResourceManager.getFont("楷体", 13, SWT.BOLD));
		
		text_1 = new Text(composite, SWT.BORDER);
		text_1.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				String name = text_1.getText();
				if(StringUtils.isNotEmpty(name)){
					if(properties!=null){
						if (properties.containsKey(name)) {
							txtAdmin.setText(properties.getProperty(name));
						}else{
							txtAdmin.setText("");
						}
					}
				}
			}
		});
		text_1.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
		text_1.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		text_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		text_1.setBounds(133, 58, 265, 23);
		
		Label label_3 = new Label(composite, SWT.NONE);
		label_3.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		label_3.setText("密码:");
		label_3.setFont(SWTResourceManager.getFont("楷体", 13, SWT.BOLD));
		label_3.setBounds(78, 91, 48, 23);
		
		txtAdmin = new Text(composite, SWT.BORDER);
		txtAdmin.setText("");
		txtAdmin.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
		txtAdmin.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		txtAdmin.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtAdmin.setBounds(133, 90, 265, 23);
		txtAdmin.setEchoChar('*');
		
		btnCheckButton = new Button(composite, SWT.CHECK);
		btnCheckButton.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		btnCheckButton.setBounds(329, 125, 69, 17);
		btnCheckButton.setText("记住密码");
		
		Button button = new Button(composite, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlservice.dispose();
				MainFrame.mainFrame.newUser();
			}
		});
		button.setText("注册");
		button.setImage(SWTResourceManager.getImage(ConfigPath.LoginImagePath));
		button.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		button.setBounds(248, 120, 80, 27);

		init();
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

	private UserInfoPojo newUser() {
		String name = text_1.getText();
		String password = txtAdmin.getText();
		if(!StringUtils.isNotEmpty(name.trim())){
			CommonDialog.showMessage(shlservice, "提示", "请输入工号!", SWT.ICON_INFORMATION);
			return null;
		}
		if(!StringUtils.isNotEmpty(password.trim())){
			CommonDialog.showMessage(shlservice, "提示", "请输入密码!", SWT.ICON_INFORMATION);
			return null;
		}
		if(password.trim().length()<6){
			CommonDialog.showMessage(shlservice, "提示", "密码长度应大于6!", SWT.ICON_INFORMATION);
			return null;
		}
		assiger = new UserInfoPojo();
		assiger.setUser_name(name);
		assiger.setUser_password(password);
		return assiger;
	}
	
	private void init(){
		try {
			pathName = System.getProperty("user.home") + File.separator +"userinfo" + File.separator;
			properties = new Properties();
			map = new HashMap<>();
			file = new File(pathName);
			file.mkdirs();
			file = new File(pathName+fileName);
			if(!file.exists()){
				file.createNewFile();
			}else{
				properties.load(new FileInputStream(file));
				Set<String> set = properties.stringPropertyNames();
				if(set.size()>0){
					String name = null;
					String password = null;
					for (String string : set) {
						if (!isSelect.equals(string)) {
							name =  string;
							password = properties.getProperty(name);
							map.put(name, password);
						}
					}
					if("true".equalsIgnoreCase(properties.getProperty(isSelect))){
						btnCheckButton.setSelection(true);
						if(StringUtils.isNotEmpty(name) && StringUtils.isNotEmpty(password)){
							text_1.setText(name);
							txtAdmin.setText(password);
						}
					}else{
						btnCheckButton.setSelection(false);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	private void sendInfo(){
		try {
			properties.clear();
			if(btnCheckButton.getSelection()){
				String name = text_1.getText();
				String password = txtAdmin.getText();
				properties.setProperty(name,password);
				properties.setProperty(isSelect, "true");
			}else{
				properties.setProperty(isSelect, "false");
			}
			properties.store(new FileOutputStream(file,false), "");
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	
	
	
}
