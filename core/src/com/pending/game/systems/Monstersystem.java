package com.pending.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.Pools;
import com.pending.game.GameConfig;
import com.pending.game.manager.AshleyManager;
import com.pending.game.support.GlobalInline;
import com.pending.game.tools.MapperTools;

/**
 * 障碍系统
 * 
 * 1000高度 算一关, 提升一个难度, 总100关
 * 
 * 角色 宽20 高30
 * 平台高度 20
 * 
 * 难度提升点
 * 	跳台宽度 (100,85,70,55,40)
 * 	跳台高间隔 左右0到屏幕宽/2(左右0-270) ()
 * 	跳台宽间隔  平台高+角色高 到 跳跃最高点-平台高/2-1 = (40-84)
 * 	跳跃时间  
 * 		0.2秒的话 最高是60 起跳速度是极限600 
 *  	0.3秒的话 最高时90 起跳速度是极限600 最慢0.5秒 *
 *  
 * 
 * 
 * @author D
 * @date 2016年11月29日 下午8:52:18
 * 
 */
public class Monstersystem extends EntitySystem{
	
	/**
	 * 最高关, 从0开始
	 */
	public final static int maxLevel = 9;
	
	/**
	 * 难度阶数。以下难度变量数组长度必须等于它
	 */
	private final static int levelRange = 5;
	
	private final static float[] boardWidth = {100, 85, 70, 55, 40}; // 跳台宽度
//	private final static float[] boardWidthOffset = {54, 108, 162, 216f, 270}; // 跳台宽间隔
	
	private final static float minBoardHeightOffset = 40; // 跳台高间隔最小值
	private final static float maxBoardHeightOffset = 84; // 跳台高间隔最大值
	
	private static float[] boardHeightOffset; // 难度阶数数组
	static{
		boardHeightOffset = new float[levelRange];
		float step = (maxBoardHeightOffset-minBoardHeightOffset) / (levelRange-1);
		for(int i=0; i<levelRange; ++i){
			boardHeightOffset[i] = minBoardHeightOffset + step * i;
		}
	}
	
	/**
	 * 当前关卡, 从0开始，显示的时候+1
	 */
	private int level = 1;
	
	private Pool<Board> boardPool = Pools.get(Board.class, 100);
	
	private final Vector2 curPosition = new Vector2();

	public Monstersystem (int priority) {
		super(priority);
	}

	@Override
	public void update (float deltaTime) {
		
		Entity hero = GlobalInline.instance.get("hero");
		Vector2 heroPosition = MapperTools.physicsCM.get(hero).rigidBody.getPosition();
		
		// 第一次执行
//		if(positionY == 0){
		if(curPosition.y == 0){
//			positionY = heroY + 20;
			curPosition.x = heroPosition.x;
			curPosition.y = heroPosition.y + 20;
			getEngine().addEntityListener(new MonstersystemEntityListener());
		}
				
//		if(positionY - heroY >= GameConfig.height)
		if(curPosition.y - heroPosition.y >= GameConfig.height)
			return;
		
		float maxPositionY = heroPosition.y + GameConfig.height;
		
		AshleyManager ashleyManager = GlobalInline.instance.getAshleyManager();
				
//		while(positionY < maxPositionY){
		while(curPosition.y < maxPositionY){
			
//			Entity entity = ashleyManager.entityDao.createEntity2(MathUtils.random(100, GameConfig.width - 100), positionY, MathUtils.random(40, 40), 10);
//			ashleyManager.engine.addEntity(entity);
//			
//			positionY += MathUtils.random(90, 90);
			
			Board board = randomBoard(curPosition);
			Entity entity = ashleyManager.entityDao.createEntity2(board.x, board.y, board.width, Board.height);
			ashleyManager.engine.addEntity(entity);
			
			curPosition.set(board.x, board.y);
			boardPool.free(board);
		}
	}
	
	private final static float range = maxLevel * 1f / (levelRange+1);
	/**
	 * 共5阶难度配置
	 * 同时最多在3个连续阶内随机
	 * 最低是1阶自己，最高是5阶自己
	 * 
	 * 每阶的出现范围 设为range
	 * 
	 * 用进度条类比如下
	 * 总进度长 = range * 5阶
	 * 滑块长 = range * 3
	 * 滑块可移动长度 = maxLevel -1
	 * 滑块头部起始位置 = range
	 * 滑块头部结速位置 = range + maxLevel
	 * 
	 * range = (maxLevel -1) / (5阶 + 1)
	 * 
	 */
	private Board randomBoard(Vector2 curPosition){
		
		Board board = boardPool.obtain();
		
		float factorRange  = range + level;
		
		// 宽随机
		int index = (int)(MathUtils.random(factorRange - range*3, factorRange) / range);
		board.width = boardWidth[MathUtils.clamp(index, 0, levelRange - 1)];
		
//		index = (int)(MathUtils.random(factorRange - range*3, factorRange) / range);
//		float widthOffset = boardWidthOffset[MathUtils.clamp(index, 0, levelRange - 1)];
//		widthOffset = MathUtils.random(widthOffset - 54, widthOffset);
//		board.widthOffset = MathUtils.randomBoolean() ? widthOffset : -widthOffset;
		
		// x坐标随机
		float widthOffset = MathUtils.random(0, GameConfig.width/2);
		widthOffset = MathUtils.randomBoolean() ? widthOffset : -widthOffset;
		float x = curPosition.x + widthOffset;
		if(x < 10 + board.width/2){
			board.x = curPosition.x + (10 + board.width/2 - x); 
		}
		else if(x > GameConfig.width - board.width/2 -10){
			board.x = curPosition.x - (x - (GameConfig.width - board.width/2 - 10)); 
		}
		else{
			board.x = x;
		}
		
		// y坐标随机
		index = (int)(MathUtils.random(factorRange - range*3, factorRange) / range);
		index = MathUtils.clamp(index, 0, levelRange - 1);
		board.y = curPosition.y + MathUtils.random((index == 0 ? Board.height : boardHeightOffset[index-1]), boardHeightOffset[index]);
		
		Gdx.app.log(this.toString(), "boardPool free:" + boardPool.getFree());
		
		return board;
	}
	
	/**
	 * 关卡提升
	 */
	public void levelUp(){
		
		if(level < maxLevel){
			 ++level;
			 Gdx.app.log(this.toString(), "level up:" + level);
		}
	}
	
	public void setlevel(int level){
		if(level >= 0 && level <= maxLevel) this.level = level;
	}
	
	public int getLevel(){
		return level;
	}
	
	/**
	 * 跳台数值
	 * 
	 * @author D
	 * @date 2016年12月27日
	 */
	public static class Board implements Poolable {

		public final static float height = 10;
		public float width;
		public float x;
		public float y;
		
		@Override
		public void reset() {
			width = 0;
			x = 0;
			y = 0;
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
