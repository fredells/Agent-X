package com.ells.agentex.tables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.ells.agentex.AgentExGame;

public class LevelComplete extends Table {

	Skin skin;
	String str;
	int var;
	float screenWidth = Gdx.graphics.getWidth();
	float screenHeight = Gdx.graphics.getHeight();
	private AgentExGame game;
	public LevelComplete(AgentExGame game) {
		this.game = game;
		skin = game.loader.skin;
		this.setDebug(true);
		this.setPosition(this.getWidth()+110, this.getHeight() / 2 + screenHeight  - 50);
		
		var = (Integer) game.loader.getProfile().get("coins");
		Label heading = new Label("Bitcoins: " + var,skin.get("style", LabelStyle.class));
		
		this.add(heading);
		
	}
	
}