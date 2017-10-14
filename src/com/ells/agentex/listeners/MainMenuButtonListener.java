package com.ells.agentex.listeners;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ells.agentex.AgentExGame;
import com.ells.agentex.screens.MenuScreen;
import com.ells.agentex.stages.NewGameStage;

public class MainMenuButtonListener extends ClickListener{
	NewGameStage stage;
	Table table;
	AgentExGame game;
	public MainMenuButtonListener(NewGameStage s, Table t, AgentExGame game) {
		this.stage = s;
		this.table = t;
		this.game = game;
	}
	@Override
	public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
		table.remove();
		stage.getUiStage().addAction(sequence(fadeOut(0.01f), run(new Runnable() {

			@Override
			public void run() {
				((Game) Gdx.app.getApplicationListener()).setScreen(new MenuScreen(game));
				game.loader.darkActive = false;
			}
		})));
}
}
