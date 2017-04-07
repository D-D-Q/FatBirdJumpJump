package com.pending.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.pending.game.Assets;
import com.pending.game.GameConfig;
import com.pending.game.GameVar;
import com.pending.game.Settings;
import com.pending.game.manager.InputManager;
import com.pending.game.support.GameUtil;
import com.pending.game.ui.GameLevelUI;
import com.pending.game.ui.GameSettingUI;
import com.pending.game.ui.MainScreenUI;

public class MainScreen extends ScreenAdapter implements InputProcessor {

	/**
	 * UI根节点
	 */
	private Stage UIstage;
	
	/**
	 * 适配屏幕后的UI跟节点
	 */
	private Group screenUI;
	
	public MainScreen() {
		Gdx.app.log(this.toString(), "create begin");
		
		// UI
		UIstage = new Stage(GameVar.UIViewport, GameVar.batch); // 创建UI根节点，注意它会重置相机的位置到(设计分辨率宽/2, 设计分辨率高/2)
		initUI();
		InputManager.instance.addProcessor(UIstage); // UI事件
		
		InputManager.instance.addProcessor(this); // 事件
	}
	
	public void initUI() {
		
		screenUI = GameUtil.createDisplaySizeGroup(UIstage, GameVar.UIViewport);
		
		float displayWidth = GameUtil.getDisplayWidth(GameVar.UIViewport);
		
		GameSettingUI gameSettingUI = new GameSettingUI(this, Assets.instance.get(GameConfig.skin), Assets.instance.get(GameConfig.i18NBundle));
		gameSettingUI.setPosition(displayWidth * -1, 0);
		screenUI.addActor(gameSettingUI);
		
		MainScreenUI mainScreenUI = new MainScreenUI(this, Assets.instance.get(GameConfig.skin), Assets.instance.get(GameConfig.i18NBundle));
		mainScreenUI.setPosition(displayWidth * 0, 0);
		screenUI.addActor(mainScreenUI);
		
		GameLevelUI gameMarketUI = new GameLevelUI(this, Assets.instance.get(GameConfig.skin), Assets.instance.get(GameConfig.i18NBundle));
		gameMarketUI.setPosition(displayWidth * 1, 0);
		screenUI.addActor(gameMarketUI);
	}
	
	@Override
	public void render(float delta) {
		
		GameVar.UIViewport.apply();
		UIstage.act(delta);
		UIstage.draw(); // 它自己会把相机信息设置给SpriteBatch
	}
	
	@Override
	public void hide() {
		Gdx.app.log(this.toString(), "dispose begin");
		
		InputManager.instance.removeProcessor(UIstage);
		UIstage.dispose();
	}

	/**
	 * 显示指定UI
	 * 
	 * @param UIIndex UI序号0开始
	 */
	public void scrollUITo(int UIIndex) {
		
		float displayWidth = GameUtil.getDisplayWidth(GameVar.UIViewport);
		
		if(screenUI.getX()/displayWidth == -1f){ // 保存设置
			Settings.instance.updateSetting();
		}
		
		InputManager.instance.setDisabled(true);
		screenUI.addAction(
				Actions.sequence(Actions.moveTo(displayWidth * -UIIndex, 0, 0.3f),
						Actions.run(new Runnable() {

							@Override
							public void run() {
								InputManager.instance.setDisabled(false);
							}
						}))); 
	}

	/**
	 * android返回键
	 */
	@Override
	public boolean keyDown(int keycode) {
		
		if(Input.Keys.BACK == keycode){
			
			float displayWidth = GameUtil.getDisplayWidth(GameVar.UIViewport);
			int index = (int)(screenUI.getX()/displayWidth);
			if(index > 0)
				scrollUITo(--index);
			else if(index < 0)
				scrollUITo(++index);
			
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
