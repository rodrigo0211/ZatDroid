package com.zatdroid.rodrigo;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
//Parcelable to pass the variable between classes
public class sat implements Parcelable {
	private String name;
	private String number;
	private double inclination;
	private double raan;
	private double eccentricity;
	private double argumentPerigee;
	private double meanAnomaly;
	private double meanMotion;
	// epochN = concatenate(epochYear;epoch)
	private int epochYear; //Year NORAD
	private double epoch; //Day-minutes NORAD (without year)
	private double epochC; //Eliminarrr NO SE USA À?À?À?À??À?À Date in normal format
	private double epochN; //Year+DayMinutes in NORAD
	private double epochJD; //Epoch in Julian Date
	private double meanMotionDerivate;
	private double bstarDragTerm;
	private double ephemeridesType;
	private int encontrado;
	
	//Data calculated during process
	private double r1;
	private double r2;
	private double r3;
	private double rdot1;
	private double rdot2;
	private double rdot3;
	private double lat;
	private double lon;
	private double az;
	private double ele;
	private double alt;
	private double range;

	// Constructor All values initializes to 0
	public sat() {
		this.name = "";
		this.number = "";
		this.inclination = 0;
		this.raan = 0;
		this.eccentricity = 0;
		this.argumentPerigee = 0;
		this.meanAnomaly = 0;
		this.meanMotion = 0;
		this.epochYear = 0;
		this.epoch = 0;
		this.epochC = 0;
		this.epochN = 0;
		this.epochJD = 0;
		this.meanMotionDerivate = 0;
		this.bstarDragTerm = 0;
		this.ephemeridesType = 0;
		this.encontrado = 0;
		
		this.r1 = 0;
		this.r2 = 0;
		this.r3 = 0;
		this.rdot1 = 0;
		this.rdot2 = 0;
		this.rdot3 = 0;
		this.lat = 0;
		this.lon = 0;
		this.az = 0;
		this.ele = 0;
		this.alt = 0;
		this.range = 0;
	}
	
	public sat(String name, String number, double inclination, double raan,
			double eccentricity, double argumentPerigee, double meanAnomaly, double meanMotion,
			int epochYear,double epoch, double epochC, double epochN, double epochJD, 
			double meanMotionDerivate, double bstarDragTerm,
			double ephemeridesType, int encontrado,
			double r1, double r2, double r3, double rdot1, double rdot2, double rdot3,
			double lat, double lon, double az, double ele,double alt, double range) {

		this.name = name;
		this.number = number;
		this.inclination = inclination;
		this.raan = raan;
		this.eccentricity = eccentricity;
		this.argumentPerigee = argumentPerigee;
		this.meanAnomaly = meanAnomaly;
		this.meanMotion = meanMotion;
		this.epochYear = epochYear;
		this.epoch = epoch;
		this.epochC = epochC;
		this.epochN = epochN;
		this.epochJD = epochJD;
		this.meanMotionDerivate = meanMotionDerivate;
		this.bstarDragTerm = bstarDragTerm;
		this.ephemeridesType = ephemeridesType;
		this.encontrado = encontrado;
		
		this.r1 = r1;
		this.r2 = r2;
		this.r3 = r3;
		this.rdot1 = rdot1;
		this.rdot2 = rdot2;
		this.rdot3 = rdot3;
		this.lat = lat;
		this.lon = lon;
		this.az = az;
		this.ele = ele;
		this.alt = alt;
		this.range = range;
	}

	// Getter and setter methods
   public String getName() {
	   return name;
   }
   public String getNumber() {
	   return number;
   }   
   public double getInclination() {
	   return inclination;
   }
   public double getRaan() {
	   return raan;
   }
   public double getEccentricity() {
	   return eccentricity;
   }
   public double getArgumentPerigee() {
	   return argumentPerigee;
   }
   public double getMeanAnomaly() {
	   return meanAnomaly;
   }
   public double getMeanMotion() {
	   return meanMotion;
   }
   public int getEpochYear() {
	   return epochYear;
   }
   public double getEpoch() {
	   return epoch;
   }
   public double getEpochC() {
	   return epochC;
   }
   public double getEpochN() {
		   return epochN;
   }
   public double getEpochJD() {
	   return epochJD;
   }
   public double getMeanMotionDerivate() {
	   return meanMotionDerivate;
   }
   public double getBstarDragTerm() {
	   return bstarDragTerm;
   }
   public double getEphemeridesType() {
	   return ephemeridesType;
   }
   public int getEncontrado() {
	   return encontrado;
   }
   
