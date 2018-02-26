package com.example.nazar.v102_l100;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class oneRevert extends AppCompatActivity {

    TextView txt_1;
    String name_operation;
    int operation;
    DBFunctions dbF;
    ArrayList<Double> dataArray_Ax, dataArray_Ay, dataArray_Az, dataArray_Wx, dataArray_Wy, dataArray_Wz;
    ArrayList<Double> dataArray_Ax_oYo, dataArray_Ay_oYo, dataArray_Az_oYo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_revert);

        txt_1 = (TextView) findViewById(R.id.oneRevert_text_1);

        /**
         * дані що приходять з бази даних
         * nameGraphics - для якої операції будуємо графік
         * operation - для якого досліду виконуємо дану побудову
         */
        Intent intent = getIntent();
        name_operation = intent.getStringExtra(AllGraphs.KEY_FOR_INTENT_GRAPH);
        operation = intent.getIntExtra(ChoiseOperationGraphs.KEY_FOR_INTENT_OPERATION, -1);

        /**
         * відкриваємо базу і читаємо дані потрібні для побудови
         * dataArray_A? - прискорення по осі ?
         * dataArray_W? - кут повороту навколо осі ?
         */
        dbF = new DBFunctions(this);
        dbF.open();
        dataArray_Ax = dbF.getArrayForGraphics(DBHelper.KEY_T1_Ax, operation);
        dataArray_Ay = dbF.getArrayForGraphics(DBHelper.KEY_T1_Ay, operation);
        dataArray_Az = dbF.getArrayForGraphics(DBHelper.KEY_T1_Az, operation);

        dataArray_Wx = dbF.getArrayForGraphics(DBHelper.KEY_T1_Wx, operation);
        dataArray_Wy = dbF.getArrayForGraphics(DBHelper.KEY_T1_Wy, operation);
        dataArray_Wz = dbF.getArrayForGraphics(DBHelper.KEY_T1_Wz, operation);
        dbF.close();

//        txt_1.setText("Vidbulysia zminy" + name_operation + "  " + operation);
        //powrot wokol OY, dataArray_Ay=const
        //powrot przeciw wskazowki zegarka
        // [x']   [x][cosQ,  0, -sinQ]   [ x*cosQ - z*sinQ ]
        // [y'] = [y][0,     1,     0] = [        y        ]
        // [z']   [z][sinQ,  0,  cosQ]   [ x*sinQ + z*cosQ ]
        // dataArray_Wy -> Q

//        Log.d("qqqqq", "3_3__" + Math.cos(Math.toRadians(60)) +"");
        Log.d("qqqqq", "lenght_AX_Start_" + dataArray_Ax.size() +"");
        dataArray_Ax_oYo = new ArrayList<>();
        dataArray_Ay_oYo = new ArrayList<>();
        dataArray_Az_oYo = new ArrayList<>();
        for(int i = 0; i < dataArray_Ax.size(); i++){
            double tmp_Ax, tmp_Ay, tmp_Az;
            tmp_Ax = dataArray_Ax.get(i)*Math.cos(Math.toRadians(dataArray_Wy.get(i))) - dataArray_Az.get(i)*Math.sin(Math.toRadians(dataArray_Wy.get(i)));
            tmp_Ay = dataArray_Ay.get(i);
            tmp_Az = dataArray_Ax.get(i)*Math.sin(Math.toRadians(dataArray_Wy.get(i))) + dataArray_Az.get(i)*Math.cos(Math.toRadians(dataArray_Wy.get(i)));
//            Log.d("qqqqq", "i=" + i+"");
//            Log.d("qqqqq", "tmp_Ax=" + tmp_Ax+"");
//            Log.d("qqqqq", "tmp_Ay=" + tmp_Ay+"");
//            Log.d("qqqqq", "tmp_Az=" + tmp_Az+"");
//            Log.d("qqqqq", "-------------------");
            dataArray_Ax_oYo.add(tmp_Ax);
            dataArray_Ay_oYo.add(tmp_Ay);
            dataArray_Az_oYo.add(tmp_Az);
        }

        Log.d("qqqqq", "lenght_AX_oYo_" + dataArray_Ax_oYo.size() +"");
        for(int i = 0; i < dataArray_Ax_oYo.size(); i++){
            Log.d("qqqqq", "AX_Start_"+i+"="+dataArray_Ax.get(i) + "{kat(oy)= }"+ dataArray_Wy.get(i) + "---AX_o_Y_o__"+i+"="+dataArray_Ax_oYo.get(i));
            Log.d("qqqqq", "AY_Start_"+i+"="+dataArray_Ay.get(i) + "{kat(oy)= }"+ dataArray_Wy.get(i) + "---AY_o_Y_o__"+i+"="+dataArray_Ay_oYo.get(i));
            Log.d("qqqqq", "AZ_Start_"+i+"="+dataArray_Az.get(i) + "{kat(oy)= }"+ dataArray_Wy.get(i) + "---AZ_o_Y_o__"+i+"="+dataArray_Az_oYo.get(i));
            Log.d("qqqqq", "-----------------------------");
        }

    }
}
