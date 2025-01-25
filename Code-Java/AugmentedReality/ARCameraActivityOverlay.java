package com.zatdroid.rodrigo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.TextView;

public class ARCameraActivityOverlay extends Activity implements SensorEventListener {

	private Camera mCamera;
	private ARCameraPreview_Overlay mPreview;

	private AROurSurfaceOverlaySat mSat;
	private AROurSurfaceOverlayCruz mCruz;
	private AROurSurfaceOverlayFlecha mFlecha;
	private TextView mTextView1, mTextView2;
	
	private double az, ele; // azimuth & elevation of the sat
	private String name;
	
	/////////////////////////////////////////////////////
	
	private SensorManager mSensorManager;
	private Sensor mAccelerometer, mMagnetic, mGravity;
	float[] vGrav = new float[3]; // Vector gravedad con acceler—metro
	float[] vMag = new float[3]; // Vector Campo geomagnŽtico
	float[] vR = new float[9]; // Matriz de Rotaci—n con aceler—metro -
								// GeomagnŽtico
	float[] vGravSensorGrav = new float[3]; //Vector Gravedad con Sensor de Gravedad
	float[] vI = new float[9]; // Matriz I
	float[] vOrient = new float[3]; // Acimuth, pitch, Roll
	double elevationCamera, azimuthCamera;

	ARGLRenderOverlayMovSat _ARGLRenderOverlayMovSat;
	ARGLRenderOverlayMovCruz _ARGLRenderOverlayMovCruz; 
	ARGLRenderOverlayMovFlecha _ARGLRenderOverlayMovFlecha; 
	
	DevicePositionProvider devicePositionProvider ;
	
	////////////////////////////////////////////////////////

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Retrieving data from Satellite
		Bundle data = getIntent().getExtras();
		sat sat = data.getParcelable("datosSatElegido");
		// Azimuth & Elevation are retrieved from satellite only once.
		// They are not Updated during the AR View. The calculations should be too complicated
		// and both values do not change significantly in seconds.
		az = sat.getAz();
		ele = sat.getEle();
		name = sat.getName();
		// Keeping screen alive
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		// FullScreen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		_ARGLRenderOverlayMovSat = new ARGLRenderOverlayMovSat(this, sat);
		_ARGLRenderOverlayMovFlecha = new ARGLRenderOverlayMovFlecha(this,sat);
		_ARGLRenderOverlayMovCruz = new ARGLRenderOverlayMovCruz(sat);

		///////////////////////////////////////////////////
		// Create device orientation sensors
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mAccelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mMagnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		mGravity = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
		
		mSensorManager.registerListener(this, mMagnetic,
				SensorManager.SENSOR_DELAY_UI);
		mSensorManager.registerListener(this, mAccelerometer, 300000);
		mSensorManager.registerListener(this, mGravity, SensorManager.SENSOR_DELAY_UI);
		//////////////////////////////////////////////////////////
				
		// Create an instance of Camera
		mCamera = getCameraInstance();

