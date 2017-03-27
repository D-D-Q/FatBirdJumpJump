package com.pending.game.entityscript;

import com.pending.game.EntityScript;
import com.pending.game.GameConfig;
import com.pending.game.GameVar;
import com.pending.game.components.TransformComponent;
import com.pending.game.tools.MapperTools;

/**
 * 背景脚本
 * 
 * @author D
 * @date 2017年3月27日
 */
public class BgScript extends EntityScript{
	
	/**
	 * 第几个背景, 从上往底数
	 */
	public int index; 
	
	@Override
	public void update(float deltaTime) {
		
		TransformComponent transformComponent = MapperTools.transformCM.get(entity);
		
		transformComponent.position.set(0, GameVar.gameViewport.getCamera().position.y -  GameConfig.height/2 + index * 200);
	}
}
