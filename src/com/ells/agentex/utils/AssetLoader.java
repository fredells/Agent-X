package com.ells.agentex.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.TimeUtils;
import com.ells.agentex.AgentExGame;
import com.ells.agentex.actors.TrickLabel;
import com.ells.agentex.screens.LevelScreen.LEVEL_PACK_LETTER;

public class AssetLoader {

	public float screenWidth = Gdx.graphics.getWidth();
	public float screenHeight = Gdx.graphics.getHeight();
	// public static
	public Pixmap pixmap;

	// prefs and save data
	public Preferences prefs;
	private FileHandle playerData;
	private FileHandle levelData;
	private FileHandle vehicleData;
	private FileHandle skinData;
	public Map<String, Object> profile = new HashMap<String, Object>();
	// public static Map<String, Map<String, Object>> levelProfile = new
	// HashMap<String, Map<String, Object>>();
	public Map<String, Object> levelProfile = new HashMap<String, Object>();
	public Map<String, Map<String, Object>> nestedLevelProfile = new HashMap<String, Map<String, Object>>();
	public Map<String, Object> vehicleProfile = new HashMap<String, Object>();

	public Skin skin;
	public TextureAtlas testAtlas;
	public TextureAtlas uiSkinAtlas;
	// should be added to assetLoader
	// public WinOverlay winOverlay;
	// public PauseOverlay pauseOverlay;
	// public InGameOverlay inGameOverlay;
	// public PreGameOverlay preGameOverlay;
	// public DeathOverlay deathOverlay;
	// public TutorialOverlay tutorialOverlay;

	public boolean loadedTrickLabels = false;
	public Image darkness;
	public AssetManager assetManager;
	public boolean loaded = false;
	private AgentExGame game;

	public boolean darkActive = false;
	private PixmapPacker packer = new PixmapPacker(512, 512, Pixmap.Format.RGBA8888, 2, false);

	public Array<FreeTypeFontLoaderParameter> parameters = new Array<FreeTypeFontLoaderParameter>();
	public Array<String> fontNames = new Array<String>();
	public Array<Integer> fontSizes = new Array<Integer>();
	public boolean afterFontsComplete = false;
	public ObjectMap<String, BitmapFont> fonts;
	public Array<TrickLabel> tricks = new Array<TrickLabel>();

	// load assets
	public AssetLoader() {
		// white title
		fontNames.add("largeFont");
		fontNames.add("tableFont");
		fontNames.add("inGameButtonFont");
		fontNames.add("topRightButtonFont");
		// black subtitle and buttons
		fontNames.add("titleFont");
		fontNames.add("scoreFont");
		fontNames.add("deathFont");
		fontNames.add("bebasBold");
		fontNames.add("bebasBook");
		fontNames.add("bebasBoldBig");
		fontNames.add("linear");
		fontNames.add("exponent");
		fontNames.add("flipFont");
	}

	public void load(AgentExGame game) {
		long timeOne = TimeUtils.millis();
		this.game = game;
		assetManager = game.manager;
		createAndroidFiles();

		// create fonts
		// Create (or retrieve existing) preferences file
		prefs = Gdx.app.getPreferences("agentx");
		// clear prefs for testing first startup
		// prefs.clear();

		// set default sound prefs to on
		if (!prefs.contains("sound")) {
			prefs.putBoolean("sound", true);
			prefs.flush();
		}
		// set default music prefs to on
		if (!prefs.contains("music")) {
			prefs.putBoolean("music", true);
			prefs.flush();
		}

		// set save file to 0 if none
		if (!prefs.contains("activeSave")) {
			prefs.putString("activeSave", "1");
			prefs.flush();
		}

		playerData = Gdx.files.local("save/save" + getSave() + ".json");
		//Gdx.app.log("Agent X", playerData.file().getAbsolutePath() + " exists: " + playerData.exists());
		Json json = new Json();
		// profile = json.fromJson(HashMap.class,
		// Base64Coder.decodeString(playerData.readString()));
		profile = json.fromJson(HashMap.class, playerData.readString());
		// System.out.print(profile);
		// build level profile
		// levelData = Gdx.files.local("save/levels" + getSave() + ".json");
		// profile = json.fromJson(HashMap.class,
		// Base64Coder.decodeString(playerData.readString()));
		// levelProfile = json.fromJson(HashMap.class, levelData.readString());

		// build nested level data map
		String[] indeces = { "a", "b", "c", "d", "e", "f", "g" }; // , "c", "d", "e"};
		for (int i = 0; i < indeces.length; i++) {
			// System.out.print(indeces[i]);
			for (int level = 0; level < 9; level++) {
				String key = indeces[i] + String.valueOf(level);
				levelData = Gdx.files.local("save/savedata" + getSave() + "/" + key + ".json");
				//Gdx.app.log("Agent X", levelData.file().getAbsolutePath() + " exists: " + levelData.exists());
				levelProfile = json.fromJson(HashMap.class, levelData.readString());
				nestedLevelProfile.put(key, levelProfile);
				// System.out.print(nestedLevelData);
			}
			// vehicles for now
			/*
			 * levelData = Gdx.files.local("save/savedata" + getSave() + "/" +
			 * "v1" + ".json"); levelProfile = json.fromJson(HashMap.class,
			 * levelData.readString()); nestedLevelProfile.put("v1",
			 * levelProfile);
			 * 
			 * levelData = Gdx.files.local("save/savedata" + getSave() + "/" +
			 * "v2" + ".json"); levelProfile = json.fromJson(HashMap.class,
			 * levelData.readString()); nestedLevelProfile.put("v2",
			 * levelProfile);
			 */
		}
		for (int v = 1; v < 13; v++) {
			levelData = Gdx.files.local("save/savedata" + getSave() + "/v" + v + ".json");
			//Gdx.app.log("Agent X", levelData.file().getAbsolutePath() + " exists: " + levelData.exists());
			levelProfile = json.fromJson(HashMap.class, levelData.readString());
			nestedLevelProfile.put("v" + String.valueOf(v), levelProfile);
		}

		// int[] test = {1,2,3};
		/*
		 * skinData = Gdx.files.local("save/skin" + getSave() + ".json");
		 * vehicleProfile.put("coins", test);
		 * json.setOutputType(OutputType.json);
		 * skinData.writeString(json.prettyPrint(vehicleProfile), false);
		 */

		// setLevelData("a1", "coins", test);
		// setLevelData("a1", "completed", true);
		loaded = true;
		Gdx.app.log("time", (TimeUtils.millis() - timeOne) + " main Load");
	}

