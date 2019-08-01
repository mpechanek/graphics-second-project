package pong;

import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import utils.OglUtils;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * KPGR2 Projekt 2 - PONG
 * 
 * @author Milan
 *
 */
public class Renderer implements GLEventListener, KeyListener {
	int width, height, x, y = 0;
	private float shiftX, shiftY;
	private final int maximumPlayerScore = 5;
	private int playerOneScore = 0;
	private int playerTwoScore = 0;
	private TextRenderer playerOneScoreText;
	private TextRenderer playerTwoScoreText;
	private boolean gameover = false;
	private PlayerTwo playerTwo = new PlayerTwo();
	private PlayerOne playerOne = new PlayerOne();
	private Ball ball = new Ball();
	GLU glu;
	GLUT glut;
	private int ballTexture;
	private int paddleTexture;
	private int grassTexture;
	GLUquadric quadratic;
	int rotAngle = 0;
	boolean rotateR = false;
	boolean rotateL = false;

	public enum GamePhase {
		MENU, START, PAUSE, GAME
	}

	private GamePhase gamePhase = GamePhase.MENU;

	public void init(GLAutoDrawable glDrawable) {
		final GL2 gl = glDrawable.getGL().getGL2();
		glu = new GLU();
		glut = new GLUT();

		OglUtils.printOGLparameters(gl);

		gl.glEnable(GL2.GL_DEPTH_TEST);

		float[] mat_amb = new float[] { 0.1f, 0.1f, 0.1f, 1 };
		float[] mat_dif = new float[] { 1, 1, 1, 1 };
		float[] mat_spec = new float[] { 0.3f, 0.3f, 0.3f, 1 };

		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, mat_amb, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, mat_dif, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, mat_spec, 0);

