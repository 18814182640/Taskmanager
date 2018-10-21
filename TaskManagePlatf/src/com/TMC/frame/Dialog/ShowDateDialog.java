package com.TMC.frame.Dialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import com.configpath.ConfigPath;

public class ShowDateDialog extends Dialog {

	private Shell shlservice;
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	private DateTime dateTime;
	private Date date;

	/**
	 * @wbp.parser.constructor
	 */
	
	public ShowDateDialog(Shell parent) {
		super(parent);
	}
  
	public Date open() {
		shlservice = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shlservice.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				date = null;
			}
		});
		shlservice.setImage(new Image(shlservice.getDisplay(), ConfigPath.AddImagePath));
		shlservice.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		shlservice.setSize(323, 272);
		shlservice.setText("请选择时间");
		shlservice.setLocation(getParent().getLocation().x + getParent().getSize().x / 2 - shlservice.getSize().x / 2,
				getParent().getLocation().y + getParent().getSize().y / 2 - shlservice.getSize().y / 2);
		shlservice.setLayout(new FillLayout());

		Composite composite = new Composite(shlservice, SWT.BORDER);
		composite.setFont(SWTResourceManager.getFont("楷体", 11, SWT.NORMAL));
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		composite.setLayout(new FormLayout());

		Button okButton = new Button(composite, SWT.CENTER);
		okButton.setImage(new Image(shlservice.getDisplay(), ConfigPath.AddImagePath));
		okButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setDate();
				shlservice.dispose();
			}
		});
		FormData fd_okButton = new FormData();
		fd_okButton.bottom = new FormAttachment(100, -10);
		fd_okButton.left = new FormAttachment(50, -85);
		fd_okButton.right = new FormAttachment(50, -5);
		okButton.setLayoutData(fd_okButton);
		okButton.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		okButton.setText("OK");

		Button cancelButton = new Button(composite, SWT.CENTER);
		cancelButton.setImage(new Image(shlservice.getDisplay(), ConfigPath.RemoveImagePath));
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				date = null;
				shlservice.dispose();
			}
		});
		cancelButton.setText("Cancel");
		FormData fd_button = new FormData();
		fd_button.top = new FormAttachment(okButton, 0, SWT.TOP);
		fd_button.right = new FormAttachment(50, 80);
		fd_button.left = new FormAttachment(50, 5);
		cancelButton.setLayoutData(fd_button);
		cancelButton.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		
		dateTime = new DateTime(composite, SWT.CALENDAR);
		dateTime.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				setDate();
				shlservice.dispose();
			}
		});
		dateTime.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		FormData fd_dateTime_1 = new FormData();
		fd_dateTime_1.top = new FormAttachment(0, 0);
		fd_dateTime_1.bottom = new FormAttachment(okButton, -6);
		fd_dateTime_1.left = new FormAttachment(0, 0);
		fd_dateTime_1.right = new FormAttachment(100, 0);
		dateTime.setLayoutData(fd_dateTime_1);
		
	
		

		

		shlservice.layout();
		shlservice.open();
		Display display = shlservice.getDisplay();
		while (!shlservice.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return date;
	}

	private void setDate(){
		 try {
			date = format.parse(dateTime.getYear()+"-"+dateTime.getMonth()+"-"+dateTime.getDay());
		} catch (ParseException e) {
			e.printStackTrace();
			date = null;
			return ;
		}
	}
	
	
}
