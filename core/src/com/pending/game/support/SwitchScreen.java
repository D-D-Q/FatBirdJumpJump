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
public class SwitchScreen extends ScreenAdapter {
	
	private Game game;
	private Class<? extends Screen> screen;
	private Screen instance;
	
	private Stage UIstage;
	private ProgressBar progressBar;
	
//	private Action action; // 切换动画
	
	public SwitchScreen(Game game, Class<? extends Screen> screen, Class<?> screenAssets) {
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
	public SwitchScreen(Game game, Class<? extends Screen> screen, Class<?> screenAssets, boolean isShowProgress) {
		
		this.game = game;
		this.screen = screen;
		
		 // 开始加载资源
		try {
			Assets.instance.loadAssets(screenAssets);
			Assets.instance.update();
		} catch (Exception e) {
			e.printStackTrace();
			Gdx.app.error(this.toString(), "资源加载失败:" + e.getMessage());
			Gdx.app.exit();
		}
		
		if(isShowProgress){
			UIstage = new Stage(GAME.UIViewport, GAME.batch);
//			Skin skin = Assets.instance.get(GameScreenAssets.default_skin, Skin.class); // 获得皮肤
			initUI();
		}
		else{
			Assets.instance.finishLoading();
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
	}
	
	@Override
	public void render(float delta) {
		
		// 判断加载完成 && 未创建屏幕 &&  显示100%
		if(Assets.instance.update() && instance == null && (progressBar == null || progressBar.getVisualValue() == 1)){ 
			
			try {
				ScreenProxy.instance.disabledProxy(game.getScreen().getClass()); // TODO有问题 销毁原Screen代理
				instance = ScreenProxy.instance.createScreen(screen); // 创建Screen代理
			} 
			catch (Exception e) {
				Gdx.app.error(this.toString(), "screen切换失败");
				e.printStackTrace();
				Gdx.app.exit();
			}
			
			// 切换移出动画
//			UIstage.getRoot().getColor().a = 1;
//		    SequenceAction sequenceAction = new SequenceAction();
//		    sequenceAction.addAction(Actions.fadeOut(0.5f));
//		    sequenceAction.addAction(Actions.run(new Runnable() {
//		        @Override
//		        public void run() {
//		            game.setScreen(instance);
//		        }
//		    }));
//		    UIstage.getRoot().addAction(sequenceAction);
			
			game.setScreen(instance);
		}
		
		if(UIstage != null){
			
			progressBar.setValue(Assets.instance.getProgress());
			UIstage.getViewport().apply();
			UIstage.act(delta);
			UIstage.draw();
		}
	}
	
	@Override
	public void hide() {
		Gdx.app.log(this.toString(), "dispose begin");
		game = null;
		screen = null;
		instance = null;
		if(UIstage != null) UIstage.dispose();
	}
}
