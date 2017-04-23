package com.huanshi.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * 玩家可修改设置
 * 
 * @author D
 * @date 2016年12月26日
 */
public class Settings {

	public final static Settings instance = new Settings();
	
	/**
	 * 音效开关
	 */
	public boolean sound;
	
	/**
	 * 左右灵敏度 [1, 3]
	 */
	public float sensitivity;
	
	/**
	 * 最高分
	 */
	public long score;
	
	/**
	 * 最高关
	 */
	public int level;
	
	public final static float min_sensitivity = 0.5f;
	public final static float max_sensitivity = 2;
	
	public void load(){
		
		Preferences preferences = Gdx.app.getPreferences(GameConfig.settingsFileName);
		
		// TODO 最好加解密+验证范围
		sound = preferences.getBoolean("sound", true);
		sensitivity = preferences.getFloat("sensitivity", 1.3f);
		score = preferences.getLong("score", 0);
		level = preferences.getInteger("level", 0);
	}
	
	public void updateScore(){
		
		Preferences preferences = Gdx.app.getPreferences(GameConfig.settingsFileName);
		preferences.putLong("score", score);
		preferences.putInteger("level", level);
		preferences.flush();
	}

	public void updateSetting(){
		
		Preferences preferences = Gdx.app.getPreferences(GameConfig.settingsFileName);
		preferences.putBoolean("sound", sound);
		preferences.putFloat("sensitivity", sensitivity);
		preferences.flush();
	}
}
