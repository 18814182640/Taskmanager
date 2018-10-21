package com.TMC.Thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.sun.org.glassfish.gmbal.Description;

public class QueueThread {

	private static QueueThread queueThread = new QueueThread();
	private ExecutorService threadService = Executors.newSingleThreadExecutor();
    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(); //固定长度的
	private QueueThread() {
	}

	public static QueueThread getInstance() {
		return queueThread;
	}

	public void AddThreadInQueue(QueueRunnable qRunnable) {
		threadService.execute(qRunnable);
	}
	
	
	public void addscheduleWithFixedDelayThread(QueueRunnable qRunnable,int time){
		scheduledExecutorService.scheduleWithFixedDelay(qRunnable, 1000, time, TimeUnit.MILLISECONDS);
	}
	
	@Description("只建议在进程退出时调用")
	public void shutdown(){
		threadService.shutdownNow();
		scheduledExecutorService.shutdownNow();
	}
	
	
}
