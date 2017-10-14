package com.ells.agentex.actors;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RopeJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.ells.agentex.AgentExGame;
import com.ells.agentex.stages.NewGameStage;
import com.gushikustudios.rube.RubeScene;
import com.badlogic.gdx.physics.box2d.Fixture;

public class Player {

	private String file = "rube/bicycle.json";
	private boolean spinLeft = false;
	private boolean spinRight = false;
	private boolean gas = false;
	private boolean brake = false;
	private boolean isDead = false;
	private boolean boosting = false;
	private float boostAngle;
	// vars for flips
	private boolean inAir = false;
	private int wheelCollisions = 0;
	private float frontFlips = 0;
	private float backFlips = 0;

	private float speed = 0;
	private float spin = 0;
	private Array<Body> bodies;
	private Array<Body> playerBodies = new Array<Body>();
	private Array<Body> boostBodies = new Array<Body>();
	private float delta;
	private boolean active = false;
	private Array<Joint> joints;
	private Array<Body> wheels;
	private Body theFrame, theTorso;
	private float airTime = 0;
	private float spd, acc, control;
	private Map<String, Object> levelProfile = new HashMap<String, Object>();

	private Array<Joint> connectors = new Array<Joint>();
	public Array<String> playerImageNames = new Array<String>();
	public Body torso, head, wheel1, wheel2, frame, pedal;
	private int vehicleNum;
	
	public Player() {
		playerImageNames.addAll("shin", "thigh", "torso", "upperarm", "head", "forearm", "frame", "wheel", "ski", "topbar", "backbar", "tread", "clearPixel");
	}

	public void indetifyVehicleParts(RubeScene scene, AgentExGame game) {
		bodies = scene.getBodies();
		wheels = new Array<Body>();
		World world = scene.getWorld();
		vehicleNum = (Integer) game.loader.getProfile().get("selectedVehicle");
		Map<String, Object> stats = game.loader.nestedLevelProfile
				.get("v" + game.loader.getProfile().get("selectedVehicle"));
		spd = (Float) stats.get("speed");
		//adjust speed on bmx for smaller wheels
		if (vehicleNum == 4 || vehicleNum == 8) {
			System.out.println("BMX SELECTED");
			//spd = spd + 1f;
		}
		acc = (Float) stats.get("acceleration");
		control = (Float) stats.get("control");
		/*
		 * spd = 50; acc = 40; control = 15;
		 */

		if ((bodies != null) && (bodies.size > 0)) {
			for (int i = 0; i < bodies.size; i++) {
				Body body = bodies.get(i);
				String type = (String) scene.getCustom(body, "type", "none");
				// dont assign to dynamic objects
				if (!type.equals("object") && !type.equals("object2")) {
					boostBodies.add(bodies.get(i));
				}
				// assign bodies to entity
				if (type.equals("head")) {
					head = bodies.get(i);
					head.setUserData("head");
					playerBodies.add(bodies.get(i));
				}
				if (type.equals("wheel1")) {
					wheel1 = bodies.get(i);
					wheel1.setUserData("wheel");
					wheels.add(bodies.get(i));
					playerBodies.add(bodies.get(i));

				} else if (type.equals("wheel2")) {
					wheel2 = bodies.get(i);
					wheel2.setUserData("wheel");
					playerBodies.add(bodies.get(i));
					
				} else if (type.equals("pedal")) {
					pedal = bodies.get(i);
					pedal.setAngularDamping(0.4f);
					pedal.setAngularVelocity(3);
				} else if (type.equals("frame")) {
					frame = bodies.get(i);
					playerBodies.add(bodies.get(i));
					
				} else if (type.equals("body")) {
					torso = bodies.get(i);
					torso.setUserData("body");
					playerBodies.add(bodies.get(i));
				}
				//playerBodies.add(bodies.get(i));
			}
			
			System.out.println("NUM OF PLAYER BODIES: " + playerBodies.size);
			
			joints = scene.getJoints();
			if ((joints != null) && (joints.size > 0)) {
				for (int i = 0; i < joints.size; i++) {
					Joint joint = joints.get(i);
					if (joints.get(i).getType().toString().equals("WeldJoint")) {
						((WeldJoint) joints.get(i)).setDampingRatio((Float) scene.getCustom(joint, "dampingRatio", 0));
						((WeldJoint) joints.get(i)).setFrequency((Float) scene.getCustom(joint, "frequency", 0));
					} else if (joints.get(i).getType().toString().equals("RopeJoint")) {
						((RopeJoint) joints.get(i)).setUserData(null);
					}

					// System.out.print(joints.get(i).getType());
					Boolean type = (Boolean) scene.getCustom(joint, "detach", false);
					// assign bodies to entity
					if (type == true) {
						connectors.add(joints.get(i));
					}
				}
			}
		}
	}

