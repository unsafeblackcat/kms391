package client;

import server.life.MobSkill;
import java.util.concurrent.ScheduledFuture;

public class MapleDiseases {
	// 使用final修饰不可变字段
	private final int value;
	private final long startTime;

	// 使用泛型参数
	private MobSkill mobskill;
	private ScheduledFuture<?> schedule;
	private int duration;

	// 构造函数增加参数验证
	public MapleDiseases(int value, int duration, long startTime) {
		if (duration < 0) {
			throw new IllegalArgumentException("Duration cannot be negative");
		}
		this.value = value;
		this.duration = duration;
		this.startTime = startTime;
	}

	// Getter方法增加空值检查注解
	public MobSkill getMobskill() {
		return this.mobskill;
	}

	public void setMobskill(MobSkill mobskill) {
		this.mobskill = mobskill;
	}

	// 使用泛型参数
	public ScheduledFuture<?> getSchedule() {
		return this.schedule;
	}

	public void setSchedule(ScheduledFuture<?> schedule) {
		this.schedule = schedule;
	}

	public int getDuration() {
		return this.duration;
	}

	public void setDuration(int duration) {
		if (duration < 0) {
			throw new IllegalArgumentException("Duration cannot be negative");
		}
		this.duration = duration;
	}

	public long getStartTime() {
		return this.startTime;
	}

	public int getValue() {
		return this.value;
	}

	// 移除setValue方法，使value成为不可变字段
}