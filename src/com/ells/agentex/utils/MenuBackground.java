package com.ells.agentex.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter.Particle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.ells.agentex.actors.WhiteDot;

public class MenuBackground {
	SpriteBatch mBatch;
	Sprite back;
	ParticleEffect p;
	private float boostTime = 0;
	private float boostMultiplier = 1;

	public MenuBackground() {
		mBatch = new SpriteBatch();
		back = new Sprite(new Texture(Gdx.files.internal("data/gradient1.png")));
		p = new ParticleEffect();
		p.load(Gdx.files.internal("particles/background"), Gdx.files.internal("particles"));
		p.getEmitters().get(0).setAdditive(false);
		p.getEmitters().get(0).getEmission().setHigh(1, 4);
		p.getEmitters().get(0).getLife().setHigh(3000, 15000);
		p.getEmitters().get(0).getVelocity().setHigh(Gdx.graphics.getWidth()/(p.getEmitters().get(0).getLife().getHighMin()/900), Gdx.graphics.getWidth()/(p.getEmitters().get(0).getLife().getHighMax()/100));
		p.getEmitters().get(0).getSpawnHeight().setHigh(0, Gdx.graphics.getHeight()*0.8f);
		p.start();
		p.setPosition(Gdx.graphics.getWidth()*1.2f, Gdx.graphics.getHeight()/10);
		
	}

	public void render(float delta) {
		if(boostTime > 0) {
			boostTime -= delta;
			if(boostTime <= 0) {
				boostMultiplier = 1;
				boostTime = 0;
				p.update(delta);
			} else {
				p.update(delta*boostMultiplier);
			}
			
		} else {
			p.update(delta);
		}
		mBatch.begin();
		mBatch.draw(back, 0f, 0f, Gdx.graphics.getWidth(), Gdx.graphics.getWidth());
		p.draw(mBatch);
		mBatch.end();
		Gdx.gl.glEnable(GL20.GL_BLEND);

	}

	public void boost(float boost, float time) {
		if(boost < 0) {
			boost = -1/boost;
		}
		boostMultiplier = boost;
		boostTime = time;
	}
	public void stopBoost(){
		boostMultiplier = 1;
		boostTime = 0;
	}
}
