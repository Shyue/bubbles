package edu.mbhs.cs.bubbles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Class representing the bubbles bouncing around the screen.
 */
public class Bubble extends Actor {
	private float x = 0;
	private float y = 0;
	private float xVelocity;
	private float yVelocity;
	private int radius = 64;	//pic size
	private float r, g, b;	//color floats
	private float r_vel, g_vel, b_vel;	//color velocities
	private static final float TRANSPARENCY = 0.3f;
	
	Texture texture = new Texture(Gdx.files.internal("bubble.png"));

	/**
	 * Places bubble at bottom left corner, gives it a radius of 64, gives it a random x and y velocity
	 * between 0 and 10, sets rgb values to random values between 0 and 1, and sets rgb "velocities" to 0.5
	 */
	public Bubble(){
		this.setBounds(x, y, radius * 2, radius * 2);
		//This is a TERRIBLE way of doing things
		r = (float)Math.pow(Math.random(), 1.0 / 5);
		g = (float)Math.pow(Math.random(), 1.0 / 5);
		b = (float)Math.pow(Math.random(), 1.0 / 5);
		xVelocity = (float)Math.random() * 10;
		yVelocity = (float)Math.random() * 10;
		r_vel = 0.5f;
		g_vel = 0.5f;
		b_vel = 0.5f;
	}
	
	@Override
	public void act(float delta){
		move(delta);
		changeMotionVelocities(delta);
		changeColor(delta);
		changeColorVelocities(delta);

		System.out.println(xVelocity);
	}

	/**
	 * Moves the bubble in the x and y directions by its x and y velocities multiplied by delta
	 * and reverses the direction the bubble is moving in if it hits a boundary
	 * @param delta amount of "time" since last movement
	 */
	private void move(float delta) {
		x += xVelocity * delta;
		y += yVelocity * delta;

		// if on edge, bounce
		if(x >= this.getStage().getWidth() - 140 || x <= 0){
			xVelocity *= -1;
		}
		if(y >= this.getStage().getHeight() - 170 || y <= 0){
			yVelocity *= -1;
		}
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

		// keeping ourselves between 0 and 1
		if(r < 0 || r > 1){
			r_vel *= -1;
		}
		if(g < 0 || g > 1){
			g_vel *= -1;
		}
		if(b < 0 || b > 1){
			b_vel *= -1;
		}
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
	}

	/**
	 * Changes the x and y velocities by a random number multiplied by delta
	 * @param delta the amount of "time" since last x-y velocity change
	 */
	private void changeMotionVelocities(float delta) {
		xVelocity += 200 * Math.pow(1 - Math.random(), 3) * delta;
		yVelocity += 200 * Math.pow(1 - Math.random(), 3) * delta;
	}

	@Override
	public void draw(Batch batch, float alpha){
		batch.setColor(new Color(r, g, b, TRANSPARENCY));
		batch.draw(texture, x, y, radius * 2, radius * 2);
	}
}
