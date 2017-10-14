package com.ells.agentex.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ells.agentex.AgentExGame;
import com.ells.agentex.utils.loaders.DarkOverlayLoader;
import com.ells.agentex.utils.loaders.DarkOverlayLoader.DarkOverlayLoaderParameter;
import com.ells.agentex.utils.loaders.DeathOverlayLoader;
import com.ells.agentex.utils.loaders.DeathOverlayLoader.DeathOverlayLoaderParameter;
import com.ells.agentex.utils.loaders.InGameOverlayLoader;
import com.ells.agentex.utils.loaders.InGameOverlayLoader.InGameOverlayLoaderParameter;
import com.ells.agentex.utils.loaders.PauseOverlayLoader;
import com.ells.agentex.utils.loaders.PauseOverlayLoader.PauseOverlayLoaderParameter;
import com.ells.agentex.utils.loaders.PreGameOverlayLoader;
import com.ells.agentex.utils.loaders.PreGameOverlayLoader.PreGameOverlayLoaderParameter;
import com.ells.agentex.utils.loaders.TutorialOverlayLoader;
import com.ells.agentex.utils.loaders.TutorialOverlayLoader.TutorialOverlayLoaderParameter;
import com.ells.agentex.utils.loaders.WinOverlayLoader;
import com.ells.agentex.utils.loaders.WinOverlayLoader.WinOverlayLoaderParameter;
import com.ells.agentex.utils.loading.AbstractScreen;
import com.ells.agentex.utils.overlays.DarkOverlay;
import com.ells.agentex.utils.overlays.DeathOverlay;
import com.ells.agentex.utils.overlays.InGameOverlay;
import com.ells.agentex.utils.overlays.PauseOverlay;
import com.ells.agentex.utils.overlays.PreGameOverlay;
import com.ells.agentex.utils.overlays.TutorialOverlay;
import com.ells.agentex.utils.overlays.WinOverlay;

public class LoadingScreen extends AbstractScreen {

	private Stage stage;

	private Image logo;
	private Image loadingFrame;
	private Image loadingBarHidden;
	private Image screenBg;
	private Image loadingBg;
	private Label loadingText;

	private float startX, endX;
	private float percent;

	private Actor loadingBar;
	private String out = "";
	private boolean doOnce = true;
	private boolean saveFonts = false;
	private BitmapFont loadingFont;
	float loadTime = 0;

	public LoadingScreen(AgentExGame game) {
		super(game);
	}

