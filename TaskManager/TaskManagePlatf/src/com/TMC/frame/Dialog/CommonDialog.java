package com.TMC.frame.Dialog;


import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.TMC.Thread.QueueRunnable;

public class CommonDialog {
	
	public static void progress(Shell shell,final QueueRunnable queueRunnable) {
		ProgressMonitorDialog pDialog = new ProgressMonitorDialog(shell);
		try {
			pDialog.run(true, false, new  IRunnableWithProgress(){
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
				{
					monitor.setTaskName(queueRunnable.getName());
					int sum=queueRunnable.getStep().length-1;
					monitor.beginTask(queueRunnable.getStep()[0],sum);
					int count=0;
					while(count<sum){
						int cur=queueRunnable.getNowStep();
						if(count<=cur){
							int addtion=cur-count;
							monitor.setTaskName(queueRunnable.getStep()[queueRunnable.getNowStep()]);
							count=cur;
							monitor.worked(addtion);
						}
						delay(50);
					}
					delay(500);
					monitor.done();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}

	public static String openFileDialog(Shell shell, String[] strings) {
		FileDialog fileDialog = new FileDialog(shell);
		fileDialog.setFileName("");
		fileDialog.setFilterExtensions(strings);
		return fileDialog.open();
	}

	public static String openFileDialog(Shell shell,String name,String[] strings,int style) {
		FileDialog fileDialog = new FileDialog(shell,style);
		fileDialog.setFileName(name);
		fileDialog.setFilterExtensions(strings);
		return fileDialog.open();
	}
	
	public static int showMessage(Shell shell, String title, String message, int style) {
		MessageBox messageBox = new MessageBox(shell, style);
		messageBox.setText(title);
		messageBox.setMessage(message);
		return messageBox.open();
	}

	public static void delay(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	public static String  openDirectoryDialog (Shell shell,String title,String message){
		DirectoryDialog  dialog = new DirectoryDialog(shell);
		dialog.setText(title);
		dialog.setMessage(message);
		return dialog.open();
	}

	public static void showMessageSyn(final Shell shell, final String title, final String message, final int style){
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				MessageBox messageBox = new MessageBox(shell, style);
				messageBox.setText(title);
				messageBox.setMessage(message+"");
				messageBox.open();
			}
		});
	}
	
	
}
