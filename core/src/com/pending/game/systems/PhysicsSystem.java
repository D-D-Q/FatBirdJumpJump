package com.pending.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.pending.game.components.PhysicsComponent;
import com.pending.game.components.ScriptComponent;
import com.pending.game.components.TransformComponent;
import com.pending.game.manager.PhysicsManager;
import com.pending.game.tools.FamilyTools;
import com.pending.game.tools.MapperTools;

/**
 * 物理系统，碰撞检测系统
 * 
 * @author Administrator
 * @date 2016年10月12日
 */
public class PhysicsSystem extends IteratingSystem implements ContactListener{
	
	/**
	 * 更新物理引擎的时间量
	 */
	private float accumulator;
	
	private float TIME_STEP = 1/PhysicsManager.PYHSICS_FPS;
	
	public PhysicsManager physicsManager = new PhysicsManager();

	public PhysicsSystem(int priority) {
		super(FamilyTools.physicsF, priority);
		
		accumulator = 0;
		
		physicsManager.world.setContactListener(this); // 碰撞监听
		
//		InputManager.instance.addProcessor(new InputAdapter(){
//			
//			private Vector2 from = new Vector2();
//			private Vector2 to = new Vector2();
//			
//			Body body = null;
//			
//			@Override
//			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
//				
//				Vector3 vector3 = GAME.gameViewport.getCamera().unproject(new Vector3(screenX, screenY, 0));
//				from.set(vector3.x, vector3.y);
//				
//				return true;
//			}
//			
//			@Override
//			public boolean touchDragged(int screenX, int screenY, int pointer) {
//				
//				Vector3 vector3 = GAME.gameViewport.getCamera().unproject(new Vector3(screenX, screenY, 0));
//				to.set(vector3.x, vector3.y);
//				
//				physicsManager.world.rayCast(new RayCastCallback() {
//					
//					@Override
//					public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
//						
////						physicsManager.disposeBody(fixture.getBody());
//						body = fixture.getBody();
//						return fraction;
//					}
//				}, from, to);
//
//				from.set(to);
//				
//				physicsManager.disposeBody(body);
//				
//				return true;
//			}
//		});
	}
	
	/**
	 * 更新物理世界
	 * 物理引擎需要固定时间更新，不能跟随帧数，因为帧数不稳定
	 */
	@Override
	public void update(float deltaTime) {
		
		float frameTime = Math.min(deltaTime, 0.25f); // 最大帧间隔时间0.25，防止死亡螺旋（spiral of death）
	    accumulator += frameTime;
	    
	    while (accumulator >= TIME_STEP) {
	    	physicsManager.world.step(TIME_STEP, PhysicsManager.VELOCITY_ITERATIONS, PhysicsManager.POSITION_ITERATIONS); // 更新
	    	accumulator -= TIME_STEP;
	    }
	    physicsManager.disposeBody();
	    
	    super.update(deltaTime); // 更新精灵实体
	}
	
	/**
	 * 跟随刚体更新位置
	 */
	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		
		TransformComponent transformComponent = MapperTools.transformCM.get(entity);
		
