package com.zatdroid.rodrigo;

import android.util.Log;

public class CalculationsSatTRACK {

	public static final double XKMPER	 = 6.378137E3;				// Earth Radius Km
	public static final double XMNPDA	 = 1.44E3;					// Minutes per day
	public static final double SECDAY	 = 8.6400E4;				// Seconds per day
	public static final double F		 = 3.35281066474748E-3;		// Flattering factor

	CalculationsTime time = new CalculationsTime();
	CalculationsOrbit calculations = new CalculationsOrbit();
	
	public sat satTRACK (double currentTime, sat sat, vector devicePosition) {
	/* calculates the azimuth, elevation of a satellite over a specified ground location.
	INPUTS:
		ctime: Current time in JD
		sat: sat data from TLE.
		deviceLocation: vector ( V1-lat, V2-lon and V3-altitutde) from device location
	OUTPUT:
		sat: sat data completed with azimuth, elevation from device Location. */
	 
		// given device latitude and longitude convert to radians
		vector devicePositionRad = new vector( devicePosition.getV1() * (Math.PI/180),  // latitude north positive south negative
							 				   (-1) * devicePosition.getV2() * (Math.PI/180), // longitude west positive east negative
							 				   devicePosition.getV3()/1000); // altitude in km
		double tsince;
		sat position = new sat();

		//time since epoch in minutes
		position = calculations.prepareTimeData(sat);
		tsince=(currentTime - position.getEpochJD())*1440;

		//orbit calculation by the SGP4/SDP4 implementation (r and rdot and alt)
		position = calculations.orbit(tsince, position);
		
		// Copy values of r sat ECI
		vector r = new vector(position.getR1(), position.getR2(), position.getR3());

		/** calculation of Elevation and Azimuth **/
		// ECI position of the device
		vector ground = null;
		ground = geo2eciobs(devicePositionRad,currentTime);
		// azimuth, elevation and range calculation
		position = azielev(position,r,ground,devicePositionRad,currentTime);
		return position;
	}
		
		public vector geo2eciobs(vector devicePositionRad , double ctime) {
		/* http://celestrak.com/columns/v02n02/
		   INPUTS:
				ctime: Current time in JD
				deviceLocationRAD: vector ( V1-lat, V2-lon and V3-altitutde) from device location in Radians
		   OUTPUT:
		 		vector: observer position in the ECI frame */
			
		//Local Mean Sidereal Time (LMST) = "GMST + longitude"
		double thetaobs = (time.ThetaG_JD(ctime) + devicePositionRad.getV2()) % (2*Math.PI);	
		//c:  Correction factor for Earth Radius in x and y
		double c = 1./ Math.sqrt(1 + F * (F-2) * Math.pow(Math.sin(devicePositionRad.getV1()),2)  );
		double sq = Math.pow((1-F),2) * c; // Correction factor for Earth Radius in z
		double achcp = (XKMPER * c + devicePositionRad.getV3()) * Math.cos(devicePositionRad.getV1());
		vector deviceECI = new vector( achcp * Math.cos(thetaobs),	// kilometers
									 achcp * Math.sin(thetaobs),
									 (XKMPER * sq + devicePositionRad.getV3()) * 
									 	Math.sin(devicePositionRad.getV1()) );
		return deviceECI;
		}
		
		public sat azielev(sat sat, vector r, vector ground, vector devicePositionRad, double ctime) {
		/* http://celestrak.com/columns/v02n02/ 
		INPUTS:
			sat: data of the satellite imported from TLE and calculated afterwards with sgp4/spd4
			vector r: vector object with the position ECI of the sat (just a copy of the sat data)
			vector ground: vector object with the position ECI of the device. It comes from geo2eciobs function
			devicePositionRad: vector ( V1-lat, V2-lon and V3-altitutde) from device location in Radians
			ctime: Current time in JD
   		OUTPUT:
 			sat: sat completed with azi and elevation */
			
			sat sat2 = new sat();
			sat2.copyData(sat);

			// Local Mean Sidereal Time (LMST)
		 	double thetaobs = (time.ThetaG_JD(ctime) + devicePositionRad.getV2()) % (2*Math.PI);	
			// difference between the two points in ECI coordinates
		 	vector slant = r.subtract(ground);
			// range between the points Euclidean length
		 	double range = slant.magnitude();

		 	// faster computation
		 	double sinlat = Math.sin(devicePositionRad.getV1());
		 	double coslat = Math.cos(devicePositionRad.getV1());
		 	double sintheta = Math.sin(thetaobs);
		 	double costheta = Math.cos(thetaobs);

		 	// Transformation matrix 
		 	// Reference: Kosch, H.J., Mathematische Ergaenzung zur Einfuehrung in die Physik
		 	// Binomi, pp.29-ff., 1999.
		 	vector mat = new vector( sinlat * costheta * slant.getV1() + 
		 							 sinlat * sintheta * slant.getV2() - 
		 							 coslat * slant.getV3(),
		 							 (-1) * sintheta * slant.getV1() + 
		 							 costheta * slant.getV2(), 
		 							 coslat * costheta * slant.getV1() +
		 							 coslat * sintheta * slant.getV2() + 
		 							 sinlat * slant.getV3() );
			double elv = Math.asin(mat.getV3() / range );
			double azi = Math.atan(((-1) * mat.getV2()) / mat.getV1() );
			if (mat.getV1() > 0) azi = azi + Math.PI;
			//if (azi < 0)	azi = azi + 2 * Math.PI; // -180 < az < 180 
			sat2.setAz(azi*180/Math.PI);
			sat2.setEle(elv*180/Math.PI);
			Log.d("AZIMUTH",Double.toString(sat2.getAz()));
			Log.d("ELEVATION",Double.toString(sat2.getEle()));
		//	sat2.showLog();
			return sat2;
		 }
}
	
