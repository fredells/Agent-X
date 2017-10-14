package com.ells.agentex.utils.overlays;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.TimeUtils;
import com.ells.agentex.AgentExGame;
import com.ells.agentex.actors.Player;
import com.ells.agentex.screens.GameScreen;
import com.ells.agentex.stages.UIStage;

public class InGameOverlay {
	private ImageButton downButton, upButton, leftButton, rightButton, restart, pause, tutorial;
	private Label timerLabel;
	private AgentExGame game;
	private Table top, bottom;
	private Boolean isTutorial = false;
	public InGameOverlay(final float screenWidth, final float screenHeight, final Skin skin, AgentExGame game) {
		this.game = game;
		top = new Table();
		bottom = new Table();
		top.addAction(Actions.alpha(0.6f));
		bottom.addAction(Actions.alpha(0.5f));
		float BUTTON_SIZE = Gdx.graphics.getWidth()/9;
		
		//new images
		Texture texture = new Texture(Gdx.files.internal("img/backward.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		leftButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(texture)));
		texture = new Texture(Gdx.files.internal("img/forward.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		rightButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(texture)));
		texture = new Texture(Gdx.files.internal("img/gas.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		upButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(texture)));
		texture = new Texture(Gdx.files.internal("img/brake.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		downButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(texture)));
		
		TextureRegion reTexture = game.loader.uiSkinAtlas.findRegion("restart");
		restart = new ImageButton(new TextureRegionDrawable(reTexture));
		TextureRegion opTexture = game.loader.uiSkinAtlas.findRegion("pause");
		pause = new ImageButton(new TextureRegionDrawable(opTexture));
		float TOPBUTTONSIZE = Gdx.graphics.getWidth()/10;
		top.add(restart).pad(20).width(TOPBUTTONSIZE).height(TOPBUTTONSIZE);
		/*if (isTutorial == true) {
			top.add(tutorial);
		}*/
		top.add(pause).pad(20).spaceLeft(Gdx.graphics.getWidth()-top.getCells().get(0).getPrefWidth()*2 - 60).width(TOPBUTTONSIZE).height(TOPBUTTONSIZE);
		top.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()-top.getPrefHeight()/2);
		//top.debug();
		
		//bottom.debug();
		bottom.add(downButton).pad(15).width(BUTTON_SIZE).height(BUTTON_SIZE).spaceRight(30);
		bottom.add(upButton).pad(15).width(BUTTON_SIZE).height(BUTTON_SIZE).fill();
		bottom.add(leftButton).pad(15).width(BUTTON_SIZE).height(BUTTON_SIZE).spaceRight(30).spaceLeft(Gdx.graphics.getWidth()-180-bottom.getCells().get(1).getPrefWidth()*4).fill();
		bottom.add(rightButton).pad(15).width(BUTTON_SIZE).height(BUTTON_SIZE).fill();
		bottom.setPosition(Gdx.graphics.getWidth()/2, bottom.getPrefHeight()/2);

		//ratios of button weird, need to remake some arrows with square image
		/*downButton = new ImageButton(skin, "downButton");
		upButton = new ImageButton(skin, "upButton");
		rightButton = new ImageButton(skin, "rightButton");
		TextureRegion leTexture = game.loader.uiSkinAtlas.findRegion("leftArrow");
		leftButton = new ImageButton(new TextureRegionDrawable(leTexture));
		TextureRegion reTexture = game.loader.uiSkinAtlas.findRegion("restart");
		restart = new ImageButton(new TextureRegionDrawable(reTexture));
		//pause = new ImageButton(new TextureRegionDrawable(game.manager.get("data/uiskin.atlas", TextureAtlas.class).findRegion("pause")));
		//pause.setBounds(screenWidth*0.89f, screenHeight*0.89f, screenWidth*0.1f, screenHeight*0.1f);
		TextureRegion opTexture = game.loader.uiSkinAtlas.findRegion("pause");
		pause = new ImageButton(new TextureRegionDrawable(opTexture));
		float TOPBUTTONSIZE = Gdx.graphics.getWidth()/25;
		top.add(restart).width(TOPBUTTONSIZE).height(TOPBUTTONSIZE);
		top.add(pause).spaceLeft(Gdx.graphics.getWidth()-top.getCells().get(0).getPrefWidth()*2).width(TOPBUTTONSIZE).height(TOPBUTTONSIZE);
		top.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()-top.getPrefHeight()/2);
		top.debug();
		float BUTTON_SIZE = Gdx.graphics.getWidth()/10;
		bottom.debug();
		bottom.add(upButton).width(BUTTON_SIZE).height(BUTTON_SIZE).row();
		bottom.add(downButton).width(BUTTON_SIZE).height(BUTTON_SIZE).fill();
		bottom.add(leftButton).width(BUTTON_SIZE).height(BUTTON_SIZE).spaceLeft(Gdx.graphics.getWidth()-bottom.getCells().get(1).getPrefWidth()*3).fill();
		bottom.add(rightButton).width(BUTTON_SIZE).height(BUTTON_SIZE).fill();
		bottom.setPosition(Gdx.graphics.getWidth()/2, bottom.getPrefHeight()/2);*/

	}
	public void addToStage(UIStage stage) {
		stage.gameMode = "playing";
		stage.gameStage.startTime = TimeUtils.millis();
		stage.addActor(top);
		stage.addActor(bottom);
		
	}
	public void addThingsThatNeedStage(final UIStage stage, final Player player, Label timerLabel) {
		this.timerLabel = timerLabel;
		pause.clearListeners();
		downButton.clearListeners();
		upButton.clearListeners();
		rightButton.clearListeners();
		leftButton.clearListeners();
		restart.clearListeners();
		pause.addListener(new ClickListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;

		}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
						game.musicManager.pause();
						game.loader.darkActive = true;
						game.manager.get("PauseOverlay", PauseOverlay.class).updatePause(stage.gameMode);
						stage.gameStage.setGameMode("pause");
						game.manager.get("PauseOverlay", PauseOverlay.class).addToStage(stage);
						disableButtons();
				}
		});
		downButton.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				player.setBrake(true);
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				player.setBrake(false);
			}

		});
		rightButton.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				player.setSpinRight(true);
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				player.setSpinRight(false);
			}

		});
		leftButton.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				player.setSpinLeft(true);
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				player.setSpinLeft(false);
			}

		});
		upButton.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				player.setGas(true);
				return true;
			}
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				player.setGas(false);
			}

		});
		restart.addListener(new ClickListener() {
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(stage.gameStage.getEquationsFromAxes()));
			}
		});
		timerLabel.setSize(game.loader.screenWidth*0.1f, game.loader.screenHeight*0.05f);
		timerLabel.setPosition(game.loader.screenWidth*0.5f-timerLabel.getWidth()/2, game.loader.screenHeight-timerLabel.getHeight());
	}
	public void disableButtons() {
		downButton.setTouchable(Touchable.disabled);
		upButton.setTouchable(Touchable.disabled);
		leftButton.setTouchable(Touchable.disabled);
		rightButton.setTouchable(Touchable.disabled);
		restart.setTouchable(Touchable.disabled);
		pause.setTouchable(Touchable.disabled);
		downButton.setVisible(false);
		upButton.setVisible(false);
		leftButton.setVisible(false);
		rightButton.setVisible(false);
		restart.setVisible(false);
		pause.setVisible(false);
		/*try {
        	tutorial.setTouchable(Touchable.disabled);
        } catch (NullPointerException e) {
            //System.out.print("Caught the NullPointerException");
        }*/
		//tutorial.setTouchable(Touchable.disabled);
	}
	public void enableButtons() {
			downButton.setTouchable(Touchable.enabled);
			upButton.setTouchable(Touchable.enabled);
			leftButton.setTouchable(Touchable.enabled);
			rightButton.setTouchable(Touchable.enabled);
			restart.setTouchable(Touchable.enabled);
			pause.setTouchable(Touchable.enabled);
			downButton.setVisible(true);
			upButton.setVisible(true);
			leftButton.setVisible(true);
			rightButton.setVisible(true);
			restart.setVisible(true);
			pause.setVisible(true);
			//tutorial.setTouchable(Touchable.enabled);
	}
	public void removeOverlay() {
		top.remove();
		bottom.remove();
		//tutorial.remove();
	}
	
}
