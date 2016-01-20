package edu.mbhs.cs.bubbles;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Eyob on 1/20/2016.
 */
public final class Util {

	public static float vectorToAngle (Vector2 vector) {
		return (float)Math.atan2(-vector.x, vector.y);
	}

	public static Vector2 angleToVector (Vector2 outVector, float angle) {
		outVector.x = -(float)Math.sin(angle);
		outVector.y = (float)Math.cos(angle);
		return outVector;
	}


}
