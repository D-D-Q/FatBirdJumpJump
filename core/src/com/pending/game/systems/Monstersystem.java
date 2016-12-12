package com.pending.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.pending.game.manager.AshleyManager;
import com.pending.game.support.GlobalInline;

/**
 * 障碍系统
 * 
 * @author D
 * @date 2016年11月29日 下午8:52:18
 */
public class Monstersystem extends EntitySystem {
	
	/**
	 * 间隔, 秒
	 */
	private float interval;
	
	/**
	 * 未执行的流逝时间
	 */
	private float accumulator;

	public Monstersystem (int priority) {
		super(priority);
		this.interval = 0;
		this.accumulator = 0;
		
	}

	@Override
	public final void update (float deltaTime) {
		accumulator += deltaTime;

		while (accumulator >= interval) {
			accumulator -= interval;
			if(updateInterval())
				break; // 防止卡住时间很长，出怪结束了还在循环
		}
	}

	/**
	 * 生成波数和BOSS
	 */
	protected boolean updateInterval (){
		
		
		AshleyManager ashleyManager = GlobalInline.instance.getAshleyManager();
		
		Entity entity = ashleyManager.entityDao.createEntity2(520, 2080);
		ashleyManager.engine.addEntity(entity);
		
		return false;
	}
}
