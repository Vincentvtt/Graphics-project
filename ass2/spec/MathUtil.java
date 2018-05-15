package ass2.spec;

public class MathUtil{

	 public static double [] crossProduct(double u [], double v[]){
	    	double crossProduct[] = new double[3];
	    	crossProduct[0] = u[1]*v[2] - u[2]*v[1];
	    	crossProduct[1] = u[2]*v[0] - u[0]*v[2];
	    	crossProduct[2] = u[0]*v[1] - u[1]*v[0];
	    
	    	return crossProduct;
	    }
	    
	    public static double[] calculateNormal(double[] p0, double p1[], double p2[]){
	    	double [] u = {p1[0] - p0[0], p1[1] - p0[1], p1[2] - p0[2]};
	    	double [] v = {p2[0] - p0[0], p2[1] - p0[1], p2[2] - p0[2]};
	    	double [] normal = crossProduct(u,v);
	    	return normalise(normal);
	    }
	    //reword methods below!!!
	    public static double [] normalise(double [] n){
	    	double  mag = getMagnitude(n);
	    	double norm[] = {n[0]/mag,n[1]/mag,n[2]/mag};
	    	return norm;
	    }
	    
	    public static double getMagnitude(double [] n){
	    	double mag = n[0]*n[0] + n[1]*n[1] + n[2]*n[2];
	    	mag = Math.sqrt(mag);
	    	return mag;
	    }

	    //get Angle on the XZ plane between 2 points. Angle is calculated from the Z axis
	    public static double getXZAngle(double []p1, double[] p2){
	    	double angle = 0;
	    	double x = (p2[0] - p1[0]);
	    	double y = (p2[2] - p1[2]);
	    	
	    	if(x >= 0 && y>=0){
		    	angle = (Math.atan(x/y));
	    	}else if(y < 0 && x >= 0 ){
		    	angle = Math.abs(Math.atan(y/x)) + Math.PI/2;
	    	}else if(y < 0 && x < 0 ){
		    	angle = Math.abs(Math.atan(x/y)) + Math.PI;
	    	}else if(y >= 0 && x < 0 ){
		    	angle = Math.abs(Math.atan(y/x)) + (Math.PI *1.5);
	    	}
	    	return angle;
	    }
	    public static double getXZDistance(double[]p1, double[] p2){
	    	double x = (p2[0] - p1[0]);
	    	double z = (p2[2] - p1[2]);
	    	
	    	return Math.sqrt(x*x + z*z);	    	
	    }
	    public static double degreesToRad(double degrees){
	    	return degrees * (Math.PI/180);
	    }
}
