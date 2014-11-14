package com.goviami.bullseye.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.goviami.bullseye.R;
import com.goviami.bullseye.util.AppConstants;
import com.goviami.bullseye.util.NotificationId;
import com.goviami.bullseye.util.PreferencesManager;

/**
 * Created by subbu on 11/14/2014.
 */
public class DartBackgroundService extends Service {

    private PowerManager.WakeLock wakeLock;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        wakeLock.release();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleIntent(intent);
        return START_NOT_STICKY;
    }

    private void handleIntent(Intent intent) {
        // obtain the wake lock
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "DartTag");
        wakeLock.acquire();

        // check the global background data setting
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (wifiNetwork != null && wifiNetwork.getState() == NetworkInfo.State.CONNECTED) {
            // do the work in separate thread
            new PoolTask().execute();
        } else if (mobileNetwork != null && mobileNetwork.getState() == NetworkInfo.State.CONNECTED) {
            // do the work in separate thread
            new PoolTask().execute();
        } else {
            stopSelf();
            return;
        }
    }

    private void broadcastToActivity() {
        Intent intent = new Intent();
        intent.setAction(AppConstants.DART_SERVICE_BROADCAST_NAME);
        intent.putExtra("message", "message received at " + System.currentTimeMillis());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private class PoolTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Log.v("service", "do in back");
            // call webservice to push and pull messages
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // handle your data returned from webservice
            // call notification or update activity
            if (PreferencesManager.isInstantiated()) {
                broadcastToActivity();
            } else {
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                        .setGroup(AppConstants.NOTIFICATION_MESSAGE_GROUP)
                        .setGroupSummary(true)
                        .setSound(alarmSound)
                        .setSmallIcon(R.drawable.ic_orange)
                        .setContentTitle("My notification")
                        .setContentText("message received at " + System.currentTimeMillis());
                manager.notify(NotificationId.getId(), mBuilder.build());
            }
            stopSelf();
        }
    }

}
