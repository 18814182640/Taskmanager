package com.FP.frame;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

import javax.xml.crypto.Data;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.SWTResourceManager;

import com.FD.model.DataModel.DataType;
import com.FP.frame.Dialog.AddDataTypeDialog;
import com.FP.frame.Dialog.CommonDialog;
import com.FP.frame.Dialog.CreateXMLDialog;
import com.PD.Task.LoadDataTypeOnFileTask;
import com.PD.Task.LoadProjectTask;
import com.PD.Task.LoadXMLContextTask;
import com.PD.Task.SaveProjectTask;
import com.PD.Thread.CellBack;
import com.PD.Thread.QueueRunnable;
import com.PD.Thread.QueueThread;
import com.PD.Thread.Result;
import com.PD.Tool.LoadXMLFile;
import com.PD.Tool.ProjectTool;
import com.PD.Tool.FileTool;
import com.PD.cache.CacheData;
import com.PD.model.ModuleModel;
import com.PD.model.RTEModel;
import com.PD.model.SWCModel;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;

public class MainFrame {

	private Display displayMain;
	private Shell shellMain, shellDrag;
	private ToolBar toolBar;
	private Composite editCom, pulgCom;
	private EditFrame editFrame;
	private PlugFrame plugFrame;
	private MenuItem menu_6Item1, menu_6Item2;

	public static void main(String[] args) {
		MainFrame mainFrame = new MainFrame();
		mainFrame.openMainFrame();
	}

