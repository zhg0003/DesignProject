package com.example.g.luciddreamgenerator;

import android.app.Application;

public class LucidApp extends Application{
    private boolean loggedIn  = false;
    private boolean recentlyLogged = false;
    private boolean isSynced = false;
    public boolean getLogged() {
        return loggedIn;
    }
    public boolean getRecentlyLogged() {
        return recentlyLogged;
    }


    public void setLogged(boolean loggedIn) {
        this.loggedIn = loggedIn;
        setRecentlyLogged(loggedIn);
    }
    public void setRecentlyLogged(boolean loggedIn) {
        this.recentlyLogged = loggedIn;
    }
}
