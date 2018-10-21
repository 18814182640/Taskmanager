package com.FP.frame.Dialog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.TableTree;
import org.eclipse.swt.custom.TableTreeItem;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.FP.frame.Config;
import com.PD.Tool.FileTool;
import com.PD.model.DataModel;
import com.PD.model.ModuleModel;
import com.PD.model.SWCModel;

public class RTEDesignDialog extends Dialog {

	private Shell shlRteLoader;
	private Text text;
	private TableTree tableTree;
	private List<ModuleModel> moduleModels;
	private List<SWCModel> swcModelList;
	private Table table, table_1, table_2;
	private Text text_1, text_2;
	private Composite compositeBase, signalComposite, functionComposite;
	private StackLayout stackLayout;

	public RTEDesignDialog(Shell parent, List<ModuleModel> moduleModels, List<SWCModel> swcModelList) {
		super(parent);
		this.moduleModels = moduleModels;
		this.swcModelList = swcModelList;
	}

	@SuppressWarnings("deprecation")
	public void open() {
		shlRteLoader = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.MAX | SWT.RESIZE);
		shlRteLoader.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
		shlRteLoader.setSize(1090, 485);
		shlRteLoader.setImage(new Image(shlRteLoader.getDisplay(), Config.RTEImagePath));
		shlRteLoader.setText("RTE");
		shlRteLoader.setLocation(
				getParent().getLocation().x + getParent().getSize().x / 2 - shlRteLoader.getSize().x / 2,
				getParent().getLocation().y + getParent().getSize().y / 2 - shlRteLoader.getSize().y / 2);
		shlRteLoader.setLayout(new FormLayout());

