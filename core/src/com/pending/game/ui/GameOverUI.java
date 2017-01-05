package com.pending.game.ui;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.I18NBundle;
import com.pending.game.GameConfig;

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
	
	public GameOverUI(Skin skin, I18NBundle i18NBundle) {
		
		this.setDebug(GameConfig.UIdebug);
		this.setName("GameOverUI");
	}
	

	@Override
	public boolean handleMessage(Telegram msg) {
		return false;
	}
}