		float[] light_amb = new float[] { 1, 1, 1, 1 };
		float[] light_dif = new float[] { 1, 1, 1, 1 };
		float[] light_spec = new float[] { 0.3f, 0.3f, 0.3f, 1 };

		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, light_amb, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, light_dif, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, light_spec, 0);

		try {
			File in = new File("res/ballTexture.jpg");
			Texture t = TextureIO.newTexture(in, true);
			ballTexture = t.getTextureObject(gl);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("File not found!");

		}
		try {
			File in = new File("res/woodTexture.jpg");
			Texture t = TextureIO.newTexture(in, true);
			paddleTexture = t.getTextureObject(gl);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("File not found!");

		}
		try {
			File in = new File("res/grassTexture.jpg");
			Texture t = TextureIO.newTexture(in, true);
			grassTexture = t.getTextureObject(gl);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("File not found!");

		}
		playerOneScoreText = new TextRenderer(new Font("SansSerif", Font.BOLD, 72));
		playerTwoScoreText = new TextRenderer(new Font("SansSerif", Font.BOLD, 72));
		startDirection();
	}

	public void display(GLAutoDrawable glDrawable) {

		final GL2 gl = glDrawable.getGL().getGL2();
		gl.glEnable(GL2.GL_DEPTH_TEST);

		gl.glClearColor(0.2f, 1f, 0.2f, 1f);
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();

		glu.gluPerspective(50, width / (float) height, 0.5f, 20.0f);
		glu.gluLookAt(0, -2.5, 0.9, 0, 0, 0, 0, 0, 1);

		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);

		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();

		float[] light_position;
		light_position = new float[] { -5, 0, 3, 0.0f };
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, light_position, 0);

		playerOneScoreText.beginRendering(1024, 768);
		playerOneScoreText.setColor(1.0f, 0f, 0f, 1.0f);
		playerOneScoreText.draw(Integer.toString(playerOneScore), 392, 708);
		playerOneScoreText.endRendering();

		playerTwoScoreText.beginRendering(1024, 768);
		playerTwoScoreText.setColor(0f, 0f, 1.0f, 1.0f);
		playerTwoScoreText.draw(Integer.toString(playerTwoScore), 582, 708);
		playerTwoScoreText.endRendering();

		playerOne(glDrawable);
		playerTwo(glDrawable);
		dividingLine(glDrawable);
		field(glDrawable);

		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glTranslatef(ball.getActualX(), ball.getActualY(), -0.04f);
		gl.glRotated(rotAngle, 0, 1, 0);

		ball(glDrawable);
		gl.glPopMatrix();

		gameover = scoreCheck();

		if (gameover) {
			gameOver();
			return;
		}

		if (gamePhase == GamePhase.GAME) {

			ball.setActualX(ball.getActualX() + shiftX);
			ball.setActualY(ball.getActualY() + shiftY);
			if (rotateL) {
				rotAngle = (--rotAngle) % 360;
			}
			if (rotateR) {
				rotAngle = (++rotAngle) % 360;
			}
		}

		drawMessage();
		OglUtils.drawStr2D(glDrawable, width / 20, 5,
				"Ovl·d·nÌ: LEV› hr·Ë: W, S, PRAV› hr·Ë: UP, DOWN, PAUZA: P, RESET: R, MEZERNÕK: start ESC: exit.");
	}

	public void playerOne(GLAutoDrawable glDrawable) {
		final GL2 gl = glDrawable.getGL().getGL2();
		gl.glColor3f(1f, 1f, 1f);
		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glBindTexture(GL2.GL_TEXTURE_2D, paddleTexture);
		playerOne.draw(gl);
		gl.glDisable(GL2.GL_TEXTURE_2D);

	}

	public void playerTwo(GLAutoDrawable glDrawable) {
		final GL2 gl = glDrawable.getGL().getGL2();
		gl.glColor3f(1f, 1f, 1f);
		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glBindTexture(GL2.GL_TEXTURE_2D, paddleTexture);
		playerTwo.draw(gl);
		gl.glDisable(GL2.GL_TEXTURE_2D);

	}

	public void field(GLAutoDrawable glDrawable) {
		final GL2 gl = glDrawable.getGL().getGL2();
		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glBindTexture(GL2.GL_TEXTURE_2D, grassTexture);
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3f(1f, 1f, 1f);
		gl.glTexCoord2f(0f, 0f);
		gl.glVertex3f(1f, -1f, -0.12f);
		gl.glTexCoord2f(0f, 1f);
		gl.glVertex3f(1f, 1f, -0.12f);
		gl.glTexCoord2f(1f, 1f);
		gl.glVertex3f(-0.95f, 1f, -0.12f);
		gl.glTexCoord2f(1f, 0f);
		gl.glVertex3f(-0.95f, -1f, -0.12f);
		gl.glEnd();
		gl.glDisable(GL2.GL_TEXTURE_2D);

	}

	private void dividingLine(GLAutoDrawable glDrawable) {
		final GL2 gl = glDrawable.getGL().getGL2();
		gl.glLineWidth(25);
		gl.glColor3f(0.8f, 0.8f, 0.8f);

		float placement = 0.95f;

		for (int i = 0; i < 20; i++) {
			gl.glBegin(GL2.GL_QUADS);
			gl.glVertex3f(0.01f, 0.025f + placement, -0.119f);
			gl.glVertex3f(-0.01f, 0.025f + placement, -0.119f);
			gl.glVertex3f(-0.01f, -0.025f + placement, -0.119f);
			gl.glVertex3f(0.01f, -0.025f + placement, -0.119f);
			gl.glEnd();

			placement -= 0.1f;

		}
	}

	public void ball(GLAutoDrawable glDrawable) {
		final GL2 gl = glDrawable.getGL().getGL2();
		checkBallBounce();

		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glBindTexture(GL2.GL_TEXTURE_2D, ballTexture);
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);
		gl.glShadeModel(GL2.GL_SMOOTH);
		ball.draw(gl);
		gl.glDisable(GL2.GL_TEXTURE_2D);
		gl.glDisable(GL2.GL_LIGHTING);
		gl.glDisable(GL2.GL_LIGHT0);

	}

	private void checkBallBounce() {

		if (ball.getActualX() > 0.87f) {
			reset();
			playerOneScore++;
		}

		if (ball.getActualX() < -0.87f) {
			reset();
			playerTwoScore++;
		}

		if (ball.getActualX() <= -0.83f && ((ball.getActualY() >= -0.15f + playerOne.getPosY())
				&& (ball.getActualY() <= 0.15f + playerOne.getPosY()))) {
			shiftX *= -1.05f;
			rotateR = true;
			rotateL = false;

		}

		if (ball.getActualX() >= 0.83f && ((ball.getActualY() >= -0.15f + playerTwo.getPosY())
				&& (ball.getActualY() <= 0.15f + playerTwo.getPosY()))) {
			shiftX *= -1.05f;
			rotateL = true;
			rotateR = false;
		}

		if (ball.getActualY() >= 0.92f || ball.getActualY() <= -0.92f) {
			shiftY *= -1;
		}
	}

	private void reset() {
		ball.setActualX(0);
		ball.setActualY(0);
		playerOne.setPosY(0);
		playerTwo.setPosY(0);
		startDirection();
	}

	private void setGamePhase(GamePhase phase) {
		gamePhase = phase;
		if (phase == GamePhase.START) {
			playerOneScore = playerTwoScore = 0;
			reset();
		}
		if (gamePhase == GamePhase.PAUSE) {

			ball.setActualX(ball.getActualX());
			ball.setActualY(ball.getActualY());
			playerOne.setPosY(playerOne.getPosY());
			playerTwo.setPosY(playerTwo.getPosY());

		}
	}

	private void drawMessage() {
		TextRenderer prompt = new TextRenderer(new Font("Monospaced", Font.BOLD, 72));
		if (gamePhase == GamePhase.PAUSE) {
			String line1 = "Pauza!";
			String line2 = String.format("MezernÌk pro pokraËov·nÌ");
			prompt.beginRendering(1024, 768);
			prompt.setColor(0f, 0f, 0f, 1f);
			prompt.draw(line1, 380, 500);
			prompt.draw(line2, 3, 80);
			prompt.endRendering();
		} else if (gamePhase == GamePhase.MENU || gamePhase == GamePhase.START) {
			String line1 = "P O N G";
			String line2 = "MezernÌk pro start";
			String line3 = "VÌtÏznÈ skÛre 5!";
			prompt.beginRendering(1024, 768);
			prompt.setColor(0f, 0f, 0f, 1f);
			prompt.draw(line1, 360, 500);
			prompt.draw(line2, 115, 100);
			prompt.draw(line3, 150, 40);
			prompt.endRendering();
		}
	}

	private void gameOver() {
		TextRenderer prompt = new TextRenderer(new Font("Monospaced", Font.BOLD, 72));
		String winner = "";

		if (playerOneScore == maximumPlayerScore) {
			winner = "LEV›";
			String line1 = "Konec hry!";
			String line2 = String.format(" VÌtÏzÌ " + winner + " hr·Ë");
			String line3 = "Stiskem R nov· hra";

			prompt.setColor(1f, 0f, 0f, 1f);
			prompt.beginRendering(1024, 768);

			prompt.draw(line1, 310, 500);
			prompt.draw(line2, 130, 90);
			prompt.draw(line3, 130, 30);
			prompt.endRendering();
		} else {
			winner = "PRAV›";
			String line1 = "Konec hry!";
			String line2 = String.format(" VÌtÏzÌ " + winner + " hr·Ë");
			String line3 = "Stiskem R nov· hra";

			prompt.setColor(0f, 0f, 0.9f, 1f);
			prompt.beginRendering(1024, 768);
			prompt.draw(line1, 310, 500);
			prompt.draw(line2, 130, 90);
			prompt.draw(line3, 130, 30);
			prompt.endRendering();
		}

	}

	private boolean scoreCheck() {
		if (playerOneScore == maximumPlayerScore || playerTwoScore == maximumPlayerScore) {
			return true;
		} else {
			return false;
		}
	}

	private void startDirection() {
		Random startDirection = new Random();
		int start = startDirection.nextInt(4);

		if (start == 0) {
			shiftX = 0.01f;
			shiftY = 0.01f;
			rotateR = true;
			rotateL = false;
		}

		if (start == 1) {
			shiftX = -0.01f;
			shiftY = 0.01f;
			rotateR = false;
			rotateL = true;
		}

		if (start == 2) {
			shiftX = -0.01f;
			shiftY = -0.01f;
			rotateR = false;
			rotateL = true;
		}

		if (start == 3) {
			shiftX = 0.01f;
			shiftY = -0.01f;
			rotateR = true;
			rotateL = false;
		}

	}

	public void reshape(GLAutoDrawable glDrawable, int x, int y, int width, int height) {
		GL2 gl = glDrawable.getGL().getGL2();
		this.width = width;
		this.height = height;
		gl.glViewport(0, 0, this.width, this.height);
	}

	public static void exit() {
		System.exit(0);
	}

	public void dispose(GLAutoDrawable glDrawable) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_W) {
			if (gamePhase != GamePhase.PAUSE) {
				playerOne.setMovingUp(true);
			}
		}

		if (key == KeyEvent.VK_S) {
			if (gamePhase != GamePhase.PAUSE) {
				playerOne.setMovingDown(true);
			}
		}

		if (key == KeyEvent.VK_UP) {
			if (gamePhase != GamePhase.PAUSE) {
				playerTwo.setMovingUp(true);
			}
		}

		if (key == KeyEvent.VK_DOWN) {
			if (gamePhase != GamePhase.PAUSE) {
				playerTwo.setMovingDown(true);
			}
		}
		if (key == KeyEvent.VK_ESCAPE) {
			exit();
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_W) {
			playerOne.setMovingUp(false);
		}

		if (key == KeyEvent.VK_S) {
			playerOne.setMovingDown(false);
		}

		if (key == KeyEvent.VK_UP) {
			playerTwo.setMovingUp(false);
		}

		if (key == KeyEvent.VK_DOWN) {
			playerTwo.setMovingDown(false);
		}
		if (key == KeyEvent.VK_R) {
			setGamePhase(GamePhase.START);
		}
		if (key == KeyEvent.VK_SPACE) {
			setGamePhase(GamePhase.GAME);
		}
		if (key == KeyEvent.VK_P) {
			setGamePhase(GamePhase.PAUSE);
		}

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

}
