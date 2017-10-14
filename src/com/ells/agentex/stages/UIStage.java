package com.ells.agentex.stages;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.ells.agentex.AgentExGame;
import com.ells.agentex.actors.Player;
import com.ells.agentex.listeners.MainGameGestures;
import com.ells.agentex.screens.GameScreen;
import com.ells.agentex.screens.LevelScreen;
import com.ells.agentex.screens.MenuScreen;
import com.ells.agentex.utils.overlays.InGameOverlay;
import com.ells.agentex.utils.overlays.PreGameOverlay;
import com.ells.agentex.utils.overlays.WinOverlay;
import com.gushikustudios.rube.RubeScene;

public class UIStage extends Stage implements InputProcessor {
	private float screenWidth;
	public PreGameOverlay preGameUI;
	public NewGameStage gameStage;
	public int activeAxis = 0;
	private InputMultiplexer m;
	private Player player;
	public String cameraMode = "preGame";
	public String gameMode = "preGame";
	public Vector2 cameraVelocity = new Vector2(0, 0);
	public OrthographicCamera camera;
	public AgentExGame game;
	public Rectangle bounds;
	private MainGameGestures listener;
	private boolean fling = false;
	public boolean movingToAxis = false;
	public boolean leftAxisFocus = false;
	private float ratioZoom = 1280 / screenWidth;

	public UIStage(final NewGameStage stage, final Player player, final AgentExGame game) {
		this.game = game;
		preGameUI = game.manager.get("PreGameOverlay", PreGameOverlay.class);
		screenWidth = game.loader.screenWidth;
		this.gameStage = stage;
		this.player = player;
		setupCamera();
		preGameUI.addToStage(this);
		m = new InputMultiplexer();
		Gdx.input.setInputProcessor(m);

	}

	public void resize(int width, int height) {
		// See below for what true means.
		this.getViewport().update(width, height, true);
	}

	@Override
	public void dispose() {
		this.gameStage = null;
		preGameUI =null;
		m.clear();
		this.clear();
		super.dispose();
	}

	public void updatePregameUI() {
		if (gameStage.focus < gameStage.getAxes().size + 1 && gameStage.focus > 0 && gameStage.focus % 1 == 0) {
			activeAxis = (int) gameStage.focus - 1;
		}
		if (cameraMode.equals("preGame")) {
			player.setActive(false);
			// determining focus between focusPoints

			if (preGameUI.topRightButtons.get(0).getStage() != null) {
				for (Actor actor : preGameUI.topRightButtons) {
					actor.remove();
				}
				preGameUI.addToStage(this);
				if (gameStage.getAxes().get(activeAxis).getEquationTable() != null) {
					gameStage.getAxes().get(activeAxis).getEquationTable().remove();
				}
			}
		} else if (cameraMode.equals("axisFocus")) {
			// axis focus mode
			if (gameStage.focus % 1 == 0) {
				if (gameStage.getAxes().get(activeAxis).getEquationTable() != null) {
					this.addActor(gameStage.getAxes().get(activeAxis).getEquationTable());
				}
				preGameUI.addBackNext(this);
				preGameUI.addTopRight(this);
			}
		}
	}

	private void setupCamera() {
		camera = new OrthographicCamera(gameStage.VIEWPORT_WIDTH, gameStage.VIEWPORT_HEIGHT);
		cameraMode = "preGame";
		gameStage.focus = 0;
		gameStage.snapMode = false;
		camera.zoom = 1.1f;
		camera.position.x += camera.unproject(new Vector3(Gdx.graphics.getWidth() * 0.75f, 0, 0)).x;
		camera.update();

	}

