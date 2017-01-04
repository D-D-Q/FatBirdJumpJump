package com.pending.game.support;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.pending.game.Assets;
import com.pending.game.manager.InputManager;

/**
 * 屏幕切换, 可以使切换效果的中间屏
 * 		TODO 可以继承Game类，在game切换Screen的时候执行切换效果。这个版本不做了先
 * 
 * @author D
 * @date 2017年1月4日
 */
public class TransitionScreen extends ScreenAdapter {
	
	private Game game;
	private Class<? extends Screen> screenClass;
	private Class<?> screenAssets;
	private Screen screen;
	
	/**
	 * 当前屏幕的截图,用于效果操作
	 */
	private TextureRegion frameBufferTexture;
	
	/**
	 * 效果对象
	 */
	public TransitionEffect transitionEffect;
	
	/**
	 * 切换屏幕
	 * 
	 * @param game
	 * @param screenClass 目标屏幕
	 */
	public TransitionScreen(Game game, Screen targetScreen, TransitionEffect transitionEffect) {
		
		this(game, null, null, transitionEffect);
		this.screen = targetScreen;
	}
	
	/**
	 * 切换屏幕
	 * 
	 * @param game
	 * @param screenClass 目标屏幕
	 * @param screenAssets 目标屏幕资源类
	 */
	public TransitionScreen(Game game, Class<? extends Screen> targetScreen, Class<?> screenAssets, TransitionEffect transitionEffect) {
		
		this.game = game;
		this.screenClass = targetScreen;
		this.screenAssets = screenAssets;
		
		this.transitionEffect = transitionEffect;
		this.frameBufferTexture = ScreenUtils.getFrameBufferTexture();
		
		InputManager.instance.setDisabled(true);
	}
	
	@Override
	public void show() {
		
		if(screen == null){
			
			// 开始加载资源
			if(screenAssets != null){
				Assets.instance.loadAssets(screenAssets);
				Assets.instance.finishLoading();
			}
			
			// 实例化屏幕
			if(screenClass != null){
				ScreenProxy.instance.disabledProxy(game.getScreen().getClass()); // TODO有问题 销毁原Screen代理
				screen = ScreenProxy.instance.createScreen(screenClass); // 创建Screen代理
				screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			}
		}
	}
	
	@Override
	public void render(float delta) {
		
		if(transitionEffect == null){
			game.setScreen(screen);
			InputManager.instance.setDisabled(false);
		}
		else if(transitionEffect.render(delta, frameBufferTexture, screen)){
			game.setScreen(screen);
			InputManager.instance.setDisabled(false);
		}
	}
	
	@Override
	public void hide() {
		Gdx.app.log(this.toString(), "dispose begin");
		game = null;
		screenClass = null;
		screen = null;
		
		frameBufferTexture = null;
		transitionEffect = null;
	}
}
