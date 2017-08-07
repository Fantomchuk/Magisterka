package com.example.nazar.v102_l100;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button start_service, stop_service, data_base, data_base_delete, create_files;
    LinearLayout mainActivity_ll_pb;
    final public static String KEY_FOR_INTENT_STEP_TIME = "com.example.nazar/time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        start_service = (Button) findViewById(R.id.mainActivity_btn_start_service_data_collection);
        stop_service = (Button) findViewById(R.id.mainActivity_btn_stop_service_data_collection);
        data_base = (Button) findViewById(R.id.mainActivity_btn_data_base);
        data_base_delete = (Button) findViewById(R.id.mainActivity_btn_data_base_delete);
        create_files = (Button) findViewById(R.id.mainActivity_btn_create_files);
        mainActivity_ll_pb = (LinearLayout) findViewById(R.id.mainActivity_ll_pb);

        stop_service.setEnabled(false);
        mainActivity_ll_pb.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void startService(View view) {
        Log.d("qqqqq", "startService");
        startService(new Intent(this, MyServiceDataCollection.class).putExtra(KEY_FOR_INTENT_STEP_TIME, 1000));
        start_service.setEnabled(false);
        stop_service.setEnabled(true);
        data_base_delete.setEnabled(false);
        mainActivity_ll_pb.setVisibility(View.VISIBLE);
        create_files.setEnabled(false);
    }

    public void stopService(View view) {
        Log.d("qqqqq", "stopService");
        stopService(new Intent(this, MyServiceDataCollection.class));
        stop_service.setEnabled(false);
        start_service.setEnabled(true);
        data_base_delete.setEnabled(true);
        mainActivity_ll_pb.setVisibility(View.GONE);
        create_files.setEnabled(true);
    }


    public void dataBaseShow(View view) {
        Log.d("qqqqq", "dataBaseShow");
        startActivity(new Intent(this, ShowDataBase.class));
    }

    public void dataBaseDelete(View view) {
        Log.d("qqqqq", "dataBaseDelete");
        deleteDatabase(DBHelper.DATABASE_NAME);
    }

    public void createFiles(View view) {
        startActivity(new Intent(this, CreateFiles.class));
    }

    public void graphsShow(View view) {
        startActivity(new Intent(this, AllGraphs.class));
    }


}
