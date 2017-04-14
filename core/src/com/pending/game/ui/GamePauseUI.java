package com.pending.game.ui;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;
import com.pending.game.GameConfig;
import com.pending.game.assets.MainScreenAssets;
import com.pending.game.manager.MsgManager;
import com.pending.game.screen.MainScreen;
import com.pending.game.support.GlobalInline;
import com.pending.game.support.LoadingScreen;
import com.pending.game.systems.RenderingSystem;

/**
 * 恢复游戏延时
 * 暂停时有广告
 * 
 * @author D
 * @date 2017年1月6日
 */
public class GamePauseUI extends Table implements Telegraph  {
	
	public final static int MSG_PAUSE = 20001;
	
	/**
	 * 最大恢复延迟
	 */
	private final float resumeDelay = 3;
	
	/**
	 * 恢复延迟 计时
	 */
	private float delay;
	
	private Button pauseButton;
	private Button resumeButton;
	private Button backButton;

	private Skin skin;
	
	public GamePauseUI(Skin skin, I18NBundle i18NBundle) {
		
		this.skin = skin;
		
		this.setDebug(GameConfig.UIdebug);
		this.setName("GameResume");
		this.setFillParent(true);
		this.pad(10);
		
		MsgManager.instance.addListener(this, MSG_PAUSE);
		
		// 暂停按钮
		pauseButton = new Button(skin, "pause");
		pauseButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				pause();
			}
		});
		this.add(pauseButton).colspan(1).expand().bottom().right();;
		
		// 恢复按钮
		resumeButton = new Button(skin, "resume");
		resumeButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				resume();
			}
		});
		
		// 返回按钮
		backButton = new Button(skin, "main");
		backButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
				Game game = GlobalInline.instance.getGame();
				game.setScreen(new LoadingScreen(game, MainScreen.class, MainScreenAssets.class, false));
			}
		});
	}
	
	/**
	 * 暂停游戏
	 */
	private void pause(){
		
		for (EntitySystem system : GlobalInline.instance.getAshleyManager().engine.getSystems()) {
			if (system instanceof RenderingSystem)
				continue;
			system.setProcessing(false);
		}
		
		this.clear();
		this.add(resumeButton).colspan(1).expand().center();
		this.row();
		this.add(backButton).colspan(1).bottom().right();
	}
	
	/**
	 * 恢复游戏
	 */
	private void resume(){
		
		delay = resumeDelay;
		
		// 恢复延迟
		Label label = new Label(String.format("%.0f", delay), skin, "delay");
		label.addAction(Actions.repeat(3, Actions.delay(1, Actions.run(new Runnable() {

			@Override
			public void run() {
				if (delay == 1) {
					
					// 恢复游戏
					for (EntitySystem system : GlobalInline.instance.getAshleyManager().engine.getSystems()) {
						if (system instanceof RenderingSystem)
							continue;
						system.setProcessing(true);
					}
					
					GamePauseUI.this.clear();
					GamePauseUI.this.add(pauseButton).colspan(1).expand().bottom().right();;
					
				} else {
					label.setText(String.format("%.0f", --delay));
				}
			}
		}))));
		
		this.clear();
		this.row().pad(300, 0, 0, 0);
		this.add(label).expand().top();
	}

	@Override
	public boolean handleMessage(Telegram msg) {
		
		switch (msg.message) {
		
		case MSG_PAUSE:{
			pause();
		}
		break;

		default:
			break;
		}
		
		return false;
	}
}
