package com.PD.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import com.FP.frame.Config;
import com.FP.frame.Dialog.CommonDialog;
import com.FP.frame.Dialog.SWCDesignDialog;
import com.PD.Tool.ExceptionTool;
import com.PD.Tool.ProjectTool;
import com.PD.Tool.SerializationTool;
import com.PD.cache.CacheData;

import org.eclipse.wb.swt.SWTResourceManager;

public class SWCModel extends Composite implements Serializable {

	private static final long serialVersionUID = 3643778841173732619L;
	private Button swcBut;
	private Composite parent;
	private Shell shellMain;
	private RTEModel rteData; // RTE数据
	private ModuleModel swcData; // 模组数据
	private boolean isLineToRTE;
	private List<SWCModel> swcModelList;
	private Menu menu;
	private MenuItem mntmNewItem_4;

	/**
	 * @wbp.parser.constructor
	 */
	public SWCModel(Shell shellMain, Composite parent, RTEModel rteData,List<SWCModel> swcModelList) {  //new
		super(parent, SWT.NONE);
		this.shellMain = shellMain;
		this.parent = parent;
		this.rteData = rteData;
		this.swcModelList = swcModelList;
		this.setSize(Config.SWCWidth, Config.SWCHeight);
		swcData = new ModuleModel();
		swcData.setName("SWC");
		swcData.setDataModels(new ArrayList<DataModel>());
		if (rteData != null) {
			rteData.addSwcModel(this);
		}
	}
	
	public SWCModel(Shell shellMain, Composite parent, RTEModel rteData,List<SWCModel> swcModelList,ModuleModel swcData,int x,int y,boolean isLineToRTE) {  //load
		super(parent,SWT.NONE);
		this.shellMain = shellMain;
		this.parent = parent;
		this.rteData = rteData;
		this.swcModelList = swcModelList;
		this.setSize(Config.SWCWidth, Config.SWCHeight);
		this.swcData = swcData;
		this.setLocation(x, y);
		this.isLineToRTE = isLineToRTE;
		if (rteData != null) {
			rteData.addSwcModel(this);
		}
	}
	

