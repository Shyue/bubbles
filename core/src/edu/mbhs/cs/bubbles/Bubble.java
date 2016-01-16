package edu.mbhs.cs.bubbles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Arrays;

/**
 * Class representing the bubbles bouncing around the screen.
 */
public class Bubble extends Actor {

	public static final float METERS_TO_PIXELS = 3f * 1.5f;
	public static final float RADIUS = 8f / 1.5f;	//pic size

	private static final float MAXIMUM_VELOCITY = 2000;
	private static final float TRANSPARENCY = 0.5f;

	private Body body;
	private float r, g, b;	//color floats
	private float r_vel, g_vel, b_vel;	//color velocities
	private Fixture fixture;

	Texture texture = new Texture(Gdx.files.internal("ball.png"));

	/**
	 * Places bubble at bottom left corner, gives it a radius of 64, gives it a random x and y velocity
	 * between 0 and 10, sets rgb values to random values between 0 and 1, and sets rgb "velocities" to 0.5
	 */
	public Bubble(World w){
		//This is a TERRIBLE way of doing things
		r = (float)Math.pow(Math.random(), 1.0 / 5);
		g = (float)Math.pow(Math.random(), 1.0 / 5);
		b = (float)Math.pow(Math.random(), 1.0 / 5);
		r_vel = 0.5f;
		g_vel = 0.5f;
		b_vel = 0.5f;

		BodyDef bodyDef = new BodyDef();
	    bodyDef.type = BodyDef.BodyType.DynamicBody;
	    bodyDef.position.set(10 / METERS_TO_PIXELS, 10 / METERS_TO_PIXELS);
	    body = w.createBody(bodyDef);

	    CircleShape shape = new CircleShape();
	    shape.setRadius(RADIUS);
	    FixtureDef def = new FixtureDef();
	    def.shape = shape;
	    def.density = 0.1f;
		def.restitution = 0.5f;
	    fixture = body.createFixture(def);
		fixture.setUserData(Arrays.asList("Bubble", String.valueOf(body.getPosition().x), String.valueOf(body.getPosition().y)));
		shape.dispose();
	}

	@Override
	public void act(float delta){
		move();
		changeColor(delta);
		changeColorVelocities(delta);
	}

	public Body getBody() {
		return body;
	}

	/**
	 * Do physics
	 *
	 */
	private void move() {
		body.applyForceToCenter(
				new Vector2(
						(float)(1 - 2 * Math.random()) * (MAXIMUM_VELOCITY - body.getLinearVelocity().x),
						(float)(1 - 2 * Math.random()) * (MAXIMUM_VELOCITY - body.getLinearVelocity().y)
				),
				true
		);
		fixture.setUserData(Arrays.asList("Bubble", String.valueOf(body.getPosition().x), String.valueOf(body.getPosition().y)));
	}

	/**
	 * Changes the color of the bubble by the color velocities multiplied by delta
	 * and reverses the "direction" of the color change if it hits the boundaries of 0 or 255
	 * @param delta amount of "time" since last color change
	 */
	private void changeColor(float delta) {
		r += r_vel * delta;
		g += g_vel * delta;
		b += b_vel * delta;
	}

	/**
	 * Changes the color velocities by a random number multiplied by delta
	 * @param delta the amount of "time" since last color velocity change
	 */
	private void changeColorVelocities(float delta) {
		r_vel += Math.pow(1 - Math.random(), 3) * delta;
		g_vel += Math.pow(1 - Math.random(), 3) * delta;
		b_vel += Math.pow(1 - Math.random(), 3) * delta;

		if(r + g + b < 1){
			r_vel += 0.5f;
			g_vel += 0.5f;
			b_vel += 0.5f;
		}

		if(r < 0)	r_vel = Math.abs(r_vel);
		if(r > 1)	r_vel = -Math.abs(r_vel);
		if(g < 0)	g_vel = Math.abs(g_vel);
		if(g > 1)	g_vel = -Math.abs(g_vel);
		if(b < 0)	b_vel = Math.abs(b_vel);
		if(b > 1)	b_vel = -Math.abs(b_vel);

		if(r_vel > 3.4f)	r_vel -= 0.5f;
		if(g_vel > 3.4f)	g_vel -= 0.5f;
		if(b_vel > 3.4f)	b_vel -= 0.5f;

	}

	@Override
	public void draw(Batch batch, float alpha){
		batch.setColor(new Color(r, g, b, TRANSPARENCY));
		batch.draw(texture,
				(body.getPosition().x - RADIUS) * METERS_TO_PIXELS,
				(body.getPosition().y - RADIUS) * METERS_TO_PIXELS,
				RADIUS * 2 * METERS_TO_PIXELS,
				RADIUS * 2 * METERS_TO_PIXELS
		);
	}

}
