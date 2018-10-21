package com.FP.frame;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.wb.swt.SWTResourceManager;

import com.FP.frame.Dialog.CommonDialog;
import com.PD.Task.ExportXMLTask;
import com.PD.Thread.CellBack;
import com.PD.Thread.QueueThread;
import com.PD.Thread.Result;
import com.PD.cache.CacheData;
import com.PD.model.ModuleModel;
import com.PD.model.RTEModel;
import com.PD.model.SWCModel;
import org.eclipse.swt.widgets.Text;

public class EditFrame extends Composite {

	private TabFolder editCom;
	private TabItem tbtmNewItem;
	private Shell shellMain;
	private Composite parent;
	private RTEModel rteModel;
	private Composite composite;
	private List<SWCModel> swcModelList = new ArrayList<>();
	private Text text;

	public EditFrame(Shell shellMain, Composite parent, int style) {
		super(parent, style);
		setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		this.setLayout(new FillLayout());
		this.shellMain = shellMain;
		this.parent = parent;
		createFrame();
		this.layout();
	}

	public void createFrame() {
		editCom = new TabFolder(this, SWT.BOTTOM);
		editCom.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = editCom.getSelectionIndex();
				if (index == 1) {
					if (rteModel == null || swcModelList.isEmpty()) {
						text.setText("");
						return;
					}
					ExportXMLTask exportXMLTask = new ExportXMLTask(rteModel.getSwcModelList(),
							CacheData.getDataTypes(), null, new CellBack() {
						@Override
						public void cellback(final Result result) {
							Display.getDefault().syncExec(new Runnable() {
								@Override
								public void run() {
									if (!result.isSuccessful()) {
										CommonDialog.showMessage(shellMain, "错误", result.getMessage(),
												SWT.ICON_INFORMATION);
									} else {
										text.setText(result.getMessage() == null ? "" : result.getMessage());
									}
								}
							});
						}
					});
					QueueThread.getInstance().AddThreadInQueue(exportXMLTask);
				}
			}
		});
		editCom.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		tbtmNewItem = new TabItem(editCom, SWT.CLOSE);
		tbtmNewItem.setText("Design");
		composite = new Composite(editCom, SWT.BORDER);
		tbtmNewItem.setControl(composite);
		DropTarget dropTarget = new DropTarget(composite, DND.DROP_MOVE);
		dropTarget.setTransfer(new Transfer[] { TextTransfer.getInstance() });

		Menu menu = new Menu(composite);
		composite.setMenu(menu);

		MenuItem mntmNewItem = new MenuItem(menu, SWT.NONE);
		mntmNewItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int x = Display.getCurrent().getCursorLocation().x - shellMain.getBounds().x
						- shellMain.getBorderWidth() * 2 - 20;
				int y = Display.getCurrent().getCursorLocation().y - shellMain.getBounds().y
						- (shellMain.getBounds().height - parent.getSize().y) - 30;

				/*
				 * SWCModel swcModel = CacheData.getCopySWCModel();
				 * if(swcModel!=null){ swcModel.setLocation(x-Config.SWCWidth/2,
				 * y); swcModelList.add(swcModel); }
				 */
			}
		});
		mntmNewItem.setText("\u7C98\u8D34");

		TabItem tbtmSource = new TabItem(editCom, SWT.NONE);
		tbtmSource.setText("Source");

		Composite composite_1 = new Composite(editCom, SWT.NONE);
		tbtmSource.setControl(composite_1);
		composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));

		text = new Text(composite_1, SWT.BORDER | SWT.READ_ONLY | SWT.V_SCROLL | SWT.MULTI);
		text.setForeground(SWTResourceManager.getColor(0, 0, 255));
		dropTarget.addDropListener(new DropTargetAdapter() {

			@Override
			public void dropAccept(DropTargetEvent event) {
				// TODO Auto-generated method stub

			}

			@Override
			public void drop(DropTargetEvent event) {
				// System.err.println(composite.getLocation().x+"..."+composite.getLocation().y+"..."+composite.getClientArea().x+"---"+composite.getClientArea().y+"---"+composite.getClientArea().width+"----"+composite.getClientArea().height+"***"+composite.getSize().x+"==="+composite.getSize().y);
				int x = Display.getCurrent().getCursorLocation().x - shellMain.getBounds().x
						- shellMain.getBorderWidth() * 2 - 20;
				int y = Display.getCurrent().getCursorLocation().y - shellMain.getBounds().y
						- (shellMain.getBounds().height - parent.getSize().y) - 30;

				if (event.data instanceof String) {
					String dataStr = (String) event.data;
					if (dataStr.contains(":")) {
						String[] temp = dataStr.split(":");
						switch (Integer.parseInt(temp[0])) {
						case Config.PUT: // 放置
						{
							switch (Integer.parseInt(temp[1])) {
							case Config.RTE: // RTE
								if (rteModel == null) {
									rteModel = new RTEModel(shellMain, composite, SWT.NONE);
									rteModel.open();
									rteModel.setLocation(x - Config.RTCWidth / 2, y);
								} else {
									CommonDialog.showMessage(shellMain, "提示", "已存在RTE!", SWT.ICON_INFORMATION);
									return;
								}
								break;
							case Config.SWC: // SWC
								if (rteModel == null) {
									CommonDialog.showMessage(shellMain, "提示", "请先创建RTE!", SWT.ICON_INFORMATION);
									return;
								} else {
									SWCModel swcModel = new SWCModel(shellMain, composite, rteModel,swcModelList);
									swcModel.open();
									swcModel.setModuleName("SWC" + swcModelList.size());
									swcModel.setLocation(x - Config.SWCWidth / 2, y);
									swcModelList.add(swcModel);
								}
								break;
							default:
								break;
							}
						}
							break;
						case Config.MOVE: // 移动

							break;
						default:
							break;
						}
					}

				}

			}

			@Override
			public void dragOver(DropTargetEvent event) {
				// TODO Auto-generated method stub

			}

			@Override
			public void dragOperationChanged(DropTargetEvent event) {
				// TODO Auto-generated method stub

			}

			@Override
			public void dragLeave(DropTargetEvent event) {
				// TODO Auto-generated method stub

			}

			@Override
			public void dragEnter(DropTargetEvent event) {
				// TODO Auto-generated method stub

			}
		});

		composite.addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent e) {
				// TODO Auto-generated method stub
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

	}

	public Composite getComposite() {
		return composite;
	}

	public RTEModel getRteModel() {
		return rteModel;
	}
	
	public void loadSWCmodel(ModuleModel model,int x,int y,boolean islink){
		if(isRepeatName(model)){
			CommonDialog.showMessage(shellMain, "提示", "已存在命名为"+model.getName()+"的模块!", SWT.ERROR);
			return ;
		}
		SWCModel swcModel = new SWCModel(shellMain, composite, rteModel,swcModelList,model,x,y,islink);
		swcModel.open();
		swcModel.drawLine();
		swcModelList.add(swcModel);
	}
	
	private void loadRTEModel(){
		rteModel = new RTEModel(shellMain, composite, SWT.NONE);
		rteModel.open();
		rteModel.setLocation(composite.getSize().x/2, composite.getSize().y/2);
	}
	
	public void clearAll(){
		composite.redraw();
		if(rteModel!=null){
			rteModel.dispose();
		}
		if(swcModelList!=null){
			for (int i = 0; i < swcModelList.size(); i++) {
				SWCModel swcModel = swcModelList.get(i);
				if(swcModel!=null){
					swcModel.dispose();
				}
			}
		}
		swcModelList = new ArrayList<>();
		loadRTEModel();
	}
	
	
	private boolean isRepeatName(ModuleModel model){
		if(model!=null){
			for (int i = 0; i < swcModelList.size(); i++) {
				SWCModel swcModel = swcModelList.get(i);
				if(swcModel!=null){
					if(swcModel.getSwcData()!=null && model.getName().equals(swcModel.getSwcData().getName())){
						return true;
					}
				}
			}
		}
		return false;
	}

}
