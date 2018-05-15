package ass2.spec;

import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;



/**
 * COMMENT: Comment HeightMap 
 *
 * @author malcolmr
 */
public class Terrain {

    private Dimension mySize;
    private double[][] myAltitude;
    private List<Tree> myTrees;
    private List<Road> myRoads;
    private float[] mySunlight;
    
    private final static int TERRAIN = 0;
    private final static int TREE = 1;
    private final static int ROAD= 1;


    private Texture[] textures;
    
    /**
     * Create a new terrain
     *
     * @param width The number of vertices in the x-direction
     * @param depth The number of vertices in the z-direction
     */
    public Terrain(int width, int depth) {
        mySize = new Dimension(width, depth);
        myAltitude = new double[width][depth];
        myTrees = new ArrayList<Tree>();
        myRoads = new ArrayList<Road>();
        mySunlight = new float[3];
        textures=  new Texture[3];
    }
    
    public Terrain(Dimension size) {
        this(size.width, size.height);
    }

    public Dimension size() {
        return mySize;
    }

    public List<Tree> trees() {
        return myTrees;
    }

    public List<Road> roads() {
        return myRoads;
    }

    public float[] getSunlight() {
        return mySunlight;
    }

    /**
     * Set the sunlight direction. 
     * 
     * Note: the sun should be treated as a directional light, without a position
     * 
     * @param dx
     * @param dy
     * @param dz
     */
    public void setSunlightDir(float dx, float dy, float dz) {
        mySunlight[0] = dx;
        mySunlight[1] = dy;
        mySunlight[2] = dz;        
    }
    
    /**
     * Resize the terrain, copying any old altitudes. 
     * 
     * @param width
     * @param height
     */
    public void setSize(int width, int height) {
        mySize = new Dimension(width, height);
        double[][] oldAlt = myAltitude;
        myAltitude = new double[width][height];
        
        for (int i = 0; i < width && i < oldAlt.length; i++) {
            for (int j = 0; j < height && j < oldAlt[i].length; j++) {
                myAltitude[i][j] = oldAlt[i][j];
            }
        }
    }

    /**
     * Get the altitude at a grid point
     * 
     * @param x
     * @param z
     * @return
     */
    public double getGridAltitude(int x, int z) {
        return myAltitude[x][z];
    }
    
    /**
     * Set the altitude at a grid point
     * 
     * @param x
     * @param z
     * @return
     */
    public void setGridAltitude(int x, int z, double h) {
        myAltitude[x][z] = h;
    }

    /**
     * Get the altitude at an arbitrary point. 
     * Non-integer points should be interpolated from neighbouring grid points
     * 
     * TO BE COMPLETED
     * 
     * @param x
     * @param z
     * @return
     */
    public double altitude(double x, double z) {//FIX THIS!
    	double altitude = 0;
    	// ensure parameters are within bounds of dimension
    	if (x > mySize.getWidth() - 1){
    		x = mySize.getWidth() - 1;
    	}
    	if (z > mySize.getHeight() - 1){
    		z = mySize.getHeight() - 1;
    	}
    	if (x < 0){
    		x = 0;
    	}
    	if (z < 0){
    		z = 0;
    	}

    	if (x % 1 != 0 && z % 1 != 0){
    		int x1 = (int) Math.floor(x);
    		int x2 = (int) Math.ceil(x);
    		double leftAltitude = altitude(x1, z);
    		double rightAltitude = altitude(x2, z);
    		altitude = (x - x1)/(x2 - x1) * rightAltitude + (x2 - x)/(x2 - x1) * leftAltitude;
    	} else if (x % 1 != 0) {
    		int x1 = (int) Math.floor(x);
    		int x2 = (int) Math.ceil(x);
    		double rightAltitude = getGridAltitude(x2, (int) z);
    		double leftAltitude =  getGridAltitude(x1, (int) z);
    		altitude = (x - x1)/(x2 - x1) * rightAltitude + (x2 - x)/(x2 - x1) * leftAltitude;
    	} else if (z % 1 != 0) {
    		int z1 = (int) Math.floor(z);
    		int z2 = (int) Math.ceil(z);
    		double floorAltitude = getGridAltitude((int) x, z1);
    		double ceilAltitude = getGridAltitude((int) x, z2);
			altitude = (z - z1)/(z2 - z1) * ceilAltitude + (z2 - z)/(z2 - z1) * floorAltitude;
    	} else {
    		altitude = getGridAltitude((int) x, (int) z);
    	}
        return altitude;
    }

