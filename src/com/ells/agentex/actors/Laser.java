package com.ells.agentex.actors;

import java.math.BigDecimal;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.gushikustudios.rube.RubeScene;

public class Laser extends Actor {
	Rectangle border;
	Vector2 startPosition;
	private double angle;

	Array<Vector2> laserPoints = new Array<Vector2>();
	public Array<Vector2> getLaserPoints() {
		return laserPoints;
	}
	World world;
	private Color color;
	private Array<Body> body = new Array<Body>(); 
	private RubeScene scene;
	private Stage stage;
	private FixtureDef endSensor = new FixtureDef();
	public Body tip;
	public float xAdder;
	public float yAdder;
	public Laser(float angle, Color color, Vector2 startPosition, Rectangle border, World world, RubeScene scene, Stage stage) {
		this.setPosition(startPosition.x, startPosition.y);
		this.world = world;
		this.stage = stage;
		this.scene = scene;
		this.color = color;
		this.border = border;
		this.startPosition = startPosition;
		this.angle = (angle/180.0 * Math.PI);
		xAdder = (float) (Math.cos(this.angle) / 10f);
		yAdder = (float) (Math.sin(this.angle) / 10f);
		generateLaserPoints();
		CircleShape p = new CircleShape();
		p.setPosition(new Vector2(laserPoints.get(0).x, laserPoints.get(0).y));
		p.setRadius(0.1f);
		endSensor.shape = p;
		BodyDef bd = new BodyDef();
		bd.type = BodyType.StaticBody;
		bd.active = true;
		bd.awake = true;
		bd.gravityScale = 0;
		//so move tip to check if laser hits or needs to be exteneded.
		tip = world.createBody(bd);
		tip.setUserData("BOX");
		tip.createFixture(endSensor);
		tip.setActive(false);
		tip.setTransform(0,0, 0);
	}
	public void generateLaserPoints() {
		laserPoints.add(startPosition);
		
		Vector2 currentEndPoint = new Vector2(startPosition);
		while (stage.hit(currentEndPoint.x + xAdder, currentEndPoint.y + yAdder, true) == null && border.contains(currentEndPoint)) {
			currentEndPoint.add(new Vector2(xAdder, yAdder));
		}
		laserPoints.add(currentEndPoint);
		createLine();
	}

