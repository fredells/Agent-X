package com.ells.agentex.tables;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.ells.agentex.AgentExGame;
import com.ells.agentex.screens.LevelScreen;

public class CoinCounter extends Table {

	Skin skin;
	String str;
	int coins;
	int totalData;
	float screenWidth = Gdx.graphics.getWidth();
	float screenHeight = Gdx.graphics.getHeight();
	int tempVar = 0;
	int varDif;
	Interpolation interp = Interpolation.linear;
	Label experienceNum;
	float lifeTime = 0.5f;
	float elapsed = 0f;
	int acts = 0;
	public float expWidth = 0;

	public CoinCounter(AgentExGame game) {
		skin = game.loader.skin;
		//this.setDebug(true);
		coins = (Integer) game.loader.getProfile().get("coins");
		Label experience = new Label("Experience  ", skin, "numberStyle");
		experienceNum = new Label("" + tempVar, skin, "textStyle");
		totalData = (Integer) game.loader.getProfile().get("totalData");
		Label data = new Label("Data Collected  ", skin, "numberStyle");
		Label dataNum = new Label("" + totalData + " /200", skin, "textStyle");
		
		expWidth = experience.getWidth();

		// this.addAction(Actions.fadeIn(10f));
		this.add(experience).spaceBottom(10).align(Align.right);
		this.add(experienceNum).align(Align.left).spaceBottom(10).row();
		this.add(data).align(Align.right);
		this.add(dataNum).align(Align.left);
		this.setPosition(screenWidth - experience.getWidth() - 50, screenHeight - experience.getHeight() - 30);
		/*
		 * this.addAction(sequence(Actions.fadeIn(2f), run(new Runnable() {
		 * 
		 * @Override public void run() { } })));
		 */
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		acts++;
		elapsed += delta;
		if (acts == 3 && elapsed < lifeTime*1.1) {
			acts = 0;
			float progress = elapsed / lifeTime;
			int progNum = (int) (coins * (interp.apply(progress)));
			experienceNum.setText(Integer.toString(progNum));
		}
		else if(elapsed > lifeTime) {
			   experienceNum.setText(""+coins);
		  }

	}

}
