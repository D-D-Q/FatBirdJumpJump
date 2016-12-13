package com.pending.game.entityscript;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.pending.game.EntityScript;
import com.pending.game.components.PhysicsComponent;
import com.pending.game.manager.PhysicsManager;
import com.pending.game.tools.MapperTools;

/**
 * 英雄脚本
 * 
 * @author D
 * @date 2016年10月13日 下午9:56:24
 */
public class HeroScript extends EntityScript implements InputProcessor{
	
	private boolean isStart = false;
	
	
	private float maxSpeed = PhysicsManager.TIME_STEP * 2;
	
	private float time = 0.5f;
	
	@Override
	public boolean beginContact(Contact contact, Entity target) {
		
		PhysicsComponent physicsComponent = MapperTools.physicsCM.get(entity);
		if(isStart && physicsComponent.rigidBody.getLinearVelocity().y <= 0){
			
			
//			physicsComponent.rigidBody.setLinearVelocity(0, 2 * 120 / 0.5f);
		}
		
		return true;
	}
	
	@Override
	public void preSolve(Contact contact, Manifold oldManifold, Entity target) {
		
		PhysicsComponent physicsComponent = MapperTools.physicsCM.get(entity);
		if(physicsComponent.rigidBody.getLinearVelocity().y > 0){
			contact.setEnabled(false); // 向上禁用当前碰撞
		}
	}
	
	@Override
	public boolean keyDown (int keycode) {
		return true;
	}
	
	@Override
	public boolean keyUp(int keycode) {
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		
//		if(isStart)
//			return false;
		
		PhysicsComponent physicsComponent = MapperTools.physicsCM.get(entity);
		
//		(v2-v1)/t = g
//		2h=t*t*g
//		2h = (v2-v1)*t
		float v = 2 * 60 / 0.2f;
		physicsComponent.rigidBody.setLinearVelocity(0, v);
		
		isStart = true;
		
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public void update(float deltaTime) {
		
		PhysicsComponent physicsComponent = MapperTools.physicsCM.get(entity);
		if(!physicsComponent.rigidBody.getLinearVelocity().isZero())
			Gdx.app.log("", physicsComponent.rigidBody.getLinearVelocity().toString());
	}
}
