package com.pending.game.ui;

import com.badlogic.ashley.core.EntitySystem;
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
import com.pending.game.manager.MsgManager;
import com.pending.game.support.GlobalInline;
import com.pending.game.systems.RenderingSystem;

/**
 * 恢复游戏延时
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

	private Skin skin;
	
	public GamePauseUI(Skin skin, I18NBundle i18NBundle) {
		
		this.skin = skin;
		
		this.setDebug(GameConfig.UIdebug);
		this.setName("GameResume");
		this.setFillParent(true);
		
		MsgManager.instance.addListener(this, MSG_PAUSE);
		
		// 暂停按钮
		pauseButton = new Button(skin, "pause");
		pauseButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				pause();
			}
		});
		this.add(pauseButton).colspan(1).expand().bottom().left();
		
		// 恢复按钮
		resumeButton = new Button(skin, "resume");
		resumeButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				resume();
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
		this.add(resumeButton).colspan(1).center();
	}
	
	/**
	 * 恢复游戏
	 */
	private void resume(){
		
		delay = resumeDelay;
		
		// 恢复延迟
		Label label = new Label(String.format("%.0f", delay), skin);
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
					GamePauseUI.this.add(pauseButton).colspan(1).expand().bottom().left();
					
				} else {
					label.setText(String.format("%.0f", --delay));
				}
			}
		}))));
		
		this.clear();
		this.add(label).center();
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
