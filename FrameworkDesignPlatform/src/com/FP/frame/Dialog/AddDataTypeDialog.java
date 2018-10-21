package com.FP.frame.Dialog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.FD.model.DataModel.DataType;
import com.FD.model.DataModel.EnumType;
import com.FD.model.DataModel.FixdtType;
import com.FD.model.DataModel.StructType;
import com.FD.model.DataModel.StructType.Elenment;
import com.FP.frame.Config;
import com.PD.Task.SaveDataTypeOnFileTask;
import com.PD.Thread.CellBack;
import com.PD.Thread.QueueThread;
import com.PD.Thread.Result;
import com.PD.Tool.DataUnit;
import com.PD.Tool.FileTool;

public class AddDataTypeDialog extends Dialog {

	private Shell shlservice;
	private Set<DataType> set;
	private Table table;
	private Text text;
	private Combo combo, combo_1, combo_2, editCombp;
	private Composite compositebase, composite_1, composite_2, composite_3;
	private StackLayout stackLayout;
	private Text text_1;
	private Text text_2;
	private Text text_3;
	private Table table_1;
	private TableEditor tableEditor_1;
	private Table table_2;
	private TableEditor tableEditor_2;
	private Text text1;

	public AddDataTypeDialog(Shell parent, Set<DataType> set) {
		super(parent);
		this.set = set;
	}

