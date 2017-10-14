package com.ells.agentex.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.ells.agentex.AgentExGame;
import com.ells.agentex.sound.MusicManager.MUSICSTATE;
import com.ells.agentex.tables.CoinCounter;
import com.ells.agentex.tables.OptionsOverlay;
import com.ells.agentex.utils.loading.AbstractScreen;

public class MenuScreen extends AbstractScreen {

	private Stage stage;
	private Skin skin;
	private Table table;
	private Table optionsTable;
	private SpriteBatch mBatch;
	Array<Sprite> textures;
	private OptionsOverlay optionsOverlay;
	private Texture texture;
	private Image image;
	private float screenWidth = Gdx.graphics.getWidth();
	private float screenHeight = Gdx.graphics.getHeight();

	public MenuScreen(AgentExGame game) {
		super(game);
	}

	@Override
	public void render(float delta) {
		 Gdx.gl.glClearColor(0, 0, 0, 1);
		 Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.background.render(delta);
		if (mBatch != null) {
			mBatch.begin();
			//optionsOverlay.update();
			// mBatch.draw(texture, screenWidth - texture.getWidth(), 0);
			mBatch.end();
		}
		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// stage.setViewport(width, height, false);
		table.invalidateHierarchy();
		optionsTable.invalidateHierarchy();
	}

	@Override
	public void show() {
		stage = new Stage();
		mBatch = new SpriteBatch();
		
		//game.musicManager.play("Sound/Game-Menu_Looping.mp3");
		game.musicManager.changeTrack(MUSICSTATE.MENU);
		
		InputMultiplexer m = new InputMultiplexer();
		m.addProcessor(stage);
		/*m.addProcessor(new InputAdapter() {
			@Override
			public boolean keyDown(int keycode) {
				switch (keycode) {
				case Keys.A:
					Array<String> b = new Array<String>();
					b.add("img/cutscene1.jpg");
					b.add("img/cutscene2.jpg");
					((AgentExGame) Gdx.app.getApplicationListener()).setScreen(new CutsceneScreen(game, b));
					break;
				}
				return true;

			}
		});*/
		Gdx.input.setInputProcessor(m);

		// add coin counter
		CoinCounter bitcoins = new CoinCounter(game);
		stage.addActor(bitcoins);
		skin = game.loader.skin;

		table = new Table(skin);
		optionsTable = new Table(skin);

		// agent x silhouette
		texture = new Texture(Gdx.files.internal("img/silhouette.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		image = new Image(texture);
		Table imageTable = new Table();
		//imageTable.debug();
		imageTable.add(image);
		float ratio = Gdx.graphics.getHeight()*0.8f/imageTable.getPrefHeight();
		float he = ratio*image.getHeight();
		float wi = image.getWidth()*ratio;
		imageTable.setPosition(Gdx.graphics.getWidth()-wi/2, he/2);
		imageTable.add(image).width(wi).height(he);
		stage.addActor(imageTable);
		// creating heading
		List<String> greetings = Arrays.asList("Good to see you,", "You're still alive?",
				"Back from the dead?", "We've been waiting,", "We've got a mission,", "I hope you're ready,",
				"The world needs you,");
		Random random = new Random();
		int buttonPadding = Gdx.graphics.getHeight()/120;
		String greeting = greetings.get(random.nextInt(greetings.size()));
		Label titleLabel = new Label(greeting, skin, "titleStyle");
		Label nameLabel = new Label("" + game.loader.getProfile().get("name"), skin, "giantStyle");
		// titleLabel.setPosition(screenWidth / 8, screenHeight * 7 / 8 -
		// titleLabel.getHeight());

		// creating buttons
		TextButton buttonPlay = new TextButton("PLAY      ", skin, "menuButtonStyle");
		buttonPlay.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				// game.background.boost(new Vector2(3, 0), 1f);
				stage.addAction(sequence(Actions.alpha(0, 0.5f), run(new Runnable() {

					@Override
					public void run() {
						((AgentExGame) Gdx.app.getApplicationListener()).setScreen(new LevelScreen(game));
					}
				})));
			}
		});
		buttonPlay.pad(buttonPadding);

