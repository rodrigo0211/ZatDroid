package com.zatdroid.rodrigo;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

public class ARGLRenderOverlayMovFlecha implements Renderer {

	private ARGLFlecha satPoint;
	private sat satellite;
	float[] vGravedad = new float[3]; //Gravity vector
	private float[] sat = new float[3];

	// sat data
	private double eleSat;
	private double azSat;
	private double dist2;
	
	/// elevation y azimuth where the device points at
	double eleDevice;
	double azDevice;
	
	private Context context;

	// Receives info from the Activity with data of the device orientation
	public void receiveOrientation( double elevation, double azimuth) {
		//Filters so that the sat icon does not jump
		//http://stackoverflow.com/questions/4699417/android-compass-orientation-on-unreliable-low-pass-filter
		eleDevice = filterSmoothMovements(eleDevice, elevation);
		azDevice = filterSmoothMovements(azDevice, azimuth);
		
	}
	
	private double filterSmoothMovements(double oldValue, double newValue) { //Valores en grados 
		
		// The easing float that defines how smooth the movement will be (1 is
			// no smoothing and 0 is never updating, my default is 0.5). We will
			// call it SmoothFactorCompass.
		// The threshold in which the distance is big enough to turn immediatly
			// (0 is jump always, 360 is never jumping, my default is 30). We will
			// call it SmoothThresholdCompass.
		double SmoothFactorCompass = .4; // factor so that small jumps do not disturb
		double SmoothThresholdCompass = 30.0; //minimum distance for the icon to redraw
		double temp;
		
		if (Math.abs(newValue - oldValue) < 180) {
		    if (Math.abs(newValue - oldValue) > SmoothThresholdCompass) {
		        temp = newValue;
		    }
		    else {
		        temp = oldValue + SmoothFactorCompass * (newValue - oldValue);
		    }
		}
		else {
		    if (360.0 - Math.abs(newValue - oldValue) > SmoothThresholdCompass) {
		        temp = newValue;
		    }
		    else {
		        if (oldValue > newValue) {
		            temp = (oldValue + SmoothFactorCompass * ((360 + newValue - oldValue) % 360) + 360) % 360;
		        } 
		        else {
		            temp = (oldValue - SmoothFactorCompass * ((360 - newValue + oldValue) % 360) + 360) % 360;
		        }
		    }
		}
		
		return temp;
	}
	
	public ARGLRenderOverlayMovFlecha(Context context, sat satellite) {
		this.context = context;
		satPoint = new ARGLFlecha();
		this.satellite = satellite;
		/* Retrieving sat data
		 * Azimuth & Elevation are retrieved from satellite only once.
		 * They are not Updated during the AR View. The calculations should be too complicated
		 * and both values do not change significantly in seconds. */
		eleSat = satellite.getEle();
		azSat = satellite.getAz();
		dist2 = 400f;
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig eglConfig) {
		
	//	gl.glEnable(GL10.GL_TEXTURE_2D);			//Enable Texture Mapping
		gl.glShadeModel(GL10.GL_SMOOTH); 			//Enable Smooth Shading
		gl.glClearColor(.1f, .5f, .1f, 0f); 	//Black Background
		gl.glClearDepthf(1.0f); 					//Depth Buffer Setup
		gl.glEnable(GL10.GL_DEPTH_TEST); 			//Enables Depth Testing
		gl.glDepthFunc(GL10.GL_LEQUAL); 			//The Type Of Depth Testing To Do

		//Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glDisable(GL10.GL_DITHER);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glColor4f(1f, 1f, 0f, 1f);
		gl.glLineWidth(6);

		/* **********Draw sat using proportionality   ******** */
		   GLU.gluLookAt(gl, -5, 0, 0,  //Camera position
		                      0, 0, 0,  // direction
		                      0, 0, 1);  //Vertical vector, axis "z"
		   	   
		   // Configuration of the screen limits
		   // Erase the arrow when device and sat orientation com close
		   float yMax, zMax, eleMax, azMax;
		   yMax = 1; zMax = 1; 
		   eleMax = 20; azMax = 30;
		   sat[0] = 0;
		   //Draw Arrow only when Sat is out of the limits of the screen
		   if (((Math.abs(eleDevice-eleSat) > eleMax) || Math.abs(azDevice-azSat) > azMax)) {
		   // Different location for the arrow for device positions far away from sat
			   sat[2] = (float) (-eleDevice + eleSat) * zMax / eleMax;
			   sat[1] = (float) ( azDevice - azSat ) * yMax / azMax;
			 
			  // Load the texture for the square
			   satPoint.Update(sat);
			   //Draw satellite
			   satPoint.draw(gl);
			   }
	}
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// TODO Auto-generated method stub
		gl.glViewport(0, 0, width, height);
		float ratio = (float) width / height;
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();;
		gl.glFrustumf(-ratio, ratio, -.6f, .6f, 0.5f, 500);
	}

}
