package pong;

import com.jogamp.opengl.GL2;

/**
 * Tøída reprezentující høáèe 2
 * 
 * @author Milan
 * 
 */
public class PlayerTwo {
	private boolean movingUp = false;
	private boolean movingDown = false;
	private float posY = 0;
	private float speed = 0.02f;

	public void draw(GL2 gl) {

		if (movingUp && posY <= 0.85f) {
			posY += speed;
		} else if (movingDown && posY >= -0.85f) {
			posY -= speed;
		}

		gl.glBegin(GL2.GL_QUADS);
		gl.glTexCoord3f(0f, 0f, 0f);
		gl.glVertex3f(0.95f, -0.15f + posY, 0.1f);
		gl.glTexCoord3f(0f, 1f, 0f);
		gl.glVertex3f(0.9f, -0.15f + posY, 0.1f);
		gl.glTexCoord3f(1f, 1f, 0f);
		gl.glVertex3f(0.9f, 0.15f + posY, 0.1f);
		gl.glTexCoord3f(1f, 0f, 1f);
		gl.glVertex3f(0.95f, 0.15f + posY, 0.1f);

		gl.glTexCoord3f(0f, 0f, 0f);
		gl.glVertex3f(0.9f, 0.15f + posY, -0.1f);
		gl.glTexCoord3f(0f, 1f, 0f);
		gl.glVertex3f(0.95f, 0.15f + posY, -0.1f);
		gl.glTexCoord3f(1f, 1f, 0f);
		gl.glVertex3f(0.95f, -0.15f + posY, -0.1f);
		gl.glTexCoord3f(1f, 0f, 1f);
		gl.glVertex3f(0.9f, -0.15f + posY, -0.1f);

		gl.glTexCoord3f(0f, 0f, 0f);
		gl.glVertex3f(0.9f, -0.15f + posY, 0.1f);
		gl.glTexCoord3f(0f, 1f, 0f);
		gl.glVertex3f(0.9f, -0.15f + posY, -0.1f);
		gl.glTexCoord3f(1f, 1f, 0f);
		gl.glVertex3f(0.9f, 0.15f + posY, -0.1f);
		gl.glTexCoord3f(1f, 0f, 0f);
		gl.glVertex3f(0.9f, 0.15f + posY, 0.1f);

		gl.glTexCoord3f(0f, 0f, 0f);
		gl.glVertex3f(0.9f, 0.15f + posY, -0.1f);
		gl.glTexCoord3f(0f, 1f, 0f);
		gl.glVertex3f(0.9f, 0.15f + posY, 0.1f);
		gl.glTexCoord3f(1f, 1f, 0f);
		gl.glVertex3f(0.95f, 0.15f + posY, 0.1f);
		gl.glTexCoord3f(1f, 0f, 0f);
		gl.glVertex3f(0.95f, 0.15f + posY, -0.1f);

		gl.glTexCoord3f(0f, 0f, 0f);
		gl.glVertex3f(0.95f, 0.15f + posY, 0.1f);
		gl.glTexCoord3f(0f, 1f, 0f);
		gl.glVertex3f(0.95f, 0.15f + posY, -0.1f);
		gl.glTexCoord3f(1f, 1f, 0f);
		gl.glVertex3f(0.95f, -0.15f + posY, -0.1f);
		gl.glTexCoord3f(1f, 0f, 0f);
		gl.glVertex3f(0.95f, -0.15f + posY, 0.1f);

		gl.glTexCoord3f(0f, 0f, 0f);
		gl.glVertex3f(0.95f, -0.15f + posY, -0.1f);
		gl.glTexCoord3f(0f, 1f, 0f);
		gl.glVertex3f(0.95f, -0.15f + posY, 0.1f);
		gl.glTexCoord3f(1f, 1f, 0f);
		gl.glVertex3f(0.9f, -0.15f + posY, 0.1f);
		gl.glTexCoord3f(1f, 0f, 0f);
		gl.glVertex3f(0.9f, -0.15f + posY, -0.1f);

		gl.glEnd();
	}

	public float getPosY() {
		return posY;
	}

	public void setPosY(float y2) {
		this.posY = y2;
	}

	public boolean isMovingUp() {
		return movingUp;
	}

	public void setMovingUp(boolean movingUp) {
		this.movingUp = movingUp;
	}

	public boolean isMovingDown() {
		return movingDown;
	}

	public void setMovingDown(boolean movingDown) {
		this.movingDown = movingDown;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

}
