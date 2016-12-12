package com.pending.game.screen;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.pending.game.Assets;
import com.pending.game.GAME;
import com.pending.game.GameConfig;
import com.pending.game.manager.AshleyManager;
import com.pending.game.manager.InputManager;
import com.pending.game.support.GlobalInline;
import com.pending.game.systems.PhysicsSystem;
import com.pending.game.systems.RenderingSystem;

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
	
	public GameScreen() {
		Gdx.app.log(this.toString(), "create begin");

		// TODO 可以添加语言切换功能
		
		// 游戏视口，分辨率匹配
		GAME.gameViewport = new FillViewport(GameConfig.width, GameConfig.hieght); // 默认扩大显示
		
		// 资源
//		GAME.i18NBundle = Assets.instance.get(GameScreenAssets.i18NBundle , I18NBundle.class); // 获得国际化
//		GAME.skin = Assets.instance.get(GameScreenAssets.default_skin, Skin.class); // 获得皮肤
		UIstage = new Stage(GAME.UIViewport, GAME.batch); // 创建UI根节点，注意它会重置相机的位置到(设计分辨率宽/2, 设计分辨率高/2)
		
		// ECS系统
		AshleyManager ashleyManager = new AshleyManager();
		GlobalInline.instance.putAshleyManager(ashleyManager);
		
		ashleyManager.engine.addSystem(new PhysicsSystem(10));
		ashleyManager.engine.addSystem(new RenderingSystem(50));
		
		// 英雄
		Entity hero = ashleyManager.entityDao.createEntity(GAME.position.x, GAME.position.y); // 创建英雄
		ashleyManager.engine.addEntity(hero);
		
		Entity entity = ashleyManager.entityDao.createEntity2(GAME.position.x, GAME.position.y - 25);
		ashleyManager.engine.addEntity(entity);
		
		entity = ashleyManager.entityDao.createEntity2(GAME.position.x, GAME.position.y + 40);
		ashleyManager.engine.addEntity(entity);
		
		// UI
		initUI();
		
		InputManager.instance.addProcessor(UIstage); // UI事件
		
//		int height = Gdx.graphics.getHeight(); // 设备高
//		float offset = height * (0.618f - 0.5f); // 0.618是黄金分割点
		
		// 初始化相机位置, 该位置会在屏幕中心
		GAME.gameViewport.getCamera().position.set(GAME.position.x, GAME.position.y, 0);  // 相机锚点是中心, 如果相机位置是0,0 那么虚拟世界坐标原点(0,0)拍摄的画面就是屏幕中间了
	}
	
	/**
	 * 创建UI
	 */
	
	private void initUI(){
		
	}
	
	@Override
	public void render(float delta) {
		
		// 游戏速度
		delta *= GameConfig.gameSpeed;
		
		GAME.gameViewport.apply();
		
		// ECS系统
		GlobalInline.instance.getAshleyManager().engine.update(delta);
		
		GAME.UIViewport.apply();
		UIstage.act(delta);
		UIstage.draw(); // 它自己会把相机信息设置给SpriteBatch
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		
		GAME.gameViewport.update(width, height, false); // 设置屏幕宽高。必须！
	}
	
	@Override
	public void pause() {
		super.pause();
	}
	
	/**
	 * 游戏切出去过，资源可能被回收。需要重新判断加载
	 */
	@Override
	public void resume() {
		
		if(Assets.instance.update()){
			// TODO 恢复完成
		}
		
		// loadding
	}

	/**
	 * dispose的时候调用, Screen的dispose根本不会被调用
	 */
	@Override
	public void hide() {
		Gdx.app.log(this.toString(), "dispose begin");
		UIstage.dispose();
		
		GlobalInline.instance.disabled();
	}
}
