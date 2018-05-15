package ass2.spec;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

public class Avatar implements KeyListener {
	
	private double[] position;
	private double rotAngle;
	private double[] rotationVector;
	private double size;
	private double speed;
	private double rotSpeed;
	private Texture texture;
	
	public Avatar(double size, double speed,double rotSpeed){
		position = new double[3];
		position[1] =  1.5;


		rotationVector = new double[]{0,1,0};//rotation along x axis
		rotAngle = 0;
		this.size = size;
		this.speed = speed; 
		this.rotSpeed = rotSpeed;
	}
	
	public void load(File textureFile){
		try{
			texture= TextureIO.newTexture(textureFile, true);
		}catch(Exception e){
    		e.printStackTrace();
    	}
	}
	public double[] getPosition(){
		return position;
	}
	public void drawSelf(GL2 gl){
		gl.glPushMatrix();
		gl.glMatrixMode(GL2.GL_MODELVIEW_MATRIX);
		gl.glTranslated(position[0], position[1], position[2]);
		gl.glRotated(rotAngle,rotationVector[0],rotationVector[1],rotationVector[2]);

        float matAmbAndDif[] = {1.0f, .85f, .5f, 1.0f};
        float matSpec[] = { .0f, .5f, 1.0f, 1.0f };
        float matShine[] = { 0.0f };
        float emm[] = {0.0f, 0.0f, 0.0f, 1.0f};
    	GLUT glut = new GLUT();
    	// Material properties
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDif,0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, matSpec,0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, matShine,0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, emm,0);

        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 80);	// phong
    	gl.glEnable(GL2.GL_TEXTURE_2D);
    	gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
    	//gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
    	gl.glMatrixMode(GL2.GL_MODELVIEW);   	
    	gl.glBindTexture(GL2.GL_TEXTURE_2D, texture.getTextureObject());
    	
    	glut.glutSolidTeapot(size); //draw the avatar- teapot. 

		gl.glBindTexture(GL2.GL_TEXTURE_2D, 0); //reset texture
	    gl.glPopMatrix();
	    
	   // lights.setSunLighting(gl, sunDirection, angle);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent keyPress) {
		switch(keyPress.getKeyCode()){
			 case KeyEvent.VK_UP:
				 moveInDirection(-1);				
				 //  System.out.println("x: " + Double.toString(position[0]) + "z: " + Double.toString(position[2]));
				  break;
				  
			 case KeyEvent.VK_DOWN:
				  moveInDirection(1);
				//  System.out.println("x: " + Double.toString(position[0]) + "z: " + Double.toString(position[2]));
				  break;
				  
			 case KeyEvent.VK_RIGHT:
				  rotAngle -= rotSpeed;
	
				  break;
				  
			 case KeyEvent.VK_LEFT:
				  rotAngle += rotSpeed;
				  break;
			}
	
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	} 
	private void moveInDirection(double multiplier){
		double facingAngle = rotAngle -90; //offset so we move in direction of spout!
		double radAngle = MathUtil.degreesToRad(facingAngle);
		position[0] += speed * Math.sin(radAngle) * multiplier;
		position[2] += speed * Math.cos(radAngle) * multiplier;

	}
	
	
}
