package com.PD.Thread;

public abstract class QueueRunnable implements Runnable {
	private String name;
	private String[] Step;
	private int nowStep;
	private CellBack cellBack; // 回调接口
	private Result result; // 任务执行结果
	abstract public void doTask();

	@Override
	public void run() {
		doTask();
		if (Step != null) {
			nowStep = Step.length - 1;
		}
		if (this.cellBack != null) {
			cellBack.cellback(result);
		}
	}

	protected String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	public String[] getStep() { // 公有
		return Step;
	}

	protected void setStep(String[] step) {
		Step = step;
	}

	public int getNowStep() { // 公有
		return nowStep;
	}

	protected void setNowStep(int nowStep) {
		this.nowStep = nowStep;
	}

	protected CellBack getCellBack() {
		return cellBack;
	}

	protected void setCellBack(CellBack cellBack) {
		this.cellBack = cellBack;
	}

	protected Result getResult() {
		return result;
	}

	protected void setResult(Result result) {
		this.result = result;
	}

}
