package com.pending.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.pending.game.EntityScript;

/**
 * 脚本组件，包含消息
 * 
 * @author D
 * @date 2016年9月15日 下午10:30:26
 */
public class ScriptComponent implements Component, Telegraph, Poolable{

	/**
	 * 脚本对象。
	 */
	public EntityScript script;
	

	@Override
	public boolean handleMessage(Telegram msg) {
		
		ScriptComponent senderScriptComponent = (ScriptComponent)msg.sender;
		return script.message(msg.message, senderScriptComponent.script.entity, msg.extraInfo, msg);
	}
	
	@Override
	public void reset() {
		script = null;
	}

}
