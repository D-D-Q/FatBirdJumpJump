package com.huanshi.game.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;

/**
 * 输入事件管理器
 * 		在InputMultiplexer增加整体禁用输入事件功能
 * 
 * @author D
 * @date 2016年10月14日
 */
public class InputManager extends InputMultiplexer{
	
	public static InputManager instance = new InputManager();
	
	private boolean disabled = false;
	
	public InputManager() {
		Gdx.input.setCatchBackKey(true);
	}
	
	public boolean isDisabled(){
		return disabled;
	}
	
	public void setDisabled(boolean disabled){
		this.disabled = disabled;
	}
	
	@Override
	public boolean keyDown (int keycode) {
		if(disabled)
			return true;
		return super.keyDown(keycode);
	}

	@Override
	public boolean keyUp (int keycode) {
		if(disabled)
			return true;
		return super.keyUp(keycode);
	}

	@Override
	public boolean keyTyped (char character) {
		if(disabled)
			return true;
		return super.keyTyped(character);
	}

	@Override
	public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		if(disabled)
			return true;
		return super.touchDown(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		if(disabled)
			return true;
		return super.touchUp(screenX, screenY, pointer, button);
	}
	
	@Override
	public boolean touchDragged (int screenX, int screenY, int pointer) {
		if(disabled)
			return true;
		return super.touchDragged(screenX, screenY, pointer);
	}

	@Override
	public boolean mouseMoved (int screenX, int screenY) {
		if(disabled)
			return true;
		return super.mouseMoved(screenX, screenY);
	}

	@Override
	public boolean scrolled (int amount) {
		if(disabled)
			return true;
		return super.scrolled(amount);
	}
}
