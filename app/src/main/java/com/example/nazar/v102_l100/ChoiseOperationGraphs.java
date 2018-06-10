package com.example.nazar.v102_l100;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChoiseOperationGraphs extends AppCompatActivity {

    ListView choiseOperationGraphs_ListView;
    DBFunctions dbF;
    int lastOperation;
    public static final String KEY_FOR_INTENT_RUN_MY_SERVICE = "com.example.nazar/run_my_service";
    public static final String KEY_FOR_INTENT_OPERATION = "com.example.nazar/operation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choise_operation_graphs);

        final Intent intent = getIntent();
        //провіряємо чи сервіс працює
        if (MyServiceRunning(MyServiceDataCollection.class))
            intent.putExtra(KEY_FOR_INTENT_RUN_MY_SERVICE, true);
        else
            intent.putExtra(KEY_FOR_INTENT_RUN_MY_SERVICE, false);


        dbF = new DBFunctions(this);
        dbF.open();
        lastOperation = dbF.lastOperationDataBase();
        dbF.close();

        Integer operations[] = new Integer[lastOperation + 1];
        for(int i = 0; i <= lastOperation; i++)
            operations[i] = i;

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, operations);
        choiseOperationGraphs_ListView = (ListView) findViewById(R.id.choiseOperationGraphs_ListView);
        choiseOperationGraphs_ListView.setAdapter(adapter);

        choiseOperationGraphs_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if( intent.getStringExtra(AllGraphs.KEY_FOR_INTENT_GRAPH).equals("revert") ){
                    if (MyServiceRunning(MyServiceDataCollection.class)){
                        choiseOperationGraphs_ListView.setVisibility(View.GONE);
                        ((TextView)findViewById(R.id.choiseOperationGraphs_tv)).setText("Works server, this operation is impossible");
                    }else{
                        intent.putExtra(KEY_FOR_INTENT_OPERATION, i);
                        intent.setClass(ChoiseOperationGraphs.this, oneRevert.class);
                        startActivity(intent);
                    }
                }else{
                    intent.putExtra(KEY_FOR_INTENT_OPERATION, i);
                    intent.setClass(ChoiseOperationGraphs.this, OneGraph.class);
                    startActivity(intent);
                }

            }
        });
    }

    private boolean MyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (int i = 0; i < manager.getRunningServices(Integer.MAX_VALUE).size(); i++) {
            if (serviceClass.getName().equals(manager.getRunningServices(Integer.MAX_VALUE).get(i).service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
