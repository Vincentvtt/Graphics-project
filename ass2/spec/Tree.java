package ass2.spec;

import java.io.File;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

/**
 * COMMENT: Comment Tree 
 *
 * @author malcolmr
 */
public class Tree {

    private double[] myPos;
    private Texture textures[];
    
    public Tree(double x, double y, double z) {
        myPos = new double[3];
        myPos[0] = x;
        myPos[1] = y;
        myPos[2] = z;
        textures = new Texture[2];
    }
    
    public void load(File sphere, File cylinder){
    	try{
             this.textures[0] = TextureIO.newTexture(sphere, true);
             this.textures[1] = TextureIO.newTexture(cylinder, true);
             //TODO: add all textures;
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    public double[] getPosition() {
        return myPos;
    }
    
    public void draw(GL2 gl){
    	GLUT glut = new GLUT();
    	
    	gl.glPushMatrix();
    	gl.glTranslated(myPos[0], myPos[1], myPos[2]);
    	gl.glRotated(-90, 1, 0, 0);
        gl.glEnable(GL2.GL_TEXTURE_GEN_S); //enable texture coordinate generation
        gl.glEnable(GL2.GL_TEXTURE_GEN_T);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, textures[1].getTextureObject(gl));
        glut.glutSolidCylinder(0.1, 1, 10, 10);
        gl.glDisable(GL2.GL_TEXTURE_GEN_S); //enable texture coordinate generation
        gl.glDisable(GL2.GL_TEXTURE_GEN_T);
    	
    	gl.glPopMatrix();
    	
    	
    	gl.glPushMatrix();
    	gl.glTranslated(myPos[0], myPos[1]+.9, myPos[2]);
    	gl.glTexGeni(GL2.GL_S, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_SPHERE_MAP);
    	gl.glTexGeni(GL2.GL_T, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_SPHERE_MAP);
    	gl.glEnable(GL2.GL_TEXTURE_2D);
        gl.glEnable(GL2.GL_TEXTURE_GEN_S); //enable texture coordinate generation
        gl.glEnable(GL2.GL_TEXTURE_GEN_T);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, textures[0].getTextureObject(gl));
        glut.glutSolidSphere(0.2, 10, 10);
        
        gl.glDisable(GL2.GL_TEXTURE_GEN_S); //enable texture coordinate generation
        gl.glDisable(GL2.GL_TEXTURE_GEN_T);
    	
    	gl.glPopMatrix();
    	
    }
    

}
