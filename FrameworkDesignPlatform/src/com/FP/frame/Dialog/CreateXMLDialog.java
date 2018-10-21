package com.FP.frame.Dialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.FD.model.DataModel.DataType;
import com.FP.frame.Config;
import com.PD.Task.ExportXMLTask;
import com.PD.Thread.CellBack;
import com.PD.Thread.QueueThread;
import com.PD.Thread.Result;
import com.PD.Tool.CreateXML;
import com.PD.model.DataModel;
import com.PD.model.ModuleModel;
import com.PD.model.RTEModel;
import com.PD.model.SWCModel;
import com.sun.org.apache.bcel.internal.generic.NEW;

import org.eclipse.swt.widgets.Label;

public class CreateXMLDialog extends Dialog {

	private Shell shlservice;
	private Text text;
	private RTEModel rteModel;
	private Set<DataType> dataTypes;
	private Text text_1;
	private Button okButton;

	public CreateXMLDialog(Shell parent, RTEModel rteModel, Set<DataType> dataTypes) {
		super(parent);
		this.rteModel = rteModel;
		this.dataTypes = dataTypes;
	}

	public void open() {
		shlservice = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.MAX | SWT.RESIZE);
		shlservice.setText("ExportXML");
		shlservice.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
		shlservice.setSize(443, 607);
		shlservice.setImage(new Image(shlservice.getDisplay(), Config.ExportImagePath));
		shlservice.setLocation(getParent().getLocation().x + getParent().getSize().x / 2 - shlservice.getSize().x / 2,
				getParent().getLocation().y + getParent().getSize().y / 2 - shlservice.getSize().y / 2);
		shlservice.setLayout(new FillLayout());

		// 函数
		Composite composite = new Composite(shlservice, SWT.BORDER);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND));
		composite.setLayout(new FormLayout());

		text = new Text(composite, SWT.BORDER | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		text.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_RED));
		text.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
		text.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		FormData fd_text_11 = new FormData();
		fd_text_11.left = new FormAttachment(0, 0);
		fd_text_11.right = new FormAttachment(100, 0);
		text.setLayoutData(fd_text_11);

		okButton = new Button(composite, SWT.NONE);
		fd_text_11.bottom = new FormAttachment(okButton, -10);
		okButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				text.setText("");
				text.setText(getInfo());
				String path = text_1.getText();
				if (path == null || path.isEmpty()) {
					CommonDialog.showMessage(shlservice, "提示", "请选择文件", SWT.ICON_INFORMATION);
					return;
				}
				ExportXMLTask exportXMLTask = new ExportXMLTask(rteModel.getSwcModelList(), dataTypes, path,
						new CellBack() {
					@Override
					public void cellback(final Result result) {
						Display.getDefault().syncExec(new Runnable() {
							@Override
							public void run() {
								if (!result.isSuccessful()) {
									CommonDialog.showMessage(shlservice, "错误", result.getReason(),
											SWT.ICON_INFORMATION);
								} else {
									text.append(result.getMessage());
								}
							}
						});
					}
				});
				QueueThread.getInstance().AddThreadInQueue(exportXMLTask);
				CommonDialog.progress(shlservice, exportXMLTask);
			}
		});
		FormData fd_okButton = new FormData();
		fd_okButton.bottom = new FormAttachment(100, -10);
		fd_okButton.left = new FormAttachment(50, -40);
		fd_okButton.right = new FormAttachment(50, 40);
		okButton.setLayoutData(fd_okButton);
		okButton.setImage(new Image(shlservice.getDisplay(), Config.ExportImagePath));
		okButton.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		okButton.setText("Export");

		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.left = new FormAttachment(0);
		fd_lblNewLabel.top = new FormAttachment(0, 7);
		lblNewLabel.setLayoutData(fd_lblNewLabel);
		lblNewLabel.setText("\u6587\u4EF6\u8DEF\u5F84:");

		text_1 = new Text(composite, SWT.BORDER);
		FormData fd_text_1 = new FormData();
		fd_text_1.top = new FormAttachment(0, 4);
		fd_text_1.left = new FormAttachment(lblNewLabel, 6);
		text_1.setLayoutData(fd_text_1);

		Button btnNewButton = new Button(composite, SWT.NONE);
		fd_text_11.top = new FormAttachment(btnNewButton, 6);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String path = CommonDialog.openFileDialog(shlservice,"", new String[] { "*.xml", "*.*" },SWT.SAVE);
				if (path != null && !path.isEmpty()) {
					text_1.setText(path);
				}
			}
		});
		fd_text_1.right = new FormAttachment(btnNewButton, -6);
		FormData fd_btnNewButton = new FormData();
		fd_btnNewButton.right = new FormAttachment(100);
		fd_btnNewButton.top = new FormAttachment(lblNewLabel, -5, SWT.TOP);
		btnNewButton.setLayoutData(fd_btnNewButton);
		btnNewButton.setText("\u9009\u62E9\u6587\u4EF6");

		text.setText("");
		text.setText(getInfo());
		shlservice.layout();
		shlservice.open();
		Display display = shlservice.getDisplay();
		while (!shlservice.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	private String getInfo() {
		int linkNum = 0;
		int unlinkNum = 0;
		StringBuffer info = new StringBuffer();
		if (rteModel != null) {
			List<SWCModel> sList = rteModel.getSwcModelList();
			for (int i = 0; i < sList.size(); i++) {
				SWCModel swcModel = sList.get(i);
				ModuleModel model = swcModel.getSwcData();
				if (model != null) {
					info.append("模块名称:").append(model.getName()).append("\t类型:")
							.append(Config.ModelC_Code[model.getFrameModel()]).append("\t周期:").append(model.getTime())
							.append("ms");
					List<DataModel> dList = swcModel.getSwcData().getDataModels();
					if (dList != null) {
						info.append("\t信号&方程:").append(swcModel.getSwcData().getDataModels().size());
					}
				}
				if (swcModel.isLineToRTE()) {
					linkNum++;
					info.append("\t链接状态:已连接");
					info.append("\r\n");
				} else {
					unlinkNum++;
					info.append("\t链接状态:未连接");
					info.append("\r\n");
				}
			}
		}
		info.append("模块总数").append((linkNum + unlinkNum) + "").append(":").append("链接数").append(linkNum).append("\r\n");
		return info.toString();
	}

}
