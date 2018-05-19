package com.example.g.luciddreamgenerator;

import android.app.Application;

public class LucidApp extends Application{
    private boolean loggedIn  = false;
    public boolean getLogged() {
        return loggedIn;
    }
    public void setLogged(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}
