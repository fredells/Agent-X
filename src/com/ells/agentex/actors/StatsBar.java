package com.ells.agentex.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class StatsBar extends Image{
	
	private Texture back, front;
	private float pct;
	public StatsBar(Texture back, Texture front, float pct) {
		this.back = back;
		this.front = front;
		this.pct = pct/100f;
		this.setHeight(back.getHeight());
		System.out.println(back.getHeight());
		System.out.println(this.getWidth());
	}
	@Override
	public void draw(Batch batch, float parentOpacity) {
		batch.draw(back, this.getX(), this.getY(), this.getWidth(), this.getHeight());
		batch.draw(front, this.getX(), this.getY(), pct*this.getWidth(), this.getHeight());
	}
}
