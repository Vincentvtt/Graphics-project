package ass2.spec;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

/**
 * COMMENT: Comment Road 
 *
 * @author malcolmr
 */
public class Road {

    private List<Double> myPoints;
    private double myWidth;
    private Texture textures;
    
    /** 
     * Create a new road starting at the specified point
     */
    public Road(double width, double x0, double y0) {
        myWidth = width;
        myPoints = new ArrayList<Double>();
        myPoints.add(x0);
        myPoints.add(y0);
    }
    
    public void load(File road){
    	try{
             this.textures = TextureIO.newTexture(road, true);
             //TODO: add all textures;
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }

    /**
     * Create a new road with the specified spine 
     *
     * @param width
     * @param spine
     */
    public Road(double width, double[] spine) {
        myWidth = width;
        myPoints = new ArrayList<Double>();
        for (int i = 0; i < spine.length; i++) {
            myPoints.add(spine[i]);
        }
    }

    /**
     * The width of the road.
     * 
     * @return
     */
    public double width() {
        return myWidth;
    }

    /**
     * Add a new segment of road, beginning at the last point added and ending at (x3, y3).
     * (x1, y1) and (x2, y2) are interpolated as bezier control points.
     * 
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     */
    public void addSegment(double x1, double y1, double x2, double y2, double x3, double y3) {
        myPoints.add(x1);
        myPoints.add(y1);
        myPoints.add(x2);
        myPoints.add(y2);
        myPoints.add(x3);
        myPoints.add(y3);        
    }
    
    /**
     * Get the number of segments in the curve
     * 
     * @return
     */
    public int size() {
        return myPoints.size() / 6;
    }

    /**
     * Get the specified control point.
     * 
     * @param i
     * @return
     */
    public double[] controlPoint(int i) {
        double[] p = new double[2];
        p[0] = myPoints.get(i*2);
        p[1] = myPoints.get(i*2+1);
        return p;
    }
    
    /**
     * Get a point on the spine. The parameter t may vary from 0 to size().
     * Points on the kth segment take have parameters in the range (k, k+1).
     * 
     * @param t
     * @return
     */
    public double[] point(double t) {
        int i = (int)Math.floor(t);
        t = t - i;
        
        i *= 6;
        
        double x0 = myPoints.get(i++);
        double y0 = myPoints.get(i++);
        double x1 = myPoints.get(i++);
        double y1 = myPoints.get(i++);
        double x2 = myPoints.get(i++);
        double y2 = myPoints.get(i++);
        double x3 = myPoints.get(i++);
        double y3 = myPoints.get(i++);
        
        double[] p = new double[2];

        p[0] = b(0, t) * x0 + b(1, t) * x1 + b(2, t) * x2 + b(3, t) * x3;
        p[1] = b(0, t) * y0 + b(1, t) * y1 + b(2, t) * y2 + b(3, t) * y3;        
        
        return p;
    }
    
	/**
	 * Get a point on the spine. The parameter t may vary from 0 to size().
	 * Points on the kth segment take have parameters in the range (k, k+1).
	 * 
	 * @param t
	 * @return
	 */
	public double[] PointTangent(double t) {
	    int i = (int)Math.floor(t);
	    t = t - i;
	    
	    i *= 6;
	    
	    double x0 = myPoints.get(i++);
	    double y0 = myPoints.get(i++);
	    double x1 = myPoints.get(i++);
	    double y1 = myPoints.get(i++);
	    double x2 = myPoints.get(i++);
	    double y2 = myPoints.get(i++);
	    double x3 = myPoints.get(i++);
	    double y3 = myPoints.get(i++);
	    
	    double[] p = new double[2];
	
	    p[0] = (b2(0, t) * (x1-x0) + b2(1, t) * (x2-x1) + b2(2, t) * (x3-x2))*3;
	    p[1] = (b2(0, t) * (y1-y0) + b2(1, t) * (y2-y1) + b2(2, t) * (y3-y2))*3;        
	    
	    return p;
	}
    
    /**
     * Calculate the Bezier coefficients
     * 
     * @param i
     * @param t
     * @return
     */
    private double b(int i, double t) {
        
        switch(i) {
        
        case 0:
            return (1-t) * (1-t) * (1-t);

        case 1:
            return 3 * (1-t) * (1-t) * t;
            
        case 2:
            return 3 * (1-t) * t * t;

        case 3:
            return t * t * t;
        }
        
        // this should never happen
        throw new IllegalArgumentException("" + i);
    }
    
	private double b2(int i, double t) {
	    
	    switch(i) {
	    
	    case 0:
	        return (1-t) * (1-t);
	
	    case 1:
	        return 2 * (1-t) * t;
	        
	    case 2:
	        return t * t ;
	    }
	    
	    // this should never happen
	    throw new IllegalArgumentException("" + i);
	}
	
	public void normalize(double t[]){
		double i = Math.sqrt((t[0] * t[0]) + (t[1] * t[1]));
		if(i != 0){
			t[0] = t[0]/i;
			t[1] = t[1]/i;
		}
	}

	public void draw(GL2 gl,double height) {
		gl.glPushMatrix();
		gl.glBindTexture(GL2.GL_TEXTURE_2D, textures.getTextureObject(gl));
		gl.glBegin(GL2.GL_TRIANGLE_STRIP);
		 
		for(double i = 0; i < size(); i += 0.05){
			
        	double[] p = point(i);
        	double[] t = PointTangent(i);
        	double[] normal = new double[2];
        	normal[0] = -t[1];
        	normal[1] = t[0];
        	double w = width()/2;
        	normalize(normal);
        	gl.glNormal3d(0, 0, 1);
        	gl.glTexCoord2d(p[0]-w*normal[0], p[1]-w*normal[1]); 
    		gl.glVertex3d(p[0]-w*normal[0], height + 0.01, p[1]-w*normal[1]);
    		gl.glTexCoord2d(p[0]+w*normal[0], p[1]+w*normal[1]); 
    		gl.glVertex3d(p[0]+w*normal[0], height + 0.01, p[1]+w*normal[1]);    	
		}
		 
		gl.glEnd();
		gl.glBindTexture(GL2.GL_TEXTURE_2D, 0); //reset texture
	    gl.glPopMatrix();
		
	}

}
