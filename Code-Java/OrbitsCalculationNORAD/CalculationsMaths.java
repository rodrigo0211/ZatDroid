package com.zatdroid.rodrigo;

import android.util.Log;

public class CalculationsMaths {

	public static final double TWOPI 	 = 6.28318530717958623;		// 2*PI
	
	/* ***************************************************************************************** */
	/* ********************** Math functions for SGP4/SDP4   ***************************************	*/
	/* ***************************************************************************************** */

	/* returns angles 0 - 2PI of argument from any number (delete 2PI rounds)	OK
	 * Same as FixAngle or % (2 * Math.PI)*/
	public double FMod2p(double x) {
		int i;
		double ret_val;
		ret_val = x;
		i = (int)(ret_val / TWOPI); //// CHECK QUE FUNCIONA
		ret_val -= i*TWOPI;
		if (ret_val<0.0) ret_val+=TWOPI;
		return ret_val;
	}
	/* reduces angles greater than two pi by subtracting two pi from the angle	OK
	 * Same as FMod2PI or % (2 * Math.PI)*/
	public double FixAngle(double x) {
		while (x>TWOPI) {
			x-=TWOPI;
		}
		return x;
	}
	
	/* returns arg1 mod arg2	OK
	 * SAME AS % Java OPERATOR */
	/* ****************************** */
	/* public double Modulus(double arg1, double arg2) {
		int i;
		double ret_val;
		ret_val = arg1;
		i = (int) (ret_val/arg2) ;
		ret_val -= i*arg2;
		if (ret_val<0.0) ret_val+=arg2;
	return ret_val;
	}
	*/
	
	/* returns fractional part of double argument 34.567 -> 0.567 OK*/
	public double Frac(double arg) {
		return(arg-Math.floor(arg)); 
	}
	
	/* returns argument rounded up to nearest integer (Round up or down) OK */
	public int Round(double arg) {
		return((int) Math.floor(arg+0.5));
	}
	
	/* returns the floor integer of a double argument, as double (always integer part)	OK */
	public double Int(double arg) {
		return(Math.floor(arg));
	}
	
	public int test() { 
		//Both are the same
		double m = FMod2p(18);
		Log.d("MOD2PI", Double.toString(m));
		double n = FixAngle(18);
		Log.d("FixAngle", Double.toString(n));
		double mm = (18) % (2 * Math.PI);
		Log.d("% 2PI", Double.toString(mm));
		
	//	double o = orbit.Modulus(234,34);
	//	Log.d("MODULUS", Double.toString(o));
		
		double s = 234 % 34;
		Log.d("MODULUS - %", Double.toString(s));
		
		double p = Frac(234.34);
		Log.d("Frac", Double.toString(p));
		
		int q = Round(234.6544);
		Log.d("Round", Integer.toString(q));
		
		double r = Int(234.6544);
		Log.d("Int", Double.toString(r));
		
		return 1;
	}
	
}
