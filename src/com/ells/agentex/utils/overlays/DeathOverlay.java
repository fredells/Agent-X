package com.ells.agentex.utils.overlays;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ells.agentex.AgentExGame;
import com.ells.agentex.screens.GameScreen;
import com.ells.agentex.screens.LevelScreen;
import com.ells.agentex.screens.MenuScreen;
import com.ells.agentex.sound.MusicManager.MUSICSTATE;
import com.ells.agentex.stages.NewGameStage;
import com.ells.agentex.stages.UIStage;

public class DeathOverlay {
	private TextButton restart, levelSelect, mainMenu, retry;
	private Table deathTable = new Table();
	private AgentExGame game;
	private ClickListener levelSelectListener = new ClickListener();
	private ClickListener restartListener = new ClickListener();
	private ClickListener retryListener = new ClickListener();
	private ClickListener mainMenuListener = new ClickListener();

	public DeathOverlay(float screenWidth, float screenHeight, Skin skin, final AgentExGame game) {
		this.game = game;
		if (mainMenu != null) {
			mainMenu.clearListeners();
		}
		if (levelSelect != null) {
			levelSelect.clearListeners();
		}
		// deathTable.debug();
		mainMenu = new TextButton("Main Menu", skin, "backStyle");
		restart = new TextButton("restart", skin, "backStyle");
		restart.addListener(restartListener);
		retry = new TextButton("retry", skin, "backStyle");
		retry.addListener(retryListener);
		levelSelect = new TextButton("Level Select", skin, "backStyle");
		levelSelect.addListener(levelSelectListener);
		deathTable.add(mainMenu).padTop(40).padBottom(40);
		deathTable.row();
		deathTable.add(levelSelect).padBottom(40);
		deathTable.row();
		deathTable.add(restart).padBottom(40);
		deathTable.row();
		deathTable.add(retry);
		deathTable.setFillParent(true);

		// deathTable.setPosition(screenWidth/2- deathTable.getWidth(),
		// screenHeight- restart.getHeight() -
		// levelSelect.getHeight()-mainMenu.getHeight());
	}

	public void addToStage(final UIStage stage) {
		// really weird, otherwise whenever you pause, press resume, pause, then
		// try to resume it does nothing, all other buttons work
		stage.gameMode = "dead";
		game.manager.get("InGameOverlay", InGameOverlay.class).removeOverlay();
		stage.addActor(deathTable);
	}

	public void addThingsThatNeedStage(final NewGameStage stage) {
		levelSelect.removeListener(levelSelectListener);
		restart.removeListener(restartListener);
		retry.removeListener(retryListener);
		mainMenu.removeListener(mainMenuListener);
		mainMenuListener = new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				game.musicManager.changeTrack(MUSICSTATE.MENU);
				game.loader.darkActive = false;
				((Game) Gdx.app.getApplicationListener()).setScreen(new MenuScreen(game));
				// InGameOverlay.enablebuttons();
			}

		};

		levelSelectListener = new ClickListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				game.musicManager.changeTrack(MUSICSTATE.MENU);
				game.loader.darkActive = false;
				((AgentExGame) Gdx.app.getApplicationListener()).setScreen(new LevelScreen(game));
			}
		};
		restartListener = new ClickListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				game.loader.darkActive = false;
				((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(stage.getEquationsFromAxes()));
			}
		};
		retryListener = new ClickListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				game.loader.darkActive = false;
				// stage.goNow = true;
				((Game) Gdx.app.getApplicationListener())
						.setScreen(new GameScreen(stage.getEquationsFromAxes(), game, true));
			}
		};

		retry.addListener(retryListener);
		restart.addListener(restartListener);
		levelSelect.addListener(levelSelectListener);
		mainMenu.addListener(mainMenuListener);
	}
	public void clearListeners() {
		retry.removeListener(retryListener);
		restart.removeListener(restartListener);
		levelSelect.removeListener(levelSelectListener);
		mainMenu.removeListener(mainMenuListener);
	}

}
