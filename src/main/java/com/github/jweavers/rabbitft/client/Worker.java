package com.github.jweavers.rabbitft.client;

import java.util.concurrent.Callable;

/**
 * @author ravi Create worker for each task to be executed.
 */
class Worker implements Callable<Boolean> {

	private Task task;

	public Worker(Task task) {
		this.task = task;
	}

	@Override
	public Boolean call() {
		task.uploadFile();
		return true;
	}
}
