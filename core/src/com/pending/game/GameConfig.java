package com.pending.game;

import com.badlogic.gdx.Application;

/**
 * @author Administrator
 * @date 2017年1月3日
 */
public class GameConfig {

	/**
	 * 游戏设计分辨率
	 */
	public final static float width = 1080;
	public final static float height = 1920;
	
	/**
	 * 国际化配置
	 */
	public final static String i18NBundle = "i18n/GameScreenMessage";
	
	/**
	 * UI皮肤配置
	 */
	public final static String skin = "skin/defaultUI.json";
	
	/**
	 * 记录和设置保存文件名
	 */
	public final static String settingsFileName = "pending.data";
	
	/**
	 * 日志级别
	 */
	public final static int logLevel = Application.LOG_INFO;
	
	/**
	 * UI的debug模式
	 */
	public final static boolean UIdebug = false;
	
	/**
	 * 物理引擎的debug模式
	 */
	public final static boolean physicsdebug = true;
	
	/**
	 * fps
	 */
	public final static boolean fpsDebug = false;
}
