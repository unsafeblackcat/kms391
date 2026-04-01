package server.games;

import client.MapleCharacter;
import server.Randomizer;

import java.util.ArrayList;
import java.util.List;

/**
 * 这个类代表彩色邀请卡游戏的实例，用于管理与游戏相关的数据，如连击数、计量表、得分等。
 */
public class ColorInvitationCard {

	// 与该彩色邀请卡游戏实例关联的玩家角色
	private MapleCharacter chr;

	// 当前的连击数
	private int Combo;

	// 游戏中的计量表数值
	private int Gauge;

	// 玩家在游戏中获得的总得分
	private int Point;

	// 红色卡片成功操作的次数
	private int Redsuc;

	// 蓝色卡片成功操作的次数
	private int Bluesuc;

	// 绿色卡片成功操作的次数
	private int Greensuc;

	// 玩家在游戏中失败的次数
	private int FailCount;

	// 总的连击数
	private int ComboCount;

	// 表示是否处于狂热时间的标志
	private boolean FeverTime;

	// 存储卡片类型的列表，每个元素代表一张卡片，值为 0、1 或 2
	private List<Integer> CardList;

	/**
	 * 构造函数，初始化彩色邀请卡游戏实例。
	 * 
	 * @param chr 与该游戏实例关联的玩家角色
	 */
	public ColorInvitationCard(MapleCharacter chr) {
		// 初始化卡片列表
		this.CardList = new ArrayList<>();
		// 关联玩家角色
		this.chr = chr;
		// 初始化连击数为 0
		this.Combo = 0;
		// 初始化计量表数值为 0
		this.Gauge = 0;
		// 初始化总得分值为 0
		this.Point = 0;
		// 初始化红色卡片成功操作次数为 0
		this.Redsuc = 0;
		// 初始化蓝色卡片成功操作次数为 0
		this.Bluesuc = 0;
		// 初始化绿色卡片成功操作次数为 0
		this.Greensuc = 0;
		// 初始化失败次数为 0
		this.FailCount = 0;
		// 初始化总连击数为 0
		this.ComboCount = 0;
		// 初始化狂热时间标志为 false
		this.FeverTime = false;
		// 生成 1000 张随机卡片，卡片类型为 0、1 或 2
		for (int i = 0; i < 1000; i++) {
			this.CardList.add(Integer.valueOf(Randomizer.rand(0, 2)));
		}
	}

	/**
	 * 获取与该游戏实例关联的玩家角色。
	 * 
	 * @return 玩家角色对象
	 */
	public MapleCharacter getChr() {
		return this.chr;
	}

	/**
	 * 设置与该游戏实例关联的玩家角色。
	 * 
	 * @param chr 玩家角色对象
	 */
	public void setChr(MapleCharacter chr) {
		this.chr = chr;
	}

	/**
	 * 获取当前的连击数。
	 * 
	 * @return 当前连击数
	 */
	public int getCombo() {
		return this.Combo;
	}

	/**
	 * 设置当前的连击数。
	 * 
	 * @param Combo 新的连击数
	 */
	public void setCombo(int Combo) {
		this.Combo = Combo;
	}

	/**
	 * 获取游戏中的计量表数值。
	 * 
	 * @return 计量表数值
	 */
	public int getGauge() {
		return this.Gauge;
	}

	/**
	 * 设置游戏中的计量表数值。
	 * 
	 * @param Gauge 新的计量表数值
	 */
	public void setGauge(int Gauge) {
		this.Gauge = Gauge;
	}

	/**
	 * 获取玩家在游戏中获得的总得分。
	 * 
	 * @return 总得分
	 */
	public int getPoint() {
		return this.Point;
	}

	/**
	 * 设置玩家在游戏中获得的总得分。
	 * 
	 * @param Point 新的总得分
	 */
	public void setPoint(int Point) {
		this.Point = Point;
	}

	/**
	 * 获取红色卡片成功操作的次数。
	 * 
	 * @return 红色卡片成功操作次数
	 */
	public int getRedsuc() {
		return this.Redsuc;
	}

	/**
	 * 设置红色卡片成功操作的次数。
	 * 
	 * @param Redsuc 新的红色卡片成功操作次数
	 */
	public void setRedsuc(int Redsuc) {
		this.Redsuc = Redsuc;
	}

	/**
	 * 获取蓝色卡片成功操作的次数。
	 * 
	 * @return 蓝色卡片成功操作次数
	 */
	public int getBluesuc() {
		return this.Bluesuc;
	}

	/**
	 * 设置蓝色卡片成功操作的次数。
	 * 
	 * @param Bluesuc 新的蓝色卡片成功操作次数
	 */
	public void setBluesuc(int Bluesuc) {
		this.Bluesuc = Bluesuc;
	}

	/**
	 * 获取绿色卡片成功操作的次数。
	 * 
	 * @return 绿色卡片成功操作次数
	 */
	public int getGreensuc() {
		return this.Greensuc;
	}

	/**
	 * 设置绿色卡片成功操作的次数。
	 * 
	 * @param Greensuc 新的绿色卡片成功操作次数
	 */
	public void setGreensuc(int Greensuc) {
		this.Greensuc = Greensuc;
	}

	/**
	 * 获取玩家在游戏中失败的次数。
	 * 
	 * @return 失败次数
	 */
	public int getFailCount() {
		return this.FailCount;
	}

	/**
	 * 设置玩家在游戏中失败的次数。
	 * 
	 * @param FailCount 新的失败次数
	 */
	public void setFailCount(int FailCount) {
		this.FailCount = FailCount;
	}

	/**
	 * 获取总的连击数。
	 * 
	 * @return 总的连击数
	 */
	public int getComboCount() {
		return this.ComboCount;
	}

	/**
	 * 设置总的连击数。
	 * 
	 * @param ComboCount 新的总的连击数
	 */
	public void setComboCount(int ComboCount) {
		this.ComboCount = ComboCount;
	}

	/**
	 * 检查是否处于狂热时间。
	 * 
	 * @return 如果处于狂热时间返回 true，否则返回 false
	 */
	public boolean isFeverTime() {
		return this.FeverTime;
	}

	/**
	 * 设置是否处于狂热时间。
	 * 
	 * @param FeverTime 新的狂热时间标志
	 */
	public void setFeverTime(boolean FeverTime) {
		this.FeverTime = FeverTime;
	}

	/**
	 * 获取存储卡片类型的列表。
	 * 
	 * @return 卡片类型列表
	 */
	public List<Integer> getCardList() {
		return this.CardList;
	}

	/**
	 * 设置存储卡片类型的列表。
	 * 
	 * @param CardList 新的卡片类型列表
	 */
	public void setCardList(List<Integer> CardList) {
		this.CardList = CardList;
	}
}