package com.lefuorgn.interactive.impl;

import com.lefuorgn.interactive.Interactive;
import com.lefuorgn.interactive.interf.Sync;
import com.lefuorgn.interactive.interf.SyncCallback;
import com.lefuorgn.interactive.util.Actuator;


public class RealSync implements Sync {

    private final Interactive interactive;
    private boolean executed;

    public RealSync(Interactive interactive) {
        this.interactive = interactive;
    }

    @Override
    public void enqueue(SyncCallback syncCallback) {
        synchronized (this) {
            if (isExecuted()) throw new IllegalStateException("Already Executed");
            executed = true;
        }
        Actuator actuator = Actuator.getInstance();
        if(!actuator.isSyncing()) {
            actuator.start(interactive, syncCallback);
        }else if(syncCallback != null) {
            syncCallback.syncing();
        }
    }

    @Override
    public synchronized boolean isExecuted() {
        return executed;
    }

}
