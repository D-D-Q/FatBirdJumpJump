package com.pending.game.support;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * 屏幕切换效果接口
 * 
 * @author D
 * @date 2017年1月4日
 */
public interface TransitionEffect {

	public boolean render(float delta, TextureRegion frameBufferTexture, Screen nextScreen);
}
