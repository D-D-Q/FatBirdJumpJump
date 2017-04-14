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
import com.pending.game.GameVar;
import com.pending.game.GlobalInlineVar;
import com.pending.game.assets.MainScreenAssets;
import com.pending.game.screen.GameScreen;
import com.pending.game.screen.MainScreen;
import com.pending.game.support.GameUtil;
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
		this.pad(10, 20, 170, 20);
		
		// 重新开始按钮
		Button restartButton = new Button(skin, "restart");
		restartButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
				gameScreen.restart();
			}
		});
					
		int continueNum = GlobalInline.instance.get(GlobalInlineVar.continueNum);
		if(continueNum == 0){ // 可以继续游戏
			
			Table startTable = new Table();
			startTable.setDebug(GameConfig.UIdebug);
			this.add(startTable).colspan(5).expand().fill().padTop(350).padLeft((GameUtil.getDisplayWidth(GameVar.UIViewport)-20-20)/3);
			
			startTable.add(restartButton).expand().center();
			
			// 继续游戏按钮
			Button continueButton = new Button(skin, "restart");
			continueButton.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					
					gameScreen.continueStart();
				}
			});
			startTable.add(continueButton).expand().center();
		}
		else{
			this.add(restartButton).colspan(5).expand().padTop(350).center();
		}
		
		this.row();
		
		// 返回
		Button backButton = new Button(skin, "main");
		backButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
				Game game = GlobalInline.instance.getGame();
				game.setScreen(new LoadingScreen(game, MainScreen.class, MainScreenAssets.class, false));
			}
		});
		this.add(backButton).colspan(1).center();
		
		// 排行
		Button rankButton = new Button(skin, "rank");
		rankButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
			}
		});
		this.add(rankButton).colspan(1).center();

		// 购买
		Button shopButton = new Button(skin, "shop");
		shopButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
			}
		});
		this.add(shopButton).colspan(1).center();
		
		// 喜欢
		Button likeButton = new Button(skin, "like");
		likeButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
			}
		});
		this.add(likeButton).colspan(1).center();
		
		// 分享
		Button settingButton = new Button(skin, "share");
		settingButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
			}
		});
		this.add(settingButton).colspan(1).center();
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
