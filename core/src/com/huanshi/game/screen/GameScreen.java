package com.huanshi.game.screen;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.huanshi.game.Assets;
import com.huanshi.game.GameConfig;
import com.huanshi.game.GameVar;
import com.huanshi.game.GlobalInlineVar;
import com.huanshi.game.assets.MainScreenAssets;
import com.huanshi.game.components.PhysicsComponent;
import com.huanshi.game.manager.AshleyManager;
import com.huanshi.game.manager.InputManager;
import com.huanshi.game.manager.MsgManager;
import com.huanshi.game.manager.PhysicsManager;
import com.huanshi.game.support.GameUtil;
import com.huanshi.game.support.GlobalInline;
import com.huanshi.game.systems.GeneralSystem;
import com.huanshi.game.systems.Monstersystem;
import com.huanshi.game.systems.Monstersystem.Board;
import com.huanshi.game.systems.PhysicsSystem;
import com.huanshi.game.systems.RenderingSystem;
import com.huanshi.game.tools.MapperTools;
import com.huanshi.game.ui.GameOverUI;
import com.huanshi.game.ui.GamePauseUI;
import com.huanshi.game.ui.GameScreenUI1;


/**
 * 游戏主屏幕
 * 
 * @author D
 * @date 2016年8月29日 下午9:40:56
 */
public class GameScreen extends ScreenAdapter implements InputProcessor{
	
	/**
	 * UI根节点
	 */
	private Stage UIstage;
	
	/**
	 * 适配屏幕后的UI跟节点
	 */
	private Group screenUI;
	
	/**
	 * 手势事件
	 */
	private GestureDetector gestureDetector;
	
	public GameScreen() {
		Gdx.app.log(this.toString(), "create begin");

		// TODO 可以添加语言切换功能
		
		// 游戏视口，分辨率匹配
		GameVar.gameViewport = new ScalingViewport(Scaling.fillX, GameConfig.width, GameConfig.height); // 默认扩大显示
//		GameVar.gameViewport.getCamera().position.set(GameVar.position.x, GameVar.position.y, 0);
		
		// ECS系统
		AshleyManager ashleyManager = new AshleyManager();
		GlobalInline.instance.putAshleyManager(ashleyManager);
		
		ashleyManager.engine.addSystem(new PhysicsSystem(10));
		ashleyManager.engine.addSystem(new Monstersystem(20));
		ashleyManager.engine.addSystem(new RenderingSystem(30));
		ashleyManager.engine.addSystem(new GeneralSystem(40));
				
		// UI
		UIstage = new Stage(GameVar.UIViewport, GameVar.batch); // 创建UI根节点，注意它会重置相机的位置到(设计分辨率宽/2, 设计分辨率高/2)
		initUI();
		InputManager.instance.addProcessor(UIstage); // UI事件
		
		gestureDetector = new GestureDetector(new GestureAdapter() {
			
			@Override
			public boolean tap(float x, float y, int count, int button) {
				if(count >= 2){
					pause(); // 双击暂停
					return true;
				}
				return false;
			}
		});
		InputManager.instance.addProcessor(gestureDetector); // 手势事件
		
		InputManager.instance.addProcessor(this); // 事件
				
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
		
		Gdx.gl.glClearColor(166/255f, 228/255f, 227/255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
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
//		monstersystem.start(Settings.instance.level);
		monstersystem.start(0);
		
		// 继续游戏次数重置
		GlobalInline.instance.put(GlobalInlineVar.continueNum, 0);
		
		// 背景
		Entity bg = ashleyManager.entityDao.createBg(MainScreenAssets.bg1, 1);
		ashleyManager.engine.addEntity(bg);
		bg = ashleyManager.entityDao.createBg(MainScreenAssets.bg2, 2);
		ashleyManager.engine.addEntity(bg);
		bg = ashleyManager.entityDao.createBg(MainScreenAssets.bg3, 3);
		ashleyManager.engine.addEntity(bg);
		
		// 云朵
		Entity cloud = ashleyManager.entityDao.createCloud(MainScreenAssets.cloud1, MainScreenAssets.cloud2);
		ashleyManager.engine.addEntity(cloud);
		cloud = ashleyManager.entityDao.createCloud(MainScreenAssets.cloud3, MainScreenAssets.cloud4);
		ashleyManager.engine.addEntity(cloud);
		
		// 英雄
		float x = GameConfig.width/2;
		float y = monstersystem.getScore() * Monstersystem.scoreScale ;
		Entity hero = ashleyManager.entityDao.createEntity(GameConfig.width/2, y + Board.height/2+60/2, 40, 60);
		MapperTools.physicsCM.get(hero).rigidBody.setGravityScale(0);
		MapperTools.physicsCM.get(hero).rigidBody.setBullet(true);
		ashleyManager.engine.addEntity(hero);
		GlobalInline.instance.put("hero", hero);
		
		// 第一个跳台
		Board board = new Board();
		board.x = x;
		board.y = y;
		board.width = Monstersystem.boardWidthRange[0];
		board.speed = 0;
		Entity entity = ashleyManager.entityDao.createBoard(board);
		MapperTools.physicsCM.get(entity).rigidBody.setGravityScale(0);
		ashleyManager.engine.addEntity(entity);
		GlobalInline.instance.put(GlobalInlineVar.jumpBoardPosition, new Vector2(x, y));
		
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
	
	/**
	 * 继续
	 */
	public void continueStart() {
		
		Vector2 jumpBoardPosition = GlobalInline.instance.get(GlobalInlineVar.jumpBoardPosition);
		
		// 继续游戏次数加1
		int continueNum = GlobalInline.instance.get(GlobalInlineVar.continueNum);
		GlobalInline.instance.put(GlobalInlineVar.continueNum, ++continueNum);
		
		// 跳台
		AshleyManager ashleyManager = GlobalInline.instance.getAshleyManager();
		Board board = new Board();
		board.x = jumpBoardPosition.x;
		board.y = jumpBoardPosition.y;
		board.width = Monstersystem.boardWidthRange[0];
		board.speed = 0;
		Entity entity = ashleyManager.entityDao.createBoard(board);
		MapperTools.physicsCM.get(entity).rigidBody.setGravityScale(0);
		ashleyManager.engine.addEntity(entity);
		
		// 英雄
		Entity hero = GlobalInline.instance.get("hero");
		PhysicsComponent physicsComponent = MapperTools.physicsCM.get(hero);
		float y = PhysicsManager.pixelToMeter(jumpBoardPosition.y + Board.height/2 + 60/2);
		physicsComponent.rigidBody.setTransform(PhysicsManager.pixelToMeter(jumpBoardPosition.x), y, 0); 
		
		screenUI.clear();
		screenUI.addActor(new GameScreenUI1(Assets.instance.get(GameConfig.skin), Assets.instance.get(GameConfig.i18NBundle)));
		screenUI.addActor(new GamePauseUI(Assets.instance.get(GameConfig.skin), Assets.instance.get(GameConfig.i18NBundle)));
		
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
		
//		if(Assets.instance.update()){
//			
//			
//		}
		// loadding
		
		Assets.instance.finishLoading();
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
		InputManager.instance.removeProcessor(gestureDetector);
		UIstage.dispose();
	}

	/**
	 * android返回键
	 */
	@Override
	public boolean keyDown(int keycode) {
		
		if(Input.Keys.BACK == keycode){
			
			pause();
			return true;
		}
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
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
		return false;
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
}
