package com.pending.game.support;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pending.game.GameConfig;

public class GameUtil {

	public static float getDisplayWidth(Viewport viewport){
		
		// ScreenX和ScreenY谁是0，谁就是可以全部显示的，使用的就是该边的比例
		float scale = viewport.getScreenX() == 0 ? viewport.getScreenWidth()/GameConfig.width : viewport.getScreenHeight()/GameConfig.height; //
				
		return (viewport.getScreenWidth() - Math.abs(viewport.getScreenX()) * 2) / scale;
	}
	
	/**
	 * 创建一个实际屏幕显示大小的UI根
	 * 
	 * @param stage
	 * @param viewport
	 * @return
	 */
	public static Group createDisplaySizeGroup(Stage stage, Viewport viewport) {
		
		// ScreenX和ScreenY谁是0，谁就是可以全部显示的，使用的就是该边的比例
		float scale = viewport.getScreenX() == 0 ? viewport.getScreenWidth()/GameConfig.width : viewport.getScreenHeight()/GameConfig.height; //
		
		// 实际屏幕上能显示出来的宽高
		float displayWidth = (viewport.getScreenWidth() - Math.abs(viewport.getScreenX()) * 2) / scale;
		float displayHeight = (viewport.getScreenHeight() - Math.abs(viewport.getScreenY()) * 2) / scale;
		
		Table defaultTable = new Table();
		defaultTable.setFillParent(true);
		defaultTable.defaults().size(displayWidth, displayHeight).center();
		stage.addActor(defaultTable);
		
		Group group = new Group();
		defaultTable.add(group);
		
		return group;
	}
}