	public void open() {
		final SWCModel swcModel = this;
		swcBut = new Button(this, SWT.NONE);
		swcBut.setBounds(0, 0, Config.SWCWidth, Config.SWCHeight);
		swcBut.setText(swcData.getName());
		swcBut.setBackground(SWTResourceManager.getColor(255, 0, 0));
		swcBut.setImage(new Image(shellMain.getDisplay(), Config.SWCImagePath));
		swcBut.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				SWCDesignDialog swcDesignDialog = new SWCDesignDialog(shellMain, swcModel);
				swcDesignDialog.open();
				rteData.refreshSWC();
			}
		});
		DragSource dSource = new DragSource(swcBut, DND.DROP_MOVE);
		dSource.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dSource.addDragListener(new DragSourceAdapter() {

			@Override
			public void dragStart(DragSourceEvent event) {
				if (isLineToRTE) {
					parent.redraw();
				}
				swcModel.layout();
			}

			@Override
			public void dragSetData(DragSourceEvent event) {
				int x = Display.getCurrent().getCursorLocation().x - shellMain.getBounds().x
						- shellMain.getBorderWidth() * 2 - 20;
				int y = Display.getCurrent().getCursorLocation().y - shellMain.getBounds().y
						- (shellMain.getBounds().height - parent.getSize().y) - shellMain.getBorderWidth() * 2;
				swcModel.setLocation(x - Config.SWCWidth / 2, y);
				event.data = Config.MOVE + ":" + Config.SWC + ":" + swcBut.getText();
			}

			@Override
			public void dragFinished(DragSourceEvent event) {
				parent.redraw();
				drawLineALL();
			}
		});

		menu = new Menu(swcBut);
		swcBut.setMenu(menu);

		MenuItem mntmNewItem = new MenuItem(menu, SWT.NONE);
		mntmNewItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				SWCDesignDialog swcDesignDialog = new SWCDesignDialog(shellMain, swcModel);
				swcDesignDialog.open();
				rteData.refreshSWC();
			}
		});
		mntmNewItem.setText("\u7F16\u8F91(E)");

		MenuItem mntmNewItem_1 = new MenuItem(menu, SWT.NONE);
		mntmNewItem_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				/*
				 * SWCModel temp = (SWCModel)
				 * SerializationTool.copyObjec(swcModel); if(temp!=null){
				 * CacheData.setCopySWCModel(temp); }
				 */
			}
		});
		mntmNewItem_1.setText("\u590D\u5236(C)");

		MenuItem mntmNewItem_2 = new MenuItem(menu, SWT.NONE);
		mntmNewItem_2.setText("\u526A\u5207(T)");

		MenuItem mntmNewItem_3 = new MenuItem(menu, SWT.NONE);
		mntmNewItem_3.setText("\u5220\u9664(D)");
		mntmNewItem_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				parent.redraw();
				if (rteData != null) {
					if (rteData.getModuleModels() != null) {
						rteData.getModuleModels().remove(swcData);
					}
					rteData.removeSwcModel(swcModel);
					rteData.refreshSWC();
				}
				if(swcModelList!=null){
					swcModelList.remove(swcModel);
				}
				swcModel.dispose();
				drawLineALL();
			}
		});
		mntmNewItem_4 = new MenuItem(menu, SWT.NONE);
		mntmNewItem_4.setText("\u94FE\u63A5\u5230RTE(L)");
		
		MenuItem mntmNewItem_5 = new MenuItem(menu, SWT.NONE);
		mntmNewItem_5.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String path = CommonDialog.openFileDialog(shellMain,swcModel.getSwcData().getName(),new String[]{Config.SWCSuffix,"*.*"},SWT.SAVE);
				if(StringUtils.isNotEmpty(path)){
					try {
						ProjectTool.sendSWCModel(swcModel, path);
					} catch (IOException e1) {
						CommonDialog.showMessage(shellMain, "错误信息", ExceptionTool.getExceptionMessage(e1), SWT.ERROR);
					}
				}
			}
		});
		mntmNewItem_5.setText("\u4FDD\u5B58\u6A21\u5757");
		mntmNewItem_4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!isLineToRTE) {
					drawLineThis();
				} else {
					cacelLineThis();
				}
			}
		});

	}

	public boolean drawLineThis() {
		if (rteData.getModuleModels() == null) {
			CommonDialog.showMessage(shellMain, "提示", "请先加载BWS接口文件", SWT.ERROR);
			return false;
		}
		if (!isLineToRTE) {
			mntmNewItem_4.setText("\u53D6\u6D88\u94FE\u63A5(L)");
			RTEModel rteModel = (RTEModel) parent.getChildren()[0];
			GC gc = new GC(parent);
			gc.drawLine(this.getLocation().x + Config.SWCWidth / 2, this.getLocation().y + Config.SWCHeight / 2,
					rteModel.getLocation().x + Config.RTCWidth / 2, rteModel.getLocation().y + Config.RTCHeight / 2);
			swcBut.setBackground(SWTResourceManager.getColor(0, 255, 0));
			isLineToRTE = true;
			parent.layout();
			gc.dispose();
			rteData.getModuleModels().add(swcData);
		}
		return true;
	}

	
	public void drawLine() {
		if(isLineToRTE){
			mntmNewItem_4.setText("\u53D6\u6D88\u94FE\u63A5(L)");
			RTEModel rteModel = (RTEModel) parent.getChildren()[0];
			GC gc = new GC(parent);
			gc.drawLine(this.getLocation().x + Config.SWCWidth / 2, this.getLocation().y + Config.SWCHeight / 2,
					rteModel.getLocation().x + Config.RTCWidth / 2, rteModel.getLocation().y + Config.RTCHeight / 2);
			swcBut.setBackground(SWTResourceManager.getColor(0, 255, 0));
			isLineToRTE = true;
			parent.layout();
			gc.dispose();
			rteData.getModuleModels().add(swcData);
		}
	}
	
	
	
	public void cacelLineThis() {
		if (isLineToRTE){
			mntmNewItem_4.setText("\u94FE\u63A5\u5230RTE(L)");
			isLineToRTE = false;
			swcBut.setBackground(SWTResourceManager.getColor(255, 0, 0));
			parent.redraw();
			drawLineALL();
			rteData.getModuleModels().remove(swcData);
			rteData.refreshSWC();
		}
	}

	public void drawLineALL() {
		RTEModel rteModel = (RTEModel) parent.getChildren()[0];
		GC gc = new GC(parent);
		for (int i = 1; i < parent.getChildren().length; i++) {
			SWCModel swcModel = (SWCModel) parent.getChildren()[i];
			if (swcModel.isLineToRTE) {
				gc.drawLine(swcModel.getLocation().x + Config.SWCWidth / 2,
						swcModel.getLocation().y + Config.SWCHeight / 2, rteModel.getLocation().x + Config.RTCWidth / 2,
						rteModel.getLocation().y + Config.RTCHeight / 2);
			}
		}
		parent.layout();
		gc.dispose();
	}

	public void setModuleName(String name) {
		swcData.setName(name);
		swcBut.setText(name);
	}

	public boolean isLineToRTE() {
		return isLineToRTE;
	}

	public void setLineToRTE(boolean isLineToRTE) {
		this.isLineToRTE = isLineToRTE;
	}

	public RTEModel getRteData() {
		return rteData;
	}

	public void setRteData(RTEModel rteData) {
		this.rteData = rteData;
	}

	public ModuleModel getSwcData() {
		return swcData;
	}

	public void setSwcData(ModuleModel swcData) {
		this.swcData = swcData;
	}
}
