package com.ells.agentex.tables;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ells.agentex.AgentExGame;
import com.ells.agentex.screens.GameScreen;

public class DeathMenu extends Table {
	
	
	Skin skin;
	String str;
	int var;
	float screenWidth = Gdx.graphics.getWidth();
	float screenHeight = Gdx.graphics.getHeight();

	public DeathMenu(final AgentExGame game) {
		this.skin = game.loader.skin;
		this.setDebug(true);
		this.setFillParent(false);
		this.setOrigin(this.getWidth() / 2f, this.getHeight() / 2f);
		this.setTransform(true);
		this.invalidateHierarchy();
		
		this.setX(screenWidth / 2);
		this.setY(screenHeight / 2);
		
		
		//var = (Integer) AssetLoader.getProfile().get("coins");
		Label heading = new Label("Mission Failed", skin, "default");
		heading.setFontScale(2);
		
		// creating buttons
		TextButton buttonRetry = new TextButton("RETRY", skin, "default");
		buttonRetry.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen());
			}
		});
		buttonRetry.pad(25);

		
		this.add(heading).spaceBottom(25).row();
		this.add(buttonRetry);
		
		
	}
}