	// make sure bounds is initialized in gameStage first
	public void addListener() {
		listener = new MainGameGestures(this);
		GestureDetector d = new GestureDetector(listener);
		m.clear();
		m.addProcessor(this);
		m.addProcessor(d);
		m.addProcessor(new InputAdapter() {
			@Override
			public boolean keyDown(int keycode) {
				switch (keycode) {
				case Keys.UP:
					if (!player.isDead()) {
						player.setGas(true);
					}
					break;
				case Keys.DOWN:
					if (!player.isDead()) {
						player.setBrake(true);
					}
					break;
				case Keys.RIGHT:
					if (!player.isDead()) {
						player.setSpinRight(true);
					}
					break;
				case Keys.LEFT:
					if (!player.isDead()) {
						player.setSpinLeft(true);
					}
					break;
				case Keys.PAGE_UP:
					String nextLevel = LevelScreen.stringPack + String.valueOf(LevelScreen.intLevel + 1);
					if (game.loader.nestedLevelProfile.get(nextLevel) == null) {
						// move to next level pack
						System.out.println("next level pack");
						LevelScreen.stringPack = LevelScreen.stringPack.next();
						LevelScreen.intLevel = 1;
						LevelScreen.currentLevel = LevelScreen.stringPack + String.valueOf(LevelScreen.intLevel);
						game.loader.setLevelData(LevelScreen.currentLevel, "unlocked", true);
					}

					else {
						System.out.println("next level");
						LevelScreen.intLevel++;
						LevelScreen.currentLevel = LevelScreen.stringPack + String.valueOf(LevelScreen.intLevel);
						game.loader.setLevelData(LevelScreen.currentLevel, "unlocked", true);
					}
					((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen());

					break;
				case Keys.PAGE_DOWN:
					if ((LevelScreen.currentLevel.equals("a1")) == false) {
						String prevLevel = LevelScreen.stringPack + String.valueOf(LevelScreen.intLevel - 1);
						if (game.loader.nestedLevelProfile.get(prevLevel) == null) {
							// move to next level pack
							System.out.println("prev level pack");
							LevelScreen.stringPack = LevelScreen.stringPack.previous();
							// should be # levels in pack, not necisarilly 6
							LevelScreen.intLevel = 6;
							LevelScreen.currentLevel = LevelScreen.stringPack + String.valueOf(LevelScreen.intLevel);
							game.loader.setLevelData(LevelScreen.currentLevel, "unlocked", true);
						}

						else {
							System.out.println("prev level");
							LevelScreen.intLevel--;
							LevelScreen.currentLevel = LevelScreen.stringPack + String.valueOf(LevelScreen.intLevel);
							game.loader.setLevelData(LevelScreen.currentLevel, "unlocked", true);
						}
						((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen());
					}
					break;

				case Keys.F:
					gameStage.fadeOut = true;
					break;
				case Keys.G:
					gameStage.setWinState(true);
					gameStage.levelComplete();
					game.manager.get("WinOverlay", WinOverlay.class).addWinOverlay(UIStage.this);
					break;
				case Keys.X:
					game.fxManager.play("Sound/death.ogg");
					break;
				}

				return true;
			}

			@Override
			public boolean keyUp(int keycode) {
				switch (keycode) {
				case Keys.UP:
					player.setGas(false);
					break;
				case Keys.DOWN:
					player.setBrake(false);
					break;
				case Keys.RIGHT:
					player.setSpinRight(false);
					break;
				case Keys.LEFT:
					player.setSpinLeft(false);
					break;
				case Keys.ENTER:
					game.loader.darkActive = false;
					((Game) Gdx.app.getApplicationListener())
							.setScreen(new GameScreen(gameStage.getEquationsFromAxes()));
					break;
				case Keys.ESCAPE:
					((Game) Gdx.app.getApplicationListener()).setScreen(new MenuScreen(game));
					break;

				}
				return true;
			}

			@Override
			public boolean scrolled(int amount) {
				if (cameraMode.equals("preGame")) {
					camera.zoom += (amount * 0.1f);
					if (camera.zoom < 0.9f) {
						camera.zoom = 0.9f;
					} else if (camera.zoom > 1.9f) {
						camera.zoom = 1.9f;
					}
					camera.update();
				}
				return true;

			}
		});
	}

	public boolean getFling() {
		// TODO Auto-generated method stub
		return fling;
	}

	public void setFling(boolean fling) {
		// TODO Auto-generated method stub
		this.fling = fling;
	}
}
