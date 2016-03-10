package com.frevocomunicacao.tracker.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.frevocomunicacao.tracker.activities.MainActivity;

/**
 * Created by Vinicius on 10/03/16.
 */
public class GpsTracker implements LocationListener {

    // application context
    private final Context mContext;

    // flag for gps status
    private boolean isGpsEnabled = false;

    // flag for network status
    private boolean isNetworkEnabled = false;

    // flag for can get location
    private boolean canGetLocation = false;

    // position data
    private Location location;
    private double latitude;
    private double longitude;

    // location listener
    private LocationListener mLocationListener;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATE = 1; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BTW_UPDATES = 1; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;

    public GpsTracker(Context mContext, LocationListener locationListener) {
        this.mContext           = mContext;
        this.mLocationListener  = locationListener != null ? locationListener : this;

        // location service
        this.locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
    }

    /**
     * Function to get GPS location
     *
     * @return Location
     */
    public Location getLocation() {
        try {
            // get providers status
            this.refreshProvidersStatus();

            // get location with provider enabled
            if (isGpsEnabled) {
                this.canGetLocation = true;
                this.location       = null;

                if (location == null) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BTW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATE, this.mLocationListener);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            this.latitude = location.getLatitude();
                            this.longitude= location.getLongitude();
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    /**
     * Function to stop GPS updates
     *
     * @return void
     */
    public void stopUsingGps() {
        if (locationManager != null) {
            locationManager.removeUpdates(mLocationListener);
        }
    }

    /**
     * Function to get latitude
     *
     * @return double
     * */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        return latitude;
    }

    /**
     * Function to get longitude
     *
     * @return double
     * */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     * */
    public boolean canGetLocation() {
        this.refreshProvidersStatus();
        this.canGetLocation = isGpsEnabled ? true : false;

        return this.canGetLocation;
    }

    /**
     * Function to get providers status
     *
     * @return void
     */
    private void refreshProvidersStatus() {

        // gps status
        this.isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // network status
        this.isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

    }

    /**
     * Function to build settings alert to request enable GPS
     *
     * @return void
     */
    public void showSettingsAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);
        builder.setTitle("Requisição de GPS");
        builder.setMessage("Por favor, ative seu GPS");
        builder.setPositiveButton("Ativar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mContext.startActivity(
                        new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

            }
        });

        builder.setNegativeButton("Ignorar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                mContext.startActivity(new Intent(mContext, MainActivity.class));
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void setLocationListener(LocationListener locationListener) {
        this.mLocationListener = locationListener;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
