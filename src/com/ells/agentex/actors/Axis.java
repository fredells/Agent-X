package com.ells.agentex.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.ells.agentex.stages.UIStage;
import com.ells.agentex.utils.SimpleSpatial;
import com.gushikustudios.rube.RubeScene;

public class Axis extends Actor {
	private Vector2 position;
	private float height;
	private float width;
	private Equation equation;
	public RubeScene scene;
	private World world;
	private float axisZoom;
	private Rectangle hitBox;
	private Array<Body> body = new Array<Body>();
	private CustomTable equationTable;
	private Skin skin;
	private UIStage uiStage;
	private Array<Vector2> verts;
	private int num;
	private Vector3 center;
	private boolean built = false;
	private boolean hidden = false;

	public Axis(Vector2 position, float height, float width, RubeScene scene, World world, Skin skin, UIStage game, Boolean isHidden, int num) {
		this.skin = skin;
		this.num = num;
		this.position = position;
		this.height = 10;
		this.uiStage = game;
		this.width = width;
		this.setHidden(isHidden);
		// make the float smaller to set default zoom further out
		axisZoom = height / 40;
		this.scene = scene;
		this.world = world;
		this.setBounds(position.x, position.y, width, height);
		hitBox = new Rectangle(position.x - width / 2, position.y - height / 2, width, height);
		center = new Vector3(hitBox.getCenter(new Vector2()), 0);
		setupTable();

	}

	public UIStage getGame() {
		return uiStage;
	}

	public void setGame(UIStage game) {
		this.uiStage = game;
	}

	public Vector2[] equationToPoints() {
		//create initial points
		Array<Vector2> points = new Array<Vector2>();
		for (float i = 10/2; i >= -10/2; i -= 10f / 1000f) {
			float yValue = equation.output(i);
			if (!Float.isNaN(yValue)) {
				points.add(new Vector2(i, yValue));
			}
		}
		// remove out of the box points
		for (int j = 0; j < points.size; j++) {
			if (points.get(j).y > height / 2 || points.get(j).y < -height / 2) {
				points.removeIndex(j);
				j--;
			}

		}
		for (Vector2 v : points) {
			v.x*= width/10;
			v.y *= height/10;
		}

		verts = points;
		//parse into a line or multiple lines if required
		Array<Vector2> line = new Array<Vector2>();
		
		//grab outside points only for linear equations
		if (equation.power == 0 || equation.power == 1) {
			if (points.size > 1) {
				line.add(points.first());
				line.add(points.get(points.size - 1));
				if (equation.getLines() == 0) {
					equation.setLine1((Vector2[]) line.toArray(Vector2.class));
					equation.setLines(equation.getLines() + 1);
				} else if (equation.getLines() == 1) {
					equation.setLine2((Vector2[]) line.toArray(Vector2.class));
					equation.setLines(equation.getLines() + 1);
				}
			}
		}
		
		//quadratic line
		else if (equation.power == 2) {
			if (points.size > 1) {
				Array<Vector2> tmp = new Array<Vector2>();
				//step by 5 to reduce the amount of points but add the last point
				for (int p = 0; p < points.size - 6; p += 5) {
					tmp.add(points.get(p));
				}
				tmp.add(points.get(points.size-1));
				points = tmp;
				
				//split lines if there is a gap
				for (int k = 1; k < points.size; k++) {
					if ((points.get(k).x - points.get(k - 1).x < -0.25) || k == points.size - 1) {
						line = new Array<Vector2>();
						for (int j = k; k > 1; k--) {
							line.add(points.removeIndex(k - 1));
						}
						if (equation.getLines() == 0) {
							equation.setLine1((Vector2[]) line.toArray(Vector2.class));
							equation.setLines(equation.getLines() + 1);
						} else if (equation.getLines() == 1) {
							equation.setLine2((Vector2[]) line.toArray(Vector2.class));
							equation.setLines(equation.getLines() + 1);
						}
					}
				}
			}
		}

		return verts.toArray(Vector2.class);
		
	}

	public void createLine(RubeScene scene, Vector2[] chain) {
		if (equation.getLine1() != null) {
			built = true;
			BodyDef bodyDef = new BodyDef();
			bodyDef.position.set(position);
			bodyDef.active = true;
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.friction = 0.3f;
			fixtureDef.restitution = 0.2f;
			fixtureDef.density = 1;
			//chainshapes need to be split into multiple chain shapes so a line isn't drawn between two points outside the grid
			ChainShape chainShape = new ChainShape();
			chainShape.createChain(equation.getLine1());
			fixtureDef.shape = chainShape;
			Body body = world.createBody(bodyDef);
			if (equation.getLine2() != null) {
				FixtureDef fixtureDef2 = new FixtureDef();
				fixtureDef2.friction = 0.3f;
				fixtureDef2.restitution = 0.5f;
				fixtureDef2.density = 1;
				ChainShape ps2 = new ChainShape();
				ps2.createChain(equation.getLine2());
				fixtureDef2.shape = ps2;
				body.createFixture(fixtureDef2);
				ps2.dispose();
			}
			body.createFixture(fixtureDef);
			chainShape.dispose();
			setBody(body);
			scene.addBodies(this.body);
		}

	}
	public void setupTable() {
		equationTable = new CustomTable(equation, Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), skin, this,
				uiStage);
	}

	public void destroyBody(Body body) {
		world.destroyBody(body);
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public Equation getEquation() {
		return equation;
	}

	public void setEquation(Equation equation) {
		this.equation = equation;
	}

	public Array<Body> getBodyArray() {
		return body;
	}

	public void setBody(Body body) {
		this.body.clear();
		this.body.add(body);
	}

	public Body getBody() {
		return body.get(0);
	}

	public float getAxisZoom() {
		return axisZoom;
	}

	public void setAxisZoom(float axisZoom) {
		this.axisZoom = axisZoom;
	}

	public Rectangle getHitBox() {
		return hitBox;
	}

	public void setHitBox(Rectangle hitBox) {
		this.hitBox = hitBox;
	}

	public CustomTable getEquationTable() {
		return equationTable;
	}

	public void setEquationTable(CustomTable equationTable) {
		this.equationTable = equationTable;
	}

	public void setWorld(World mWorld) {
		this.world = mWorld;
		// TODO Auto-generated method stub
	}
	
	public boolean isBuilt() {
		return built;
	}
	public void setBuilt(boolean built) {
		this.built = built;
	}
	public boolean isHidden() {
		return hidden;
	}
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public int getNum() {
		// TODO Auto-generated method stub
		return num;
	}

	public Vector3 getCenter() {
		// TODO Auto-generated method stub
		return center;
	}
}
