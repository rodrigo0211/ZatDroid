package com.zatdroid.rodrigo;

import java.util.Calendar;

import android.util.Log;

public class CalculationsTime {

	public static final double TWOPI 	 = 6.28318530717958623;		// 2*PI
	public static final double XKMPER	 = 6.378137E3;				// Earth Radius Km
	public static final double SECDAY	 = 8.6400E4;				// Seconds per day
	public static final double OMEGA_E	 = 1.00273790934;			// Earth rot./sid. day
	public static final double SR		 = 6.96000E5;				// Solar Radius
	
	CalculationsMaths MathFunctions = new CalculationsMaths();
	
	/* ************************************************************* */
	/* ******************* TIME CALCULATIONS *********************** */
	/* ************************************************************* */
	
	/* ********* julian(month, day, year) **********OK********************************/
	// Calculate the julian date knowing the date in conventional format 
		//day, Month, year (2,3,1998)
	// it returns:
		//double with the date in JD
	public double julian(int year, int month, int day) {
		int y = year; int m = month; int b = 0; double c = 0;
		double jd,jdate;int a;
		if (m <= 2) {
		y = y - 1;
		m = m + 12;
		}
		if (y < 0) {
			c = -.75;
		}
		// check for valid calendar date
		if (year < 1582) {
		}
		else if (year > 1582) {
				a = (int)(y / 100);
				b = 2 - a + (int)(a / 4);
			} else if (month < 10) {
			} else if (month > 10) {
						a = (int)(y / 100);
						b = 2 - a + (int)(a / 4);
				} else if (day <= 4) {
				} else if (day > 14) {
					a = (int)(y / 100);	
					b = 2 - a + (int)(a / 4);
				}
					else {
						//msgbox(’this is an invalid calendar date’,’info’,’warn’);
						return 0;
					}
		//calculate the Julian date
		
		jd = (int)(365.25 * y + c) + (int)(30.6001 * (m + 1));
		jdate = jd + day + b + 1720994.5;
		return jdate;
	}

	/* calculates the Julian Date of YEAR  OK */
	public double Julian_Date_of_Year(double year) {
		/* Astronomical Formulae for Calculators, Jean Meeus, */
		/* pages 23-25. Calculate Julian Date of 0.0 Jan year */
		long A, B, i; double jdoy;
		year=year-1;
		i=(int) (year/100);
		A=i; 
		i=A/4;
		B=2-A+i;
		i=(int)(365.25*year);
		i+=30.6001*14;
		jdoy=i+1720994.5+B;
		return jdoy;
	}
	
	/* returns the Julian Date of an epoch specified in the format used in
	   the NORAD two-line element sets. epoch is the NORAD without Year  OK 	*/
	public double Julian_Date_of_Epoch(double epoch, int epochYear) {
		double year, day;
		year = epochYear;
		/* modification to support Y2K */ /* valid 1957 through 2056	*/
		day=epoch;
		if (year<57) year=year+2000;
			else year=year+1900;
		return (Julian_Date_of_Year(year)+day);
	}

	/* calculates the day of the year for the specified date.
	   The calculation uses the rules for the Gregorian calendar	OK */
	public int DOY ( int yr, int mo, int dy) {
		int days[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		int i, day;
		day=0;
		for (i=0; i<mo-1; i++ ) day+=days[i];
		day=day+dy;
		/* Leap year correction */
		if ((yr%4==0) && ((yr%100!=0) || (yr%400==0)) && (mo>2)) day++;
		return day;
	}
	
	/* calculates the fraction of a day passed at the specified input time OK	*/
	public double Fraction_of_Day(int hr, int mi, double se) {
		double dhr, dmi;
		dhr=(double)hr;
		dmi=(double)mi;
		return ((dhr+(dmi+se/60.0)/60.0)/24.0);
	}
	
	/* converts a calendar type to a Julian Date as a double OK */
	public double Julian_Date(Calendar date) {
		double julian_date;
		julian_date = Julian_Date_of_Year(date.get(Calendar.YEAR)) +
				      DOY(date.get(Calendar.YEAR),date.get(Calendar.MONTH) + 1,date.get(Calendar.DAY_OF_MONTH)) +  // Month start in 0
				      Fraction_of_Day(date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE),date.get(Calendar.SECOND)) +
				      5.787037e-06; /* Round up to nearest 1 sec */
		return julian_date;
	}