	public void dispose() {
		assetManager.clear();
	}

	/**
	 * public void LoadFontsFromTTF(AssetManager m) { FileHandleResolver
	 * resolver = new InternalFileHandleResolver();
	 * m.setLoader(FreeTypeFontGenerator.class, new
	 * FreeTypeFontGeneratorLoader(resolver)); m.setLoader(BitmapFont.class,
	 * ".ttf", new FreetypeFontLoader(resolver)); for (int i = 0; i <
	 * fontNames.size; i++) { m.load(fontNames.get(i), BitmapFont.class,
	 * parameters.get(i)); } m.finishLoading(); }
	 **/
	public void GenerateFontParameters() {
		long timeOne = TimeUtils.millis();
		int tmp = 3400;
		FreeTypeFontLoaderParameter param1 = new FreeTypeFontLoaderParameter();
		param1.fontParameters.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
		param1.fontFileName = "fonts/muro slant.ttf";
		param1.fontParameters.size = (int) Math.ceil(tmp*65/screenWidth);
		param1.fontParameters.color = Color.WHITE;

		// table font
		FreeTypeFontLoaderParameter param2 = new FreeTypeFontLoaderParameter();
		param2.fontParameters.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
		param2.fontFileName = "fonts/muro.ttf";
		param2.fontParameters.size = (int) Math.ceil(tmp*45/screenWidth);

		FreeTypeFontLoaderParameter param3 = new FreeTypeFontLoaderParameter();
		param3.fontParameters.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
		param3.fontFileName = "fonts/muro slant.ttf";

		// buttons for pause overlay and win overlay
		param3.fontParameters.size = (int) (tmp*36/screenWidth);
		param3.fontParameters.color = Color.WHITE;
		param3.fontParameters.shadowColor = Color.LIGHT_GRAY;
		param3.fontParameters.shadowOffsetX = 3;
		// topRightButtons font
		FreeTypeFontLoaderParameter param4 = new FreeTypeFontLoaderParameter();
		param4.fontParameters.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
		param4.fontFileName = "fonts/muro slant.ttf";
		param4.fontParameters.borderWidth = (int) (tmp*3/screenWidth);
		param4.fontParameters.size = (int) (tmp*50/screenWidth);
		param4.fontParameters.borderColor = Color.BLACK;
		param4.fontParameters.color = Color.RED;

		// title font (called title)
		FreeTypeFontLoaderParameter param5 = new FreeTypeFontLoaderParameter();
		param5.fontParameters.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
		param5.fontFileName = "fonts/muro slant.ttf";
		param5.fontParameters.size = (int) (tmp*100/screenWidth);
		param5.fontParameters.color = Color.WHITE;

		// bebas bold (called scoreFont)
		FreeTypeFontLoaderParameter param6 = new FreeTypeFontLoaderParameter();
		param6.fontParameters.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
		param6.fontFileName = "fonts/bebas bold.ttf";
		param6.fontParameters.size = (int) (tmp*30/screenWidth);
		param6.fontParameters.color = Color.WHITE;

		// DeathOverlay font
		FreeTypeFontLoaderParameter param7 = new FreeTypeFontLoaderParameter();
		param7.fontParameters.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
		param7.fontFileName = "fonts/bebas book.ttf";
		param7.fontParameters.size = (int) (tmp*35/screenWidth);
		param7.fontParameters.color = Color.WHITE;

		FreeTypeFontLoaderParameter param8 = new FreeTypeFontLoaderParameter();
		param8.fontParameters.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
		param8.fontFileName = "fonts/bebas bold.ttf";
		param8.fontParameters.size = (int) (tmp*45/screenWidth);
		param8.fontParameters.color = Color.WHITE;
		
		FreeTypeFontLoaderParameter param9 = new FreeTypeFontLoaderParameter();
		param9.fontParameters.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
		param9.fontFileName = "fonts/bebas book.ttf";
		param9.fontParameters.size = (int) (tmp*45/screenWidth);
		param9.fontParameters.color = Color.WHITE;
		
		FreeTypeFontLoaderParameter param10 = new FreeTypeFontLoaderParameter();
		param10.fontParameters.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
		param10.fontFileName = "fonts/bebas bold.ttf";
		param10.fontParameters.size = (int) (tmp*60/screenWidth);
		param10.fontParameters.color = Color.WHITE;
		
		FreeTypeFontLoaderParameter param11 = new FreeTypeFontLoaderParameter();
		param11.fontParameters.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
		param11.fontFileName = "fonts/bebas bold.ttf";
		param11.fontParameters.size = (int) (tmp*50/screenWidth);
		param11.fontParameters.color = Color.BLACK;
		
		FreeTypeFontLoaderParameter param12 = new FreeTypeFontLoaderParameter();
		param12.fontParameters.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
		param12.fontFileName = "fonts/bebas bold.ttf";
		param12.fontParameters.size = (int) (tmp*25/screenWidth);
		param12.fontParameters.color = Color.BLACK;
		
		FreeTypeFontLoaderParameter param13 = new FreeTypeFontLoaderParameter();
		param13.fontParameters.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
		param13.fontFileName = "fonts/muro slant.ttf";
		param13.fontParameters.size = (int) (tmp*40/screenWidth);
		param13.fontParameters.color = Color.BLACK;
		/*
		 * 		FreeTypeFontLoaderParameter param1 = new FreeTypeFontLoaderParameter();
		param1.fontParameters.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
		param1.fontFileName = "fonts/muro slant.ttf";
		param1.fontParameters.size = (int) Math.ceil(screenWidth / 16);
		param1.fontParameters.color = Color.WHITE;

		// table font
		FreeTypeFontLoaderParameter param2 = new FreeTypeFontLoaderParameter();
		param2.fontParameters.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
		param2.fontFileName = "fonts/muro.ttf";
		param2.fontParameters.size = (int) Math.ceil(screenWidth / 28);

		FreeTypeFontLoaderParameter param3 = new FreeTypeFontLoaderParameter();
		param3.fontParameters.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
		param3.fontFileName = "fonts/muro slant.ttf";

		// buttons for pause overlay and win overlay
		param3.fontParameters.size = (int) (screenWidth / 30);
		param3.fontParameters.color = Color.WHITE;
		param3.fontParameters.shadowColor = Color.LIGHT_GRAY;
		param3.fontParameters.shadowOffsetX = 3;
		// topRightButtons font
		FreeTypeFontLoaderParameter param4 = new FreeTypeFontLoaderParameter();
		param4.fontParameters.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
		param4.fontFileName = "fonts/muro slant.ttf";
		param4.fontParameters.borderWidth = (int) (screenWidth / 640.0);
		param4.fontParameters.size = (int) (screenHeight / 20);
		param4.fontParameters.borderColor = Color.BLACK;
		param4.fontParameters.color = Color.RED;

		// title font (called title)
		FreeTypeFontLoaderParameter param5 = new FreeTypeFontLoaderParameter();
		param5.fontParameters.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
		param5.fontFileName = "fonts/muro slant.ttf";
		param5.fontParameters.size = (int) (screenWidth / 12);
		param5.fontParameters.color = Color.WHITE;

		// bebas bold (called scoreFont)
		FreeTypeFontLoaderParameter param6 = new FreeTypeFontLoaderParameter();
		param6.fontParameters.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
		param6.fontFileName = "fonts/bebas bold.ttf";
		param6.fontParameters.size = (int) (screenWidth / 32);
		param6.fontParameters.color = Color.WHITE;

		// DeathOverlay font
		FreeTypeFontLoaderParameter param7 = new FreeTypeFontLoaderParameter();
		param7.fontParameters.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
		param7.fontFileName = "fonts/bebas book.ttf";
		param7.fontParameters.size = (int) (screenWidth / 32);
		param7.fontParameters.color = Color.WHITE;

		FreeTypeFontLoaderParameter param8 = new FreeTypeFontLoaderParameter();
		param8.fontParameters.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
		param8.fontFileName = "fonts/bebas bold.ttf";
		param8.fontParameters.size = (int) (screenWidth / 25);
		param8.fontParameters.color = Color.WHITE;
		
		FreeTypeFontLoaderParameter param9 = new FreeTypeFontLoaderParameter();
		param9.fontParameters.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
		param9.fontFileName = "fonts/bebas book.ttf";
		param9.fontParameters.size = (int) (screenWidth / 25);
		param9.fontParameters.color = Color.WHITE;
		
		FreeTypeFontLoaderParameter param10 = new FreeTypeFontLoaderParameter();
		param10.fontParameters.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
		param10.fontFileName = "fonts/bebas bold.ttf";
		param10.fontParameters.size = (int) (screenWidth / 10);
		param10.fontParameters.color = Color.WHITE;
		
		FreeTypeFontLoaderParameter param11 = new FreeTypeFontLoaderParameter();
		param11.fontParameters.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
		param11.fontFileName = "fonts/bebas bold.ttf";
		param11.fontParameters.size = (int) (screenWidth / 20);
		param11.fontParameters.color = Color.BLACK;
		
		FreeTypeFontLoaderParameter param12 = new FreeTypeFontLoaderParameter();
		param12.fontParameters.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
		param12.fontFileName = "fonts/bebas bold.ttf";
		param12.fontParameters.size = (int) (screenWidth / 40);
		param12.fontParameters.color = Color.BLACK;
		
		FreeTypeFontLoaderParameter param13 = new FreeTypeFontLoaderParameter();
		param13.fontParameters.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
		param13.fontFileName = "fonts/muro slant.ttf";
		param13.fontParameters.size = (int) (screenWidth / 32);
		param13.fontParameters.color = Color.BLACK;
		 */
		parameters.add(param1);
		parameters.add(param2);
		parameters.add(param3);
		parameters.add(param4);
		parameters.add(param5);
		parameters.add(param6);
		parameters.add(param7);
		parameters.add(param8);
		parameters.add(param9);
		parameters.add(param10);
		parameters.add(param11);
		parameters.add(param12);
		parameters.add(param13);
		for (FreeTypeFontLoaderParameter p : parameters) {
			p.fontParameters.packer = packer;
			fontSizes.add(p.fontParameters.size);
			p.fontParameters.minFilter = TextureFilter.Nearest;
			p.fontParameters.magFilter = TextureFilter.Linear;
		}
	}

