package com.ells.agentex.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class OverflowButton extends ImageButton{

	public OverflowButton(Skin skin) {
		super(skin);
		
	}
	@Override
	public void draw(Batch batch, float parentAlpha) {
	    setClip(true);
	    super.draw(batch, parentAlpha);
	    setClip(false);
	}
	
}
