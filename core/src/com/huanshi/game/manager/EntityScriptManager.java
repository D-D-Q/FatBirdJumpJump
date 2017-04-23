package com.huanshi.game.manager;

import com.badlogic.gdx.utils.ObjectMap;
import com.huanshi.game.EntityScript;

/**
 * 所有脚本对象管理, 暂时不用了
 * 
 * @author D
 * @date 2017年1月5日
 */
public class EntityScriptManager {
	
	public static EntityScriptManager instance = new EntityScriptManager();

	private ObjectMap<Object, EntityScript> EntityScriptMap;
	
	public EntityScriptManager() {
		EntityScriptMap = new ObjectMap<>(); // TODO 脚本池大小
	}
}
