package com.pending.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.pending.game.Assets;
import com.pending.game.GAME;
import com.pending.game.GameConfig;
import com.pending.game.assets.GameScreenAssets;
import com.pending.game.assets.LogoScreenAssets;
import com.pending.game.support.SwitchScreen;

/**
 * 启动闪屏Logo
 * 
 * @author D
 * @date 2017年1月3日
 */
public class LogoScreen extends ScreenAdapter {
	
	private Game game;
	
	private Texture texture;
	
	private float delta;
	
	public LogoScreen(Game game) {
		Gdx.app.log(this.toString(), "create begin");
		
		this.game = game;
		
		try {
			Assets.instance.loadAssets(LogoScreenAssets.class);
		} catch (Exception e) {
			e.printStackTrace();
			Gdx.app.error(this.toString(), "资源加载失败:" + e.getMessage());
			Gdx.app.exit();
		}
		Assets.instance.update();
		Assets.instance.finishLoading();

		texture = Assets.instance.get(LogoScreenAssets.logo, Texture.class);

	}
	@Override
	public void render(float delta) {
		
		GAME.UIViewport.apply();
		GAME.batch.setProjectionMatrix(GAME.UIViewport.getCamera().combined);
		
		GAME.batch.begin();
		GAME.batch.draw(texture, 0, 0);
		GAME.batch.end();
		
		this.delta += delta;
		if(this.delta >= GameConfig.logoShowTime){
			game.setScreen(new SwitchScreen(game, GameScreen.class, GameScreenAssets.class, false));
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
		
//		GlobalInline.instance.disabled();
	}
}
