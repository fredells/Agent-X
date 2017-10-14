package com.ells.agentex.utils.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.ells.agentex.AgentExGame;
import com.ells.agentex.actors.Axis;
import com.ells.agentex.actors.Equation;
import com.ells.agentex.actors.Player;
import com.ells.agentex.stages.UIStage;

public class PreGameOverlay {
	public Array<TextButton> topRightButtons = new Array<TextButton>();
	TextButton start;
	public TextButton back, next;
	public ImageButton leftButton, rightButton, pause;
	public Table backNext = new Table();
	private AgentExGame game;
	private Skin skin;
	private int checked = 0;
	private int lastChecked = 0;
	private Boolean isTutorial = false;
	private float screenWidth = Gdx.graphics.getWidth();

	public PreGameOverlay(final float screenWidth, final float screenHeight, final Skin skin, AgentExGame game) {
		this.game = game;
		this.skin = skin;
		
		//new buttons
		float buttonSize = Gdx.graphics.getWidth() / 9;
		float axisButtonSize = Gdx.graphics.getWidth()/20;
		Texture texture = new Texture(Gdx.files.internal("img/backward.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		leftButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(texture)));
		leftButton.addAction(Actions.alpha(0.5f));
		texture = new Texture(Gdx.files.internal("img/forward.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		rightButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(texture)));
		rightButton.addAction(Actions.alpha(0.5f));
		TextureRegion opTexture = game.loader.uiSkinAtlas.findRegion("pause");
		pause = new ImageButton(new TextureRegionDrawable(opTexture));
		pause.addAction(Actions.alpha(0.6f));
		pause.setSize(Gdx.graphics.getWidth() / 10, Gdx.graphics.getWidth() / 10);
		pause.setPosition(Gdx.graphics.getWidth() - pause.getWidth(), Gdx.graphics.getHeight() - pause.getHeight());
		
		
		TextButtonStyle parallelogramStyle = new TextButtonStyle();
		parallelogramStyle.font = game.loader.fonts.get("largeFont");
		skin.add("parallelogram", parallelogramStyle);
		
		Pixmap p = new Pixmap(100, 100, Pixmap.Format.RGBA8888);
		p.setColor(Color.GRAY);
		p.fillRectangle(0, 0, 100, 100);
		Texture pixmaptex = new Texture(p);
		p.dispose();
		
		createTopRightButtons();

		/*tutorial = new TextButton("Help", skin, "Hexagon");
		tutorial.sizeBy(game.loader.screenWidth / 20);
		tutorial.setBounds(0, game.loader.screenHeight - game.loader.screenWidth / 20, game.loader.screenWidth / 20,
				game.loader.screenWidth / 20);*/

		start = new TextButton("Start", skin, "backStyle");
		//start.sizeBy(game.loader.screenWidth / 10);
		//start.setSize(screenWidth * 0.1f, screenWidth * 0.05f);
		start.setPosition(Gdx.graphics.getWidth() / 2 - start.getWidth() / 2, 15);
		back = new TextButton("Back", skin, "parallelogram");
		next = new TextButton("Next", skin, "parallelogram");
		next.align(Align.center);
		back.align(Align.center);
		
		//backNext.debugAll();
		backNext.setWidth(Gdx.graphics.getWidth());
		backNext.add(leftButton).align(Align.bottom).size(buttonSize, buttonSize).pad(20);
		backNext.add().padLeft(screenWidth - back.getWidth() - next.getWidth());
		backNext.add(rightButton).align(Align.bottom).size(buttonSize, buttonSize).pad(20);
		backNext.setPosition(0, back.getHeight() / 2 + 50);

	}

	public void createTopRightButtons() {
		topRightButtons.clear();
		int var = (Integer) game.loader.getProfile().get("unlockedEquations");
		if (var > 3) {
			topRightButtons.add(new TextButton("Cubic", skin, "equationStyle"));
		}
		
		TextButton quadratic = new TextButton("Quadratic", skin, "equationStyle");
		TextButton linear = new TextButton("Linear", skin, "equationStyle");
		TextButton line = new TextButton("Line", skin, "equationStyle");
		topRightButtons.add(quadratic);
		topRightButtons.add(linear);
		topRightButtons.add(line);
		if (var < 2) {
			quadratic.setDisabled(true);
			quadratic.setTouchable(Touchable.disabled);
			quadratic.setText(" Locked ");
		} else {
			quadratic.setDisabled(false);
			quadratic.setTouchable(Touchable.enabled);
			quadratic.setText("Quadratic");
		}
		if (var < 1) {
			linear.setDisabled(true);
			linear.setTouchable(Touchable.disabled);
			linear.setText("Locked");
		} else {
			linear.setDisabled(false);
			linear.setTouchable(Touchable.enabled);
			linear.setText("Linear");
		}
		if (var < 0) {
			line.setDisabled(true);
			line.setTouchable(Touchable.disabled);
			;
		} else {
			line.setDisabled(false);
			line.setTouchable(Touchable.enabled);
		}
	}

	public void addThingsThatNeedStage(final UIStage uiStage, final Player player) {
		start.clearListeners();
		back.clearListeners();
		next.clearListeners();
		pause.clearListeners();
		leftButton.clearListeners();
		rightButton.clearListeners();
		
		for (TextButton b : topRightButtons) {
			if (b.getListeners() != null) {
				b.clearListeners();
			}
		}
		
		pause.addListener(new ClickListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;

		}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				
				game.musicManager.stop();
				for (Actor a : uiStage.getActors()) {
					a.setVisible(false);
				}
				
				game.loader.darkActive = true;
						game.manager.get("PauseOverlay", PauseOverlay.class).updatePause(uiStage.gameMode);
						uiStage.gameStage.setGameMode("pause");
						backNext.setTouchable(Touchable.disabled);
						for (TextButton tb : topRightButtons) {
							if (tb.getListeners() != null) {
								tb.setTouchable(Touchable.disabled);
								tb.setVisible(false);
							}
						}
						start.setTouchable(Touchable.disabled);
						start.setVisible(false);
						backNext.setVisible(false);
						game.manager.get("PauseOverlay", PauseOverlay.class).updatePause(uiStage.gameMode);
						game.manager.get("PauseOverlay", PauseOverlay.class).addToStage(uiStage);
				}
		});
		
		leftButton.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				try {
					uiStage.gameStage.getAxes().get(uiStage.activeAxis).getEquationTable().remove();
				} catch (Exception e) {

				}
				try {
					for (TextButton t : topRightButtons) {
						t.remove();
					}
				} catch (Exception e) {
				}
				uiStage.gameStage.cameraSliding = false;
				uiStage.gameStage.snapMode = true;
				uiStage.gameStage.cameraFollow = false;
				if(!uiStage.leftAxisFocus) {
				uiStage.gameStage.focus--;
				}
				uiStage.leftAxisFocus = false;
				if (uiStage.gameStage.focus < 0) {
					uiStage.gameStage.focus = uiStage.gameStage.focusPoints.size - 1;
				}
				if (uiStage.gameStage.focus < uiStage.gameStage.getAxes().size + 1 && uiStage.gameStage.focus > 0) {
					uiStage.cameraMode = "axisFocus";
					uiStage.activeAxis = (int) (uiStage.gameStage.focus) - 1;
					uiStage.movingToAxis = true;
				} else {
					uiStage.cameraMode = "preGame";
				}
				uiStage.gameStage.setFirstTime(0);

			}
		});
		rightButton.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				try {
					uiStage.gameStage.getAxes().get(uiStage.activeAxis).getEquationTable().remove();
				} catch (Exception e) {

				}
				try {
					for (TextButton t : topRightButtons) {
						t.remove();
					}
				} catch (Exception e) {
				}
				uiStage.gameStage.cameraSliding = false;
				uiStage.gameStage.snapMode = true;
				uiStage.gameStage.cameraFollow = false;
				uiStage.gameStage.focus++;
				if (uiStage.gameStage.focus > uiStage.gameStage.focusPoints.size - 1) {
					uiStage.gameStage.focus = 0;
					uiStage.activeAxis = (int) (uiStage.gameStage.focus) - 1;
				}
				System.out.println("FOCUS FOCUS"+ uiStage.gameStage.focus);
				if (uiStage.gameStage.focus < uiStage.gameStage.getAxes().size + 1 && uiStage.gameStage.focus > 0) {
					uiStage.movingToAxis = true;
					uiStage.cameraMode = "axisFocus";
					uiStage.activeAxis = (int) (uiStage.gameStage.focus) - 1;
				} else {
					uiStage.cameraMode = "preGame";
				}
				uiStage.gameStage.setFirstTime(0);

			}
		});
		
		start.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				game.fxManager.play("Sound/click.ogg");
				//start.setDisabled(true);
				//uiStage.gameStage.getPlayer().setActive(true);
				//uiStage.gameMode = "playing";