	@Override
	public void show() {

		// this is just because prefs dont work on desktop well. remove in final
		// release
		Gdx.app.getPreferences("agentx").putFloat("equationTableOpacity", 0.8f);

		// Tell the manager to load assets for the loading screen
		game.manager.load("data/loading.pack", TextureAtlas.class);
		// Wait until they are finished loading
		game.manager.finishLoading();

		// Initialize the stage where we will place everything
		stage = new Stage();
		// Get our textureatlas from the manager
		TextureAtlas atlas = game.manager.get("data/loading.pack", TextureAtlas.class);

		// Grab the regions from the atlas and create some images
		logo = new Image(atlas.findRegion("libgdx-logo"));
		loadingFrame = new Image(atlas.findRegion("loading-frame"));
		loadingBarHidden = new Image(atlas.findRegion("loading-bar-hidden"));
		screenBg = new Image(atlas.findRegion("screen-bg"));
		loadingBg = new Image(atlas.findRegion("loading-frame-bg"));
		FreeTypeFontGenerator g = new FreeTypeFontGenerator(Gdx.files.internal("fonts/OpenSans-Regular.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = Gdx.graphics.getHeight() / 50;
		parameter.color = Color.TEAL;
		LabelStyle l = new LabelStyle();
		loadingFont = g.generateFont(parameter);
		l.font = loadingFont;
		loadingText = new Label("Loading initiated;", l);

		// Add the loading bar animation
		// Animation anim = new Animation(0.05f,
		// atlas.findRegions("loading-bar-anim"));
		// anim.setPlayMode(Animation.PlayMode.LOOP_REVERSED);
		// loadingBar = new LoadingBar(anim);
		// Or if you only need a static bar, you can do
		loadingBar = new Image(atlas.findRegion("loading-bar2"));
		// Add all the actors to the stage
		stage.addActor(screenBg);
		stage.addActor(loadingBar);
		stage.addActor(loadingBg);
		stage.addActor(loadingBarHidden);
		stage.addActor(loadingFrame);
		stage.addActor(logo);
		stage.addActor(loadingText);
		stage.act();
		stage.draw();
		// Add everything to be loaded, for instance:
		// game.manager.load("data/assets1.pack", TextureAtlas.class);
		// game.manager.load("data/assets2.pack", TextureAtlas.class);
		// game.manager.load("data/assets3.pack", TextureAtlas.class);
		// load skin
		game.loader.GenerateFontParameters();
		game.loader.smartFontFromFiles();
		game.manager.load("data/uiskin.atlas", TextureAtlas.class);
		Gdx.app.log("Agent X", "Atlas Loaded");
		game.manager.load("data/uiskin.json", Skin.class, new SkinLoader.SkinParameter("data/uiskin.atlas"));
		Gdx.app.log("Agent X", "UIskin Loaded");
		game.manager.load("packs/test.pack", TextureAtlas.class);

		game.manager.load("img/audioOn.png", Texture.class);
		game.manager.load("img/audioOff.png", Texture.class);

		game.manager.load("Sound/death.ogg", Sound.class);
		game.manager.load("Sound/impact.ogg", Sound.class);
		game.manager.load("Sound/click.ogg", Sound.class);
		game.manager.load("Sound/pickup.ogg", Sound.class);
		game.manager.load("Sound/flip_reward.ogg", Sound.class);
		game.manager.load("Sound/win.ogg", Sound.class);
		game.manager.load("Sound/Background #2.ogg", Music.class);
		game.manager.load("Sound/Game-Menu_Looping.mp3", Music.class);
		Gdx.app.log("Agent X", "Sounds Loaded");
		// load images
		game.manager.load("img/futuristic.png", Texture.class);
		game.manager.load("img/gear.png", Texture.class);
		game.manager.load("img/music.png", Texture.class);
		game.manager.load("img/music(mute).png", Texture.class);
		game.manager.load("img/information.png", Texture.class);
		//game.manager.load("img/imagineit.png", Texture.class);
		game.manager.load("img/locked.png", Texture.class);
		game.manager.load("img/unlocked.png", Texture.class);
		Gdx.app.log("Agent X", "Images loaded");
		if (saveFonts) {
			// game.loader.saveFontsToFile(game.manager);
		} else if (!saveFonts) {
			// game.loader.generateFontsFromFiles(game.manager);
			// game.loader.smartFontFromFiles();
		}
		FileHandleResolver resolver = new InternalFileHandleResolver();
		game.manager.setLoader(DeathOverlay.class, new DeathOverlayLoader(resolver));
		DeathOverlayLoaderParameter d = new DeathOverlayLoaderParameter();
		d.game = game;
		game.manager.load("DeathOverlay", DeathOverlay.class, d);

		game.manager.setLoader(InGameOverlay.class, new InGameOverlayLoader(resolver));
		InGameOverlayLoaderParameter i = new InGameOverlayLoaderParameter();
		i.game = game;
		game.manager.load("InGameOverlay", InGameOverlay.class, i);

		game.manager.setLoader(PauseOverlay.class, new PauseOverlayLoader(resolver));
		PauseOverlayLoaderParameter p = new PauseOverlayLoaderParameter();
		p.game = game;
		game.manager.load("PauseOverlay", PauseOverlay.class, p);

		game.manager.setLoader(PreGameOverlay.class, new PreGameOverlayLoader(resolver));
		PreGameOverlayLoaderParameter pr = new PreGameOverlayLoaderParameter();
		pr.game = game;
		game.manager.load("PreGameOverlay", PreGameOverlay.class, pr);

		game.manager.setLoader(TutorialOverlay.class, new TutorialOverlayLoader(resolver));
		TutorialOverlayLoaderParameter t = new TutorialOverlayLoaderParameter();
		t.game = game;
		game.manager.load("TutorialOverlay", TutorialOverlay.class, t);

		game.manager.setLoader(WinOverlay.class, new WinOverlayLoader(resolver));
		WinOverlayLoaderParameter w = new WinOverlayLoaderParameter();
		w.game = game;
		game.manager.load("WinOverlay", WinOverlay.class, w);

		game.manager.setLoader(DarkOverlay.class, new DarkOverlayLoader(resolver));
		DarkOverlayLoaderParameter x = new DarkOverlayLoaderParameter();
		x.game = game;
		game.manager.load("DarkOverlay", DarkOverlay.class, x);
		game.loader.load(game);

	}

	@Override
	public void resize(int width, int height) {
		// Set our screen to always be XXX x 480 in size
		// width = 480 * width / height;
		// height = 480;
		stage.getViewport().update(width, height, false);

		// Make the background fill the screen
		screenBg.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		// Place the logo in the middle of the screen and 100 px up
		logo.setX((width - logo.getWidth()) / 2);
		logo.setY((height - logo.getHeight()) / 2 + 100);

		// Place the loading frame in the middle of the screen
		loadingFrame.setX((stage.getWidth() - loadingFrame.getWidth()) / 2);
		loadingFrame.setY((stage.getHeight() - loadingFrame.getHeight()) / 2);

		// Place the loading bar at the same spot as the frame, adjusted a few
		// px
		loadingBar.setX(loadingFrame.getX() + 15);
		loadingBar.setY(loadingFrame.getY() + 5);

		// Place the image that will hide the bar on top of the bar, adjusted a
		// few px
		loadingBarHidden.setX(loadingBar.getX() + 35);
		loadingBarHidden.setY(loadingBar.getY() - 3);
		// The start position and how far to move the hidden loading bar
		startX = loadingBarHidden.getX();
		endX = 440;

		// The rest of the hidden bar
		loadingBg.setSize(450, 50);
		loadingBg.setX(loadingBarHidden.getX() + 30);
		loadingBg.setY(loadingBarHidden.getY() + 3);
	}

	@Override
	public void render(float delta) {
		loadTime += delta;
		// "parallelogramFont.ttf" just last font loaded
		// System.out.println("labels " + !game.loader.loadedTrickLabels +
		// "uiskin" + game.manager.isLoaded("data/uiskin.json") + " font" +
		// game.manager.isLoaded("generated-fonts/deathFont.fnt",
		// BitmapFont.class));
		if (!game.loader.loadedTrickLabels && game.manager.isLoaded("data/uiskin.json")) {
			game.loader.skin = game.manager.get("data/uiskin.json", Skin.class);
			game.loader.generateAfterFontsComplete();
			game.loader.generateTrickLabels();
			game.loader.loadedTrickLabels = true;
		}
		if (doOnce && game.manager.isLoaded("data/uiskin.atlas") && game.manager.isLoaded("packs/test.pack")
				&& game.loader.loadedTrickLabels) {
			game.loader.testAtlas = game.manager.get("packs/test.pack", TextureAtlas.class);
			game.loader.uiSkinAtlas = game.manager.get("data/uiskin.atlas", TextureAtlas.class);
			doOnce = false;
		}

		// System.out.println(out);
		for (String s : game.manager.getAssetNames()) {
			if (!out.contains(s)) {
				out += "loaded: " + s + "\n";
			}
		}
		loadingText.setText(out);
		loadingText.setPosition(2, loadingText.getPrefHeight() / 2);
		// Clear the screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (game.manager.update() && game.loader.loadedTrickLabels) { // Load
																		// some,
																		// will
																		// return
																		// true
																		// if
																		// done
			// loading
			// if (Gdx.input.isTouched()) { // If the screen is touched after
			// the game is done loading, go to the main menu screen
			if (game.loader.getName().equals("Empty")) {
				game.setScreen(new SaveScreen(game));
			} else {
				System.out.println(loadTime);
				game.musicManager.play("Sound/Game-Menu_Looping.mp3");
				game.setScreen(new MenuScreen(game));
			}
			// }
		}

		// Interpolate the percentage to make it more smooth
		percent = Interpolation.linear.apply(percent, game.manager.getProgress(), 0.1f);

		// Update positions (and size) to match the percentage
		loadingBarHidden.setX(startX + endX * percent);
		loadingBg.setX(loadingBarHidden.getX() + 30);
		loadingBg.setWidth(450 - 450 * percent);
		loadingBg.invalidate();

		// Show the loading screen
		stage.act();
		stage.draw();
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
		// Dispose the loading assets as we no longer need them
		game.manager.unload("data/loading.pack");
		stage.dispose();
		loadingFont.dispose();
	}

	public static int countLines(String str) {
		if (str == null || str.isEmpty()) {
			return 0;
		}
		int lines = 1;
		int pos = 0;
		while ((pos = str.indexOf("\n", pos) + 1) != 0) {
			lines++;
		}
		return lines;
	}

}
