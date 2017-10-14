package com.ells.agentex.stages;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter.Particle;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.particles.values.ScaledNumericValue;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ShortArray;
import com.badlogic.gdx.utils.TimeUtils;
import com.ells.agentex.AgentExGame;
import com.ells.agentex.actors.Axis;
import com.ells.agentex.actors.Equation;
import com.ells.agentex.actors.Player;
import com.ells.agentex.actors.TrickLabel;
import com.ells.agentex.listeners.NewGameStageContactListener;
import com.ells.agentex.screens.LevelScreen;
import com.ells.agentex.sound.MusicManager.MUSICSTATE;
import com.ells.agentex.tables.CoinCounter;
import com.ells.agentex.utils.PolySpatial;
import com.ells.agentex.utils.SimpleSpatial;
import com.ells.agentex.utils.overlays.DeathOverlay;
import com.ells.agentex.utils.overlays.InGameOverlay;
import com.ells.agentex.utils.overlays.PauseOverlay;
import com.ells.agentex.utils.overlays.PreGameOverlay;
import com.ells.agentex.utils.overlays.TutorialOverlay;
import com.ells.agentex.utils.overlays.WinOverlay;
import com.ells.agentex.utils.parallax.ParallaxBackground;
import com.ells.agentex.utils.parallax.ParallaxLayer.TileMode;
import com.ells.agentex.utils.parallax.TextureRegionParallaxLayer;
//import com.ells.agentex.utils.parallax.ParallaxBackground;
//import com.ells.agentex.utils.parallax.ParallaxLayer;
import com.gushikustudios.rube.RubeScene;
import com.gushikustudios.rube.loader.RubeSceneAsyncLoader;
import com.gushikustudios.rube.loader.serializers.utils.RubeImage;
import com.badlogic.gdx.graphics.g3d.particles.values.ScaledNumericValue;

/**
 * Use the left-click to pan. Scroll-wheel zooms.
 * 
 * @author cvayer, tescott
 * 
 */
public class NewGameStage extends Stage {
	private RubeScene mScene;
	private Box2DDebugRenderer mDebugRender;

	private Array<SimpleSpatial> mSpatials; // used for rendering rube images
	private Array<SimpleSpatial> tmpSpatials;
	private Array<SimpleSpatial> axisSpatials;
	private Array<SimpleSpatial> cautionSpatials;
	private Array<SimpleSpatial> addOnStart;
	private Array<SimpleSpatial> hashSpatials;
	private Array<SimpleSpatial> playerSpatials = new Array<SimpleSpatial>();
	private Array<PolySpatial> mPolySpatials;
	private Texture gridLines, caution;
	private Map<String, Texture> mTextureMap;
	private Map<Texture, TextureRegion> mTextureRegionMap;
	private ParallaxBackground rbg2;
	private Texture gradient;
	private int firstTime = 0;
	private Boolean goNow = false;
	private TrickLabel trickLabel;
	private float labelCounter = 0;

	private static final Vector2 spatialPos = new Vector2(); // shared by all objects
	private static final Vector2 polyPos = new Vector2(); // shared during polygon
														// creation
	private SpriteBatch mBatch;
	private PolygonSpriteBatch mPolyBatch;
	private AssetManager mAssetManager;

	// used for pan and scanning with the mouse.
	private Vector3 mCamPos;
	private Vector3 mCurrentPos;

	private World mWorld;
	private float mAccumulator; // time accumulator to fix the physics step.

	private int mVelocityIter = 8;
	private int mPositionIter = 3;
	private float mSecondsPerStep = 1f / 60f;

	// new custom stuff
	private Player player;
	private AgentExGame game;
	private UIStage uiStage;
	public float screenWidth = Gdx.graphics.getWidth();
	public float screenHeight = Gdx.graphics.getHeight();
	public final float VIEWPORT_WIDTH = screenWidth / 75f;
	public final float VIEWPORT_HEIGHT = screenHeight / 75f;
	private float ratioZoom = 1280 / screenWidth;


	private static final String[][] RUBE_SCENE_FILE_LIST = { { "rube/a1.json", "rube/v1.json" } };

	private static final float FLASH_RATE = 0.25f;

	public enum GAME_STATE {
		STARTING, LOADING, RUNNING
	};

	private GAME_STATE mState;
	@SuppressWarnings("unused")
	private GAME_STATE mPrevState;
	private GAME_STATE mNextState;

	private boolean mUseAssetManager;
	private int mRubeFileList;
	private int mRubeFileIndex;

	// defined
	private Skin skin;
	private Array<Float> radiusOrNo = new Array<Float>();
	public Array<Vector2> focusPoints = new Array<Vector2>();
	private Array<Vector2> verts = new Array<Vector2>();
	private Array<Axis> axes = new Array<Axis>();
	private Array<String> imageNames = new Array<String>();
	private Array<Body> groundBodies = new Array<Body>();
	Array<Body> finishes = new Array<Body>();
	private Array<Vector2> verticies = new Array<Vector2>();
	public long startTime = TimeUtils.millis();
	public long startPause = 0;
	public long endPause = 0;
	public Label timerLabel;
	private Vector2 playerCameraPosition = new Vector2();
	public long lastHit = TimeUtils.millis();
	private int scoreCounter = 0;
	private Array<Float> coinsCollected;
	private Array<Integer> coinSpatialIndeces;
	private Array<Texture> texturesToDisposeOf = new Array<Texture>();

	public enum DEADSTATE {
		ALIVE, DEAD;
	};

	public static DEADSTATE deadState;
	private boolean winState;
	public int bonusCoins;
	private Array<Body> toDestroy = new Array<Body>();
	private Array<Fixture> destroyFix = new Array<Fixture>();
	private Array<Fixture> toLengthen = new Array<Fixture>();
	private Map<Integer, SimpleSpatial> coinMap = new HashMap<Integer, SimpleSpatial>();
	public Array<Equation> equations = new Array<Equation>();
	private BitmapFont smallFont;
	private Array<Body> lineBodies = new Array<Body>();
	private Array<Vector2> groundVerts = new Array<Vector2>();

	// stuff that needs to be defined
	public double focus;
	public boolean cameraFollow;
	public boolean snapMode;
	public boolean cameraSliding;
	public boolean fadeOut;
	public Rectangle bounds;
	private Vector2 finishPos;
	private String layer1 = "", layer2 = "", layer3 = "", layer4 = "";
	private TextureLoader.TextureParameter mipMapParameter = new TextureLoader.TextureParameter();
	public static float finalTime = 0;
	public static float timeTrial = 0;

	private Array<Integer> coinsToRemove = new Array<Integer>();
	private boolean cameraLock = false;

	private ParticleEffect portalParticle, spawnParticle;
	// winParticle;
	private boolean deletePlayerOnce = true;
	private Array<Body> playerBodies = new Array<Body>();
	private RepeatAction spawnAction;
	private RepeatAction fadeAction;

	private Array<Body> fireBodies = new Array<Body>();
	private Array<ParticleEffect> fireParticles = new Array<ParticleEffect>();
	private Array<ParticleEffect> boostParticles = new Array<ParticleEffect>();
	private Array<Body> boostBodies = new Array<Body>();
	
	private ContactListener contactListener;
	private boolean firstPlay = true;

	private NewGameStage(boolean useAssetManager) {
		this.setGame(((AgentExGame) Gdx.app.getApplicationListener()));
		deadState = DEADSTATE.ALIVE;
		cameraFollow = false;
		snapMode = true;
		cameraSliding = false;
		fadeOut = false;
		setWinState(false);
		bonusCoins = 0;
		RUBE_SCENE_FILE_LIST[0][0] = "rube/" + LevelScreen.currentLevel + ".json";
		// fix or remove etGame().loader.nestedLevelProfile
		String str = String.valueOf(game.loader.getProfile().get("selectedVehicle"));
		RUBE_SCENE_FILE_LIST[0][1] = "rube/v" + str + ".json";
		mUseAssetManager = useAssetManager;

		mDebugRender = new Box2DDebugRenderer();
		mSpatials = new Array<SimpleSpatial>();
		tmpSpatials = new Array<SimpleSpatial>();
		axisSpatials = new Array<SimpleSpatial>();
		addOnStart = new Array<SimpleSpatial>();
		hashSpatials = new Array<SimpleSpatial>();
		cautionSpatials = new Array<SimpleSpatial>();
		caution = new Texture(Gdx.files.internal("img/caution.png"));
		caution.setFilter(TextureFilter.Nearest, TextureFilter.Linear);
		gridLines = new Texture(Gdx.files.internal("img/gridLines.png"));
		gridLines.setFilter(TextureFilter.Nearest, TextureFilter.Linear);
		texturesToDisposeOf.add(caution);
		texturesToDisposeOf.add(gridLines);

		mBatch = new SpriteBatch();
		mPolyBatch = new PolygonSpriteBatch();

		mTextureMap = new HashMap<String, Texture>();
		mTextureRegionMap = new HashMap<Texture, TextureRegion>();

		setmState(mNextState = GAME_STATE.STARTING);
		
		//needs something here i think
		game.musicManager.changeTrack(MUSICSTATE.INGAME);
		/*if (game.musicManager.isPlaying("Sound/Game-Menu_Looping.mp3")) {
			game.musicManager.fadeNewSong("Sound/Background #2.ogg");
		}*/

	}

	public NewGameStage(boolean useAssetManager, int rubeFileList) {
		this(useAssetManager);
		skin = getGame().loader.skin;
		System.out.println("SKIN " + skin.get("smallStyle", LabelStyle.class));
		smallFont = skin.get("smallStyle", LabelStyle.class).font;
		setCoinsCollected(new Array<Float>(
				(Array<Float>) game.loader.nestedLevelProfile.get(LevelScreen.currentLevel).get("coins")));
		mRubeFileList = 0;
	}

	public NewGameStage(boolean useAssetManager, int rubeFileList, Array<Equation> equations) {
		this(useAssetManager);
		this.setGame(game);
		skin = getGame().loader.skin;
		System.out.println("SKIN " + skin.get("smallStyle", LabelStyle.class));
		smallFont = skin.get("smallStyle", LabelStyle.class).font;
		this.equations = equations;
		setCoinsCollected(new Array<Float>(
				(Array<Float>) game.loader.nestedLevelProfile.get(LevelScreen.currentLevel).get("coins")));
		mRubeFileList = 0;
	}

