package edu.mbhs.cs.bubbles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.sun.org.apache.bcel.internal.generic.ARRAYLENGTH;

import java.sql.Time;
import java.util.Arrays;
import java.util.List;

/**
 * Class representing the player, a small circle in the sea of bubbles
 */
public class PlayerAI extends Actor implements Steerable<Vector2> {

	public static final float RADIUS = 5f;
	public static final float METERS_TO_PIXELS = Bubble.METERS_TO_PIXELS;

	private static final float BASE_SPEED = 900;

	public int score = 0;

	private boolean tagged;
	private float maxLinearSpeed, maxLinearAcceleration;
	private float maxAngularSpeed, maxAngularAcceleration;
	private float zeroLinearSpeedThreshold;

	private SteeringBehavior<Vector2> behavior;
	private SteeringAcceleration<Vector2> steeringAcceleration;

	private float x, y;
	private Body body;
	private Fixture fixture;
	private TextureRegion texture = new TextureRegion();

	private World world;

	/**
	 * Makes a new player
	 */
	public PlayerAI(World w) {
		world = w;

		texture.setTexture(new Texture(Gdx.files.internal("mandleplayer.png")));
		texture.setRegion(texture.getTexture());

		x = Gdx.graphics.getWidth() / 2;
		y = Gdx.graphics.getHeight() / 2;
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(x/ METERS_TO_PIXELS, y/ METERS_TO_PIXELS);
		body = w.createBody(bodyDef);

		CircleShape shape = new CircleShape();
		shape.setRadius(RADIUS);
		FixtureDef def = new FixtureDef();
		def.shape = shape;
		def.density = 0.09f;
		def.restitution = 0.77f;
		def.friction = 0.8f;

		fixture = body.createFixture(def);
		fixture.setUserData(Arrays.asList("AI", String.valueOf(body.getPosition().x), String.valueOf(body.getPosition().y)));
		shape.dispose();
	}

	@Override
	public void act(float delta) {
		fixture.setUserData(Arrays.asList("AI", String.valueOf(body.getPosition().x), String.valueOf(body.getPosition().y)));
		updateBounds();
		updateBehavior();
		update(delta);
	}

	private void updateBehavior() {
		SteeringBehavior<Vector2> behavior = null;

		Body[] bodies = new Body[TimeTrial.BUBBLE_NUMBER + TimeTrial.FLAG_NUMBER + 2];
		world.getBodies(Array.with(bodies));
		Body closest = null;
		float minDist = getStage().getWidth() / 5;
		for (Body body : bodies) {
			float dist = body.getPosition().dst(this.body.getPosition());
			if (body != this.body && dist < minDist) {
				minDist = dist;
				closest = body;
			}
		}

		String name = "";
		if (closest != null) {
			name = (String) ((List) closest.getUserData()).get(0);
		}

		if (name.equals("Bubble")) {
			// if about to collide with bubble, avoid collision
		}
		else if (name.equals("Player")) {
			// if about to collide with player, evade

		}
		else if (name.equals("Flag")) {
			// if in valid radius of flag, seek flag
		}
		else {
			// otherwise wander
			behavior = new Wander<Vector2>(this)
					.setWanderRadius(500);
		}

		setBehavior(behavior);
	}

	public void update(float delta) {
		if (behavior != null) {
			behavior.calculateSteering(steeringAcceleration);
			applySteering(delta);
		}
	}

	private void applySteering(float delta) {
		boolean anyAccelerations = false;

		if (!steeringAcceleration.linear.isZero()) {
			Vector2 force = steeringAcceleration.linear.scl(delta);
			body.applyForceToCenter(force, true);
			anyAccelerations = true;
		}

		if (anyAccelerations) {
			Vector2 velocity = body.getLinearVelocity();
			if (velocity.len2() > maxLinearSpeed * maxLinearSpeed) {
				body.setLinearVelocity(velocity.scl(maxLinearSpeed / (float) Math.sqrt(velocity.len2())));
			}
		}
	}

