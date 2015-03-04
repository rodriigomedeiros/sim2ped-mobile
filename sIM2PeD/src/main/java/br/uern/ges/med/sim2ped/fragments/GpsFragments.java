package br.uern.ges.med.sim2ped.fragments;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.math.BigDecimal;

import br.uern.ges.med.sim2ped.R;
import br.uern.ges.med.sim2ped.gps.GPSCallback;
import br.uern.ges.med.sim2ped.gps.GPSManager;

/**
 * Created by Rodrigo on 18/09/2014 (11:01).
 * <p/>
 * Package: br.uern.ges.med.sim2ped.fragments
 */
public class GpsFragments extends Fragment implements GPSCallback {

    private GPSManager gpsManager = null;
    private double speed = 0.0;
    Boolean isGPSEnabled=false;
    LocationManager locationManager;
    double currentSpeed,kmphSpeed;
    TextView txtview;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View rootView;

        rootView = inflater.inflate(R.layout.layout_alert_care, container, false);

		/*
		LocationManager locationManager = (LocationManager) container.getContext().getSystemService(Context.LOCATION_SERVICE);
		// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				location.getLatitude();
				//Toast.makeText(container.getContext(), "Current speed: " + (location.getSpeed()*3600/1000 +"km/h"), Toast.LENGTH_SHORT).show();
			}
			public void onStatusChanged(String provider, int status, Bundle extras) { }
			public void onProviderEnabled(String provider) { }
			public void onProviderDisabled(String provider) { }

		};


		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
*/
		// Código que funciona a partir daqui.
		//txtview = (TextView) rootView.findViewById(R.id.speed);
		locationManager = (LocationManager)container.getContext().getSystemService(Context.LOCATION_SERVICE);
        gpsManager = new GPSManager(container.getContext());
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(isGPSEnabled){
        	gpsManager.startListening(container.getContext());
            gpsManager.setGPSCallback(this);
        } else {
        	gpsManager.showSettingsAlert();
        }


        return rootView;
    }

    @Override
    public void onGPSUpdate(Location location) {
        // TODO Auto-generated method stub
        speed = location.getSpeed();
        currentSpeed = round(speed,3, BigDecimal.ROUND_HALF_UP);
        kmphSpeed = round((currentSpeed*3.6),3,BigDecimal.ROUND_HALF_UP);
        if(kmphSpeed > 0){
            txtview.setText(kmphSpeed + "Km/h");
        } else {
            txtview.setText("Parado");
        }
    }

    public static double round(double unrounded, int precision, int roundingMode)
    {
        BigDecimal bd = new BigDecimal(unrounded);
        BigDecimal rounded = bd.setScale(precision, roundingMode);
        return rounded.doubleValue();
    }

}
