package com.ells.agentex.utils.overlays;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.ells.agentex.AgentExGame;
import com.ells.agentex.listeners.MainMenuButtonListener;
import com.ells.agentex.screens.GameScreen;
import com.ells.agentex.screens.LevelScreen;
import com.ells.agentex.screens.MenuScreen;
import com.ells.agentex.stages.NewGameStage;
import com.ells.agentex.stages.UIStage;

public class PauseOverlay {
	private TextButton restart, levelSelect, resume, mainMenu;
	private ImageButton tutorial;
	private Table table;
	private AgentExGame game;
	private ClickListener oldResume = new ClickListener(), oldRestart = new ClickListener(),
			levelSelectListener = new ClickListener();
	private ClickListener mainMenuListener = new ClickListener();
	private String lastGameMode = "";

	public PauseOverlay(float screenWidth, float screenHeight, Skin skin) {
		this.game = (AgentExGame) Gdx.app.getApplicationListener();
		table = new Table();
		// table.debug();
		mainMenu = new TextButton("Main Menu", skin, "backStyle");
		restart = new TextButton("restart", skin, "backStyle");
		levelSelect = new TextButton("Level Select", skin, "backStyle");
		resume = new TextButton("resume", skin, "backStyle");

		table.add(mainMenu).padBottom(25).row();
		table.add(levelSelect).padBottom(25).row();
		table.add(restart).padBottom(25).row();
		table.add(resume).padBottom(25).row();
		table.setFillParent(true);
	}

	public void addToStage(final UIStage stage) {
		stage.gameStage.startPause += TimeUtils.millis();
		// really weird, otherwise whenever you pause, press resume, pause, then
		// try to resume it does nothing, all other buttons work so readding the
		// listener
		stage.addActor(table);

	}

