package com.huanshi.game.tools;

import com.badlogic.ashley.core.Family;
import com.huanshi.game.components.PhysicsComponent;
import com.huanshi.game.components.ScriptComponent;
import com.huanshi.game.components.SpriterPlayerComponent;
import com.huanshi.game.components.TextureComponent;
import com.huanshi.game.components.TransformComponent;

/**
 * family集合工具
 * 
 * @author D
 * @date 2016年8月28日 下午8:40:28
 */
public class FamilyTools {

	public static final Family generalF = Family.one(ScriptComponent.class).get();
	public static final Family renderingF = Family.all(TransformComponent.class).one(TextureComponent.class, SpriterPlayerComponent.class).get();
	public static final Family physicsF = Family.all(TransformComponent.class).one(PhysicsComponent.class).get();
}
