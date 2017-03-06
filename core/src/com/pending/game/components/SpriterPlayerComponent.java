package com.pending.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.brashmonkey.spriter.Player;

/**
 * Spriter的精灵组件
 * 
 * @author D
 * @date 2017年3月6日
 */
public class SpriterPlayerComponent implements Component, Poolable {

	public Player player;
	
	/** 
	 * 对象池回收组件调用
	 * @see com.badlogic.gdx.utils.Pool.Poolable#reset()
	 */
	@Override
	public void reset() {
		
		player = null;
	}
}
