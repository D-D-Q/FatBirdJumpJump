package com.pending.game.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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
 * 开始页面UI
 * 
 * @author D
 */
public class MainScreenUI extends Table {

	public MainScreenUI(MainScreen mainScreen, Skin skin, I18NBundle i18NBundle) {
		
		this.setDebug(GameConfig.UIdebug);
		this.setName("MainScreenUI");
		this.setFillParent(true);
		this.pad(360, 20 , 170, 10);
		
		// 开始按钮
		Button startButton = new Button(skin, "start");
		startButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
				// 开始游戏
				Game game = GlobalInline.instance.getGame();
				game.setScreen(new LoadingScreen(game, GameScreen.class, GameScreenAssets.class, false));
			}
		});
		this.add(startButton).colspan(4).expand().center();
		
		this.row().bottom();
		
		// 设置
		Button settingButton = new Button(skin, "setting");
		settingButton.addListener(new ClickListener(){
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
				mainScreen.scrollUITo(-1);
			}
		});
		this.add(settingButton).colspan(1).center();
		
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
				
//				Group screenUI = mainScreen.getScreenUI();
//				screenUI.addAction(
//						Actions.sequence(Actions.moveBy(-GameUtil.getDisplayWidth(GameVar.UIViewport), 0, 0.3f),
//								Actions.run(new Runnable() {
//
//									@Override
//									public void run() {
//										InputManager.instance.setDisabled(false);
//									}
//								})));
				mainScreen.scrollUITo(1);
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
	}
}
