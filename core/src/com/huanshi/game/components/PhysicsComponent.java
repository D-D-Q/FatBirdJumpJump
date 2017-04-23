package com.huanshi.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.huanshi.game.manager.AshleyManager;
import com.huanshi.game.manager.PhysicsManager;
import com.huanshi.game.support.GlobalInline;
import com.huanshi.game.systems.PhysicsSystem;
import com.huanshi.game.tools.MapperTools;

/**
 * 物理组件。
 * 
 * @author D
 * @date 2016年10月13日 下午10:28:35
 */
public class PhysicsComponent  implements Component, Poolable {
	
	/**
	 * 刚体
	 */
	public Body rigidBody;
	
	/**
	 * 范围半径
	 */
//	public float radius;
	
	/**
	 * 类型
	 */
	public BodyType bodyType;
	
	/**
	 * 形状
	 */
	public Shape shape;
	
	/**
	 * 是否是传感器
	 */
	public boolean isSensor;
	
	/** 
	 * 对象池回收组件调用
	 * @see com.badlogic.gdx.utils.Pool.Poolable#reset()
	 */
	@Override
	public void reset() {
		
//		AshleyManager ashleyManager = GlobalInline.instance.getAshleyManager();
//		PhysicsSystem physicsSystem = ashleyManager.engine.getSystem(PhysicsSystem.class);
//		
//		// 销毁碰撞检测
//		if(rigidBody != null){
//			physicsSystem.physicsManager.addDisposeBody(rigidBody);
//		}
//		
		rigidBody = null;
		bodyType = null;
		shape = null;
		isSensor = false;
	}
}	
