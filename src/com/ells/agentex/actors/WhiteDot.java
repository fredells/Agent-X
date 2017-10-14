package com.ells.agentex.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class WhiteDot {

	private float x;
	private float y;
	private Vector2 movement;
	private float opacity;
	private float radius;

	public WhiteDot(boolean startEnd) {
		movement = new Vector2((float) ((Math.random()+0.1f)*10),0);
		radius =(float)Math.random()*30;
		opacity = (float)Math.random();
		x = (Gdx.graphics.getWidth()+radius);
		if(!startEnd) {
			x*= Math.random();
		}
		y = (int) (Math.random()*Gdx.graphics.getHeight());
	}
	public WhiteDot(Vector2 movement, float radius, float opacity, int startX, int startY) {
		this.radius = radius;
		x = startX;
		y = startY;
		opacity -= 0.1f;
		this.opacity = opacity;
		this.movement = movement;
	}

	public void update(float delta) {
		this.x -= delta * movement.x * 10;
		this.y -= delta * movement.y;
		opacity -= 0.05f*delta;
	}
	public void update(float delta, Vector2 boost) {
		this.x -= delta * (movement.x) * 10+ delta * (movement.x) * 10*boost.x;
		this.y -= delta * movement.y*boost.y;
		if(this.x > Gdx.graphics.getWidth()+this.radius) {
			this.x = Gdx.graphics.getWidth()+this.radius;
		}
		opacity -= Math.abs(0.05f*delta*boost.x);
	}
	public void draw(ShapeRenderer m) {
		m.setColor(1, 1, 1, opacity);
		m.circle(x, y, radius, 100);
		// super.draw(m, opacity);
	}

	public float getRadius() {
		// TODO Auto-generated method stub
		return radius;
	}

	public float getCustomX() {
		// TODO Auto-generated method stub
		return x;
	}
	public void setCustomX(float customX) {
		x = customX;
	}
	public void addX(float x) {
		this.x +=x;
	}
	public float getOpactiy() {
		// TODO Auto-generated method stub
		return opacity;
	}
}
