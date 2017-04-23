package com.huanshi.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.huanshi.game.Assets;
import com.huanshi.game.GameConfig;
import com.huanshi.game.GameVar;
import com.huanshi.game.assets.LogoScreenAssets;
import com.huanshi.game.support.FadeOutTransitionEffect;
import com.huanshi.game.support.ScreenProxy;
import com.huanshi.game.support.TransitionScreen;

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
	private Class<? extends Screen> screenClass;
	private Class<?> screenAssetsClass;
	
	private Texture texture;
	
	private float time;
	
	private boolean complete;
	
	public LogoScreen(Game game, Class<? extends Screen> screenClass, Class<?> screenAssetsClass) {
		Gdx.app.log(this.toString(), "create begin");
		
		this.game = game;
		this.screenClass = screenClass;
		this.screenAssetsClass = screenAssetsClass;
		this.complete = false;
	}
	
	@Override
	public void render(float delta) {
		
		GameVar.UIViewport.apply();
		GameVar.batch.setProjectionMatrix(GameVar.UIViewport.getCamera().combined);
		
		GameVar.batch.begin();
		GameVar.batch.draw(texture, 0, 0);
		GameVar.batch.end();
		
		this.time += delta;
		if(this.time >= logoShowTime && Assets.instance.update() && !complete){
			
			complete = true;
			
			ScreenProxy.instance.disabledProxy(game.getScreen().getClass()); // TODO有问题 销毁原Screen代理
			Screen screen = ScreenProxy.instance.createScreen(screenClass); // 创建Screen代理
			screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			
			game.setScreen(new TransitionScreen(game, screen, new FadeOutTransitionEffect()));
		}
	}
	
	@Override
	public void show() {
		
		Assets.instance.loadAssets(LogoScreenAssets.class);
		Assets.instance.finishLoading();

		texture = Assets.instance.get(LogoScreenAssets.logo, Texture.class);
		
		// 皮肤资源
		Assets.instance.load(GameConfig.i18NBundle, I18NBundle.class);
		Assets.instance.load(GameConfig.skin, Skin.class);
		
		// 闪屏之后的screen
		Assets.instance.loadAssets(screenAssetsClass);
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
