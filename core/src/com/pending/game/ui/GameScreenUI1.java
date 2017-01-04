package com.pending.game.ui;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.pending.game.GameConfig;
import com.pending.game.entityscript.HeroScript;
import com.pending.game.manager.MsgManager;

/**
 * 
 * @author D
 * @date 2016年12月22日
 */
public class GameScreenUI1 extends Table implements Telegraph {
	
	public final static int MSG_ADD_SCORE = 10001;
	public final static int MSG_SET_POWER = 10002;
	
	private long score = 0;
	
	private Label scoreLabel;
	
	private ProgressBar powerProgress;

	public GameScreenUI1(Skin skin, I18NBundle i18NBundle) {
		
		this.setDebug(GameConfig.UIdebug);
		this.setName("AttributesUI");
//		this.setFillParent(true);
//		this.pad(heightOffset, widthOffset, heightOffset, widthOffset);
//		this.pad(-GAME.UIViewport.getScreenY(), -GAME.UIViewport.getScreenX(), -GAME.UIViewport.getScreenY(), -GAME.UIViewport.getScreenX());
		
		MsgManager.instance.addListener(this, MSG_ADD_SCORE);
		MsgManager.instance.addListener(this, MSG_SET_POWER);
		
		scoreLabel = new Label(String.valueOf(score) , skin);
		this.add(scoreLabel).expandY().top();
		
		
		// TODO 替换资源
		Pixmap pixmap1 = new Pixmap(1, 8, Format.RGB888);
		pixmap1.setColor(Color.WHITE);
		pixmap1.fill();
		
		Pixmap pixmap2 = new Pixmap(1, 8, Format.RGB888);
		pixmap2.setColor(Color.BLUE);
		pixmap2.fill();
		
		ProgressBarStyle progressBarStyle = new ProgressBarStyle();
//		progressBarStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap1)));
		progressBarStyle.knob = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap2)));
		progressBarStyle.knobBefore = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap2)));
		
		powerProgress = new ProgressBar(0, HeroScript.superJumpNum, 0.1f, false, progressBarStyle);
		powerProgress.setAnimateDuration(0.1f);
//		powerProgress.setSize(GameConfig.width * 0.8f, 16);
//		powerProgress.setPosition((GameConfig.width - powerProgress.getWidth())/2, 100);
		
		this.row();
		this.add(powerProgress).expandX().fill().bottom();
		
		this.getMaxWidth();
		this.getMaxHeight();
	}

	@Override
	public boolean handleMessage(Telegram msg) {
		
		switch (msg.message) {
		
		case MSG_ADD_SCORE:{
			score = (long)msg.extraInfo;
			scoreLabel.setText(String.valueOf(score));
		}
		break;

		case MSG_SET_POWER:{
			powerProgress.setValue((float)msg.extraInfo);
		}
		break;
		
		default:
			break;
		}
		
		return false;
	}
	
	@Override
	public Table center() {
		
		MsgManager.instance.removeListener(this, MSG_ADD_SCORE);
		MsgManager.instance.removeListener(this, MSG_SET_POWER);
		
		return super.center();
	}
}
