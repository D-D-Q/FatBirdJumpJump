package com.pending.game.tools;

import com.badlogic.ashley.core.ComponentMapper;
import com.pending.game.components.PhysicsComponent;
import com.pending.game.components.ScriptComponent;
import com.pending.game.components.TextureComponent;
import com.pending.game.components.TransformComponent;

/**
 * 组件映射工具
 * 
 * @author D
 * @date 2016年8月28日 下午8:40:47
 */
public class MapperTools {

	
	public static final ComponentMapper<TransformComponent> transformCM = ComponentMapper.getFor(TransformComponent.class);;
//	public static final ComponentMapper<AnimationComponent> animationCM = ComponentMapper.getFor(AnimationComponent.class);
	public static final ComponentMapper<TextureComponent> textureCM = ComponentMapper.getFor(TextureComponent.class);
	public static final ComponentMapper<ScriptComponent> scriptCM = ComponentMapper.getFor(ScriptComponent.class);
	
	public static final ComponentMapper<PhysicsComponent> physicsCM = ComponentMapper.getFor(PhysicsComponent.class);
}
