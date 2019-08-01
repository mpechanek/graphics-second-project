package pong;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

/**
 * Spouštìcí tøída
 * 
 * @author Milan
 *
 */
public class MainApp {
	private static final int FPS = 60;

	public static void main(String[] args) {

		Frame frame = new Frame("KPGR2Pong");
		frame.setTitle("KGR2 úloha 2");
		frame.setSize(1000, 700);

		GLProfile profile = GLProfile.getDefault();
		GLCapabilities capabilities = new GLCapabilities(profile);
		capabilities.setRedBits(8);
		capabilities.setBlueBits(8);
		capabilities.setGreenBits(8);
		capabilities.setAlphaBits(8);
		capabilities.setDepthBits(24);

		GLCanvas canvas = new GLCanvas(capabilities);
		Renderer ren = new Renderer();
		canvas.addGLEventListener(ren);
		frame.setResizable(true);
		canvas.addKeyListener(ren);
		canvas.setFocusable(true);
		canvas.setSize(1000, 700);
		frame.add(canvas);

		final FPSAnimator animator = new FPSAnimator(canvas, FPS, true);

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				new Thread() {
					@Override
					public void run() {
						if (animator.isStarted())
							animator.stop();
						System.exit(0);
					}
				}.start();
			}
		});

		frame.setTitle(ren.getClass().getName());
		frame.pack();
		frame.setVisible(true);
		animator.start();

	}

}
