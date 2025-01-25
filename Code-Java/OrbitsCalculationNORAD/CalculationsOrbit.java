package com.zatdroid.rodrigo;

import android.util.Log;

public class CalculationsOrbit {

	//CONSTANTS used by SGP4/SDP4 imported from Fortran code
	public static final double DEG2RAD	 = 1.745329251994330E-2; 	// degrees to radians
	public static final double TWOPI 	 = 6.28318530717958623;		// 2*PI
	public static final double TOTHRD	 = 6.6666666666666666E-1;	// 2/3
	public static final double XKE		 = 7.43669161E-2;
	public static final double XMNPDA	 = 1.44E3;					// Minutes per day
	public static final double AE		 = 1.0;						
	public static final double CK2		 = 5.413079E-4;
	        	
	/* Prepare time data epochN and epochJD from original sat data taken from xml - TLE.
	INPUTS: to fit future requirements in calculation orbits.
		satOriginal is the data of the satellite from xml
	OUTPUT: it returns:
		sat object with new satEpochN, EpochJD */
	
	public sat prepareTimeData(sat satOriginal) {
		int yr;
		CalculationsTime time = new CalculationsTime();
		
		Double xjdtmp;
		sat satFinal = new sat();
		satFinal.copyData(satOriginal);
		//NORAD specification epochN. satOriginal.EpochN is complete year, dayAndTime of the year in decimals.
		satFinal.setEpochN(1e3 * satOriginal.getEpochYear()  + satOriginal.getEpoch());
		//EpochJD epoch in Julian Date
		if (satOriginal.getEpochYear() < 57) {
			yr = 2000 + satOriginal.getEpochYear(); 
		}
		else yr = 1900 + satOriginal.getEpochYear();
		xjdtmp = time.julian( yr, 1, 0);
		//Julian date of the epoch
		satFinal.setEpochJD(xjdtmp + satOriginal.getEpoch());
		//satFinal.setEpochC(); It should be date in Calendar format. Not calculated
		
		return satFinal;
	}
	
	
	/* Prepare TLE data from original sat data taken from xml - TLE.
	INPUTS: to fit future requirements in calculation orbits and 
			do not call sat object constantly.
		sat is the data of the satellite from xml
	OUTPUT: it modifies the object CalculationsVarGlobal of global variables: 
	 	CalculationsVarGlobal with new preprocessed 
	 	xnodeo, omegao, xmo, xno, xincl, xndt2o, bstar, eo */
	
	public CalculationsVarGlobal prepareTLEData (sat sat) {
		double temp;
		/* preprocess tle set */
		CalculationsVarGlobal var2 = new CalculationsVarGlobal();
		var2.setXnodeo(sat.getRaan()*DEG2RAD);
		var2.setOmegao(sat.getArgumentPerigee()*DEG2RAD);
		var2.setXmo(sat.getMeanAnomaly()*DEG2RAD);
		var2.setXincl(sat.getInclination()*DEG2RAD);
		temp=TWOPI/XMNPDA/XMNPDA;
		var2.setXno(sat.getMeanMotion()*temp*XMNPDA);
		var2.setXndt2o(sat.getMeanMotionDerivate()*temp);
		// 2nd Derivation Mean Motion is not used.
		// var2.setXndd6o(xndd6o*temp/XMNPDA);
		var2.setBstar(sat.getBstarDragTerm()/AE);
		var2.setEo(sat.getEccentricity());
		return var2;
	}
	
	/* ********** orbit(tsince, sat) ******************************************/
	/* choose between SGP4 or SDP4 according to SpaceTrack Report n.6
	 * INPUTS:
	 *	 tsince: time since epoch in minutes
	 *	 sat is the data of the satellite from xml
	 * OUTPUT: it returns a sat object name r_rdot
	 *	 sat containing position r and velocity rdot */
	
	public sat orbit(double tsince, sat sat) {
		sat r_rdot = new sat();
		
		//Prepare Data is executed in CalcultionsSatPOS to get tsince
		CalculationsVarGlobal var = new CalculationsVarGlobal();
		var = prepareTLEData(sat);
//		var.showLog();
		
	    if (select_ephemeris(sat, var)==1) {
			CalculationsOrbitSDP4 SDP4 = new CalculationsOrbitSDP4();
			r_rdot= SDP4.getOrbit(tsince, sat, var);
			SDP4.resetStaticVars(); // Reset static variables for next calculation
	    } else {
			CalculationsOrbitSGP4 SGP4 = new CalculationsOrbitSGP4();
			r_rdot= SGP4.getOrbit(tsince, sat, var);
			SGP4.resetStaticVars(); // Reset static variables for next calculation
		}
		return r_rdot;
	}

	/* ***********select_ephemeris(sat sat) ******************************************/
	/* choose between SGP4 or SDP4 according to SpaceTrack Report n.6
	 * Selects the appropriate ephemeris type to be used for predictions
	 * according to the data in the TLE (formula page ----------------),
	 * it also processes values in the tle set so that they are appropriate
	 * for the SGP4/SDP4 routines
	 * INPUTS: 
	 		sat is the data of the satellite from xml 
	 * OUPTUT: it returns an int:
		  0 - SGP4
		  1 - Deep SDP4 */

	public int select_ephemeris(sat sat, CalculationsVarGlobal var) {
		double ao, xnodp, dd1, dd2, delo, temp, a1, del1, r1;
		
		/* period > 225 minutes is deep space 
		  And Checking formula PAGE *******************
		  ********************* Completar f—rmula ************************* */
		dd1=(XKE/var.getXno());
		dd2=TOTHRD;
		a1=Math.pow(dd1,dd2);
		r1=Math.cos(var.getXincl());
		dd1=(1.0-var.getEo()*var.getEo());
		temp=CK2*1.5f*(r1*r1*3.0-1.0)*Math.pow(dd1,-1.5);
		del1=temp/(a1*a1);
		ao=a1*(1.0-del1*(TOTHRD*.5+del1*(del1*1.654320987654321+1.0)));
		delo=temp/(ao*ao);
		xnodp=var.getXno()/(delo+1.0);
		
		/* select a deep-space/near-earth ephemeris */
		if (TWOPI/xnodp/XMNPDA >= 0.15625) return 1; //Deep Space SDP4
		else return 0; // SGP4
	}
	
}
	