	public NewGameStage(boolean useAssetManager, int rubeFileList, Array<Equation> equations, Boolean start) {
		this(useAssetManager);
		this.setGame(game);
		skin = getGame().loader.skin;
		smallFont = skin.get("smallStyle", LabelStyle.class).font;
		this.equations = equations;
		setCoinsCollected(new Array<Float>(
				(Array<Float>) game.loader.nestedLevelProfile.get(LevelScreen.currentLevel).get("coins")));
		mRubeFileList = 0;
		if (start) {
			goNow = true;
		}

	}

	@Override
	public void dispose() {
		uiStage.dispose();
		for (Texture t : mTextureMap.values()) {
			t.dispose();
		}
		if (mBatch != null) {
			mBatch.dispose();
			mBatch = null;
		}
		for (ParticleEffect p : getFireParticles()) {
			p.dispose();
		}
		for (ParticleEffect p : getBoostParticles()) {
			p.dispose();
		}
		if (getPortalParticle() != null) {
			getPortalParticle().dispose();
		}
		if (spawnParticle != null) {
			spawnParticle.dispose();
		}
		// if (winParticle != null) {
		// winParticle.dispose();
		// }
		if (mPolyBatch != null) {
			mPolyBatch.dispose();
			mPolyBatch = null;
		}
		if (mDebugRender != null) {
			mDebugRender.dispose();
			mDebugRender = null;
		}
		if (mWorld != null) {
			mWorld.dispose();
			mWorld.setContactListener(null);
			mWorld = null;
		}
		mAssetManager.clear();
		if (mAssetManager != null) {
			mAssetManager.dispose();
		}
		for (SimpleSpatial m : mSpatials) {
			m.mSprite.getTexture().dispose();
		}
		for (Texture t : texturesToDisposeOf) {
			t.dispose();
		}
		for (SimpleSpatial m : playerSpatials) {
			m.mSprite.getTexture().dispose();
		}
		getGame().manager.get("PauseOverlay", PauseOverlay.class).removeListeners();
		getGame().manager.get("DeathOverlay", DeathOverlay.class).clearListeners();
		
		rbg2.dispose();
		
		super.dispose();

	}

	@Override
	public void act (float delta) {
		//Gdx.app.log("debugging", "NewGameStage.act");
		update(delta);
		
		if (getToDestroy() != null && getToDestroy().size > 0) {
			for (int i = 0; i < getToDestroy().size; i++) {
				destroyBody(getToDestroy().get(i));
				if (getCoinsToRemove().size > 0) {
					for (int coinNumber : getCoinsToRemove()) {
						//mSpatials.removeValue(coinMap.get(coinNumber), true);
						//coinMap.get(coinNumber).mSprite.setAlpha(0);
					}
					getCoinsToRemove().clear();
				}
			}
			getToDestroy().clear();
		}
		if (labelCounter > 0) {
			labelCounter -= delta;
		}
		else {
			labelCounter = 0;
		}
		//System.out.println("LABEL COUNTER: " + labelCounter);
		
		present(delta);
		// state transitions here...
		mPrevState = getmState();
		setmState(mNextState);
	}

	private void activateBodies() {
		for (Body dbody : getmScene().getBodies()) {
			if (dbody.isActive() == false) {
				Vector2 bpos = dbody.getPosition();
				if (bpos.x < (this.getPlayer().getPlayer().getPosition().x + 10)) {
					dbody.setActive(true);
				}

			}
		}

	}

	/**
	 * This method is used for game logic updates.
	 * 
	 * @param delta
	 */
	private void update(float delta) {
		
		//Gdx.app.log("debugging", "NewGameStage.update");
		
		// game logic here...

		switch (getmState()) {
		case STARTING:
			initiateSceneLoad();
			break;

		case LOADING:
			processSceneLoad();
			break;

		case RUNNING:
			if (uiStage.gameMode.equals("pause")) {
				return;
			}
			
			if (player.isDead()) {
				this.cameraLock = true;
				if (deadState == DEADSTATE.ALIVE) {
					game.loader.darkActive = true;
					game.manager.get("InGameOverlay", InGameOverlay.class).disableButtons();
					destroyJoints(player.getConnectors());
					destroyJoints(player.getConnectors());
					destroyJoints(player.getConnectors());
					if (isWinState() == false) {
						for (int i = 0; i < getCoinsCollected().size; i++) {
							getCoinsCollected().set(i, (float) 0);
						}
						bonusCoins = 0;
						getGame().manager.get("DeathOverlay", DeathOverlay.class).addToStage(this.uiStage);
						deadState = DEADSTATE.DEAD;
					}
				}
			}
			if (winState) {
				for (Body b : playerBodies) {
					float playerVel = b.getLinearVelocity().x;
					//System.out.println("PLAYER VELOCITY: " + Math.abs(playerVel) * 4);
					b.setLinearDamping(Math.abs(playerVel) * 5);
				}
				if (deletePlayerOnce) {
					deletePlayerOnce = false;
					// player.torso.setUserData(winParticle);
					// for (Body s : playerBodies) {
					// s.setUserData(winParticle);
					// }
					this.uiStage.addAction(fadeAction);
				}
				// for (Body b : finishes) {
				// player.applyForce(b, delta);
				// }
			}
			player.update(delta);
			// activated inactive bodies as you get close
			
			
			updatePhysics(delta);
			updateCamera(delta);
			if (axes.size > 0 && uiStage.cameraMode.equals("axisFocus")) {
				if (axes.get(uiStage.activeAxis).getEquationTable() != null) {
					axes.get(uiStage.activeAxis).getEquationTable().update(delta);
				}
				uiStage.camera.update();
				if (!uiStage.camera.frustum.pointInFrustum(axes.get(uiStage.activeAxis).getCenter())
						&& !uiStage.movingToAxis) {
					uiStage.cameraMode = "preGame";
					uiStage.leftAxisFocus = true;
				}

			}
			
			//System.out.println("FIRSTTIME: " + firstTime);
			if (axes.size > 0 && uiStage.cameraMode.equals("axisFocus")) {
				//System.out.println("CAMERA MODE: " + uiStage.cameraMode);
				//renderAxes(true);
				if (firstTime == 0) {
					System.out.print("FIRST TIME");
					mSpatials.removeAll(axisSpatials, true);
					firstTime = 1;
					
					// adding something here to set caution symbol on hidden
					// axes
					axisSpatials.clear();
					Axis tmpAxis = axes.get(uiStage.activeAxis);
					renderAxes(true);
					//reset checked buttons
					uiStage.preGameUI.resetButtons(tmpAxis);
					
					if (tmpAxis.isHidden()) {
						mSpatials.add(new SimpleSpatial(gridLines, false, null, Color.WHITE,
								new Vector2(tmpAxis.getHeight(), tmpAxis.getWidth()),
								new Vector2(tmpAxis.getPosition().x, tmpAxis.getPosition().y), tmpAxis.getRotation()));
						axisSpatials.add(mSpatials.get(mSpatials.size - 1));

						mSpatials.add(new SimpleSpatial(caution, false, null, Color.WHITE,
								new Vector2(tmpAxis.getHeight() * 0.5f, tmpAxis.getWidth() * 0.35f),
								new Vector2(tmpAxis.getPosition().x, tmpAxis.getPosition().y), tmpAxis.getRotation()));
						cautionSpatials.add(mSpatials.get(mSpatials.size - 1));
						//axisSpatials.add(mSpatials.get(mSpatials.size - 1));
						/*if (tmpSpatials.contains(cautionSpatials.first(), true) == false) {
							System.out.println("ADDING CAUTION TO TMP");
							tmpSpatials.add(cautionSpatials.first());
						}*/

					} else {
						mSpatials.add(new SimpleSpatial(gridLines, false, null, Color.WHITE,
								new Vector2(tmpAxis.getHeight(), tmpAxis.getWidth()),
								new Vector2(tmpAxis.getPosition().x, tmpAxis.getPosition().y), tmpAxis.getRotation()));
						axisSpatials.add(mSpatials.get(mSpatials.size - 1));
					}
					//axisSpatials.clear();
					//renderAxes(false);
					renderAxes(true);
				}
			} else {
				mSpatials.removeAll(axisSpatials, true);
				//mSpatials.removeAll(cautionSpatials, true);
				//axisSpatials.clear();
				//cautionSpatials.clear();
				firstTime = 0;
				//System.out.println("reset first time");
			}

			if (uiStage.gameMode.equals("playing")) {
				
				if (firstPlay == true && hashSpatials.size > 0) {
					//firstPlay = false;
					mSpatials.removeAll(hashSpatials, false);
					mSpatials.removeAll(cautionSpatials, true);
				}
				
				if (firstPlay == true) {
					player.setActive(true);
					for (Body b : mScene.getBodies()) {
						if (!b.isAwake()) {
							b.setAwake(true);
						}
					}
					mScene.getWorld().setGravity(new Vector2(0,-10));
					firstPlay = false;
				}
				
				activateBodies();
				
				firstTime = 0;
				if (addOnStart.size > 0) {
					mSpatials.addAll(addOnStart);
					addOnStart.clear();
					//added below line
					mSpatials.removeAll(axisSpatials, true);
					mSpatials.removeAll(cautionSpatials, false);
					cautionSpatials.clear();
					
				}
				timerLabel.setText(milToMinSecMil(TimeUtils.timeSinceMillis(startTime - (startPause - endPause))));
			}
			break;
		}
	}

