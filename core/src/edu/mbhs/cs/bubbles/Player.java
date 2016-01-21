package edu.mbhs.cs.bubbles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Arrays;

/**
 * Class representing the player, a small circle in the sea of bubbles
 */
public class Player extends Actor implements GestureListener{

	public static final float RADIUS = 5f;
	public static final float METERS_TO_PIXELS = Bubble.METERS_TO_PIXELS;

	private static final float BASE_SPEED = 900;

	public int score = 0;

	private float x, y;
	private Body body;
	private Fixture fixture;
	private TextureRegion texture = new TextureRegion();
	private Vector2 androidForce = new Vector2(0,0);

	/**
	 * Makes a new player
	 */
	public Player(World w) {
		texture.setTexture(new Texture(Gdx.files.internal("turtle.png")));
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
		fixture.setUserData(Arrays.asList("Player", String.valueOf(body.getPosition().x), String.valueOf(body.getPosition().y)));
		shape.dispose();

		GestureDetector gd = new GestureDetector(this);
		Gdx.input.setInputProcessor(gd);
	}

	@Override
	public void act(float delta) {
		fixture.setUserData(Arrays.asList("Player", String.valueOf(body.getPosition().x), String.valueOf(body.getPosition().y)));
		move();
		updateBounds();
	}

	public Body getBody() {
		return body;
	}

	public void move(){
		switch (Gdx.app.getType()) {
			case Android:
				body.applyForceToCenter(androidForce, true);
				break;
			case Desktop:
				boolean up = Gdx.input.isKeyPressed(Input.Keys.UP);
				boolean down = Gdx.input.isKeyPressed(Input.Keys.DOWN);
				boolean left = Gdx.input.isKeyPressed(Input.Keys.LEFT);
				boolean right = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
				float speed = (up || down) && (left || right) ? (float) (BASE_SPEED / Math.sqrt(2)) : BASE_SPEED;
				if (down){
					body.applyForceToCenter(new Vector2(0, -speed), true);
				}
				if (up){
					body.applyForceToCenter(new Vector2(0, speed), true);
				}
				if (right){
					body.applyForceToCenter(new Vector2(speed, 0), true);
				}
				if (left){
					body.applyForceToCenter(new Vector2(-speed, 0), true);
				}
				break;
		}
		x = body.getPosition().x * METERS_TO_PIXELS;
		y = body.getPosition().y * METERS_TO_PIXELS;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(
				texture,
				(body.getPosition().x - RADIUS) * METERS_TO_PIXELS,
				(body.getPosition().y - RADIUS) * METERS_TO_PIXELS,
				RADIUS * METERS_TO_PIXELS,
				RADIUS * METERS_TO_PIXELS,
				RADIUS * 2 * METERS_TO_PIXELS,
				RADIUS * 2 * METERS_TO_PIXELS,
				1f,
				texture.getRegionHeight() / texture.getRegionWidth(),
				body.getLinearVelocity().angle(),
				true
		);
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		androidForce.x = 0;
		androidForce.y = 0;
		return true;
	}

	@Override
	public boolean longPress(float x, float y) {
		androidForce.x = 0;
		androidForce.y = 0;
		return true;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		double mag = (float) Math.sqrt(velocityX * velocityX + velocityY * velocityY);
		double cos = velocityX / mag;
		double sin = velocityY / mag;
		androidForce.x = (float) cos * BASE_SPEED;
		androidForce.y = (float) -sin * BASE_SPEED;
		return true;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		return false;
	}

	private void updateBounds() {
		this.setBounds(x, y, RADIUS * 2 * METERS_TO_PIXELS, RADIUS * 2 * METERS_TO_PIXELS);
	}

	public Rectangle getBounds(){
		return new Rectangle(x, y, RADIUS * 2 * METERS_TO_PIXELS, RADIUS * 2 * METERS_TO_PIXELS);
	}
}
