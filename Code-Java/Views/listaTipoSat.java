package com.zatdroid.rodrigo;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class listaTipoSat extends Activity implements View.OnClickListener {

	public static String fileName = "myFile";
	Button bSpecial, bWeather, bCommunications, bNavigation, bScientific, bMisc, 
		   bBreadCrumbs, bBreadCrumbsEnd;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final float height = getResources().getDisplayMetrics().heightPixels; // Scale for dif screens sizes
		final float width = getResources().getDisplayMetrics().widthPixels; // Scale for dif screens sizes

		setContentView(layoutCode(width, height));
	}

	// Need to set layout with code to assure icons UI be the same in different screen sizes
	private LinearLayout layoutCode(float width, float height) {
		
		//base linearlayout vertical
		LinearLayout l0 = new LinearLayout(this);
		l0.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams paramsl0 = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		l0.setLayoutParams(paramsl0);
		
		// Add BreadCrumbs horizontal linearlayout to base linearlayout
		l0.addView(breadCrumbs(height, width));	
		
		// Linearlayout to send to scrollview
		LinearLayout l1 = new LinearLayout(this);
		l1.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams paramsl1 = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		l1.setLayoutParams(paramsl1);
		
		// Linearlayout searchActivity
		int sct = (int) (0.05 * height);
		int m = (int) (0.15 * width);
		LinearLayout lsearch = new LinearLayout(this);
		lsearch.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams paramslsearch = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		paramslsearch.setMargins(m, sct, m, 0);
		lsearch.setLayoutParams(paramslsearch);

        // scrollview does not contain BreadCrumbs
		// It is usually not needed. Just in case and for future reuses of this module
		int sct2 = (int) (0.02 * height);
		ScrollView scrollView= new ScrollView(this);
		LinearLayout.LayoutParams paramsSc = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		paramsSc.setMargins(0, sct2, 0, 0);
		scrollView.setLayoutParams(paramsSc);
		
			// Three lines of icons (2icons per line)
		//Retrieve images of icons
		Drawable d1 = getResources().getDrawable(R.drawable.special_interest);
		Drawable d2 = getResources().getDrawable(R.drawable.weather_earth);
		Drawable d3 = getResources().getDrawable(R.drawable.communications);
		Drawable d4 = getResources().getDrawable(R.drawable.navigation);
		Drawable d5 = getResources().getDrawable(R.drawable.scientific);
		Drawable d6 = getResources().getDrawable(R.drawable.miscellaneous);
		
		//Retrieve strings
		String s1 = getResources().getString(R.string.lg_0);
		String s2 = getResources().getString(R.string.lg_1);
		String s3 = getResources().getString(R.string.lg_2);
		String s4 = getResources().getString(R.string.lg_3);
		String s5 = getResources().getString(R.string.lg_4);
		String s6 = getResources().getString(R.string.lg_5);

		//Create buttons
		Button  bSpecial = new Button(this);
		Button  bWeather = new Button(this);
		Button  bCommunications = new Button(this);
		Button  bNavigation = new Button(this);
		Button  bScientific = new Button(this);
		Button  bMisc = new Button(this);
		Button  bSearch = new Button(this);
		
		// Adding buttons to the main layout
		l1.addView(iconsLine(bSpecial, bWeather,width,height, d1, d2, s1, s2, 1, 2 ));
		l1.addView(iconsLine(bCommunications, bNavigation,width,height, d3, d4, s3, s4, 3, 4 ));
		l1.addView(iconsLine(bScientific, bMisc, width, height, d5, d6, s5, s6, 5, 6 ));
		
		// ScrollView does not contain BreadCrumbs.
		scrollView.addView(l1);
		l0.addView(scrollView);
		
		// Search Button
		bSearch.setText(R.string.search1);
		bSearch.setBackgroundDrawable(getResources().getDrawable(R.drawable.cool_button_background));
		bSearch.setTextColor(Color.BLACK);
		bSearch.setId(100);
		bSearch.setSingleLine(true);
		bSearch.setTextScaleX(scaleButtonText((int) (0.05 * height),(int)( 0.80* width), (int)(0.1*width),
				getResources().getString(R.string.search1)));
		bSearch.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (0.05 * height));
		
		lsearch.addView(bSearch);
		l0.addView(lsearch);
		
		// Setting onClickListeners
		bSpecial.setOnClickListener(this);
		bWeather.setOnClickListener(this);
		bCommunications.setOnClickListener(this);
		bNavigation.setOnClickListener(this);
		bScientific.setOnClickListener(this);
		bMisc.setOnClickListener(this);
		bSearch.setOnClickListener(this);
		
		return l0;
	}
	
	private LinearLayout iconsLine( Button b0, Button b1, float width,float height,
							    Drawable d0, Drawable d1, 
								String s0, String s1, int id0, int id1) {

		// Layout from first 2 icons
		int h2 = (int) (0.18 * height);
		int m2_l = (int) (0.125* width);
		int m2_r = (int) (0.125* width);
		int m2_t = (int) (0.05 * height);
		LinearLayout l2 = new LinearLayout(this);
		l2.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams paramsl22 = new LinearLayout.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		paramsl22.height = h2;
		paramsl22.setMargins(m2_l,m2_t, m2_r, 0);
		l2.setLayoutParams(paramsl22);
		int w_icon = (int) (0.3* width);
		int m_l_icon = (int) (0.15 * width);
		
		// modify margins
		LinearLayout.LayoutParams params_icon_l = new LinearLayout.LayoutParams(
				w_icon,	LinearLayout.LayoutParams.MATCH_PARENT );
		params_icon_l.setMargins(0, 0, 0, 0); // Center icons for different screen sizes
		
		// Icon0 
		b0.setLayoutParams(params_icon_l);
		b0.setText(s0);
		b0.setTextColor(Color.WHITE);
		int t_icon = (int) (0.12 * h2);

		// Fit text size to button size
		b0.setTextScaleX(scaleButtonText(t_icon, w_icon, 0,s0));
		b0.setTextSize(TypedValue.COMPLEX_UNIT_PX, t_icon);
		b0.setSingleLine(true);

		b0.setPadding(0, 0, 0, 0);
		b0.setGravity(Gravity.BOTTOM | Gravity.CENTER );
		b0.setBackgroundDrawable(d0);
		b0.setId(id0);
		l2.addView(b0);
		
		LinearLayout.LayoutParams params_icon_r = new LinearLayout.LayoutParams(
				w_icon,	LinearLayout.LayoutParams.MATCH_PARENT );
		params_icon_r.setMargins(m_l_icon, 0, 0, 0); 
		
		// Icon1
		b1.setLayoutParams(params_icon_r);
		b1.setText(s1);
		b1.setTextColor(Color.WHITE);
		// Fit text size to button size
		b1.setSingleLine(true);
		b1.setTextScaleX(scaleButtonText(t_icon, w_icon, 0,s1));
		b1.setTextSize(TypedValue.COMPLEX_UNIT_PX, t_icon);
		
		b1.setPadding(0, 0, 0, 0);
		b1.setGravity(Gravity.BOTTOM | Gravity.CENTER);
		b1.setBackgroundDrawable(d1);
		b1.setId(id1);
		l2.addView(b1);	
		
		return l2;
	}	
	
	private LinearLayout breadCrumbs(float height, float width) {
		// First LinearLayout horizontal for BreadCrumbs
		int h = (int) (0.05 * height);
		LinearLayout l1 = new LinearLayout(this);
		l1.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams paramsl1 = new LinearLayout.LayoutParams(
			     ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		paramsl1.setMargins(0, 0, 0, 0);
		paramsl1.height = h;
		l1.setLayoutParams(paramsl1);

		// BreadCrumbs button 1
		int w1 = (int) (0.20 * width);
		int h1 = (int) (0.8 * h);
		int p1 = (int) (0.15 * w1);
		int p1t = (int) (0.10 * h);
		String bText = getResources().getString(R.string.home);
		LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
				w1, LinearLayout.LayoutParams.MATCH_PARENT);
		Button  bBreadCrumbs = new Button(this);
		bBreadCrumbs.setText(bText);
		bBreadCrumbs.setBackgroundColor(getResources().getColor(R.color.BreadCrumbs1));
		params1.setMargins(0,0,0,0); // Center icons for different screen sizes
		bBreadCrumbs.setTextColor(Color.WHITE);
		bBreadCrumbs.setPadding(0, 0, 0, 0);
		bBreadCrumbs.setGravity(Gravity.CENTER | Gravity.CENTER);
		bBreadCrumbs.setLayoutParams(params1);
			// Fit text to button
		bBreadCrumbs.setTextScaleX(scaleButtonText(h1-p1t, w1, p1,bText));
		bBreadCrumbs.setTextSize(TypedValue.COMPLEX_UNIT_PX, h1);
		bBreadCrumbs.setSingleLine(true);
		
		l1.addView(bBreadCrumbs);

		// BreadCrumbs button 2
		Button  bBreadCrumbsEnd = new Button(this);
		bBreadCrumbsEnd.setBackgroundColor(getResources().getColor(R.color.BreadCrumbs2));
		LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT );
		params2.setMargins(0, 0, 0, 0);
		bBreadCrumbsEnd.setLayoutParams(params2);
		
		l1.addView(bBreadCrumbsEnd);
	return l1;
		
	}
	
	private float scaleButtonText (int h, int w, int p, String bText) {
		
		Paint paintRectText = new Paint();
		paintRectText.setTextSize(h); // Units are pixels here
		paintRectText.setTextScaleX(1.0f);
		Rect bounds = new Rect();

		// ask the paint for the bounding rect if it were to draw this text.
		paintRectText.getTextBounds(bText, 0, bText.length(), bounds);

		// determine the width
		int wText = bounds.right - bounds.left;

		// Calculate the new scale in x direction to fit the text to the button width
		float TextScaleX = (float) (w - 2*p) / (float) wText; 

	return TextScaleX;
	}
	
	@Override
	public void onClick(View v) {
		
		SharedPreferences tipoGuardado;
		int position = 20;
		
		//Retrieve type list from resources
		
		switch (v.getId()) {
		case 1: // Special Interest
			position=1;
			break;
		case 2: // Weather and Earth
			position=2;
			break;
		case 3: // Communications
			position=3;
			break;
		case 4: //Navigation
			position=4;
			break;
		case 5: // Scientific
			position=5;
			break;
		case 6: // Miscellaneous
			position=6;
			break;
		case 20: // extra case
			position = 10;
			break;
		case 100: // search Dialog
			
			onSearchRequested();
			break;
		}

		try {
			if (position>=1 && position<7) {
		//     	String elegido = sListaTipoSat[position];
				
		/*		tipoGuardado = getSharedPreferences(nombreArchivo, 0);
				SharedPreferences.Editor editor = tipoGuardado.edit();
				editor.putString("tipoSatGeneral", elegido);
		*/
				// save sat chosen in Preferences 
				// Also an intent.PutExtra can be used to pass data through activities
				int elegido = position;
				tipoGuardado = getSharedPreferences(fileName, 0);
				SharedPreferences.Editor editor = tipoGuardado.edit();
				editor.putInt("tipoSatGeneral", elegido);
				editor.commit();
	
				// Open activity listaTipoSat2
				Intent startApp = new Intent("com.zatdroid.rodrigo.LISTATIPOSAT2");
				startActivity(startApp);
			} else {
				
			}
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
		}

	}
	@Override
	public void onBackPressed() {
		// manage creation of XML in searchActivity
		searchActivity.isXMLCreated=false;
		finish();
	}
}
	




