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
import com.TMS.model.TaskModel;
import com.configpath.ConfigPath;

public class CreateItemDialog extends Dialog {

	private Shell shlservice;
	private TaskModel taskModel;
	private Text text;
	private Text text_1;
	private Combo combo;

	/**
	 * @wbp.parser.constructor
	 */
	public CreateItemDialog(Shell parent) {
		super(parent);
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
		shlservice.setSize(486, 197);
		shlservice.setText("新增项目");
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
				newTask();
			}
		});
		FormData fd_okButton = new FormData();
		fd_okButton.bottom = new FormAttachment(100, -10);
		fd_okButton.left = new FormAttachment(50, -85);
		fd_okButton.right = new FormAttachment(50, -5);
		okButton.setLayoutData(fd_okButton);
		okButton.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		okButton.setText("Add");

		Button cancelButton = new Button(composite, SWT.CENTER);
		cancelButton.setImage(new Image(shlservice.getDisplay(), ConfigPath.RemoveImagePath));
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				taskModel = null;
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
		
		Label label_2 = new Label(composite, SWT.NONE);
		label_2.setText("项目名:");
		label_2.setFont(SWTResourceManager.getFont("楷体", 11, SWT.BOLD));
		label_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		FormData fd_label_2 = new FormData();
		fd_label_2.top = new FormAttachment(0, 10);
		fd_label_2.left = new FormAttachment(0, 10);
		label_2.setLayoutData(fd_label_2);
		
		text = new Text(composite, SWT.BORDER);
		FormData fd_text = new FormData();
		fd_text.left = new FormAttachment(label_2, 18);
		fd_text.right = new FormAttachment(label_2, 396, SWT.RIGHT);
		fd_text.top = new FormAttachment(0, 6);
		text.setLayoutData(fd_text);
		
		Label label = new Label(composite, SWT.NONE);
		label.setText("项目描述:");
		label.setFont(SWTResourceManager.getFont("楷体", 11, SWT.BOLD));
		label.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		FormData fd_label = new FormData();
		fd_label.top = new FormAttachment(label_2, 22);
		fd_label.left = new FormAttachment(label_2, 0, SWT.LEFT);
		label.setLayoutData(fd_label);
		
		text_1 = new Text(composite, SWT.BORDER);
		FormData fd_text_1 = new FormData();
		fd_text_1.right = new FormAttachment(100, -10);
		fd_text_1.left = new FormAttachment(0, 88);
		fd_text_1.top = new FormAttachment(label, -3, SWT.TOP);
		text_1.setLayoutData(fd_text_1);
		
		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setText("所属部门:");
		label_1.setFont(SWTResourceManager.getFont("楷体", 11, SWT.BOLD));
		label_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		FormData fd_label_1 = new FormData();
		fd_label_1.top = new FormAttachment(label, 22);
		fd_label_1.left = new FormAttachment(label_2, 0, SWT.LEFT);
		label_1.setLayoutData(fd_label_1);
		
		combo = new Combo(composite, SWT.READ_ONLY);
		FormData fd_combo = new FormData();
		fd_combo.right = new FormAttachment(text, 0, SWT.RIGHT);
		fd_combo.left = new FormAttachment(text_1, 0, SWT.LEFT);
		fd_combo.top = new FormAttachment(text_1, 12);
		combo.setLayoutData(fd_combo);
		combo.setItems(DataCache.getDepartmentNames());
		

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

	private void newTask() {
		String name = text.getText();
		if(!StringUtils.isNotEmpty(name)){
			CommonDialog.showMessage(shlservice, "提示", "项目名不能为空!", SWT.ICON_INFORMATION);
			return;
		}
		String explain = text_1.getText();
		if(!StringUtils.isNotEmpty(explain)){
			CommonDialog.showMessage(shlservice, "提示", "项目说明不能为空!", SWT.ICON_INFORMATION);
			return;
		}
		int select = combo.getSelectionIndex();
		if(select<0){
			CommonDialog.showMessage(shlservice, "提示", "请选择所属部门!", SWT.ICON_INFORMATION);
			return;
		}
		int id = DataCache.getDepartmentID(select);
		ItemInfoPojo itemInfoPojo = new ItemInfoPojo();
		itemInfoPojo.setItemName(name);
		itemInfoPojo.setExplain(explain);
		itemInfoPojo.setDepartment_id(id);
		itemInfoPojo.setStatus(1);
		taskModel = new TaskModel();
		taskModel.setItemInfoPojo(itemInfoPojo);
		shlservice.dispose();
	}
	

}