	public void generateAfterFontsComplete() {
		// TO BE MOVED
		NinePatch tableIMG = new NinePatch(new Texture("img/tableBackground.png"), 10, 10, 10, 10);
		skin.add("background", tableIMG);
		skin.add("style", new LabelStyle(fonts.get("largeFont"), Color.WHITE));
		// skin.add("smallStyle", new
		// LabelStyle(assetManager.get("generated-fonts/largeFont.fnt",
		// BitmapFont.class), Color.WHITE));
		skin.add("tableStyle", new LabelStyle(fonts.get("largeFont"), Color.WHITE));
		skin.add("largeStyle", new LabelStyle(fonts.get("largeFont"), Color.WHITE));
		skin.add("smallStyle", new LabelStyle(fonts.get("scoreFont"), Color.WHITE));

		// ones I use
		skin.add("giantStyle", new LabelStyle(fonts.get("titleFont"), Color.WHITE));
		skin.add("titleStyle", new LabelStyle(fonts.get("largeFont"), Color.WHITE));
		skin.add("subtitleStyle", new LabelStyle(fonts.get("largeFont"), Color.BLACK));
		skin.add("muroStyle", new LabelStyle(fonts.get("tableFont"), Color.BLACK));
		skin.add("textStyle", new LabelStyle(fonts.get("scoreFont"), Color.BLACK));
		skin.add("numberStyle", new LabelStyle(fonts.get("deathFont"), Color.BLACK));
		skin.add("bebasBoldScore", new LabelStyle(fonts.get("scoreFont"), Color.WHITE));
		skin.add("bebasBookScore", new LabelStyle(fonts.get("deathFont"), Color.WHITE));
		

		NinePatch buttonIMG = new NinePatch(new Texture("img/tableBackground.png"), 10, 10, 10, 10);
		skin.add("buttonIMG", buttonIMG);
		TextButton.TextButtonStyle equationStyle = new TextButton.TextButtonStyle();
		equationStyle.font = fonts.get("scoreFont");
		equationStyle.fontColor = Color.BLACK;
		equationStyle.overFontColor = Color.BLACK;
		equationStyle.up = skin.getDrawable("buttonIMG");
		skin.add("equationStyle", equationStyle);

		TextButton.TextButtonStyle checkedStyle = new TextButton.TextButtonStyle();
		checkedStyle.font = fonts.get("scoreFont");
		checkedStyle.fontColor = Color.BLACK;
		checkedStyle.overFontColor = Color.BLACK;
		skin.add("checkedStyle", checkedStyle);

		TextButton.TextButtonStyle menuButtonStyle = new TextButton.TextButtonStyle();
		menuButtonStyle.font = fonts.get("largeFont");
		menuButtonStyle.fontColor = Color.BLACK;
		menuButtonStyle.overFontColor = Color.WHITE;
		skin.add("menuButtonStyle", menuButtonStyle);

		TextButton.TextButtonStyle backStyle = new TextButton.TextButtonStyle();
		backStyle.font = fonts.get("largeFont");
		//System.out.println("EXISTS" + fonts.containsKey("largeFont"));
		backStyle.fontColor = Color.WHITE;
		backStyle.overFontColor = Color.WHITE;
		skin.add("backStyle", backStyle);

		TextButton.TextButtonStyle menuStyle = new TextButton.TextButtonStyle();
		menuStyle.font = fonts.get("tableFont");
		menuStyle.fontColor = Color.WHITE;
		menuStyle.overFontColor = Color.WHITE;
		skin.add("menuStyle", menuStyle);

		TextButton.TextButtonStyle upgradeStyle = new TextButton.TextButtonStyle();
		upgradeStyle.font = fonts.get("tableFont");
		upgradeStyle.fontColor = Color.BLACK;
		upgradeStyle.overFontColor = Color.WHITE;
		skin.add("upgradeStyle", upgradeStyle);

		TextButtonStyle deathStyle = new TextButtonStyle();
		deathStyle.font = fonts.get("deathFont");
		skin.add("deathStyle", deathStyle);
		// TO BE MOVED
		// TO BE MOVED
		// TO MOVE TO AFTER FONT GENERATED

		// TO BE MOVED TO after fonts loaded
		// SHOULD BE MOVED SOMEWHERE ELSE
		// TO BE MOVED to later, after fonts have been loaded
		Label.LabelStyle resumeStyle = new Label.LabelStyle();
		resumeStyle.font = fonts.get("largeFont");
		skin.add("resumeStyle", resumeStyle);
		afterFontsComplete = true;

	}

