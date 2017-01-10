package com.pending.game.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;
import com.pending.game.GameConfig;
import com.pending.game.assets.MainScreenAssets;
import com.pending.game.screen.GameScreen;
import com.pending.game.screen.MainScreen;
import com.pending.game.support.GlobalInline;
import com.pending.game.support.LoadingScreen;

/**
 * 游戏结束弹出
 * 	广告
 * 	高度分数
 * 	评分、分享、购买、返回
 * 	重新开始、继续游戏
 * 
 * @author D
 * @date 2017年1月5日
 */
public class GameOverUI extends Table implements Telegraph {
	
	public final static int MSG_OVER = 30001;
	
	private GameScreen gameScreen;
	
	public GameOverUI(GameScreen gameScreen, Skin skin, I18NBundle i18NBundle) {
		
		this.gameScreen = gameScreen;
		
		this.setDebug(GameConfig.UIdebug);
		this.setName("GameOverUI");
		this.setFillParent(true);
		
		// 重新开始按钮
		Button restartButton = new Button(skin, "restart");
		restartButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				gameScreen.restart();
			}
		});
		this.add(restartButton).colspan(1).expand().center();
		
		this.row();
		
		Button backButton = new Button(skin, "restart");
		backButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
				// 开始游戏
				Game game = GlobalInline.instance.getGame();
				game.setScreen(new LoadingScreen(game, MainScreen.class, MainScreenAssets.class, false));
			}
		});
		this.add(backButton).colspan(1).expand().center();
	}
	

	@Override
	public boolean handleMessage(Telegram msg) {
		
		switch (msg.message) {
		
		case MSG_OVER:{
			gameScreen.over();
		}
		break;

		default:
			break;
		}
		
		return false;
	}
}
