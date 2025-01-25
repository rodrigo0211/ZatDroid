package com.zatdroid.rodrigo;

import android.util.Log;

public class CalculationsSatPOS {

	public static final double XKMPER	 = 6.378137E3;				// Earth Radius Km
	public static final double XMNPDA	 = 1.44E3;					// Minutes per day
	public static final double SECDAY	 = 8.6400E4;				// Seconds per day
	public static final double F		 = 3.35281066474748E-3;		// Flattering factor	
	
	CalculationsTime time = new CalculationsTime();
	CalculationsOrbit calculations = new CalculationsOrbit();
	
	public sat satPOS(double currentTime, sat sat) {
	//SATPOS calculates the footprint of a satellite at a specified time in UT 
	//SATPOS(CurrentTime,satElegido) where
			//CurrentTime is the current time in format Julian Date.
			//Sat is the data of the satellite from xml
	// it returns a sat object with:
			// the position r
			// the velocity rdot
			// latitude and longitude subsatellite point and altitude of the sat
		
		double tsince;
		sat position = new sat();
		
		//time since epoch in minutes
		position = calculations.prepareTimeData(sat);
		tsince=(currentTime - position.getEpochJD())*1440;
		//orbit calculation by the SGP4/SDP4 implementation
		position = calculations.orbit(tsince, position);

		//latitude and longitude and altitude calculation
		position=latlon(position,currentTime);

		return position;
	}

	public sat latlon(sat sat, double currentTime) {
		
	//calculates the geodetic position of an object given:
	//Inputs: 
		//sat: its ECI position calculated and all info about sat
		//currenTime in JulianDate.
	//Outputs:
		//sat with info of latitude (N) and longitude (W)
	//Reference: The 1992 Astronomical Almanac, page K12.
		
		double theta, lat, lon, r, e2, phi, c;
		theta = Math.atan2(sat.getR2(),sat.getR1());// radians
		
		sat sat2 = new sat();
		sat2.copyData(sat);
		
		lon = (theta-time.ThetaG_JD(currentTime)) % (2*Math.PI); //radians % is modulus function
		r=Math.sqrt(Math.pow(sat.getR1(),2) + Math.pow(sat.getR2(),2));
		e2=F*(2-F);
		lat=Math.atan2(sat.getR3(),r);
		
		//initial step
		phi=lat;
		c=1/Math.sqrt(1-e2*(Math.sin(phi)*Math.sin(phi)));
		lat=Math.atan2(sat.getR3()+Math.sin(phi),r);
		
		while (Math.abs(lat-phi)>=1e-10) {
			phi=lat;
			c=1/Math.sqrt(1-e2*(Math.sin(phi)*Math.sin(phi)));
			lat=Math.atan2(sat.getR3()+XKMPER*c*e2*Math.sin(phi),r);
		}
		
		if (lat>(Math.PI/2)) lat=lat-2*Math.PI;
		
		// lon (-PI,PI)
		if (Math.abs(lon)>(Math.PI)) {
			if (lon<0) lon = 2*Math.PI + lon; 
			else lon = 2*Math.PI - lon;
		}
		
		// Save lat - lon in degrees
		sat2.setLat(lat*180/Math.PI);
		sat2.setLon(lon*180/Math.PI);
		
		/** Altitude
		sat2.setAlt(Math.sqrt(Math.pow(sat2.getR1(), 2) + Math.pow(sat2.getR2(), 2) +
				Math.pow(sat2.getR3(), 2) ) - XKMPER); 
		 This method gives 10Km error  **/ 
		/* http://celestrak.com/columns/v02n03/ */
		sat2.setAlt(r/Math.cos(lat) - XKMPER*c); /*kilometers*/
		
	return  sat2;
	}
	
}

