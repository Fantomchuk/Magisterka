package com.example.nazar.v102_l100;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AllGraphs extends AppCompatActivity {

    Button btn_Ax,btn_Ay,btn_Az,btn_Wx,btn_Wy,btn_Wz,btn_revert;
    public static final String KEY_FOR_INTENT_GRAPH = "com.example.nazar/graph";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_graphs);
        btn_Ax = (Button) findViewById(R.id.allGraphs_btn_Ax);
        btn_Ay = (Button) findViewById(R.id.allGraphs_btn_Ay);
        btn_Az = (Button) findViewById(R.id.allGraphs_btn_Az);
        btn_Wx = (Button) findViewById(R.id.allGraphs_btn_Wx);
        btn_Wy = (Button) findViewById(R.id.allGraphs_btn_Wy);
        btn_Wz = (Button) findViewById(R.id.allGraphs_btn_Wz);
        btn_revert = (Button) findViewById(R.id.allGraphs_btn_revert);

        View.OnClickListener listener_btn = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllGraphs.this, ChoiseOperationGraphs.class);
                switch (view.getId()){
                    case R.id.allGraphs_btn_Ax:
                        intent.putExtra(KEY_FOR_INTENT_GRAPH, DBHelper.KEY_T1_Ax);
                        break;
                    case R.id.allGraphs_btn_Ay:
                        intent.putExtra(KEY_FOR_INTENT_GRAPH, DBHelper.KEY_T1_Ay);
                        break;
                    case R.id.allGraphs_btn_Az:
                        intent.putExtra(KEY_FOR_INTENT_GRAPH, DBHelper.KEY_T1_Az);
                        break;
                    case R.id.allGraphs_btn_Wx:
                        intent.putExtra(KEY_FOR_INTENT_GRAPH, DBHelper.KEY_T1_Wx);
                        break;
                    case R.id.allGraphs_btn_Wy:
                        intent.putExtra(KEY_FOR_INTENT_GRAPH, DBHelper.KEY_T1_Wy);
                        break;
                    case R.id.allGraphs_btn_Wz:
                        intent.putExtra(KEY_FOR_INTENT_GRAPH, DBHelper.KEY_T1_Wz);
                        break;
                    case R.id.allGraphs_btn_revert:
                        intent.putExtra(KEY_FOR_INTENT_GRAPH, "revert");
                        break;
                }
                startActivity(intent);
            }
        };
        btn_Ax.setOnClickListener(listener_btn);
        btn_Ay.setOnClickListener(listener_btn);
        btn_Az.setOnClickListener(listener_btn);
        btn_Wx.setOnClickListener(listener_btn);
        btn_Wy.setOnClickListener(listener_btn);
        btn_Wz.setOnClickListener(listener_btn);
        btn_revert.setOnClickListener(listener_btn);
    }
}
