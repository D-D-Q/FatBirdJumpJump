package com.pending.game.entityscript;

import com.badlogic.gdx.math.Vector2;
import com.pending.game.EntityScript;
import com.pending.game.GameVar;
import com.pending.game.GameConfig;
import com.pending.game.components.PhysicsComponent;
import com.pending.game.components.TransformComponent;
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
		
		TransformComponent transformComponent = MapperTools.transformCM.get(entity);
		Vector2 position = transformComponent.position;
		
		Float boardMax = GlobalInline.instance.get("jumPBoardY");
		
		if(boardMax !=null && position.y <= boardMax){
			
			if(position.y < GameVar.gameViewport.getCamera().position.y - GameConfig.height/2){
				GlobalInline.instance.getAshleyManager().engine.removeEntity(entity);
			}
			else{
				PhysicsComponent physicsComponent = MapperTools.physicsCM.get(entity);
//				physicsComponent.rigidBody.setLinearVelocity(0, -PhysicsManager.MAX_SPEED);
				physicsComponent.rigidBody.setAwake(true);
				physicsComponent.rigidBody.setGravityScale(1);
			}
		}
	}
}
