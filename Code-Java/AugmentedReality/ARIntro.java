package com.zatdroid.rodrigo;

import java.util.Calendar;
import java.util.TimeZone;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.WindowManager;

public class ARIntro extends Activity {

	private double latitude, longitude, altitude; // Of device
	private sat sat;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Allow fullscreen view
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		//Retrieving data from Satellite
		Bundle data = getIntent().getExtras();
		sat = data.getParcelable("datosSatElegido");

        /** ********** Obtain device position- long- lt - alt ********************** **/
        // check if GPS enabled
        DevicePositionProvider devicePositionProvider = new DevicePositionProvider(ARIntro.this);
        if(devicePositionProvider.canGetLocation()){
            latitude = devicePositionProvider.getLatitude();
            longitude = devicePositionProvider.getLongitude();
            altitude = devicePositionProvider.getAltitude();
         } else {
            // can't get location - GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
         	devicePositionProvider.showSettingsAlert();
         }
        /** *************************************************************************** **/
        
        /* ************************ Calculate Azimuth - Elevation **************************** */	
		CalculationsTime TimeFunctions = new CalculationsTime();
		//Current DATETIME in UTC
		Calendar ctime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		//Convert currentTime (Calendar) to JD
		double currentTimeJD = TimeFunctions.Julian_Date(ctime);
		
		// lat - lon .alt from device in a vector
		vector devicePosition= new vector(latitude, longitude, altitude);
		
		//Calculate azimuth and elevation
		CalculationsSatTRACK calculationsSatTRACK = new CalculationsSatTRACK();
		sat = calculationsSatTRACK.satTRACK(currentTimeJD, sat, devicePosition);
		 /* ******************************************************************************** */	
		
		// check elevation from satellite is > 0. 
		// If elevation < 0 -> Calculate time elapsed to next overhead pass
		 
		if (sat.getEle() < 0 ) { 
			//Loop to calculate next overhead pass
			sat satNext;
			double time=currentTimeJD;
			double min = 1;// min is the minutes interval increased on every interaction
			int i=0;
			do {// loop adding 1 minute to calculation of elevation
				time=time + 0.000694*min;
				satNext = calculationsSatTRACK.satTRACK(time, sat, devicePosition);
				i++;
			} while (satNext.getEle() < 0);
			showAlert(getResources().getString(R.string.satNotVisible_Title),
					getResources().getString(R.string.satNotVisible_1),
					getResources().getString(R.string.satNotVisible_2) + " " +
					Integer.toString(i) + " min");
		} else {
		
		// Show camera view even if the satellite elevation is < 0
		Intent startApp = new Intent("com.zatdroid.rodrigo.ARCAMERAACTIVITYOVERLAY");
		startApp.putExtra("datosSatElegido", sat);
		startActivity(startApp);
		}
		
	}

	private void showAlert(String titulo, String mensaje1, String mensaje2) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(titulo);
		alertDialog.setMessage(mensaje1 + "\n" + mensaje2);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//finish();
				// Show camera view even if the satellite elevation is < 0
				Intent startApp = new Intent("com.zatdroid.rodrigo.ARCAMERAACTIVITYOVERLAY");
				startApp.putExtra("datosSatElegido", sat);
				startActivity(startApp);
			}
		});
		alertDialog.show();
	}

	@Override
	protected void onPause() {
		super.onPause();
		this.finish(); //This activity is not showing anything after using it
		}
	}
	
