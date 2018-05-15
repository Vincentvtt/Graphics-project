package ass2.spec;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;
import javax.swing.JFrame;
import com.jogamp.opengl.util.FPSAnimator;



/**
 * COMMENT: Comment Game 
 *
 * @author malcolmr
 */
public class Game extends JFrame implements GLEventListener, KeyListener{

    private Terrain myTerrain;
    private Avatar avatar; 
    private Camera ThirdPersonCam;
    private Camera FirstPersonCam;
    private Lighting lights;
    
    private boolean isFirstPerson;

    public Game(Terrain terrain) {
    	super("Assignment 2");
        myTerrain = terrain;
        avatar = new Avatar(1,0.5,2);
        isFirstPerson = false; 
    }
    
    /** 
     * Run the game.
     *
     */
    public void run() {
    	  GLProfile glp = GLProfile.getDefault();
          GLCapabilities caps = new GLCapabilities(glp);
          GLJPanel panel = new GLJPanel(caps);
          
          ThirdPersonCam = new Camera(5,114, 4/3, 0.1f,100);//dummy values
          FirstPersonCam = new Camera(new double[]{0,0,0},0,114, 4/3, 0.1f,100);
          
          panel.addGLEventListener(this);
          panel.addKeyListener(avatar);
          panel.addKeyListener(avatar);	

          // Add an animator to call 'display' at 60fps        
          FPSAnimator animator = new FPSAnimator(60);
          animator.add(panel);
          animator.start();

          getContentPane().add(panel);
          setSize(800, 600);        
          setVisible(true);
          setDefaultCloseOperation(EXIT_ON_CLOSE);        
    }
    
    /**
     * Load a level file and display it.
     * 
     * @param args - The first argument is a level file in JSON format
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        Terrain terrain = LevelIO.load(new File(args[0]));
        Game game = new Game(terrain);
        game.run();
    }

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
    	gl.glLoadIdentity();
    	
    	ThirdPersonCam.setLookAt(avatar.getPosition()[0], avatar.getPosition()[1], avatar.getPosition()[2]);
    	
    	if(isFirstPerson == true){
    		
    	}else{
    		ThirdPersonCam.lookThrough(drawable);
    	}
		myTerrain.draw(drawable);
		avatar.drawSelf(gl);
		lights.setSunLighting(gl, myTerrain.getSunlight(), 10);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		
		gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glEnable(GL2.GL_NORMALIZE);
        lights = new Lighting();
        avatar.load(new File("tileTexture.jpg"));
		myTerrain.load(new File("grassTexture.jpg"),new File("grassTexture.jpg"), new File("roadTexture.jpg"));

	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		
		GL2 gl = drawable.getGL().getGL2();
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        
        GLU glu = new GLU();
        
        gl.glMatrixMode(GL2.GL_MODELVIEW);
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
