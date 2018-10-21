package com.FP.frame.Dialog;

import java.awt.Toolkit;
import java.awt.image.SinglePixelPackedSampleModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.custom.TableTree;
import org.eclipse.swt.custom.TableTreeItem;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;
import org.omg.CORBA.PRIVATE_MEMBER;

import com.FP.frame.Config;
import com.FP.frame.Config.PortAttribute;
import com.FP.frame.Config.PortGroup;
import com.PD.Tool.FileTool;
import com.PD.Tool.SerializationTool;
import com.PD.cache.CacheData;
import com.PD.model.DataModel;
import com.PD.model.FunctionModel;
import com.PD.model.ModuleModel;
import com.PD.model.RTEModel;
import com.PD.model.SWCModel;
import com.PD.model.SignalModel;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.sun.org.apache.bcel.internal.generic.RET;
import com.sun.org.apache.regexp.internal.recompile;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Single;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class SWCDesignDialog extends Dialog {

	private Shell shell;
	private SWCModel swcModel;
	private TableTree inputSelectTableTree, inputNewTableTree, outputSelectTableTree, outputNewTableTree;
	private MenuItem addMenuItem;
	private Menu menu, menu_1;
	private MenuItem addItme;
	private Text text;
	private Text text_2;
	private MenuItem deleteOutputItem, creatService, createSend, upOutput, downOutput;

	public SWCDesignDialog(Shell parent) {
		super(parent);
	}

	/**
	 * @wbp.parser.constructor
	 */

	public SWCDesignDialog(Shell parent, SWCModel swcModel) {
		super(parent);
		this.swcModel = swcModel;
	}

	@SuppressWarnings("deprecation")
	public void open() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.MAX | SWT.RESIZE);
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
		shell.setSize(1090, 480);
		shell.setImage(new Image(shell.getDisplay(), Config.SWCImagePath));
		shell.setText(swcModel.getSwcData().getName());
		// shell.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width-shell.getSize().x)/2,
		// (Toolkit.getDefaultToolkit().getScreenSize().height-shell.getSize().y)/2);
		shell.setLocation(getParent().getLocation().x + getParent().getSize().x / 2 - shell.getSize().x / 2,
				getParent().getLocation().y + getParent().getSize().y / 2 - shell.getSize().y / 2);
		shell.setLayout(new FormLayout());

		Group upgroup = new Group(shell, SWT.BORDER);
		upgroup.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_FOREGROUND));
		upgroup.setLayout(new FormLayout());
		FormData fd_upgroup = new FormData();
		fd_upgroup.top = new FormAttachment(0, 50);
		fd_upgroup.left = new FormAttachment(0, 0);
		fd_upgroup.bottom = new FormAttachment(50, 0);
		fd_upgroup.right = new FormAttachment(100, 0);
		upgroup.setLayoutData(fd_upgroup);

		Group downgroup = new Group(shell, SWT.BORDER);
		downgroup.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_FOREGROUND));
		downgroup.setLayout(new FormLayout());
		FormData fd_downgroup = new FormData();
		fd_downgroup.top = new FormAttachment(50, 0);
		fd_downgroup.left = new FormAttachment(0, 0);
		fd_downgroup.bottom = new FormAttachment(100, 0);
		fd_downgroup.right = new FormAttachment(100, 0);
		downgroup.setLayoutData(fd_downgroup);

		Group grpRte = new Group(upgroup, SWT.BORDER);
		grpRte.setFont(SWTResourceManager.getFont("楷体", 11, SWT.BOLD));
		grpRte.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		grpRte.setText("\u8F93\u5165\u7AEF\u53E3\u53EF\u9009\u9879:");
		FormData fd_grpRte = new FormData();
		fd_grpRte.top = new FormAttachment(0, 0);
		fd_grpRte.left = new FormAttachment(0, 0);
		fd_grpRte.bottom = new FormAttachment(100, 0);
		fd_grpRte.right = new FormAttachment(50, 0);
		grpRte.setLayoutData(fd_grpRte);
		grpRte.setLayout(new FillLayout());

		inputSelectTableTree = new TableTree(grpRte, SWT.BORDER | SWT.FULL_SELECTION);
		inputSelectTableTree.getTable().setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND));
		inputSelectTableTree.getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				Point point = new Point(e.x, e.y);
				TableTreeItem item = inputSelectTableTree.getItem(point);
				if (item == null) {
					menu.setVisible(false);
					return;
				} else {
					if (item.getItemCount() <= 0) {
						addMenuItem.setText("\u9009\u62E9\u4FE1\u53F7");
					} else {
						addMenuItem.setText("\u9009\u62E9\u5168\u90E8\u4FE1\u53F7");
					}
				}
			}
		});
		inputSelectTableTree.getTable().setLinesVisible(true);
		inputSelectTableTree.getTable().setHeaderVisible(true);
		menu = new Menu(inputSelectTableTree.getTable());
		inputSelectTableTree.getTable().setMenu(menu);

		addMenuItem = new MenuItem(menu, SWT.NONE);
		addMenuItem.setText("\u9009\u62E9\u4FE1\u53F7/\u65B9\u7A0B(S)");
		addMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				TableTreeItem item = inputSelectTableTree.getSelection()[0];
				if (item != null) {
					if (item.getItemCount() <= 0 && item.getText() == null) {
						addDataRow(item, inputNewTableTree);
					} else {
						for (int i = 0; i < item.getItemCount(); i++) {
							TableTreeItem tableTreeItem = item.getItem(i);
							if (tableTreeItem != null) {
								addDataRow(tableTreeItem, inputNewTableTree);
							}
						}
					}
				}
			}
		});

		TableColumn moduleName = new TableColumn(inputSelectTableTree.getTable(), SWT.NONE);
		moduleName.setWidth(90);
		moduleName.setText("Module");

		TableColumn tblclmnName = new TableColumn(inputSelectTableTree.getTable(), SWT.CENTER);
		tblclmnName.setWidth(95);
		tblclmnName.setText("PortName");

		TableColumn group = new TableColumn(inputSelectTableTree.getTable(), SWT.CENTER);
		group.setWidth(85);
		group.setText("PortGroup");

		TableColumn tblclmnArrtibute = new TableColumn(inputSelectTableTree.getTable(), SWT.CENTER);
		tblclmnArrtibute.setWidth(85);
		tblclmnArrtibute.setText("PortArrtibute");

		TableColumn tblclmnDescription = new TableColumn(inputSelectTableTree.getTable(), SWT.CENTER);
		tblclmnDescription.setWidth(158);
		tblclmnDescription.setText("Description");

		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblNewLabel.setFont(SWTResourceManager.getFont("宋体", 12, SWT.BOLD));
		lblNewLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.bottom = new FormAttachment(0, 35);
		fd_lblNewLabel.top = new FormAttachment(0, 15);
		fd_lblNewLabel.left = new FormAttachment(0, 30);
		lblNewLabel.setLayoutData(fd_lblNewLabel);
		lblNewLabel.setText("\u6A21\u5757\u540D\u79F0:");

		text = new Text(shell, SWT.BORDER);
		text.setText(swcModel.getSwcData().getName());
		text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (text.getText() != null && !text.getText().isEmpty()) {
					swcModel.setModuleName(text.getText());
				}
			}
		});
		text.setFont(SWTResourceManager.getFont("微软雅黑", 15, SWT.NORMAL));
		FormData fd_text = new FormData();
		fd_text.top = new FormAttachment(0, 5);
		fd_text.bottom = new FormAttachment(0, 38);
		fd_text.left = new FormAttachment(lblNewLabel, 5);
		fd_text.right = new FormAttachment(lblNewLabel, 300);
		text.setLayoutData(fd_text);

		Label lblms = new Label(shell, SWT.NONE);
		lblms.setText("\u8C03\u7528\u5468\u671F[ms]:");
		lblms.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		lblms.setFont(SWTResourceManager.getFont("宋体", 12, SWT.BOLD));
		lblms.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		FormData fd_lblms = new FormData();
		fd_lblms.bottom = new FormAttachment(0, 35);
		fd_lblms.top = new FormAttachment(0, 15);
		fd_lblms.left = new FormAttachment(text, 30);
		lblms.setLayoutData(fd_lblms);

		text_2 = new Text(shell, SWT.BORDER);
		text_2.setFont(SWTResourceManager.getFont("微软雅黑", 15, SWT.NORMAL));
		FormData fd_text_2 = new FormData();
		fd_text_2.top = new FormAttachment(0, 5);
		fd_text_2.bottom = new FormAttachment(0, 38);
		fd_text_2.left = new FormAttachment(lblms, 5);
		fd_text_2.right = new FormAttachment(lblms, 300);
		text_2.setLayoutData(fd_text_2);
		text_2.setText(swcModel.getSwcData().getTime() + "");
		text_2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (text_2.getText() != null && !text_2.getText().isEmpty()) {
					try {
						int time = Integer.parseInt(text_2.getText());
						swcModel.getSwcData().setTime(time);
					} catch (Exception e2) {
						CommonDialog.showMessage(shell, "错误", "请输入Int型数字!", SWT.ERROR);
					}
				}
			}
		});

		Group group_1 = new Group(downgroup, SWT.BORDER);
		group_1.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		group_1.setFont(SWTResourceManager.getFont("楷体", 11, SWT.BOLD));
		group_1.setText("\u8F93\u51FA\u7AEF\u53E3\u53EF\u9009\u9879:");
		group_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		group_1.setLayout(new FillLayout());
		FormData fd_group_1 = new FormData();
		fd_group_1.top = new FormAttachment(0, 0);
		fd_group_1.bottom = new FormAttachment(100, 0);
		fd_group_1.left = new FormAttachment(0, 0);
		fd_group_1.right = new FormAttachment(50, 0);
		group_1.setLayoutData(fd_group_1);

		outputSelectTableTree = new TableTree(group_1, SWT.BORDER | SWT.FULL_SELECTION);
		outputSelectTableTree.getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				Point point = new Point(e.x, e.y);
				TableTreeItem item = outputSelectTableTree.getItem(point);
				if (item == null) {
					menu_1.setVisible(false);
					return;
				} else {
					if (item.getItemCount() <= 0) {
						addItme.setText("\u9009\u62E9\u4FE1\u53F7(S)");
					} else {
						addItme.setText("\u9009\u62E9\u5168\u90E8\u4FE1\u53F7(S)");
					}
				}
			}
		});
		outputSelectTableTree.getTable().setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND));
		outputSelectTableTree.getTable().setLinesVisible(true);
		outputSelectTableTree.getTable().setHeaderVisible(true);

		TableColumn tblclmnSign = new TableColumn(outputSelectTableTree.getTable(), SWT.NONE);
		tblclmnSign.setWidth(90);
		tblclmnSign.setText("Module");

		TableColumn tblclmnPortname = new TableColumn(outputSelectTableTree.getTable(), SWT.CENTER);
		tblclmnPortname.setWidth(95);
		tblclmnPortname.setText("PortName");

		TableColumn tblclmnPortgroup = new TableColumn(outputSelectTableTree.getTable(), SWT.CENTER);
		tblclmnPortgroup.setWidth(85);
		tblclmnPortgroup.setText("PortGroup");

		TableColumn tblclmnPortarrtibute = new TableColumn(outputSelectTableTree.getTable(), SWT.CENTER);
		tblclmnPortarrtibute.setWidth(85);
		tblclmnPortarrtibute.setText("PortArrtibute");

		TableColumn tableColumn_12 = new TableColumn(outputSelectTableTree.getTable(), SWT.CENTER);
		tableColumn_12.setWidth(158);
		tableColumn_12.setText("Description");

		menu_1 = new Menu(outputSelectTableTree.getTable());
		outputSelectTableTree.getTable().setMenu(menu_1);
		addItme = new MenuItem(menu_1, SWT.NONE);
		addItme.setText("\u9009\u62E9\u4FE1\u53F7/\u65B9\u7A0B(S)");
		addItme.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableTreeItem item = outputSelectTableTree.getSelection()[0];
				if (item != null) {
					if (item.getItemCount() <= 0 && item.getText(0) == null) {
						addDataRow(item, outputNewTableTree);
					} else {
						for (int i = 0; i < item.getItemCount(); i++) {
							TableTreeItem tableTreeItem = item.getItem(i);
							if (tableTreeItem != null) {
								addDataRow(tableTreeItem, outputNewTableTree);
							}
						}
					}
				}
			}
		});

		Label label_1 = new Label(shell, SWT.NONE);
		label_1.setText("\u751F\u6210\u6846\u67B6\u683C\u5F0F:");
		label_1.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		label_1.setFont(SWTResourceManager.getFont("宋体", 12, SWT.BOLD));
		label_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		FormData fd_label_1 = new FormData();
		fd_label_1.left = new FormAttachment(text_2, 58);
		fd_label_1.top = new FormAttachment(lblNewLabel, 0, SWT.TOP);
		label_1.setLayoutData(fd_label_1);

		final Combo combo = new Combo(shell, SWT.READ_ONLY);
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				swcModel.getSwcData().setFrameModel(combo.getSelectionIndex());
			}
		});
		combo.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
		combo.setItems(Config.ModelC_Code);
		FormData fd_combo = new FormData();
		fd_combo.bottom = new FormAttachment(label_1, 0);
		fd_combo.right = new FormAttachment(label_1, 106, SWT.RIGHT);
		fd_combo.left = new FormAttachment(label_1, 6);
		fd_combo.top = new FormAttachment(label_1, -5, SWT.TOP);
		combo.setLayoutData(fd_combo);
		combo.select(swcModel.getSwcData().getFrameModel());

		Label label = new Label(upgroup, SWT.NONE);
		label.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_FOREGROUND));
		label.setFont(SWTResourceManager.getFont("微软雅黑", 11, SWT.BOLD));
		label.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		FormData fd_label = new FormData();
		fd_label.top = new FormAttachment(50, -10);
		fd_label.left = new FormAttachment(50, 1);
		label.setLayoutData(fd_label);
		label.setText(">>");

		Group group_2 = new Group(upgroup, SWT.BORDER);
		group_2.setFont(SWTResourceManager.getFont("楷体", 11, SWT.BOLD));
		group_2.setText("\u8F93\u5165\u7AEF\u53E3\u914D\u7F6E:");
		group_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		group_2.setLayout(new FillLayout());
		FormData fd_group2 = new FormData();
		fd_group2.top = new FormAttachment(0, 0);
		fd_group2.left = new FormAttachment(50, 25);
		fd_group2.bottom = new FormAttachment(100, 0);
		fd_group2.right = new FormAttachment(100, 0);
		group_2.setLayoutData(fd_group2);

		inputNewTableTree = new TableTree(group_2, SWT.BORDER | SWT.FULL_SELECTION);
		inputNewTableTree.getTable().setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND));
		inputNewTableTree.getTable().setLinesVisible(true);
		inputNewTableTree.getTable().setHeaderVisible(true);

		TableColumn tableColumn_1 = new TableColumn(inputNewTableTree.getTable(), SWT.CENTER);
		tableColumn_1.setWidth(95);
		tableColumn_1.setText("PortName");

		TableColumn tableColumn_2 = new TableColumn(inputNewTableTree.getTable(), SWT.CENTER);
		tableColumn_2.setWidth(90);
		tableColumn_2.setText("PortGroup");

		TableColumn tableColumn_3 = new TableColumn(inputNewTableTree.getTable(), SWT.CENTER);
		tableColumn_3.setWidth(90);
		tableColumn_3.setText("PortArrtibute");

		TableColumn tableColumn_4 = new TableColumn(inputNewTableTree.getTable(), SWT.CENTER);
		tableColumn_4.setWidth(215);
		tableColumn_4.setText("Description");

		Menu menu_2 = new Menu(inputNewTableTree.getTable());
		inputNewTableTree.getTable().setMenu(menu_2);

		MenuItem deleteInputItem = new MenuItem(menu_2, SWT.NONE);
		deleteInputItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableTreeItem item = inputNewTableTree.getSelection()[0];
				if (item != null) {
					deleteInputRow(item, inputNewTableTree, swcModel.getSwcData().getDataModels());
				}
			}
		});
		deleteInputItem.setText("\u5220\u9664\u4FE1\u53F7/\u65B9\u7A0B(D)");

		MenuItem upInputItem = new MenuItem(menu_2, SWT.NONE);
		upInputItem.setText("\u4E0A\u79FB(UP)");

		MenuItem downInputItem = new MenuItem(menu_2, SWT.NONE);
		downInputItem.setText("\u4E0B\u79FB(Down)");

		Label label_2 = new Label(downgroup, SWT.NONE);
		label_2.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_FOREGROUND));
		label_2.setText(">>");
		label_2.setFont(SWTResourceManager.getFont("微软雅黑", 11, SWT.BOLD));
		label_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		FormData fd_label_2 = new FormData();
		fd_label_2.top = new FormAttachment(50, -10);
		fd_label_2.left = new FormAttachment(50, 1);
		label_2.setLayoutData(fd_label_2);

		Group group_3 = new Group(downgroup, SWT.BORDER);
		group_3.setFont(SWTResourceManager.getFont("楷体", 11, SWT.BOLD));
		group_3.setText("\u8F93\u51FA\u7AEF\u53E3\u914D\u7F6E:");
		group_3.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		group_3.setLayout(new FillLayout());
		FormData fd_group_3 = new FormData();
		fd_group_3.left = new FormAttachment(50, 25);
		fd_group_3.right = new FormAttachment(100, 0);
		fd_group_3.top = new FormAttachment(0, 0);
		fd_group_3.bottom = new FormAttachment(100, 0);
		group_3.setLayoutData(fd_group_3);

		outputNewTableTree = new TableTree(group_3, SWT.BORDER | SWT.FULL_SELECTION);
		outputNewTableTree.getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				Point point = new Point(e.x, e.y);
				TableTreeItem item = outputNewTableTree.getItem(point);
				if (item == null) {
					deleteOutputItem.setEnabled(false);
					upOutput.setEnabled(false);
					downOutput.setEnabled(false);
				} else {
					deleteOutputItem.setEnabled(true);
					upOutput.setEnabled(true);
					downOutput.setEnabled(true);
				}
			}
		});
		outputNewTableTree.getTable().setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND));
		outputNewTableTree.getTable().setLinesVisible(true);
		outputNewTableTree.getTable().setHeaderVisible(true);

		TableColumn tableColumn_6 = new TableColumn(outputNewTableTree.getTable(), SWT.CENTER);
		tableColumn_6.setWidth(95);
		tableColumn_6.setText("PortName");

		TableColumn tableColumn_7 = new TableColumn(outputNewTableTree.getTable(), SWT.CENTER);
		tableColumn_7.setWidth(90);
		tableColumn_7.setText("PortGroup");

		TableColumn tableColumn_8 = new TableColumn(outputNewTableTree.getTable(), SWT.CENTER);
		tableColumn_8.setWidth(90);
		tableColumn_8.setText("PortArrtibute");

		TableColumn tableColumn_9 = new TableColumn(outputNewTableTree.getTable(), SWT.CENTER);
		tableColumn_9.setWidth(215);
		tableColumn_9.setText("Description");

		Menu menu_3 = new Menu(outputNewTableTree.getTable());
		outputNewTableTree.getTable().setMenu(menu_3);

		deleteOutputItem = new MenuItem(menu_3, SWT.NONE);
		deleteOutputItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableTreeItem item = outputNewTableTree.getSelection()[0];
				if (item != null) {
					deleteOutputRow(item, outputNewTableTree, swcModel.getSwcData().getDataModels());
				}
			}
		});
		deleteOutputItem.setText("\u5220\u9664\u4FE1\u53F7/\u65B9\u7A0B(D)");

		creatService = new MenuItem(menu_3, SWT.NONE);
		creatService.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				CreateServiceDialog serviceDialog = new CreateServiceDialog(shell);
				DataModel dataModel = serviceDialog.open();
				if (dataModel != null) {
					showRowData(outputNewTableTree, dataModel);
					ModuleModel module = swcModel.getSwcData();
					if (isRepeat(dataModel, module.getDataModels())) {
						CommonDialog.showMessage(shell, "提示", "重复添加，已跳过!", SWT.ICON_INFORMATION);
						return;
					}
					swcModel.getSwcData().getDataModels().add(dataModel);
				}
			}
		});
		creatService.setText("\u65B0\u5EFAService\u7AEF\u53E3");

		createSend = new MenuItem(menu_3, SWT.NONE);
		createSend.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				CreateSendDialog sendeDialog = new CreateSendDialog(shell);
				DataModel dataModel = sendeDialog.open();
				if (dataModel != null) {
					showRowData(outputNewTableTree, dataModel);
					ModuleModel module = swcModel.getSwcData();
					if (isRepeat(dataModel, module.getDataModels())) {
						CommonDialog.showMessage(shell, "提示", "重复添加，已跳过!", SWT.ICON_INFORMATION);
						return;
					}
					swcModel.getSwcData().getDataModels().add(dataModel);
				}
			}
		});
		createSend.setText("\u65B0\u5EFASend\u7AEF\u53E3");

		upOutput = new MenuItem(menu_3, SWT.NONE);
		upOutput.setText("\u4E0A\u79FB(UP)");

		downOutput = new MenuItem(menu_3, SWT.NONE);
		downOutput.setText("\u4E0B\u79FB(Down)");

		RTEModel rteModuls = swcModel.getRteData();
		if (rteModuls != null) { // 加载RTE
			inputSelectTableTree.removeAll();
			showInputSelect(inputSelectTableTree, rteModuls.getModuleModels());
			outputSelectTableTree.removeAll();
			showOutputSelect(outputSelectTableTree, rteModuls.getModuleModels());
		}
		ModuleModel swcModule = swcModel.getSwcData();
		if (swcModule != null) { // 加载SWC
			inputNewTableTree.removeAll();
			showInputData(inputNewTableTree, swcModule.getDataModels());
			outputNewTableTree.removeAll();
			showOutputData(outputNewTableTree, swcModule.getDataModels());
		}
		shell.layout();
		shell.open();
		Display display = shell.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	// 输入端口可选项
	@SuppressWarnings("deprecation")
	private void showInputSelect(TableTree tableTree, List<ModuleModel> models) {

		if (models != null) {
			for (int i = 0; i < models.size(); i++) {
				ModuleModel moduleModel = models.get(i);
				if (moduleModel != null) {
					if (swcModel.getSwcData().getName().equals(moduleModel.getName())) { // 过滤自己
						continue;
					}
					TableTreeItem tableTreeItem = new TableTreeItem(tableTree, SWT.NONE);
					tableTreeItem.setText(0, moduleModel.getName());
					if (moduleModel.getDataModels() != null) {
						showModuleData(tableTreeItem, moduleModel.getDataModels(), Config.PortGroup.P_Port.getName(),
								1);
					}
				}
			}
		}
	}

	// 输出端口可选项
	@SuppressWarnings("deprecation")
	private void showOutputSelect(TableTree tableTree, List<ModuleModel> models) {

		if (models != null) {
			for (int i = 0; i < models.size(); i++) {
				ModuleModel moduleModel = models.get(i);
				if (moduleModel != null) {
					if (swcModel.getSwcData().getName().equals(moduleModel.getName())) { // 过滤自己
						continue;
					}
					TableTreeItem tableTreeItem = new TableTreeItem(tableTree, SWT.NONE);
					tableTreeItem.setText(0, moduleModel.getName());
					if (moduleModel.getDataModels() != null) {
						showModuleData(tableTreeItem, moduleModel.getDataModels(),
								Config.PortAttribute.Client.toString(), 2);
					}
				}
			}
		}
	}

	// 输入端口配置
	private void showInputData(TableTree tableTree, List<DataModel> dataModels) {
		if (tableTree != null && dataModels != null) {
			for (int i = 0; i < dataModels.size(); i++) {
				DataModel dataModel = dataModels.get(i);
				if (dataModel != null) {
					String value = (String) dataModel.getAttributeValue(dataModel.getAttributeNames()[1]);
					if (!Config.PortGroup.R_Port.getName().equals(value)) { // 过滤R-Port
						continue;
					}
					showRowData(tableTree, dataModel);
				}
			}
		}

	}

	// 输出端口配置
	private void showOutputData(TableTree tableTree, List<DataModel> dataModels) {
		if (tableTree != null && dataModels != null) {
			for (int i = 0; i < dataModels.size(); i++) {
				DataModel dataModel = dataModels.get(i);
				if (dataModel != null) {
					String value = (String) dataModel.getAttributeValue(dataModel.getAttributeNames()[1]);
					if (!Config.PortGroup.P_Port.getName().equals(value)) { // 过滤P-Port
						continue;
					}
					showRowData(tableTree, dataModel);
				}
			}
		}

	}

	// 输入输出端口可选项 模组
	@SuppressWarnings("deprecation")
	private void showModuleData(TableTreeItem tableTreeItem, List<DataModel> list, String filter, int index) {

		if (tableTreeItem != null && list != null) {
			for (int j = 0; j < list.size(); j++) {
				if (index > 0 && index < list.get(j).getArributeNum()) {
					String value = (String) list.get(j).getAttributeValue(list.get(j).getAttributeNames()[index]);
					if (!value.equalsIgnoreCase(filter)) {
						continue;
					}
				}
				TableTreeItem newItem = new TableTreeItem(tableTreeItem, SWT.NONE);
				showRowData(newItem, list.get(j));
			}
		}
	}

	// 输入输出端口可选项 信号/方程
	@SuppressWarnings("deprecation")
	private void showRowData(TableTreeItem tableTreeItem, DataModel dataModel) {
		if (tableTreeItem != null && dataModel != null) {
			String[] name = dataModel.getAttributeNames();
			tableTreeItem.setText(1, (String) dataModel.getAttributeValue(name[0]));
			tableTreeItem.setText(2, (String) dataModel.getAttributeValue(name[1]));
			tableTreeItem.setText(3, (String) dataModel.getAttributeValue(name[2]));
			tableTreeItem.setText(4, (String) dataModel.getAttributeValue(name[name.length - 1]));
			tableTreeItem.setData(dataModel);
		}
	}

	// 输入输出端口配置 信号/方程
	@SuppressWarnings("deprecation")
	private void showRowData(TableTree tableTree, DataModel dataModel) {
		if (tableTree != null && dataModel != null) {
			String[] name = dataModel.getAttributeNames();
			TableTreeItem tableTreeItem = new TableTreeItem(tableTree, SWT.NONE);
			tableTreeItem.setText(0, (String) dataModel.getAttributeValue(name[0]));
			tableTreeItem.setText(1, (String) dataModel.getAttributeValue(name[1]));
			tableTreeItem.setText(2, (String) dataModel.getAttributeValue(name[2]));
			tableTreeItem.setText(3, (String) dataModel.getAttributeValue(name[name.length - 1]));
			tableTreeItem.setData(dataModel);
			if (dataModel.getFalg() == Config.LOST) {
				tableTreeItem.setForeground(new Color(shell.getDisplay(), new RGB(255, 0, 0)));
			}
		}
	}

	// 添加 信号/方程
	private void addDataRow(TableTreeItem item, TableTree destTable) {
		if (item != null && destTable != null) {
			DataModel dataModel = (DataModel) item.getData();
			if (dataModel != null) {
				DataModel newDatamodule = (DataModel) SerializationTool.copyObjec(dataModel);
				String portGroupValue = (String) newDatamodule.getAttributeValue("PortGroup");
				if (portGroupValue != null) {
					String value = "";
					portGroupValue = portGroupValue.trim();
					if (Config.PortGroup.P_Port.getName().equals(portGroupValue)) {
						value = Config.PortGroup.R_Port.getName();
					} else if (Config.PortGroup.R_Port.getName().equals(portGroupValue)) {
						value = Config.PortGroup.P_Port.getName();
					}
					if (value != null && !value.isEmpty()) {
						newDatamodule.setAttributeValue("PortGroup", value);
					}
				}
				String portAttribute = (String) newDatamodule.getAttributeValue("PortAttribute");
				if (portAttribute != null) {
					String value = "";
					portAttribute = portAttribute.trim();
					if (Config.PortAttribute.Client.getName().equals(portAttribute)) {
						value = Config.PortAttribute.Service.getName();
					} else if (Config.PortAttribute.Service.getName().equals(portAttribute)) {
						value = Config.PortAttribute.Client.getName();
					} else if (Config.PortAttribute.Send.getName().equals(portAttribute)) {
						value = Config.PortAttribute.Receive.getName();
					} else if (Config.PortAttribute.Receive.getName().equals(portAttribute)) {
						value = Config.PortAttribute.Send.getName();
					}
					if (value != null && !value.isEmpty()) {
						newDatamodule.setAttributeValue("PortAttribute", value);
					}
				}
				ModuleModel module = swcModel.getSwcData();
				if (isRepeat(newDatamodule, module.getDataModels())) {
					CommonDialog.showMessage(shell, "提示", "重复添加，已跳过!", SWT.ICON_INFORMATION);
					return;
				}
				newDatamodule.setFalg(Config.ADD);
				showRowData(destTable, newDatamodule);
				module.getDataModels().add(newDatamodule);
			}
		}
	}

	private void deleteOutputRow(TableTreeItem item, TableTree tableTree, List<DataModel> list) {
		if (item != null && tableTree != null && list != null) {
			DataModel dataModel = (DataModel) item.getData();
			if (list.contains(dataModel)) {
				list.remove(dataModel);
				tableTree.removeAll();
				showOutputData(tableTree, list);
			}
		}
	}

	private void deleteInputRow(TableTreeItem item, TableTree tableTree, List<DataModel> list) {
		if (item != null && tableTree != null && list != null) {
			DataModel dataModel = (DataModel) item.getData();
			if (list.contains(dataModel)) {
				list.remove(dataModel);
				tableTree.removeAll();
				showInputData(tableTree, list);
			}
		}
	}

	private boolean isRepeat(DataModel dataModel, List<DataModel> list) {
		if (dataModel != null && list != null) {
			for (int i = 0; i < list.size(); i++) {
				DataModel temp = list.get(i);
				if (temp != null) {
					if (dataModel.equals(temp)) {
						return true;
					}
				} else {
					continue;
				}
			}
			return false;
		} else {
			return false;
		}
	}
}
