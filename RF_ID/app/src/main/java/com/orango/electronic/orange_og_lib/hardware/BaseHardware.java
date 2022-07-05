package com.orango.electronic.orange_og_lib.hardware;

import android.app.Application;

abstract public class BaseHardware {

    public Application app;

    public BaseHardware (Application app) {
        this.app = app;
    }

    public BaseHardware () {
    }

    public void onCreate (){
    }

    public void setApp (Application app) {
        this.app = app;
    }
}
