package com.huanshi.game.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;
import com.huanshi.game.GameConfig;
import com.huanshi.game.GameVar;
import com.huanshi.game.assets.GameScreenAssets;
import com.huanshi.game.manager.InputManager;
import com.huanshi.game.screen.GameScreen;
import com.huanshi.game.screen.MainScreen;
import com.huanshi.game.support.GameUtil;
import com.huanshi.game.support.GlobalInline;
import com.huanshi.game.support.LoadingScreen;

/**
 * 关卡选择
 * 
 * @author D
 * @date 2017年1月10日
 */
public class GameLevelUI extends Table {

	public GameLevelUI(MainScreen mainScreen, Skin skin, I18NBundle i18NBundle) {
		
		this.setDebug(GameConfig.UIdebug);
		this.setName("GameLevelUI");
		this.setFillParent(true);
		
		// 开始按钮
		Button restartButton = new Button(skin, "start");
		restartButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
			}
		});
		this.add(restartButton).colspan(1).expand().center();
		
		this.row();
		// 开始按钮
		Button backButton = new Button(skin, "start");
		backButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
//				InputManager.instance.setDisabled(true);
//				
//				Group screenUI = mainScreen.getScreenUI();
//				screenUI.addAction(
//						Actions.sequence(Actions.moveBy(GameUtil.getDisplayWidth(GameVar.UIViewport), 0, 0.3f),
//								Actions.run(new Runnable() {
//
//									@Override
//									public void run() {
//										InputManager.instance.setDisabled(false);
//									}
//								})));
				mainScreen.scrollUITo(0);
			}
		});
		this.add(backButton).colspan(1).expand().center();
	}
}
