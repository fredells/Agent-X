package com.ells.agentex.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.ells.agentex.AgentExGame;
import com.ells.agentex.listeners.Responder;
import com.ells.agentex.stages.UIStage;

public class CustomTable extends Table {
	private String[] tableText;
	private Equation myEquation;
	private Skin mySkin;
	private Axis myAxis;
	private boolean aValueEditable = true;
	private boolean dValueEditable = true;
	private boolean cValueEditable = true;
	//private UIStage uiStage;
	private LabelStyle linearCustomStyle;
	private LabelStyle nonLinearCustomStyle;
	private LabelStyle exponentCustomStyle;
	private LabelStyle lineCustomStyle;
	private Table centerRow = new Table();
	private float tableHeight;
	private float tableWidth;
	private float acceleration = 0;
	private float heldTime = 0;
	private float buttonSize = 120;
	private float screenWidth = Gdx.graphics.getWidth();
	private float screenHeight = Gdx.graphics.getHeight();
	private float buttonPadding = 15;
	private float ratio = screenWidth / 1280;
	private UIStage uiStage;
	private Label exponentActor;
	private Array<Array<TableButton>> buttons = new Array<Array<TableButton>>();

	public CustomTable(Equation equation, float screenHeight, float screenWidth, Skin skin, Axis axis, UIStage uiStage) {
		buttons.add(new Array<TableButton>());
		buttons.add(new Array<TableButton>());
		if(!((AgentExGame)Gdx.app.getApplicationListener()).manager.isLoaded("img/upArrow.png")) {
			((AgentExGame)Gdx.app.getApplicationListener()).manager.load("img/upArrow.png", Texture.class);
		}
		if(!((AgentExGame)Gdx.app.getApplicationListener()).manager.isLoaded("img/downArrow.png")) {
			((AgentExGame)Gdx.app.getApplicationListener()).manager.load("img/downArrow.png", Texture.class);
		}
		((AgentExGame)Gdx.app.getApplicationListener()).manager.finishLoading();
		this.pack();
		myAxis = axis;
		//this.debugAll();
		this.uiStage = uiStage;
		tableHeight = screenHeight-uiStage.preGameUI.next.getHeight()- uiStage.preGameUI.topRightButtons.get(0).getHeight();
		AgentExGame game = (((AgentExGame) Gdx.app.getApplicationListener()));
		Label.LabelStyle largeStyle = new Label.LabelStyle(game.loader.fonts.get("linear"), Color.BLACK);
		Label.LabelStyle smallStyle = new Label.LabelStyle(game.loader.fonts.get("bebasBold"), Color.BLACK);
		lineCustomStyle = largeStyle;
		linearCustomStyle = largeStyle;
		nonLinearCustomStyle = smallStyle;
		exponentCustomStyle = new Label.LabelStyle(game.loader.fonts.get("exponent"), Color.BLACK);
		generateTable(equation, skin);
		updateSizeAndPos(uiStage);
		//this.setBackground(skin.getDrawable("background"));
		this.setTransform(true);
		this.setSize((float) (500 * Math.sqrt(ratio)), (float) (460 * Math.sqrt(ratio)));
		//this.addAction(Actions.alpha(Gdx.app.getPreferences("agentx").getFloat("equationTableOpacity")));
		Pixmap p = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		//p.setColor(Color.GRAY);
		p.setColor(1, 1, 1, 0.8f);
		p.fillRectangle(0, 0, 1, 1);
		Texture pixmaptex = new Texture(p);
		p.dispose();
		this.background(new TextureRegionDrawable(new TextureRegion(pixmaptex)));
		this.align(Align.bottom);
		this.padBottom(20);
		/*Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(1, 1, 1, 0.5f);
		pixmap.fill();
		Texture pixTexture = new Texture(pixmap);
		Image pixImage = new Image(pixTexture);
		pixImage.setSize(450, 400);
		pixImage.setPosition(screenWidth - 265, screenHeight / 2 + 50);
		uiStage.addActor(pixImage);*/
		

	}
	public void updateSizeAndPos(UIStage uiStage) {
		//this.setWidth(tableWidth);
		//this.setHeight(tableHeight);
		//this.setWidth(500);
		//this.setHeight(300);
		this.setSize((float) (500 * Math.sqrt(ratio)), (float) (460 * Math.sqrt(ratio)));
		this.setPosition((float) (Gdx.graphics.getWidth() - this.getWidth() - 40 * ratio), Gdx.graphics.getHeight() / 2 - this.getHeight() / 2 - 30 * ratio + 40);
		//this.setPosition(uiStage.gameStage.getGame().loader.screenWidth- this.getWidth(), uiStage.preGameUI.next.getHeight());
	}
	public void update(float delta) {

		if(acceleration < 0.1f) {
			acceleration+= delta/20*uiStage.gameStage.getAxes().get(uiStage.activeAxis).getHeight()/28;
		}
		TableButton notOver = null;
		for(Array<TableButton> b: buttons) {
			for(TableButton c: b) {
				if(c.isPressed()) {
					heldTime+= delta;
					notOver = c;
					if(heldTime > 0.2) {
					c.update(acceleration);
					}
				}
				else if(c.getPressedTime() > 0 && c.getPressedTime() < 200) {
					heldTime = 0;
					if(c.getPressedTime() != 0) {
					c.setPressedStartTime(0);
					c.setPressedTime(0);
					c.update(1, true);
					}
				}
			}
		}
		if(notOver == null) {
			acceleration = 0;
			heldTime =0;
		}


	}

