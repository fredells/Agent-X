package com.ells.agentex.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.ells.agentex.AgentExGame;
import com.ells.agentex.sound.MusicManager.MUSICSTATE;
import com.ells.agentex.tables.LevelPack;
import com.ells.agentex.tables.LevelSelect;

public class LevelScreen implements Screen {

	// how bad are public static vars?
	public static Stage stage;
	private Skin skin;
	public static String currentLevel = "";
	// public static String stringPack = "";
	public static Integer intLevel = 0;
	private float startX, endX;
	public boolean packShown = false;
	private static TextButton back;
	public static boolean panning = false;

	public static enum LEVEL_PACK_LETTER {
		a, b, c, d, e, f, g;
		private static LEVEL_PACK_LETTER[] vals = values();

		public LEVEL_PACK_LETTER next() {
			if ((this.ordinal() + 1) % vals.length < vals.length) {
				return vals[(this.ordinal() + 1) % vals.length];
			}
			return null;
		}

		public LEVEL_PACK_LETTER previous() {
			if ((this.ordinal() - 1) % vals.length < 0) {
				return vals[(this.ordinal() - 1) % vals.length];
			}
			return null;
		}

		public int length() {
			return this.vals.length;
		}

		public int value() {
			return LEVEL_PACK_LETTER.valueOf(this.toString()).ordinal();
		}
	};

	public static LEVEL_PACK_LETTER stringPack = LEVEL_PACK_LETTER.a;

	private String[] keys = { "a", "b", "c", "d", "e", "f", "g" };

	private AgentExGame game;
	
	Texture bg;
	private static boolean packTable = false;
	private Array<LevelPack> packs = new Array<LevelPack>();

