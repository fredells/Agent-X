package com.ells.agentex.utils.overlays;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.ells.agentex.AgentExGame;
import com.ells.agentex.listeners.MainMenuButtonListener;
import com.ells.agentex.screens.GameScreen;
import com.ells.agentex.screens.LevelScreen;
import com.ells.agentex.screens.LevelScreen.LEVEL_PACK_LETTER;
import com.ells.agentex.sound.MusicManager.MUSICSTATE;
import com.ells.agentex.stages.NewGameStage;
import com.ells.agentex.stages.UIStage;

public class WinOverlay {
	private TextButton mainMenu;
	private TextButton retry;
	private TextButton levelSelect;
	private TextButton nextLevel;
	private Label coins, complete, score, goalTimeLabel, yourTimeLabel;
	private Label levelLabel, levelNum, flipLabel, flipNum, coinLabel, coinNum, totalLabel, totalNum, collectedLabel;
	private Table coinsTable, mainTable, c1;
	public static boolean isHighScore = false;
	private AgentExGame game;
	private Skin skin;
	private ClickListener mainListener = new ClickListener();
	private ClickListener retryListener = new ClickListener();
	private ClickListener nextListener = new ClickListener();
	private ClickListener levelSelectListener = new ClickListener();
	private int scoreCounter = 0, flipScore = 0, levelScore = 0, coinScore = 0, totalScore = 0;
	private Texture texture, dot, dotFilled;
	private TextureRegion textureReg;
	private TextureRegionDrawable textureDraw;
	private Image dotImage, dotFilledImage;
	private int totalCoins = 0, coinsCollected = 0;
	private String str = "";
	private float screenWidth = Gdx.graphics.getWidth();
	private float screenHeight = Gdx.graphics.getHeight();
	private float ratio = screenWidth / 1280;

	public WinOverlay(float screenWidth, float screenHeight, final AgentExGame game) {
		this.game = game;
		skin = game.loader.skin;
		mainTable = new Table(skin);
		mainTable.setFillParent(true);
		//mainTable.setDebug(true);
		
		//label styles for now
		LabelStyle bebasBoldScore = new LabelStyle();
		bebasBoldScore.font =game.loader.fonts.get("bebasBold");
		LabelStyle bebasBookScore = new LabelStyle();
		bebasBookScore.font = game.loader.fonts.get("bebasBook");
		//LabelStyle bebasBoldScore = game.loader.generateLabel((int) screenWidth / 25, "fonts/bebas bold.ttf", Color.WHITE);
		//LabelStyle bebasBookScore = game.loader.generateLabel((int) screenWidth / 25, "fonts/bebas book.ttf", Color.WHITE);
		
		// main table that holds overview and score
		complete = new Label("Mission Complete!", skin, "titleStyle");
		score = new Label("Score: ",  skin, "titleStyle");
		/*if (isHighScore) {
			score.setText("New High Score!\n");
		}*/
		
		//new labels
		levelLabel = new Label("Level", bebasBookScore);
		coinLabel = new Label(String.valueOf(coinScore/1000) + "x Data", bebasBookScore);
		flipLabel = new Label("Tactical", bebasBookScore);
		totalLabel = new Label("Total", bebasBookScore);
		collectedLabel = new Label("Data Bits Found", bebasBookScore);
		
		levelNum = new Label(String.valueOf(levelScore), bebasBoldScore);
		coinNum = new Label(String.valueOf(coinScore), bebasBoldScore);
		flipNum = new Label(String.valueOf(flipScore), bebasBoldScore);
		totalNum = new Label(String.valueOf(totalScore), bebasBoldScore);
		
		
		// get time values and create label
		String scoreString = "";
		float time = 0;
		float goalTime = 0;
		goalTimeLabel = new Label("Target Time: " + milToMinSecMil((long) goalTime),
				skin.get("largeStyle", LabelStyle.class));
		yourTimeLabel = new Label("Mission Time: " + milToMinSecMil((long) time) + "\n",
				skin.get("largeStyle", LabelStyle.class));

		int timeScore = (int) ((goalTime - time) / 50);
		String timeScoreString = String.valueOf(timeScore);
		scoreString = "";
		int scoreCounter = 0;

		for (int i = 5; i > String.valueOf(scoreCounter).length(); i--) {
			scoreString += "0";
		}
		scoreString += String.valueOf(scoreCounter);

		// calculate total score
		scoreString = "";
		int totalScore = Integer.parseInt(timeScoreString) + scoreCounter;
		for (int i = 5; i > String.valueOf(totalScore).length(); i--) {
			scoreString += "0";
		}
		scoreString += String.valueOf(totalScore);
		score.setText("Score: " + scoreString);
		Label.LabelStyle resume = new Label.LabelStyle();
		resume.font = game.loader.fonts.get("deathFont");
		resume.fontColor = Color.WHITE;
		game.loader.addStyleToSkin(resume, "resume");
		
		
		mainMenu = new TextButton("Main Menu", skin, "menuStyle");
		levelSelect = new TextButton("Level Select", skin, "menuStyle");
		levelSelect.addListener(levelSelectListener);

		retry = new TextButton("restart", skin, "menuStyle");
		// fix the actual position later
		nextLevel = new TextButton("Next Level", skin, "menuStyle");
		nextLevel.addListener(nextListener);
		//mainTable.add(nextLevel).row();
		// add to table
		mainTable.add(complete).colspan(4).spaceBottom(50).row();
		//mainTable.add(yourTimeLabel).colspan(2).row();
		mainTable.add(levelLabel).colspan(2).right().spaceRight(50);
		mainTable.add(levelNum).right().padRight(screenWidth/5).row();
		mainTable.add(coinLabel).colspan(2).right().spaceRight(50);
		mainTable.add(coinNum).right().padRight(screenWidth/5).row();
		mainTable.add(flipLabel).colspan(2).right().spaceRight(50);
		mainTable.add(flipNum).right().padRight(screenWidth/5).row();
		mainTable.add(totalLabel).colspan(2).right().spaceRight(50).spaceBottom(50);
		mainTable.add(totalNum).right().padRight(screenWidth/5).spaceBottom(50).row();
		
		mainTable.add(mainMenu).padRight(75);
		mainTable.add(levelSelect).padRight(75);
		mainTable.add(retry).padRight(75);
		mainTable.add(nextLevel);
		
		//coin table stuff
		dot = new Texture(Gdx.files.internal("img/dotWhite.png"));
		dot.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		dotImage = new Image(dot);
		
		dotFilled = new Texture(Gdx.files.internal("img/dotWhiteFilled.png"));
		dotFilled.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		dotFilledImage = new Image(dotFilled);
		
		c1 = new Table();

	}