	public float getAirTime() {
		return airTime;
	}

	public void setAirTime(float airTime) {
		this.airTime = airTime;
	}

	public void update(float delta) {
		if (boosting) {
			float force = 0.9f;
			if (vehicleNum == 4 || vehicleNum == 8) { force = 1.2f; }
			System.out.println("FORCE: " + force);
			double angle = (double) -(boostAngle);
			// System.out.println("x: " +(force * Math.cos(angle))+ " y: "
			// +(force * Math.sin(angle)));
			// System.out.println("lin vel: " + frame.getLinearVelocity());
			for (Body bod : boostBodies) {
				// bod.setLinearVelocity(0, 0);
				bod.setLinearVelocity((float) (frame.getLinearVelocity().x + Math.sin(angle) * force),
						(float) (frame.getLinearVelocity().y + Math.cos(angle) * force));
			}
			/*
			 * frame.setLinearVelocity((float) (frame.getLinearVelocity().x +
			 * Math.sin(angle) * force), (float) (frame.getLinearVelocity().y +
			 * Math.cos(angle) * force)); frame.setAngularVelocity(0);
			 */
			// torso.(new Vector2(0, 4), torso.getLocalCenter(), true);
			// frame.applyLinearImpulse(new Vector2(0, 4),
			// frame.getLocalCenter(), true);
			boosting = false;
			this.isDead = false;
			wheelCollisions = 0;
			
		}
		
		if (active) {
			airTime += delta;
			// back pedaling
			if (pedal != null) {
				// back pedaling
				if (wheel1.getAngularVelocity() > 0) {
					pedal.setAngularVelocity(wheel1.getAngularVelocity() / 5f);
				}
				// moderate bursts of pedaling
				if (wheel1.getAngularVelocity() < -10) {
					pedal.setAngularVelocity(-10);
				}
			}
			if (isBrake()) {
				// wheel1.setAngularVelocity(0);
				for (Body b : wheels) {
					b.setAngularVelocity(0);
				}
				if (wheel2 != null) {
					wheel2.setAngularVelocity(0);
				}
				if (pedal != null) {
					pedal.setAngularVelocity(0);
				}
			}

			else if (isGas()) {
				// original max was 50
				// if (Math.abs(wheel1.getAngularVelocity()) < 50) {
				if (!boosting) {
				for (Body b : wheels) {
					float rad = b.getFixtureList().first().getShape().getRadius();
					float maxW = (spd / 3.5f) / rad;
					// original was 40
					if (Math.abs(b.getAngularVelocity()) < maxW) {
						b.applyTorque((-acc * delta), true);
						if (pedal != null) {
							pedal.setAngularVelocity(b.getAngularVelocity() / 5f);
						}
					}
					// }

					/*
					 * wheel1.applyTorque((-25f * delta), true); if (pedal !=
					 * null) {
					 * pedal.setAngularVelocity(wheel1.getAngularVelocity() /
					 * 5f); }
					 */

				}
				}
			}

			// to control maximum spin speed
			// float spin = frame.getAngularVelocity();
			// original is fram 20 torso 10
			if (Math.abs(frame.getAngularVelocity()) < 8) {
				if (isSpinLeft()) {
					frame.applyTorque((control * delta), true);
					torso.applyTorque((8f * delta), true);
				} else if (isSpinRight()) {
					frame.applyTorque((-control * delta), true);
					torso.applyTorque((-8f * delta), true);
				}
			}

			// System.out.print(wheelCollisions + "\n");
			// flip counter stuff
			if (wheelCollisions <= 1) {
				spin = frame.getAngularVelocity() * delta;
				if (spin >= 0) {
					backFlips += spin;
				} else {
					frontFlips -= spin;
				}
			}

			else {
				frontFlips = frontFlips / 6.283f + 0.25f;
				backFlips = backFlips / 6.283f + 0.25f;
				/*
				 * if (frontFlips >= 1) {
				 * 
				 * System.out.print(frontFlips + "\n");
				 * System.out.print("FRONTFLIP" + "\n"); frontFlips = 0; } if
				 * (backFlips >= 1) {
				 * 
				 * System.out.print(backFlips + "\n");
				 * System.out.print("BACKFLIP" + "\n"); backFlips = 0; }
				 */
				/*
				 * if (frontFlips >= 1 || backFlips >= 1) {
				 * GameStage.flipAction(frontFlips, backFlips); }
				 */

			}

			// System.out.print("Wheel collisions: " + wheelCollisions + "\n" +
			// "Rotation: " + frame.getAngularVelocity()*delta);
		}
	}

