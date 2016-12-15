package com.pending.game.entityscript;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
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
	
	private boolean isDispose = false;
	
	@Override
	public boolean beginContact(Contact contact, Entity target) {
		
		if(contact.isEnabled()){
			isDispose = true;
		}
		
		return true;
	}
	
	@Override
	public void preSolve(Contact contact, Manifold oldManifold, Entity target) {
		
	}
	
	@Override
	public void update(float deltaTime) {
		
	}
}
