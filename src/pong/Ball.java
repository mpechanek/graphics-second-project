package pong;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

/**
 * Tøída reprezentující míè
 * 
 * @author Milan
 *
 */
public class Ball {
	private float actualX = 0, actualY = 0;
	GLU glu;
	GLUquadric quadratic;

	public void draw(GL2 gl) {
		glu = new GLU();
		quadratic = glu.gluNewQuadric();
		glu.gluQuadricNormals(quadratic, GLU.GLU_SMOOTH);
		glu.gluQuadricTexture(quadratic, true);
		glu.gluSphere(quadratic, 0.08, 32, 32);
	}

	public float getActualX() {
		return actualX;
	}

	public void setActualX(float actualX) {
		this.actualX = actualX;
	}

	public float getActualY() {
		return actualY;
	}

	public void setActualY(float cy) {
		this.actualY = cy;
	}
}