		// 获得刚体位置，更新精灵位置
		PhysicsComponent physicsComponent = MapperTools.physicsCM.get(entity);
		if(physicsComponent != null){
			
			Vector2 position = physicsComponent.rigidBody.getPosition();
			transformComponent.position.x = PhysicsManager.meterToPixel(position.x);
			transformComponent.position.y = PhysicsManager.meterToPixel(position.y);
		}
	}
	
	/**
	 * 进入碰撞，最先调用
	 * 只给碰撞检测(CollisionComponent)刚体的实体转发碰撞事件
	 * 
	 * @see com.badlogic.gdx.physics.box2d.ContactListener#beginContact(com.badlogic.gdx.physics.box2d.Contact)
	 */
	@Override
	public void beginContact(Contact contact) {
		
		Entity entityA = (Entity)contact.getFixtureA().getBody().getUserData();
		Entity entityB = (Entity)contact.getFixtureB().getBody().getUserData();
		
		if(entityA != null){
			// 转发事件
			ScriptComponent scriptComponent = MapperTools.scriptCM.get(entityA);
			if(scriptComponent != null){
				scriptComponent.script.beginContact(contact, entityB); // 普通碰撞检测事件
			}
		}
		if(entityB != null){
			// 转发事件
			ScriptComponent scriptComponent = MapperTools.scriptCM.get(entityB);
			if(scriptComponent != null){
				scriptComponent.script.beginContact(contact, entityA); // 普通碰撞检测事件
			}
		}
	}
	
	/**
	 * 计算碰撞之前调用。未产生效果和力, 通常用来改变效果和力
	 * 只给碰撞检测(CollisionComponent)刚体的实体转发碰撞事件
	 * 
	 * @see com.badlogic.gdx.physics.box2d.ContactListener#preSolve(com.badlogic.gdx.physics.box2d.Contact, com.badlogic.gdx.physics.box2d.Manifold)
	 */
	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		
		Entity entityA = (Entity)contact.getFixtureA().getBody().getUserData();
		Entity entityB = (Entity)contact.getFixtureB().getBody().getUserData();
		
		if(entityA != null){
			// 转发事件
			ScriptComponent scriptComponent = MapperTools.scriptCM.get(entityA);
			if(scriptComponent != null){
				scriptComponent.script.preSolve(contact, oldManifold, entityB); // 普通碰撞检测事件
			}
		}
		if(entityB != null){
			// 转发事件
			ScriptComponent scriptComponent = MapperTools.scriptCM.get(entityB);
			if(scriptComponent != null){
				scriptComponent.script.preSolve(contact, oldManifold, entityA); // 普通碰撞检测事件
			}
		}
	}

	/**
	 * 计算碰撞之后调用。已产生效果和力，通常用来使用效果和力
	 * @see com.badlogic.gdx.physics.box2d.ContactListener#postSolve(com.badlogic.gdx.physics.box2d.Contact, com.badlogic.gdx.physics.box2d.ContactImpulse)
	 */
	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		
		Entity entityA = (Entity)contact.getFixtureA().getBody().getUserData();
		Entity entityB = (Entity)contact.getFixtureB().getBody().getUserData();
		
		if(entityA != null){
			// 转发事件
			ScriptComponent scriptComponent = MapperTools.scriptCM.get(entityA);
			if(scriptComponent != null){
				scriptComponent.script.postSolve(contact, impulse, entityB); // 普通碰撞检测事件
			}
		}
		if(entityB != null){
			// 转发事件
			ScriptComponent scriptComponent = MapperTools.scriptCM.get(entityB);
			if(scriptComponent != null){
				scriptComponent.script.postSolve(contact, impulse, entityA); // 普通碰撞检测事件
			}
		}
	}

	/**
	 * 离开碰撞
	 * 只给碰撞检测(CollisionComponent)刚体的实体转发碰撞事件
	 * 
	 * @see com.badlogic.gdx.physics.box2d.ContactListener#endContact(com.badlogic.gdx.physics.box2d.Contact)
	 */
	@Override
	public void endContact(Contact contact) {
		
		Entity entityA = (Entity)contact.getFixtureA().getBody().getUserData();
		Entity entityB = (Entity)contact.getFixtureB().getBody().getUserData();
		
		if(entityA != null){
			// 转发事件
			ScriptComponent scriptComponent = MapperTools.scriptCM.get(entityA);
			if(scriptComponent != null){
				scriptComponent.script.endContact(contact, entityB); // 普通碰撞检测事件
			}
		}
		if(entityB != null){
			// 转发事件
			ScriptComponent scriptComponent = MapperTools.scriptCM.get(entityB);
			if(scriptComponent != null){
				scriptComponent.script.endContact(contact, entityA); // 普通碰撞检测事件
			}
		}
	}
}
