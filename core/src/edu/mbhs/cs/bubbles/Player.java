package edu.mbhs.cs.bubbles;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.LinkedList;

/**
 * Class representing the player, a small circle in the sea of bubbles
 */
public class Player extends Actor {

	public static final float RADIUS = 8f/3f;

	private Color color;
	private float x, y;
	private Body body;
	public Body getBody(){
		return body;
	}
	private Fixture fixture;
	public static final float METERS_TO_PIXELS = Bubble.METERS_TO_PIXELS;
	private static final float BASE_SPEED = 500;
	Texture texture = new Texture(Gdx.files.internal("player.png"));


	//private ShapeRenderer sr = new ShapeRenderer();

	/**
	 *
	 * Makes a new chartreuse player
	 */
	public Player(World w, float maxWidth, float maxHeight) {
		color = Color.CHARTREUSE;
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
		def.density = 0.5f;
		def.restitution = 0.75f;
		def.friction = 1f;

		fixture = body.createFixture(def);
		LinkedList<String> holder = new LinkedList();
		holder.add("Player"); holder.add(body.getPosition().x+""); holder.add(body.getPosition().y+"");
		fixture.setUserData(holder);
		shape.dispose();

	}

	@Override
	public void act(float delta) {
		//super.act(delta);
		//x = getStage().getWidth() / 2;
		//y = getStage().getHeight() / 2;

		LinkedList<String> holder = new LinkedList();
		holder.add("Player"); holder.add(body.getPosition().x+""); holder.add(body.getPosition().y+"");
		fixture.setUserData(holder);
		move();
	}

	public void move(){
	switch (Gdx.app.getType()){
			case Android:
				break;
			case Desktop:
				if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
					body.applyForceToCenter(new Vector2(0, -BASE_SPEED), true);
				}
				if (Gdx.input.isKeyPressed(Input.Keys.UP)){
					body.applyForceToCenter(new Vector2(0, BASE_SPEED), true);
				}
				if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
					body.applyForceToCenter(new Vector2(BASE_SPEED, 0), true);
				}
				if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
					body.applyForceToCenter(new Vector2(-BASE_SPEED, 0), true);
				}
				break;
		}
		//x = body.getPosition().x * METERS_TO_PIXELS;
		//y = body.getPosition().y * METERS_TO_PIXELS;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(texture, (body.getPosition().x - RADIUS) * METERS_TO_PIXELS, (body.getPosition().y - RADIUS) * METERS_TO_PIXELS, RADIUS * 2 * METERS_TO_PIXELS, RADIUS * 2 * METERS_TO_PIXELS);
		System.out.println("Player "+body.getPosition().x*METERS_TO_PIXELS+" "+body.getPosition().y*METERS_TO_PIXELS);

	}
}
