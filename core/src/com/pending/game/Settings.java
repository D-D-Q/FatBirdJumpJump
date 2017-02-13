package com.pending.game;

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
	
	public void load(){
		
		Preferences preferences = Gdx.app.getPreferences(GameConfig.settingsFileName);
		
		// TODO 最好加解密+验证范围
		sensitivity = preferences.getFloat("sensitivity", 1.3f);
		score = preferences.getLong("score", 0);
		level = preferences.getInteger("level", 0);
	}
	
	public void updateScore(long score){
		
		if(score <= this.score)
			return;
		
		this.score = score;
		
		Preferences preferences = Gdx.app.getPreferences(GameConfig.settingsFileName);
		preferences.putLong("score", score);
		preferences.flush();
	}
	
	public void updateLevel(int level){
		
		this.level = level;
		
		Preferences preferences = Gdx.app.getPreferences(GameConfig.settingsFileName);
		preferences.putInteger("level", level);
		preferences.flush();
	}
}
