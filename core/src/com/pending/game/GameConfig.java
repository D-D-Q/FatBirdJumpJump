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
	 * 相机和角色的高度距离
	 */
	public static float cameraOffset = 0;
	
	/**
	 * 游戏速度
	 */
	public static float gameSpeed = 1;
	
	/**
	 * 启动闪屏最短持续时间
	 */
	public final static float logoShowTime = 3;
	
	
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
