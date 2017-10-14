package com.ells.agentex.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.ells.agentex.AgentExGame;
import com.ells.agentex.actors.Equation;
import com.ells.agentex.stages.NewGameStage;
import com.ells.agentex.stages.UIStage;
import com.ells.agentex.utils.overlays.DarkOverlay;

public class GameScreen implements Screen {

	// private GameStage stage;
	private NewGameStage stage;
	private UIStage uiStage;
	private boolean firstTime = true;

	public NewGameStage getStage() {
		return stage;
	}

	public void setStage(NewGameStage stage) {
		this.stage = stage;
	}

	public UIStage getUiStage() {
		return uiStage;
	}

	public void setUiStage(UIStage uiStage) {
		this.uiStage = uiStage;
	}

	private SpriteBatch mBatch;
	private DarkOverlay dark;

	public GameScreen() {
		mBatch = new SpriteBatch();
		mBatch.enableBlending();
		dark = ((AgentExGame) Gdx.app.getApplicationListener()).manager.get("DarkOverlay", DarkOverlay.class);
		stage = new NewGameStage(true, 0);
		/**
		 * if(firstTime) { stage = new GameStage(game); firstTime =false; }
		 * stage.newScene(new Array<Equation>()); uiStage = stage.getUIStage();
		 **/
	}

	public GameScreen(Array<Equation> equation) {
		mBatch = new SpriteBatch();
		mBatch.enableBlending();
		dark = ((AgentExGame) Gdx.app.getApplicationListener()).manager.get("DarkOverlay", DarkOverlay.class);
		stage = new NewGameStage(true, 0, equation);
		/**
		 * if(firstTime) { stage = new GameStage(game); firstTime = false; }
		 * stage.newScene(equation); uiStage = stage.getUIStage();
		 **/
	}

	public GameScreen(Array<Equation> equation, AgentExGame game, Boolean goNow) {
		mBatch = new SpriteBatch();
		mBatch.enableBlending();
		dark = game.manager.get("DarkOverlay", DarkOverlay.class);
		stage = new NewGameStage(true, 0, equation, goNow);

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		// Clear the screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		/**
		 * //Update the stage stage.draw(); uiStage.draw(); stage.act(delta);
		 * uiStage.act();
		 **/
		stage.act(delta);
		if (uiStage == null) {
			if (stage.getmState() == NewGameStage.GAME_STATE.RUNNING) {
				uiStage = stage.getUiStage();
			}
		} else {
			uiStage.updatePregameUI();
			if (stage.getGame().loader.darkActive) {
				mBatch.begin();
				dark.draw(mBatch);
				mBatch.end();
			}
			uiStage.act(delta);
			uiStage.draw();

			// uiStage.updatePregameUI();
			// uiStage.render(delta);
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
		// might be unnecisary
		System.out.println("starting dispose");
		mBatch.dispose();
		stage.dispose();
		// stage.dispose();

	}
}
