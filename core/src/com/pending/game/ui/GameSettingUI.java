package com.pending.game.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;
import com.pending.game.GameConfig;
import com.pending.game.assets.GameScreenAssets;
import com.pending.game.screen.GameScreen;
import com.pending.game.screen.MainScreen;
import com.pending.game.support.GlobalInline;
import com.pending.game.support.LoadingScreen;

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
	}
}
