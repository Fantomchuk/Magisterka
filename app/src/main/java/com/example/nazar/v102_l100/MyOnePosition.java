package com.example.nazar.v102_l100;

import android.util.Log;

/**
 * Created by Nazar on 8/1/2017.
 */

public class MyOnePosition {
    private double ax,ay,az,wx,wy,wz;
    private int stepTime, operation;


    public MyOnePosition(double _ax, double _ay, double _az, double _wx, double _wy, double _wz, int _stepTime, int _operation){
        this.ax = _ax;
        this.ay = _ay;
        this.az = _az;
        this.wx = _wx;
        this.wy = _wy;
        this.wz = _wz;
        this.stepTime = _stepTime;
        this.operation = _operation;
    }

    public double getAx() {return ax;}
    public double getAy() {return ay;}
    public double getAz() {return az;}
    public double getWx() {return wx;}
    public double getWy() {return wy;}
    public double getWz() {return wz;}
    public int getOperation() {return operation;}
    public int getStepTime() {return stepTime;}

}
