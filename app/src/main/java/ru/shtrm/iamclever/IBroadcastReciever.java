package ru.shtrm.iamclever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Shtrm on 23.02.2016.
 */
 public class IBroadcastReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent startServiceIntent = new Intent(context, DrawerActivity.class);
            context.startService(startServiceIntent);
        }
 }
