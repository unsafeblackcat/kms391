package server;

import java.util.concurrent.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 定时任务管理器 线程安全，支持注册周期性任务和延迟任务
 */
public class TimerManager {
	private static TimerManager instance;
	private final ScheduledExecutorService scheduler;
	private final AtomicInteger taskCounter = new AtomicInteger(0);
	private final Map<Integer, ScheduledFuture<?>> taskMap = new ConcurrentHashMap<>();

	// 私有构造器
	private TimerManager() {
		// 创建具有4个线程的调度线程池
		this.scheduler = Executors.newScheduledThreadPool(4, r -> {
			Thread t = new Thread(r, "TimerManager-Worker-" + taskCounter.incrementAndGet());
			t.setDaemon(true); // 设置为守护线程
			return t;
		});
	}

	/**
	 * 获取TimerManager单例
	 */
	public static synchronized TimerManager getInstance() {
		if (instance == null) {
			instance = new TimerManager();
		}
		return instance;
	}

	/**
	 * 注册周期性任务
	 * 
	 * @param task   要执行的任务
	 * @param delay  初始延迟(毫秒)
	 * @param period 执行周期(毫秒)
	 * @return 任务ID，可用于取消任务
	 */
	public int register(Runnable task, long delay, long period) {
		ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(wrapTask(task), delay, period, TimeUnit.MILLISECONDS);
		int taskId = taskCounter.incrementAndGet();
		taskMap.put(taskId, future);
		return taskId;
	}

	/**
	 * 注册一次性延迟任务
	 * 
	 * @param task  要执行的任务
	 * @param delay 延迟时间(毫秒)
	 * @return 任务ID
	 */
	public int schedule(Runnable task, long delay) {
		ScheduledFuture<?> future = scheduler.schedule(wrapTask(task), delay, TimeUnit.MILLISECONDS);
		int taskId = taskCounter.incrementAndGet();
		taskMap.put(taskId, future);
		return taskId;
	}

	/**
	 * 取消任务
	 * 
	 * @param taskId 任务ID
	 */
	public void cancel(int taskId) {
		ScheduledFuture<?> future = taskMap.remove(taskId);
		if (future != null) {
			future.cancel(false);
		}
	}

	/**
	 * 包装任务，添加异常处理
	 */
	private Runnable wrapTask(Runnable task) {
		return () -> {
			try {
				task.run();
			} catch (Exception e) {
				System.err.println("TimerTask 执行异常: " + e.getMessage());
				e.printStackTrace();
			}
		};
	}

	/**
	 * 关闭TimerManager，释放资源
	 */
	public void shutdown() {
		scheduler.shutdownNow();
		taskMap.clear();
	}
}