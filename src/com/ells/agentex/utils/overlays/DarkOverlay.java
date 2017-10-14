package com.ells.agentex.utils.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.ells.agentex.AgentExGame;

public class DarkOverlay extends Actor{
	private Sprite dark;
	public DarkOverlay(final AgentExGame game) {
		dark = new Sprite(new TextureRegion(new Texture("img/dark.png")));
		dark.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		dark.setAlpha(0.75f);
	}

	public void draw(SpriteBatch batch) {
        dark.draw(batch);
    }
}
