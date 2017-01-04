package com.pending.game.support;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pending.game.GAME;
import com.pending.game.GameConfig;

/**
 * 渐隐效果
 * 
 * @author D
 * @date 2017年1月4日
 */
public class FadeOutTransitionEffect implements TransitionEffect {
	
	private float duration, time;
	private float begin, end;
	
	private boolean complete;
	
	public FadeOutTransitionEffect() {
		
		duration = 0.8f;
		time = 0;
		begin = 1;
		end = 0;
		
		complete = false;
	}

	@Override
	public boolean render(float delta, TextureRegion frameBufferTexture, Screen nextScreen) {
		
		time += delta;
		
		if(time>=duration)
			complete = true;
		
		nextScreen.render(delta);
		
		GAME.UIViewport.apply();
		GAME.batch.setProjectionMatrix(GAME.UIViewport.getCamera().combined);
		
		GAME.batch.begin();
		GAME.batch.setColor(1, 1, 1, begin + (end-begin) * (complete?1:time/duration));
		GAME.batch.draw(frameBufferTexture, 0, 0, GameConfig.width, GameConfig.height);
		GAME.batch.end();
		
		return complete;
	}
}
