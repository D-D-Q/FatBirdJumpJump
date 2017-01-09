package com.pending.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.pending.game.Assets;
import com.pending.game.GameConfig;
import com.pending.game.GameVar;
import com.pending.game.manager.InputManager;
import com.pending.game.support.GameUtil;
import com.pending.game.ui.MainScreenUI;

public class MainScreen extends ScreenAdapter {

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
	}
	
	public void initUI() {
		
		screenUI = GameUtil.createDisplaySizeGroup(UIstage, GameVar.UIViewport);
		
		screenUI.addActor(new MainScreenUI(this, Assets.instance.get(GameConfig.skin), Assets.instance.get(GameConfig.i18NBundle)));
		
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
}