		TextButton unlocks = new TextButton("UPGRADES", skin, "menuButtonStyle");
		unlocks.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				// game.background.boost(new Vector2(3, 0), 1f);
				stage.addAction(sequence(Actions.alpha(0, 0.5f), run(new Runnable() {

					@Override
					public void run() {
						((AgentExGame) Gdx.app.getApplicationListener()).setScreen(new UnlockScreen(game));
					}
				})));
			}
		});
		unlocks.pad(buttonPadding);

		TextButton buttonChangeSave = new TextButton("SAVE FILES", skin, "menuButtonStyle");
		buttonChangeSave.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				// game.background.boost(new Vector2(3, 0), 1);
				stage.addAction(sequence(Actions.alpha(0, 0.5f), run(new Runnable() {

					@Override
					public void run() {
						((AgentExGame) Gdx.app.getApplicationListener()).setScreen(new SaveScreen(game));
					}
				})));
			}
		});
		buttonChangeSave.pad(buttonPadding);

		final CheckBox soundfx = new CheckBox("", skin, "fx");
		if (game.loader.getSoundPrefs()) {
			soundfx.setChecked(true);
		} else
			soundfx.setChecked(false);
		soundfx.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.loader.setSoundPrefs(soundfx.isChecked());
				game.fxManager.setMuted(!soundfx.isChecked());
			}
		});

		final CheckBox music = new CheckBox("", skin, "music");
		if (game.loader.getMusicPrefs()) {
			music.setChecked(true);
		} else
			music.setChecked(false);
		music.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.loader.setMusicPrefs(music.isChecked());
				game.musicManager.setMuted(!music.isChecked());
				if (!game.musicManager.isMuted()) {
					System.out.println("play plz");
					//game.musicManager.changeTrack(MUSICSTATE.MENU);
					game.musicManager.play("Sound/Game-Menu_Looping.mp3");
				}
				//Toggle music mute
				
			}
		});

		ImageButton options = new ImageButton(skin, "options");
		options.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//addOptions();
			}
		});

		ImageButton information = new ImageButton(skin, "info");
		information.addCaptureListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				Array<String> b = new Array<String>();
				b.add("img/cutscene1.jpg");
				b.add("img/cutscene2.jpg");
				((AgentExGame) Gdx.app.getApplicationListener()).setScreen(new CutsceneScreen(game, b));
			}
		});

		optionsTable.addAction(Actions.alpha(0.5f));
		// optionsTable.add(information).height(Gdx.graphics.getHeight() /
		// 10).width(Gdx.graphics.getHeight() / 10);
		optionsTable.add(soundfx).height(Gdx.graphics.getHeight() / 10).width(Gdx.graphics.getHeight() / 10)
				.pad(buttonPadding).row();
		optionsTable.add(music).height(Gdx.graphics.getHeight() / 10).width(Gdx.graphics.getHeight() / 10)
				.pad(buttonPadding).row();
		//optionsTable.add(options).height(Gdx.graphics.getHeight() / 10).width(Gdx.graphics.getHeight() / 10).pad(buttonPadding);
		optionsTable.pad(20);
		optionsTable.setPosition(screenWidth - optionsTable.getPrefWidth() / 2, optionsTable.getPrefHeight() / 2);

		/*
		 * TextButton buttonChangeSave = new TextButton("CHANGE SAVE", skin,
		 * "default"); buttonChangeSave.addListener(new ClickListener() {
		 * 
		 * @Override public void clicked(InputEvent event, float x, float y) {
		 * Timeline.createParallel().beginParallel() .push(Tween.to(table,
		 * ActorAccessor.ALPHA, .75f).target(0)) .push(Tween.to(table,
		 * ActorAccessor.Y, .75f).target(table.getY() - 50) .setCallback(new
		 * TweenCallback() {
		 * 
		 * @Override public void onEvent(int type, BaseTween<?> source) {
		 * Gdx.app.exit(); } })) .end().start(tweenManager); } });
		 * buttonChangeSave.pad(15);
		 */

		// putting stuff together

		table.add(titleLabel).row();
		table.add(nameLabel).padRight(50).align(Align.right).spaceBottom(screenHeight / 15).row();
		table.add(buttonChangeSave).spaceBottom(screenHeight / 25).align(Align.left).row();
		table.add(unlocks).spaceBottom(screenHeight / 25).align(Align.left).row();
		table.add(buttonPlay).align(Align.left);
		// table.setPosition(0, 0);
		// table.add(image).row();
		float width = 0;
		float height = 0;
		for (Cell a : table.getCells()) {
			if (width < a.getActor().getWidth()) {
				width = a.getActor().getWidth();
			}
			height += a.getActor().getHeight() + buttonPadding * 2;
		}
		height += screenHeight * 11 / 75;

		table.setPosition(width / 2 + screenWidth / 20, screenHeight - height / 2);

		stage.addActor(table);
		stage.addActor(optionsTable);

		stage.addAction(Actions.alpha(0));
		stage.addAction(Actions.alpha(1, 1));

		createOptions();
	}

	protected void addOptions() {
		stage.addActor(optionsOverlay);

	}

	private void createOptions() {
		//optionsOverlay = new OptionsOverlay(skin);

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
		for(Actor a: table.getChildren()) {
			a.clear();
		}
		for(Actor a: optionsTable.getChildren()) {
			a.clear();
		}
		texture.dispose();
		stage.dispose();
		mBatch.dispose();
		mBatch = null;
	}

}