    /**
     * Add a tree at the specified (x,z) point. 
     * The tree's y coordinate is calculated from the altitude of the terrain at that point.
     * 
     * @param x
     * @param z
     */
    public void addTree(double x, double z) {
        double y = altitude(x, z);
        Tree tree = new Tree(x, y, z);
        myTrees.add(tree);
    }


    /**
     * Add a road. 
     * 
     * @param x
     * @param z
     */
    public void addRoad(double width, double[] spine) {
        Road road = new Road(width, spine);
        myRoads.add(road);        
    }
    public void load(File terrain, File tree,File road){
    	try{
             this.textures[TERRAIN] = TextureIO.newTexture(terrain, true);
             this.textures[TREE] = TextureIO.newTexture(tree, true);
             //TODO: add all textures;
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	for(Road r: myRoads){
    		r.load(road);
    	}
    	for(Tree t :myTrees){
    		t.load(tree, terrain);
    	}
    }
    public void draw(GLAutoDrawable drawable){
    	GL2 gl = drawable.getGL().getGL2();

        float matAmbAndDif[] = {1.0f, .85f, .5f, 1.0f};
        float matSpec[] = { .0f, .5f, 1.0f, 1.0f };
        float matShine[] = { 0.0f };
        float emm[] = {0.0f, 0.0f, 0.0f, 1.0f};

        // Material properties
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDif,0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, matSpec,0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, matShine,0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, emm,0);

        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 80);	// phong
    	gl.glEnable(GL2.GL_TEXTURE_2D);
        // Specify how texture values combine with current surface color values.
    	gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
    	gl.glMatrixMode(GL2.GL_MODELVIEW);
    	drawBaseTerrain(gl);
    	drawTrees(gl);
    	drawRoads(gl);
    	//sunLighting.setSunLighting(gl, mySunlight, 20);
    	//test(gl);
    	//TODO: DRAW TREES and ROADS!!!
    }
    private void drawTrees(GL2 gl){
    	for(Tree t: myTrees){
    		t.draw(gl);
    	}
    }
    private void drawRoads(GL2 gl){
    	for(Road r: myRoads){
    		r.draw(gl,altitude(r.controlPoint(0)[0],r.controlPoint(0)[0]));
    	}
    }
    private void drawBaseTerrain(GL2 gl){
    	gl.glPushMatrix();
		gl.glBindTexture(GL2.GL_TEXTURE_2D, textures[TERRAIN].getTextureObject(gl)); //CHECK
		gl.glBegin(GL2.GL_TRIANGLE_STRIP);
		for(int row =0;row< myAltitude.length -1; row++){
			for(int col = 0; col<myAltitude[0].length -1; col++){
				
				double[] v0 = new double[]{col, this.getGridAltitude(col, row) , row};
				double[] v1 = new double[]{col, this.getGridAltitude(col, row+1) , row+1};
				double[] v2 = new double[]{col+1, this.getGridAltitude(col+1, row) , row};
				double[] v3 = new double[]{col+1, this.getGridAltitude(col+1, row+1) , row+1};

				double normal1[] = MathUtil.calculateNormal(v0,v1,v2);
				double normal2[] = MathUtil.calculateNormal(v1,v2,v3);//for each triangle

				gl.glNormal3d(normal1[0], normal1[1], normal1[2]);
				gl.glTexCoord2d(0.0, 0.0);	
				gl.glVertex3d(v0[0],v0[1],v0[2]);
				gl.glTexCoord2d(0.0, 1.0);
				gl.glVertex3d(v1[0],v1[1],v1[2]);
				gl.glTexCoord2d(1.0, 0.0);
				gl.glVertex3d(v2[0],v2[1],v2[2]);
				gl.glTexCoord2d(1.0, 1.0);
				gl.glNormal3d(normal2[0], normal2[1], normal2[2]);
				gl.glVertex3d(v3[0],v3[1],v3[2]);
			}
		}
		gl.glEnd();
		gl.glBindTexture(GL2.GL_TEXTURE_2D, 0); //reset texture
	    gl.glPopMatrix();
    }


}