	public void addWinOverlay(UIStage stage) {
		game.loader.darkActive = true;
		//game.manager.get("Sound/win.ogg", Sound.class).play(0.2f, 1, 1);
		game.fxManager.play("Sound/win.ogg");
		stage.addActor(mainTable);
		
		//for coin dots
				str = String.valueOf(LevelScreen.currentLevel);
				totalCoins = (Integer) game.loader.nestedLevelProfile.get(str).get("coinsTotal");
				coinsCollected = (Integer) game.loader.nestedLevelProfile.get(str).get("coinsCollected");
				
				//make coin table
						int c, v;
						int side = 10;
						int dotSize = (int) (50 * ratio);
						int spaceBottom = 30;
						float padLeft = (int) (15 * ratio) ; float padFirst = 80; float width = 250;
						System.out.println("LEVEL: " + str);

						c1 = new Table();
						if (totalCoins > 0) {
							c = 1; v = 1;
							if (coinsCollected > 0) {
								for (c = 1; c < (coinsCollected + 1); c++) {
									if (c == 3) { c1.add(dotFilledImage).size(dotSize, dotSize).align(Align.left).pad(padLeft).padRight(side).padLeft(side).row(); }
									else { c1.add(dotFilledImage).size(dotSize, dotSize).align(Align.left).pad(padLeft).padRight(side).padLeft(side);}
									dotFilledImage = new Image(dotFilled); 
								}
							}
							for (v = 1; v < (totalCoins - coinsCollected + 1); v++) {
								if ((v + coinsCollected) == 3) { c1.add(dotImage).size(dotSize, dotSize).align(Align.left).pad(padLeft).padRight(side).padLeft(side).row(); }
								else { c1.add(dotImage).size(dotSize, dotSize).align(Align.left).pad(padLeft).padRight(side).padLeft(side);}
								dotImage = new Image(dot); 
							}
						}
						//mainTable.add(c1);
				c1.setPosition(screenWidth * 3 / 4 - c1.getWidth() / 2, screenHeight / 2 - c1.getHeight() / 2 - 25 * ratio);
				stage.addActor(c1);
						
				//end coin table stuff
		
		game.manager.get("InGameOverlay", InGameOverlay.class).removeOverlay();
		// stage.addActor(coinsTable);
		// stage.addActor(retry);
		// stage.addActor(mainMenu);
		// stage.addActor(levelSelect);
		stage.gameMode = "win";
	}

