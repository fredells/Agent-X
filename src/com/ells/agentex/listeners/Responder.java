package com.ells.agentex.listeners;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.TimeUtils;
import com.ells.agentex.actors.Equation;
import com.ells.agentex.actors.TableButton;
import com.ells.agentex.actors.CustomTable;

public class Responder extends ClickListener {
	final TableButton button;
	final Equation equation;
	final Skin skin;
	final CustomTable table;
	public Responder(TableButton button, Equation equation, Skin skin, CustomTable table) {
		this.button = button;
		this.equation = equation;
		this.skin = skin;
		this.table = table;
	}
	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		this.button.setPressedStartTime(TimeUtils.millis());
		this.button.setPressed(true);
		table.generateTable(equation, skin);
		return true;
		
	}
	@Override
	public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
		this.button.setPressedTime(TimeUtils.millis() - this.button.getPressedStartTime());
		this.button.setPressed(false);
	}
}