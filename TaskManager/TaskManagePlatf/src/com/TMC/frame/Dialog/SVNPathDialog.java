package com.TMC.frame.Dialog;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.TMS.entity.TaskInfoPojo;
import com.TMS.model.TaskModel;
import com.config.Config.TASK_STATUS;
import com.configpath.ConfigPath;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.DisposeEvent;

public class SVNPathDialog extends Dialog {

	private Shell shlservice;
	private Text text_7;
	String svnpath;
	
	public SVNPathDialog(Shell parent) {
		super(parent);
	}

	public String open() {
		shlservice = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shlservice.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				svnpath = null;
			}
		});
		shlservice.setImage(new Image(shlservice.getDisplay(), ConfigPath.EditImagePath));
		shlservice.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND));
		shlservice.setSize(476, 358);
		shlservice.setText("请输入SVN路径");
		shlservice.setLocation(getParent().getLocation().x + getParent().getSize().x / 2 - shlservice.getSize().x / 2,
				getParent().getLocation().y + getParent().getSize().y / 2 - shlservice.getSize().y / 2);
		shlservice.setLayout(new FillLayout());

		Composite composite = new Composite(shlservice, SWT.BORDER);
		composite.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		composite.setLayout(null);

		Button cancelButton = new Button(composite, SWT.NONE);
		cancelButton.setBounds(193, 289, 80, 27);
		cancelButton.setImage(new Image(shlservice.getDisplay(), ConfigPath.OKImagePath));
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				svnpath = text_7.getText();
				if(svnpath==null || svnpath.isEmpty()){
					CommonDialog.showMessage(shlservice, "提示", "请输入SVN路径", SWT.ICON_INFORMATION);
					return;
				}
				shlservice.dispose();
			}
		});
		cancelButton.setText("确认");
		cancelButton.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		Label lblSvn = new Label(composite, SWT.NONE);
		lblSvn.setText("SVN路径");
		lblSvn.setFont(SWTResourceManager.getFont("楷体", 10, SWT.BOLD));
		lblSvn.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblSvn.setBounds(10, 10, 85, 13);
		
		text_7 = new Text(composite, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		text_7.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		text_7.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
		text_7.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		text_7.setBounds(10, 27, 446, 256);

		shlservice.layout();
		shlservice.open();
		Display display = shlservice.getDisplay();
		while (!shlservice.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return svnpath;
	}
	
	
}
