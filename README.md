# ZatDroid <img src="https://github.com/user-attachments/assets/40d08be3-87d9-4dd8-8021-89bcc9170f29" alt="image description" width="100" />
Satellite Tracking and Augmented Reality. App for Android


![_logo_3_512x512](https://github.com/user-attachments/assets/a7d552d3-8808-46aa-bfb7-c29695c1e80b)


![image](https://github.com/user-attachments/assets/40d08be3-87d9-4dd8-8021-89bcc9170f29)



The purpose of ZATDROID is to offer the user the possibility of tracking any artificial satellite. It locates it in Google Maps together with its predicted trajectory and also it allows the user to see it in real time in the sky with augmented reality camera view. The application is implemented in Java for ANDROID devices (tablets or smartphones). 

Orbital mechanics calculations are developed following Newton equations and NORAD (North American Aerospace Defence Command) propagation models, firstly published in 1980 Spacetrack Report #3: Models for Propagation of the NORAD Element Sets
ZATDROID:
- downloads information from a data base of satellites provided by CELESTRAK.COM
- does the orbital mechanics calculations (NORAD models)
- gets the device sensors magnitudes
- connects to the web service Google Maps Elevation to get the altitude of the user location
- processes data in terms of XML language
- creates a GOOGLE MAPS views with the updated position in real time of the satellite picked
- shows the position in the sky in augmented reality using OPENGL for the camera view.


The GUI (Graphical User Interface) manages to lead the users through an easy and friendly navigation to achieve their aims quickly, showing the results of the user picks with BreadCrumbs, supporting English and Spanish, showing icon-based menus and keeping the users informed.