	private void openMainFrame() {
		displayMain = new Display();
		shellMain = new Shell(displayMain);
		shellMain.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				e.doit = false;
				if (CommonDialog.showMessage(shellMain, "询问", "确认退出程序？",
						SWT.YES | SWT.NO | SWT.ICON_QUESTION) == SWT.YES) {
					System.exit(0);
				}
			}
		});
		shellMain.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		shellMain.setText("\u8F6F\u4EF6\u67B6\u6784\u8BBE\u8BA1\u5E73\u53F0 V1.1.1 2018.08.03");
		int screenH = Toolkit.getDefaultToolkit().getScreenSize().height;
		int screenW = Toolkit.getDefaultToolkit().getScreenSize().width;
		shellMain.setSize((int) (screenW * 0.8), (int) (screenH * 0.8));
		shellMain.setLocation((screenW - shellMain.getSize().x) / 2, (screenH - shellMain.getSize().y) / 2);
		shellMain.setImage(new Image(displayMain, Config.IconImagePath));

		LoadDataTypeOnFileTask loadDataTypeOnFileTask = new LoadDataTypeOnFileTask(CacheData.getDataTypes(),
				Config.DataTypeFilePath, new CellBack() {
					@Override
					public void cellback(Result result) {
						if (!result.isSuccessful()) {
							CommonDialog.showMessage(shellMain, "提示", result.getMessage(), SWT.ERROR);
						}
					}
				});
		LoadXMLContextTask linkNum = new LoadXMLContextTask(new CellBack() {
			@Override
			public void cellback(Result result) {
				if (!result.isSuccessful()) {
					CommonDialog.showMessage(shellMain, "提示", result.getMessage(), SWT.ERROR);
				}
			}
		});
		QueueThread.getInstance().AddThreadInQueue(loadDataTypeOnFileTask);
		QueueThread.getInstance().AddThreadInQueue(linkNum);

		Menu menu = new Menu(shellMain, SWT.BAR);
		shellMain.setMenuBar(menu);
		MenuItem mntmf = new MenuItem(menu, SWT.CASCADE);
		mntmf.setText("\u6587\u4EF6(F)");

		MenuItem mntme = new MenuItem(menu, SWT.CASCADE);
		mntme.setText("\u5DE5\u7A0B(P)");

		MenuItem mntmt = new MenuItem(menu, SWT.CASCADE);
		mntmt.setText("\u5DE5\u5177(T)");

		MenuItem mntmw = new MenuItem(menu, SWT.CASCADE);
		mntmw.setText("\u7A97\u53E3(W)");

		MenuItem mntmNewItem = new MenuItem(menu, SWT.CASCADE);
		mntmNewItem.setText("\u5E2E\u52A9(H)");

		Menu menu_1 = new Menu(shellMain, SWT.DROP_DOWN);
		mntmf.setMenu(menu_1);

		MenuItem mntmNewItem_1 = new MenuItem(menu_1, SWT.NONE);
		mntmNewItem_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String path = CommonDialog.openFileDialog(shellMain, "选择模块", new String[]{("*"+Config.SWCSuffix)}, SWT.OPEN);
				if(StringUtils.isNotEmpty(path)){
					ModuleModel model = ProjectTool.loadModuleModel(path);
					editFrame.loadSWCmodel(model, 0, 0,false);
				}
			}
		});
		mntmNewItem_1.setText("\u52A0\u8F7D\u6A21\u5757(L)");

		MenuItem mntmNewItem_3 = new MenuItem(menu_1, SWT.NONE);
		mntmNewItem_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (CommonDialog.showMessage(shellMain, "询问", "确认退出程序？",
						SWT.YES | SWT.NO | SWT.ICON_QUESTION) == SWT.YES) {
					System.exit(0);
				}
			}
		});
		mntmNewItem_3.setText("\u9000\u51FA(C)");

		Menu menu_2 = new Menu(shellMain, SWT.DROP_DOWN);
		mntme.setMenu(menu_2);

		MenuItem mntmNewItem_4 = new MenuItem(menu_2, SWT.NONE);
		mntmNewItem_4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String path = CommonDialog.openFileDialog(shellMain, "",new String[]{("*"+Config.ProSuffix)}, SWT.OPEN);
				if(StringUtils.isNotEmpty(path)){
					LoadProjectTask loadProjectTask = new LoadProjectTask(editFrame, path, new CellBack() {
						@Override
						public void cellback(final Result result) {
							Display.getDefault().syncExec(new Runnable() {
								@Override
								public void run() {
									if (!result.isSuccessful()) {
										CommonDialog.showMessage(shellMain, "提示", result.getReason(), SWT.ERROR);
									}else{
//										CommonDialog.showMessage(shellMain, "提示", result.getMessage(), SWT.ICON_INFORMATION);
									}
								}
							});
						}
					});
					QueueThread.getInstance().AddThreadInQueue(loadProjectTask);
					CommonDialog.progress(shellMain, loadProjectTask);
				}
			}
		});
		mntmNewItem_4.setText("\u6253\u5F00\u5DE5\u7A0B(OP)");
		
		MenuItem menuItem = new MenuItem(menu_2, SWT.NONE);
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				startSaveTask();
			}
		});
		menuItem.setText("\u5DE5\u7A0B\u53E6\u5B58\u4E3A(S)");

		Menu menu_3 = new Menu(shellMain, SWT.DROP_DOWN);
		mntmt.setMenu(menu_3);

		MenuItem mntmNewItem_6 = new MenuItem(menu_3, SWT.NONE);
		mntmNewItem_6.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (editFrame.getRteModel() != null) {
					new CreateXMLDialog(shellMain, editFrame.getRteModel(), CacheData.getDataTypes()).open();
				} else {
					CommonDialog.showMessage(shellMain, "提示", "请先添加模块", SWT.ERROR);
				}
			}
		});
		mntmNewItem_6.setText("\u8F93\u51FAXML\u6587\u4EF6(I)");
		mntmNewItem_6.setImage(new Image(displayMain, Config.ExportImagePath));

		MenuItem addDatatype = new MenuItem(menu_3, SWT.NONE);
		addDatatype.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				AddDataTypeDialog addDataTypeDialog = new AddDataTypeDialog(shellMain, CacheData.getDataTypes());
				addDataTypeDialog.open();
			}
		});
		addDatatype.setText("\u6DFB\u52A0DataType");
		addDatatype.setImage(new Image(displayMain, Config.DataTypeImagePath));

		MenuItem linkALlItem = new MenuItem(menu_3, SWT.NONE);
		linkALlItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				linkAll();
			}
		});
		linkALlItem.setText("\u5168\u90E8\u94FE\u63A5\u5230RTE(A)");
		linkALlItem.setImage(new Image(displayMain, Config.LinkImagePath));

		MenuItem cacelLinkALlItem = new MenuItem(menu_3, SWT.NONE);
		cacelLinkALlItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cacelLinkAll();
			}
		});
		cacelLinkALlItem.setText("\u53D6\u6D88\u5168\u90E8\u94FE\u63A5(Cancel)");
		cacelLinkALlItem.setImage(new Image(displayMain, Config.CancelLinkImagePath));

		MenuItem calcSWCCount = new MenuItem(menu_3, SWT.NONE);
		calcSWCCount.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int linkNum = 0;
				int unlinkNum = 0;
				if (editFrame != null) {
					Control[] frame = editFrame.getComposite().getChildren();
					if (frame.length > 1) {
						for (int i = 1; i < frame.length; i++) {
							SWCModel swcModel = (SWCModel) frame[i];
							if (swcModel.isLineToRTE()) {
								linkNum++;
							} else {
								unlinkNum++;
							}
						}
					}
				}
				CommonDialog.showMessage(shellMain, "计算结果",
						"SWC总数 " + (linkNum + unlinkNum) + ", 已链接 " + linkNum + ", 未链接 " + unlinkNum + "",
						SWT.ICON_INFORMATION);
			}
		});
		calcSWCCount.setText("\u8BA1\u7B97SWC\u6570\u91CF(Calc)");

		Menu menu_4 = new Menu(shellMain, SWT.DROP_DOWN);
		mntmw.setMenu(menu_4);

		MenuItem toolFrameItem = new MenuItem(menu_4, SWT.CASCADE);
		toolFrameItem.setText("\u5DE5\u5177\u680F\u7A97\u53E3");

		Menu menu_6 = new Menu(toolFrameItem);
		toolFrameItem.setMenu(menu_6);
		menu_6Item1 = new MenuItem(menu_6, SWT.RADIO);
		menu_6Item1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (pulgCom.isVisible()) {
					toDragShell();
				}
			}
		});
		menu_6Item1.setText("\u6D6E\u52A8");
		menu_6Item2 = new MenuItem(menu_6, SWT.RADIO);
		menu_6Item2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (shellDrag != null && !shellDrag.isDisposed()) {
					shellDrag.dispose();
					editCom.setBounds(0, toolBar.getSize().y, (int) (shellMain.getSize().x * 0.8),
							shellMain.getSize().y - toolBar.getSize().y - 55);
					pulgCom.setVisible(true);
				}
			}
		});
		menu_6Item2.setSelection(true);
		menu_6Item2.setText("\u56FA\u5B9A");

		Menu menu_5 = new Menu(shellMain, SWT.DROP_DOWN);
		mntmNewItem.setMenu(menu_5);

		MenuItem mntmNewItem_7 = new MenuItem(menu_5, SWT.NONE);
		mntmNewItem_7.setText(
				"\u6709\u7591\u95EE\u8BF7\u8054\u7CFB\u5F20\u8FBE\u5929\uFF08\u90AE\u7BB1:zhangdatian@xwoda.com\uFF09");

		toolBar = new ToolBar(shellMain, SWT.RIGHT);
		toolBar.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND));
		toolBar.setBounds(0, 0, shellMain.getSize().x - 20, 25);

		new ToolItem(toolBar, SWT.SEPARATOR);
		ToolItem saveTool = new ToolItem(toolBar, SWT.NONE);
		saveTool.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				startSaveTask();
			}
		});
		saveTool.setToolTipText("Save");
		saveTool.setImage(new Image(displayMain, Config.SaveImagePath));

		new ToolItem(toolBar, SWT.SEPARATOR);
		ToolItem refreshTool = new ToolItem(toolBar, SWT.NONE);
		refreshTool.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Composite composite = editFrame.getComposite();
				composite.redraw();
				if (composite.getChildren().length > 0) {
					RTEModel rteModel = (RTEModel) composite.getChildren()[0];
					GC gc = new GC(composite);
					for (int i = 1; i < composite.getChildren().length; i++) {
						SWCModel swcModel = (SWCModel) composite.getChildren()[i];
						if (swcModel.isLineToRTE()) {
							gc.drawLine(swcModel.getLocation().x + Config.SWCWidth / 2,
									swcModel.getLocation().y + Config.SWCHeight / 2,
									rteModel.getLocation().x + Config.RTCWidth / 2,
									rteModel.getLocation().y + Config.RTCHeight / 2);
						}
					}
					composite.layout();
					gc.dispose();

				}
			}
		});
		refreshTool.setToolTipText("\u5237\u65B0");
		refreshTool.setImage(new Image(displayMain, Config.refresh));

		new ToolItem(toolBar, SWT.SEPARATOR);
		ToolItem datatypeTool = new ToolItem(toolBar, SWT.NONE);
		datatypeTool.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				AddDataTypeDialog addDataTypeDialog = new AddDataTypeDialog(shellMain, CacheData.getDataTypes());
				addDataTypeDialog.open();
			}
		});
		datatypeTool.setToolTipText("\u6DFB\u52A0/\u5220\u9664DataType");
		datatypeTool.setImage(new Image(displayMain, Config.DataTypeImagePath));

		new ToolItem(toolBar, SWT.SEPARATOR);
		ToolItem linkAllTool = new ToolItem(toolBar, SWT.NONE);
		linkAllTool.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				linkAll();
			}
		});
		linkAllTool.setToolTipText("\u5168\u90E8\u94FE\u63A5\u5230RTE");
		linkAllTool.setImage(new Image(displayMain, Config.LinkImagePath));

		new ToolItem(toolBar, SWT.SEPARATOR);
		ToolItem cancelLinkAllTool = new ToolItem(toolBar, SWT.NONE);
		cancelLinkAllTool.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cacelLinkAll();
			}
		});
		cancelLinkAllTool.setToolTipText("\u53D6\u6D88\u5168\u90E8\u94FE\u63A5");
		cancelLinkAllTool.setImage(new Image(displayMain, Config.CancelLinkImagePath));

		new ToolItem(toolBar, SWT.SEPARATOR);
		ToolItem exportXML = new ToolItem(toolBar, SWT.NONE);
		exportXML.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (editFrame.getRteModel() != null) {
					new CreateXMLDialog(shellMain, editFrame.getRteModel(), CacheData.getDataTypes()).open();
				} else {
					CommonDialog.showMessage(shellMain, "提示", "请先添加模块", SWT.ERROR);
				}
			}
		});
		exportXML.setToolTipText("\u8F93\u51FAXML\u6587\u4EF6");
		exportXML.setImage(new Image(displayMain, Config.ExportImagePath));

		editCom = new Composite(shellMain, SWT.NONE);
		editCom.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		editCom.setBounds(0, toolBar.getSize().y, (int) (shellMain.getSize().x * 0.8),
				shellMain.getSize().y - toolBar.getSize().y - 55);
		editCom.setLayout(new FillLayout(SWT.HORIZONTAL));
		editFrame = new EditFrame(shellMain, editCom, SWT.NONE);
		editFrame.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		editCom.layout();

		// plug
		pulgCom = new Composite(shellMain, SWT.BORDER);
		pulgCom.setBounds(editCom.getSize().x + 5, toolBar.getSize().y, (int) (shellMain.getSize().x * 0.2 - 25),
				shellMain.getSize().y - toolBar.getSize().y - 65);
		pulgCom.setLayout(new FormLayout());
		Composite ti = new Composite(pulgCom, SWT.BORDER);
		ti.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND));
		ti.setLayout(new FormLayout());
		FormData fd_ti = new FormData();
		fd_ti.top = new FormAttachment(0);
		fd_ti.left = new FormAttachment(0);
		fd_ti.right = new FormAttachment(100);
		fd_ti.bottom = new FormAttachment(0, 20);
		ti.setLayoutData(fd_ti);
		Button btnNewButton = new Button(ti, SWT.PUSH);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				menu_6Item1.setSelection(true);
				menu_6Item2.setSelection(false);
				toDragShell();
			}
		});
		FormData fd_btnNewButton = new FormData();
		fd_btnNewButton.bottom = new FormAttachment(0, 14);
		fd_btnNewButton.right = new FormAttachment(100, 0);
		fd_btnNewButton.top = new FormAttachment(0);
		fd_btnNewButton.left = new FormAttachment(100, -20);
		btnNewButton.setLayoutData(fd_btnNewButton);
		btnNewButton.setImage(new Image(displayMain, Config.CloseImagePath));

		plugFrame = new PlugFrame(pulgCom, SWT.NONE);
		FormData fd_plugFrame = new FormData();
		fd_plugFrame.bottom = new FormAttachment(100);
		fd_plugFrame.right = new FormAttachment(ti, 0, SWT.RIGHT);
		fd_plugFrame.top = new FormAttachment(ti, 0);
		fd_plugFrame.left = new FormAttachment(ti, 0, SWT.LEFT);

		Label lblNewLabel = new Label(ti, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
		lblNewLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.left = new FormAttachment(0);
		lblNewLabel.setLayoutData(fd_lblNewLabel);
		lblNewLabel.setText("\u5DE5\u5177\u680F");

		plugFrame.setLayoutData(fd_plugFrame);
		pulgCom.layout();

		shellMain.addControlListener(new ControlListener() {

			@Override
			public void controlResized(ControlEvent e) {
				toolBar.setBounds(0, 0, shellMain.getSize().x - 20, 25);
				editCom.setBounds(0, toolBar.getSize().y, (int) (shellMain.getSize().x * 0.8),
						shellMain.getSize().y - toolBar.getSize().y - 55);
				pulgCom.setBounds(editCom.getSize().x, toolBar.getSize().y, (int) (shellMain.getSize().x * 0.2 - 20),
						shellMain.getSize().y - toolBar.getSize().y - 55);
			}

			@Override
			public void controlMoved(ControlEvent e) {
				// System.err.println("shell==="+shellMain.getBounds().x+"---"+shellMain.getBounds().y+"----"+shellMain.getBounds().width+"---"+shellMain.getBounds().height);
				// System.err.println("cus"+Display.getCurrent().getCursorLocation().x+"----"+Display.getCurrent().getCursorLocation().y);
			}
		});

		shellMain.layout();
		shellMain.open();

		while (!shellMain.isDisposed()) {
			if (!displayMain.readAndDispatch()) {
				displayMain.sleep();
			}
		}

	}

	// 浮动工具栏
	private void toDragShell() {
		pulgCom.setVisible(false);
		editCom.setBounds(0, toolBar.getSize().y, (int) (shellMain.getSize().x - 20),
				shellMain.getSize().y - toolBar.getSize().y - 55);
		shellDrag = new Shell(shellMain, SWT.CLOSE | SWT.RESIZE);
		shellDrag.setText("\u5DE5\u5177\u680F");
		shellDrag.setLayout(new FillLayout());
		OS.SetWindowPos(shellDrag.handle, 0, toolBar.getSize().x, toolBar.getSize().y,
				(int) (shellMain.getSize().x * 0.2 - 20), shellMain.getSize().y - toolBar.getSize().y - 55, SWT.NULL);
		plugFrame = new PlugFrame(shellDrag, SWT.NONE);
		shellDrag.layout();
		shellDrag.open();
		shellDrag.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				editCom.setBounds(0, toolBar.getSize().y, (int) (shellMain.getSize().x * 0.8),
						shellMain.getSize().y - toolBar.getSize().y - 55);
				pulgCom.setVisible(true);
				menu_6Item1.setSelection(false);
				menu_6Item2.setSelection(true);
			}
		});
	}
	
	
	private void startSaveTask(){
		String path = CommonDialog.openFileDialog(shellMain, "",new String[]{("*"+Config.ProSuffix)}, SWT.SAVE);
		if(StringUtils.isNotEmpty(path)){
			SaveProjectTask saveProjectTask = new SaveProjectTask(editFrame.getRteModel(), path, new CellBack() {
				@Override
				public void cellback(final Result result) {
					Display.getDefault().syncExec(new Runnable() {
						@Override
						public void run() {
							if (!result.isSuccessful()) {
								CommonDialog.showMessage(shellMain, "提示", result.getReason(), SWT.ERROR);
							}else{
								CommonDialog.showMessage(shellMain, "提示", result.getMessage(), SWT.ICON_INFORMATION);
							}
						}
					});
				}
			});
			QueueThread.getInstance().AddThreadInQueue(saveProjectTask);
			CommonDialog.progress(shellMain, saveProjectTask);
		}
	}
	
	

	private void linkAll() {
		if (editFrame != null) {
			Control[] frame = editFrame.getComposite().getChildren();
			if (frame.length > 1) {
				for (int i = 1; i < frame.length; i++) {
					SWCModel swcModel = (SWCModel) frame[i];
					if (!swcModel.drawLineThis()) {
						return;
					}
				}
			} else {
				CommonDialog.showMessage(shellMain, "提示", "请先添加Module!", SWT.ERROR);
			}
		}
	}

	private void cacelLinkAll() {
		if (editFrame != null) {
			Control[] frame = editFrame.getComposite().getChildren();
			if (frame.length > 1) {
				for (int i = 1; i < frame.length; i++) {
					SWCModel swcModel = (SWCModel) frame[i];
					swcModel.cacelLineThis();
				}
			} else {
				CommonDialog.showMessage(shellMain, "提示", "请先添加SWC!", SWT.ERROR);
			}
		}
	}
}
