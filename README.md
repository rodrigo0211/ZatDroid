# <img src="https://github.com/user-attachments/assets/a7d552d3-8808-46aa-bfb7-c29695c1e80b" alt="image description" width="100" /> ZatDroid
Satellite Tracking and Augmented Reality. App for Android

<img src="https://github.com/user-attachments/assets/8e685378-6e9f-472a-ab06-3f34e0cad31b" alt="image description" width="400" /> <img src="https://github.com/user-attachments/assets/0ad7f3c2-a669-46ec-91fe-e281754712f9" alt="image description" width="400" />




The purpose of ZATDROID is to offer the user the possibility of tracking any artificial satellite. It locates it in Google Maps together with its predicted trajectory and also it allows the user to see it in real time in the sky with augmented reality camera view. The application is implemented in Java for ANDROID devices (tablets or smartphones). 

Orbital mechanics calculations are developed following Newton equations and NORAD (North American Aerospace Defence Command) propagation models, firstly published in 1980 Spacetrack Report: Models for Propagation of the NORAD Element Sets
ZATDROID:
- downloads information from a data base of satellites provided by CELESTRAK.COM
- does the orbital mechanics calculations (NORAD models)
- gets the device sensors magnitudes
- connects to the web service Google Maps Elevation to get the altitude of the user location
- processes data in terms of XML language
- creates a GOOGLE MAPS views with the updated position in real time of the satellite picked
- shows the position in the sky in augmented reality using OPENGL for the camera view.


The GUI (Graphical User Interface) manages to lead the users through an easy and friendly navigation to achieve their aims quickly, showing the results of the user picks with BreadCrumbs, supporting English and Spanish, showing icon-based menus and keeping the users informed.

Here it is the flowchart of the App:

<img src="https://github.com/user-attachments/assets/6699344e-782b-45a1-b3a8-33c6406c49eb" alt="image description" width="600" />

![image](https://github.com/user-attachments/assets/3a151da6-0802-4dc4-88ab-b3d8785e7368)

Documentation:
- [Final Project Report](./Documentation/Final_Year_Project_Report.pdf)
- [Technical Manual](./Documentation/Technical_Manual_1.pdf)
- [User Manual](./Documentation/User_Manual.pdf)



