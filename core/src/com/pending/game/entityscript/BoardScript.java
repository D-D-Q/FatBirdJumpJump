package com.pending.game.entityscript;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.pending.game.EntityScript;
import com.pending.game.GameConfig;
import com.pending.game.GameVar;
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
	
	public Entity sensor;
	
	@Override
	public void update(float deltaTime) {
		
		TransformComponent transformComponent = MapperTools.transformCM.get(entity);
		Vector2 position = transformComponent.position;
		
		Vector2 jumpBoardPosition = GlobalInline.instance.get("jumpBoardPosition");
		
		if(jumpBoardPosition !=null && position.y < jumpBoardPosition.y){
			
			if(position.y < GameVar.gameViewport.getCamera().position.y - GameConfig.height/2){
				if(entity != null){
					GlobalInline.instance.getAshleyManager().engine.removeEntity(entity);
					entity = null;
				}
			}
			else{
				PhysicsComponent physicsComponent = MapperTools.physicsCM.get(entity);
//				physicsComponent.rigidBody.setLinearVelocity(0, -PhysicsManager.MAX_SPEED);
				physicsComponent.rigidBody.setAwake(true);
				physicsComponent.rigidBody.setGravityScale(1);
				
				if(sensor != null){
					GlobalInline.instance.getAshleyManager().engine.removeEntity(sensor);
					sensor = null;
				}
			}
		}
	}
}
