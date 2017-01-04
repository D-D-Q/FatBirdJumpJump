package com.pending.game.support;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.pending.game.Assets;
import com.pending.game.GAME;
import com.pending.game.GameConfig;

/**
 * 屏幕切换用，中间屏
 * 
 * @author D
 * @date 2016年9月2日 上午6:02:20
 */
public class LoadingScreen extends ScreenAdapter {
	
	private Game game;
	private Class<? extends Screen> screenClass;
	private Screen screen;
	
	private Stage UIstage;
	private ProgressBar progressBar;
	
	public LoadingScreen(Game game, Class<? extends Screen> screen, Class<?> screenAssets) {
		this(game, screen, screenAssets, true);
	}
	
	/**
	 * 切换屏幕
	 * 
	 * @param game
	 * @param screen 目标屏幕
	 * @param screenAssets 目标屏幕资源类
	 * @param isShowProgress 是否显示加载进度条(很快时不必显示)
	 */
	public LoadingScreen(Game game, Class<? extends Screen> screen, Class<?> screenAssets, boolean isShowProgress) {
		
		this.game = game;
		this.screenClass = screen;
		
		 // 要加载的资源。调用Assets.instance.update()才是真正开始加载
		Assets.instance.loadAssets(screenAssets);
		
		if(isShowProgress){
			UIstage = new Stage(GAME.UIViewport, GAME.batch);
//			Skin skin = Assets.instance.get(GameScreenAssets.default_skin, Skin.class); // 获得皮肤
			initUI();
		}
	}
	
	/**
	 * 创建UI
	 */
	private void initUI(){
		
		Pixmap pixmap1 = new Pixmap(1, 16, Format.RGB888);
		pixmap1.setColor(Color.WHITE);
		pixmap1.fill();
		
		Pixmap pixmap2 = new Pixmap(1, 16, Format.RGB888);
		pixmap2.setColor(Color.BLUE);
		pixmap2.fill();
		
		ProgressBarStyle progressBarStyle = new ProgressBarStyle();
		progressBarStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap1)));
		progressBarStyle.knob = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap2)));
		progressBarStyle.knobBefore = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap2)));
		
		progressBar = new ProgressBar(0, 1, 0.01f, false, progressBarStyle);
		progressBar.setAnimateDuration(0.1f);
		progressBar.setSize(GameConfig.width * 0.8f, 16);
		progressBar.setPosition((GameConfig.width - progressBar.getWidth())/2, 100);
		
		UIstage.addActor(progressBar);
		
		pixmap1.dispose();
		pixmap2.dispose();
	}
	
	@Override
	public void render(float delta) {
		
		// 判断加载完成 && 未创建屏幕 &&  显示100%
		if(Assets.instance.update() && screen == null && (progressBar == null || progressBar.getVisualValue() == 1)){ 
			
			ScreenProxy.instance.disabledProxy(game.getScreen().getClass()); // TODO有问题 销毁原Screen代理
			screen = ScreenProxy.instance.createScreen(screenClass); // 创建Screen代理
			
			game.setScreen(screen);
		}
		
		if(UIstage != null){
			
			progressBar.setValue(Assets.instance.getProgress());
			UIstage.getViewport().apply();
			UIstage.act(delta);
			UIstage.draw();
		}
	}
	
	@Override
	public void show() {
		
		// 不显示载入页面，必须加载完资源
		if(UIstage == null){
			Assets.instance.finishLoading();
		}
	}
	
	@Override
	public void hide() {
		Gdx.app.log(this.toString(), "dispose begin");
		game = null;
		screenClass = null;
		screen = null;
		if(UIstage != null) UIstage.dispose();
	}
}
