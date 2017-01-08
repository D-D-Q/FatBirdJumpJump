package com.pending.game.manager;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.InputProcessor;
import com.pending.game.EntityDao;
import com.pending.game.components.PhysicsComponent;
import com.pending.game.components.ScriptComponent;
import com.pending.game.systems.PhysicsSystem;
import com.pending.game.tools.MapperTools;

/**
 * 实体引擎
 * 
 * @author D
 * @date 2016年10月16日 下午8:22:07
 */
public class AshleyManager{
	
//	public static AshleyManager instance = new AshleyManager();
	
	/**
	 * 标识有效的Entity
	 */
	public final static int VALID_ENTITY = 1;
	/**
	 * 标识无效的Entity
	 */
	public final static int INVALID_ENTITY = 0;
	
	/**
	 * ashley组件实体系统引擎
	 */
	public PooledEngine engine;
	
	/**
	 * 实体生产
	 */
	// TODO 不用多个 一个就行
	public EntityDao entityDao;
	
	private boolean isCopy = false;
	
	public AshleyManager() {
		engine = new PooledEngine(); // TODO PooledEngine池大小
		engine.addEntityListener(new AshleyManagerEntityListener());
		entityDao = new EntityDao();
	}
	
	/**
	 * 添加removeForCopy方法移出的Entity
	 * @param entity
	 */
	public void addCopy(Entity entity){
		isCopy = true;
		engine.addEntity(entity);
		
		isCopy = false;
	}
	
	/**
	 * 移出ECS, 但是不销毁。只有new的Entity有效
	 * @param entity
	 */
	public void removeForCopy(Entity entity){
		isCopy = true;
		engine.removeEntity(entity);
		
		isCopy = false;
	}
	
	/**
	 * 初始化组件相关
	 * 
	 * @param entity
	 */
	public void initComponent(Entity entity){
		
		entity.flags = VALID_ENTITY; // 设置成有效
		
		PhysicsSystem physicsSystem = engine.getSystem(PhysicsSystem.class);
		
		// 添加碰撞检测
		if(MapperTools.physicsCM.get(entity) != null){
			physicsSystem.physicsManager.addPhysicsRigidBody(entity);
		}
		
		// 脚本组件
		ScriptComponent scriptComponent = MapperTools.scriptCM.get(entity);
		if(scriptComponent != null){
			if(scriptComponent.script instanceof InputProcessor)
				InputManager.instance.addProcessor((InputProcessor)scriptComponent.script); // 输入事件
		}
	}
	
	/**
	 * 销毁
	 */
	public void disabled(){
		
		// 必须回收Entity，在销毁System
		engine.removeAllEntities();
		engine.clearPools();
		
		// 销毁物理引擎
		PhysicsSystem physicsSystem = engine.getSystem(PhysicsSystem.class);
		if(physicsSystem != null)
			physicsSystem.physicsManager.dispose();
	}
	
	/**
	 * 组件监听
	 * 
	 * @author D
	 * @date 2016年11月14日
	 */
	private class AshleyManagerEntityListener implements EntityListener{
		
		@Override
		public void entityAdded(Entity entity) {
			
			if(isCopy)
				return;
		}

		@Override
		public void entityRemoved(Entity entity) {
			
			if(isCopy)
				return;
			
			entity.flags = INVALID_ENTITY; // 设置成无效
			
			PhysicsSystem physicsSystem = engine.getSystem(PhysicsSystem.class);
			
			// 销毁碰撞检测
			PhysicsComponent physicsComponent = MapperTools.physicsCM.get(entity);
			if(physicsComponent != null){
				physicsSystem.physicsManager.addDisposeBody(physicsComponent.rigidBody);
			}
			
			// 脚本组件，移出输入监听
			ScriptComponent scriptComponent = MapperTools.scriptCM.get(entity);
			if(scriptComponent != null){
				scriptComponent.script.disabled();
				if(scriptComponent.script instanceof InputProcessor)
					InputManager.instance.removeProcessor((InputProcessor)scriptComponent.script);
			}
			
		}
	}
	
}
