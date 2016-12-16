package com.pending.game.entityscript;

import com.pending.game.EntityScript;
import com.pending.game.components.PhysicsComponent;
import com.pending.game.support.GlobalInline;
import com.pending.game.tools.MapperTools;

/**
 * 跳台脚本
 * 
 * @author D
 * @date 2016年12月14日
 */
public class BoardScript extends EntityScript{
	
	@Override
	public void update(float deltaTime) {
		
		PhysicsComponent physicsComponent = MapperTools.physicsCM.get(entity);
		
		Float boardMax = GlobalInline.instance.get("jumPBoardY");
		
		if(boardMax !=null && physicsComponent.rigidBody.getPosition().y <= boardMax){
			GlobalInline.instance.getAshleyManager().engine.removeEntity(entity);
		}
	}
}
