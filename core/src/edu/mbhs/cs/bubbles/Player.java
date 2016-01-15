package edu.mbhs.cs.bubbles;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.LinkedList;

/**
 * Class representing the player, a small circle in the sea of bubbles
 */
public class Player extends Actor implements GestureListener{

	public static final float RADIUS = 5f;

	private Color color;
	private float x, y;
	private Body body;
	public Body getBody(){
		return body;
	}
	private Fixture fixture;
	public static final float METERS_TO_PIXELS = Bubble.METERS_TO_PIXELS;
	private static final float BASE_SPEED = 900;
	private TextureRegion texture = new TextureRegion();
	private Vector2 androidForce = new Vector2(0,0);

	//private ShapeRenderer sr = new ShapeRenderer();

	/**
	 *
	 * Makes a new chartreuse player
	 */
	public Player(World w, float maxWidth, float maxHeight) {
		texture.setTexture(new Texture(Gdx.files.internal("mandleplayer.png")));
		texture.setRegion(texture.getTexture());
		//this.setOrigin(515, 340);
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
		def.density = 0.09f;
		def.restitution = 0.77f;
		def.friction = 0.8f;

		fixture = body.createFixture(def);
		LinkedList<String> holder = new LinkedList();
		holder.add("Player"); holder.add(body.getPosition().x+""); holder.add(body.getPosition().y+"");
		fixture.setUserData(holder);
		shape.dispose();

		GestureDetector gd = new GestureDetector(this);
        Gdx.input.setInputProcessor(gd);
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
		updateBounds();
	}

	public void move(){
	switch (Gdx.app.getType()){
			case Android:
				body.applyForceToCenter(androidForce, true);
				break;
			case Desktop:
				boolean up = Gdx.input.isKeyPressed(Input.Keys.UP);
				boolean down = Gdx.input.isKeyPressed(Input.Keys.DOWN);
				boolean left = Gdx.input.isKeyPressed(Input.Keys.LEFT);
				boolean right = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
				float speed = (up || down) && (left || right) ? (float) (BASE_SPEED / Math.sqrt(2)):
						BASE_SPEED;
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
	//this.setRotation(body.getLinearVelocity().angle());
	//this.rotateBy(body.getLinearVelocity().angle());
		x = body.getPosition().x * METERS_TO_PIXELS;
		y = body.getPosition().y * METERS_TO_PIXELS;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(texture, (body.getPosition().x - RADIUS) * METERS_TO_PIXELS, 
				(body.getPosition().y - RADIUS) * METERS_TO_PIXELS, 
				518f/texture.getRegionWidth()*RADIUS * 1 * METERS_TO_PIXELS,
				(686f-340f)/texture.getRegionHeight()*RADIUS * 1 * METERS_TO_PIXELS
				, RADIUS * 2 * METERS_TO_PIXELS
				, RADIUS * 2 * METERS_TO_PIXELS, 1f, texture.getRegionHeight() / texture.getRegionWidth(), 
				body.getLinearVelocity().angle()-90, true);
		//batch.draw(texture,(Gdx.graphics.getWidth() - texture.getWidth()) / 2.0f,(Gdx.graphics.getHeight() - texture.getHeight()) / 2.0f,texture.getWidth()/2.0f,texture.getHeight()/2.0f, texture.getWidth(), texture.getHeight(), 1f, 1f,body.getLinearVelocity().angle(), false);	
		
		/*batch.draw(texture, (body.getPosition().x - RADIUS) * METERS_TO_PIXELS, 
				(body.getPosition().y - RADIUS) * METERS_TO_PIXELS, RADIUS * 2 * METERS_TO_PIXELS, 
				RADIUS * 2*texture.getRegionHeight() / texture.getRegionWidth() * METERS_TO_PIXELS,
				body.getLinearVelocity().angle(), false);
		System.out.println("Player "+body.getPosition().x*METERS_TO_PIXELS+" "+body.getPosition().y*METERS_TO_PIXELS+" "+this.getRotation());*/
		/*515f/texture.getRegionWidth()*RADIUS * 2 * METERS_TO_PIXELS + (body.getPosition().x - RADIUS) * METERS_TO_PIXELS ,
				(686-340)/texture.getRegionHeight()*RADIUS * 2 * METERS_TO_PIXELS + (body.getPosition().x - RADIUS) * METERS_TO_PIXELS*/
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		// TODO Auto-generated method stub
		androidForce.x = 0;
		androidForce.y = 0;
		return true;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		androidForce.x = 0;
		androidForce.y = 0;
		return true;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		double mag = (float) Math.sqrt(velocityX * velocityX + velocityY * velocityY);
		double cos = velocityX / mag;
		double sin = velocityY / mag;
		androidForce.x = (float) cos * BASE_SPEED;
		androidForce.y = (float) -sin * BASE_SPEED;
		
		return true;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}
	private void updateBounds() {
		this.setBounds(x, y, RADIUS * 2 * METERS_TO_PIXELS, RADIUS * 2 * METERS_TO_PIXELS);
	}
	public Rectangle getBounds(){
		return new Rectangle(x, y, RADIUS * 2 * METERS_TO_PIXELS, RADIUS * 2 * METERS_TO_PIXELS);
	}
}
