package com.huanshi.game.entityscript;

import com.huanshi.game.EntityScript;
import com.huanshi.game.GameConfig;
import com.huanshi.game.GameVar;
import com.huanshi.game.components.TransformComponent;
import com.huanshi.game.tools.MapperTools;

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
		
		float offet = GameVar.gameViewport.getCamera().position.y/100;
		
		transformComponent.position.set(0, GameVar.gameViewport.getCamera().position.y -  GameConfig.height/2 + (index-1) * 200 - offet);
	}
}
