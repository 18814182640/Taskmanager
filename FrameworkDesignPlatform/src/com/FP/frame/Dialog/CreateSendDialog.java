package com.FP.frame.Dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.FP.frame.Config;
import com.PD.cache.CacheData;
import com.PD.model.DataModel;
import com.PD.model.FunctionModel;
import com.PD.model.SignalModel;

import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;

public class CreateSendDialog extends Dialog {

	private Shell shlservice;
	private Table table_1;
	private Text text;
	private DataModel dataModel;
	private TableEditor tableEditor1, tableEditor2;
	private Text text1;
	private Combo combo;

	public CreateSendDialog(Shell parent) {
		super(parent);
		dataModel = new SignalModel();
	}

	@SuppressWarnings("deprecation")
	public DataModel open() {
		shlservice = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.MAX | SWT.RESIZE);
		shlservice.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				dataModel = null;
			}
		});
		shlservice.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
		shlservice.setSize(342, 490);
		shlservice.setImage(new Image(shlservice.getDisplay(), Config.SWCImagePath));
		shlservice.setText("\u65B0\u5EFASend\u7AEF\u53E3");
		shlservice.setLocation(getParent().getLocation().x + getParent().getSize().x / 2 - shlservice.getSize().x / 2,
				getParent().getLocation().y + getParent().getSize().y / 2 - shlservice.getSize().y / 2);
		shlservice.setLayout(new FillLayout());

		// 函数
		Composite composite = new Composite(shlservice, SWT.BORDER);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		composite.setLayout(new FormLayout());

		table_1 = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				int select = table_1.getSelectionIndex();
				if (select == 1 || select == 2) {
					return;
				}
				Point point = new Point(e.x, e.y);
				editTable(point, tableEditor1, table_1);
			}
		});
		table_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND));
		FormData fd_table1 = new FormData();
		fd_table1.left = new FormAttachment(0, 10);
		fd_table1.top = new FormAttachment(0, 30);
		fd_table1.right = new FormAttachment(100, -10);
		table_1.setLayoutData(fd_table1);
		table_1.setLinesVisible(true);

		Label lblNewLabel_11 = new Label(composite, SWT.NONE);
		lblNewLabel_11.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblNewLabel_11.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
		FormData fd_lblNewLabel_11 = new FormData();
		fd_lblNewLabel_11.bottom = new FormAttachment(table_1, -3);
		fd_lblNewLabel_11.left = new FormAttachment(0, 10);
		lblNewLabel_11.setLayoutData(fd_lblNewLabel_11);
		lblNewLabel_11.setText("Base Attribute:");

		TableColumn tblclmnNewColumn_21 = new TableColumn(table_1, SWT.CENTER);
		tblclmnNewColumn_21.setWidth(120);
		tblclmnNewColumn_21.setText("New Column");

		TableColumn tblclmnNewColumn_31 = new TableColumn(table_1, SWT.CENTER);
		tblclmnNewColumn_31.setWidth(178);
		tblclmnNewColumn_31.setText("New Column");

		Label lblNewLabel_21 = new Label(composite, SWT.NONE);
		fd_table1.bottom = new FormAttachment(lblNewLabel_21, -6);
		lblNewLabel_21.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblNewLabel_21.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
		FormData fd_lblNewLabel_21 = new FormData();
		fd_lblNewLabel_21.top = new FormAttachment(0, 291);
		fd_lblNewLabel_21.left = new FormAttachment(0, 10);
		lblNewLabel_21.setLayoutData(fd_lblNewLabel_21);
		lblNewLabel_21.setText("Description:");

		text = new Text(composite, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		text.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND));
		text.setEditable(true);
		FormData fd_text_11 = new FormData();
		fd_text_11.top = new FormAttachment(lblNewLabel_21, 3);
		fd_text_11.left = new FormAttachment(0, 10);
		fd_text_11.right = new FormAttachment(100, -10);
		fd_text_11.bottom = new FormAttachment(100, -50);
		text.setLayoutData(fd_text_11);

		Button okButton = new Button(composite, SWT.NONE);
		okButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (createDataModel(dataModel)) {
					shlservice.dispose();
				}
			}
		});
		FormData fd_okButton = new FormData();
		fd_okButton.bottom = new FormAttachment(100, -10);
		fd_okButton.left = new FormAttachment(50, -85);
		fd_okButton.right = new FormAttachment(50, -5);
		okButton.setLayoutData(fd_okButton);
		okButton.setImage(new Image(shlservice.getDisplay(), Config.AddImagePath));
		okButton.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		okButton.setText("OK");

		Button cancelButton = new Button(composite, SWT.NONE);
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dataModel = null;
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
		cancelButton.setImage(new Image(shlservice.getDisplay(), Config.RemoveImagePath));

		tableEditor1 = new TableEditor(table_1);

		initData();
		shlservice.layout();
		shlservice.open();
		Display display = shlservice.getDisplay();
		while (!shlservice.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return dataModel;
	}

	private void initData() {
		if (dataModel != null) {
			for (int i = 0; i < dataModel.getArributeNum() - 1; i++) {
				TableItem tableItem = new TableItem(table_1, SWT.NONE);
				tableItem.setText(0, dataModel.getAttributeNames()[i]);
				if (i == 1) {
					tableItem.setText(1, Config.PortGroup.P_Port.getName());
				} else if (i == 2) {
					tableItem.setText(1, Config.PortAttribute.Send.getName());
				}
			}

		}
	}

	private void editTable(Point point, TableEditor tableEditor, Table table) {

		Control old = tableEditor.getEditor();
		if (old != null) {
			old.dispose();
		}
		final TableItem item = table.getItem(point);
		if (item == null) {
			return;
		}
		int col = -1;
		for (int i = 0; i < table.getColumnCount(); i++) {
			Rectangle rectang = item.getBounds(i);// 矩阵
			if (rectang.contains(point)) {
				col = i;
				break;
			}
		}
		if (col != 1) {
			return;
		}

		int selecte = table.getSelectionIndex();
		if (selecte == 3) {
			combo = new Combo(table, SWT.READ_ONLY);
			combo.setItems(CacheData.getAllDataType());
			tableEditor.grabHorizontal = true;
			tableEditor.minimumWidth = combo.getBounds().width;
			tableEditor.setEditor(combo, item, col);
			final int pos = col;
			combo.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					item.setText(pos, combo.getText());
					combo.dispose();
				}
			});
		} else {
			text1 = new Text(table, SWT.NONE);
			text1.setText(item.getText(col));
			text1.setForeground(item.getForeground());
			text1.selectAll();
			text1.setFocus();
			tableEditor.grabHorizontal = true;
			tableEditor.minimumWidth = text1.getBounds().width;
			tableEditor.setEditor(text1, item, col);
			final int pos = col;
			text1.addFocusListener(new FocusListener() {
				@Override
				public void focusLost(FocusEvent e) {
					item.setText(pos, text1.getText());
					text1.dispose();
				}

				@Override
				public void focusGained(FocusEvent e) {
				}
			});
		}
	}

	private boolean createDataModel(DataModel dataModel) {
		if (dataModel == null) {
			dataModel = new SignalModel();
		} else {
			for (int i = 0; i < table_1.getItemCount(); i++) {
				TableItem tableItem = table_1.getItem(i);
				String name = tableItem.getText(0);
				String value = tableItem.getText(1);
				if (value == null || value.isEmpty()) {
					CommonDialog.showMessage(shlservice, "提示", name + "不可为空!", SWT.ERROR);
					return false;
				}
				if ((i > 4 && i < 7) || (i < table_1.getItemCount() && i >= table_1.getItemCount() - 2)) {
					try {
						if (value.contains(".")) {
							Double data = Double.parseDouble(value);
							dataModel.setAttributeValue(name, data);
						} else {
							int data = Integer.parseInt(value);
							dataModel.setAttributeValue(name, data);
						}
					} catch (Exception e) {
						CommonDialog.showMessage(shlservice, "提示", name + "输入格式有误!", SWT.ERROR);
					}
				} else {
					dataModel.setAttributeValue(name, value);
				}
			}

			String description = text.getText();
			if (description != null && !description.isEmpty()) {
				dataModel.setAttributeValue("Description", description);
			}
		}
		return true;
	}

}
