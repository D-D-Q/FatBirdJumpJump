package com.pending.game.systems;

import java.util.Comparator;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.brashmonkey.spriter.Data;
import com.brashmonkey.spriter.Drawer;
import com.pending.game.Assets;
import com.pending.game.GameVar;
import com.pending.game.assets.MainScreenAssets;
import com.pending.game.components.SpriterPlayerComponent;
import com.pending.game.components.TextureComponent;
import com.pending.game.components.TransformComponent;
import com.pending.game.support.GlobalInline;
import com.pending.game.support.spriter.SpriterDataLoader;
import com.pending.game.support.spriter.SpriterLibGdxDrawer;
import com.pending.game.tools.FamilyTools;
import com.pending.game.tools.MapperTools;

/**
 * 渲染系统
 * 渲染精灵的每一帧
 * 
 * @author D
 * @date 2016年8月28日 上午10:57:35
 */
public class RenderingSystem extends SortedIteratingSystem {
	
	private Drawer<Sprite> drawer;
	
	/**
	 * 显示效果
	 */
	private Stage subtitleStage;
	
	public RenderingSystem(int priority) {
		
		super(FamilyTools.renderingF, new Comparator<Entity>(){
			@Override
	        public int compare(Entity e1, Entity e2) {// 降序排列，后面的后绘制在上层。 返回正数绘制e1在上,返回负数绘制e2在上
				
				TransformComponent e1TransformComponent = MapperTools.transformCM.get(e1);
				TransformComponent e2TransformComponent = MapperTools.transformCM.get(e2);
				
//				int y = (int)Math.signum(e2TransformComponent.position.y - e1TransformComponent.position.y); // 先按y轴算
//				if(y == 0)
//					return (int)Math.signum(e1TransformComponent.index_z - e2TransformComponent.index_z); // 再按z抽算
				
				int y = (int)Math.signum(e1TransformComponent.index_z - e2TransformComponent.index_z); // 先按z轴算
				if(y == 0)
					return (int)Math.signum(e2TransformComponent.position.y - e1TransformComponent.position.y); // 再按y抽算
				
				return y;
	        }
		}, priority);
		
		SpriterDataLoader dataLoader = (SpriterDataLoader)Assets.instance.getLoader(Data.class);
		drawer = new SpriterLibGdxDrawer(dataLoader.getLoader(MainScreenAssets.spriterData), GameVar.batch);
		
		subtitleStage = new Stage(GameVar.gameViewport, GameVar.batch);
	}
	
	@Override
	public void update(float deltaTime) {
		
		GlobalInline.instance.mark();
		
		// 更新相机数据，并设置相机数据给batch
		GameVar.gameViewport.getCamera().update();
		GameVar.batch.setProjectionMatrix(GameVar.gameViewport.getCamera().combined);
		
		forceSort(); // 绘制排序
		
		GameVar.batch.begin();
		
		super.update(deltaTime);
		
		GameVar.batch.end();
		
		// 字幕
		subtitleStage.act();
		subtitleStage.draw();
		
		PhysicsSystem physicsSystem = GlobalInline.instance.getAshleyManager().engine.getSystem(PhysicsSystem.class);
		if(physicsSystem != null)
			physicsSystem.physicsManager.debugRender(GameVar.gameViewport.getCamera());
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		
		TransformComponent transformComponent = MapperTools.transformCM.get(entity);
		
		TextureComponent textureComponent = MapperTools.textureCM.get(entity);
		if(textureComponent != null){
			if(textureComponent.textureRegion == null){
	//			Gdx.app.log(this.toString(), "textureRegion is null");
			}
			else if(textureComponent.textureRegion instanceof Sprite){ // 绘制精灵
				Sprite sprite = (Sprite)textureComponent.textureRegion;
				sprite.setPosition(transformComponent.getRenderPositionX(), transformComponent.getRenderPositionY());
				sprite.draw(GameVar.batch);
			}
			else{  // 绘制纹理
		        /*
		         *  TextureRegion region, 绘制纹理
		         *  float x, float y, 绘制位置，已左下角为原点，该位置是指纹理的左下角的要在位置
		         *  float originX, float originY, 设置锚点，值是相对于原点（纹理左下角）的位置
		         *  float width, float height, 纹理宽高
		         *  float scaleX, float scaleY, 缩放，1是原始大小。从锚点向四周缩放
		         *  float rotation, 旋转，正数是逆时针。以锚点为圆心旋转
		         */
				GameVar.batch.draw(textureComponent.textureRegion, 
						transformComponent.getRenderPositionX(), transformComponent.getRenderPositionY(), 
						transformComponent.origin.x, transformComponent.origin.y,
						transformComponent.getWidth(), transformComponent.getHeight(),
						transformComponent.scale.x, transformComponent.scale.y,
						transformComponent.rotation);
			}
		}
		
		SpriterPlayerComponent spriterPlayerComponent = MapperTools.SpriterPlayerCM.get(entity);
		if(spriterPlayerComponent != null){
			spriterPlayerComponent.player.setPosition(transformComponent.getRenderPositionX(), transformComponent.getRenderPositionY());
			spriterPlayerComponent.player.update();
			// 1是未翻过 -1是亿翻过
			if(spriterPlayerComponent.player.flippedX() == 1 && transformComponent.flipX || spriterPlayerComponent.player.flippedX() == -1 && !transformComponent.flipX)
				spriterPlayerComponent.player.flipX();
			
			drawer.draw(spriterPlayerComponent.player);
		}
	}
}
