package com.ells.agentex.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class TrickLabel extends Label{
	public CharSequence text;
	public boolean inUse = false;
	
	public TrickLabel(CharSequence text, LabelStyle style) {
		super(text, style);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void act(float delta) {
		super.act(delta);
		if(super.getActions().size < 1) {
			this.inUse = false;
		} else {
			this.inUse = true;
		}
	}
	public void activateLabel(Stage stage, float x, float y, String text) {
		System.out.println("ACTIVATE LABEL");
		this.inUse = true;
		this.setPosition(x, y);
		this.setText(text);
		stage.addActor(this);
		this.addAction(Actions.sequence(Actions.fadeOut(1), Actions.removeActor()));
		this.addAction(Actions.moveBy(0, Gdx.graphics.getHeight() / 6, 1));
	}

}
