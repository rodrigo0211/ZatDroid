package com.zatdroid.rodrigo;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

public class ARGLRenderOverlayMovSat implements Renderer {

	private ARGLSat satPoint;
	private sat satellite;
	float[] vGravedad = new float[3]; // gravity vector
	private float[] sat = new float[3];
	
	// Azimuth, Elevation of sat and distance for openGL camera view
	private double eleSat;
	private double azSat;
	private double dist2;
		
	// Elevation and Azimuth where the devices points at
	double eleDevice;
	double azDevice;
	
	private Context context;

	// Receives information from the activity with data of the actual device orientation
	public void receiveOrientation( double elevation, double azimuth) {
		//Filters so that the sat icon does not moves constantly
		//http://stackoverflow.com/questions/4699417/android-compass-orientation-on-unreliable-low-pass-filter
		eleDevice = filterSmoothMovements(eleDevice, elevation);
		azDevice = filterSmoothMovements(azDevice, azimuth);
	}
	
	private double filterSmoothMovements(double oldValue, double newValue) { // Values in degrees 
		
		// The easing float that defines how smooth the movement will be (1 is
			// no smoothing and 0 is never updating, my default is 0.5). We will
			// call it SmoothFactorCompass.
		// The threshold in which the distance is big enough to turn immediately
			// (0 is jump always, 360 is never jumping, my default is 30). We will
			// call it SmoothThresholdCompass.
		double SmoothFactorCompass = .4; //factor so that the small jumps do not disturb
		double SmoothThresholdCompass = 30.0; // minimum distance so that the icon jumps
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

	public void receiveGravityVector(float[] g) {
		vGravedad[0] = -g[0];
		vGravedad[1] = -g[1];
		vGravedad[2] = -g[2];
	}
	
	public ARGLRenderOverlayMovSat(Context context, sat satellite) {
		this.context = context;
		satPoint = new ARGLSat();
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
		
		gl.glEnable(GL10.GL_TEXTURE_2D);			//Enable Texture Mapping
		gl.glShadeModel(GL10.GL_SMOOTH); 			//Enable Smooth Shading
		gl.glClearColor(.1f, .5f, .1f, 0f); 		//Black Background
		gl.glClearDepthf(1.0f); 					//Depth Buffer Setup
		gl.glEnable(GL10.GL_DEPTH_TEST); 			//Enables Depth Testing
		gl.glDepthFunc(GL10.GL_LEQUAL); 			//The Type Of Depth Testing To Do

		//Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		// TODO Auto-generated method stub
		gl.glDisable(GL10.GL_DITHER);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();

		////Not needed //////
		//sat position
/*		double[] satD = new double[3]; //  sat position
		satD[0] = dist2 * Math.cos(Math.toRadians(eleSat))
				* Math.cos(Math.toRadians(azSat));
		satD[1] = -dist2 * Math.cos(Math.toRadians(eleSat))
				* Math.sin(Math.toRadians(azSat));
		satD[2] = dist2 * Math.sin(Math.toRadians(eleSat));
	*/	
/* ******************************************************************************** */		

		// Change sat icon color, when it is centered
		if (Math.abs(eleDevice-eleSat) <5 && Math.abs(azDevice-azSat)<5) {

			gl.glColor4f(1f, .5f, 0f, 1f);
			gl.glPointSize(40.0f);
		} else {
			//gl.glColor4f(0f, 0f, 0f, .5f); 
			gl.glPointSize(20.0f);
		}

///////////////////////////// Moving openGl according to its coord system ///////////////////////////
/*		// Calculating object coordinates where th GLSurfaceView camera points at
		double dist = 10f; // View distance from the camera
		//North +axisx, left +axisy Up +axisz
		
		CameraPointAt[0] = dist * Math.cos(eleDevice * Math.PI / 180f)
				* Math.cos(azDevice * Math.PI / 180f);
		CameraPointAt[1] = -dist * Math.cos(eleDevice * Math.PI / 180f)
				* Math.sin(azDevice * Math.PI / 180f);
		CameraPointAt[2] = dist * Math.sin(eleDevice * Math.PI / 180f);

		double mCameraPointAt = Math
				.sqrt(Math.pow(CameraPointAt[0], 2)
						+ Math.pow(CameraPointAt[1], 2)
						+ Math.pow(CameraPointAt[2], 2));

		double[] CamPointAtNormalizado = new double[3];
		CamPointAtNormalizado[0] = CameraPointAt[0] / mCameraPointAt;
		CamPointAtNormalizado[1] = CameraPointAt[1] / mCameraPointAt;
		CamPointAtNormalizado[2] = CameraPointAt[2] / mCameraPointAt;

		double[] CamCenter = new double[3];
		double dc = 5;
		CamCenter[0] = -dc * CamPointAtNormalizado[0];
		CamCenter[1] = -dc * CamPointAtNormalizado[1];
		CamCenter[2] = -dc * CamPointAtNormalizado[2];

		// Getting integer values to be able to use gluLookAt
		int[] iCamPointAt = new int[3]; // integer vector converts to
										// gluLookAt()
		iCamPointAt[0] = (int) Math.round(CameraPointAt[0]);
		iCamPointAt[1] = (int) Math.round(CameraPointAt[1]);
		iCamPointAt[2] = (int) Math.round(CameraPointAt[2]);
				
		int[] iCamCenter = new int[3];
		iCamCenter[0] = (int) CamCenter[0];
		iCamCenter[1] = (int) CamCenter[1];
		iCamCenter[2] = (int) CamCenter[2];
		
		float[] sat = new float[3];
		sat[0] = (float) satD[0];
		sat[1] = (float) satD[1];
		sat[2] = (float) satD[2];
		
		GLU.gluLookAt(gl, iCamCenter[0], iCamCenter[1], iCamCenter[2],
				iCamPointAt[0], iCamPointAt[1], iCamPointAt[2], 0, 0, 1);
		tri = new ARGLTriangEx(sat, sat);
		tri.draw(gl);
*/
///////////////////////////////////////////////////////////////////////////////////

/////////////// Draw sat using proportionality  ///////////////////////////////

		   //Plane vGravedad y vCameraPointAt.
		   //vCameraPointAt is always (0,0,-1) in device coord system. Set (0,0,1) due to vector sign
		   //vGravedad is the gravity vector expressed in the device coord system.
		   float vCameraPoinAt_Device[] = new float[3];
		   vCameraPoinAt_Device[0] = 0;
		   vCameraPoinAt_Device[1] = 0;
		   vCameraPoinAt_Device[2] = 1;
		   
		   // Intersection PLane (gravity and vCamerapointAt) and screen pane (z=0)
		   float vVerticalScreen[] = new float[3];
		   vVerticalScreen[1] = - (vCameraPoinAt_Device[1] * vGravedad[2] - vCameraPoinAt_Device[2] *vGravedad[1] ) 
				   				/ (vCameraPoinAt_Device[2] * vGravedad[0] - vCameraPoinAt_Device[0] *vGravedad[2]);
		   // Correction of free coordinate of vector Vertical
		   // we have the line, but not the direction and we have to correct that
		   if ( vGravedad[0] >  0) {
			   vVerticalScreen[0] = -1;
		   } else vVerticalScreen[0] = 1;
		   vVerticalScreen[2] = 0;

		   GLU.gluLookAt(gl, -5, 0, 0,  // Camera position
				              0, 0, 0,  // direction pointing at
				              0, 0, 1); // vertical vector,  "z" axis
		   
		   float vVerticalScreenMod[] = new float[3];
		   vVerticalScreenMod[0] = vVerticalScreen[2]; //always 0
		   vVerticalScreenMod[1] = vVerticalScreen[1];
		   vVerticalScreenMod[2] = vVerticalScreen[0];

		   // Configuration of screeen limits
		   float yMax, zMax, eleMax, azMax;
		   yMax = 15; zMax = 15; 
		   eleMax = 30; azMax = 30;
		   sat[0] = 0;
		   
		   if (((Math.abs(eleDevice-eleSat) < eleMax) && Math.abs(azDevice-azSat) < azMax)) {
			   sat[2] = (float) (-eleDevice + eleSat) * zMax / eleMax;
			   sat[1] = (float) ( azDevice - azSat ) * yMax / azMax;
			// Load the texture for the square
			   satPoint.Update(sat, vVerticalScreenMod);
			   satPoint.loadGLTexture(gl, this.context);
			   gl.glEnable(GL10.GL_TEXTURE_2D);  
			// Draw satellite
			   satPoint.draw(gl);
			   
		   } 
//////////////////////////////////////////////////////////////////////////////
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
