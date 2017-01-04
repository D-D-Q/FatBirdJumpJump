package com.pending.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.pending.game.Assets;
import com.pending.game.GAME;
import com.pending.game.assets.GameScreenAssets;
import com.pending.game.assets.LogoScreenAssets;
import com.pending.game.support.FadeOutTransitionEffect;
import com.pending.game.support.ScreenProxy;
import com.pending.game.support.TransitionScreen;

/**
 * 启动闪屏Logo
 * 
 * @author D
 * @date 2017年1月3日
 */
public class LogoScreen extends ScreenAdapter {
	
	/**
	 * 启动闪屏最短持续时间
	 */
	public final static float logoShowTime = 2;
	
	private Game game;
	
	private Texture texture;
	
	private float time;
	
	private boolean complete;
	
	public LogoScreen(Game game) {
		Gdx.app.log(this.toString(), "create begin");
		
		this.game = game;
		this.complete = false;
		
		Assets.instance.loadAssets(LogoScreenAssets.class);
		Assets.instance.finishLoading();

		texture = Assets.instance.get(LogoScreenAssets.logo, Texture.class);
		
		// 闪屏之后的screen
		Assets.instance.loadAssets(GameScreenAssets.class);
	}
	
	@Override
	public void render(float delta) {
		
		GAME.UIViewport.apply();
		GAME.batch.setProjectionMatrix(GAME.UIViewport.getCamera().combined);
		
		GAME.batch.begin();
		GAME.batch.draw(texture, 0, 0);
		GAME.batch.end();
		
		this.time += delta;
		if(this.time >= logoShowTime && Assets.instance.update() && !complete){
			
			complete = true;
			
			ScreenProxy.instance.disabledProxy(game.getScreen().getClass()); // TODO有问题 销毁原Screen代理
			Screen screen = ScreenProxy.instance.createScreen(GameScreen.class); // 创建Screen代理
			screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			
			game.setScreen(new TransitionScreen(game, screen, new FadeOutTransitionEffect()));
		}
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}
	
	@Override
	public void show() {
		
	}
	
	@Override
	public void pause() {
		
	}
	
	/**
	 * 游戏切出去过，资源可能被回收。需要重新判断加载
	 */
	@Override
	public void resume() {
		
		if(Assets.instance.update()){
			
		}
		
		// loadding
	}

	/**
	 * dispose的时候调用, Screen的dispose根本不会被调用
	 */
	@Override
	public void hide() {
		Gdx.app.log(this.toString(), "dispose begin");
		game = null;
		texture = null;
//		GlobalInline.instance.disabled();
	}
}
