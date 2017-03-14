package com.pending.game.entityscript;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.brashmonkey.spriter.Animation;
import com.pending.game.EntityScript;
import com.pending.game.GameConfig;
import com.pending.game.GameVar;
import com.pending.game.Settings;
import com.pending.game.components.PhysicsComponent;
import com.pending.game.components.SpriterPlayerComponent;
import com.pending.game.components.TransformComponent;
import com.pending.game.manager.MsgManager;
import com.pending.game.manager.PhysicsManager;
import com.pending.game.screen.GameScreen;
import com.pending.game.support.GlobalInline;
import com.pending.game.systems.Monstersystem;
import com.pending.game.tools.MapperTools;
import com.pending.game.ui.GameScreenUI1;

/**
 * 英雄脚本
 * 
 * @author D
 * @date 2016年10月13日 下午9:56:24
 */
public class HeroScript extends EntityScript implements InputProcessor{
	
	private boolean isStart = false;
	
	private boolean isContinue = false;
	private float continueHeight = 0;
	
	/**
	 * 跳跃高度(米)
	 */
	private final float jumpHeight = 100;
	
	/**
	 * 最长跳跃上升时间
	 */
	private final float maxJumpTime = 0.5f;
	
	/**
	 * 最短跳跃上升时间
	 */
	private final float minJumpTime = 0.34f;
	
	/**
	 * 当前跳跃上升时间
	 */
	private float jumpTime;
	
	/**
	 * 起跳速度 = 2 * height / time
	 */
	private float speed;
	
	/**
	 * 手指位置
	 */
	private Vector3 touchPosition = new Vector3();
	
	/**
	 * 跟随左右滑动的偏移位置
	 */
	private float offetX = 0;;
	
	// 超级跳，暂时不用了
	private final float superJumpTime = 2.5f;
	public final static int superJumpNum = 30;
	
	private int num = 0;
	private float time = superJumpTime;
	
	public HeroScript() {
		
		Monstersystem monstersystem = GlobalInline.instance.getAshleyManager().engine.getSystem(Monstersystem.class);
		jumpTime = maxJumpTime - (maxJumpTime-minJumpTime)/Monstersystem.maxLevel * monstersystem.getLevel();
	}
	
	@Override
	public void preSolve(Contact contact, Manifold oldManifold, Entity target) {
			
		PhysicsComponent physicsComponent = MapperTools.physicsCM.get(entity);
		
		if(physicsComponent.rigidBody.getLinearVelocity().y > 0){ // 上升过程
			contact.setEnabled(false);
			return;
		}
		
		TransformComponent transformComponent = MapperTools.transformCM.get(entity);
		Vector2 position = transformComponent.position;
		
		TransformComponent targetTransformComponent = MapperTools.transformCM.get(target);
		Vector2 targetPosition = targetTransformComponent.position;
		
		if(position.y - transformComponent.height/2 < targetPosition.y + targetTransformComponent.height/2){ // 精灵底部 < 台阶顶部
			contact.setEnabled(false); // 禁用当前碰撞
			return;
		}
//		else if(isStart){
//			physicsComponent.rigidBody.setLinearVelocity(0, speed);
//			GlobalInline.instance.put("jumPBoardY", targetPosition.y);
//			++num;
//			
//			// 更新分数
//			Monstersystem monstersystem = GlobalInline.instance.getAshleyManager().engine.getSystem(Monstersystem.class);
//			if(monstersystem.updateScore(position.y)){
//				
//				jumpTime = maxJumpTime - (maxJumpTime-minJumpTime)/Monstersystem.maxLevel * monstersystem.getLevel();
//				speed = 2 * jumpHeight / jumpTime;
//				physicsComponent.rigidBody.getWorld().setGravity(new Vector2(0, -speed/jumpTime));
//			}
//		}
	}
	
	@Override
	public void postSolve(Contact contact, ContactImpulse impulse, Entity target) {
		
		PhysicsComponent physicsComponent = MapperTools.physicsCM.get(entity);
		
		TransformComponent transformComponent = MapperTools.transformCM.get(entity);
		Vector2 position = transformComponent.position;
		
		TransformComponent targetTransformComponent = MapperTools.transformCM.get(target);
		Vector2 targetPosition = targetTransformComponent.position;
		
		physicsComponent.rigidBody.setLinearVelocity(0, speed);
		GlobalInline.instance.put("jumPBoardY", targetPosition.y);
		++num;
		
		// 更新分数
		Monstersystem monstersystem = GlobalInline.instance.getAshleyManager().engine.getSystem(Monstersystem.class);
		if(monstersystem.updateScore(position.y)){
			
			jumpTime = maxJumpTime - (maxJumpTime-minJumpTime)/Monstersystem.maxLevel * monstersystem.getLevel();
			speed = 2 * jumpHeight / jumpTime;
			physicsComponent.rigidBody.getWorld().setGravity(new Vector2(0, -speed/jumpTime));
		}
		
		SpriterPlayerComponent spriterPlayerComponent = MapperTools.SpriterPlayerCM.get(entity);
		spriterPlayerComponent.player.setAnimation("fly");
		Animation animation = spriterPlayerComponent.player.getAnimation();
		animation.prepare();
		
	}
	
