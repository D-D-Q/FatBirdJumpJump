package com.pending.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * 游戏逻辑需求的变量
 * 
 * @author D
 * @date 2016年9月11日 下午2:08:56
 */
public class GAME {
	
	/**
	 * 全局的openGL绘制对象
	 */
	public static SpriteBatch batch;
	
	/**
	 * 游戏窗口相机
	 */
	public static Viewport gameViewport;
	
	/**
	 * UI相机组件 
	 */
	public static Viewport UIViewport;
	
	/**
	 * 国际化
	 */
	public static I18NBundle i18NBundle;
	
	/**
	 * 当前UI皮肤
	 */
	public static Skin skin;

	public final static Vector2 position = new Vector2(0, 0);
}
