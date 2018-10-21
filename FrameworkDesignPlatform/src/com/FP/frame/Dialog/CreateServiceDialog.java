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

public class CreateServiceDialog extends Dialog {

	private Shell shlservice;
	private Table table_1, table_2;
	private Text text;
	private DataModel dataModel;
	private TableEditor tableEditor1, tableEditor2;
	private Text text1;
	private Combo combo;

	public CreateServiceDialog(Shell parent) {
		super(parent);
		dataModel = new FunctionModel();
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
		shlservice.setText("\u65B0\u5EFAService\u7AEF\u53E3");
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
				editTable1(point, tableEditor1, table_1);
			}
		});
		table_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND));
		FormData fd_table1 = new FormData();
		fd_table1.bottom = new FormAttachment(30, -16);
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
		fd_lblNewLabel_11.left = new FormAttachment(table_1, 0, SWT.LEFT);
		lblNewLabel_11.setLayoutData(fd_lblNewLabel_11);
		lblNewLabel_11.setText("Base Attribute:");

		TableColumn tblclmnNewColumn_21 = new TableColumn(table_1, SWT.CENTER);
		tblclmnNewColumn_21.setWidth(120);
		tblclmnNewColumn_21.setText("New Column");

		TableColumn tblclmnNewColumn_31 = new TableColumn(table_1, SWT.CENTER);
		tblclmnNewColumn_31.setWidth(178);
		tblclmnNewColumn_31.setText("New Column");

		Label lblNewLabel_111 = new Label(composite, SWT.NONE);
		lblNewLabel_111.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblNewLabel_111.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
		FormData fd_lblNewLabel_111 = new FormData();
		fd_lblNewLabel_111.top = new FormAttachment(table_1, 10);
		fd_lblNewLabel_111.left = new FormAttachment(table_1, 0, SWT.LEFT);
		lblNewLabel_111.setLayoutData(fd_lblNewLabel_111);
		lblNewLabel_111.setText("Parameters:");

		table_2 = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table_2.setToolTipText("\u6CE8:\u53F3\u51FB\u65B0\u5EFA\u4E00\u884C");
		table_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				Point point = new Point(e.x, e.y);
				editTable2(point, tableEditor2, table_2);
			}
		});
		table_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND));
		table_2.setHeaderVisible(true);
		FormData fd_table11 = new FormData();
		fd_table11.bottom = new FormAttachment(60, 13);
		fd_table11.left = new FormAttachment(0, 10);
		fd_table11.top = new FormAttachment(lblNewLabel_111, 3);
		fd_table11.right = new FormAttachment(100, -10);
		table_2.setLayoutData(fd_table11);
		table_2.setLinesVisible(true);

		Menu menu = new Menu(table_2);
		table_2.setMenu(menu);

		MenuItem mntmNewItem = new MenuItem(menu, SWT.NONE);
		mntmNewItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new TableItem(table_2, SWT.NONE).setText(0, table_2.getItemCount() + "");
			}
		});
		mntmNewItem.setText("\u65B0\u5EFAParam");

		TableColumn tblclmnNewColumn = new TableColumn(table_2, SWT.NONE);
		tblclmnNewColumn.setWidth(35);
		tblclmnNewColumn.setText("NO");

		TableColumn tblclmnNewColumn_211 = new TableColumn(table_2, SWT.CENTER);
		tblclmnNewColumn_211.setWidth(70);
		tblclmnNewColumn_211.setText("Name");

		TableColumn tblclmnNewColumn_4 = new TableColumn(table_2, SWT.CENTER);
		tblclmnNewColumn_4.setWidth(80);
		tblclmnNewColumn_4.setText("DataType");

		TableColumn tblclmnNewColumn_311 = new TableColumn(table_2, SWT.CENTER);
		tblclmnNewColumn_311.setWidth(112);
		tblclmnNewColumn_311.setText("Description");

		Label lblNewLabel_21 = new Label(composite, SWT.NONE);
		lblNewLabel_21.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblNewLabel_21.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
		FormData fd_lblNewLabel_21 = new FormData();
		fd_lblNewLabel_21.top = new FormAttachment(table_2, 10);
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
		tableEditor2 = new TableEditor(table_2);

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
			for (int i = 0; i < 4; i++) {
				TableItem tableItem = new TableItem(table_1, SWT.NONE);
				tableItem.setText(0, dataModel.getAttributeNames()[i]);
				if (i == 1) {
					tableItem.setText(1, Config.PortGroup.P_Port.getName());
				} else if (i == 2) {
					tableItem.setText(1, Config.PortAttribute.Service.getName());
				}
			}

		}
	}

	private void editTable1(Point point, TableEditor tableEditor, Table table) {
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

	private void editTable2(Point point, TableEditor tableEditor, Table table) {

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
		if (col < 1 || col > 3) {
			return;
		}

		if (col == 2) {
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
			dataModel = new FunctionModel();
		} else {
			for (int i = 0; i < table_1.getItemCount(); i++) {
				TableItem tableItem = table_1.getItem(i);
				String name = tableItem.getText(0);
				String value = tableItem.getText(1);
				if (value == null || value.isEmpty()) {
					CommonDialog.showMessage(shlservice, "提示", name + "不可为空!", SWT.ERROR);
					return false;
				}
				dataModel.setAttributeValue(name, value);
			}
			StringBuffer param = new StringBuffer();
			for (int i = 0; i < table_2.getItemCount(); i++) {
				TableItem tableItem = table_2.getItem(i);
				String name = tableItem.getText(1);
				String dataType = tableItem.getText(2);
				String description = tableItem.getText(3);
				if (name == null || name.isEmpty()) {
					CommonDialog.showMessage(shlservice, "提示", "Parameters的Name不可为空!", SWT.ERROR);
					return false;
				}
				if (dataType == null || dataType.isEmpty()) {
					CommonDialog.showMessage(shlservice, "提示", "Parameters的DataType不可为空!", SWT.ERROR);
					return false;
				}
				param.append(dataType).append(" ").append(name);
				if (description != null && !description.isEmpty()) {
					param.append(" ").append(description);
				}
				param.append(";");
			}
			dataModel.setAttributeValue("Parameters", param.toString());
			String description = text.getText();
			if (description != null && !description.isEmpty()) {
				dataModel.setAttributeValue("Description", description);
			}
			dataModel.setFalg(Config.NEW);
		}
		return true;
	}

}
