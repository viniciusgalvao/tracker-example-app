package com.frevocomunicacao.tracker.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.frevocomunicacao.tracker.activities.MainActivity;

/**
 * Created by Vinicius on 29/01/16.
 */
public class ConnectionDetector {

    public static boolean isNetworkConnected(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting() ? true : false;

        return isConnected;
    }

    public static boolean isGpsEnabled(Context ctx) {
        LocationManager mlocManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        return mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static void showRequestGPSDialog(final Context ctx) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setCancelable(false);
        builder.setTitle("Requisição de GPS");
        builder.setMessage("Por favor, ative seu GPS");
        builder.setPositiveButton("Ativar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ctx.startActivity(
                        new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        builder.setNegativeButton("Ignorar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ctx.startActivity(new Intent(ctx, MainActivity.class));
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}
