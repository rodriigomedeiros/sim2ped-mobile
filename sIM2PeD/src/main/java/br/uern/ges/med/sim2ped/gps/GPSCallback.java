package br.uern.ges.med.sim2ped.gps;

import android.location.Location;

public interface GPSCallback
{
        public abstract void onGPSUpdate(Location location);
}