	public void addListenersToButtons(final NewGameStage stage) {
		mainMenu.removeListener(mainListener);
		retry.removeListener(retryListener);
		nextLevel.removeListener(nextListener);
		levelSelect.removeListener(levelSelectListener);
		levelSelectListener = new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				game.musicManager.changeTrack(MUSICSTATE.MENU);
				mainTable.remove();
				stage.getUiStage().addAction(sequence(fadeOut(0.01f), run(new Runnable() {
					@Override
					public void run() {
						game.loader.darkActive = false;
						((AgentExGame) Gdx.app.getApplicationListener()).setScreen(new LevelScreen(game));
					}
				})));

			}
		};
		mainListener = new MainMenuButtonListener(stage, mainTable, game);
		retryListener = new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				game.musicManager.changeTrack(MUSICSTATE.MENU);
				mainTable.remove();
				stage.getUiStage().addAction(sequence(fadeOut(0.01f), run(new Runnable() {

					@Override
					public void run() {
						game.loader.darkActive = false;
						((Game) Gdx.app.getApplicationListener())
								.setScreen(new GameScreen(stage.getEquationsFromAxes()));
					}
				})));
			}
		};
		coins = new Label("coins", skin.get("style", LabelStyle.class));
		Array<Float> coinArray = new Array<Float>();
		coinArray = (Array<Float>) (game.loader.getLevelProfile(LevelScreen.currentLevel).get("coins"));
		int coinz = 0;
		int totalCoinz = 0;
		for (Float f : coinArray) {
			if (f == 1) {
				coinz++;
			}
			totalCoinz += 1;
		}
		nextListener = new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				mainTable.remove();
				String nextLevel = LevelScreen.stringPack + String.valueOf(LevelScreen.intLevel + 1);
				if (game.loader.nestedLevelProfile.get(nextLevel) == null) {
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
				stage.getUiStage().addAction(sequence(fadeOut(0.01f), run(new Runnable() {

					@Override
					public void run() {
						((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen());
						game.loader.darkActive = false;
					}
				})));

			}
		};
		nextLevel.addListener(nextListener);
		retry.addListener(retryListener);
		mainMenu.addListener(mainListener);
		levelSelect.addListener(levelSelectListener);
		coins.setText("Coins: " + coinz + "/" + totalCoinz);
		Image coinImage = new Image(new Texture(Gdx.files.internal("img/dataCoin.png")));
		float aspectRatio = coinImage.getWidth() / coinImage.getHeight();
		coinsTable = new Table();
		coinsTable.add(coins);
		coinsTable.add(coinImage);
		coinsTable.setSize(game.loader.screenWidth * 0.15f * aspectRatio + coins.getWidth(),
				(float) (game.loader.screenWidth * 0.15f * Math.pow(aspectRatio, -1)));
		coinsTable.setPosition(game.loader.screenWidth * 0.5f - coinsTable.getWidth() / 2,
				game.loader.screenHeight * 0.75f);
	}

	public static void setIsHighScore(boolean bool) {
		isHighScore = bool;
	}

	public void setLabelTimes(float endTime, float timeTrial) {
		float goalTime = timeTrial * 1000;
		goalTimeLabel.setText("Target Time: " + milToMinSecMil((long) goalTime));
		yourTimeLabel.setText("Mission Time: " + milToMinSecMil((long) endTime) + "\n");
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
			if (mi == 0) {
				return "00:" + s + ":" + "00";
			} else
				return "00:" + s + ":" + mi;
		} else {
			return "00:00:" + Math.round(x) / 10;
		}
	}
	public void setScoreCounter(int score) {
		scoreCounter = score;
	}
	
	public void setScores(int level, int flip, int coin) {
		
		
		
		levelScore = level;
		coinScore = coin;
		flipScore = flip;
		totalScore = level + flip + coin;
		
		coinLabel.setText(String.valueOf(coinScore/1000) + "x Data");
		levelNum.setText(String.valueOf(levelScore));
		coinNum.setText(String.valueOf(coinScore));
		flipNum.setText(String.valueOf(flipScore));
		totalNum.setText(String.valueOf(totalScore));
	}
}
