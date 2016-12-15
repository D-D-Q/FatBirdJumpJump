package com.pending.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.pending.game.assets.GameScreenAssets;
import com.pending.game.manager.InputManager;
import com.pending.game.screen.GameScreen;
import com.pending.game.support.GlobalInline;
import com.pending.game.support.SwitchScreen;

public class GameMain extends Game {
	
	/**
	 * 在控制台输出fps
	 */
	FPSLogger fpsLog;
	
	/**
	 * 因为EMS系统的各系统顺序执行无法中断，所以要切换的Screen标示在此
	 */
	public Screen switchScreen = null;
	
	@Override
	public void create () {
		
		Gdx.app.setLogLevel(Application.LOG_INFO); // 日志级别
		
		if(GameConfig.fpsDebug)
			fpsLog = new FPSLogger();
		
		Gdx.app.log(this.toString(), "create begin");
		
		GAME.batch = new SpriteBatch();
		
		Gdx.input.setInputProcessor(InputManager.instance.inputMultiplexer); // 监听输入事件
		
		GAME.UIViewport = new FillViewport(GameConfig.width, GameConfig.hieght);
		
		GlobalInline.instance.putGlobal("game", this);
		
		setScreen(new SwitchScreen(this, GameScreen.class, GameScreenAssets.class));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		super.render();
		
		// 切换屏幕
		if(switchScreen != null){
			setScreen(switchScreen);
			switchScreen = null;
		}
		
		if(GameConfig.fpsDebug)
			fpsLog.log();
	}
	
	/**
	 * 窗口大小改变
	 * @see com.badlogic.gdx.Game#resize(int, int)
	 */
	@Override
	public void resize(int width, int height) {
		Gdx.app.log(this.toString(), "resize " + width + "," + height);
		super.resize(width, height);
		
		GAME.UIViewport.update(width, height, true); // true表示设置相机的位置在:(设计分辨率宽/2, 设计分辨率高/2)。这样起始显示屏幕左下角就是虚拟世界坐标原点0,0了
	}
	
	@Override
	public void dispose () {
		
		Gdx.app.log(this.toString(), "dispose begin");
		super.dispose();
		
		GlobalInline.instance.disabledALL();
		
		GAME.batch.dispose();
		Assets.instance.dispose();
	}
}
