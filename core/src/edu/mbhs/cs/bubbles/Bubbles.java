package edu.mbhs.cs.bubbles;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Main Application: Bubbles Game
 */
public class Bubbles extends ApplicationAdapter {
	private static int BUBBLE_NUMBER = 5;
	private Bubble[] b = new Bubble[BUBBLE_NUMBER];
	private Stage stage;
	private World world;
	private OrthographicCamera cam;

	@Override
	public void create () {
		stage = new Stage();
		world = new World(new Vector2(0,0), false);
		Gdx.app.log("AssetPath", Gdx.files.internal("assets/bubble.jpg").file().getAbsolutePath());
		for(int i = 0; i < BUBBLE_NUMBER; i++){
			b[i] = new Bubble(world, stage.getWidth(), stage.getHeight());

			stage.addActor(b[i]);
		}
		//b[0] = new Bubble(world);
		//stage.addActor(b[0]);
		stage.addActor(new Player(world, stage.getWidth(), stage.getHeight()));


		//this edge detection doesn't work somebody fix it
		LinkedList<String> holder;
		float with = Gdx.graphics.getWidth()/Bubble.METERS_TO_PIXELS;
        float hite = Gdx.graphics.getHeight()/Bubble.METERS_TO_PIXELS;
	    BodyDef bottom = new BodyDef();
	    bottom.type = BodyDef.BodyType.StaticBody;
	    bottom.position.set(0,0);
	    FixtureDef bot = new FixtureDef();
	    EdgeShape but = new EdgeShape();
	    but.set(new Vector2(-with, 0), new Vector2(with, 0));
	    bot.shape = but;
	    Body botEdge = world.createBody(bottom);
	    Fixture botFixture = botEdge.createFixture(bot);
		holder = new LinkedList<String>(); holder.add("bot");
		botFixture.setUserData(holder);

	    BodyDef top = new BodyDef();
	    top.type = BodyDef.BodyType.StaticBody;
	    top.position.set(0, stage.getHeight() / Bubble.METERS_TO_PIXELS);
	    FixtureDef tip = new FixtureDef();
	    EdgeShape tup = new EdgeShape();
	    tup.set(new Vector2(-with, 0), new Vector2(with, 0));
	    tip.shape = tup;
	    Body topEdge = world.createBody(top);
	    Fixture topFixture = topEdge.createFixture(tip);
		holder = new LinkedList<String>(); holder.add("top");
		topFixture.setUserData(holder);
	    
	    BodyDef left = new BodyDef();
	    left.type = BodyDef.BodyType.StaticBody;
	    left.position.set(0, 0);
	    FixtureDef lift = new FixtureDef();
	    EdgeShape luft = new EdgeShape();
	    luft.set(new Vector2(0, -hite), new Vector2(0, hite));
	    lift.shape = luft;
	    Body leftEdge = world.createBody(left);
	    Fixture leftFixture = leftEdge.createFixture(lift);
		holder = new LinkedList<String>(); holder.add("left");
		leftFixture.setUserData(holder);
	    
	    BodyDef right = new BodyDef();
	    right.type = BodyDef.BodyType.StaticBody;
	    right.position.set(stage.getWidth() / Bubble.METERS_TO_PIXELS,0);
	    FixtureDef rot = new FixtureDef();
	    EdgeShape rut = new EdgeShape();
	    rut.set(new Vector2(0, -hite), new Vector2(0, hite));
	    rot.shape = rut;
	    Body rightEdge = world.createBody(right);
	    Fixture rightFixture = rightEdge.createFixture(rot);
		holder = new LinkedList<String>(); holder.add("right");
	  	rightFixture.setUserData(holder);
	    
	    but.dispose();
	    rut.dispose();
	    luft.dispose();
	    tup.dispose();

		//setting listener for bubble/player colliding
		world.setContactListener(new ContactListener() {

			boolean Colliding;
			@Override
			public void beginContact(Contact contact) {

				if (((LinkedList)contact.getFixtureA().getUserData()).get(0)=="Player" || ((LinkedList)contact.getFixtureB().getUserData()).get(0)=="Player"){
					System.out.println(contact.getFixtureA().getUserData()+" "+contact.getFixtureB().getUserData());
				}

			}

			@Override
			public void endContact(Contact contact) {
			}

			@Override
			public void postSolve(Contact arg0, ContactImpulse arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void preSolve(Contact arg0, Manifold arg1) {
				// TODO Auto-generated method stub
			}
		});
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		world.step(1f/60f, 6, 2);
		stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
	}
}