	public void generateTable(Equation equation, Skin skin) {
		if (equation == null) return;
		myEquation = equation;
		mySkin = skin;
		Array<Float> formula = equation.getFormula();
		this.clearChildren();
		
		
		if(formula.get(1) == 0) {
			if(cValueEditable) {
				//add buttons
			}
			int cValuePosition;
			Array<String> middleLineChars = new Array<String>();
			// converted them to int temporarily, this may be changed in the
			// future
			// if changed can probably replaceAll Integer.toString to
			// Float.toString
			// and remove the (int)'s
			middleLineChars.add("y");
			middleLineChars.add("=");
			if (cValueEditable) {
				cValuePosition = middleLineChars.size;
				middleLineChars.add(String.format("%.1f",(myEquation.verticalTranslation)));
			}else {
				cValuePosition = -1;
			}
			for (int i = 0; i < middleLineChars.size; i++) {
				if (middleLineChars.get(i).replaceAll("\\D", "").length() > 0) {
					centerRow.add(new Label(middleLineChars.get(i), lineCustomStyle));
				} else if (middleLineChars.get(i).equals("[\\-\\+\\=]")) {
					centerRow.add(new Label(middleLineChars.get(i), lineCustomStyle)).padLeft(10).padRight(10);
				} else {
					centerRow.add(new Label(middleLineChars.get(i), lineCustomStyle));
				}
			}
			Label b = (new Label("-", lineCustomStyle));
			centerRow.add(b);
			centerRow.pack();
			tableWidth = centerRow.getWidth();
			centerRow.removeActor(b);
			
			for (int i = 0; i < middleLineChars.size; i++) {
				if (middleLineChars.get(i).replaceAll("\\D", "").length() > 0) {
					final TableButton upButton = new TableButton(uiStage.gameStage.getGame().loader.skin, "upArrow", 0, true, this.myEquation, this.myAxis, CustomTable.this);
					//this.add(upButton).size(tableWidth/middleLineChars.size, tableHeight/3);
					this.add(upButton).size(buttonSize, buttonSize).padBottom(buttonPadding);
					if (i == cValuePosition) {
						upButton.setValueToEdit(2);
						upButton.addListener(new Responder(upButton, equation, skin, CustomTable.this));
					}
					buttons.get(0).add(upButton);
				} else
					this.add();

			}
			this.row();
			for(Cell c: centerRow.getCells()) {
				if(c.getActor()!= null) {
					this.add(c.getActor());
				}
			}
			this.row();
			for (int i = 0; i < middleLineChars.size; i++) {
				if (middleLineChars.get(i).replaceAll("\\D", "").length() > 0) {
					final TableButton downButton = new TableButton(skin, "downArrow", 0, false, this.myEquation, this.myAxis, CustomTable.this);
					//this.add(downButton).size(tableWidth/middleLineChars.size, tableHeight/3);
					this.add(downButton).size(buttonSize, buttonSize).padTop(buttonPadding);
					if (i == cValuePosition) {
						downButton.setValueToEdit(2);
						downButton.addListener(new Responder(downButton, equation, skin, CustomTable.this));
					}
					buttons.get(1).add(downButton);
				} else
					this.add();

			}
		}
		if (formula.get(1) == 1) {
			int aValuePosition;
			int dValuePosition;
			int cValuePosition;
			int exponentPosition;
			Array<String> middleLineChars = new Array<String>();
			// converted them to int temporarily, this may be changed in the
			// future
			// if changed can probably replaceAll Integer.toString to
			// Float.toString
			// and remove the (int)'s
			middleLineChars.add("y");
			middleLineChars.add("=");
			if (aValueEditable) {
				aValuePosition = middleLineChars.size;
				middleLineChars.add(String.format("%.1f", myEquation.aValue));
				middleLineChars.add("x");

				if (cValueEditable && myEquation.verticalTranslation >= 0) {
					middleLineChars.add("+");
				}
			} else {
				aValuePosition = -1;
			}
			if (cValueEditable && !aValueEditable) {
				cValuePosition = middleLineChars.size;
				middleLineChars.add((String.format("%.1f", (myEquation.horizontalTranslation + myEquation.verticalTranslation))));
			} else if (cValueEditable) {
				cValuePosition = middleLineChars.size;
				middleLineChars.add(String.format("%.1f", (myEquation.horizontalTranslation + myEquation.verticalTranslation)));
			} else {
				cValuePosition = -1;
			}
			for (int i = 0; i < middleLineChars.size; i++) {
				if (middleLineChars.get(i).replaceAll("\\D", "").length() > 0) {
					centerRow.add(new Label(middleLineChars.get(i), linearCustomStyle));
				} else if (middleLineChars.get(i).equals("[\\-\\+\\=]")) {
					centerRow.add(new Label(middleLineChars.get(i), linearCustomStyle)).padLeft(10).padRight(10);
				} else {
					centerRow.add(new Label(middleLineChars.get(i), linearCustomStyle));
				}
			}
			Label b = (new Label("-", lineCustomStyle));
			centerRow.add(b);
			centerRow.pack();
			tableWidth = centerRow.getWidth();
			centerRow.removeActor(b);
			for (int i = 0; i < middleLineChars.size; i++) {
				if (middleLineChars.get(i).replaceAll("\\D", "").length() > 0) {
					final TableButton upButton = new TableButton(skin, "upArrow", 0, true, this.myEquation, this.myAxis, CustomTable.this);
					//this.add(upButton).size(tableWidth/middleLineChars.size, tableHeight/3);
					this.add(upButton).size(buttonSize, buttonSize).padBottom(buttonPadding);
					if (i == aValuePosition) {
						upButton.setValueToEdit(0);
						upButton.addListener(new Responder(upButton, equation, skin, CustomTable.this));
					}else if (i == cValuePosition) {
						upButton.setValueToEdit(2);
						upButton.addListener(new Responder(upButton, equation, skin, CustomTable.this));
					}
					buttons.get(0).add(upButton);
				} else {
					this.add();
				}
			}
			this.row();
			for(Cell c: centerRow.getCells()) {
				if(c.getActor()!= null) {
					this.add(c.getActor());
				}
			}
			this.row();
			for (int i = 0; i < middleLineChars.size; i++) {
				if (middleLineChars.get(i).replaceAll("\\D", "").length() > 0) {
					final TableButton downButton = new TableButton(skin, "downArrow", 0, false, this.myEquation, this.myAxis, CustomTable.this);
					//this.add(downButton).size(tableWidth/middleLineChars.size, tableHeight/3);
					this.add(downButton).size(buttonSize, buttonSize).padTop(buttonPadding);
					if (i == aValuePosition) {
						downButton.setValueToEdit(0);
						downButton.addListener(new Responder(downButton, equation, skin, CustomTable.this));
					}else if (i == cValuePosition) {
						downButton.setValueToEdit(2);
						downButton.addListener(new Responder(downButton, equation, skin, CustomTable.this));
					}
					buttons.get(1).add(downButton);
				} else {
					this.add();
				}

			}
		}

		// not linear, not done, mainly A value is a piece of shit
		else if (formula.get(1) >= 2){
				int aValuePosition;
				int dValuePosition;
				int cValuePosition;
				int exponentPosition;
				//middlelineChars is just anything not a button essentially
				Array<String> middleLineChars = new Array<String>();
				// converted them to int temporarily, this may be changed in the
				// future
				// if changed can probably replaceAll Integer.toString to
				// Float.toString
				// and remove the (int)'s
				middleLineChars.add("y");
				middleLineChars.add("=");
				aValuePosition = middleLineChars.size;
				if (aValueEditable) {
					//middleLineChars.add(Float.toString(myEquation.aValue));
					middleLineChars.add(String.format("%.1f", myEquation.aValue));
				}

				if (dValueEditable) {
					middleLineChars.add(" ( ");
					middleLineChars.add("x");
					if (myEquation.horizontalTranslation < 0) {
						middleLineChars.add("-");
					} else {
						middleLineChars.add("+");
					}
					dValuePosition = middleLineChars.size;
					middleLineChars.add(String.format("%.1f",(Math.abs(myEquation.horizontalTranslation))));
					middleLineChars.add(" ) ");
				} else {
					dValuePosition = -1;
					middleLineChars.add("x");
				}

				exponentPosition = middleLineChars.size;
				middleLineChars.add(""+ Float.toString(formula.get(1)).replaceAll(".0", ""));
				if (cValueEditable) {
					if (myEquation.verticalTranslation >= 0) {
						middleLineChars.add("+");
					} else {
						middleLineChars.add("-");
					}
					cValuePosition = middleLineChars.size;
					middleLineChars.add(String.format("%.1f",(Math.abs( myEquation.verticalTranslation))));
				} else {
					cValuePosition = -1;
				}
				for (int i = 0; i < middleLineChars.size; i++) {
					if (middleLineChars.get(i).replaceAll("\\D", "").length() > 0) {
						if (i == exponentPosition) {
							exponentActor = new Label(middleLineChars.get(i), exponentCustomStyle);
							centerRow.add(exponentActor);
						} else {
							centerRow.add(new Label(middleLineChars.get(i), nonLinearCustomStyle));
						}
					} else if (middleLineChars.get(i).equals("[\\-\\+\\=]")) {
						centerRow.add(new Label(middleLineChars.get(i), nonLinearCustomStyle)).padLeft(10).padRight(10);
					} else {
						centerRow.add(new Label(middleLineChars.get(i), nonLinearCustomStyle));
					}
				}
				Label sizeAddition = new Label("-", nonLinearCustomStyle);
				centerRow.add(sizeAddition);
				centerRow.pack();
				centerRow.removeActor((sizeAddition), false);
				tableWidth = centerRow.getWidth();
				for (int i = 0; i < middleLineChars.size; i++) {
					if (middleLineChars.get(i).replaceAll("\\D", "").length() > 0 && i != exponentPosition) {
						final TableButton upButton = new TableButton(skin, "upArrow", 0, true, this.myEquation, this.myAxis, CustomTable.this);
						//this.add(upButton).size(tableWidth/middleLineChars.size, tableHeight/3);
						this.add(upButton).size(buttonSize * 0.8f, buttonSize * 0.8f).padBottom(buttonPadding);
						if (i == aValuePosition) {
							upButton.setValueToEdit(0);
							upButton.addListener(new Responder(upButton, equation, skin, CustomTable.this));
						} else if (i == dValuePosition) {
							upButton.setValueToEdit(1);
							upButton.addListener(new Responder(upButton, equation, skin, CustomTable.this));
						} else if (i == cValuePosition) {
							upButton.setValueToEdit(2);
							upButton.addListener(new Responder(upButton, equation, skin, CustomTable.this));
						}
						buttons.get(0).add(upButton);
					} else {
						this.add();
					}
				}
				this.row();
				for(Cell c: centerRow.getCells()) {
					if(c.getActor()!= null) {
						this.add(c.getActor());
					}
				}
				if(exponentActor!= null) {
					this.getCell(exponentActor).padBottom(tableHeight/10);
				}
				this.row();
				for (int i = 0; i < middleLineChars.size; i++) {
					if (middleLineChars.get(i).replaceAll("\\D", "").length() > 0 && i != exponentPosition) {
						final TableButton downButton = new TableButton(skin, "downArrow", 0, false, this.myEquation, this.myAxis, CustomTable.this);
						//this.add(downButton).size(tableWidth/middleLineChars.size, tableHeight/3);
						this.add(downButton).size(buttonSize * 0.8f, buttonSize * 0.8f).padTop(buttonPadding);
						if (i == aValuePosition) {
							downButton.setValueToEdit(0);
							downButton.addListener(new Responder(downButton, equation, skin, CustomTable.this));
						} else if (i == dValuePosition) {
							downButton.setValueToEdit(1);
							downButton.addListener(new Responder(downButton, equation, skin, CustomTable.this));
						} else if (i == cValuePosition) {
							downButton.setValueToEdit(2);
							downButton.addListener(new Responder(downButton, equation, skin, CustomTable.this));
						}
						buttons.get(1).add(downButton);
					} else {
						this.add();
					}

				}
		}
		
		uiStage.gameStage.renderAxes(true);
	}

	public String[] getTableText() {
		return tableText;
	}

	public void setTableText(String[] tableText) {
		this.tableText = tableText;
	}
}