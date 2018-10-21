package com.TMS.frame;

import java.awt.Toolkit;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.TMS.service.RequestHandler;
import com.TMS.service.ServerBoot;

import io.netty.channel.ChannelHandlerContext;

public class MainFrame {
	private Display displayMain;
	private Shell shellMain;
	private static MainFrame mainFrame;
	private Table table;
	public static void main(String[] args) {
		
		mainFrame = new MainFrame();
//		mainFrame.startServer();
		mainFrame.initFrame();

	}
	
	private void  initFrame(){
		displayMain = new Display();
		shellMain = new Shell(displayMain,SWT.SHELL_TRIM );
		shellMain.setText("任务管理系统");
		int screenH = Toolkit.getDefaultToolkit().getScreenSize().height;
		int screenW = Toolkit.getDefaultToolkit().getScreenSize().width;
		shellMain.setSize(640, 320);
		shellMain.setLocation((screenW - shellMain.getSize().x) / 2, (screenH - shellMain.getSize().y) / 2);
		shellMain.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		table = new Table(shellMain, SWT.BORDER | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.CENTER);
		tblclmnNewColumn.setWidth(100);
		tblclmnNewColumn.setText("序号");
		
		TableColumn tableColumn = new TableColumn(table, SWT.CENTER);
		tableColumn.setWidth(150);
		tableColumn.setText("工号");
		
		TableColumn tblclmnIpport = new TableColumn(table, SWT.CENTER);
		tblclmnIpport.setWidth(200);
		tblclmnIpport.setText("IP/Port");
		
		TableColumn tableColumn_2 = new TableColumn(table, SWT.CENTER);
		tableColumn_2.setWidth(100);
		tableColumn_2.setText("状态");
		
		Menu menu = new Menu(shellMain, SWT.BAR);
		shellMain.setMenuBar(menu);
		
		MenuItem startMenu = new MenuItem(menu, SWT.CASCADE);
		startMenu.setText("option");
		
		Menu startMenuDrop = new Menu(shellMain, SWT.DROP_DOWN);
		startMenu.setMenu(startMenuDrop);
		
		final MenuItem mntmNewItem = new MenuItem(startMenuDrop, SWT.NONE);
		mntmNewItem.setText("开始");
		
		final MenuItem menuItem = new MenuItem(startMenuDrop, SWT.NONE);
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ServerBoot.getInstance().close();
				mntmNewItem.setEnabled(true);
				menuItem.setEnabled(false);
			}
		});
		menuItem.setEnabled(false);
		menuItem.setText("关闭");
		
		mntmNewItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				startServer();
				mntmNewItem.setEnabled(false);
				menuItem.setEnabled(true);
			}
		});
		
		
		new Thread(new Runnable() {
			public void run() {
				while(true){
					try {
						Display.getDefault().syncExec(new Runnable() {
							public void run() {
								table.removeAll();
								ConcurrentHashMap<String, ChannelHandlerContext>  map = RequestHandler.AllLogginClient;
								Set<Entry<String, ChannelHandlerContext>> set = map.entrySet();
								int i=1;
								for (Entry<String, ChannelHandlerContext> entry : set) {
									TableItem tableItem= new TableItem(table, SWT.NONE);
									tableItem.setText(0,i+"");
									tableItem.setText(1,entry.getKey());
									tableItem.setText(2, entry.getValue().channel().remoteAddress().toString());
									tableItem.setText(3, "在线");
									i++;
								}
							}
						});
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		
		shellMain.layout();
		shellMain.open();
		while (!shellMain.isDisposed()) {
			if (!displayMain.readAndDispatch()) {
				displayMain.sleep();
			}
		}
		ServerBoot.getInstance().close();
		System.exit(0);
	}

	private void startServer(){
		new Thread(new  Runnable() {
			public void run() {
				ServerBoot.getInstance().start();
			}
		}).start();
	}
}
