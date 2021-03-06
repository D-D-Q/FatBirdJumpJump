package com.huanshi.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * 变换组件
 * 保存精灵的位置、缩放、旋转信息
 * 
 * @author D
 * @date 2016年8月28日 下午2:31:59
 */
public class TransformComponent implements Component, Poolable  {

	/**
	 * 绘制位置信息
	 * x轴和y轴 屏幕左下为原点
	 * index_z抽、深度、绘制优先级、数越小越先绘制（先绘制会被覆盖）
	 */
	public final Vector2 position = new Vector2();
	public int index_z = 1;
	
	/**
	 * 画布大小，非精灵大小
	 */
	public float width;
	public float height;
	
	/**
	 * 精灵定位锚点。保证精灵定位锚点绘制到this.position位置绘制的偏移量
	 * 以画布大小为世界，画布左下角为原点0,0
	 * 绘制显示时，会把该点显示在position所指的位置上
	 */
	public float offsetX;
	public float offsetY;
	
	/**
	 * 精灵身高
	 */
	public float spriteHeight;
	
	/**
	 * 精灵身宽
	 */
	public float spriteWidth;
	
	/**
	 * 是否翻转
	 */
	public boolean flipX = false;
	
//-------------------------------------------------------------暂时没用start
	/**
	 * 引擎锚点位置，缩放、旋转使用
	 */
	public final Vector2 origin = new Vector2();
	
	/**
	 * 缩放
	 */
	public final Vector2 scale = new Vector2(1.0f, 1.0f);
	 
	/**
	 * 逆时针旋转角度
	 */
	public float rotation = 0.0f;
	 
	/**
	 * 是否隐藏
	 */
	public boolean isHidden = false;
	
//-------------------------------------------------------------暂时没用end
	
	/**
	 * 获得绘制位置X
	 * @return
	 */
	public float getRenderPositionX(){
		return this.position.x - this.offsetX;
//		return this.position.x - this.offsetX * (width/spriteWidth);
	}
	
	/**
	 * 获得绘制位置Y
	 * @return
	 */
	public float getRenderPositionY(){
		return this.position.y - this.offsetY;
//		return this.position.y - this.offsetY * (width/spriteWidth);
	}
	
	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	/**
	 * 复制
	 */
	public TransformComponent copyTo(TransformComponent temp){
		temp.position.set(this.position);
		temp.width = this.width;
		temp.height = this.height;
		temp.offsetX = this.offsetX;
		temp.offsetY = this.offsetY;
		temp.flipX = this.flipX;
		temp.origin.set(this.origin);
		temp.scale.set(this.scale);
		temp.rotation = this.rotation;
		temp.isHidden = this.isHidden;
		return temp;
	}
	
	/* 
	 * 对象池回收组件调用
	 * @see com.badlogic.gdx.utils.Pool.Poolable#reset()
	 */
	@Override
	public void reset() {
		position.setZero();
		width = 0;
		height = 0;
		offsetX = 0;
		offsetY = 0;
		spriteHeight = 0;
		flipX = false;
		
		origin.setZero();
		scale.set(1.0f, 1.0f);
		rotation = 0.0f;
		isHidden = false;
	}
}