//				game.manager.get("Sound/Background #2.ogg", Music.class).setVolume(0.2f);
//				game.manager.get("Sound/Background #2.ogg", Music.class).play();
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				start.setDisabled(false);
				if (isOver(start, x, y)) {
					player.setActive(true);
					uiStage.cameraMode = "playing";
					uiStage.gameStage.cameraFollow = true;
					uiStage.gameStage.snapMode = false;
					uiStage.gameMode = "playing";
					// doing this just makes it so if you start on last one the
					// camera works
					uiStage.gameStage.focus = 0;
					if (uiStage.activeAxis >= 0 && uiStage.activeAxis < uiStage.gameStage.getAxes().size) {
						if (uiStage.gameStage.getAxes().size > 0) {
							if (uiStage.gameStage.getAxes().get(uiStage.activeAxis).getEquationTable() != null) {
								uiStage.gameStage.getAxes().get(uiStage.activeAxis).getEquationTable().remove();
							}
						}
					}
					removeOverlay();
					game.manager.get("InGameOverlay", InGameOverlay.class).addToStage(uiStage);
				}
			}
		});
		
		for (int i = 0; i < topRightButtons.size; i++) {
			int displacement = 0;
			for (int j = 0; j <= i; j++) {
				displacement += topRightButtons.get(j).getWidth();
				//if (j > 0) {displacement += topRightButtons.get(j - 1).getWidth();}
			}
			/*topRightButtons.get(i).setPosition(game.loader.screenWidth - displacement,
					game.loader.screenHeight - topRightButtons.get(i).getHeight());*/
			topRightButtons.get(i).setPosition(game.loader.screenWidth - 40 * (screenWidth / 1280) - displacement - (i + 1) * 40,
					game.loader.screenHeight / 2 + 300 / 2);
			
			
			if (topRightButtons.get(i).getText().toString().equals("Line")) {
				topRightButtons.get(i).addListener(new ClickListener() {

					@Override
					public void clicked(InputEvent event, float x, float y) {
						if (uiStage.gameStage.getAxes().get(uiStage.activeAxis).getBodyArray().size != 0) {
							uiStage.gameStage
									.destroyBody(uiStage.gameStage.getAxes().get(uiStage.activeAxis).getBody());
						}
						if (uiStage.gameStage.getAxes().get(uiStage.activeAxis).getEquationTable() != null) {
							uiStage.gameStage.getAxes().get(uiStage.activeAxis).getEquationTable().remove();
						}
						// a value, k value, horizontal translation, vertical
						// translation, power
						uiStage.gameStage.getAxes().get(uiStage.activeAxis).setEquation(new Equation(0, 0, 0, 0, 0));
						uiStage.gameStage.getAxes().get(uiStage.activeAxis).createLine(uiStage.gameStage.getmScene(),
								uiStage.gameStage.getAxes().get(uiStage.activeAxis).equationToPoints());
						uiStage.gameStage.getAxes().get(uiStage.activeAxis).setupTable();
						uiStage.gameStage.getAxes().get(uiStage.activeAxis).getEquationTable()
								.updateSizeAndPos(uiStage);
						uiStage.addActor(uiStage.gameStage.getAxes().get(uiStage.activeAxis).getEquationTable());
						/*lastChecked = checked;
						updateButtons();
						lastChecked = checked;
						checked = 0;
						updateButtons();*/
						resetButtons(uiStage.gameStage.getAxes().get(uiStage.activeAxis));
					}
				});
			} else if (topRightButtons.get(i).getText().toString().equals("Linear")) {
				topRightButtons.get(i).addListener(new ClickListener() {

					@Override
					public void clicked(InputEvent event, float x, float y) {

						if (uiStage.gameStage.getAxes().get(uiStage.activeAxis).getBodyArray().size != 0) {
							uiStage.gameStage
									.destroyBody(uiStage.gameStage.getAxes().get(uiStage.activeAxis).getBody());
						}
						if (uiStage.gameStage.getAxes().get(uiStage.activeAxis).getEquationTable() != null) {
							uiStage.gameStage.getAxes().get(uiStage.activeAxis).getEquationTable().remove();
						}
						// a value, k value, horizontal translation, vertical
						// translation, power
						uiStage.gameStage.getAxes().get(uiStage.activeAxis).setEquation(new Equation(1, 1, 0, 0, 1));
						uiStage.gameStage.getAxes().get(uiStage.activeAxis).createLine(uiStage.gameStage.getmScene(),
								uiStage.gameStage.getAxes().get(uiStage.activeAxis).equationToPoints());
						uiStage.gameStage.getAxes().get(uiStage.activeAxis).setupTable();
						uiStage.addActor(uiStage.gameStage.getAxes().get(uiStage.activeAxis).getEquationTable());
						/*lastChecked = checked;
						checked = 1;
						updateButtons();*/
						resetButtons(uiStage.gameStage.getAxes().get(uiStage.activeAxis));
					}
				});
			} else if (topRightButtons.get(i).getText().toString().equals("Quadratic")) {
				topRightButtons.get(i).addListener(new ClickListener() {

					@Override
					public void clicked(InputEvent event, float x, float y) {
						if (uiStage.gameStage.getAxes().get(uiStage.activeAxis).getBodyArray().size != 0) {
							uiStage.gameStage
									.destroyBody(uiStage.gameStage.getAxes().get(uiStage.activeAxis).getBody());
						}
						if (uiStage.gameStage.getAxes().get(uiStage.activeAxis).getEquationTable() != null) {
							uiStage.gameStage.getAxes().get(uiStage.activeAxis).getEquationTable().remove();
						}
						// a value, k value, horizontal translation, vertical
						// translation, power
						uiStage.gameStage.getAxes().get(uiStage.activeAxis).setEquation(new Equation(1, 1, 0, 0, 2));
						uiStage.gameStage.getAxes().get(uiStage.activeAxis).createLine(uiStage.gameStage.getmScene(),
								uiStage.gameStage.getAxes().get(uiStage.activeAxis).equationToPoints());
						uiStage.gameStage.getAxes().get(uiStage.activeAxis).setupTable();
						uiStage.addActor(uiStage.gameStage.getAxes().get(uiStage.activeAxis).getEquationTable());
						/*lastChecked = checked;
						checked = 2;
						updateButtons();*/
						resetButtons(uiStage.gameStage.getAxes().get(uiStage.activeAxis));
						
					}
				});
			} else if (topRightButtons.get(i).getText().toString().equals("Cubic")) {
				topRightButtons.get(i).addListener(new ClickListener() {

					@Override
					public void clicked(InputEvent event, float x, float y) {
						if (uiStage.gameStage.getAxes().get(uiStage.activeAxis).getEquationTable() != null) {
							uiStage.gameStage.getAxes().get(uiStage.activeAxis).getEquationTable().remove();
						}
						if (uiStage.gameStage.getAxes().get(uiStage.activeAxis).getBodyArray().size != 0) {
							uiStage.gameStage
									.destroyBody(uiStage.gameStage.getAxes().get(uiStage.activeAxis).getBody());
						}
						// a value, k value, horizontal translation, vertical
						// translation, power
						uiStage.gameStage.getAxes().get(uiStage.activeAxis).setEquation(new Equation(1, 1, 0, 0, 3));
						uiStage.gameStage.getAxes().get(uiStage.activeAxis).createLine(uiStage.gameStage.getmScene(),
								uiStage.gameStage.getAxes().get(uiStage.activeAxis).equationToPoints());
						uiStage.gameStage.getAxes().get(uiStage.activeAxis).setupTable();
						uiStage.gameStage.getAxes().get(uiStage.activeAxis).getEquationTable().remove();
						uiStage.addActor(uiStage.gameStage.getAxes().get(uiStage.activeAxis).getEquationTable());
						lastChecked = checked;
						checked = 3;
						updateButtons();
					}
				});
			} else if (topRightButtons.get(i).getText().toString().equals("Quartic")) {
				topRightButtons.get(i).addListener(new ClickListener() {

					@Override
					public void clicked(InputEvent event, float x, float y) {
						if (uiStage.gameStage.getAxes().get(uiStage.activeAxis).getEquationTable() != null) {
							uiStage.gameStage.getAxes().get(uiStage.activeAxis).getEquationTable().remove();
						}
						if (uiStage.gameStage.getAxes().get(uiStage.activeAxis).getBodyArray().size != 0) {
							uiStage.gameStage
									.destroyBody(uiStage.gameStage.getAxes().get(uiStage.activeAxis).getBody());
						}
						// a value, k value, horizontal translation, vertical
						// translation, power
						uiStage.gameStage.getAxes().get(uiStage.activeAxis).setEquation(new Equation(1, 1, 0, 0, 4));
						uiStage.gameStage.getAxes().get(uiStage.activeAxis).createLine(uiStage.gameStage.getmScene(),
								uiStage.gameStage.getAxes().get(uiStage.activeAxis).equationToPoints());
						uiStage.gameStage.getAxes().get(uiStage.activeAxis).setupTable();
						uiStage.gameStage.getAxes().get(uiStage.activeAxis).getEquationTable().remove();
						uiStage.addActor(uiStage.gameStage.getAxes().get(uiStage.activeAxis).getEquationTable());
					}
				});
			}
		}
	}

	public void removeOverlay() {
		pause.remove();
		start.remove();
		backNext.remove();
		for (TextButton top : topRightButtons) {
			top.remove();
		}
	}

	public void addToStage(UIStage uiStage) {
		uiStage.addActor(backNext);
		uiStage.addActor(pause);
		/*if (isTutorial == true) {
			System.out.println("IS TRUEEEEEE");
			uiStage.addActor(tutorial);
		}*/
		uiStage.addActor(start);
	}

	public void removeLRS() {
		backNext.remove();
		start.remove();
		pause.remove();

	}
	
	public void updateButtons() {
		//topRightButtons.get(topRightButtons.size - lastChecked - 1).setStyle(skin.get("equationStyle", TextButtonStyle.class));
		//topRightButtons.get(topRightButtons.size - checked - 1).setStyle(skin.get("checkedStyle", TextButtonStyle.class));
		
	}
	
	public void resetButtons(Axis axis) {
		int power;
		System.out.println("RESET BUTTONS.  isBuilt: " + axis.isBuilt());
		if (axis.isBuilt() == false) {
			power = 0;
		}
		else {
			power = (int) axis.getEquation().getPower() + 1;
		}
		System.out.println("RESET BUTTONS.  power: " + power);
		
		if (power == 0) {
			topRightButtons.get(2).setStyle(skin.get("equationStyle", TextButtonStyle.class));
			topRightButtons.get(1).setStyle(skin.get("equationStyle", TextButtonStyle.class));
			topRightButtons.get(0).setStyle(skin.get("equationStyle", TextButtonStyle.class));
		}
		else if (power == 1) {
			topRightButtons.get(2).setStyle(skin.get("checkedStyle", TextButtonStyle.class));
			topRightButtons.get(1).setStyle(skin.get("equationStyle", TextButtonStyle.class));
			topRightButtons.get(0).setStyle(skin.get("equationStyle", TextButtonStyle.class));
		}
		else if (power == 2) {
			topRightButtons.get(2).setStyle(skin.get("equationStyle", TextButtonStyle.class));
			topRightButtons.get(1).setStyle(skin.get("checkedStyle", TextButtonStyle.class));
			topRightButtons.get(0).setStyle(skin.get("equationStyle", TextButtonStyle.class));
		}
		else if (power == 3) {
			topRightButtons.get(2).setStyle(skin.get("equationStyle", TextButtonStyle.class));
			topRightButtons.get(1).setStyle(skin.get("equationStyle", TextButtonStyle.class));
			topRightButtons.get(0).setStyle(skin.get("checkedStyle", TextButtonStyle.class));
		}
	}

	public void addBackNext(UIStage uiStage) {
		uiStage.addActor(backNext);
		//uiStage.addActor(pause);
		//uiStage.addActor(start);
	}

	public void addTopRight(UIStage uiStage) {
		for (TextButton top : topRightButtons) {
			uiStage.addActor(top);
		}
	}
	
	public void enableButtons() {
		start.setTouchable(Touchable.enabled);
		start.setVisible(true);
		backNext.setTouchable(Touchable.enabled);
		backNext.setVisible(true);
		for (TextButton tb : topRightButtons) {
			if (tb.getListeners() != null) {
				tb.setTouchable(Touchable.enabled);
				tb.setVisible(true);
			}
		}
	}
}