	public void generateLaserEndPoint(Vector2 endPoint) {
		laserPoints.clear();
		laserPoints.add(startPosition);
		Vector2 currentEndPoint = new Vector2(startPosition);
		float xAdder = (float) (Math.cos(angle) / 10f);
		float yAdder = (float) (Math.sin(angle) / 10f);
		xAdder = round(xAdder, 5);
		yAdder = round(yAdder, 5);
		if(body.size > 0) {
			for(Body b: body) {
				b.setUserData("remove");
			}
		}
		body.clear();
		while(currentEndPoint.dst(endPoint) > 0.21f || (Math.abs(endPoint.x-currentEndPoint.x)>1)) {
			//the 0.11f is because the math.cos(angle) is divided by 10 therefore largest add/subtract value is 0.1
			currentEndPoint.add(new Vector2(xAdder, yAdder));
		}
		laserPoints.add(currentEndPoint);
		createLine();
		/**
		if(xAdder >= 0) {
			System.out.println("+");
			if(yAdder >= 0) {
				System.out.println("++ "+currentEndPoint);
				while (currentEndPoint.x < endPoint.x) { // && currentEndPoint.y < endPoint.y
					currentEndPoint.add(new Vector2(xAdder, yAdder));
				}
			} else {
				System.out.println("+-"+currentEndPoint);
				while (currentEndPoint.x < endPoint.x) { // && currentEndPoint.y > endPoint.y
					System.out.println(currentEndPoint.x +" vs " +endPoint.x);
					currentEndPoint.add(new Vector2(xAdder, yAdder));
				}
			}
		} else {
			System.out.println("-");
			if(yAdder <= 0) {
				System.out.println("-- "+currentEndPoint);
				System.out.println(currentEndPoint.x +" vs " + endPoint.x + "\n and " +currentEndPoint.y +" vs "+ endPoint.y);
				while (currentEndPoint.x > endPoint.x) { // && currentEndPoint.y > endPoint.y
					currentEndPoint.add(new Vector2(xAdder, yAdder));
				}
			} else {
				System.out.println("-+"+currentEndPoint);
				while (currentEndPoint.x > endPoint.x) { // && currentEndPoint.y < endPoint.y
					currentEndPoint.add(new Vector2(xAdder, yAdder));
				}
			}
		}
		**/
		laserPoints.add(currentEndPoint);
		for(Vector2 l: laserPoints) {
			System.out.println(l);
		}
	}
	public Vector2 getEndPoint() {
		return laserPoints.get(laserPoints.size-1);
	}
	public void setLaserPoints(Array<Vector2> laserPoints) {
		this.laserPoints = laserPoints;
	}
	public void createLine() {
		System.out.println("laserShit");
		for(Vector2 l: laserPoints) {
			System.out.println(l);
		}
			BodyDef bd = new BodyDef();
			bd.type = BodyType.DynamicBody;
			bd.active = true;
			bd.awake = true;
			bd.gravityScale = 0;
			FixtureDef fd = new FixtureDef();
			fd.friction = 0.3f;
			fd.restitution = 0.5f;
			fd.density = 0.1f;
			//fd.filter.categoryBits = 4;
			//fd.filter.maskBits = (short) 65532;
			ChainShape ps = new ChainShape();
			ps.createChain((Vector2[]) laserPoints.toArray(Vector2.class));
			fd.shape = ps;
			//Body body = world.createBody(bd);
			//GameStage.bodiesToBeAdded.add(bd);
			//GameStage.fixturesToBeAdded.add(fd);
			//GameStage.lasersToBeDeleted.add(this);
			/**
			body.createFixture(fd);
			setBody(body);
			scene.addBodies(this.body);
			if(this.body.size > 1) {
			this.body.get(1).setUserData("laser");
			} else {
				this.body.get(0).setUserData("laser");
			}
			**/

	}
	/**
	public void setBody(Body body) {
		this.body.clear();
		this.body.add(body);
	}
	**/
	//so will have to mark body to be removed then remove when game updates next.
	//I think this is currently useless
	public void update(Vector2 collisionPoint) {
		System.out.println("CollisionPoint "+collisionPoint);
		laserPoints.clear();
		laserPoints.add(startPosition);
		collisionPoint.add(new Vector2((float)-(Math.cos(angle)/10), (float)-(Math.sin(angle)/10)));
		laserPoints.add(collisionPoint);
		if(body.size > 0) {
			for(Body b: body) {
				b.setUserData("remove");
			}
		}
		body.clear();
		createLine();
		
	}
	public void update() {
		Vector2 d = new Vector2(laserPoints.get(laserPoints.size-1).add(this.xAdder, this.yAdder));
		laserPoints.clear();
		laserPoints.add(startPosition);
		laserPoints.add(d);
		if(body.size > 0) {
			for(Body b: body) {
				b.setUserData("remove");
			}
		}
		body.clear();
		createLine();
	}
	public void setBody(Body body) {
		this.body.clear();
		this.body.add(body);
	}
	public void nonContactUpdate() {
		
	}
	public double getAngle() {
		return angle;
	}
	public Vector2 getStartPosition() {
		return startPosition;
	}
	public void setStartPosition(Vector2 startPosition) {
		this.startPosition = startPosition;
	}
	public void setAngle(double angle) {
		this.angle = angle;
	}
	//http://stackoverflow.com/questions/8911356/whats-the-best-practice-to-round-a-float-to-2-decimals
    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
}
