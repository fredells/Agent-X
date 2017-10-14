package com.ells.agentex;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.ells.agentex.screens.LoadingScreen;
import com.ells.agentex.sound.FXManager;
import com.ells.agentex.sound.MusicManager;
import com.ells.agentex.utils.AssetLoader;
import com.ells.agentex.utils.MenuBackground;
import com.ells.agentex.utils.overlays.PreGameOverlay;

public class AgentExGame extends Game {

	public static final String TITLE = "Agent X", VERSION = "0.1";
	public AssetManager manager;
	public AssetLoader loader;
	public MenuBackground background;
	public MusicManager musicManager;
	public FXManager fxManager;

	@Override
	public void create() {
		Gdx.app.log("Agent X", "created");
		manager = new AssetManager();
		loader = new AssetLoader();
		background = new MenuBackground();
		musicManager = new MusicManager(!Gdx.app.getPreferences("agentx").getBoolean("music"), 1, manager);
		fxManager = new FXManager(!Gdx.app.getPreferences("agentx").getBoolean("sound"), 1, manager);
		// Bypass menu
		/**
		 * LevelScreen.currentLevel = "a1"; LevelScreen.intLevel = 1;
		 * LevelScreen.stringPack = "a";
		 * setScreen(new GameScreen());
		 * 
		 **/
		// Normal Start Procedure

		setScreen(new LoadingScreen(this));
		Gdx.app.log("Agent X", "Screen set to load");

	}
	@Override
	public void render() {
		super.render();
		musicManager.act(Gdx.graphics.getDeltaTime());
	}
	@Override
	public void setScreen(Screen screen) {
		System.out.println("changing a screen");
		if (this.screen != null) this.screen.dispose();
		this.screen = screen;
		if (this.screen != null) {
			this.screen.show();
			this.screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
}
	}

}
