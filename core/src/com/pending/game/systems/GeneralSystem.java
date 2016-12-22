package com.pending.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import com.pending.game.components.ScriptComponent;
import com.pending.game.manager.MsgManager;
import com.pending.game.tools.FamilyTools;
import com.pending.game.tools.MapperTools;

public class GeneralSystem extends IteratingSystem {

	public GeneralSystem(int priority) {
		super(FamilyTools.generalF, priority);
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		
		MsgManager.instance.update(); // 处理消息
		
		ScriptComponent scriptComponent = MapperTools.scriptCM.get(entity);
		if(scriptComponent != null)
			scriptComponent.script.update(deltaTime);
	}
}