	// maybe only generate the font once??
	public void generateTrickLabels() {
		Label.LabelStyle style = new Label.LabelStyle();
		style.font = fonts.get("flipFont");
		//style.fontColor = Color.BLACK;
		skin.add("flipStyle", style);
		tricks.add(new TrickLabel("", style));
		/*tricks.add(new TrickLabel("", style));
		tricks.add(new TrickLabel("", style));
		tricks.add(new TrickLabel("", style));
		tricks.add(new TrickLabel("", style));*/
	}

	public void addStyleToSkin(LabelStyle labelStyle, String name) {
		skin.add(name, labelStyle);
	}

	// get save profiles
	public Map<String, Object> getProfile() {
		return profile;
	}

	public Map<String, Object> getLevelProfile(String level) {
		return nestedLevelProfile.get(level);

	}

	public void setLevelData(String level, String key, Object object) {
		nestedLevelProfile.get(level).put(key, object);
		Json json = new Json();
		json.setOutputType(OutputType.json);
		levelData = Gdx.files.local("save/savedata" + getSave() + "/" + level + ".json");
		levelData.writeString(json.prettyPrint(nestedLevelProfile.get(level)), false);
	}

	public void setLevelData(String level, String key, Object object, boolean replace) {
		nestedLevelProfile.get(level).put(key, object);
		Json json = new Json();
		json.setOutputType(OutputType.json);
		levelData = Gdx.files.local("save/savedata" + getSave() + "/" + level + ".json");
		levelData.writeString(json.prettyPrint(nestedLevelProfile.get(level)), replace);
	}