	public Body getBody() {
		return body;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(
				texture,
				(body.getPosition().x - RADIUS) * METERS_TO_PIXELS,
				(body.getPosition().y - RADIUS) * METERS_TO_PIXELS,
				518f / texture.getRegionWidth()* RADIUS * METERS_TO_PIXELS,
				(686f - 340f) / texture.getRegionHeight() * RADIUS * METERS_TO_PIXELS,
				RADIUS * 2 * METERS_TO_PIXELS,
				RADIUS * 2 * METERS_TO_PIXELS,
				1f,
				texture.getRegionHeight() / texture.getRegionWidth(),
				body.getLinearVelocity().angle() - 90,
				true
		);
	}

	private void updateBounds() {
		this.setBounds(x, y, RADIUS * 2 * METERS_TO_PIXELS, RADIUS * 2 * METERS_TO_PIXELS);
	}

	public Rectangle getBounds(){
		return new Rectangle(x, y, RADIUS * 2 * METERS_TO_PIXELS, RADIUS * 2 * METERS_TO_PIXELS);
	}

	@Override
	public Vector2 getLinearVelocity() {
		return body.getLinearVelocity();
	}

	@Override
	public float getAngularVelocity() {
		return body.getAngularVelocity();
	}

	@Override
	public float getBoundingRadius() {
		return RADIUS;
	}

	@Override
	public boolean isTagged() {
		return tagged;
	}

	@Override
	public void setTagged(boolean tagged) {
		this.tagged = tagged;
	}

	@Override
	public float getZeroLinearSpeedThreshold() {
		return zeroLinearSpeedThreshold;
	}

	@Override
	public void setZeroLinearSpeedThreshold(float value) {
		zeroLinearSpeedThreshold = value;
	}

	@Override
	public float getMaxLinearSpeed() {
		return maxLinearSpeed;
	}

	@Override
	public void setMaxLinearSpeed(float maxLinearSpeed) {
		this.maxLinearSpeed = maxLinearSpeed;
	}

	@Override
	public float getMaxLinearAcceleration() {
		return maxLinearAcceleration;
	}

	@Override
	public void setMaxLinearAcceleration(float maxLinearAcceleration) {
		this.maxLinearAcceleration = maxLinearAcceleration;
	}

	@Override
	public float getMaxAngularSpeed() {
		return maxAngularSpeed;
	}

	@Override
	public void setMaxAngularSpeed(float maxAngularSpeed) {
		this.maxAngularSpeed = maxAngularSpeed;
	}

	@Override
	public float getMaxAngularAcceleration() {
		return maxAngularAcceleration;
	}

	@Override
	public void setMaxAngularAcceleration(float maxAngularAcceleration) {
		this.maxAngularAcceleration = maxAngularAcceleration;
	}

	@Override
	public Vector2 getPosition() {
		return body.getPosition();
	}

	@Override
	public float getOrientation() {
		return body.getAngle();
	}

	@Override
	public void setOrientation(float orientation) {
	}

	@Override
	public float vectorToAngle(Vector2 vector) {
		return Util.vectorToAngle(vector);
	}

	@Override
	public Vector2 angleToVector(Vector2 outVector, float angle) {
		return Util.angleToVector(outVector, angle);
	}

	@Override
	public Location<Vector2> newLocation() {
		return new Box2dLocation();
	}

	public void setBehavior(SteeringBehavior<Vector2> behavior) {
		this.behavior = behavior;
	}

	public SteeringBehavior<Vector2> getBehavior() {
		return behavior;
	}

}

class Box2dLocation implements Location<Vector2> {

	private Vector2 position;
	private float orientation;

	public Box2dLocation() {
		position = new Vector2();
		orientation = 0;
	}

	@Override
	public Vector2 getPosition() {
		return position;
	}

	@Override
	public float getOrientation() {
		return orientation;
	}

	@Override
	public void setOrientation(float orientation) {
		this.orientation = orientation;
	}

	@Override
	public float vectorToAngle(Vector2 vector) {
		return Util.vectorToAngle(vector);
	}

	@Override
	public Vector2 angleToVector(Vector2 outVector, float angle) {
		return Util.angleToVector(outVector, angle);
	}

	@Override
	public Location<Vector2> newLocation() {
		return new Box2dLocation();
	}
}
