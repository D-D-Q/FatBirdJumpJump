package com.pending.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.MathUtils;
import com.pending.game.GameConfig;
import com.pending.game.manager.AshleyManager;
import com.pending.game.support.GlobalInline;
import com.pending.game.tools.MapperTools;

/**
 * 障碍系统
 * 
 * @author D
 * @date 2016年11月29日 下午8:52:18
 */
public class Monstersystem extends EntitySystem {
	
	private float positionY;

	public Monstersystem (int priority) {
		super(priority);
		positionY = 0;
	}

	@Override
	public final void update (float deltaTime) {
		
		Entity hero = GlobalInline.instance.get("hero");
		float heroY = MapperTools.physicsCM.get(hero).rigidBody.getPosition().y;
		
		// 第一次执行
		if(positionY == 0){
			positionY = heroY + 20;
			getEngine().addEntityListener(new MonstersystemEntityListener());
		}
				
		if(positionY - heroY >= GameConfig.height)
			return;
		
		float maxPositionY = heroY + GameConfig.height;
		
		AshleyManager ashleyManager = GlobalInline.instance.getAshleyManager();
				
		while(positionY < maxPositionY){
			
			Entity entity = ashleyManager.entityDao.createEntity2(MathUtils.random(100, GameConfig.width - 100), positionY, 100, 10);
			ashleyManager.engine.addEntity(entity);
			
			positionY += MathUtils.random(10, 100);
		}
	}
	
	/**
	 * engine.addEntity并不是实时添加, 所以只能在添加后才能操作刚体
	 * 
	 * @author D
	 * @date 2016年12月19日
	 */
	private class MonstersystemEntityListener implements EntityListener{
		
		@Override
		public void entityAdded(Entity entity) {
			MapperTools.physicsCM.get(entity).rigidBody.setGravityScale(0);
		}

		@Override
		public void entityRemoved(Entity entity) {
			
		}
	}
}
