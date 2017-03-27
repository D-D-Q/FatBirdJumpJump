package com.pending.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.pending.game.assets.MainScreenAssets;
import com.pending.game.components.PhysicsComponent;
import com.pending.game.components.ScriptComponent;
import com.pending.game.components.SpriterPlayerComponent;
import com.pending.game.components.TextureComponent;
import com.pending.game.components.TransformComponent;
import com.pending.game.entityscript.BgScript;
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
		transformComponent.index_z = 10000;
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
	 * 创建跳台实体
	 * 
	 * @param
	 * @return
	 */
	public Entity createBoard(float positionX, float positionY, float width, float height){
		
		AshleyManager ashleyManager = GlobalInline.instance.getAshleyManager();
		Entity entity = ashleyManager.engine.createEntity();
		
		TransformComponent transformComponent = ashleyManager.engine.createComponent(TransformComponent.class);
		transformComponent.position.set(positionX, positionY);
		transformComponent.width = width;
		transformComponent.height = height;
//		transformComponent.spriteWidth = 768;
//		transformComponent.spriteHeight = 256;
//		transformComponent.offsetX = 256/2f;
//		transformComponent.offsetY = 768/2f;
		transformComponent.offsetX = width/2f;
		transformComponent.offsetY = height/2f;
		transformComponent.origin.set(width/2f, height/2f);
		entity.add(transformComponent);
		
		TextureComponent textureComponent = ashleyManager.engine.createComponent(TextureComponent.class);
		textureComponent.textureRegion = new TextureRegion(Assets.instance.get(MainScreenAssets.p, Texture.class));
		entity.add(textureComponent);
		
		PhysicsComponent physicsComponent = ashleyManager.engine.createComponent(PhysicsComponent.class);
		PolygonShape polygon = new PolygonShape();
		polygon.setAsBox(PhysicsManager.pixelToMeter(width/2), PhysicsManager.pixelToMeter(height/2));
		physicsComponent.shape = polygon;
//		physicsComponent.bodyType = BodyType.StaticBody;
		physicsComponent.bodyType = BodyType.DynamicBody;
		entity.add(physicsComponent);
		
		Entity boardSensor = createBoardSensor(positionX, positionY + height, width, height);
		
		ScriptComponent scriptComponent = ashleyManager.engine.createComponent(ScriptComponent.class);
		BoardScript boardScript = new BoardScript();
		boardScript.entity = entity;
		boardScript.sensor = boardSensor;
		scriptComponent.script = boardScript;
		entity.add(scriptComponent);
		
		ashleyManager.engine.addEntity(boardSensor);
		
		GlobalInline.instance.getAshleyManager().initComponent(entity);
		
		Assets.instance.finishLoading();
		
		return entity;
	}
	

	/**
	 * 创建跳台感应器
	 * 
	 * @param
	 * @return
	 */
	public Entity createBoardSensor(float positionX, float positionY, float width, float height){
		
		height = height * 4;
		
		AshleyManager ashleyManager = GlobalInline.instance.getAshleyManager();
		Entity entity = ashleyManager.engine.createEntity();
		
		TransformComponent transformComponent = ashleyManager.engine.createComponent(TransformComponent.class);
		transformComponent.position.set(positionX, positionY);
		transformComponent.width = width;
		transformComponent.height = height;
		entity.add(transformComponent);
		
		PhysicsComponent physicsComponent = ashleyManager.engine.createComponent(PhysicsComponent.class);
		PolygonShape polygon = new PolygonShape();
		polygon.setAsBox(PhysicsManager.pixelToMeter(width/2), PhysicsManager.pixelToMeter(height/2));
		physicsComponent.shape = polygon;
		physicsComponent.bodyType = BodyType.StaticBody;
		physicsComponent.isSensor = true;
		entity.add(physicsComponent);
		
		GlobalInline.instance.getAshleyManager().initComponent(entity);
		
		entity.flags = 100; //TODO 100表示传感器
		
		Assets.instance.finishLoading();
		
		return entity;
	}
	
	/**
	 * 创建背景
	 * 
	 * @param assets 背景资源
	 * @param index 第几个背景, 从上往底数
	 * @return
	 */
	public Entity createBg(String assets, int index){
		
		AshleyManager ashleyManager = GlobalInline.instance.getAshleyManager();
		Entity entity = ashleyManager.engine.createEntity();
		
		TransformComponent transformComponent = ashleyManager.engine.createComponent(TransformComponent.class);
		transformComponent.position.set(0, 0);
		transformComponent.width = 1024;
		transformComponent.height = 968;
		transformComponent.index_z = -index;
		entity.add(transformComponent);
		
		TextureComponent textureComponent = ashleyManager.engine.createComponent(TextureComponent.class);
		Texture texture = Assets.instance.get(assets, Texture.class);
		texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
		textureComponent.textureRegion = new TextureRegion(texture, texture.getWidth() * 2, texture.getHeight());
		
		entity.add(textureComponent);
		
		ScriptComponent scriptComponent = ashleyManager.engine.createComponent(ScriptComponent.class);
		BgScript bgScript = new BgScript();
		bgScript.entity = entity;
		bgScript.index = index;
		scriptComponent.script = bgScript;
		entity.add(scriptComponent);
		
		GlobalInline.instance.getAshleyManager().initComponent(entity);
		
		Assets.instance.finishLoading();
		
		return entity;
	}
	
}