	/**
	 * The present() method is for drawing / rendering...
	 * 
	 * @param delta
	 */
	private void present(float delta) {
		// game rendering logic here...
		Gdx.gl.glClearColor(0, 0, 0, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		switch (getmState()) {
		case STARTING:
		case LOADING:
			mBatch.begin();
			mBatch.draw(game.manager.get("img/futuristic.png", Texture.class), screenWidth - game.manager.get("img/futuristic.png", Texture.class).getWidth(), 0);
			mBatch.end();
			break;

		case RUNNING:
			renderWorld(delta);
			break;
		}
	}

	/**
	 * Kicks off asset manager if selected...
	 * 
	 */
	private void initiateSceneLoad() {
		if (mUseAssetManager) {
			//start asset manager operations...
			mAssetManager = new AssetManager();
			mAssetManager.setLoader(RubeScene.class, new RubeSceneAsyncLoader(new InternalFileHandleResolver()));
			String levelPack = String.valueOf(LevelScreen.currentLevel.charAt(0));
			System.out.println("LEVEL PACK" + levelPack);

			//load rube scene
			mAssetManager.load(RUBE_SCENE_FILE_LIST[mRubeFileList][mRubeFileIndex], RubeScene.class);
		}
		mNextState = GAME_STATE.LOADING;
	}

	/**
	 * Either performs a blocking load or a poll on the asset manager load...
	 */
	private void processSceneLoad() {
		if (mAssetManager.update()) {
			// each iteration adds to the scene that is ultimately returned...
			setmScene(mAssetManager.get(RUBE_SCENE_FILE_LIST[mRubeFileList][mRubeFileIndex++], RubeScene.class));
			if (mRubeFileIndex < RUBE_SCENE_FILE_LIST[mRubeFileList].length) {
				mAssetManager.load(RUBE_SCENE_FILE_LIST[mRubeFileList][mRubeFileIndex], RubeScene.class);
			} else {
				processScene();
				mNextState = GAME_STATE.RUNNING;
			}
		}
	}

	/**
	 * Builds up world based on info from the scene...
	 */
	private void processScene() {
		game.manager.get("InGameOverlay", InGameOverlay.class).enableButtons();
		Label.LabelStyle style = new Label.LabelStyle();
		style.font = game.loader.fonts.get("scoreFont");
		timerLabel = new Label("00:00", style);
		player = new Player();
		player.indetifyVehicleParts(getmScene(), game);
		player.setActive(false);
		setUiStage(new UIStage(this, getPlayer(), getGame()));
		mWorld = getmScene().getWorld();
		mWorld.setGravity(new Vector2(0,0));
		timeTrial = (Float) mScene.getCustom(mWorld, "time", 0f);
		//create image and poly spatials
		createSpatialsFromRubeImages(getmScene());
		createPolySpatialsFromRubeFixtures(getmScene());
		
		Array<Body> bodies = mScene.getBodies();
		player.setActive(false);

		// draw pixmaps for objects only 1 time
		Color lvlColor = Color.BLACK;
		lvlColor.set(0, 0, 0, 0.9f);
		Pixmap pixmapObj = new Pixmap(1, 1, Format.RGBA8888);
		pixmapObj.setColor(lvlColor);
		pixmapObj.fill();
		Texture textureObj = new Texture(pixmapObj);
		texturesToDisposeOf.add(textureObj);
		TextureRegion regionObj = new TextureRegion(textureObj);
		pixmapObj.dispose();
		// now for circles
		Pixmap circlePixmap = new Pixmap(200, 200, Format.RGBA8888);
		circlePixmap.setColor(0, 0, 0, 0f);
		circlePixmap.fill();
		circlePixmap.setColor(lvlColor);
		circlePixmap.fillCircle(100, 100, 100);
		Texture circleTexture = new Texture(circlePixmap);
		texturesToDisposeOf.add(circleTexture);
		circlePixmap.dispose();
		
		
		//cycle through bodies and set user data to act as flags
		if ((bodies != null) && (bodies.size > 0)) {
			for (int i = 0; i < bodies.size; i++) {
				Body body = bodies.get(i);
				String type = (String) mScene.getCustom(body, "type", null);
				int number = (Integer) (mScene.getCustom(body, "coinNumber", -1));
				// add user data to coin bodies for collision

				if (type != null && type.equals("coin")) {
					body.setUserData("coin");
				}

				else if (type != null && type.equals("bg")) {
					body.setUserData("bg");
				}

				else if (type != null && type.contains("finish")) {
					body.setUserData("finish");
					finishes.add(body);
				}

				else if (type != null && type.equals("booster")) {
					//body.setUserData("booster");
					boostBodies.add(body);
				}

				else if (type != null && type.equals("fire")) {
					fireBodies.add(body);
				}

				if (type != null && type.contains("axis")) {
					verts.clear();
					Vector2 boxCenter = new Vector2(body.getPosition().x, body.getPosition().y);
					PolygonShape s = ((PolygonShape) (body.getFixtureList().get(0).getShape()));
					for (int k = 0; k < s.getVertexCount(); k++) {
						Vector2 tmp = new Vector2();
						s.getVertex(k, tmp);
						verts.add(tmp);
					}
					axes.add(new Axis(boxCenter, (verts.get(0).x - verts.get(3).x), (verts.get(1).y - verts.get(0).y),
							mScene, mWorld, skin, getUiStage(), (Boolean) mScene.getCustom(body, "hidden", false),
							Integer.parseInt(type.replaceAll("axis", ""))));

				} else if (type != null && type.contains("finish")) {
					if (!type.contains("a")) {
						finishPos = new Vector2(body.getPosition().x, body.getPosition().y);
					}
				}
				
				//all bodies with type object are created out of polygons and added to polyspatials
				else if (type != null && type.equals("object")) {

					for (Fixture f : body.getFixtureList()) {

						if (f.getShape().getType().equals(Shape.Type.Circle)) {
							CircleShape s;
							s = ((CircleShape) (f.getShape()));
							float radius = s.getRadius();

							mSpatials.add(new SimpleSpatial(circleTexture, false, body, Color.WHITE,
									new Vector2(radius * 2, radius * 2),
									new Vector2(s.getPosition().x, s.getPosition().y), body.getAngle()));

						}

						if (f.getType().equals(Shape.Type.Polygon)) {
							// toLengthen.add(f);
							PolygonShape s = ((PolygonShape) (f.getShape()));
							float[] verts = new float[s.getVertexCount() * 2];
							Arrays.fill(verts, 0);

							for (int v = 0; v < s.getVertexCount(); v++) {
								// get vertices of shape
								Vector2 tmp = new Vector2();
								s.getVertex(v, tmp);
								verts[v * 2] = tmp.x * 32;
								verts[v * 2 + 1] = tmp.y * 32;
							}

							EarClippingTriangulator triangulator = new EarClippingTriangulator();
							ShortArray triangleIndices = triangulator.computeTriangles(verts);
							short[] tIndeces = new short[triangleIndices.size];
							for (int t = 0; t < triangleIndices.size; t++) {
								tIndeces[t] = triangleIndices.get(t);
							}

							// new render method
							PolygonRegion polyReg = new PolygonRegion(regionObj, verts, tIndeces);
							mPolySpatials.add(new PolySpatial(polyReg, body, lvlColor));

						}
					}

				}
				
				else if (type != null && type.equals("boundary")) {
					Vector2 boundryCentre = new Vector2(body.getPosition().x, body.getPosition().y);
					Vector2[] verts = new Vector2[4];
					PolygonShape s = ((PolygonShape) (body.getFixtureList().get(0).getShape()));
					for (int k = 0; k < s.getVertexCount(); k++) {
						Vector2 tmp = new Vector2();
						s.getVertex(k, tmp);
						verts[k] = (tmp);
					}
					float bottomX = verts[2].x + body.getPosition().x;
					float bottomY = verts[0].y + body.getPosition().y;
					float width = (Math.abs((body.getPosition().x + verts[2].x) - (body.getPosition().x + verts[0].x)));
					float height = (Math
							.abs((body.getPosition().y + verts[2].y) - (body.getPosition().y + verts[0].y)));
					bounds = new Rectangle(bottomX, bottomY, width, height);
					body.destroyFixture(body.getFixtureList().get(0));
					getUiStage().bounds = bounds;
				}

			}
		}
		for (Axis a : axes) {
			a.setWorld(mWorld);

		}
		int z = 0;
		for (Axis e : axes) {
			if (equations.size == 0 || equations.get(z).getAValue() == -100) {
				e.setEquation(new Equation(-100, 1, 0, 0, 1));
			} else {
				e.setEquation(equations.get(z));
			}
			z++;
		}
		// int k = 0;
		// for (Axis e : axes) {
		for (int k = 0; k < axes.size; k++) {
			if (axes.get(k).getEquation().getAValue() != -100) {
				if (axes.get(k).getEquationTable() != null) {
					axes.get(k).getEquationTable().remove();
				}
				if (axes.get(k).getBodyArray().size != 0) {
					mWorld.destroyBody(axes.get(k).getBody());
				}
				// a value, k value, horizontal translation, vertical
				// translation, power
				axes.get(k).createLine(mScene, axes.get(k).equationToPoints());
				axes.get(k).setupTable();
			}
			// k++;
		}
		
		//add caution spatials on RESTART to hidden axes
		for (Axis a : axes) {
			System.out.println("CAUTION ON RESTART: 1" + a.isBuilt());
			//add caution spatials for existing equations only
			if (a.isBuilt()) {
				System.out.println("CAUTION ON RESTART: 2");
				if (a.isHidden()) {
					System.out.println("CAUTION ON RESTART: 3");
					mSpatials.add(new SimpleSpatial(caution, false, null, Color.WHITE,
							new Vector2(a.getHeight() * 0.5f, a.getWidth() * 0.35f),
							new Vector2(a.getPosition().x, a.getPosition().y), a.getRotation()));
					cautionSpatials.add(mSpatials.get(mSpatials.size - 1));

				}
			}
		}
		
		//parallax backgrounds
				float worldWidth = 75;
				Array<TextureRegionParallaxLayer> layers = new Array<TextureRegionParallaxLayer>();
				
				String levelPack = String.valueOf(LevelScreen.currentLevel.charAt(0));
				Random random = new Random();
				if (levelPack.equals("a")) {
					gradient = new Texture (Gdx.files.internal("data/gradient19.png"));
					//layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/gradient8.png"), true))));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/clouds"+(random.nextInt(3) + 1)+".png"), true)), new Vector2(0.05f,0.025f), new Vector2(0,3)));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/clouds"+(random.nextInt(3) + 1)+".png"), true)), new Vector2(0.05f,0.025f), new Vector2(1,3)));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/clouds"+(random.nextInt(3) + 1)+".png"), true)), new Vector2(0.05f,0.025f), new Vector2(2,3)));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/mountains1.png"), true)), new Vector2(0.1f,0.05f), new Vector2(0,2)));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/mountains2.png"), true)), new Vector2(0.1f,0.05f), new Vector2(1,2)));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/mountains3.png"), true)), new Vector2(0.1f,0.05f), new Vector2(2,2)));
					
				}
				else if (levelPack.equals("b")) {
					gradient = new Texture (Gdx.files.internal("data/gradient8.png"));
					//layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/gradient8.png"), true))));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/dots"+(random.nextInt(3) + 1)+".png"), true)), new Vector2(0.05f,0.025f), new Vector2(0,3)));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/crescent.png"), true)), new Vector2(0.05f,0.025f), new Vector2(1,3)));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/dots"+(random.nextInt(3) + 1)+".png"), true)), new Vector2(0.05f,0.025f), new Vector2(2,3)));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/city1.png"), true)), new Vector2(0.1f,0.05f), new Vector2(0,0)));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/city2.png"), true)), new Vector2(0.1f,0.05f), new Vector2(1,0)));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/city3.png"), true)), new Vector2(0.1f,0.05f), new Vector2(2,0)));
				}
				else if (levelPack.equals("e")) {
					gradient = new Texture (Gdx.files.internal("data/gradient27.png"));
					//layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/gradient8.png"), true))));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/clouds"+(random.nextInt(3) + 1)+".png"), true)), new Vector2(0.05f,0.025f), new Vector2(0,5)));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/clouds"+(random.nextInt(3) + 1)+".png"), true)), new Vector2(0.05f,0.025f), new Vector2(1,5)));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/clouds"+(random.nextInt(3) + 1)+".png"), true)), new Vector2(0.05f,0.025f), new Vector2(2,5)));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/mountains1.png"), true)), new Vector2(0.1f,0.05f), new Vector2(0,0)));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/mountains2.png"), true)), new Vector2(0.1f,0.05f), new Vector2(1,0)));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/mountains3.png"), true)), new Vector2(0.1f,0.05f), new Vector2(2,0)));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/trees1.png"), true)), new Vector2(0.15f,0.075f), new Vector2(0,0)));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/trees2.png"), true)), new Vector2(0.15f,0.075f), new Vector2(1,0)));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/trees3.png"), true)), new Vector2(0.15f,0.075f), new Vector2(2,0)));
				}
				else if (levelPack.equals("d")) {
					gradient = new Texture (Gdx.files.internal("data/gradient28.png"));
					//layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/gradient8.png"), true))));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/moon.png"), true)), new Vector2(0.05f,0.025f), new Vector2(0,3)));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/dots"+(random.nextInt(3) + 1)+".png"), true)), new Vector2(0.05f,0.025f), new Vector2(1,3)));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/dots"+(random.nextInt(3) + 1)+".png"), true)), new Vector2(0.05f,0.025f), new Vector2(2,3)));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/future1.png"), true)), new Vector2(0.1f,0.05f), new Vector2(0,0)));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/future2.png"), true)), new Vector2(0.1f,0.05f), new Vector2(1,0)));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/future3.png"), true)), new Vector2(0.1f,0.05f), new Vector2(2,0)));
				}
				else if (levelPack.equals("c")) {
					gradient = new Texture (Gdx.files.internal("data/gradient32.png"));
					//layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/gradient8.png"), true))));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/dots"+(random.nextInt(3) + 1)+".png"), true)), new Vector2(0.05f,0.025f), new Vector2(0,5)));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/dots"+(random.nextInt(3) + 1)+".png"), true)), new Vector2(0.05f,0.025f), new Vector2(1,5)));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/dots"+(random.nextInt(3) + 1)+".png"), true)), new Vector2(0.05f,0.025f), new Vector2(2,5)));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/forest1.png"), true)), new Vector2(0.1f,0.05f), new Vector2(0,0)));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/forest2.png"), true)), new Vector2(0.1f,0.05f), new Vector2(1,0)));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/forest3.png"), true)), new Vector2(0.1f,0.05f), new Vector2(2,0)));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/trees1.png"), true)), new Vector2(0.15f,0.075f), new Vector2(0,0)));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/trees2.png"), true)), new Vector2(0.15f,0.075f), new Vector2(1,0)));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/trees3.png"), true)), new Vector2(0.15f,0.075f), new Vector2(2,0)));
				}
				//to change later
				else if (levelPack.equals("f")) {
					gradient = new Texture (Gdx.files.internal("data/gradient8.png"));
					//layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/gradient8.png"), true))));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/dots"+(random.nextInt(3) + 1)+".png"), true)), new Vector2(0.05f,0.025f), new Vector2(0,3)));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/crescent.png"), true)), new Vector2(0.05f,0.025f), new Vector2(1,3)));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/dots"+(random.nextInt(3) + 1)+".png"), true)), new Vector2(0.05f,0.025f), new Vector2(2,3)));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/city1.png"), true)), new Vector2(0.1f,0.05f), new Vector2(0,0)));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/city2.png"), true)), new Vector2(0.1f,0.05f), new Vector2(1,0)));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/city3.png"), true)), new Vector2(0.1f,0.05f), new Vector2(2,0)));
					
				}
				else if (levelPack.equals("g")) {
					gradient = new Texture (Gdx.files.internal("data/gradient8.png"));
					//layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/gradient8.png"), true))));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/dots"+(random.nextInt(3) + 1)+".png"), true)), new Vector2(0.05f,0.025f), new Vector2(0,3)));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/crescent.png"), true)), new Vector2(0.05f,0.025f), new Vector2(1,3)));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/dots"+(random.nextInt(3) + 1)+".png"), true)), new Vector2(0.05f,0.025f), new Vector2(2,3)));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/city1.png"), true)), new Vector2(0.1f,0.05f), new Vector2(0,0)));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/city2.png"), true)), new Vector2(0.1f,0.05f), new Vector2(1,0)));
					layers.add(new TextureRegionParallaxLayer(new TextureRegion(new Texture(Gdx.files.internal("data/city3.png"), true)), new Vector2(0.1f,0.05f), new Vector2(2,0)));
					
				}
				rbg2 = new ParallaxBackground();
				for (TextureRegionParallaxLayer l : layers) {
					l.getTexRegion().getTexture().setFilter(TextureFilter.Nearest, TextureFilter.Linear);
					rbg2.addLayers(l);
				}
		mWorld.setContactListener(new NewGameStageContactListener(this));

		// configure simulation settings
		/**
		 * mVelocityIter = getmScene().velocityIterations; mPositionIter =
		 * getmScene().positionIterations; if (getmScene().stepsPerSecond != 0)
		 * { mSecondsPerStep = 1f / getmScene().stepsPerSecond; }
		 **/
		// testSceneSettings();
		// getmScene().clear(); // no longer need any scene references
		
		//configure overlays
		getGame().manager.get("PauseOverlay", PauseOverlay.class).addThingsThatNeedStage();
		getGame().manager.get("WinOverlay", WinOverlay.class).addListenersToButtons(this);
		getGame().manager.get("InGameOverlay", InGameOverlay.class).addThingsThatNeedStage(getUiStage(), getPlayer(),
				timerLabel);
		getGame().manager.get("PreGameOverlay", PreGameOverlay.class).addThingsThatNeedStage(getUiStage(), getPlayer());
		getGame().manager.get("DeathOverlay", DeathOverlay.class).addThingsThatNeedStage(this);
		getGame().manager.get("TutorialOverlay", TutorialOverlay.class).initializeListener(getUiStage());
		//getGame().loader.setLevelData(LevelScreen.currentLevel, "firstLoad", false);
		getUiStage().addListener();
		focusPoints.add(getPlayer().getPlayer().getPosition());
		
		//add camera focus point for each axis
		Axis tempVar;
		for (int i = 0; i < axes.size; i++) {
			for (int j = i; j < axes.size; j++) {
				if (axes.get(i).getNum() > axes.get(j).getNum()) {
					tempVar = axes.get(i);
					axes.set(i, axes.get(j));
					axes.set(j, tempVar);
				}
			}
		}
		for (Axis e : axes) {
			focusPoints.add(e.getPosition());
		}
		int num1 = 0;
		
		//multiple particle effects so two boosts on same screen don't look the exact same
		for (Body b : boostBodies) {
			ParticleEffect boostParticle = new ParticleEffect();
			boostParticle.load(Gdx.files.internal("particles/boost"), Gdx.files.internal("particles"));
			ParticleEmitter e = boostParticle.getEmitters().get(0);
			e.durationTimer += num1;

			//System.out.println("ANGLE" + Math.toDegrees(b.getAngle()) + " E ANGLE: " + e.getAngle());
			
			
			e.getAngle().setHigh(90 + (float) Math.toDegrees(b.getAngle()), 90 + (float) Math.toDegrees(b.getAngle()));
			e.getRotation().setHigh((float) Math.toDegrees(b.getAngle()), (float) Math.toDegrees(b.getAngle()));
			
			num1 += 0.5f;
			b.setUserData(boostParticle);
			getBoostParticles().add(boostParticle);
		}

		setPortalParticle(new ParticleEffect());
		getPortalParticle().load(Gdx.files.internal("particles/phaseout"), Gdx.files.internal("particles"));
		getPortalParticle().getEmitters().first().setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		getPortalParticle().start();
		for (Body b : finishes) {
			ParticleEmitter p = getPortalParticle().getEmitters().first();
				Vector2 v0 = new Vector2();
				Vector2 v1 = new Vector2();
				Vector2 v2 = new Vector2();
				Vector2 v3 = new Vector2();
				PolygonShape square = ((PolygonShape) b.getFixtureList().get(0).getShape());
				square.getVertex(0, v0);
				square.getVertex(1, v1);
				square.getVertex(2, v2);
				square.getVertex(3, v3);
				float width = (float) Math.sqrt(Math.pow(v1.x - v0.x, 2) + Math.pow(v1.y - v0.y, 2));
				float height = (float) Math.sqrt(Math.pow(v2.x - v1.x, 2) + Math.pow(v2.y - v1.y, 2));
				p.getSpawnWidth().setHigh(width);
				p.getYOffsetValue().setLow(-height/2);
				//p.getAngle().setHigh((float) (90+Math.toDegrees(b.getAngle())));
			b.setUserData(getPortalParticle());
		}
		spawnParticle = new ParticleEffect();
		spawnParticle.load(Gdx.files.internal("particles/phasein"), Gdx.files.internal("particles"));
		spawnParticle.getEmitters().first().setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		spawnParticle.start();
		this.player.getPlayer().setUserData(spawnParticle);
		for (SimpleSpatial s : playerSpatials) {
			s.opacity = 0;
		}
		float num = 0;
		for (Body f : fireBodies) {
			ParticleEffect fireParticle = new ParticleEffect();
			fireParticle.load(Gdx.files.internal("particles/fire"), Gdx.files.internal("particles"));
			fireParticle.getEmitters().first().setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
			fireParticle.start();
			getFireParticles().add(fireParticle);
			f.setUserData(fireParticle);
			num += 0.5f;

		}
		spawnAction = Actions.forever(Actions.run(new Runnable() {

			@Override
			public void run() {
				//USED TO BE 0.99
				if (playerSpatials.get(0).opacity < 0.95) {
					/*for (SimpleSpatial s : playerSpatials) {
						s.opacity = (spawnParticle.getEmitters().get(0).durationTimer
								/ (spawnParticle.getEmitters().get(0).duration
										+ spawnParticle.getEmitters().get(0).getLife().getHighMin() / 1000));
					}*/
					for (SimpleSpatial s : playerSpatials) {

						s.opacity = s.opacity + Gdx.graphics.getDeltaTime() / 2;
					}
				} else {
					for (SimpleSpatial ss : playerSpatials) {
						//NewGameStage.this.destroyBody(s);
						ss.opacity = 1;
					}
					spawnAction.finish();
				}
			}
		}));
		fadeAction = Actions.forever(Actions.run(new Runnable() {

			@Override
			public void run() {
				if (playerSpatials.get(0).opacity > 0.01) {
					for (SimpleSpatial s : playerSpatials) {

						s.opacity = s.opacity - Gdx.graphics.getDeltaTime();
					}
				} else {
					for (SimpleSpatial ss : playerSpatials) {
						//NewGameStage.this.destroyBody(s);
						ss.opacity = 0;
					}
					fadeAction.finish();
				}
			}
		}));
		uiStage.addAction(spawnAction);

		focusPoints.add(finishPos);
		//getGame().manager.get("TutorialOverlay", TutorialOverlay.class).initializeListener(getUiStage());
		//getGame().manager.get("TutorialOverlay", TutorialOverlay.class).addToStage();
		
		if (axes.size < 1 || goNow == true) {
			startPlaying();
		}
		
		else {
			checkTutorial();
		}
	}

	public void startPlaying() {
		
		uiStage.cameraMode = "playing";
		uiStage.gameStage.cameraFollow = true;
		uiStage.gameStage.snapMode = false;
		uiStage.gameMode = "playing";

		uiStage.gameStage.focus = 0;
		if (uiStage.gameStage.getAxes().size > 0) {
			if (uiStage.gameStage.getAxes().get(uiStage.activeAxis).getEquationTable() != null) {
				uiStage.gameStage.getAxes().get(uiStage.activeAxis).getEquationTable().remove();
			}
		}
		game.manager.get("PreGameOverlay", PreGameOverlay.class).removeOverlay();
		game.manager.get("InGameOverlay", InGameOverlay.class).addToStage(uiStage);
		
		checkTutorial();
	}
	public void renderAxes(Boolean temp) {
		//System.out.println("RENDER AXES");
		if (tmpSpatials.size < 1) {
			//System.out.println("STORING TMP SPATIALS");
			tmpSpatials.addAll(mSpatials);
			mSpatials.addAll(cautionSpatials);

		}

		if (temp) {
			mSpatials.clear();
			mSpatials.addAll(tmpSpatials);
			mSpatials.addAll(axisSpatials);
			
			//System.out.println("NUM OF CAUTION SPATIALS: " + cautionSpatials.size);
			for (SimpleSpatial cSpatial : cautionSpatials) {
				if (mSpatials.contains(cSpatial, false)) {
					//pass
				}
				else {
					mSpatials.add(cSpatial);
				}
			}
		}

		addOnStart.clear();
		Color lvlColor = Color.RED;
		lvlColor.set(0, 0, 0, 0.9f);

		// change to only active axis??
		for (Axis a : axes) {

			if (a.isBuilt()) {

				if (a.getBody() != null) {
					Body body = a.getBody();
					Array<Fixture> fixtures = body.getFixtureList();
					if (fixtures.size > 0) {
						Fixture f = fixtures.get(0);
						ChainShape c = ((ChainShape) (f.getShape()));

						Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
						pixmap.setColor(lvlColor);
						pixmap.fill();
						Texture line = new Texture(pixmap);
						pixmap.dispose();

						Vector2 oldVert = new Vector2();
						double oldTheta = 0;
						int interval = 20;
						
						//what it used to be
						/*if (a.getEquation().getPower() == 0 || a.getEquation().getPower() == 1) {
							interval = c.getVertexCount() - 2;
						}*/
						
						//for (int v = 0; v < c.getVertexCount() - (interval + 1); v += interval) {
						
						for (int v = 0; v < c.getVertexCount() - 1; v++) {
							
							//System.out.println("VERTEX COUNT : " + c.getVertexCount());
							Vector2 v1 = new Vector2();
							Vector2 v2 = new Vector2();
							c.getVertex(v, v1);
							c.getVertex(v + 1, v2);

							/*if ((v + (interval + 1) * 2) > c.getVertexCount()) {
								interval = c.getVertexCount() - v - 1;
							}

							// get vertices of shape
							// verticies.add(new Vector2());
							Vector2 v1 = new Vector2();
							Vector2 v2 = new Vector2();
							c.getVertex(v, v1);
							c.getVertex(v + interval, v2);*/
							
							//hack to get line centered
							v1.y += 0.05;
							v2.y += 0.05;
							float height = (float) ((v2.y - v1.y));
							float width = (float) ((v2.x - v1.x));
							float dist = (float) Math.sqrt(width * width + height * height);
							double theta = Math.atan(height / width);
							double beta = 0.5 * Math.PI + theta;
							float m = (v2.y - v1.y) / (v2.x - v1.x);
							float bval = v2.y - m * v2.x;
							float pm = -1 / m;
							float pval = v1.y - pm * v1.x;
							float pval2 = v2.y - pm * v2.x;

							// Vector2[] polyVerts = new Vector2[4];
							float thk = 0.05f;
							/*
							 * polyVerts[0] = new Vector2((float) (thk *
							 * Math.cos(beta)) + v1.x, (float) (thk *
							 * Math.sin(beta)) + v1.y); polyVerts[1] = new
							 * Vector2((float) (-thk * Math.cos(beta)) + v1.x,
							 * (float) (-thk * Math.sin(beta)) + v1.y);
							 * polyVerts[2] = new Vector2((float) (-thk *
							 * Math.cos(beta)) + v2.x, (float) (-thk *
							 * Math.sin(beta)) + v2.y); polyVerts[3] = new
							 * Vector2((float) (thk * Math.cos(beta)) + v2.x,
							 * (float) (thk * Math.sin(beta)) + v2.y);
							 */
							float xMid = (float) (((-thk * Math.cos(beta) + v1.x) + (-thk * Math.cos(beta) + v2.x))
									/ 2);
							float yMid = (float) (((-thk * Math.sin(beta) + v1.y) + (-thk * Math.sin(beta) + v2.y))
									/ 2);

							// draw line
							if (a.isHidden()) {
								addOnStart
										.add(new SimpleSpatial(line, false, body, Color.WHITE, new Vector2(dist, 0.1f),
												new Vector2(xMid, yMid), (float) (theta * MathUtils.radiansToDegrees)));
							} else {
								mSpatials.add(new SimpleSpatial(line, false, body, Color.WHITE, new Vector2(dist, 0.1f),
										new Vector2(xMid, yMid), (float) (theta * MathUtils.radiansToDegrees)));
							}
							// add circle fixture to stop swinging objects from
							// breaking

							/*if (v == 0) {
								CircleShape cShape = new CircleShape();
								cShape.setRadius(0.1f);
								cShape.setPosition(new Vector2((float) (-thk * Math.cos(beta)) + v1.x,
										(float) (-thk * Math.sin(beta)) + v1.y));
								FixtureDef fixDef = new FixtureDef();
								fixDef.shape = cShape;
								body.createFixture(fixDef);
							}*/
							
							// draw triangles to fill the small gaps in lines

							/*if (v > 0 && (theta > oldTheta)) {
								Vector2 t1 = v1;
								Vector2 t2 = new Vector2((float) (-0.1 * Math.cos(beta)) + v1.x,
										(float) (-0.1 * Math.sin(beta)) + v1.y);
								Vector2 t3 = oldVert;
								float minX, minY, maxHeight, maxWidth;
								// find min x and y val of 3 verts
								if (t1.x < t2.x && t1.x < t3.x) {
									minX = t1.x;
								} else if (t2.x < t1.x && t2.x < t3.x) {
									minX = t2.x;
								} else {
									minX = t3.x;
								}
								if (t1.y < t2.y && t1.y < t3.y) {
									minY = t1.y;
								} else if (t2.y < t1.y && t2.y < t3.y) {
									minY = t2.y;
								} else {
									minY = t3.y;
								}
								// find height and width of triangle to draw
								// pixmap
								maxWidth = Math.abs(t2.x - t1.x);
								if (Math.abs(t3.x - t2.x) > maxWidth) {
									maxWidth = Math.abs(t3.x - t2.x);
								}
								if (Math.abs(t1.x - t3.x) > maxWidth) {
									maxWidth = Math.abs(t1.x - t3.x);
								}
								maxHeight = Math.abs(t2.y - t1.y);
								if (Math.abs(t3.y - t2.y) > maxHeight) {
									maxHeight = Math.abs(t3.y - t2.y);
								}
								if (Math.abs(t1.y - t3.y) > maxHeight) {
									maxHeight = Math.abs(t1.y - t3.y);
								}
								// maxHeight = maxHeight / 2;
								pixmap = new Pixmap((int) (maxWidth * 200), (int) (maxHeight * 200), Format.RGBA8888);
								pixmap.setColor(0, 0, 0, 0f);
								pixmap.fill();
								pixmap.setColor(Color.BLACK);
								pixmap.fillTriangle((int) ((t1.x - minX) * 200), (int) ((t1.y - minY) * 200),
										(int) ((t2.x - minX) * 200), (int) ((t2.y - minY) * 200),
										(int) ((t3.x - minX) * 200), (int) ((t3.y - minY) * 200));
								Texture triangle = new Texture(pixmap);

								pixmap.dispose();
								TextureRegion triReg = new TextureRegion(triangle);
								triReg.flip(false, true);

								if (a.isHidden()) {
									addOnStart.add(new SimpleSpatial(line, false, body, Color.WHITE,
											new Vector2(dist, 0.1f), new Vector2(xMid, yMid),
											(float) (theta * MathUtils.radiansToDegrees)));
								} else {
									mSpatials.add(new SimpleSpatial(triReg, false, body, Color.WHITE,
											new Vector2(maxWidth, maxHeight),
											new Vector2(minX + maxWidth / 2, minY + maxHeight / 2), 0));
								}

							}*/

							oldVert.x = (float) (-0.1 * Math.cos(beta) + v2.x);
							oldVert.y = (float) (-0.1 * Math.sin(beta) + v2.y);
							oldTheta = theta;

						}
					}
				}
			}

		}

	}
	public void checkTutorial() {
		
		if (((Boolean) getGame().loader.nestedLevelProfile.get(LevelScreen.currentLevel).get("firstLoad")) == true) {
			getGame().loader.setLevelData(LevelScreen.currentLevel, "firstLoad", false);
			System.out.print("FIRST LOAD");
			imageNames.clear();
			for (int i = 1; i < 50; i++) {
				if (Gdx.files.internal(("img/" + LevelScreen.currentLevel + i + ".png")).exists()) {
					// load tutorial images automatically on first ever level
					// load
					
					System.out.print("img/" + LevelScreen.currentLevel + i + ".png");
					imageNames.add("img/" + LevelScreen.currentLevel + i + ".png");
				}

				else {
					break;
				}

			}
			getGame().manager.get("TutorialOverlay", TutorialOverlay.class).loadImages(imageNames);
			getGame().manager.get("TutorialOverlay", TutorialOverlay.class).addToStage();
		}
	}
	

	/**
	 * Use an accumulator to ensure a fixed delta for physics simulation...
	 * 
	 * @param delta
	 */
	private void updatePhysics(float delta) {
		mAccumulator += delta;
		while (mAccumulator >= mSecondsPerStep) {
			mWorld.step(mSecondsPerStep, mVelocityIter, mPositionIter);
			mAccumulator -= mSecondsPerStep;
		}
	}

	/**
	 * Perform all world rendering...
	 * 
	 * @param delta
	 */
	private void renderWorld(float delta) {
		mBatch.setProjectionMatrix(uiStage.getCamera().projection);
		mBatch.begin();
		mBatch.draw(gradient, -screenWidth / 2, -screenHeight / 2, screenWidth, screenHeight);
		mBatch.end();
		
		mPolyBatch.setProjectionMatrix(uiStage.camera.combined);
		rbg2.draw(uiStage.camera, mBatch);
		
		if ((mSpatials != null) && (mSpatials.size > 0)) {
			mBatch.setProjectionMatrix(uiStage.camera.combined);
			mBatch.begin();
			for (int i = 0; i < mSpatials.size; i++) {
				mSpatials.get(i).render(mBatch, delta);
			}
			mBatch.end();
		}

		if ((mPolySpatials != null) && (mPolySpatials.size > 0)) {
			mPolyBatch.setProjectionMatrix(uiStage.camera.combined);
			mPolyBatch.begin();
			for (int i = 0; i < mPolySpatials.size; i++) {
				mPolySpatials.get(i).render(mPolyBatch, 0);
			}
			mPolyBatch.end();
		}

		mBatch.setProjectionMatrix(uiStage.getCamera().projection);
		mBatch.begin();
		smallFont.draw(mBatch, "fps: " + Gdx.graphics.getFramesPerSecond(), -Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() / 2);
		Array<Body> bodies = new Array<Body>();
		this.mWorld.getBodies(bodies);
		mBatch.setProjectionMatrix(uiStage.camera.combined);
		for (int i = 0; i < bodies.size; i++) {
			if (bodies.get(i).getUserData() != null && bodies.get(i).getUserData().getClass() == ParticleEffect.class) {
				ParticleEffect effect = (ParticleEffect) bodies.get(i).getUserData();
				if (effect != null) {
					Vector3 v = new Vector3(bodies.get(i).getPosition(), 0);
					// v = uiStage.camera.project(v);
					// v.x -= Gdx.graphics.getWidth() / 2;
					// v.y -= Gdx.graphics.getHeight() / 2;
					effect.setPosition(v.x, v.y);
					effect.draw(mBatch, delta);
					if (effect == spawnParticle && effect.isComplete()) {
						spawnParticle.dispose();
						spawnParticle = null;
					}
				}
			}
		}
		mBatch.end();
	}

	/**
	 * Creates an array of SimpleSpatial objects from RubeImages.
	 * 
	 * @param scene2
	 */
	private void createSpatialsFromRubeImages(RubeScene scene) {
		Array<RubeImage> images = scene.getImages();
		if ((images != null) && (images.size > 0)) {
			// mSpatials = new Array<SimpleSpatial>();
			String playerType = "test";
			TextureAtlas atlas;
			// atlas = new
			// TextureAtlas(Gdx.files.internal("packs/"+playerType+".pack"));
			atlas = game.manager.get("packs/test.pack", TextureAtlas.class);

			for (int i = 0; i < images.size; i++) {
				RubeImage image = images.get(i);
				spatialPos.set(image.width, image.height);
				String textureFileName = image.file;
				textureFileName = textureFileName.substring(textureFileName.lastIndexOf("/") + 1);
				textureFileName = textureFileName.replaceFirst("[.][^.]+$", "");

				if (scene.getCustom(image.body, "type") != null) {
					if (scene.getCustom(image.body, "type").equals("coin")) {
						Integer coinIndex = (Integer) scene.getCustom(image.body, "coinNumber", 0);
						if (((Array<Float>) game.loader.nestedLevelProfile.get(LevelScreen.currentLevel).get("coins"))
								.get(coinIndex) == 1) {
							textureFileName = "dataCollected";
						}

					}
				}

				Texture texture = new Texture(Gdx.files.internal("img/" + textureFileName + ".png"));
				texture.setFilter(TextureFilter.Nearest, TextureFilter.Linear);
				/*
				 * if (texture == null) { texture = new
				 * Texture(textureFileName); (mTextureMap).put(textureFileName,
				 * texture); }
				 */
				boolean isItPlayer = false;
				for (String s : player.playerImageNames) {
					if (image.file.contains(s)) {
						isItPlayer = true;
					}
				}
				// commented out to figure out bgs
				System.out.println("TEXTURE NAME: " +textureFileName);
				if (textureFileName.equals("portal")  || textureFileName.equals("booster") || textureFileName.equals("fire")) {
					//skip
				}
				else {
					SimpleSpatial spatial = new SimpleSpatial(texture, image.flip, image.body, image.color, spatialPos,
							image.center, image.angleInRads * MathUtils.radiansToDegrees);
					if (isItPlayer) {
						playerSpatials.add(spatial);
						playerBodies.add(image.body);
					}
					else if (textureFileName.equals("axishash")) {
						hashSpatials.add(spatial);
						//System.out.println("CAUTION SPATIALS SIZE: " + cautionSpatials.size);
					}
					mSpatials.add(spatial);
					if (textureFileName.equals("dataCoin") || textureFileName.equals("dataCollected")) {
						Integer coinIndex = (Integer) scene.getCustom(image.body, "coinNumber", 0);
						System.out.print("coin number#: " + coinIndex);
						// create a map of (RUBE index, mSpatial index) to
						// destroy
						// images when collected
						coinMap.put(coinIndex, spatial);
					
					}
				}

			}
		}
	}

	/**
	 * Creates an array of PolySpatials based on fixture information from the
	 * scene. Note that fixtures create aligned textures.
	 * 
	 * @param scene
	 */
	private void createPolySpatialsFromRubeFixtures(RubeScene scene) {
		Array<Body> bodies = scene.getBodies();

		EarClippingTriangulator ect = new EarClippingTriangulator();

		if ((bodies != null) && (bodies.size > 0)) {
			mPolySpatials = new Array<PolySpatial>();
			Vector2 bodyPos = new Vector2();
			// for each body in the scene...
			for (int i = 0; i < bodies.size; i++) {
				Body body = bodies.get(i);
				bodyPos.set(body.getPosition());
				float bodyAngle = body.getAngle() * MathUtils.radiansToDegrees;

				Array<Fixture> fixtures = body.getFixtureList();

				if ((fixtures != null) && (fixtures.size > 0)) {
					// for each fixture on the body...
					for (int j = 0; j < fixtures.size; j++) {
						Fixture fixture = fixtures.get(j);

						String textureName = (String) scene.getCustom(fixture, "TextureMask", null);
						if (textureName != null) {
							String textureFileName = "data/" + textureName;
							Texture texture = mTextureMap.get(textureFileName);
							TextureRegion textureRegion = null;
							if (texture == null) {
								texture = new Texture(textureFileName);
								texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
								mTextureMap.put(textureFileName, texture);
								textureRegion = new TextureRegion(texture);
								mTextureRegionMap.put(texture, textureRegion);
							} else {
								textureRegion = mTextureRegionMap.get(texture);
							}

							// only handle polygons at this point -- no chain,
							// edge, or circle fixtures.
							if (fixture.getType() == Shape.Type.Polygon) {
								PolygonShape shape = (PolygonShape) fixture.getShape();
								int vertexCount = shape.getVertexCount();
								float[] vertices = new float[vertexCount * 2];

								// static bodies are texture aligned and do not
								// get drawn based off of the related body.
								if (body.getType() == BodyType.StaticBody) {
									for (int k = 0; k < vertexCount; k++) {

										shape.getVertex(k, spatialPos);
										spatialPos.rotate(bodyAngle);
										spatialPos.add(bodyPos);
										// convert local coordinates to world coordinates to that textures are aligned
										vertices[k * 2] = spatialPos.x * PolySpatial.PIXELS_PER_METER;
										vertices[k * 2 + 1] = spatialPos.y * PolySpatial.PIXELS_PER_METER;
									}

									short[] triangleIndices = ect.computeTriangles(vertices).toArray();
									PolygonRegion region = new PolygonRegion(textureRegion, vertices, triangleIndices);
									PolySpatial spatial = new PolySpatial(region, Color.WHITE);
									mPolySpatials.add(spatial);
								} else {
									// all other fixtures are aligned based on
									// their associated body.
									for (int k = 0; k < vertexCount; k++) {
										shape.getVertex(k, spatialPos);
										vertices[k * 2] = spatialPos.x * PolySpatial.PIXELS_PER_METER;
										vertices[k * 2 + 1] = spatialPos.y * PolySpatial.PIXELS_PER_METER;
									}
									short[] triangleIndices = ect.computeTriangles(vertices).toArray();
									PolygonRegion region = new PolygonRegion(textureRegion, vertices, triangleIndices);
									PolySpatial spatial = new PolySpatial(region, body, Color.WHITE);
									mPolySpatials.add(spatial);
								}
							} else if (fixture.getType() == Shape.Type.Circle) {
								CircleShape shape = (CircleShape) fixture.getShape();
								float radius = shape.getRadius();
								int vertexCount = (int) (12f * radius);
								float[] vertices = new float[vertexCount * 2];
								if (body.getType() == BodyType.StaticBody) {
									polyPos.set(shape.getPosition());
									for (int k = 0; k < vertexCount; k++) {
										// set the initial position
										spatialPos.set(radius, 0);
										// rotate it by 1/vertexCount * k
										spatialPos.rotate(360f * k / vertexCount);
										// add it to the position.
										spatialPos.add(polyPos);
										spatialPos.rotate(bodyAngle);
										spatialPos.add(bodyPos); // convert local
															// coordinates to
															// world coordinates
															// so that textures
															// are aligned
										vertices[k * 2] = spatialPos.x * PolySpatial.PIXELS_PER_METER;
										vertices[k * 2 + 1] = spatialPos.y * PolySpatial.PIXELS_PER_METER;
									}
									short[] triangleIndices = ect.computeTriangles(vertices).toArray();
									PolygonRegion region = new PolygonRegion(textureRegion, vertices, triangleIndices);
									PolySpatial spatial = new PolySpatial(region, Color.WHITE);
									mPolySpatials.add(spatial);
								} else {
									polyPos.set(shape.getPosition());
									for (int k = 0; k < vertexCount; k++) {
										// set the initial position
										spatialPos.set(radius, 0);
										// rotate it by 1/vertexCount * k
										spatialPos.rotate(360f * k / vertexCount);
										// add it to the position.
										spatialPos.add(polyPos);
										vertices[k * 2] = spatialPos.x * PolySpatial.PIXELS_PER_METER;
										vertices[k * 2 + 1] = spatialPos.y * PolySpatial.PIXELS_PER_METER;
									}
									short[] triangleIndices = ect.computeTriangles(vertices).toArray();
									PolygonRegion region = new PolygonRegion(textureRegion, vertices, triangleIndices);
									PolySpatial spatial = new PolySpatial(region, body, Color.WHITE);
									mPolySpatials.add(spatial);
								}
							}
						}
					}
				}
			}
		}
	}



	/**
	 * @return the mScene
	 */
	public RubeScene getmScene() {
		return mScene;
	}

	/**
	 * @param mScene
	 *            the mScene to set
	 */
	public void setmScene(RubeScene mScene) {
		this.mScene = mScene;
	}

	/**
	 * @return the axes
	 */
	public Array<Axis> getAxes() {
		return axes;
	}

	/**
	 * @param axes
	 *            the axes to set
	 */
	public void setAxes(Array<Axis> axes) {
		this.axes = axes;
	}

	public void setGameMode(String gameMode) {
		getUiStage().gameMode = gameMode;
	}

	public String getGameMode() {
		return getUiStage().gameMode;
	}

	public Array<Equation> getEquationsFromAxes() {
		Array<Equation> equations = new Array<Equation>();
		for (int i = axes.size; i > 0; i--) {
			for (Axis e : axes) {
				if (e.getNum() == i) {
					equations.add(e.getEquation());
				}
			}
		}
		return equations;
	}

	public void destroyBody(Body body) {
		mWorld.destroyBody(body);
	}

	/**
	 * @return the uiStage
	 */
	public UIStage getUiStage() {
		return uiStage;
	}

	/**
	 * @param uiStage
	 *            the uiStage to set
	 */
	public void setUiStage(UIStage uiStage) {
		this.uiStage = uiStage;
	}

	/**
	 * @return the mState
	 */
	public GAME_STATE getmState() {
		return mState;
	}

	/**
	 * @param mState
	 *            the mState to set
	 */
	public void setmState(GAME_STATE mState) {
		this.mState = mState;
	}

	public void updateCamera(float delta) {
		if (cameraLock) {
			return;
		}
		//initial positioned on player
		playerCameraPosition.set(getPlayer().getPlayer().getPosition().x + 3.5f,
				getPlayer().getPlayer().getPosition().y);
		
		//follow player once start
		if (cameraFollow) {
			moveCameraAndZoom(playerCameraPosition, 0.8f * ratioZoom, 0.5f, 0.5f);
			uiStage.camera.update();
		}
		
		//for scanning level using left and right buttons
		if (snapMode) {
			if ((int) focus != 0 && (int) focus != focusPoints.size - 1) {
				moveCameraAndZoom(new Vector2(3.5f + focusPoints.get((int) focus).x + axes.get((int) focus - 1).getAxisZoom() * 3,
								focusPoints.get((int) focus).y),
						1.8f * ratioZoom, 0.15f, 0.05f);
					//renderAxes(true);

			} else {
				moveCameraAndZoom(focusPoints.get((int) focus), 1.8f * ratioZoom, 0.15f, 0.05f);
			}
			uiStage.camera.update();
		}
		// not snap mode, determining focus for left and right arrows
		if (uiStage.getFling()) {
			uiStage.camera.position.add(new Vector3(uiStage.cameraVelocity, 0));
			uiStage.camera.update();
			uiStage.cameraVelocity = applyAcceleration(uiStage.cameraVelocity, -0.1f * uiStage.cameraVelocity.x,
					-0.1f * uiStage.cameraVelocity.y);
			if (uiStage.cameraVelocity.y == 0 && uiStage.cameraVelocity.x == 0) {
				uiStage.setFling(false);
			}
		}
		if (uiStage.camera.position.x < bounds.x) {
			if (cameraFollow == true) {
				getPlayer().setDead(true);
			}
			uiStage.camera.position.x = bounds.x;
		}
		if (uiStage.camera.position.x > bounds.x + bounds.width) {
			if (cameraFollow == true) {
				getPlayer().setDead(true);
			}
			uiStage.camera.position.x = bounds.width + bounds.x;
		}
		if (uiStage.camera.position.y < bounds.y) {
			if (cameraFollow == true) {
				getPlayer().setDead(true);
			}
			uiStage.camera.position.y = bounds.y;
		}
		if (uiStage.camera.position.y > bounds.y + bounds.height) {
			if (cameraFollow == true) {
				getPlayer().setDead(true);
			}
			uiStage.camera.position.y = bounds.height + bounds.y;
		}
		uiStage.camera.update(true);
	}

	public String milToMinSecMil(long x) {
		if (x > 60 * 1000) {
			long m = x / 1000 / 60;
			long s = x / 1000 - m * 60;
			long mi = Math.round((x - s * 1000 - m * 1000 * 60) / 10);
			if (Long.toString(m).length() < 2) {
				if (Long.toString(s).length() < 2) {
					if (Long.toString(mi).length() < 2) {
						return "0" + m + ":0" + s + ":0" + mi;
					} else {
						return "0" + m + ":0" + s + ":" + mi;
					}

				} else {
					if (Long.toString(mi).length() < 2) {
						return "0" + m + ":" + s + ":0" + mi;
					} else {
						return "0" + m + ":" + s + ":" + mi;
					}
				}
			} else {
				if (Long.toString(s).length() < 2) {
					if (Long.toString(mi).length() < 2) {
						return m + ":0" + s + ":0" + mi;
					} else {
						return m + ":0" + s + ":" + mi;
					}

				} else {
					if (Long.toString(mi).length() < 2) {
						return m + ":" + s + ":0" + mi;
					} else {
						return m + ":" + s + ":" + mi;
					}
				}
			}
		} else if (x > 1000) {
			long s = x / 1000;
			long mi = Math.round((x - s * 1000) / 10);
			if (Long.toString(s).length() < 2) {
				if (Long.toString(mi).length() < 2) {
					return "00:0" + s + ":0" + mi;
				}
				return "00:0" + s + ":" + mi;
			}
			return "00:" + s + ":" + mi;
		} else {
			return "00:00:" + Math.round(x) / 10;
		}
	}

	private void moveCameraAndZoom(Vector2 position, float desiredZoom, float moveSpeed, float zoomSpeed) {
		moveCamera(position, moveSpeed);
		changeZoom(desiredZoom, zoomSpeed);
		uiStage.camera.update();
	}

	private void moveCamera(Vector2 position, float speed) {
		if (position.dst(new Vector2(uiStage.camera.position.x, uiStage.camera.position.y)) < 0.5) {
			uiStage.camera.position.set(new Vector3(position, 0f));
			if (uiStage.movingToAxis) {
				uiStage.movingToAxis = false;
			}
		}
		uiStage.camera.position.lerp(new Vector3(position.x, position.y, 0f), speed);
	}

	public void changeZoom(float desiredZoom, float speed) {
		uiStage.camera.zoom = (MathUtils.lerp(uiStage.camera.zoom, desiredZoom, speed));
	}

	public void applyBlackHole() {

	}

	private Vector2 applyAcceleration(Vector2 velocity, float accelerationX, float accelerationY) {
		velocity.x += accelerationX;
		velocity.y += accelerationY;
		if (accelerationX < 0 && velocity.x < 0) {
			velocity.x = 0;
		}
		if (accelerationX > 0 && velocity.x > 0) {
			velocity.x = 0;
		}
		if (accelerationY < 0 && velocity.y < 0) {
			velocity.y = 0;
		}
		if (accelerationY > 0 && velocity.y > 0) {
			velocity.y = 0;
		}
		return velocity;
	}

	public void coinLabel (boolean firstTime) {
		float labelX = uiStage.camera.project(new Vector3(player.getPlayer().getPosition(), 0)).x + screenWidth / 30;
		float labelY = uiStage.camera.project(new Vector3(player.getPlayer().getPosition(), 0)).y + screenWidth / 20;
		
		trickLabel = new TrickLabel("", skin.get("flipStyle", LabelStyle.class));
		
		labelCounter += 1;
		if (labelCounter > 1) {
			labelY += 160;
			labelX += 100;
		}
		
		if (firstTime) {
			trickLabel.activateLabel(uiStage, labelX, labelY, "Data Bit Found!\n      +1000");

		} else {
			trickLabel.activateLabel(uiStage, labelX, labelY, "Data Bit Found!");

		}

	}

	public void flipAction(Float front, Float back) {
		if (player.isDead()) {
			return;
		}
		if (front < 1 && front > -1 && back < 1 && back > -1) {
			return;
		}
		
		float labelX = uiStage.camera.project(new Vector3(player.getPlayer().getPosition(), 0)).x + screenWidth / 30;
		float labelY = uiStage.camera.project(new Vector3(player.getPlayer().getPosition(), 0)).y + screenWidth / 20;
		
		trickLabel = new TrickLabel("", skin.get("flipStyle", LabelStyle.class));
		labelCounter += 1;
		
		if (labelCounter > 1) {
			labelY -= 120;
			labelX += 50;
		}
		
		if (front >= 1 && back >= 1) {
			trickLabel.activateLabel(uiStage, labelX, labelY, "Front & Back Flip!\n     +500");
			scoreCounter += 500;
			getGame().fxManager.play("Sound/flip_reward.ogg");
		} 
		else if (front >= 3) {
			trickLabel.activateLabel(uiStage, labelX, labelY, "Triple Front Flip!\n      +225");
			scoreCounter += 225;
			getGame().fxManager.play("Sound/flip_reward.ogg");
		} 
		else if (front >= 2 && front < 3) {
			trickLabel.activateLabel(uiStage, labelX, labelY, "Double Front Flip!\n      +150");
			scoreCounter += 150;
			getGame().fxManager.play("Sound/flip_reward.ogg");

		} 
		else if (front >= 1 && front < 2) {
			trickLabel.activateLabel(uiStage, labelX, labelY, "Front Flip!\n   +75");
			scoreCounter += 75;
			getGame().fxManager.play("Sound/flip_reward.ogg");
		}

		else if (back >= 3) {
			trickLabel.activateLabel(uiStage, labelX, labelY, "Triple Back Flip!\n      +150");
			scoreCounter += 150;
			getGame().fxManager.play("Sound/flip_reward.ogg");
		} 
		else if (back >= 2 && back < 3) {
			trickLabel.activateLabel(uiStage, labelX, labelY, "Double Back Flip!\n      +100");
			scoreCounter += 100;
			getGame().fxManager.play("Sound/flip_reward.ogg");
		} 
		else if (back >= 1 && back < 2) {
			trickLabel.activateLabel(uiStage, labelX, labelY, "Back Flip!\n   +50");
			scoreCounter += 50;
			getGame().fxManager.play("Sound/flip_reward.ogg");
		}

	}

	public void levelComplete() {
		// just a test for unlocking
		System.out.println("LEVELPACK: " + LevelScreen.stringPack + " BONUS COINS COLLECTED: "
				+ (Integer) game.loader.getLevelProfile(LevelScreen.currentLevel).get("coinsCollected"));
		int tmp = (Integer) game.loader.nestedLevelProfile.get(LevelScreen.currentLevel).get("coinsCollected");
		game.loader.setLevelData(LevelScreen.currentLevel, "coinsCollected", tmp + bonusCoins);

		int coinScore = bonusCoins * 1000;
		int intLevelPack = 0;
		String str = String.valueOf(LevelScreen.currentLevel.charAt(0));
		if (str.equals("a")) { intLevelPack = 1; }
		else if (str.equals("b")) { intLevelPack = 2; }
		else if (str.equals("c")) { intLevelPack = 3; }
		else if (str.equals("d")) { intLevelPack = 4; }
		else if (str.equals("e")) { intLevelPack = 5; }
		else if (str.equals("f")) { intLevelPack = 6; }
		else if (str.equals("g")) { intLevelPack = 7; }
		int levelScore = intLevelPack * 50 + LevelScreen.intLevel * 25;
		
		int equationsUnlocked = (Integer) game.loader.getProfile().get("unlockedEquations");
		if (LevelScreen.currentLevel.equals("b8") && equationsUnlocked < 1) {
			game.loader.newProfileData("unlockedEquations", 1);
			game.manager.get("PreGameOverlay", PreGameOverlay.class).createTopRightButtons();
		}
		else if (LevelScreen.currentLevel.equals("d8") && equationsUnlocked < 2) {
			game.loader.newProfileData("unlockedEquations", 2);
			game.manager.get("PreGameOverlay", PreGameOverlay.class).createTopRightButtons();
		}

		finalTime = TimeUtils.timeSinceMillis(startTime - (startPause - endPause));

		game.manager.get("WinOverlay", WinOverlay.class).setLabelTimes(finalTime, 10);
		// include in win overlay and add timer??
		System.out.print("TOTAL SCORE: " + scoreCounter);
		Integer coinHolder = (Integer) game.loader.getProfile().get("coins");
		game.loader.newProfileData("coins", coinHolder + scoreCounter + levelScore + coinScore);
		// complete level and update highscore

		int oldHighScore = (Integer) game.loader.nestedLevelProfile.get(LevelScreen.currentLevel).get("highScore");
		if (scoreCounter > oldHighScore) {
			game.loader.setLevelData(LevelScreen.currentLevel, "highScore", scoreCounter + levelScore);
			// pass whether it is high score to win overlay
			WinOverlay.setIsHighScore(true);
		}

		else {
			WinOverlay.setIsHighScore(false);
		}

		game.manager.get("WinOverlay", WinOverlay.class).setScoreCounter(scoreCounter);
		game.manager.get("WinOverlay", WinOverlay.class).setScores(levelScore, scoreCounter, coinScore);

		// determine next level as a string
		String nextLevel = LevelScreen.stringPack + String.valueOf(LevelScreen.intLevel + 1);
		if (game.loader.nestedLevelProfile.get(nextLevel) == null) {
			char temp = LevelScreen.currentLevel.charAt(0);
			temp++;
			nextLevel = String.valueOf(temp) + 1;
			game.loader.setLevelData((String.valueOf(temp) + 0), "unlocked", true);

		}
		// update level pack data
		String levelDataKey = LevelScreen.stringPack + "0";
		if ((Boolean) game.loader.nestedLevelProfile.get(LevelScreen.currentLevel).get("completed") == false) {
			int oldLvlsCompleted = (Integer) (game.loader.nestedLevelProfile.get(levelDataKey).get("levelsCompleted"));
			game.loader.setLevelData(levelDataKey, "levelsCompleted", oldLvlsCompleted + 1);
		}

		game.loader.setLevelData(LevelScreen.currentLevel, "coins", getCoinsCollected(), false);
		int oldCoinsCollected = (Integer) (game.loader.nestedLevelProfile.get(levelDataKey).get("coinsCollected"));
		game.loader.setLevelData(levelDataKey, "coinsCollected", oldCoinsCollected + bonusCoins);

		game.loader.setLevelData(levelDataKey, "totalData", bonusCoins);
		game.loader.newProfileData("totalData", (Integer) game.loader.profile.get("totalData") + bonusCoins);

		game.loader.setLevelData(LevelScreen.currentLevel, "completed", true);
		game.loader.setLevelData(nextLevel, "unlocked", true);

	}

	private void destroyJoints(Array<Joint> joints) {
		for (int i = 0; i < joints.size; i++) {
			Joint joint = joints.get(i);
			mWorld.destroyJoint(joint);
			joints.removeIndex(i);
		}
	}

	public Array<SimpleSpatial> getmSpatials() {
		return mSpatials;
	}

	public void addToDestroy(Body b) {
		getToDestroy().add(b);
	}

	public Boolean isPlayer (Body body) {
		return playerBodies.contains(body, true);
		//return getPlayer().getPlayerBodies().contains(body, true);
	}
	public Boolean isPlayer (Fixture fixture) {
		return playerBodies.contains(fixture.getBody(), true);
	}


	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @param player
	 *            the player to set
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * @return the rbg
	 */

	/**
	 * @return the game
	 */
	public AgentExGame getGame() {
		return game;
	}

	/**
	 * @param game
	 *            the game to set
	 */
	public void setGame(AgentExGame game) {
		this.game = game;
	}

	/**
	 * @return the winState
	 */
	public boolean isWinState() {
		return winState;
	}

	/**
	 * @param winState
	 *            the winState to set
	 */
	public void setWinState(boolean winState) {
		this.winState = winState;
	}

	public void setFirstTime(int var) {
		this.firstTime = var;
	}

	public Array<Float> getCoinsCollected() {
		return coinsCollected;
	}
	
	public Map<Integer, SimpleSpatial> getCoinMap() {
		return coinMap;
	}

	public void setCoinsCollected(Array<Float> coinsCollected) {
		this.coinsCollected = coinsCollected;
	}

	public void destroy (Body body) {
		if (!toDestroy.contains(body, true))
			toDestroy.add(body);
	}
	
	public Array<Body> getToDestroy() {
		return toDestroy;
	}

	public void setToDestroy(Array<Body> toDestroy) {
		this.toDestroy = toDestroy;
	}

	public Array<Integer> getCoinsToRemove() {
		return coinsToRemove;
	}

	public void setCoinsToRemove(Array<Integer> coinsToRemove) {
		this.coinsToRemove = coinsToRemove;
	}

	public Array<ParticleEffect> getBoostParticles() {
		return boostParticles;
	}

	public void setBoostParticles(Array<ParticleEffect> boostParticles) {
		this.boostParticles = boostParticles;
	}

	public Array<ParticleEffect> getFireParticles() {
		return fireParticles;
	}

	public void setFireParticles(Array<ParticleEffect> fireParticles) {
		this.fireParticles = fireParticles;
	}

	public ParticleEffect getPortalParticle() {
		return portalParticle;
	}

	public void setPortalParticle(ParticleEffect portalParticle) {
		this.portalParticle = portalParticle;
	}

}