	// updates save profile data then writes to json file
	public void newProfileData(String string, Object object) {
		playerData = Gdx.files.local("save/save" + this.getSave() + ".json");
		profile.put(string, object);
		Json json = new Json();
		json.setOutputType(OutputType.json);
		// with encryption
		// playerData.writeString(Base64Coder.encodeString(json.prettyPrint(profile)),
		// false);
		playerData.writeString(json.prettyPrint(profile), false);
	}

	public String getName() {
		return (String) getProfile().get("name");
	}

	public int getVehicle() {
		return (Integer) getProfile().get("vehicle");
	}

	public int getSkin() {
		return (Integer) getProfile().get("skin");
	}

	// getter & setter for save files
	public String getSave() {
		return prefs.getString("activeSave");
	}

	public void setSave(String save) {
		prefs.putString("activeSave", save);
		prefs.flush();
		playerData = Gdx.files.local("save/save" + save + ".json");
		// does this need to happen?
		Json json = new Json();
		// profile = json.fromJson(HashMap.class,
		// Base64Coder.decodeString(playerData.readString()));
		profile = json.fromJson(HashMap.class, playerData.readString());
	}

	// can think of a better way later
	// get names for save menu
	public String getNameFromSave(String save) {
		playerData = Gdx.files.local("save/" + save + ".json");
		Json json = new Json();
		Map<String, Object> temp = json.fromJson(HashMap.class, playerData.readString());
		// Map<String, Object>temp = json.fromJson(HashMap.class,
		// Base64Coder.decodeString(playerData.readString()));
		playerData = Gdx.files.local("save/save" + getSave() + ".json");
		return (String) temp.get("name");

	}

	public Object getObjectFromSave(String save, String key) {
		playerData = Gdx.files.local("save/" + save + ".json");
		Json json = new Json();
		Map<String, Object> temp = json.fromJson(HashMap.class, playerData.readString());
		// Map<String, Object>temp = json.fromJson(HashMap.class,
		// Base64Coder.decodeString(playerData.readString()));
		playerData = Gdx.files.local("save/save" + getSave() + ".json");
		return (Object) temp.get(key);

	}

	// Retrieves the current sound and music preferences
	public boolean getSoundPrefs() {
		return prefs.getBoolean("sound");
	}