	public void open() {
		shlservice = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shlservice.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {

			}
		});
		shlservice.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		shlservice.setSize(967, 490);
		shlservice.setImage(new Image(shlservice.getDisplay(), Config.AddImagePath));
		shlservice.setText("\u81EA\u5B9A\u4E49\u6570\u636E\u7C7B\u578B\u7BA1\u7406");
		shlservice.setLocation(getParent().getLocation().x + getParent().getSize().x / 2 - shlservice.getSize().x / 2,
				getParent().getLocation().y + getParent().getSize().y / 2 - shlservice.getSize().y / 2);
		shlservice.setLayout(new FillLayout());

		Composite composite = new Composite(shlservice, SWT.BORDER);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		composite.setLayout(new FormLayout());

		Group group = new Group(composite, SWT.BORDER);
		group.setText("\u6570\u636E\u7C7B\u578B\u6D4F\u89C8\u5668");
		group.setLayout(new FormLayout());
		FormData fd_group = new FormData();
		fd_group.top = new FormAttachment(0, 0);
		fd_group.left = new FormAttachment(0, 0);
		fd_group.right = new FormAttachment(60, 0);
		fd_group.bottom = new FormAttachment(100, 0);
		group.setLayoutData(fd_group);

		table = new Table(group, SWT.BORDER | SWT.FULL_SELECTION);
		FormData fd_table = new FormData();
		fd_table.top = new FormAttachment(0, 0);
		fd_table.left = new FormAttachment(0, 0);
		fd_table.right = new FormAttachment(100, 0);
		fd_table.bottom = new FormAttachment(100, 0);
		table.setLayoutData(fd_table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		Menu menu = new Menu(table);
		table.setMenu(menu);

		MenuItem mntmNewItem_4 = new MenuItem(menu, SWT.NONE);
		mntmNewItem_4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = table.getSelectionIndex();
				if (index >= 0) {
					TableItem item = table.getItem(table.getSelectionIndex());
					if (item != null) {
						set.remove(item.getData());
						showAllDataType();
						SaveDataTypeOnFileTask sendDataTypeOnFileTask = new SaveDataTypeOnFileTask(set,
								Config.DataTypeFilePath, new CellBack() {
							@Override
							public void cellback(Result result) {
							}
						});
						QueueThread.getInstance().AddThreadInQueue(sendDataTypeOnFileTask);
					} else {
						CommonDialog.showMessage(shlservice, "提示", "请先选择！", SWT.ICON_INFORMATION);
					}
				}

			}
		});
		mntmNewItem_4.setText("\u5220\u9664");

		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.CENTER);
		tblclmnNewColumn.setWidth(120);
		tblclmnNewColumn.setText("\u540D\u79F0");

		TableColumn tableColumn = new TableColumn(table, SWT.CENTER);
		tableColumn.setWidth(120);
		tableColumn.setText("\u7C7B\u578B");

		TableColumn tableColumn_1 = new TableColumn(table, SWT.CENTER);
		tableColumn_1.setWidth(313);
		tableColumn_1.setText("\u63CF\u8FF0");

		Group group_1 = new Group(composite, SWT.BORDER);
		group_1.setText("\u6570\u636E\u7C7B\u578B\u7F16\u8F91");
		group_1.setLayout(new FormLayout());
		FormData fd_group_1 = new FormData();
		fd_group_1.bottom = new FormAttachment(100, 0);
		fd_group_1.right = new FormAttachment(100, 0);
		fd_group_1.top = new FormAttachment(0, 0);
		fd_group_1.left = new FormAttachment(60, 0);
		group_1.setLayoutData(fd_group_1);

		Group group_2 = new Group(group_1, SWT.NONE);
		group_2.setText("\u57FA\u7840\u5C5E\u6027:");
		group_2.setLayout(new FormLayout());
		FormData fd_group_2 = new FormData();
		fd_group_2.top = new FormAttachment(0, 10);
		fd_group_2.left = new FormAttachment(0, 20);
		fd_group_2.right = new FormAttachment(100, -20);
		fd_group_2.bottom = new FormAttachment(20, 0);
		group_2.setLayoutData(fd_group_2);

		Label lblNewLabel = new Label(group_2, SWT.NONE);
		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.top = new FormAttachment(50, -10);
		fd_lblNewLabel.left = new FormAttachment(0, 30);
		fd_lblNewLabel.right = new FormAttachment(0, 60);
		fd_lblNewLabel.bottom = new FormAttachment(50, 10);
		lblNewLabel.setLayoutData(fd_lblNewLabel);
		lblNewLabel.setText("\u540D\u79F0:");

		text = new Text(group_2, SWT.BORDER);
		FormData fd_text = new FormData();
		fd_text.right = new FormAttachment(lblNewLabel, 90, SWT.RIGHT);
		fd_text.top = new FormAttachment(lblNewLabel, -1, SWT.TOP);
		fd_text.left = new FormAttachment(lblNewLabel, 5);
		text.setLayoutData(fd_text);

		Label label = new Label(group_2, SWT.NONE);
		label.setText("\u6A21\u677F:");
		FormData fd_label = new FormData();
		fd_label.top = new FormAttachment(text, 3, SWT.TOP);
		fd_label.right = new FormAttachment(100, -119);
		label.setLayoutData(fd_label);

		combo = new Combo(group_2, SWT.READ_ONLY);
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = combo.getSelectionIndex();
				switch (index) {
				case 0:
					stackLayout.topControl = composite_1;
					clearALL();
					compositebase.layout();
					break;
				case 1:
					stackLayout.topControl = composite_2;
					clearALL();
					compositebase.layout();
					break;
				case 2:
					stackLayout.topControl = composite_3;
					clearALL();
					compositebase.layout();
					break;
				default:
					break;
				}
			}
		});
		FormData fd_text_1 = new FormData();
		fd_text_1.top = new FormAttachment(label, -3, SWT.TOP);
		fd_text_1.left = new FormAttachment(label, 5);
		fd_text_1.right = new FormAttachment(100, -29);
		combo.setLayoutData(fd_text_1);
		combo.setItems(new String[] { Config.FixdtType, Config.StructType, Config.EnumTyep });
		combo.select(0);

		stackLayout = new StackLayout();
		compositebase = new Composite(group_1, SWT.NONE);
		compositebase.setLayout(stackLayout);
		FormData fd_group_3 = new FormData();
		fd_group_3.top = new FormAttachment(20, 10);
		fd_group_3.left = new FormAttachment(0, 20);
		fd_group_3.right = new FormAttachment(100, -20);
		fd_group_3.bottom = new FormAttachment(100, -70);
		compositebase.setLayoutData(fd_group_3);

		// 定点类型
		composite_1 = new Composite(compositebase, SWT.NONE);
		FormData fd_composite_1 = new FormData();
		fd_composite_1.top = new FormAttachment(group_2, 10);
		fd_composite_1.left = new FormAttachment(0, 20);
		fd_composite_1.right = new FormAttachment(100, -20);
		compositebase.setLayoutData(fd_composite_1);
		composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));
		Group group_3 = new Group(composite_1, SWT.NONE);
		group_3.setText("\u6269\u5C55\u5C5E\u6027:");
		group_3.setLayout(new FormLayout());

		Label label_1 = new Label(group_3, SWT.NONE);
		label_1.setText("\u7B26\u53F7:");
		FormData fd_label_1 = new FormData();
		fd_label_1.top = new FormAttachment(0, 34);
		fd_label_1.left = new FormAttachment(0, 30);
		fd_label_1.right = new FormAttachment(0, 60);
		label_1.setLayoutData(fd_label_1);

		combo_1 = new Combo(group_3, SWT.READ_ONLY);
		combo_1.setItems(new String[] { "\u5B9A\u70B9\u7C7B\u578B", "\u7ED3\u6784\u4F53\u7C7B\u578B",
				"\u679A\u4E3E\u7C7B\u578B" });
		FormData fd_combo_1 = new FormData();
		fd_combo_1.top = new FormAttachment(0, 30);
		fd_combo_1.left = new FormAttachment(0, 67);
		fd_combo_1.right = new FormAttachment(0, 151);
		combo_1.setLayoutData(fd_combo_1);
		combo_1.setItems(Config.Stmol);

		Label label_2 = new Label(group_3, SWT.NONE);
		label_2.setText("\u659C\u7387:");
		FormData fd_label_2 = new FormData();
		fd_label_2.left = new FormAttachment(0, 181);
		label_2.setLayoutData(fd_label_2);

		text_1 = new Text(group_3, SWT.BORDER);
		text_1.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				String temp = text_1.getText();
				try {
					if (temp != null && !temp.isEmpty()) {
						if (temp.contains(".")) {
							Double.parseDouble(temp);
						} else {
							Integer.parseInt(temp);
						}
					}
				} catch (Exception e2) {
					CommonDialog.showMessage(shlservice, "提示", "斜率只能为数字!", SWT.ERROR);
				}
			}
		});
		fd_label_2.top = new FormAttachment(text_1, 3, SWT.TOP);
		FormData fd_text_11 = new FormData();
		fd_text_11.top = new FormAttachment(0, 30);
		fd_text_11.right = new FormAttachment(100, -30);
		fd_text_11.left = new FormAttachment(label_2, 5);
		text_1.setLayoutData(fd_text_11);

		Label label_3 = new Label(group_3, SWT.NONE);
		label_3.setText("\u957F\u5EA6:");
		FormData fd_label_3 = new FormData();
		fd_label_3.top = new FormAttachment(label_1, 29);
		fd_label_3.left = new FormAttachment(label_1, 0, SWT.LEFT);
		label_3.setLayoutData(fd_label_3);

		combo_2 = new Combo(group_3, SWT.READ_ONLY);
		combo_2.setItems(new String[] { "8", "16", "32", "64" });
		FormData fd_combo_2 = new FormData();
		fd_combo_2.right = new FormAttachment(combo_1, 0, SWT.RIGHT);
		fd_combo_2.left = new FormAttachment(combo_1, -84);
		fd_combo_2.top = new FormAttachment(label_3, -4, SWT.TOP);
		combo_2.setLayoutData(fd_combo_2);

		Label label_4 = new Label(group_3, SWT.NONE);
		label_4.setText("\u504F\u79FB:");
		FormData fd_label_4 = new FormData();
		fd_label_4.top = new FormAttachment(label_3, 0, SWT.TOP);
		fd_label_4.left = new FormAttachment(label_2, 0, SWT.LEFT);
		label_4.setLayoutData(fd_label_4);

		text_2 = new Text(group_3, SWT.BORDER);
		text_2.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				String temp = text_2.getText();
				try {
					if (temp != null && !temp.isEmpty()) {
						if (temp.contains(".")) {
							Double.parseDouble(temp);
						} else {
							Integer.parseInt(temp);
						}
					}
				} catch (Exception e2) {
					CommonDialog.showMessage(shlservice, "提示", "偏移只能为数字!", SWT.ERROR);
				}
			}
		});
		FormData fd_text_2 = new FormData();
		fd_text_2.right = new FormAttachment(text_1, 0, SWT.RIGHT);
		fd_text_2.left = new FormAttachment(text_1, -84);
		fd_text_2.top = new FormAttachment(label_4, -3, SWT.TOP);
		text_2.setLayoutData(fd_text_2);

		// 结构体类型
		composite_2 = new Composite(compositebase, SWT.NONE);
		composite_2.setLayout(new FillLayout(SWT.HORIZONTAL));

		Group group_4 = new Group(composite_2, SWT.NONE);
		group_4.setText("\u5143\u7D20\u7BA1\u7406:");
		group_4.setLayout(new FillLayout());

		table_1 = new Table(group_4, SWT.BORDER | SWT.FULL_SELECTION);
		table_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				Point point = new Point(e.x, e.y);
				if (point != null) {
					editTable(point, tableEditor_1, table_1);
				}
			}
		});

		table_1.setHeaderVisible(true);
		table_1.setLinesVisible(true);

		Menu menu1 = new Menu(table_1);
		table_1.setMenu(menu1);

		tableEditor_1 = new TableEditor(table_1);

		MenuItem mntmNewItem = new MenuItem(menu1, SWT.NONE);
		mntmNewItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem tableItem = new TableItem(table_1, SWT.NONE);
				table_1.setSelection(tableItem);
			}
		});
		mntmNewItem.setText("\u6DFB\u52A0");

		MenuItem mntmNewItem_1 = new MenuItem(menu1, SWT.NONE);
		mntmNewItem_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = table_1.getSelectionIndex();
				if (index >= 0) {
					table_1.remove(index);
				}
			}
		});
		mntmNewItem_1.setText("\u5220\u9664");

		TableColumn tblclmnNewColumn_1 = new TableColumn(table_1, SWT.CENTER);
		tblclmnNewColumn_1.setWidth(70);
		tblclmnNewColumn_1.setText("\u5143\u7D20\u540D\u79F0");

		TableColumn tableColumn_2 = new TableColumn(table_1, SWT.CENTER);
		tableColumn_2.setWidth(90);
		tableColumn_2.setText("\u6570\u636E\u7C7B\u578B");

		TableColumn tableColumn_3 = new TableColumn(table_1, SWT.CENTER);
		tableColumn_3.setWidth(80);
		tableColumn_3.setText("\u7EF4\u6570");

		TableColumn tableColumn_4 = new TableColumn(table_1, SWT.CENTER);
		tableColumn_4.setWidth(80);
		tableColumn_4.setText("\u63CF\u8FF0");

		FormData fd_composite_2 = new FormData();
		fd_composite_2.top = new FormAttachment(20, 10);
		fd_composite_2.left = new FormAttachment(0, 20);
		fd_composite_2.right = new FormAttachment(100, -20);
		fd_composite_2.bottom = new FormAttachment(100, -42);
		compositebase.setLayoutData(fd_composite_1);

		// 枚举类型
		composite_3 = new Composite(compositebase, SWT.NONE);
		composite_3.setLayout(new FillLayout(SWT.HORIZONTAL));

		Group group_5 = new Group(composite_3, SWT.NONE);
		group_5.setText("\u5143\u7D20\u7BA1\u7406:");
		group_5.setLayout(new FillLayout(SWT.HORIZONTAL));

		table_2 = new Table(group_5, SWT.BORDER | SWT.FULL_SELECTION);
		table_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				Point point = new Point(e.x, e.y);
				if (point != null) {
					editTable1(point, tableEditor_2, table_2);
				}
			}
		});
		table_2.setHeaderVisible(true);
		table_2.setLinesVisible(true);

		Menu menu2 = new Menu(table_2);
		table_2.setMenu(menu2);

		tableEditor_2 = new TableEditor(table_2);

		MenuItem mntmNewItem_2 = new MenuItem(menu2, SWT.NONE);
		mntmNewItem_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem tableItem = new TableItem(table_2, SWT.NONE);
				table_2.setSelection(tableItem);
			}
		});
		mntmNewItem_2.setText("\u6DFB\u52A0");

		MenuItem mntmNewItem_3 = new MenuItem(menu2, SWT.NONE);
		mntmNewItem_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = table_2.getSelectionIndex();
				if (index >= 0) {
					table_2.remove(index);
				}
			}
		});
		mntmNewItem_3.setText("\u5220\u9664");

		TableColumn tblclmnNewColumn_2 = new TableColumn(table_2, SWT.CENTER);
		tblclmnNewColumn_2.setWidth(70);
		tblclmnNewColumn_2.setText("\u5143\u7D20\u540D\u79F0");

		TableColumn tblclmnNewColumn_4 = new TableColumn(table_2, SWT.CENTER);
		tblclmnNewColumn_4.setWidth(80);
		tblclmnNewColumn_4.setText("\u521D\u503C");

		TableColumn tableColumn_5 = new TableColumn(table_2, SWT.CENTER);
		tableColumn_5.setWidth(80);
		tableColumn_5.setText("\u63CF\u8FF0");

		Button btnOk = new Button(group_1, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = combo.getSelectionIndex();
				createDatatype(index);
			}
		});
		btnOk.setText("Apple");
		btnOk.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		btnOk.setImage(new Image(shlservice.getDisplay(), Config.AddImagePath));
		FormData fd_btnOk = new FormData();
		fd_btnOk.top = new FormAttachment(100, -30);
		fd_btnOk.left = new FormAttachment(50, -100);
		fd_btnOk.right = new FormAttachment(50, -10);
		btnOk.setLayoutData(fd_btnOk);

		Button btnCancel = new Button(group_1, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlservice.dispose();
			}
		});
		btnCancel.setText("Cancel");
		btnCancel.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		btnCancel.setImage(new Image(shlservice.getDisplay(), Config.RemoveImagePath));
		FormData fd_btnCancel = new FormData();
		fd_btnCancel.top = new FormAttachment(100, -30);
		fd_btnCancel.left = new FormAttachment(50, 10);
		fd_btnCancel.right = new FormAttachment(50, 100);
		btnCancel.setLayoutData(fd_btnCancel);

		Label label_5 = new Label(group_1, SWT.NONE);
		fd_composite_1.bottom = new FormAttachment(label_5, -6);
		label_5.setText("\u63CF\u8FF0:");
		FormData fd_label_5 = new FormData();
		fd_label_5.left = new FormAttachment(group_2, 0, SWT.LEFT);
		label_5.setLayoutData(fd_label_5);

		text_3 = new Text(group_1, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		fd_label_5.bottom = new FormAttachment(text_3, -6);
		FormData fd_text_3 = new FormData();
		fd_text_3.bottom = new FormAttachment(btnOk, -17);
		fd_text_3.top = new FormAttachment(0, 293);
		fd_text_3.right = new FormAttachment(group_2, 0, SWT.RIGHT);
		fd_text_3.left = new FormAttachment(group_2, 0, SWT.LEFT);
		text_3.setLayoutData(fd_text_3);

		stackLayout.topControl = composite_1;

		showAllDataType();

		shlservice.layout();
		shlservice.open();
		Display display = shlservice.getDisplay();
		while (!shlservice.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
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
		if (col < 0 || col > 3) {
			return;
		}

		if (col == 1) {
			editCombp = new Combo(table, SWT.READ_ONLY);
			editCombp.setItems(Config.BaseType);
			tableEditor.grabHorizontal = true;
			tableEditor.minimumWidth = combo.getBounds().width;
			tableEditor.setEditor(editCombp, item, col);
			final int pos = col;
			editCombp.addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(ModifyEvent e) {
					item.setText(pos, editCombp.getText());
					editCombp.dispose();
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
			tableEditor.minimumHeight = text1.getBounds().height;
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
		if (col < 0 || col > 3) {
			return;
		}

		text1 = new Text(table, SWT.NONE);
		text1.setText(item.getText(col));
		text1.setForeground(item.getForeground());
		text1.selectAll();
		text1.setFocus();
		tableEditor.grabHorizontal = true;
		tableEditor.minimumWidth = text1.getBounds().width;
		tableEditor.minimumHeight = text1.getBounds().height;
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

	private void clearALL() {
		cleanFrame1();
		cleanFrame2();
		cleanFrame3();
	}

	private void cleanFrame1() {
		text.setText("");
		combo_1.deselectAll();
		text_1.setText("");
		combo_2.deselectAll();
		text_2.setText("");
		text_3.setText("");
	}

	private void cleanFrame2() {
		text.setText("");
		table_1.removeAll();
		text_3.setText("");
	}

	private void cleanFrame3() {
		text.setText("");
		table_2.removeAll();
		text_3.setText("");
	}

	private void createDatatype(int index) {
		switch (index) {
		case 0:
			if (CheckFixdtType()) {
				set.add(createFixdtType());
				cleanFrame1();
			} else {
				return;
			}
			break;
		case 1:
			if (CheckStructType()) {
				set.add(createStructType());
				cleanFrame2();
			} else {
				return;
			}
			break;
		case 2:
			if (CheckEnumType()) {
				set.add(createEnumType());
				cleanFrame3();
			} else {
				return;
			}
			break;
		default:
			break;
		}
		showAllDataType();
		SaveDataTypeOnFileTask sendDataTypeOnFileTask = new SaveDataTypeOnFileTask(set, Config.DataTypeFilePath,
				new CellBack() {
					@Override
					public void cellback(Result result) {
					}
				});
		QueueThread.getInstance().AddThreadInQueue(sendDataTypeOnFileTask);
	}

	private boolean CheckFixdtType() {
		if (text.getText() == null || text.getText().isEmpty()) {
			CommonDialog.showMessage(shlservice, "提示", "名称不可为空!", SWT.ERROR);
			return false;
		}
		if (combo_1.getSelectionIndex() < 0) {
			CommonDialog.showMessage(shlservice, "提示", "请选择符号!", SWT.ERROR);
			return false;
		}
		if (text_1.getText() == null || text_1.getText().isEmpty()) {
			CommonDialog.showMessage(shlservice, "提示", "斜率不可为空!", SWT.ERROR);
			return false;
		}
		if (combo_2.getSelectionIndex() < 0) {
			CommonDialog.showMessage(shlservice, "提示", "请选择长度!", SWT.ERROR);
			return false;
		}
		if (text_2.getText() == null || text_2.getText().isEmpty()) {
			CommonDialog.showMessage(shlservice, "提示", "偏移不可为空!", SWT.ERROR);
			return false;
		}
		return true;
	}

	private boolean CheckStructType() {
		if (text.getText() == null || text.getText().isEmpty()) {
			CommonDialog.showMessage(shlservice, "提示", "名称不可为空!", SWT.ERROR);
			return false;
		}
		for (int i = 0; i < table_1.getItemCount(); i++) {
			TableItem item = table_1.getItem(i);
			for (int j = 0; j < 3; j++) {
				if (item.getText(j) == null || item.getText(j).isEmpty()) {
					CommonDialog.showMessage(shlservice, "提示", "元素名称,数据类型或初值均不可为空!", SWT.ERROR);
					return false;
				}
			}
		}
		return true;
	}

	private boolean CheckEnumType() {
		if (text.getText() == null || text.getText().isEmpty()) {
			CommonDialog.showMessage(shlservice, "提示", "名称不可为空!", SWT.ERROR);
			return false;
		}
		for (int i = 0; i < table_2.getItemCount(); i++) {
			TableItem item = table_2.getItem(i);
			for (int j = 0; j < 3; j++) {
				if (item.getText(j) == null || item.getText(j).isEmpty()) {
					CommonDialog.showMessage(shlservice, "提示", "元素名称,数据类型或维数均不可为空!", SWT.ERROR);
					return false;
				}
			}
		}
		return true;
	}

	private DataType createFixdtType() {
		FixdtType fixdtType = new FixdtType();
		fixdtType.setShortName(text.getText());
		fixdtType.setSymbol(combo_1.getItem(combo_1.getSelectionIndex()));
		fixdtType.setSlope(DataUnit.getNumber(text_1.getText()));
		fixdtType.setOffset(DataUnit.getNumber(text_2.getText()));
		fixdtType.setLength(Integer.parseInt(combo_2.getItem(combo_2.getSelectionIndex())));
		fixdtType.setDescription(text_3.getText());
		return fixdtType;
	}

	private DataType createStructType() {
		StructType structType = new StructType();
		structType.setShortName(text.getText());
		List<Elenment> elenments = new ArrayList<>();
		for (int i = 0; i < table_1.getItemCount(); i++) {
			TableItem item = table_1.getItem(i);
			if (item != null) {
				Elenment elenment = structType.new Elenment();
				elenment.setShortName(item.getText(0));
				elenment.setDataType(item.getText(1));
				elenment.setDimension(item.getText(2));
				elenment.setDescription(item.getText(3));
				elenments.add(elenment);
			}
		}
		structType.setElenments(elenments);
		structType.setDescription(text_3.getText());
		return structType;
	}

	private DataType createEnumType() {
		EnumType enumType = new EnumType();
		enumType.setShortName(text.getText());
		List<com.FD.model.DataModel.EnumType.Elenment> elenments = new ArrayList<>();
		for (int i = 0; i < table_2.getItemCount(); i++) {
			TableItem item = table_2.getItem(i);
			if (item != null) {
				com.FD.model.DataModel.EnumType.Elenment elenment = enumType.new Elenment();
				elenment.setShortName(item.getText(0));
				elenment.setInitValue(item.getText(2));
				elenment.setDescription(item.getText(3));
				elenments.add(elenment);
			}
		}
		enumType.setElenments(elenments);
		enumType.setDescription(text_3.getText());
		return enumType;
	}

	private void showAllDataType() {
		table.removeAll();
		Iterator<DataType> iterator = set.iterator();
		while (iterator.hasNext()) {
			DataType dataType = (DataType) iterator.next();
			if (dataType != null) {
				TableItem tableItem = new TableItem(table, SWT.NONE);
				switch (dataType.getType()) {
				case Config.FixdtType:
					FixdtType fixdtType = (FixdtType) dataType;
					tableItem.setText(0, fixdtType.getShortName());
					tableItem.setText(1, fixdtType.getType());
					tableItem.setText(2, fixdtType.getDescription());
					break;
				case Config.StructType:
					StructType structType = (StructType) dataType;
					tableItem.setText(0, structType.getShortName());
					tableItem.setText(1, structType.getType());
					tableItem.setText(2, structType.getDescription());
					break;
				case Config.EnumTyep:
					EnumType enumType = (EnumType) dataType;
					tableItem.setText(0, enumType.getShortName());
					tableItem.setText(1, enumType.getType());
					tableItem.setText(2, enumType.getDescription());
					break;
				default:
					break;
				}
				tableItem.setData(dataType);
			}
		}
	}

}
