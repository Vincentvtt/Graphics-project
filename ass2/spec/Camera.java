package ass2.spec;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.math.Matrix4;


public class Camera{
	
	private double[] position;
	private double[] lookAt;
	
	//private final double SensitivityRotation;//very small value 
//	private final double SensitivityTranslation;//very small value
	private final double distance;
	
	private final float fov;
	private final float aspectRatio;
	private final float near;
	private final float far;
	
	
	public Camera(double Targetdistance,float fov, 
				  float aspectRatio, float near, float far){
		
		position = new double[3];
		position[0] = 0;
		position[1] = 4; // fiddle around with these!
		position[2] = 2;

		lookAt = new double[3];

		distance = Targetdistance;
		
		this.fov = fov;
		this.aspectRatio = aspectRatio;
		this.near = near;
		this.far  = far; 
		
	}
	public Camera(double[] position,double Targetdistance,float fov, 
			  float aspectRatio, float near, float far){
	
		this.position = position;
	
		lookAt = new double[3];
	
		distance = Targetdistance;
		
		this.fov = fov;
		this.aspectRatio = aspectRatio;
		this.near = near;
		this.far  = far; 
		
}
	
	
	private void rotateCamera(double destAngle){
		//System.out.println(MathUtil.getXZAngle(new double[] {1,0,1}, new double[]{0.5,0,1.5}));
		//System.out.println("Dest Angle " + Double.toString(destAngle));

		if(destAngle > Math.PI /2 && destAngle < Math.PI){
			destAngle = Math.PI - destAngle; 
			System.out.println("2nd QUAD");
			lookAt[0] = position[0] + (this.distance * Math.sin(destAngle));//x
			lookAt[2] = -(position[2] + (this.distance * Math.cos(destAngle)));//z
		}else if(destAngle > Math.PI && destAngle < Math.PI *1.5){
			destAngle = Math.PI*1.5 - destAngle;
			lookAt[0] = -(position[0] + (this.distance * Math.sin(destAngle)));//x
			lookAt[2] = -(position[2] + (this.distance * Math.cos(destAngle)));//z
		}else if(destAngle > Math.PI*1.5 && destAngle < Math.PI *2){
			destAngle = Math.PI*2 - destAngle;
			lookAt[0] = -(position[0] + (this.distance * Math.sin(destAngle)));//x
			lookAt[2] = (position[2] + (this.distance * Math.cos(destAngle)));//z
		}else{
			lookAt[0] = position[0] + (this.distance * Math.sin(destAngle));//x
			lookAt[2] = position[2] + (this.distance * Math.cos(destAngle));//z
		}
		System.out.println("look AT: X,Z 	" + Double.toString(lookAt[0]) + " " + Double.toString(lookAt[2]));
		System.out.println("position: X,Z " + Double.toString(position[0]) + " " + Double.toString(position[2]));

	}
	private void translateCamera(double dist,double angle){
		position[0] += dist * Math.sin(angle);
		position[2] += dist * Math.cos(angle);
		
		lookAt[0] +=dist * Math.sin(angle);
		lookAt[2] +=dist * Math.cos(angle);
	}
	public double[] getPosition(){
		return position;
	}
	public double[] getLookAt(){
		return lookAt;
	}
	public void setLookAt(double x, double y, double z){
		lookAt[0] = x;
		lookAt[1] = y;
		lookAt[2] = z; 
	}
	//call first in display method
	public void lookThrough(GLAutoDrawable drawable){
		updatePosition();
		GL2 gl = drawable.getGL().getGL2();
	    GLU glu = GLU.createGLU(gl);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		glu.gluLookAt(this.getPosition()[0], this.getPosition()[1], 
					  this.getPosition()[2], this.getLookAt()[0], 
					  this.getLookAt()[1], this.getLookAt()[2], 
					  0, 1, 0);
		gl.glPushMatrix();
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(fov, aspectRatio, near, far);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glPopMatrix();
	}
	//maintain distance apart from lookat from XZ.
	private void updatePosition(){
		double lookDist = MathUtil.getXZDistance(position, lookAt);
		double updateDist = lookDist - distance; 
		double angle = MathUtil.getXZAngle(position, lookAt);//assume first quadrant

		//System.out.println("Angle from Camera: " +Double.toString(angle * (180/Math.PI)));

		if(angle < Math.PI/2){
			position[0] += updateDist * Math.sin(angle);
			position[2] += updateDist * Math.cos(angle);
			
		}else if(angle>= Math.PI/2 && angle < Math.PI){
			angle = angle - Math.PI/2;
			position[0] += updateDist * Math.sin(angle);
			position[2] -= updateDist * Math.cos(angle);
			
		}else if(angle >= Math.PI && angle< Math.PI *1.5){
			angle = angle - Math.PI;
			position[0] -= updateDist * Math.sin(angle);
			position[2] -= updateDist * Math.cos(angle);
		}else if(angle >= Math.PI *1.5 && angle < 2* Math.PI){
			angle = 2* Math.PI - angle;
			position[0] -= updateDist * Math.sin(angle);
			position[2] += updateDist * Math.cos(angle);
		}
		//System.out.println("Lookat X: " +Double.toString(lookAt[0]) + 
		//		   " Lookat Y: " + Double.toString(lookAt[2]));
		//System.out.println("angle to Rotate: " +Double.toString(angle * (180/Math.PI)));
	

	}

	


}
