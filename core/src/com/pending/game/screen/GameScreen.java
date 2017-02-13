package com.pending.game.screen;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.pending.game.Assets;
import com.pending.game.GameConfig;
import com.pending.game.GameVar;
import com.pending.game.Settings;
import com.pending.game.manager.AshleyManager;
import com.pending.game.manager.InputManager;
import com.pending.game.manager.MsgManager;
import com.pending.game.support.GameUtil;
import com.pending.game.support.GlobalInline;
import com.pending.game.systems.GeneralSystem;
import com.pending.game.systems.Monstersystem;
import com.pending.game.systems.PhysicsSystem;
import com.pending.game.systems.RenderingSystem;
import com.pending.game.systems.Monstersystem.Board;
import com.pending.game.tools.MapperTools;
import com.pending.game.ui.GameOverUI;
import com.pending.game.ui.GamePauseUI;
import com.pending.game.ui.GameScreenUI1;


/**
 * 游戏主屏幕
 * 
 * @author D
 * @date 2016年8月29日 下午9:40:56
 */
public class GameScreen extends ScreenAdapter {
	
	/**
	 * UI根节点
	 */
	private Stage UIstage;
	
	/**
	 * 适配屏幕后的UI跟节点
	 */
	private Group screenUI;
	
	public GameScreen() {
		Gdx.app.log(this.toString(), "create begin");

		// TODO 可以添加语言切换功能
		
		// 游戏视口，分辨率匹配
		GameVar.gameViewport = new ScalingViewport(Scaling.fillX, GameConfig.width, GameConfig.height); // 默认扩大显示
//		GameVar.gameViewport.getCamera().position.set(GameVar.position.x, GameVar.position.y, 0);
		
		// UI
		UIstage = new Stage(GameVar.UIViewport, GameVar.batch); // 创建UI根节点，注意它会重置相机的位置到(设计分辨率宽/2, 设计分辨率高/2)
		initUI();
		InputManager.instance.addProcessor(UIstage); // UI事件
				
		// ECS系统
		AshleyManager ashleyManager = new AshleyManager();
		GlobalInline.instance.putAshleyManager(ashleyManager);
		
		ashleyManager.engine.addSystem(new GeneralSystem(0));
		ashleyManager.engine.addSystem(new PhysicsSystem(10));
		ashleyManager.engine.addSystem(new Monstersystem(20));
		ashleyManager.engine.addSystem(new RenderingSystem(30));
		
		start();
	}
	
	/**
	 * 创建UI
	 */
	private void initUI(){
		
		screenUI = GameUtil.createDisplaySizeGroup(UIstage, GameVar.UIViewport);
		
		screenUI.addActor(new GameScreenUI1(Assets.instance.get(GameConfig.skin), Assets.instance.get(GameConfig.i18NBundle)));
		screenUI.addActor(new GamePauseUI(Assets.instance.get(GameConfig.skin), Assets.instance.get(GameConfig.i18NBundle)));
	}
	
	@Override
	public void render(float delta) {
		
		// 游戏速度
		delta *= GameVar.gameSpeed;
		
		GameVar.gameViewport.apply();
		
		// ECS系统
		GlobalInline.instance.getAshleyManager().engine.update(delta);
		
		GameVar.UIViewport.apply();
		UIstage.act(delta);
		UIstage.draw(); // 它自己会把相机信息设置给SpriteBatch
	}
	
	@Override
	public void resize(int width, int height) {
		
		GameVar.gameViewport.update(width, height, false); // 设置屏幕宽高。必须！
		
		Vector3 offset = GameVar.gameViewport.getCamera().unproject(new Vector3(0, Gdx.graphics.getHeight() * 0.618f, 0)); // 0.618是黄金分割点
		GameVar.cameraOffset = GameVar.gameViewport.getCamera().position.y - offset.y; // 相机和英雄要保持的距离
		
		Entity hero = GlobalInline.instance.get("hero");
		Vector2 position = MapperTools.transformCM.get(hero).position;
		
		GameVar.gameViewport.getCamera().position.set(position.x, position.y + GameVar.cameraOffset, 0);  // 如果相机位置是0,0 那么虚拟世界坐标原点(0,0)拍摄的画面就是屏幕中间
	}
	
	/**
	 * 初始化英雄
	 */
	public void start(){
		
		AshleyManager ashleyManager = GlobalInline.instance.getAshleyManager();
		
		Monstersystem monstersystem = ashleyManager.engine.getSystem(Monstersystem.class);
		monstersystem.start(Settings.instance.level);
		
		// 英雄
		float y = monstersystem.getScore() * Monstersystem.scoreScale + Board.height/2 + 60/2;
		Entity hero = ashleyManager.entityDao.createEntity(GameConfig.width/2, y, 40, 60);
		MapperTools.physicsCM.get(hero).rigidBody.setGravityScale(0);
		MapperTools.physicsCM.get(hero).rigidBody.setBullet(true);
		ashleyManager.engine.addEntity(hero);
		GlobalInline.instance.put("hero", hero);
		
		GameVar.gameViewport.getCamera().position.set(GameConfig.width/2, y + GameVar.cameraOffset, 0);
	}
	
	/**
	 * 游戏结束
	 */
	public void over(){
		
		for (EntitySystem system : GlobalInline.instance.getAshleyManager().engine.getSystems()) {
			if (system instanceof RenderingSystem)
				continue;
			system.setProcessing(false);
		}
		
		screenUI.clear();
		screenUI.addActor(new GameOverUI(this, Assets.instance.get(GameConfig.skin), Assets.instance.get(GameConfig.i18NBundle)));
	}
	
	/**
	 * 重新开始
	 */
	public void restart() {
		
		AshleyManager ashleyManager = GlobalInline.instance.getAshleyManager();
		ashleyManager.engine.removeAllEntities();
		
		screenUI.clear();
		screenUI.addActor(new GameScreenUI1(Assets.instance.get(GameConfig.skin), Assets.instance.get(GameConfig.i18NBundle)));
		screenUI.addActor(new GamePauseUI(Assets.instance.get(GameConfig.skin), Assets.instance.get(GameConfig.i18NBundle)));
		
		start();
		
		for (EntitySystem system : GlobalInline.instance.getAshleyManager().engine.getSystems()) {
			if (system instanceof RenderingSystem)
				continue;
			system.setProcessing(true);
		}
	}
	
	@Override
	public void pause() {
		
		MsgManager.instance.dispatchMessage(GamePauseUI.MSG_PAUSE);
	}
	
	/**
	 * 游戏切出去过，资源可能被回收。需要重新判断加载
	 */
	@Override
	public void resume() {
		
		if(Assets.instance.update()){
			
			
		}
		
		// loadding
	}

	public Stage getUIstage() {
		return UIstage;
	}

	public Group getScreenUI() {
		return screenUI;
	}

	/**
	 * dispose的时候调用, Screen的dispose根本不会被调用
	 */
	@Override
	public void hide() {
		Gdx.app.log(this.toString(), "dispose begin");
		
		InputManager.instance.removeProcessor(UIstage);
		UIstage.dispose();
	}
}