   /* ****************** */
   public double getR1() {
	   return r1;
   }
   public double getR2() {
	   return r2;
   }   
   public double getR3() {
	   return r3;
   }
   public double getRdot1() {
	   return rdot1;
   }
   public double getRdot2() {
	   return rdot2;
   }
   public double getRdot3() {
	   return rdot3;
   }
   public double getLat() {
	   return lat;
   }
   public double getLon() {
	   return lon;
   }
   public double getAz() {
	   return az;
   }
   public double getEle() {
	   return ele;
   }
   public double getAlt() {
	   return alt;
   }
   public double getRange() {
	   return range;
   }
   
   //////////////////////////////////////
   
   public void setName(String name) {
	   this.name = name;
   }
   public void setNumber(String number) {
	  this.number = number;
   }   
   public void setInclination(double inclination) {
	   this.inclination = inclination;
   }
   public void setRaan(double raan) {
	   this.raan =  raan;
   }
   public void setEccentricity(double eccentricity) {
	   this.eccentricity = eccentricity;
   }
   public void setArgumentPerigee(double argumentPerigee) {
	   this.argumentPerigee = argumentPerigee;
   }
   public void setMeanAnomaly(double meanAnomaly) {
	   this.meanAnomaly = meanAnomaly;
   }
   public void setMeanMotion(double meanMotion) {
	   this.meanMotion = meanMotion;
   }
   public void setEpochYear(int epochYear) {
	   this.epochYear = epochYear;
   }
   public void setEpoch(double epoch) {
	   this.epoch = epoch;
   }
   public void setEpochC(double epochC) {
	   this.epochC = epochC;
   }
   public void setEpochN(double epochN) {
	   this.epochN = epochN;
   }
   public void setEpochJD(double epochJD) {
	   this.epochJD = epochJD;
   }
   public void setMeanMotionDerivate(double meanMotionDerivate) {
	   this.meanMotionDerivate = meanMotionDerivate;
   }
   public void setBstarDragTerm(double bstarDragTerm) {
	   this.bstarDragTerm = bstarDragTerm;
   }
   public void setEphemeridesType(double ephemeridesType) {
	   this.ephemeridesType = ephemeridesType;
   }
   public void setEncontrado(int encontrado) {
	   this.encontrado = encontrado;
   }  
   
   /* ******************* */
   public void setR1(double r1) {
	   this.r1 = r1;
   }
   public void setR2(double r2) {
	   this.r2 =  r2;
   }
   public void setR3(double r3) {
	   this.r3 = r3;
   }
   public void setRdot1(double rdot1) {
	   this.rdot1 = rdot1;
   }
   public void setRdot2(double rdot2) {
	   this.rdot2 = rdot2;
   }
   public void setRdot3(double rdot3) {
	   this.rdot3 = rdot3;
   }
   public void setLat(double lat) {
	   this.lat = lat;
   }
   public void setLon(double lon) {
	   this.lon = lon;
   }
   public void setAz(double az) {
	   this.az = az;
   }
   public void setEle(double ele) {
	   this.ele = ele;
   }
   public void setAlt(double alt) {
	   this.alt = alt;
   }
   public void setRangen(double range) {
	   this.range = range;
   }
   
   ////////////////////////////////////////////////////////////////
	// Parcelling part
	public sat(Parcel in) {
		String[] data = new String[29];

		in.readStringArray(data);
		this.name = data[0];
		this.number = data[1];
		this.inclination = Double.parseDouble(data[2]);
		this.raan = Double.parseDouble(data[3]);
		this.eccentricity = Double.parseDouble(data[4]);
		this.argumentPerigee = Double.parseDouble(data[5]);
		this.meanAnomaly = Double.parseDouble(data[6]);
		this.meanMotion = Double.parseDouble(data[7]);
		this.epochYear = Integer.parseInt(data[8]);
		this.epoch = Double.parseDouble(data[9]);
		this.epochC = Double.parseDouble(data[10]);
		this.epochN = Double.parseDouble(data[11]);
		this.epochJD = Double.parseDouble(data[12]);
		this.meanMotionDerivate = Double.parseDouble(data[13]);
		this.bstarDragTerm = Double.parseDouble(data[14]);
		this.ephemeridesType = Double.parseDouble(data[15]);
		this.encontrado = Integer.parseInt(data[16]);
		
		this.r1 = Double.parseDouble(data[17]);
		this.r2 = Double.parseDouble(data[18]);
		this.r3 = Double.parseDouble(data[19]);
		this.rdot1 = Double.parseDouble(data[20]);
		this.rdot2 = Double.parseDouble(data[21]);
		this.rdot3 = Double.parseDouble(data[22]);
		this.lat = Double.parseDouble(data[23]);
		this.lon = Double.parseDouble(data[24]);
		this.az = Double.parseDouble(data[25]);
		this.ele = Double.parseDouble(data[26]);
		this.alt = Double.parseDouble(data[27]);		
		this.range = Double.parseDouble(data[28]);
		
	}
   
