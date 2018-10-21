package com.PD.Thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import com.FP.frame.Dialog.CommonDialog;

public class QueueThread {

	private static QueueThread queueThread = new QueueThread();
	private ExecutorService threadService = Executors.newSingleThreadExecutor();

	private QueueThread() {
	}

	public static QueueThread getInstance() {
		return queueThread;
	}

	public void AddThreadInQueue(QueueRunnable qRunnable) {
		threadService.execute(qRunnable);
	}

}
