package com.example.nazar.v102_l100;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import android.view.Surface;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MyServiceDataCollection extends Service {

    DBFunctions dbF;
    int lastOperation;
    int stepTime;

    SensorManager sensorManager;
    Sensor sensorAccel;
    Sensor sensorMagnet;
    int rotation;

    float[] valuesAccel = new float[3];
    float[] valuesAccelMotion = new float[3];
    float[] valuesAccelGravity = new float[3];
    float[] valuesMagnet = new float[3];
    float[] valuesResultOrientation = new float[3];

    public MyServiceDataCollection() {
    }

    public void onCreate() {
        super.onCreate();
        //відкриваємо зєднання з базою даних
        dbF = new DBFunctions(this);
        dbF.open();
        lastOperation = dbF.lastOperationDataBase();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorAccel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMagnet = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        Log.d("qqqqq", "create");
        Log.d("qqqqq", "open_db");
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    //час який приходить в мілісекундах
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("qqqqq", "onStartCommand");
        stepTime = intent.getIntExtra(MainActivity.KEY_FOR_INTENT_STEP_TIME, 500);
        Log.d("qqqqq", lastOperation +"");
        Log.d("qqqqq", stepTime +"");
        MyRunCollection mrc = new MyRunCollection(lastOperation + 1, stepTime);
        mrc.run();
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d("qqqqq", "destroy");
        //закриваємо зєднання з БД
        Log.d("qqqqq", "close_db");
        dbF.close();
        dbF = null;
    }

    class MyRunCollection implements Runnable {
        private int stepTime;
        private int lastOperation;
        private int positionInListServiceStart = 0;
        private Timer timer;
        SensorEventListener listener;

        public MyRunCollection(int _lastOperation, int _stepTime) {
            this.stepTime = _stepTime;
            this.lastOperation = _lastOperation;
        }

        public void run() {
            //слухач для сенсорів
            listener = new SensorEventListener() {
                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                }

                @Override
                public void onSensorChanged(SensorEvent event) {
                    switch (event.sensor.getType()) {
                        case Sensor.TYPE_ACCELEROMETER:
                            for (int i = 0; i < 3; i++) {
                                final double alpha = (float)0.8;
                                valuesAccel[i] = event.values[i];
                                valuesAccelGravity[i] = (float)((1-alpha)* event.values[i] + alpha * valuesAccelGravity[i]);
                                valuesAccelMotion[i] = event.values[i] - valuesAccelGravity[i];
                            }
                            break;
                        case Sensor.TYPE_MAGNETIC_FIELD:
                            for (int i=0; i < 3; i++){
                                valuesMagnet[i] = event.values[i];
                            }
                            break;
                    }
                }
            };
            sensorManager.registerListener(listener, sensorAccel, SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(listener, sensorMagnet, SensorManager.SENSOR_DELAY_NORMAL);

            //завдання яке буде спрацьовувати з кроком часу який задамо,
            // для зчитування даних з сенсорів, і додавання їх в базу даних
            timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (dbF != null) {
                        Log.d("qqqqq", "db_run");
                        //showInfo();
                        addOnePositionToDataBase();
                    }
                }
            };
            timer.schedule(task, 0, stepTime);

            //завдання яке провіряє чи сервіс ще працює, спрацьовує кожну секунду, можна змінити
            TimerTask task_check_service = new TimerTask() {
                @Override
                public void run() {
                    positionInListServiceStart = MyServiceRunning(MyServiceDataCollection.class, positionInListServiceStart);
                    if (positionInListServiceStart == -1) {
                        Log.d("qqqqq", "end");
                        sensorManager.unregisterListener(listener);
                        timer.cancel();
                    }
                }
            };
            timer.schedule(task_check_service, 0, 1000);
        }

        //додавання даних
        private void addOnePositionToDataBase(){
            getActualDeviceOrientation();
            dbF.addPositionToDataBase(roundDouble(valuesAccelMotion[0], 2),roundDouble(valuesAccelMotion[1], 2),roundDouble(valuesAccelMotion[2], 2),
                    roundDouble(valuesResultOrientation[1],2),roundDouble(valuesResultOrientation[2],2),roundDouble(valuesResultOrientation[0],2),
                    stepTime, lastOperation);
        }

