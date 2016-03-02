package ru.shtrm.iamclever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

 public class IBroadcastReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent startServiceIntent = new Intent(context, DrawerActivity.class);
            context.startService(startServiceIntent);
        }
 }
