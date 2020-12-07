package com.rabbitft.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

/**
 * @author ravi
 *
 *         Executor Thread Pool Facade, this class is responsible for creating
 *         new thread pool and adding new tasks to the pool. It also verify/walk
 *         through each submitted tasks to the pool.
 */
class ExecutorFacade {
	private int _threads = Runtime.getRuntime().availableProcessors() - 1;
	private ExecutorService _taskExecutorService;
	private final static Logger _logger = Logger.getLogger(ExecutorFacade.class);

	/**
	 * @param customThread
	 */
	public ExecutorFacade(int customThread) {
		super();
		this._threads = Math.min(_threads, customThread);
		this._taskExecutorService = Executors.newWorkStealingPool(_threads);
	}

	/**
	 * @param task
	 */
	public void submit(Task task) {
		_taskExecutorService.submit(new Worker(task));
	}

	/**
	 * Shutdown pool and executor service
	 */
	public void close() {
		_taskExecutorService.shutdown();
		try {
			if (!_taskExecutorService.awaitTermination(1, TimeUnit.HOURS)) {
				_taskExecutorService.shutdownNow();
			}
		} catch (InterruptedException e) {
			_logger.error(e.getMessage(), e);
		}
	}

	public int getThreadPoolSize() {
		return _threads;
	}

}
