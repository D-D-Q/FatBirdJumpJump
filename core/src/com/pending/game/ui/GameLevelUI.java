package com.pending.game.ui;

import com.badlogic.gdx.Game;
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
import com.pending.game.assets.GameScreenAssets;
import com.pending.game.manager.InputManager;
import com.pending.game.screen.GameScreen;
import com.pending.game.screen.MainScreen;
import com.pending.game.support.GameUtil;
import com.pending.game.support.GlobalInline;
import com.pending.game.support.LoadingScreen;

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
				
				// 开始游戏
				Game game = GlobalInline.instance.getGame();
				game.setScreen(new LoadingScreen(game, GameScreen.class, GameScreenAssets.class, false));
			}
		});
		this.add(restartButton).colspan(1).expand().center();
		
		this.row();
		// 开始按钮
		Button backButton = new Button(skin, "start");
		backButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
				InputManager.instance.setDisabled(true);
				
				Group screenUI = mainScreen.getScreenUI();
				screenUI.addAction(
						Actions.sequence(Actions.moveBy(GameUtil.getDisplayWidth(GameVar.UIViewport), 0, 0.3f),
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