		Label lblNewLabel = new Label(shlRteLoader, SWT.NONE);
		lblNewLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_LIST_FOREGROUND));
		lblNewLabel.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.BOLD));
		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.right = new FormAttachment(100, -515);
		fd_lblNewLabel.top = new FormAttachment(0, 10);
		fd_lblNewLabel.left = new FormAttachment(100, -630);
		lblNewLabel.setLayoutData(fd_lblNewLabel);
		lblNewLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblNewLabel.setText("\u52A0\u8F7DBWS\u63A5\u53E3\u6587\u4EF6:");

		text = new Text(shlRteLoader, SWT.BORDER);
		FormData fd_text = new FormData();
		fd_text.right = new FormAttachment(100, -115);
		fd_text.top = new FormAttachment(0, 7);
		fd_text.left = new FormAttachment(100, -515);
		text.setLayoutData(fd_text);

		Button btnNewButton = new Button(shlRteLoader, SWT.NONE);
		FormData fd_btnNewButton = new FormData();
		fd_btnNewButton.bottom = new FormAttachment(0, 36);
		fd_btnNewButton.right = new FormAttachment(100, -10);
		fd_btnNewButton.top = new FormAttachment(0, 5);
		fd_btnNewButton.left = new FormAttachment(100, -110);
		btnNewButton.setLayoutData(fd_btnNewButton);
		btnNewButton.setText("\u52A0\u8F7D\u6587\u4EF6");

		Composite composite = new Composite(shlRteLoader, SWT.BORDER);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		FormData fd_composite = new FormData();
		fd_composite.top = new FormAttachment(0, 45);
		fd_composite.right = new FormAttachment(70, 0);
		fd_composite.bottom = new FormAttachment(100, -10);
		fd_composite.left = new FormAttachment(0, 10);
		composite.setLayoutData(fd_composite);

		tableTree = new TableTree(composite, SWT.BORDER | SWT.FULL_SELECTION);
		tableTree.getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (e.button == 1) {
					Point point = new Point(e.x, e.y);
					TableTreeItem item = tableTree.getItem(point);
					if (item != null && item.getItemCount() <= 0) {
						showAttribute((DataModel) item.getData());
						compositeBase.layout();
					}
				}
			}
		});
		tableTree.getTable().setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND));
		tableTree.getTable().setLinesVisible(true);
		tableTree.getTable().setHeaderVisible(true);

		TableColumn tblclmnNewColumn = new TableColumn(tableTree.getTable(), SWT.CENTER);
		tblclmnNewColumn.setWidth(100);
		tblclmnNewColumn.setText("Module");

		TableColumn tblclmnNewColumn_1 = new TableColumn(tableTree.getTable(), SWT.CENTER);
		tblclmnNewColumn_1.setWidth(100);
		tblclmnNewColumn_1.setText("PortName");

		TableColumn tblclmnPosr = new TableColumn(tableTree.getTable(), SWT.CENTER);
		tblclmnPosr.setWidth(100);
		tblclmnPosr.setText("PortGroup");

		TableColumn tblclmnPosrattribute = new TableColumn(tableTree.getTable(), SWT.CENTER);
		tblclmnPosrattribute.setText("PosrAttribute");
		tblclmnPosrattribute.setWidth(150);

		TableColumn tblclmnDescription = new TableColumn(tableTree.getTable(), SWT.CENTER);
		tblclmnDescription.setWidth(280);
		tblclmnDescription.setText("Description");

		Composite composite_1 = new Composite(shlRteLoader, SWT.BORDER);
		composite_1.setLayout(new FormLayout());
		FormData fd_composite_1 = new FormData();
		fd_composite_1.left = new FormAttachment(composite, 10);
		fd_composite_1.top = new FormAttachment(0, 45);
		fd_composite_1.right = new FormAttachment(100, -10);
		fd_composite_1.bottom = new FormAttachment(100, -10);
		composite_1.setLayoutData(fd_composite_1);

		Composite composite_2 = new Composite(composite_1, SWT.NONE);
		composite_2.setLayout(new FillLayout(SWT.HORIZONTAL));
		FormData fd_composite_2 = new FormData();
		fd_composite_2.left = new FormAttachment(0, 0);
		fd_composite_2.top = new FormAttachment(0, 0);
		fd_composite_2.right = new FormAttachment(100, 0);
		fd_composite_2.bottom = new FormAttachment(0, 25);
		composite_2.setLayoutData(fd_composite_2);

		Label label = new Label(composite_2, SWT.BORDER);
		label.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_CYAN));
		label.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.BOLD));
		label.setText("\u7AEF\u53E3\u8BE6\u7EC6\u5C5E\u6027");

		compositeBase = new Composite(composite_1, SWT.NONE);
		FormData fd_compositeBase = new FormData();
		fd_compositeBase.top = new FormAttachment(composite_2, 0);
		fd_compositeBase.left = new FormAttachment(0, 0);
		fd_compositeBase.right = new FormAttachment(100, 0);
		fd_compositeBase.bottom = new FormAttachment(100, 0);
		compositeBase.setLayoutData(fd_compositeBase);
		stackLayout = new StackLayout();
		compositeBase.setLayout(stackLayout);

		// 信号
		signalComposite = new Composite(compositeBase, SWT.BORDER);
		signalComposite.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		signalComposite.setLayout(new FormLayout());
		FormData fd_signalComposite = new FormData();
		fd_signalComposite.top = new FormAttachment(compositeBase, 0);
		fd_signalComposite.left = new FormAttachment(0, 0);
		fd_signalComposite.right = new FormAttachment(100, 0);
		fd_signalComposite.bottom = new FormAttachment(100, 0);
		signalComposite.setLayoutData(fd_signalComposite);

		table = new Table(signalComposite, SWT.BORDER | SWT.FULL_SELECTION);
		FormData fd_table = new FormData();
		fd_table.bottom = new FormAttachment(75, 0);
		fd_table.left = new FormAttachment(0, 10);
		fd_table.top = new FormAttachment(0, 30);
		fd_table.right = new FormAttachment(100, -10);
		table.setLayoutData(fd_table);
		table.setLinesVisible(true);

		Label lblNewLabel_1 = new Label(signalComposite, SWT.NONE);
		lblNewLabel_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblNewLabel_1.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
		FormData fd_lblNewLabel_1 = new FormData();
		fd_lblNewLabel_1.bottom = new FormAttachment(table, -3);
		fd_lblNewLabel_1.left = new FormAttachment(table, 0, SWT.LEFT);

		TableColumn tblclmnNewColumn_2 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_2.setWidth(120);
		tblclmnNewColumn_2.setText("New Column");

		TableColumn tblclmnNewColumn_3 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_3.setWidth(150);
		tblclmnNewColumn_3.setText("New Column");
		lblNewLabel_1.setLayoutData(fd_lblNewLabel_1);
		lblNewLabel_1.setText("Base Attribute:");

		Label lblNewLabel_2 = new Label(signalComposite, SWT.NONE);
		lblNewLabel_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblNewLabel_2.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
		FormData fd_lblNewLabel_2 = new FormData();
		fd_lblNewLabel_2.top = new FormAttachment(table, 10);
		fd_lblNewLabel_2.left = new FormAttachment(0, 10);
		lblNewLabel_2.setLayoutData(fd_lblNewLabel_2);
		lblNewLabel_2.setText("Description:");

		text_1 = new Text(signalComposite, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		FormData fd_text_1 = new FormData();
		fd_text_1.top = new FormAttachment(lblNewLabel_2, 3);
		fd_text_1.left = new FormAttachment(0, 10);
		fd_text_1.right = new FormAttachment(100, -10);
		fd_text_1.bottom = new FormAttachment(100, -10);
		text_1.setLayoutData(fd_text_1);

		// 函数
		functionComposite = new Composite(compositeBase, SWT.BORDER);
		functionComposite.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		functionComposite.setLayout(new FormLayout());
		FormData fd_functionComposite = new FormData();
		fd_functionComposite.top = new FormAttachment(compositeBase, 0);
		fd_functionComposite.left = new FormAttachment(0, 0);
		fd_functionComposite.right = new FormAttachment(100, 0);
		fd_functionComposite.bottom = new FormAttachment(100, 0);
		functionComposite.setLayoutData(fd_functionComposite);

		table_1 = new Table(functionComposite, SWT.BORDER | SWT.FULL_SELECTION);
		FormData fd_table1 = new FormData();
		fd_table1.bottom = new FormAttachment(40, 0);
		fd_table1.left = new FormAttachment(0, 10);
		fd_table1.top = new FormAttachment(0, 30);
		fd_table1.right = new FormAttachment(100, -10);
		table_1.setLayoutData(fd_table1);
		table_1.setLinesVisible(true);

		TableColumn tblclmnNewColumn_21 = new TableColumn(table_1, SWT.NONE);
		tblclmnNewColumn_21.setWidth(120);
		tblclmnNewColumn_21.setText("New Column");

		TableColumn tblclmnNewColumn_31 = new TableColumn(table_1, SWT.NONE);
		tblclmnNewColumn_31.setWidth(150);
		tblclmnNewColumn_31.setText("New Column");

		Label lblNewLabel_11 = new Label(functionComposite, SWT.NONE);
		lblNewLabel_11.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblNewLabel_11.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
		FormData fd_lblNewLabel_11 = new FormData();
		fd_lblNewLabel_11.bottom = new FormAttachment(table_1, -3);
		fd_lblNewLabel_11.left = new FormAttachment(table_1, 0, SWT.LEFT);
		lblNewLabel_11.setLayoutData(fd_lblNewLabel_11);
		lblNewLabel_11.setText("Base Attribute:");

		Label lblNewLabel_111 = new Label(functionComposite, SWT.NONE);
		lblNewLabel_111.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblNewLabel_111.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
		FormData fd_lblNewLabel_111 = new FormData();
		fd_lblNewLabel_111.top = new FormAttachment(table_1, 10);
		fd_lblNewLabel_111.left = new FormAttachment(table_1, 0, SWT.LEFT);
		lblNewLabel_111.setLayoutData(fd_lblNewLabel_111);
		lblNewLabel_111.setText("Parameters:");

		table_2 = new Table(functionComposite, SWT.BORDER | SWT.FULL_SELECTION);
		table_2.setHeaderVisible(true);
		FormData fd_table11 = new FormData();
		fd_table11.bottom = new FormAttachment(70, 0);
		fd_table11.left = new FormAttachment(0, 10);
		fd_table11.top = new FormAttachment(lblNewLabel_111, 3);
		fd_table11.right = new FormAttachment(100, -10);
		table_2.setLayoutData(fd_table11);
		table_2.setLinesVisible(true);

		TableColumn tblclmnNewColumn_211 = new TableColumn(table_2, SWT.NONE);
		tblclmnNewColumn_211.setWidth(90);
		tblclmnNewColumn_211.setText("Name");

		TableColumn tblclmnNewColumn_4 = new TableColumn(table_2, SWT.NONE);
		tblclmnNewColumn_4.setWidth(90);
		tblclmnNewColumn_4.setText("DataType");

		TableColumn tblclmnNewColumn_311 = new TableColumn(table_2, SWT.NONE);
		tblclmnNewColumn_311.setWidth(90);
		tblclmnNewColumn_311.setText("Description");

		Label lblNewLabel_21 = new Label(functionComposite, SWT.NONE);
		lblNewLabel_21.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblNewLabel_21.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
		FormData fd_lblNewLabel_21 = new FormData();
		fd_lblNewLabel_21.top = new FormAttachment(table_2, 10);
		fd_lblNewLabel_21.left = new FormAttachment(0, 10);
		lblNewLabel_21.setLayoutData(fd_lblNewLabel_21);
		lblNewLabel_21.setText("Description:");

		text_2 = new Text(functionComposite, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		FormData fd_text_11 = new FormData();
		fd_text_11.top = new FormAttachment(lblNewLabel_21, 3);
		fd_text_11.left = new FormAttachment(0, 10);
		fd_text_11.right = new FormAttachment(100, -10);
		fd_text_11.bottom = new FormAttachment(100, -10);
		text_2.setLayoutData(fd_text_11);
		compositeBase.setTabList(new Control[] { functionComposite, signalComposite });
		compositeBase.layout();

		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				loadBWS();
			}
		});

		if (moduleModels != null) { // 已经加载RTE，重新打开时直接显示
			tableTree.removeAll();
			showData(tableTree, moduleModels);
		}
		shlRteLoader.open();
		Display display = shlRteLoader.getDisplay();
		while (!shlRteLoader.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	@SuppressWarnings("deprecation")
	private void showData(TableTree tableTree, List<ModuleModel> models) {

		if (models != null) {
			for (int i = 0; i < models.size(); i++) {
				ModuleModel moduleModel = models.get(i);
				TableTreeItem tableTreeItem = new TableTreeItem(tableTree, SWT.NONE);
				tableTreeItem.setText(0, moduleModel.getName());
				if (moduleModel != null) {
					showModuleData(tableTreeItem, moduleModel);
				}
			}
		}

	}

	@SuppressWarnings("deprecation")
	private void showModuleData(TableTreeItem tableTree, ModuleModel moduleModel) {
		for (int j = 0; j < moduleModel.getDataModels().size(); j++) {
			TableTreeItem tableTreeItem = new TableTreeItem(tableTree, SWT.NONE);
			DataModel dataModel = moduleModel.getDataModels().get(j);
			if (dataModel != null) {
				if(Config.PortGroup.P_Port.getName().equals(dataModel.getAttributeValue("PortGroup"))){
					showDesignData(tableTreeItem, dataModel);
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	private void showDesignData(TableTreeItem tableTreeItem, DataModel dataModel) {
		if (tableTreeItem != null && dataModel != null) {
			tableTreeItem.setText(1, (String) dataModel.getAttributeValue(dataModel.getAttributeNames()[0]));
			tableTreeItem.setText(2, (String) dataModel.getAttributeValue(dataModel.getAttributeNames()[1]));
			tableTreeItem.setText(3, (String) dataModel.getAttributeValue(dataModel.getAttributeNames()[2]));
			tableTreeItem.setText(4, (String) dataModel
					.getAttributeValue(dataModel.getAttributeNames()[dataModel.getArributeNum() - 1]));
			tableTreeItem.setData(dataModel); // 存放
		}
	}

	private void showAttribute(DataModel dataModel) {
		if (dataModel != null) {
			if (dataModel.getType().equals(Config.Signal)) {
				stackLayout.topControl = signalComposite;
				table.removeAll();
				for (int i = 0; i < dataModel.getArributeNum() - 1; i++) {
					TableItem tableItem = new TableItem(table, SWT.NONE);
					tableItem.setText(0, dataModel.getAttributeNames()[i]);
					tableItem.setText(1, (dataModel.getAttributeValue(dataModel.getAttributeNames()[i]) + ""));
				}
				text_1.setText((String) dataModel
						.getAttributeValue(dataModel.getAttributeNames()[dataModel.getArributeNum() - 1]));
			} else if (dataModel.getType().equals(Config.Function)) {
				stackLayout.topControl = functionComposite;
				table_1.removeAll();
				for (int i = 0; i < dataModel.getArributeNum() - 2; i++) {
					TableItem tableItem = new TableItem(table_1, SWT.NONE);
					tableItem.setText(0, dataModel.getAttributeNames()[i]);
					tableItem.setText(1, (String) dataModel.getAttributeValue(dataModel.getAttributeNames()[i]));

				}
				table_2.removeAll();
				String param = (String) dataModel
						.getAttributeValue(dataModel.getAttributeNames()[dataModel.getArributeNum() - 2]);
				if (param != null && !param.isEmpty()) {
					if (param.contains(";")) {
						String[] rows = param.trim().split(";");
						for (int i = 0; i < rows.length; i++) {
							String temp = rows[i].trim();
							if (temp != null && !temp.isEmpty() && temp.contains(" ")) {
								String[] col = temp.split(" ");
								TableItem tableItem = new TableItem(table_2, SWT.NONE);
								if (col.length == 2) {
									tableItem.setText(0, col[1]);
									tableItem.setText(1, col[0]);
								} else if (col.length == 3) {
									tableItem.setText(0, col[1]);
									tableItem.setText(1, col[0]);
									tableItem.setText(2, col[2]);
								}
							}
						}
					}
				}
				text_2.setText((String) dataModel
						.getAttributeValue(dataModel.getAttributeNames()[dataModel.getArributeNum() - 1]));
			}
		}
	}

	private void loadBWS() {
		String path = CommonDialog.openFileDialog(shlRteLoader, new String[] { "*.xls", "*.*" });
		if (path != null && !path.isEmpty()) {
			text.setText(path);
			List<ModuleModel> temp = FileTool.parseExcel(path);
			if (temp == null) {
				CommonDialog.showMessage(shlRteLoader, "提示", "文件加载出错,请检查文件格式或确认是否加密！", SWT.ERROR);
				return;
			}
			moduleModels.clear();
			moduleModels.addAll(temp);
			if (swcModelList != null) {
				for (int i = 0; i < swcModelList.size(); i++) {
					SWCModel swcModel = swcModelList.get(i);
					if (swcModel != null && swcModel.isLineToRTE() && swcModel.getSwcData() != null) {
						moduleModels.add(swcModel.getSwcData());
					}
				}
			}
			tableTree.removeAll();
			showData(tableTree, moduleModels);
		}
	}

}
