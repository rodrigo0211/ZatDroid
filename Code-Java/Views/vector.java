package com.zatdroid.rodrigo;

public class vector {
	/*
	  Copyright 2008 - 2010
	 
	  Licensed under the Apache License, Version 2.0 (the "License");
	  you may not use this file except in compliance with the License.
	  You may obtain a copy of the License at
	 
	  http://www.apache.org/licenses/LICENSE-2.0
	 
	  Unless required by applicable law or agreed to in writing, software
	  distributed under the License is distributed on an "AS IS" BASIS,
	  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
	  either express or implied. See the License for the specific language
	  governing permissions and limitations under the License.
	 
	  project loonframework
	  author chenpeng  
	  email：ceponline@yahoo.com.cn 
	  version 0.1
	 */
    private double v1, v2, v3;

    public vector(double v1, double v2, double v3) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
    }

    public vector(vector v) {
            this.v1 = v.v1;
            this.v2 = v.v2;
            this.v3 = v.v3;
    }

    public void setV1(double v1) {
            this.v1 = v1;
    }

    public void setV2(double v2) {
            this.v2 = v2;
    }

    public void setV3(double v3) {
            this.v3 = v3;
    }

    public double getV1() {
            return v1;
    }

    public double getV2() {
            return v2;
    }

    public double getV3() {
            return v3;
    }

    public int v1() {
            return (int) v1;
    }

    public int v2() {
            return (int) v2;
    }

    public int v3() {
            return (int) v3;
    }

    public Object clone() {
            return new vector(v1, v2, v3);
    }

    public double[] getCoords() {
            return (new double[] { v1, v2,v3 });
    }

    public boolean equals(Object o) {
            if (o instanceof vector) {
                    vector p = (vector) o;
                    return p.v1 == v1 && p.v2 == v2 && p.v3 == v3;
            }
            return false;
    }

    public vector add(vector other) {
            double v1 = this.v1 + other.v1;
            double v2 = this.v2 + other.v2;
            double v3 = this.v3 + other.v3;
            return new vector(v1, v2, v3);
    }

    public vector subtract(vector other) {
            double v1 = this.v1 - other.v1;
            double v2 = this.v2 - other.v2;
            double v3 = this.v3 - other.v3;
            return new vector(v1, v2, v3);
    }

    public vector multiply(double value) {
            return new vector(value * v1, value * v2, value * v3);
    }

    public vector crossProduct(vector other) {
            double v1 = this.v2 * other.v3 - other.v2 * this.v3;
            double v2 = this.v3 * other.v1 - other.v3 * this.v1;
            double v3 = this.v1 * other.v2 - other.v1 * this.v2;
            return new vector(v1, v2, v3);
    }
    
    public double dotProduct(vector other) {
            return other.v1 * v1 + other.v2 * v2 + other.v3 * v3;
    }
    
    public double magnitude() {
        double m = Math.sqrt(dotProduct(this));
    	return m; 
    }
    
    public vector normalize() {
            return new vector(v1 / this.magnitude(), v2 / this.magnitude(), v3 / this.magnitude());
    }
    public double angle(vector other) {
        double a = Math.acos((this.dotProduct(other))/(this.magnitude()*other.magnitude()));
    	return a;
}
    public String toString() {
            return (new StringBuffer("[vector v1:")).append(v1).append(" v2:")
                            .append(v2).append(" v3:").append(v3).append("]").toString();
    }

}

