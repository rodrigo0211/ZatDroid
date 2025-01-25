package com.zatdroid.rodrigo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

public class intro extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Allow fullscreen view
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.layout_intro);

		// Thread management
		final Thread timer = new Thread() {
			public void run() {
				try {
					sleep(1000);
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					try {
						Intent startApp = new Intent("com.zatdroid.rodrigo.LISTATIPOSAT");
						startActivity(startApp);
					} catch (ActivityNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		};
		// Check if there is an internet conection
		if (isInternetOn()) {  // if it is, proceed with app	
			   timer.start();
		}
		else { // if not, alert message and close app
			showAlert(getResources().getString(R.string.internet_connection_title),
					getResources().getString(R.string.internet_connection_1),
					getResources().getString(R.string.internet_connection_2) 
			);
		}

	}
	public final boolean isInternetOn() {
		ConnectivityManager connec =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		// ARE WE CONNECTED TO THE NET?
		if ( connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
		connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
		connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING ||
		connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED ) {
		// MESSAGE TO SCREEN FOR TESTING (IF REQ)
		return true;
		} else if ( connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED ||  connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED  ) {
			return false;
		}
		return false;
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
	
	@Override
	protected void onPause() {
		super.onPause();
		this.finish(); //This activity is not showing anything when resuming.
		}
	
}