	public LevelScreen(AgentExGame game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {
		GL20 gl = Gdx.graphics.getGL20();
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.background.render(delta);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		/**
		 * if(stage.getActors().first().getX() > 0) {
		 * stage.addAction(Actions.moveTo(0, 0)); }
		 **/

	}

	@Override
	public void resize(int width, int height) {
		// stage.setViewport(width, height, false);
		// table.invalidateHierarchy();
		// nameSet.invalidateHierarchy();
	}

	@Override
	public void show() {
		stage = new Stage();
		packTable = false;
		//Tween.registerAccessor(Actor.class, new ActorAccessor());
		stage = new Stage();
		//backStyle.font = game.loader.fonts.get("tableFont");
		//backStyle.downFontColor = Color.RED;
		//back = new TextButton("back", backStyle);
		skin = game.loader.skin;
		back = new TextButton("back", skin, "backStyle");
		back.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if (!packShown) {
					//game.background.boost(new Vector2(-3f, 0), 1f);
					stage.addAction(new SequenceAction(Actions.alpha(0, 0.5f), run(new Runnable() {
						@Override
						public void run() {
							game.setScreen(new MenuScreen(game));
							
						}
					})));
				} 
				
				else {
					stage.addAction(new SequenceAction(Actions.alpha(0, 0.5f), run(new Runnable() {
						@Override
						public void run() {
							//stage.dispose();
							game.setScreen(new LevelScreen(game));
							
							//game.setScreen(new LevelScreen(game));
						}
					})));
				}
			}
		});
		back.setPosition(20, 0);
		stage.addActor(back);
		//packs.add(new LevelPack(stage, LEVEL_PACK_LETTER.a, game, this));
		//System.out.println("LEVEL PACK LETTER: " + LEVEL_PACK_LETTER.b.toString());
		packs.add(new LevelPack(stage, LEVEL_PACK_LETTER.a, game, this));
		packs.add(new LevelPack(stage, LEVEL_PACK_LETTER.b, game, this));
		packs.add(new LevelPack(stage, LEVEL_PACK_LETTER.c, game, this));
		packs.add(new LevelPack(stage, LEVEL_PACK_LETTER.d, game, this));
		packs.add(new LevelPack(stage, LEVEL_PACK_LETTER.e, game, this));
		packs.add(new LevelPack(stage, LEVEL_PACK_LETTER.f, game, this));
		packs.add(new LevelPack(stage, LEVEL_PACK_LETTER.g, game, this));

		final int DISTBETWEEN = 350;

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
				if (packs.first().getX()+ velocityX/5 > Gdx.graphics.getWidth() / 4 && velocityX > 0) {
					return false;
				}
				if (packs.peek().getX()+velocityX/5 < Gdx.graphics.getWidth() / 2 && velocityX < 0) {
					return false;
				}
				for (Actor a : packs) {
					a.clearActions();
					a.addAction(Actions.moveBy(velocityX / 5, 0, 0.5f, Interpolation.exp10Out));
				}
				//game.background.boost(-velocityX / 1000, 0.5f);
				return false;
			}

			@Override
			public boolean pan(float x, float y, float deltaX, float deltaY) {
				//System.out.println("PANNING");
				panning = true;
				if (packs.first().getX() > Gdx.graphics.getWidth() / 4 && deltaX > 0) {
					return false;
				}
				if (packs.peek().getX() < Gdx.graphics.getWidth() / 2 && deltaX < 0) {
					return false;
				}
				for (Actor a : packs) {
					a.addAction(Actions.moveBy(deltaX, 0));
				}
				//game.background.boost(-deltaX / 6f, 0.05f);
				return false;
			}

			@Override
			public boolean panStop(float x, float y, int pointer, int button) {
				float delay = 0.1f; // seconds
				Timer.schedule(new Task(){
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
		for (Actor a : packs) {
			stage.addActor(a);
		}
		m.addProcessor(stage);
		Gdx.input.setInputProcessor(m);
		// InputMultiplexer m = new InputMultiplexer();
		// m.addProcessor(new GestureDetector(new MenuGestures(progress, stage,
		// tweenManager, game)));
		// m.addProcessor(stage);
		// Gdx.input.setInputProcessor(m);
		// calls menu table
		// chooseLevelPack();
		//bg = new Texture(Gdx.files.internal("img/bg.png"));
		// creating animations
		
		stage.addAction(Actions.alpha(0));
		stage.addAction(Actions.alpha(1, 1));
		stage.act();
		
		game.musicManager.changeTrack(MUSICSTATE.MENU);
	}

	/**
	 * private void chooseLevelPack() { // Build Level Menu // add level pack
	 * tables to stage and Tween in final LevelPack levelPack1 = new
	 * LevelPack(stage, LevelScreen.LEVEL_PACK_LETTER.a, Gdx.graphics.getWidth()
	 * / 2, game, true); stage.addActor(levelPack1); tables.add(levelPack1);
	 * levelPack1.addAction(Actions.scaleTo((float) (1f), (float) (1f), 0.2f));
	 * Tween.from(levelPack1, ActorAccessor.ALPHA,
	 * .75f).target(0).start(tweenManager); n = Tween.from(levelPack1,
	 * ActorAccessor.Y, .75f).target(Gdx.graphics.getHeight() /
	 * 8).start(tweenManager); levelPack1.addListener(new ClickListener() {
	 * 
	 * @Override public void clicked(InputEvent event, float x, float y) { if
	 *           (levelPack1.unlocked) { LevelScreen.stringPack =
	 *           levelPack1.levelSet; Array<LevelPack> tables =
	 *           LevelScreen.tables; for (int i = 0; i < tables.size; i++) { //
	 *           tables.get(i).addAction(moveTo(tables.get(i).getX(), //
	 *           -stage.getHeight(), .5f));
	 *           tables.get(i).addAction(Actions.alpha(0, 0.5f)); }
	 * 
	 *           stage.addAction(sequence(Actions.delay(0.75f), run(new
	 *           Runnable() {
	 * 
	 * @Override public void run() {
	 *           LevelScreen.levelSelectMenu(levelPack1.levelSet, game);
	 *           System.out.print(levelPack1.levelSet); } }))); } } }); final
	 *           LevelPack levelPack2 = new LevelPack(stage,
	 *           LEVEL_PACK_LETTER.b, Gdx.graphics.getWidth() * 0.75f, game,
	 *           true); levelPack2.addListener(new ClickListener() {
	 * 
	 * @Override public void clicked(InputEvent event, float x, float y) { if
	 *           (levelPack2.unlocked) { LevelScreen.stringPack =
	 *           levelPack2.levelSet; Array<LevelPack> tables =
	 *           LevelScreen.tables; for (int i = 0; i < tables.size; i++) { //
	 *           tables.get(i).addAction(moveTo(tables.get(i).getX(), //
	 *           -stage.getHeight(), .5f));
	 *           tables.get(i).addAction(Actions.alpha(0, 0.5f)); }
	 * 
	 *           stage.addAction(sequence(Actions.delay(0.75f), run(new
	 *           Runnable() {
	 * 
	 * @Override public void run() {
	 *           LevelScreen.levelSelectMenu(levelPack2.levelSet, game);
	 *           System.out.print(levelPack2.levelSet); } }))); } } });
	 *           stage.addActor(levelPack2); tables.add(levelPack2);
	 *           levelPack2.addAction(Actions.scaleTo((float) (0.5f), (float)
	 *           (0.5f), 0.2f)); Tween.from(levelPack2, ActorAccessor.ALPHA,
	 *           .75f).target(0).start(tweenManager); Tween.from(levelPack2,
	 *           ActorAccessor.Y, .75f).target(Gdx.graphics.getHeight() /
	 *           8).start(tweenManager); final LevelPack levelPack3 = new
	 *           LevelPack(stage, LEVEL_PACK_LETTER.b, Gdx.graphics.getWidth() *
	 *           1.25f, game, true); stage.addActor(levelPack3);
	 *           tables.add(levelPack3);
	 *           levelPack3.addAction(Actions.scaleTo((float) (0.5f), (float)
	 *           (0.5f), 0.2f)); Tween.from(levelPack3, ActorAccessor.ALPHA,
	 *           .75f).target(0).start(tweenManager); Tween.from(levelPack3,
	 *           ActorAccessor.Y, .75f).target(Gdx.graphics.getHeight() /
	 *           8).start(tweenManager); levelPack3.addListener(new
	 *           ClickListener() {
	 * 
	 * @Override public void clicked(InputEvent event, float x, float y) { if
	 *           (levelPack3.unlocked) { LevelScreen.stringPack =
	 *           levelPack3.levelSet; Array<LevelPack> tables =
	 *           LevelScreen.tables; for (int i = 0; i < tables.size; i++) { //
	 *           tables.get(i).addAction(moveTo(tables.get(i).getX(), //
	 *           -stage.getHeight(), .5f));
	 *           tables.get(i).addAction(Actions.alpha(0, 0.5f)); }
	 * 
	 *           stage.addAction(sequence(Actions.delay(0.75f), run(new
	 *           Runnable() {
	 * 
	 * @Override public void run() {
	 *           LevelScreen.levelSelectMenu(levelPack3.levelSet, game);
	 *           System.out.print(levelPack3.levelSet); } }))); } } });
	 *           ImageButton back = new ImageButton(skin, "back"); float
	 *           aspectRatio = back.getWidth() / back.getHeight();
	 *           back.setSize(game.loader.screenWidth / 7 * aspectRatio,
	 *           game.loader.screenWidth / 7 * aspectRatio);
	 *           back.addListener(new ClickListener() {
	 * @Override public void touchUp(InputEvent event, float x, float y, int
	 *           pointer, int button) { ((Game)
	 *           Gdx.app.getApplicationListener()).setScreen(new
	 *           MenuScreen(game)); // InGameOverlay.enablebuttons(); }
	 * 
	 *           }); Tween.from(back, ActorAccessor.ALPHA,
	 *           .75f).target(0).start(tweenManager); stage.addActor(back);
	 *           numberOfPacks = tables.size;
	 * 
	 *           }
	 **/
	//this isnt working
	public static void levelSelectMenu(LEVEL_PACK_LETTER levelset, AgentExGame game) {
		System.out.println(levelset.toString());
		LevelSelect levelSelect1 = new LevelSelect(stage, levelset, game);
		Gdx.input.setInputProcessor(stage);
		stage.addActor(levelSelect1);
		stage.addActor(back);
		packTable = true;

	}

	public static void setCurrentLevel(String key) {
		currentLevel = key;

	}
	
	public static boolean getPanning() {
		return panning;
	}
	public void setPanning(Boolean bool) {
		this.panning = bool;
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
		for(LevelPack p: packs) {
			p.dispose();
		}
		packs.clear();
		// skin.dispose();
	}

}