package com.pending.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.pending.game.EntityScript;

/**
 * 脚本组件，包含消息
 * 
 * @author D
 * @date 2016年9月15日 下午10:30:26
 */
public class ScriptComponent implements Component, Poolable{

	/**
	 * 脚本对象。
	 */
	public EntityScript script;
	
	@Override
	public void reset() {
		script = null;
	}
}