	public void addThingsThatNeedStage() {
		restart.removeListener(oldRestart);
		mainMenu.removeListener(mainMenuListener);
		resume.removeListener(oldResume);
		levelSelect.removeListener(levelSelectListener);
		//mainMenuListener = new MainMenuButtonListener(((NewGameStage) ((GameScreen)game.getScreen()).getStage()), table, game);
		
		mainMenuListener = new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				game.musicManager.resume();
				if (lastGameMode == "playing") {
					// game.musicManager.resume();
					game.manager.get("InGameOverlay", InGameOverlay.class).enableButtons();
					game.loader.darkActive = false;
					table.remove();
					((GameScreen)game.getScreen()).getStage().getUiStage().addAction(sequence(fadeOut(0.01f), run(new Runnable() {
						@Override
						public void run() {
							((Game) Gdx.app.getApplicationListener()).setScreen(new MenuScreen(game));
							game.loader.darkActive = false;
						}
					})));
					
				} else {
					for (Actor a : ((NewGameStage) ((GameScreen)game.getScreen()).getStage()).getUiStage().getActors()) {
						a.setVisible(true);
					}
					game.manager.get("PreGameOverlay", PreGameOverlay.class).enableButtons();
					game.loader.darkActive = false;
					table.remove();
					((GameScreen)game.getScreen()).getStage().getUiStage().addAction(sequence(fadeOut(0.01f), run(new Runnable() {
						@Override
						public void run() {
							((Game) Gdx.app.getApplicationListener()).setScreen(new MenuScreen(game));
							game.loader.darkActive = false;
						}
					})));
				}
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		};
		
		
		oldResume = new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				game.musicManager.resume();
				if (lastGameMode == "playing") {
					// game.musicManager.resume();
					game.manager.get("InGameOverlay", InGameOverlay.class).enableButtons();
					((NewGameStage) ((GameScreen)game.getScreen()).getStage()).setGameMode("playing");
					game.loader.darkActive = false;
					System.out.println("resume");
					table.remove();
				} else {
					for (Actor a : ((NewGameStage) ((GameScreen)game.getScreen()).getStage()).getUiStage().getActors()) {
						a.setVisible(true);
					}
					game.manager.get("PreGameOverlay", PreGameOverlay.class).enableButtons();
					((NewGameStage) ((GameScreen)game.getScreen()).getStage()).setGameMode("preGame");
					game.loader.darkActive = false;
					System.out.println("resume");
					table.remove();
				}
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		};
		oldRestart = new ClickListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

				game.loader.darkActive = false;
				table.remove();
				game.manager.get("PreGameOverlay", PreGameOverlay.class).enableButtons();
				game.manager.get("InGameOverlay", InGameOverlay.class).enableButtons();
				// need to do this otherwise when you pause, restart and hit
				// pause again the restart button is
				// held down, stays red.
				for (Actor a : ((NewGameStage) ((GameScreen)game.getScreen()).getStage()).getUiStage().getActors()) {
					a.setVisible(true);
				}
				((NewGameStage) ((GameScreen)game.getScreen()).getStage()).getUiStage().addAction(sequence(fadeOut(0.01f), run(new Runnable() {

					@Override
					public void run() {
						((Game) Gdx.app.getApplicationListener())
								.setScreen(new GameScreen(((NewGameStage) ((GameScreen)game.getScreen()).getStage()).getEquationsFromAxes()));
					}
				})));
			}
		};
		levelSelectListener = new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				table.remove();
				for (Actor a : ((NewGameStage) ((GameScreen)game.getScreen()).getStage()).getUiStage().getActors()) {
					a.setVisible(true);
				}
				((NewGameStage) ((GameScreen)game.getScreen()).getStage()).getUiStage().addAction(sequence(fadeOut(0.01f), run(new Runnable() {

					@Override
					public void run() {
						game.loader.darkActive = false;
						((AgentExGame) Gdx.app.getApplicationListener()).setScreen(new LevelScreen(game));
					}
				})));
				game.manager.get("InGameOverlay", InGameOverlay.class).enableButtons();
				game.manager.get("PreGameOverlay", PreGameOverlay.class).enableButtons();
			}
		};
		mainMenu.addListener(mainMenuListener);
		restart.addListener(oldRestart);
		resume.addListener(oldResume);
		levelSelect.addListener(levelSelectListener);

	}

	public void showAfterTutorial() {
		table.setVisible(true);
		game.loader.darkActive = true;
	}

	public void updatePause(String gameMode) {
		this.lastGameMode = gameMode;
		try {
			tutorial.remove();
		} catch (Exception e) {
			// do nothing
		}

		// add tutorial images
		if (Gdx.files.internal(("img/" + LevelScreen.currentLevel + 1 + ".png")).exists()) {
			tutorial = new ImageButton(new TextureRegionDrawable(
					new TextureRegion(new Texture(Gdx.files.internal("img/information.png")))));
			tutorial.setSize(Gdx.graphics.getWidth() / 12, Gdx.graphics.getWidth() / 10);
			tutorial.setPosition(0, Gdx.graphics.getHeight() - Gdx.graphics.getWidth() / 10);
			tutorial.pad(15);
			tutorial.getImage().setColor(1, 1, 1, 0.8f);
			tutorial.addListener(new ClickListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					return true;
				}

				@Override
				public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
					// hide buttons for tutorial images
					table.setVisible(false);
					game.loader.darkActive = false;

					Array<String> imageNames = new Array<String>();
					for (int i = 1; i < 50; i++) {
						if (Gdx.files.internal(("img/" + LevelScreen.currentLevel + i + ".png")).exists()) {
							// load tutorial images automatically on first ever
							// level load
							imageNames.add("img/" + LevelScreen.currentLevel + i + ".png");
						}

						else {
							break;
						}

					}
					game.manager.get("TutorialOverlay", TutorialOverlay.class).loadImages(imageNames);
					game.manager.get("TutorialOverlay", TutorialOverlay.class).addToStageAfterTutorial();

				}
			});
			table.addActorAt(0, tutorial);
		}

	}
	public void removeListeners() {
		restart.removeListener(oldRestart);
		mainMenu.removeListener(mainMenuListener);
		resume.removeListener(oldResume);
		levelSelect.removeListener(levelSelectListener);
	}
}