		// Create our Preview view and set it as the content of our activity.
		mPreview = new ARCameraPreview_Overlay(this, mCamera);
		setContentView(mPreview, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		//Draw sat point
		mSat = new AROurSurfaceOverlaySat(this, _ARGLRenderOverlayMovSat);
		addContentView(mSat, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		
		//Draw center cross
		mCruz = new AROurSurfaceOverlayCruz(this, _ARGLRenderOverlayMovCruz);
		addContentView(mCruz, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		//Draw arrow
		mFlecha = new AROurSurfaceOverlayFlecha(this, _ARGLRenderOverlayMovFlecha);
		addContentView(mFlecha, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		//textView1 - Device orientation
		mTextView1 = new TextView(this);
		addContentView(mTextView1, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		int padding_in_dp = 100;  // 500 dps
	    final float scale = getResources().getDisplayMetrics().density; // Scale for dif screens sizes
	    int padding_in_px = (int) (padding_in_dp * scale);
		mTextView1.setPadding(padding_in_px, 0, 0, 0); // Location of textView. 
		
		//textView2 - Sat orientation
		mTextView2 = new TextView(this);
		addContentView(mTextView2, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		int padding_in_dp2 = 330;  // 150 dps
	    int padding_in_px2 = (int) (padding_in_dp2 * scale);
		mTextView2.setPadding(padding_in_px2, 0, 0, 0); // Location of textView. 
		
		FrameLayout preview = new FrameLayout(this);
		addContentView(preview, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
	}

	// A safe way to get an instance of the Camera object.
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open(); // attempt to get a Camera instance
		} catch (Exception e) {
			// Camera is not available (in use or does not exist)
		}
		return c; // returns null if camera is unavailable
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mSat.onPause();
		mCruz.onPause();
		//mFlecha.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mSat.onResume();
		mCruz.onResume();
		//mFlecha.onResume();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		switch (event.sensor.getType()) {
		case Sensor.TYPE_ACCELEROMETER:
			System.arraycopy(event.values, 0, vGrav, 0, 3);
			break;
		case Sensor.TYPE_MAGNETIC_FIELD:
			System.arraycopy(event.values, 0, vMag, 0, 3);
			break;
		case Sensor.TYPE_GRAVITY:
			System.arraycopy(event.values, 0, vGravSensorGrav, 0, 3);
			break;
		}
		
		//Obtener matrices R e I
		mSensorManager.getRotationMatrix(vR, vI, vGrav, vMag);
		mSensorManager.getOrientation(vR, vOrient);
	
		/* Elevation and Azimuth calculation of the device. From the camera«s point of view
		 * in portrait or landscape or a combination based upon the rotation matrix.
		 * The 3rd column vector is the z-axis that aims at the camera direction
		 * in its coordinates in the North-East-Center system (vR[2], vR[5],vR[8]) */
		
		elevationCamera = Math.toDegrees((- Math.asin(vR[8])));
		if (vR[2]>0) {
		azimuthCamera = Math.toDegrees(Math.acos( vR[5] / Math.cos(Math.asin(vR[8])) )) - 180;
		} else {
		azimuthCamera = 180 - Math.toDegrees((Math.acos( vR[5] / ( Math.cos(Math.asin(vR[8])) ))));
		}
			
		// pass elevation, azimuth and gravity from camera to the renderer
		_ARGLRenderOverlayMovSat.receiveOrientation(elevationCamera, azimuthCamera);
		_ARGLRenderOverlayMovSat.receiveGravityVector(vGravSensorGrav);
		_ARGLRenderOverlayMovCruz.receiveOrientation(elevationCamera, azimuthCamera);
		_ARGLRenderOverlayMovFlecha.receiveOrientation(elevationCamera, azimuthCamera);  
		
		// Update text with device orientation 
		mTextView1.setText("DEVICE:" + "\n"+
					          "Acimuth = " + String.format( "%.2f", azimuthCamera ) + "\n"+
							  "Elevation = " + String.format( "%.2f", elevationCamera  ) );  
	    // Format of textview1
		mTextView1.setTextColor(Color.GREEN);
		if (Math.abs(elevationCamera-ele) <5 && Math.abs(azimuthCamera-az)<5)  {
			mTextView1.setTextColor(Color.YELLOW);
		}
		mTextView1.setTypeface(null, Typeface.BOLD);
		mTextView1.setTextSize(18);
		
	    // TextView with sat data
		mTextView2.setText( name + "\n"+
		          "Acimuth = " + String.format( "%.2f", az) + "\n"+
				  "Elevation = " + String.format( "%.2f", ele ) );  
		// Format of textview2
		mTextView2.setTextColor(Color.YELLOW);
		mTextView2.setTypeface(null, Typeface.BOLD);
		mTextView2.setTextSize(18);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		mCamera.release();
		mSensorManager.unregisterListener(this, mMagnetic);
		mSensorManager.unregisterListener(this, mAccelerometer);
		mSensorManager.unregisterListener(this, mGravity);
	}
	
	private void showAlert(String titulo, String mensaje1, String mensaje2) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(titulo);
		alertDialog.setMessage(mensaje1 + "\n" + mensaje2);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		alertDialog.show();
	}

}