	/* converts a Julian Date to a calendar java type	*/
	public Calendar GetCalendarFromJD(double jd){
		double mo, da, yr, hh, mm, ss;	
		double z, f, a, b, c, d, e, da2;
		Calendar cal = Calendar.getInstance();
		jd += .5;
		z = Math.floor(jd);
		f = jd - z;
		if (z < 2299161) { /* Julian calendar */
			a = z;
		}
		else { /* Gregorian */
			b = Math.floor((z - 1867216.25) / 36524.25);
			a = z + 1 + b - Math.floor(b / 4);
		}
		b = a + 1524;
		c = Math.floor((b - 122.1) / 365.25);
		d = Math.floor(365.25 * c);
		e = Math.floor((b - d) / 30.6001);
		mo = (e < 14) ? e - 1 : e - 13;
		da = b - d - Math.floor(30.6001 * e) + f;
		yr = (mo < 3) ? c - 4715 : c - 4716;
		/* now get h,m,s */
		a = da;
		da2 = Math.floor(da);
		hh = 24 * (a - da2);
		a = hh;
		hh = Math.floor(hh);
		mm = (60 * (a - hh));
		a = mm;
		mm = Math.floor(mm);
		ss = 60 * (a - mm);
		ss = Math.floor(ss);
		// setting calendar type
		cal.set(Calendar.YEAR,(int)yr);
		cal.set(Calendar.MONTH, (int)mo);
		cal.set(Calendar.DAY_OF_MONTH, (int)da);
		cal.set(Calendar.HOUR_OF_DAY, (int)hh);
		cal.set(Calendar.MINUTE, (int)mm);
		cal.set(Calendar.SECOND, (int)ss);
	return (cal);
}
	
	/* The function ThetaG_JD calculates the Greenwich Mean Sidereal Time for an */
	/* epoch specified in the JD	*/
	/* Reference: The 1992 Astronomical Almanac, page B6.	*/
	public double ThetaG_JD(double jd) {
		double UT, TU, GMST;
		UT=MathFunctions.Frac(jd+0.5);
		jd=jd-UT;

		TU=(jd-2451545.0)/36525;
		GMST=24110.54841+TU*(8640184.812866+TU*(0.093104-TU*6.2E-6));
		GMST=(GMST+SECDAY*OMEGA_E*UT) % (SECDAY);
		return (TWOPI*GMST/SECDAY); //Convert to radians
	}
	
	/* The function ThetaG calculates the Greenwich Mean Sidereal Time for an */
	/* epoch specified in the format used in the NORAD two-line element sets.	*/
	/* Reference: The 1992 Astronomical Almanac, page B6.	*/
	public double ThetaG(double epoch, double epochYear) {
		double year, day, UT, jd, TU, GMST, ThetaG, ds50;
		/* Modification to support Y2K */ /* Valid 1957 through 2056	*/
		year = epochYear;
		day = epoch;
		if (year<57) year+=2000;
		else year+=1900;
		UT= MathFunctions.Frac(day);
		day = MathFunctions.Int(day);
		jd=Julian_Date_of_Year(year)+day;
		TU=(jd-2451545.0)/36525;
		GMST=24110.54841+TU*(8640184.812866+TU*(0.093104-TU*6.2E-6));
		GMST=(GMST+SECDAY*OMEGA_E*UT) % (SECDAY);
		ThetaG=TWOPI*GMST/SECDAY;
		ds50=jd-2433281.5+UT;
		ThetaG=(6.3003880987*ds50+1.72944494) % (2 * Math.PI);
		return ThetaG;
	}

	/* calculates satellite’s eclipse status and depth	*/ 
	public int Sat_Eclipsed(vector pos, vector sol, double depth) {
		double sd_sun, sd_earth, delta;
		vector Rho, earth;
		
		/* determine partial eclipse */
		sd_earth = Math.asin(XKMPER/pos.magnitude());
		Rho = sol.subtract(pos);
		sd_sun=Math.asin(SR/Rho.magnitude());
		earth = pos.multiply(-1);
		delta=sol.angle(earth);
		depth=sd_earth-sd_sun-delta;
		if (sd_earth<sd_sun) return 0;
			else if (depth>=0) return 1;
				else return 0;
		} 
	
	public int test() {
		
		double d = julian(2012,10,23);
		Log.d("FECHA", Double.toString(d));
		
		double a = Julian_Date_of_Year(1985);
		Log.d("Julian_Date_of_YEAR", Double.toString(a));
		
		double b = DOY(17,03,2013);
		Log.d("DOY", Double.toString(b));
		
		double c = Fraction_of_Day(20,35,25);
		Log.d("Fraction_of_Day", Double.toString(c));
		
		Calendar cal = Calendar.getInstance();
		cal.set(2013,03,17,16,13,59);
		double e = Julian_Date(cal);
		Log.d("Julian_Date", Double.toString(e));
		
		double f = Julian_Date_of_Epoch(275.98708465,80);
		Log.d("Julian_Date_of_Epoch", Double.toString(f));

		Calendar g = GetCalendarFromJD(e);
		Log.d("GetCalendarFromJD", g.toString());
		
		Calendar cal2 = Calendar.getInstance();
		cal2.set(2013,03,17,16,13,59);
		double e2 = Julian_Date(cal2);
		double f2 = ThetaG_JD(e2);
		Log.d("ThetaG_JD", Double.toString(f2));

		double h = ThetaG(076.67638297,13);
		Log.d("ThetaG", Double.toString(h));
		
		return 1;
	}
}
