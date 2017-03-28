package com.pending.game.assets;

import com.badlogic.gdx.graphics.Texture;
import com.brashmonkey.spriter.Data;
import com.pending.game.annotation.Asset;

/**
 * 闪屏时会加载的资源
 * 
 * @author D
 * @date 2017年3月7日
 */
public class MainScreenAssets {

	@Asset(Data.class)
	public final static String spriterData = "data/bird_2_fat_bird.scml";
	
	@Asset(Texture.class)
	public final static String p = "data/p.png";
	
	@Asset(Texture.class)
	public final static String bg1 = "data/bg1.png";
	
	@Asset(Texture.class)
	public final static String bg2 = "data/bg2.png";
	
	@Asset(Texture.class)
	public final static String bg3 = "data/bg3.png";

	@Asset(Texture.class)
	public final static String cloud1 = "data/cloud1.png";

	@Asset(Texture.class)
	public final static String cloud2 = "data/cloud2.png";

	@Asset(Texture.class)
	public final static String cloud3 = "data/cloud3.png";

	@Asset(Texture.class)
	public final static String cloud4 = "data/cloud4.png";
	
}
