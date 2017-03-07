package com.pending.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.pending.game.assets.MainScreenAssets;
import com.pending.game.components.PhysicsComponent;
import com.pending.game.components.ScriptComponent;
import com.pending.game.components.SpriterPlayerComponent;
import com.pending.game.components.TextureComponent;
import com.pending.game.components.TransformComponent;
import com.pending.game.entityscript.BoardScript;
import com.pending.game.entityscript.HeroScript;
import com.pending.game.manager.AshleyManager;
import com.pending.game.manager.PhysicsManager;
import com.pending.game.support.GlobalInline;

/**
 * 生产指定实体
 * 
 * TODO 实体纹理和实体CharactersTemplate的预加载和释放
 * 
 * @author D
 * @date 2016年9月16日 上午8:00:39
 */
public class EntityDao {
	
	public static final EntityDao instance = new EntityDao();
	
	/**
	 * 创建角色实体
	 * 
	 * @param template
	 * @return
	 */
	public Entity createEntity(float positionX, float positionY, float width, float height){
		
		AshleyManager ashleyManager = GlobalInline.instance.getAshleyManager();
		Entity entity = ashleyManager.engine.createEntity();
		
		TransformComponent transformComponent = ashleyManager.engine.createComponent(TransformComponent.class);
		transformComponent.position.set(positionX, positionY);
		transformComponent.width = width;
		transformComponent.height = height;
		transformComponent.offsetY = 30;
		entity.add(transformComponent);
		
//		TextureComponent textureComponent = ashleyManager.engine.createComponent(TextureComponent.class);
//		entity.add(textureComponent);
		
		SpriterPlayerComponent spriterPlayerComponent = ashleyManager.engine.createComponent(SpriterPlayerComponent.class);
		spriterPlayerComponent.player = Assets.instance.getSpriterPlayer(MainScreenAssets.spriterData, 0);
		spriterPlayerComponent.player.setScale(0.1f);
		entity.add(spriterPlayerComponent);
		
		PhysicsComponent physicsComponent = ashleyManager.engine.createComponent(PhysicsComponent.class);
//		CircleShape circle = new CircleShape(); // 圆形
//		circle.setRadius(10);
//		physicsComponent.shape = circle;
		PolygonShape polygon = new PolygonShape();
		polygon.setAsBox(PhysicsManager.pixelToMeter(width/2), PhysicsManager.pixelToMeter(height/2));
		physicsComponent.shape = polygon;
		physicsComponent.bodyType = BodyType.DynamicBody;
		entity.add(physicsComponent);
		
		ScriptComponent scriptComponent = ashleyManager.engine.createComponent(ScriptComponent.class);
		scriptComponent.script = new HeroScript();
		scriptComponent.script.entity = entity;
		entity.add(scriptComponent);
		
		GlobalInline.instance.getAshleyManager().initComponent(entity);
		
		Assets.instance.finishLoading();
		
		return entity;
	}

	/**
	 * 创建角色实体
	 * 
	 * @param
	 * @return
	 */
	public Entity createEntity2(float positionX, float positionY, float width, float height){
		
		AshleyManager ashleyManager = GlobalInline.instance.getAshleyManager();
		Entity entity = ashleyManager.engine.createEntity();
		
		TransformComponent transformComponent = ashleyManager.engine.createComponent(TransformComponent.class);
		transformComponent.position.set(positionX, positionY);
		transformComponent.width = width;
		transformComponent.height = height;
		entity.add(transformComponent);
		
		TextureComponent textureComponent = ashleyManager.engine.createComponent(TextureComponent.class);
		entity.add(textureComponent);
		
		PhysicsComponent physicsComponent = ashleyManager.engine.createComponent(PhysicsComponent.class);
		PolygonShape polygon = new PolygonShape();
		polygon.setAsBox(PhysicsManager.pixelToMeter(width/2), PhysicsManager.pixelToMeter(height/2));
		physicsComponent.shape = polygon;
//		physicsComponent.bodyType = BodyType.StaticBody;
		physicsComponent.bodyType = BodyType.DynamicBody;
		entity.add(physicsComponent);
		
		ScriptComponent scriptComponent = ashleyManager.engine.createComponent(ScriptComponent.class);
		scriptComponent.script = new BoardScript();
		scriptComponent.script.entity = entity;
		entity.add(scriptComponent);
		
		GlobalInline.instance.getAshleyManager().initComponent(entity);
		
		Assets.instance.finishLoading();
		
		return entity;
	}
}
