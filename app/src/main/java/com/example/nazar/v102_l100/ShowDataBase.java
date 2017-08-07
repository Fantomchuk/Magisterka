package com.example.nazar.v102_l100;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class ShowDataBase extends AppCompatActivity {

    ListView showDataBase_lv_positions;
    private ArrayList<MyOnePosition> positions, positions_end;
    ArrayList<Map<String, Object>> data;
    Handler h;
    DBFunctions dbF;
    int lastIndexT1;
    SimpleAdapter sAdapter;

    private static final String ATTRIBUTE_TEXT_AX = "com.example.nazar/ax";
    private static final String ATTRIBUTE_TEXT_AY = "com.example.nazar/ay";
    private static final String ATTRIBUTE_TEXT_AZ = "com.example.nazar/az";
    private static final String ATTRIBUTE_TEXT_WX = "com.example.nazar/wx";
    private static final String ATTRIBUTE_TEXT_WY = "com.example.nazar/wy";
    private static final String ATTRIBUTE_TEXT_WZ = "com.example.nazar/wz";
    private static final String ATTRIBUTE_TEXT_STEP_TIME = "com.example.nazar/step_time";
    private static final String ATTRIBUTE_TEXT_OPERATION = "com.example.nazar/operation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data_base);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbF = new DBFunctions(this);
        dbF.open();
        positions = new ArrayList<>();
        positions = dbF.getAllPositions();

        data = new ArrayList<>(positions.size());

        Map<String, Object> m;
        for (int i = 0; i < positions.size(); i++) {
            m = new HashMap<>();
            m.put(ATTRIBUTE_TEXT_AX, positions.get(i).getAx());
            m.put(ATTRIBUTE_TEXT_AY, positions.get(i).getAy());
            m.put(ATTRIBUTE_TEXT_AZ, positions.get(i).getAz());
            m.put(ATTRIBUTE_TEXT_WX, positions.get(i).getWx());
            m.put(ATTRIBUTE_TEXT_WY, positions.get(i).getWy());
            m.put(ATTRIBUTE_TEXT_WZ, positions.get(i).getWz());
            m.put(ATTRIBUTE_TEXT_STEP_TIME, positions.get(i).getStepTime());
            m.put(ATTRIBUTE_TEXT_OPERATION, positions.get(i).getOperation());
            data.add(m);
        }

        String[] from = new String[] {ATTRIBUTE_TEXT_AX, ATTRIBUTE_TEXT_AY, ATTRIBUTE_TEXT_AZ,
                ATTRIBUTE_TEXT_WX, ATTRIBUTE_TEXT_WY, ATTRIBUTE_TEXT_WZ,
                ATTRIBUTE_TEXT_STEP_TIME, ATTRIBUTE_TEXT_OPERATION};
        int[] to = new int[] {R.id.itemOnePosition_Ax, R.id.itemOnePosition_Ay, R.id.itemOnePosition_Az,
                R.id.itemOnePosition_Wx, R.id.itemOnePosition_Wy, R.id.itemOnePosition_Wz,
                R.id.itemOnePosition_stepTime, R.id.itemOnePosition_operation};
        sAdapter = new SimpleAdapter(this, data, R.layout.item_one_position, from, to);

        View v = getLayoutInflater().inflate(R.layout.item_one_position, null);
        ((TextView)v.findViewById(R.id.itemOnePosition_Ax)).setText("Ax");
        ((TextView)v.findViewById(R.id.itemOnePosition_Ay)).setText("Ay");
        ((TextView)v.findViewById(R.id.itemOnePosition_Az)).setText("Az");
        ((TextView)v.findViewById(R.id.itemOnePosition_Wx)).setText("Wx");
        ((TextView)v.findViewById(R.id.itemOnePosition_Wy)).setText("Wy");
        ((TextView)v.findViewById(R.id.itemOnePosition_Wz)).setText("Wz");
        ((TextView)v.findViewById(R.id.itemOnePosition_stepTime)).setText("T");
        ((TextView)v.findViewById(R.id.itemOnePosition_operation)).setText("O");

        showDataBase_lv_positions = (ListView) findViewById(R.id.ShowDataBase_lv_positions);
        showDataBase_lv_positions.addHeaderView(v);
        showDataBase_lv_positions.setAdapter(sAdapter);



        if (MyServiceRunning(MyServiceDataCollection.class)) {
            lastIndexT1 = dbF.lastIndexTable1();
            positions_end = new ArrayList<>();
            h = new Handler();
            Thread t = new Thread(new Runnable() {
                public void run() {
                    h.post(changeListView);
                }
            });
            t.start();
        }
    }

    //оновлення вигляду ListView, через кожну секунду
    Runnable changeListView = new Runnable() {
        public void run() {
            positions_end = dbF.getPositionsToEnd(lastIndexT1);

            Map<String, Object> m;
            if(positions_end.size() != 0) {
                for (int i = 0; i < positions_end.size(); i++) {
                    m = new HashMap<>();
                    m.put(ATTRIBUTE_TEXT_AX, positions_end.get(i).getAx());
                    m.put(ATTRIBUTE_TEXT_AY, positions_end.get(i).getAy());
                    m.put(ATTRIBUTE_TEXT_AZ, positions_end.get(i).getAz());
                    m.put(ATTRIBUTE_TEXT_WX, positions_end.get(i).getWx());
                    m.put(ATTRIBUTE_TEXT_WY, positions_end.get(i).getWy());
                    m.put(ATTRIBUTE_TEXT_WZ, positions_end.get(i).getWz());
                    m.put(ATTRIBUTE_TEXT_STEP_TIME, positions_end.get(i).getStepTime());
                    m.put(ATTRIBUTE_TEXT_OPERATION, positions_end.get(i).getOperation());
                    data.add(m);
                }
                lastIndexT1 = dbF.lastIndexTable1();
            }
            sAdapter.notifyDataSetChanged();
            h.postDelayed(changeListView, 1000);
        }
    };

    private boolean MyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (int i = 0; i < manager.getRunningServices(Integer.MAX_VALUE).size(); i++) {
            if (serviceClass.getName().equals(manager.getRunningServices(Integer.MAX_VALUE).get(i).service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbF.close();
        if (MyServiceRunning(MyServiceDataCollection.class))
            h.removeCallbacks(changeListView);
    }
}
