package com.FP.frame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

public class PlugFrame extends Composite {

	private Button swcBut, rteBut;

	public PlugFrame(Composite parent, int style) {
		super(parent, style);
		setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		createFrame();
	}

	public void createFrame() {
		// swc
		swcBut = new Button(this, SWT.NONE);
		swcBut.setBounds(73, 26, 80, 56);
		swcBut.setText("SWC");
		swcBut.setImage(new Image(getDisplay(), Config.SWCImagePath));
		DragSource dragSource = new DragSource(swcBut, DND.DROP_MOVE);
		dragSource.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dragSource.addDragListener(new DragSourceAdapter() {

			@Override
			public void dragStart(DragSourceEvent event) {

			}

			@Override
			public void dragSetData(DragSourceEvent event) {
				event.data = Config.PUT + ":" + Config.SWC + ":" + swcBut.getText();
			}

			@Override
			public void dragFinished(DragSourceEvent event) {

			}
		});

		// plug
		rteBut = new Button(this, SWT.NONE);
		rteBut.setBounds(73, 115, 80, 56);
		rteBut.setText("RTE");
		rteBut.setImage(new Image(getDisplay(), Config.RTEImagePath));
		// rteBut.setBackground(color);
		DragSource dSource = new DragSource(rteBut, DND.DROP_MOVE);
		dSource.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dSource.addDragListener(new DragSourceAdapter() {

			@Override
			public void dragStart(DragSourceEvent event) {

			}

			@Override
			public void dragSetData(DragSourceEvent event) {
				event.data = Config.PUT + ":" + Config.RTE + ":" + swcBut.getText();
			}

			@Override
			public void dragFinished(DragSourceEvent event) {

			}
		});

	}

}
