package com.zatdroid.rodrigo;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.os.SystemClock;
import android.util.Log;

public class ARGLRenderOverlayMovCruz implements Renderer {

	private ARGLCruz Cruz;

	// sat data
	private double eleSat;
	private double azSat;
	private double dist2;
	
	/// elevacion y azimuth donde apunta el dispositivo
	double eleDevice;
	double azDevice;

	// Recibe información de la Activity con los datos de la orientación del
	// dispositivo
	public void receiveOrientation( double elevation, double azimuth) {
		eleDevice = elevation;
		azDevice = azimuth;
	}
	
	public ARGLRenderOverlayMovCruz(sat satellite) {
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
		// TODO Auto-generated method stub
		gl.glDisable(GL10.GL_DITHER);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
		gl.glClearColor(.1f, .5f, .1f, 0f);
		gl.glColor4f(1f, 0f, 0f, 1f);
		gl.glClearDepthf(1f);
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		// TODO Auto-generated method stub
		gl.glDisable(GL10.GL_DITHER);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		//cambiar de color y espesor el cuadrado cuando la cámara apunte al satélite
		if (Math.abs(eleDevice-eleSat) <5 && Math.abs(azDevice-azSat)<5) {
			gl.glColor4f(1f, 1f, 0f, 1f);
			gl.glLineWidth(8);
		} else {
			gl.glColor4f(1f, 0f, 0f, 1f); 
			gl.glLineWidth(3);
		}

		GLU.gluLookAt(gl, -5, 0, 0, 0, 0, 0, 0 , 0, 1);

		Cruz = new ARGLCruz();
		   Cruz.draw(gl);
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
