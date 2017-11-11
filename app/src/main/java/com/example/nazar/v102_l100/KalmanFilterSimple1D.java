package com.example.nazar.v102_l100;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Nazar on 11/3/2017.
 */

class KalmanFilterSimple1D {

    private double F;    // factor of real value to previous real value
    private double Q;    // measurement noise
    private double H;    //factor of measured value to real value
    private double R;    // environment noise

    private double State;
    private double Covariance;

    private ArrayList<Double> dataOrigin;

    KalmanFilterSimple1D(double q, double r)
    {
        this.Q = q;
        this.R = r;
        this.F = 1;
        this.H = 1;
    }
    public KalmanFilterSimple1D(double q, double r, double f, double h)
    {
        this.Q = q;
        this.R = r;
        this.F = f;
        this.H = h;
    }

    private void SetState(double state, double covariance)
    {
        this.State = state;
        this.Covariance = covariance;
    }

    private void Correct(double data)
    {
        //time update - prediction
        double x0 = F * State;
        double p0 = F * Covariance * F + Q;

        //measurement update - correction
        double K = H* p0 /(H* p0 *H + R);
        State = x0 + K*(data - H* x0);
        Covariance = (1 - K*H)* p0;
    }

     public void setArrayForFiltr(ArrayList<Double> origin){
         dataOrigin = new ArrayList<>(origin.size());
         dataOrigin = origin;
     }

    ArrayList<Double> getArrayFiltr(){
         ArrayList<Double> filtr = new ArrayList<>();
         SetState(dataOrigin.get(0), 0.1);
         for(int i=0; i<dataOrigin.size(); i++){
             Correct(dataOrigin.get(i));
             filtr.add(State);
         }
         return filtr;
     }
}
