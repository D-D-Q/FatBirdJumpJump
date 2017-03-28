package com.pending.game.entityscript;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.pending.game.EntityScript;
import com.pending.game.GameConfig;
import com.pending.game.GameVar;
import com.pending.game.components.TextureComponent;
import com.pending.game.components.TransformComponent;
import com.pending.game.tools.MapperTools;

/**
 * 云朵背景
 * 
 * @author D
 * @date 2017年3月28日
 */
public class CloudScript extends EntityScript{
	
	/**
	 * 云朵纹理
	 */
	public TextureRegion[] clouds;
	
	/**
	 * 速度
	 */
	private float v = 40;
	
	/**
	 * 高度
	 */
	private float y;
	
	/**
	 * 间隔时间
	 */
	private float p = -1;
	private float time;
	
	@Override
	public void update(float deltaTime) {
		
		TransformComponent transformComponent = MapperTools.transformCM.get(entity);
		transformComponent.scale.set(.5f, .5f);
		
		TextureComponent textureComponent = MapperTools.textureCM.get(entity);
		if(textureComponent.textureRegion == null || transformComponent.position.x > GameConfig.width){
			
			if(p == 0){
				p = MathUtils.random(GameConfig.width/4/v, GameConfig.width/2/v); // 间隔时间
				time = 0;
			}
			else if(time < p){
				time += deltaTime;
			}
			else{
				textureComponent.textureRegion = clouds[MathUtils.random(clouds.length-1)];
				
				transformComponent.width = textureComponent.textureRegion.getRegionWidth();
				transformComponent.height = textureComponent.textureRegion.getRegionHeight();
				
				y = MathUtils.random(GameConfig.height/4, GameConfig.height/2 - transformComponent.height);
				
				transformComponent.position.x = -transformComponent.width;
				transformComponent.position.y = GameVar.gameViewport.getCamera().position.y + y;
				
				p = 0;
			}
		}
		else{
			transformComponent.position.x += deltaTime * v;
			transformComponent.position.y = GameVar.gameViewport.getCamera().position.y + y;
		}
	}
}