	public void setSoundPrefs(boolean sound) {
		prefs.putBoolean("sound", sound);
		prefs.flush();
	}

	public boolean getMusicPrefs() {
		return prefs.getBoolean("music");
	}

	public void setMusicPrefs(boolean music) {
		prefs.putBoolean("music", music);
		prefs.flush();
	}

	public Integer getSelectedVehicleFromSave(String save) {
		playerData = Gdx.files.local("save/" + save + ".json");
		Json json = new Json();
		Map<String, Object> temp = json.fromJson(HashMap.class, playerData.readString());
		// Map<String, Object>temp = json.fromJson(HashMap.class,
		// Base64Coder.decodeString(playerData.readString()));
		playerData = Gdx.files.local("save/save" + getSave() + ".json");
		return (Integer) temp.get("selectedVehicle");

	}

	private void createAndroidFiles() {
		FileHandle saveData;
		Map<String, Object> profile = new HashMap<String, Object>();
		Map<String, Object> levelSetProfile = new HashMap<String, Object>();
		Map<String, Object> levelProfile = new HashMap<String, Object>();
		Map<String, Object> vehicleData = new HashMap<String, Object>();

		saveData = Gdx.files.local("save/save1.json");
		if (!saveData.exists()) {
			Gdx.app.getPreferences("agentx").putFloat("equationTableOpacity", 0.8f);
			try {
				Gdx.app.log("Agent x", "Making new files and crap");
				// create json writer
				Json json = new Json();
				json.setOutputType(OutputType.json);
				Gdx.app.log("Agent x", "1");

				Array<String> names = new Array<String>();
				names.add("Training Grounds");
				names.add("City Skyline");
				names.add("Woodlands");
				names.add("Metropolis");
				names.add("Rocky Mountains");
				names.add("Airships");
				names.add("Oil Fields");
				
				Array<String> subTitles = new Array<String>();
				subTitles.add("Controls");
				subTitles.add("Line Equations");
				subTitles.add("Hidden Line Equations");
				subTitles.add("Linear Equations");
				subTitles.add("Hidden Linear Equations");
				subTitles.add("Quadratic Equations");
				subTitles.add("Hidden Quadratic Equations");
				
				Array<String> lvlNames = new Array<String>();
				lvlNames.addAll("Controls","Special Tactics","Mission Brief","Generator","Hidden Data","Accelerators","Double Canyon","Dangerous Terrain",
						"Rooftops","Sky Tracks","Bridge Building","High Terraces","Clock Tower","Elevator Shaft","Swing Boom","Concrete Jungle",
						"Pine Forest","The Great Oak","Fire Pit","Downhill Racing","Timber Mines","The Gorge","Rock Hopper","Landslide Cliff",
						"Tunnels","Radio Towers","Fire Escape","Falling Orb","Obelisk","Manipulator","Sky Beams","Jack Lift",
						"Angels Landing","The Gulch","Tumbler","Backside","Plateaus","Twin Valley","Stepping Stones","Mountain Peak",
						"Learning","Learning","Learning","Learning","Learning","Learning","Learning","Learning",
						"Learning","Learning","Learning","Learning","Learning","Learning","Learning","Learning");
				
				Array<Integer> targetTime = new Array<Integer>();

				Array<Integer> coinTotals = new Array<Integer>();
				coinTotals.addAll(0,0,1,2,3,3,3,3
						,3,3,5,4,4,4,4,6
						,3,3,3,4,4,4,6,5
						,3,5,5,6,6,6,6,6
						,4,6,4,6,5,6,6,6
						,5,5,5,5,5,5,5,5
						,5,5,5,5,5,5,5,5);
				Array<Integer> coinsPerPack = new Array<Integer>();
				coinsPerPack.addAll(15,33,34,41,43,40,40);

				// vehicle crap
				Array<String> vehicleNames = new Array<String>();
				vehicleNames.addAll("Starter Bike", "Downhill Bike", "Freestyle Bike", "BMX Bike",
						"Downhill Pro Series", "Motorcycle", "Dune Buggy",
						"BMX Pro Series", "Desert Racer", "Hover Bike", "Speed Demon", "Snow Machine");
				Array<String> vehicleDescriptions = new Array<String>();
				vehicleDescriptions.addAll("Standard issue bike", "A higher quality bike",
						"Extra control with no back shocks",
						"Lighter bike with great control", "Exceptional bike with full shocks",
						"1000cc racing motorcycle", "Buggy with a light roll cage",
						"Pro BMX bike with insane air control", "Racing buggy designed for speed", "The ultimate agent motorcycle",
						"Removed roll cage for insane speed", "The definition of power");
				Array<Integer> cost = new Array<Integer>();
				cost.addAll(0, 10000, 20000, 30000, 40000, 45000, 50000, 60000, 70000, 80000,
						80000, 80000);
				Array<Float> speed = new Array<Float>();
				//speed.addAll(30, 34, 34, 38, 34, 42, 39, 42, 42, 42, 46, 46, 48, 52, 54, 58);
				//speed.addAll(30, 32, 32, 34, 35, 36, 36, 37, 38, 40, 40, 42);
				speed.addAll(30f, 31f, 31f, 32f, 32.5f, 33f, 33f, 33.5f, 34f, 35f, 35f, 36f);
				Array<Float> acceleration = new Array<Float>();
				//acceleration.addAll(35, 35, 35, 35, 38, 38, 39, 42, 46, 42, 46, 46, 46, 55, 52, 52);
				//acceleration.addAll(35, 35, 35, 38, 36, 39, 37, 38, 40, 44, 42, 39);
				acceleration.addAll(35f, 35f, 35f, 36.5f, 35.5f, 37f, 36f, 36.5f, 37.5f, 39.5f, 38.5f, 37.5f);
				Array<Float> control = new Array<Float>();
				//control.addAll(10, 10, 13, 11, 15, 12, 17, 15, 15, 15, 20, 16, 17, 18, 18, 17);
				//control.addAll(10, 10, 12, 13, 12, 12, 12, 15, 13, 14, 14, 14);
				control.addAll(10f, 10f, 11f, 11.5f, 11f, 11f, 11f, 12.5f, 11.5f, 12f, 12f, 12f);
				
				saveData = Gdx.files.local("save/");
				saveData.file().mkdirs();

				profile.put("coins", (Integer) 500000);
				profile.put("name", (String) "Empty");
				profile.put("skin", (Integer) 1);
				profile.put("selectedVehicle", (Integer) 1);
				//normally 0 below line
				profile.put("unlockedEquations", (Integer) 2);
				profile.put("linearTerms", (Integer) 1);
				profile.put("quadraticTerms", (Integer) 1);
				profile.put("totalData", (Integer) 0);
				Gdx.app.log("Agent x", "2");
				saveData = Gdx.files.local("save/save1.json");
				saveData.file().createNewFile();
				saveData.writeString(json.prettyPrint(profile), false);

				saveData = Gdx.files.local("save/save2.json");
				saveData.file().createNewFile();
				saveData.writeString(json.prettyPrint(profile), false);

				saveData = Gdx.files.local("save/save3.json");
				saveData.file().createNewFile();
				saveData.writeString(json.prettyPrint(profile), false);

				LEVEL_PACK_LETTER[] letters = LEVEL_PACK_LETTER.values();
				// loop for every save profile (3)
				for (int j = 0; j < 3; j++) {
					saveData = Gdx.files.local("save/savedata" + (j + 1) + "/");
					saveData.file().mkdirs();

					// will need to write vehicles into this
					for (int m = 0; m < vehicleNames.size; m++) {
						saveData = Gdx.files.local("save/savedata" + (j + 1) + "/v" + (m + 1) + ".json");
						vehicleData.put("name", vehicleNames.get(m));
						vehicleData.put("info", vehicleDescriptions.get(m));
						// This makes only the first one unlocked
						if (m == 0) {
							vehicleData.put("unlocked", (Boolean) true);
						} else {
							vehicleData.put("unlocked", (Boolean) false);
						}

						// this makes all of them unlocked
						// vehicleData.put("unlocked", (Boolean) true);
						vehicleData.put("cost", cost.get(m));
						vehicleData.put("speed", speed.get(m));
						vehicleData.put("acceleration", acceleration.get(m));
						vehicleData.put("control", control.get(m));
						saveData.file().createNewFile();
						saveData.writeString(json.prettyPrint(vehicleData), false);
					}

					// every pack
					for (int k = 0; k < letters.length; k++) {
						// a0 etc data
						levelSetProfile.put("levelsCompleted", (Integer) 0);
						levelSetProfile.put("totalCoins", (Integer) coinsPerPack.get(k));
						levelSetProfile.put("completed", (Boolean) false);
						levelSetProfile.put("title", (String) names.get(k));
						levelSetProfile.put("subTitle", (String) subTitles.get(k));
						levelSetProfile.put("coinsCollected", (Integer) 0);
						if (k == 0) {
							levelSetProfile.put("unlocked", (Boolean) true);

						} else {
							levelSetProfile.put("unlocked", (Boolean) false);
						}

						Gdx.app.log("AgentEx", "" + letters[k]);
						saveData = Gdx.files.local("save/savedata" + (j + 1) + "/" + letters[k] + "0.json");
						saveData.file().createNewFile();
						saveData.writeString(json.prettyPrint(levelSetProfile), false);
						//

						for (int l = 1; l < 9; l++) {
							saveData = Gdx.files.local("save/savedata" + (j + 1) + "/" + letters[k] + l + ".json");
							saveData.file().createNewFile();
							int[] holder = { 0, 0, 0, 0, 0, 0 }; // create
																	// levelData
							levelProfile.put("highScore", (Integer) 0);
							levelProfile.put("coins", holder);
							levelProfile.put("highScore", (Integer) 0);
							levelProfile.put("completed", (Boolean) false);
							levelProfile.put("firstLoad", (Boolean) true);
							if (k == 0 && l == 1) {
								levelProfile.put("unlocked", (Boolean) true);
							} else {
								levelProfile.put("unlocked", (Boolean) false);
							}
							levelProfile.put("coinsTotal", (Integer) coinTotals.get((k * 8) + l - 1));
							levelProfile.put("title", (String) lvlNames.get((k * 8) + l - 1));
							levelProfile.put("coinsCollected", (Integer) 0);
							saveData.writeString(json.prettyPrint(levelProfile), false);
						}
					}
				}
				/**
				 * // make save directory and profile data files saveData =
				 * Gdx.files.local("save/"); saveData.file().mkdirs();
				 * 
				 * // create starting profile data, create 3 files and write it
				 * to // each of them profile.put("coins", (Integer) 0);
				 * profile.put("name", (String) "Empty"); profile.put("skin",
				 * (Integer) 1); profile.put("vehicle", (Integer) 1);
				 * profile.put("unlockedEquations", (Integer) 3);
				 * profile.put("linearTerms", (Integer) 2);
				 * profile.put("quadraticTerms", (Integer) 3);
				 * 
				 * // dont know why I have this file saveData =
				 * Gdx.files.local("save/save0.json");
				 * saveData.file().createNewFile();
				 * saveData.writeString(json.prettyPrint(profile), false);
				 * 
				 * saveData = Gdx.files.local("save/save1.json");
				 * saveData.file().createNewFile();
				 * saveData.writeString(json.prettyPrint(profile), false);
				 * 
				 * saveData = Gdx.files.local("save/save2.json");
				 * saveData.file().createNewFile();
				 * saveData.writeString(json.prettyPrint(profile), false);
				 * 
				 * saveData = Gdx.files.local("save/save3.json");
				 * saveData.file().createNewFile();
				 * saveData.writeString(json.prettyPrint(profile), false);
				 * 
				 * // now create the level data folders and create the 0 level
				 * // profiles levelSetProfile.put("levelsCompleted", (Integer)
				 * 0); levelSetProfile.put("totalCoins", (Integer) 2);
				 * levelSetProfile.put("completed", (Boolean) false);
				 * levelSetProfile.put("title", (String) "Tutorial Missions");
				 * levelSetProfile.put("coinsCollected", (Integer) 0);
				 * levelSetProfile.put("unlocked", (Boolean) true);
				 * 
				 * // create a0 files 3 times for (int i = 1; i < 4; i++) {
				 * saveData = Gdx.files.local("save/savedata" + i + "/");
				 * saveData.file().mkdirs(); saveData =
				 * Gdx.files.local("save/savedata" + i + "/a0.json");
				 * saveData.file().createNewFile();
				 * saveData.writeString(json.prettyPrint(levelSetProfile),
				 * false); }
				 * 
				 * // change title & coins & unlocked
				 * levelSetProfile.put("totalCoins", (Integer) 8);
				 * levelSetProfile.put("title", (String) "The Basics");
				 * levelSetProfile.put("unlocked", (Boolean) false); // create
				 * b0 files 3 times for (int i = 1; i < 4; i++) { saveData =
				 * Gdx.files.local("save/savedata" + i + "/b0.json");
				 * saveData.file().createNewFile();
				 * saveData.writeString(json.prettyPrint(levelSetProfile),
				 * false); }
				 * 
				 * int[] holder = { 0, 0, 0, 0, 0 }; // create levelData
				 * levelProfile.put("highScore", (Integer) 0);
				 * levelProfile.put("coins", holder);
				 * levelProfile.put("highScore", (Integer) 0);
				 * levelProfile.put("completed", (Boolean) false);
				 * levelProfile.put("firstLoad", (Boolean) true);
				 * levelProfile.put("unlocked", (Boolean) true);
				 * 
				 * // populate the level data folders // for 3 save files for
				 * (int i = 1; i < 4; i++) { // for each level set character
				 * right now only 2 for (int j = 1; j < 3; j++) { // set level
				 * pack string if (j == 1) { str = "a"; } else if (j == 2) { str
				 * = "b"; } // for each 6 levels for (int k = 1; k < 7; k++) {
				 * saveData = Gdx.files.local("save/savedata" + i + "/" + str +
				 * k + ".json"); saveData.file().createNewFile();
				 * saveData.writeString(json.prettyPrint(levelProfile), false);
				 * } } }
				 **/
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Array<Sound> getSounds() {
		Array<Sound> b = new Array<Sound>();
		assetManager.getAll(Sound.class, b);
		return b;
	}


	public void smartFontFromFiles() {
		float time = System.currentTimeMillis();
		SmartFontGenerator gen = new SmartFontGenerator();
		FileHandle f = Gdx.files.internal("");
		fonts = new ObjectMap<String, BitmapFont>();
		int i = 0;
		for (String names : fontNames) {
			String s = names.replaceAll(".ttf", "");
			fonts.put(s, gen.createFont(f.child(parameters.get(i).fontFileName), s, fontSizes.get(i)));
			i++;
			System.out.println(s);
		}
		float endTime = System.currentTimeMillis();
		System.out.println(endTime - time + " TIME");
		System.out.println("FONTS SIZE " + fonts.size);
		for (String s : fonts.keys()) {
			System.out.println(s);
		}

	}
}