	// Copy the data from another object to this sat object
	public void copyData(sat s) {
		this.name = s.name;
		this.number = s.number;
		this.inclination = s.inclination;
		this.raan = s.raan;
		this.eccentricity = s.eccentricity;
		this.argumentPerigee = s.argumentPerigee;
		this.meanAnomaly = s.meanAnomaly;
		this.meanMotion = s.meanMotion;
		this.epochYear = s.epochYear;
		this.epoch = s.epoch;
		this.epochC = s.epochC;
		this.epochN = s.epochN;
		this.epochJD = s.epochJD;
		this.meanMotionDerivate = s.meanMotionDerivate;
		this.bstarDragTerm = s.bstarDragTerm;
		this.ephemeridesType = s.ephemeridesType;
		this.encontrado = s.encontrado;
		
		this.r1 = s.r1;
		this.r2 = s.r2;
		this.r3 = s.r3;
		this.rdot1 = s.rdot1;
		this.rdot2 = s.rdot2;
		this.rdot3 = s.rdot3;
		this.lat = s.lat;
		this.lon = s.lon;
		this.az = s.az;
		this.ele = s.ele;
		this.alt = s.alt;
		this.range = range;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	public void showLog() {
		Log.d("name",this.name);
		Log.d("number",this.number);
		Log.d("inclination",String.valueOf(this.inclination));
		Log.d("raan",String.valueOf(this.raan));
		Log.d("eccentricity",String.valueOf(this.eccentricity));
		Log.d("argumentPerigee",String.valueOf(this.argumentPerigee));
		Log.d("meanAnomaly",String.valueOf(this.meanAnomaly));
		Log.d("meanMotion",String.valueOf(this.meanMotion));
		Log.d("epochYear",String.valueOf(this.epochYear));
		Log.d("epoch",String.valueOf(this.epoch));
		Log.d("epochC",String.valueOf(this.epochC));
		Log.d("epochN",String.valueOf(this.epochN));
		Log.d("epochJD",String.valueOf(this.epochJD));
		Log.d("meanMotionDerivate",String.valueOf(this.meanMotionDerivate));
		Log.d("bstarDragTerm",String.valueOf(this.bstarDragTerm));
		Log.d("ephemeridesType",String.valueOf(this.ephemeridesType));
		Log.d("encontrado",String.valueOf(this.encontrado));
		
		Log.d("r1",String.valueOf(this.r1));
		Log.d("r2",String.valueOf(this.r2));
		Log.d("r3",String.valueOf(this.r3));
		Log.d("rdot1",String.valueOf(this.rdot1));
		Log.d("rdot2",String.valueOf(this.rdot2));
		Log.d("rdot3",String.valueOf(this.rdot3));
		Log.d("lat",String.valueOf(this.lat));
		Log.d("lon",String.valueOf(this.lon));
		Log.d("az",String.valueOf(this.az));
		Log.d("ele",String.valueOf(this.ele));
		Log.d("alt",String.valueOf(this.alt));
		Log.d("range",String.valueOf(this.range));
		
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[] { this.name, this.number, String.valueOf(inclination), String.valueOf(raan),
				String.valueOf(eccentricity), String.valueOf(argumentPerigee), String.valueOf(meanAnomaly),
				String.valueOf(meanMotion),String.valueOf(epochYear),String.valueOf(epoch),
				String.valueOf(epochC),String.valueOf(epochN),	String.valueOf(epochJD), String.valueOf(meanMotionDerivate), String.valueOf(bstarDragTerm),
				String.valueOf(ephemeridesType),  String.valueOf(encontrado),
				String.valueOf(r1), String.valueOf(r2),
				String.valueOf(r3), String.valueOf(rdot1), String.valueOf(rdot2),
				String.valueOf(rdot3),String.valueOf(lat), String.valueOf(lon), 
				String.valueOf(az), String.valueOf(ele),String.valueOf(alt), String.valueOf(range)});
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public sat createFromParcel(Parcel in) {
			return new sat(in);
		}

		public sat[] newArray(int size) {
			return new sat[size];
		}
	};
}
