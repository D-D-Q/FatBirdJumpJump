package com.pending.game.assets;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.pending.game.annotation.Asset;

/**
 * 加载游戏窗口资源
 * @author D
 * @date 2016年9月11日 下午9:14:13
 */
public class GameScreenAssets {
	
//	@Asset(BitmapFont.class)
//	public final static String font0 = "fonts/fzstk.fnt";
	
	@Asset(I18NBundle.class)
	public final static String i18NBundle = "i18n/GameScreenMessage";
	
	@Asset(Skin.class)
	public final static String default_skin = "skin/defaultUI.json";
}
