package com.pending.game.entityscript;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.pending.game.EntityScript;
import com.pending.game.components.PhysicsComponent;
import com.pending.game.support.GlobalInline;
import com.pending.game.systems.PhysicsSystem;
import com.pending.game.tools.MapperTools;

/**
 * 跳台脚本
 * 
 * @author D
 * @date 2016年12月14日
 */
public class BoardScript extends EntityScript{
	
	@Override
	public boolean endContact(Contact contact, Entity target) {
		
		if(contact.isEnabled()){
			
			PhysicsComponent physicsComponent = MapperTools.physicsCM.get(entity);
			PhysicsSystem physicsSystem = GlobalInline.instance.getAshleyManager().engine.getSystem(PhysicsSystem.class);
//			physicsSystem.physicsManager.addDisposeBody(physicsComponent.rigidBody);
		}
		
		return true;
	}
	
	@Override
	public void update(float deltaTime) {
		
	}
}