	public void die() {

		this.isDead = false;

	}

	public Array<Joint> getConnectors() {
		return connectors;
	}

	/*public Body getPlayer() {
		if (torso != null) {
			return torso;
		} else {
			return frame;
		}
	}*/
	public Body getPlayer() {
		if (pedal != null) { return pedal; }
		else { return frame; }
	}

	public String getType() {
		return file;
	}

	public void setType(String fileName) {
		this.file = fileName;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getSpin() {
		return spin;
	}

	public void setSpin(float spin) {
		this.spin = spin;
	}

	public boolean isSpinLeft() {
		return spinLeft;
	}

	public void setSpinLeft(boolean spinLeft) {
		this.spinLeft = spinLeft;
	}

	public boolean isSpinRight() {
		return spinRight;
	}

	public void setSpinRight(boolean spinRight) {
		this.spinRight = spinRight;
	}

	public boolean isGas() {
		return gas;
	}

	public void setGas(boolean gas) {
		this.gas = gas;
	}

	public boolean isBrake() {
		return brake;
	}

	public void setBrake(boolean brake) {
		this.brake = brake;
	}

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {

		this.isDead = isDead;

	}

	public boolean inAir() {
		return inAir;
	}

	public void setAir(boolean inAir) {

		this.inAir = inAir;

	}

	public boolean isActive() {
		return active;
	}

	public void applyForce(Body body, float delta) {
		for (Body b : playerBodies) {
			float planetRadius = 0.1f;
			Vector2 position = body.getPosition();
			Vector2 planetDistance = new Vector2(0, 0);
			planetDistance.add(b.getPosition());
			planetDistance.add(-position.x, -position.y);

			float finalDistance = planetDistance.len();

			if (finalDistance >= planetRadius) {
				float vecSum = Math.abs(planetDistance.x) + Math.abs(planetDistance.y);
				planetDistance.x *= -((1 / vecSum) * planetRadius / finalDistance);
				planetDistance.y *= -((1 / vecSum) * planetRadius / finalDistance);
				b.applyForceToCenter(planetDistance, false);
			}
		}
	}

	public void setActive(boolean active) {
		if (active) {
			for (Body b : playerBodies) {
				b.setGravityScale(1);
				b.setAwake(true);
				//System.out.println("SET ACTIVE TRUE");
			}
		} else {
			for (Body b : playerBodies) {
				b.setGravityScale(0);
				b.setAwake(false);
				//System.out.println("SET ACTIVE FALSE");
			}
		}
		this.active = active;

	}

	public int getWheelCollisions() {
		return wheelCollisions;
	}

	public void addWheelCollisions() {
		this.wheelCollisions++;
	}

	public void subWheelCollisions() {
		this.wheelCollisions--;
	}

	public float getFrontFlips() {
		float tmp = frontFlips / 6.283f + 0.3f;
		frontFlips = 0.3f;
		return tmp;
	}

	public void setFrontFlips(int frontFlips) {
		this.frontFlips = frontFlips;
	}

	public float getBackFlips() {
		float tmp = backFlips / 6.283f + 0.3f;
		backFlips = 0.3f;
		return tmp;
	}

	public void setBackFlips(int backFlips) {
		this.backFlips = backFlips;
	}

	public float getBoostAngle() {
		return boostAngle;
	}

	public void setBoostAngle(float boostAngle) {
		this.boostAngle = boostAngle;
	}

	public boolean getBoosting() {
		return boosting;
	}

	public void setBoosting(boolean boosting) {
		this.boosting = boosting;
	}



	public Array<Body> getPlayerBodies() {
		return playerBodies;
	}

}
