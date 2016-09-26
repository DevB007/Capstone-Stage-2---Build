package com.example.deveshwar.imalive;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class IMaliveWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IMaliveRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}