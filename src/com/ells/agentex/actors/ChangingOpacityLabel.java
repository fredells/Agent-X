package com.ells.agentex.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ChangingOpacityLabel extends Label{
	public ChangingOpacityLabel(String chars, Skin skin, String styleName) {
		super(chars, skin, styleName);
	}
	@Override
	public void draw(Batch batch, float parentAlpha) {
	    super.draw(batch, Gdx.app.getPreferences("agentx").getFloat("equationTableOpacity"));
	}


}
