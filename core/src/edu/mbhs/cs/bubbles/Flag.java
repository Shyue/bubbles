package edu.mbhs.cs.bubbles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by guest1 on 1/11/2016.
 */
public class Flag extends Actor {
    private Texture texture = new Texture(Gdx.files.internal("Flag.png"));
	private Stage stage;
	private float x,y;
	
	public Flag(Stage stag){
		stage = stag;
		init();
    }
	
	@Override
	public void draw(Batch batch, float alpha){
		batch.draw(texture, x, y, 100, 100);
	}
	/**
	 * This is public on purpose, it might be called by a method elsewhere 
	 * consider privateness when the project is near done/ i finish this piece
	 */
	public void init(){
		x = (float) (Math.random() * stage.getWidth() * 3.5 + stage.getWidth() * 0.25);
		y = (float) (Math.random() * stage.getHeight() * 3.5 + stage.getHeight() * 0.25);
	}
	@Override
	public void act(float delta) {
		updateBounds();
	}
	private void updateBounds() {
		this.setBounds(x, y, 100, 100);
	}
	public Rectangle getBounds(){
		return new Rectangle(x, y, 100, 100);
	}

}
