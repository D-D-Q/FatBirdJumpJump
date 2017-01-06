package com.pending.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * 游戏逻辑需求的变量
 * 
 * @author D
 * @date 2016年9月11日 下午2:08:56
 */
public class GameVar {
	
	/**
	 * 全局的openGL绘制对象
	 */
	public static SpriteBatch batch;
	
	/**
	 * 游戏窗口相机
	 */
	public static Viewport gameViewport;
	
	/**
	 * UI相机组件 
	 */
	public static Viewport UIViewport;
	
	/**
	 * 游戏速度
	 */
	public static float gameSpeed = 1;
	
	/**
	 * 英雄位置
	 */
	public final static Vector2 position = new Vector2(GameConfig.width/2, GameConfig.height/2);
	
	/**
	 * 相机和角色的高度距离
	 */
	public static float cameraOffset = 0;
}
