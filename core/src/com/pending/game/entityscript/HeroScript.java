package com.pending.game.entityscript;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.pending.game.EntityScript;
import com.pending.game.GAME;
import com.pending.game.GameConfig;
import com.pending.game.Setting;
import com.pending.game.components.PhysicsComponent;
import com.pending.game.components.TransformComponent;
import com.pending.game.manager.MsgManager;
import com.pending.game.manager.PhysicsManager;
import com.pending.game.support.GlobalInline;
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
	 * 跳跃高度
	 */
	private float jumpHeight = 90;
	
	/**
	 * 跳跃上升时间 0.34
	 */
	private float jumpTime = 0.3f;
	
	/**
	 * 起跳速度 = 2 * height / time
	 */
	private float speed = 0;
	
	/**
	 * 手指位置
	 */
	private Vector3 touchPosition = new Vector3();
	
	/**
	 * 跟随左右滑动的偏移位置
	 */
	private float offetX = 0;;
	
	
	private final float superJumpTime = 2.5f;
	public final static int superJumpNum = 30;
	
	private int num = 0;
	private float time = superJumpTime;
	
	@Override
	public void preSolve(Contact contact, Manifold oldManifold, Entity target) {
		
		PhysicsComponent physicsComponent = MapperTools.physicsCM.get(entity);
		
		if(physicsComponent.rigidBody.getLinearVelocity().y > 0){ // 上升过程
			contact.setEnabled(false);
			return;
		}
		
		TransformComponent transformComponent = MapperTools.transformCM.get(entity);
		Vector2 position = physicsComponent.rigidBody.getPosition();
		
		PhysicsComponent targetPhysicsComponent = MapperTools.physicsCM.get(target);
		TransformComponent targetTransformComponent = MapperTools.transformCM.get(target);
		Vector2 targetPosition = targetPhysicsComponent.rigidBody.getPosition();
		
		if(position.y - transformComponent.height/2 < targetPosition.y + targetTransformComponent.height/2){ // 精灵底部 < 台阶顶部
			contact.setEnabled(false); // 禁用当前碰撞
		}
		else if(isStart){
			physicsComponent.rigidBody.setLinearVelocity(0, speed);
			GlobalInline.instance.put("jumPBoardY", targetPhysicsComponent.rigidBody.getPosition().y);
			++num;
			
			MsgManager.instance.dispatchMessage(GameScreenUI1.MSG_ADD_SCORE, (long)position.y/100); // 高度
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
		
		GAME.gameViewport.getCamera().unproject(touchPosition.set(screenX, screenY, 0));
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
		physicsComponent.rigidBody.setLinearVelocity(0, speed);
		
		isStart = true;
		
		if(speed > PhysicsManager.MAX_SPEED)
			Gdx.app.error("", "速度大于极限速度");
		
		return true;
	}

	private Vector3 touchDraggedVector = new Vector3();
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		
		GAME.gameViewport.getCamera().unproject(touchDraggedVector.set(screenX, screenY, 0));
		
		offetX = (touchDraggedVector.x - touchPosition.x) * Setting.sensitivity;
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
		Vector2 entityPosition = physicsComponent.rigidBody.getPosition();
		float newX = MathUtils.clamp(entityPosition.x + offetX,0 + transformComponent.width / 2, GameConfig.width - transformComponent.width / 2);
		physicsComponent.rigidBody.setTransform(newX, entityPosition.y, 0); 
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
		Camera camera = GAME.gameViewport.getCamera();
		float y = GameConfig.cameraOffset - (camera.position.y - entityPosition.y); 
		if(y > 0) // 英雄最高位置与摄像机的距离小于cameraOffset
			camera.position.y += y;
		
		if(!physicsComponent.rigidBody.getLinearVelocity().isZero())
			Gdx.app.debug("速度", physicsComponent.rigidBody.getLinearVelocity().toString());
		
		// 游戏结束
		if(entityPosition.y < camera.position.y - GameConfig.height/2 - transformComponent.height){
			
			physicsComponent.rigidBody.setGravityScale(0);
			continueHeight = camera.position.y;
			
			// 继续游戏
//			physicsComponent.rigidBody.setLinearVelocity(0, 60);
			isContinue = true;
			
			// 重新开始
//			GameMain game = GlobalInline.instance.getGlobal("game");
//			game.switchScreen = new SwitchScreen(game, GameScreen.class, GameScreenAssets.class);
		}
		
		if(isContinue){
			
			if(entityPosition.y >= continueHeight){
				
				physicsComponent.rigidBody.setGravityScale(1);
				isContinue = false;
			}
			else{
				physicsComponent.rigidBody.setLinearVelocity(0, PhysicsManager.MAX_SPEED);
			}
		}
		
	}
}
