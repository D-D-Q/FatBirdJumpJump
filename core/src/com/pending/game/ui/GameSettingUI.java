package com.pending.game.ui;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;
import com.pending.game.GameConfig;
import com.pending.game.GameVar;
import com.pending.game.manager.InputManager;
import com.pending.game.screen.MainScreen;
import com.pending.game.support.GameUtil;

/**
 * 游戏设置
 * 
 * @author D
 * @date 2017年1月10日
 */
public class GameSettingUI extends Table {

	public GameSettingUI(MainScreen mainScreen, Skin skin, I18NBundle i18NBundle) {
		
		this.setDebug(GameConfig.UIdebug);
		this.setName("GameSettingUI");
		this.setFillParent(true);
		
		// 返回首页
		Button backButton = new Button(skin, "start");
		backButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
				InputManager.instance.setDisabled(true);
				
				Group screenUI = mainScreen.getScreenUI();
				screenUI.addAction(
						Actions.sequence(Actions.moveBy(-GameUtil.getDisplayWidth(GameVar.UIViewport), 0, 0.3f),
								Actions.run(new Runnable() {

									@Override
									public void run() {
										InputManager.instance.setDisabled(false);
									}
								})));
			}
		});
		
		this.add(backButton).colspan(1).expand().center();
	}
}