//        void showInfo() {
//            StringBuilder sb = new StringBuilder();
//            sb.setLength(0);
//            sb.append("Orientation : " + roundDouble(valuesResultOrientation[0],2) + " || " + roundDouble(valuesResultOrientation[1],2) + " || " + roundDouble(valuesResultOrientation[2],2) + "\n");
//            sb.append("Accelerometer: " + roundDouble(valuesAccel[0], 2) + " || " + roundDouble(valuesAccel[1], 2) + " || " + roundDouble(valuesAccel[2], 2) )
//                    .append("\n\nAccel motion: " + roundDouble(valuesAccelMotion[0], 2) + " || " + roundDouble(valuesAccelMotion[1], 2) + " || " + roundDouble(valuesAccelMotion[2], 2))
//                    .append("\nAccel gravity : " + roundDouble(valuesAccelGravity[0], 2)+ " || " + roundDouble(valuesAccelGravity[1], 2)+ " || " + roundDouble(valuesAccelGravity[2], 2));
//
//            Log.d("qqqqq", sb.toString());
//        }

        //округлення даних для бази даних
        private double roundDouble(float value, int places) {
            if (places < 0) throw new IllegalArgumentException();
            long factor = (long) Math.pow(10, places);
            value = value * factor;
            long tmp = Math.round(value);
            return (double) tmp / factor;
        }

        //актуальне положення телефона
        float[] inR = new float[9];
        float[] outR = new float[9];
        void getActualDeviceOrientation() {
            SensorManager.getRotationMatrix(inR, null, valuesAccel, valuesMagnet);
            int axisX = SensorManager.AXIS_X;
            int axisY = SensorManager.AXIS_Y;
            switch (rotation) {
                case (Surface.ROTATION_0): break;
                case (Surface.ROTATION_90):
                    axisX = SensorManager.AXIS_Y;
                    axisY = SensorManager.AXIS_MINUS_X;
                    break;
                case (Surface.ROTATION_180):
                    axisY = SensorManager.AXIS_MINUS_Y;
                    break;
                case (Surface.ROTATION_270):
                    axisX = SensorManager.AXIS_MINUS_Y;
                    axisY = SensorManager.AXIS_X;
                    break;
                default: break;
            }
//            boolean isUpSideDown = valuesAccel[2] < 0;
//            int axisX,axisY;
//
//            switch (rotation) {
//                case Surface.ROTATION_0:
//                    axisX = (isUpSideDown ? SensorManager.AXIS_MINUS_X : SensorManager.AXIS_X);
//                    axisY = (Math.abs(valuesAccel[1]) > 6.0f ?
//                            (isUpSideDown ? SensorManager.AXIS_MINUS_Z : SensorManager.AXIS_Z) :
//                            (isUpSideDown ? SensorManager.AXIS_MINUS_Y : SensorManager.AXIS_Y));
//                    break;
//                case Surface.ROTATION_90:
//                    axisX = (isUpSideDown ? SensorManager.AXIS_MINUS_Y : SensorManager.AXIS_Y);
//                    axisY = (Math.abs(valuesAccel[0]) > 6.0f ?
//                            (isUpSideDown ? SensorManager.AXIS_Z : SensorManager.AXIS_MINUS_Z) :
//                            (isUpSideDown ? SensorManager.AXIS_X : SensorManager.AXIS_MINUS_X));
//                    break;
//                case  Surface.ROTATION_180:
//                    axisX = (isUpSideDown ? SensorManager.AXIS_X : SensorManager.AXIS_MINUS_X);
//                    axisY = (Math.abs(valuesAccel[1]) > 6.0f ?
//                            (isUpSideDown ? SensorManager.AXIS_Z : SensorManager.AXIS_MINUS_Z) :
//                            (isUpSideDown ? SensorManager.AXIS_Y : SensorManager.AXIS_MINUS_Y));
//                    break;
//                case Surface.ROTATION_270:
//                    axisX = (isUpSideDown ? SensorManager.AXIS_Y : SensorManager.AXIS_MINUS_Y);
//                    axisY = (Math.abs(valuesAccel[0]) > 6.0f ?
//                            (isUpSideDown ? SensorManager.AXIS_MINUS_Z : SensorManager.AXIS_Z) :
//                            (isUpSideDown ? SensorManager.AXIS_MINUS_X : SensorManager.AXIS_X));
//                    break;
//                default:
//                    axisX = (isUpSideDown ? SensorManager.AXIS_MINUS_X : SensorManager.AXIS_X);
//                    axisY = (isUpSideDown ? SensorManager.AXIS_MINUS_Y : SensorManager.AXIS_Y);
//            }





            SensorManager.remapCoordinateSystem(inR, axisX, axisY, outR);
            SensorManager.getOrientation(outR, valuesResultOrientation);
            valuesResultOrientation[0] = (float)Math.toDegrees(valuesResultOrientation[0]);
            valuesResultOrientation[1] = (float)Math.toDegrees(valuesResultOrientation[1]);
            valuesResultOrientation[2] = (float)Math.toDegrees(valuesResultOrientation[2]);
        }

        //функція яка провіряє чи сервіс ще існує. Якщо існує запамятовуємо його останнє положення в списку,
        //для того, щоб швидше йшла провірка в майбутніх провірках.
        private int MyServiceRunning(Class<?> serviceClass, int positionInList) {
            ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            if (serviceClass.getName().equals(manager.getRunningServices(Integer.MAX_VALUE).get(positionInList).service.getClassName())) {
                return positionInList;
            }
            for (int i = 0; i < manager.getRunningServices(Integer.MAX_VALUE).size(); i++) {
                if (serviceClass.getName().equals(manager.getRunningServices(Integer.MAX_VALUE).get(i).service.getClassName())) {
                    return i;
                }
            }
            return -1;
        }
    }
}
