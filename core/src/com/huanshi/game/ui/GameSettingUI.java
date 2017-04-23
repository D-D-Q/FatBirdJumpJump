package com.huanshi.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.I18NBundle;
import com.huanshi.game.GameConfig;
import com.huanshi.game.GameVar;
import com.huanshi.game.Settings;
import com.huanshi.game.manager.InputManager;
import com.huanshi.game.screen.MainScreen;
import com.huanshi.game.support.GameUtil;

/**
 * 游戏设置
 * 音效
 * 灵敏度
 * 登录google
 * fackbook
 * 恢复购买
 * 语言
 * 
 * 返回
 * 
 * @author D
 * @date 2017年1月10日
 */
public class GameSettingUI extends Table {

	public GameSettingUI(MainScreen mainScreen, Skin skin, I18NBundle i18NBundle) {
		
		this.setDebug(GameConfig.UIdebug);
		this.setName("GameSettingUI");
		this.setFillParent(true);
		this.pad(10, 20 , 170, 10);
		
		// 音效开关
		CheckBox sound = new CheckBox("", skin, "sound");
		sound.setChecked(true);
		sound.addListener(new ClickListener(){
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
				CheckBox sound = (CheckBox)event.getListenerActor();
				Settings.instance.sound = sound.isChecked();
				
				if(sound.isChecked()){
					// 有音效
					Gdx.app.log(this.toString(), "音效开");
				}
				else{
					// 无音效
					Gdx.app.log(this.toString(), "音效关");
				}
			}
		});
		this.add(sound).colspan(1).expand();
		
		this.row();
		
		Slider slider = new Slider(Settings.min_sensitivity, Settings.max_sensitivity, 0.1f, false, skin, "sensitivity");
		slider.setValue(Settings.instance.sensitivity);
		slider.setAnimateDuration(0.1f);
		slider.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				
				Slider slider = (Slider)event.getListenerActor();
				Settings.instance.sensitivity = slider.getValue();
			}
		});
		this.add(slider).colspan(1).expand().fillX();
		
		this.row().bottom();
		
		// 返回首页
		Button backButton = new Button(skin, "back");
		backButton.addListener(new ClickListener(){
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
				Settings.instance.updateSetting();
				mainScreen.scrollUITo(0);
			}
		});
		
		this.add(backButton).colspan(1).center();
	}
}
