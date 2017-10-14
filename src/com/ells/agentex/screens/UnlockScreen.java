package com.ells.agentex.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.ells.agentex.AgentExGame;
import com.ells.agentex.tables.CoinCounter;
import com.ells.agentex.tables.Vehicles;

public class UnlockScreen implements Screen {

	private Stage stage;
	private Skin skin;
	private Table table;
	private float startX, endX;
	private int relPos;
	private TextButton back;
	private CoinCounter coinCounter;
	private Array<Vehicles> packs;
	private float DISTBETWEEN;
	public static boolean panning = false;
	private float flingTime;
	private float flingVel;
	private float coinStartX;
	private float backStartX;
	

	private AgentExGame game;

	public UnlockScreen(AgentExGame game) {
		this.game = game;

	}

	@Override
	public void render(float delta) {
		/*for (Vehicles p : packs) {
			if (!p.isImageLoaded() && p.getX() < Gdx.graphics.getWidth() * 2) {
				System.out.println("LOADING IMAGE");
				p.loadImage();
			}
		}*/
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.background.render(delta);
		//System.out.println("position: " + stage.getViewport().getCamera().position.x);
		if (flingVel != 0) {
			flingCamera(flingVel, delta);
		}
		stage.act(delta);
		stage.draw();
		//tweenManager.update(delta);
	}

	@Override
	public void resize(int width, int height) {
		// stage.setViewport(width, height, false);
		// table.invalidateHierarchy();
		// nameSet.invalidateHierarchy();
	}

	@Override
	public void show() {
		game.manager.load("img/backStats.png", Texture.class);
		game.manager.load("img/frontStats.png", Texture.class);
		game.manager.finishLoading();
		skin = game.loader.skin;
		stage = new Stage();
		// Gdx.input.setInputProcessor(stage);
		//tweenManager = new TweenManager();
		//Tween.registerAccessor(Actor.class, new ActorAccessor());

		packs = new Array<Vehicles>();
		relPos = (Integer) game.loader.getProfile().get("selectedVehicle");

		back = new TextButton("back", skin, "backStyle");
		back.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				// game.background.boost(new Vector2(-3, 0), 1f);
				stage.addAction(new SequenceAction(Actions.alpha(0, 0.5f), run(new Runnable() {

					@Override
					public void run() {

						game.setScreen(new MenuScreen(game));
					}
				})));
			}
		});
		back.setPosition(20, 0);
		backStartX = back.getX();
		//stage.addActor(back);
		InputMultiplexer m = new InputMultiplexer();
		m.addProcessor(new GestureDetector(new GestureListener() {

			@Override
			public boolean touchDown(float x, float y, int pointer, int button) {
				startX = x;
				return false;
			}

			@Override
			public boolean tap(float x, float y, int count, int button) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean longPress(float x, float y) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean fling(float velocityX, float velocityY, int button) {
				/*if (stage.getViewport().getCamera().position.x < packs.first().getX() + velocityX / 4 && velocityX > 0) {
					return false;
				}
				if (stage.getViewport().getCamera().position.x > packs.get(11).getX() + velocityX / 4 && velocityX < 0) {
					return false;
				}*/
				
				flingVel = velocityX / 4;
				/*for (Actor a : packs) {
					a.clearActions();
					a.addAction(Actions.moveBy(velocityX / 3, 0, 0.5f));
				}*/
				//game.background.boost(-velocityX / 1000, 0.5f);
				return false;
			}

			@Override
			public boolean pan(float x, float y, float deltaX, float deltaY) {
				panning = true;
				if (stage.getViewport().getCamera().position.x <= packs.first().getX() && deltaX > 0) {
					return false;
				}
				if (stage.getViewport().getCamera().position.x >= packs.get(11).getX() && deltaX < 0) {
					return false;
				}
				stage.getViewport().getCamera().translate(-deltaX, 0, 0);
				
				//back.moveBy(-deltaX, 0);
				//coinCounter.moveBy(-deltaX, 0);
				
				stage.getActors().get(12).moveBy(-deltaX, 0);
				stage.getActors().get(stage.getActors().size - 1).moveBy(-deltaX, 0);
				
				/*for (Actor a : packs) {
					a.addAction(Actions.moveBy(deltaX, 0));
				}*/
				//game.background.boost(-deltaX / 6f, 0.05f);
				return false;
			}

			@Override
			public boolean panStop(float x, float y, int pointer, int button) {
				float delay = 0.1f; // seconds
				Timer.schedule(new Task() {
					@Override
					public void run() {
						panning = false;
					}
				}, delay);
				return false;
			}

			@Override
			public boolean zoom(float initialDistance, float distance) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
				// TODO Auto-generated method stub
				return false;
			}
		}));
		for (int v = 1; v < 13; v++) {
			packs.add(new Vehicles(this.game, v, stage, 0, packs));
		}
		DISTBETWEEN = packs.get(0).getPrefWidth() + 75;
		for (Actor a : packs) {
			stage.addActor(a);
		}
		stage.addActor(back);
		m.addProcessor(stage);
		Gdx.input.setInputProcessor(m);

		// calls menu table
		coinCounter = new CoinCounter(this.game);
		coinStartX = coinCounter.getX();
		stage.addActor(coinCounter);

		// creating animations
		//tweenManager.update(Gdx.graphics.getDeltaTime());
	}
	
	public void flingCamera(float vel, float delta) {
		if (vel < 0) {
			if (stage.getViewport().getCamera().position.x - vel/10 > packs.get(11).getX()){
				stage.getViewport().getCamera().position.x = packs.get(11).getX();
				//move coin counter and back button
				stage.getActors().get(12).setX(stage.getViewport().getCamera().position.x - Gdx.graphics.getWidth()/2 + 20);
				stage.getActors().get(stage.getActors().size - 1).setX(stage.getViewport().getCamera().position.x + Gdx.graphics.getWidth()/2 - coinCounter.expWidth - 50);
				vel = 0;
			}
			else {
				stage.getViewport().getCamera().translate(-vel/10, 0, 0);
				stage.getActors().get(12).moveBy(-vel/10, 0);
				stage.getActors().get(stage.getActors().size - 1).moveBy(-vel/10, 0);
				vel += delta*2000;
			}
			
			if (vel > 0) {
				vel = 0;
			}
			this.flingVel = vel;
		}
		if (vel > 0) {
			if (stage.getViewport().getCamera().position.x - vel/10 < packs.first().getX()){
				stage.getViewport().getCamera().position.x = packs.first().getX();
				//move coin counter and back button
				stage.getActors().get(12).setX(stage.getViewport().getCamera().position.x - Gdx.graphics.getWidth()/2 + 20);
				stage.getActors().get(stage.getActors().size - 1).setX(stage.getViewport().getCamera().position.x + Gdx.graphics.getWidth()/2 - coinCounter.expWidth - 50);
				vel = 0;
			}
			else {
				stage.getViewport().getCamera().translate(-vel/10, 0, 0);
				stage.getActors().get(12).moveBy(-vel/10, 0);
				stage.getActors().get(stage.getActors().size - 1).moveBy(-vel/10, 0);
				vel -= delta*2000;
			}
			if (vel < 0) {
				vel = 0;
			}
			this.flingVel = vel;
		}
		
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		stage.dispose();
		// skin.dispose();
	}

	public static boolean getPanning() {
		return panning;
	}

	public void setPanning(Boolean bool) {
		this.panning = bool;
	}

}