	@Override
	public boolean beginContact(Contact contact, Entity target) {
		
		if( target.flags == 100){ // 台阶传感器
			
			PhysicsComponent physicsComponent = MapperTools.physicsCM.get(entity);
			
			if(physicsComponent.rigidBody.getLinearVelocity().y > 0){ // 上升过程
				return true;
			}
			
			TransformComponent transformComponent = MapperTools.transformCM.get(entity);
			Vector2 position = transformComponent.position;
			
			TransformComponent targetTransformComponent = MapperTools.transformCM.get(target);
			Vector2 targetPosition = targetTransformComponent.position;
			
			if(position.y - transformComponent.height/2 < targetPosition.y + targetTransformComponent.height/2 - targetTransformComponent.height){ // 精灵底部 < 台阶顶部(传感器顶部－传感器高度)
				return true;
			}
			
			SpriterPlayerComponent spriterPlayerComponent = MapperTools.SpriterPlayerCM.get(entity);
			spriterPlayerComponent.player.setAnimation(0);
			Animation animation = spriterPlayerComponent.player.getAnimation();
			animation.prepare();
			
		}
		
		return true;
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
		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		
		GameVar.gameViewport.getCamera().unproject(touchPosition.set(screenX, screenY, 0));
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		
		if(isStart)
			return false;
		
		PhysicsComponent physicsComponent = MapperTools.physicsCM.get(entity);
		
		/* *
		 * (v2-v1)/t = g
		 * 2h=t*t*g
		 * 2h = (v2-v1)*t
		 * */
		speed = 2 * jumpHeight / jumpTime;
		physicsComponent.rigidBody.getWorld().setGravity(new Vector2(0, -speed/jumpTime));
		physicsComponent.rigidBody.setGravityScale(1);
		physicsComponent.rigidBody.setLinearVelocity(0, speed);
		
		isStart = true;
		
		if(speed > PhysicsManager.MAX_SPEED)
			Gdx.app.error("", "速度大于极限速度");
		
		return true;
	}

	private Vector3 touchDraggedVector = new Vector3();
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		
		GameVar.gameViewport.getCamera().unproject(touchDraggedVector.set(screenX, screenY, 0));
		
		offetX = (touchDraggedVector.x - touchPosition.x) * Settings.instance.sensitivity;
		touchPosition.set(touchDraggedVector);
		
		return true;
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
		TransformComponent transformComponent = MapperTools.transformCM.get(entity);
		
		// 根据滑动更新x轴位置
		Vector2 entityPosition = transformComponent.position;
		float newX = MathUtils.clamp(entityPosition.x + offetX,0 + transformComponent.width / 2, GameConfig.width - transformComponent.width / 2);
		Vector2 rigidBodyPosition = physicsComponent.rigidBody.getPosition();
		physicsComponent.rigidBody.setTransform(PhysicsManager.pixelToMeter(newX), rigidBodyPosition.y, 0); 
		
		// 精灵朝向
		if(offetX > 0)
			transformComponent.flipX = true; // 朝右
		else if(offetX < 0)
			transformComponent.flipX = false; // 朝左(默认)  
		
		offetX = 0;
		
//		if(num > superJumpNum){
//			if(time >= deltaTime){
//				GameConfig.gameSpeed = 2f;
//				physicsComponent.rigidBody.setLinearVelocity(0, PhysicsManager.MAX_SPEED);
//				time -= deltaTime;
//				
//				MsgManager.instance.dispatchMessage(GameScreenUI1.MSG_SET_POWER, superJumpNum * time/superJumpTime); // 更新UI
//			}
//			else{
//				GameConfig.gameSpeed = 1f;
//				num = 0;
//				time = superJumpTime;
//			}
//		}
//		else{
//			MsgManager.instance.dispatchMessage(GameScreenUI1.MSG_SET_POWER, (float)num); // 更新UI
//		}
		
		// 更新摄像机y轴位置
		Camera camera = GameVar.gameViewport.getCamera();
		if(entityPosition.y + GameVar.cameraOffset > camera.position.y)
			camera.position.y = entityPosition.y + GameVar.cameraOffset;
		
		if(!physicsComponent.rigidBody.getLinearVelocity().isZero())
			Gdx.app.debug("速度", physicsComponent.rigidBody.getLinearVelocity().toString());
		
		// 游戏结束
		if(entityPosition.y < camera.position.y - GameConfig.height/2 - transformComponent.height){
			
			physicsComponent.rigidBody.setGravityScale(0);
			physicsComponent.rigidBody.setLinearVelocity(Vector2.Zero);
//			continueHeight = camera.position.y;
			
			// 继续游戏
//			isContinue = true;
			
			// 重新开始
//			GameMain game = GlobalInline.instance.getGlobal("game");
//			game.switchScreen = new SwitchScreen(game, GameScreen.class, GameScreenAssets.class);
			GameScreen gameScreen = GlobalInline.instance.getScreen();
			gameScreen.over();
		}
		
//		if(isContinue){
//			
//			if(entityPosition.y >= continueHeight){
//				
//				physicsComponent.rigidBody.setGravityScale(1);
//				isContinue = false;
//			}
//			else{
//				physicsComponent.rigidBody.setLinearVelocity(0, PhysicsManager.MAX_SPEED);
//			}
//		}
	}
	
	@Override
	public void disabled() {
		
		GlobalInline.instance.remove("jumPBoardY");
	}
	
}
