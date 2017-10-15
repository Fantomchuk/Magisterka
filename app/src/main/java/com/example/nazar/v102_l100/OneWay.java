package com.example.nazar.v102_l100;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;

public class OneWay extends AppCompatActivity {

    String nameGraphics;
    Boolean isRunMyService;
    int operation;
    DBFunctions dbF;
    ArrayList<Double> dataForGraph_Ax, dataForGraph_Ay,dataForGraph_Wz;
    float way_x_min, way_x_max, way_y_min, way_y_max;

    private RelativeLayout graphLayout;
    private ScatterChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_way);

        /**
         * дані що приходять з бази даних
         * nameGraphics - для якої операції будуємо графік
         * isRunMyService - чи працює на даний момент сервіс
         * operation - для якого досліду виконуємо дану побудову
         */
        Intent intent = getIntent();
        nameGraphics = intent.getStringExtra(AllGraphs.KEY_FOR_INTENT_GRAPH);
        isRunMyService = intent.getBooleanExtra(ChoiseOperationGraphs.KEY_FOR_INTENT_RUN_MY_SERVICE, false);
        operation = intent.getIntExtra(ChoiseOperationGraphs.KEY_FOR_INTENT_OPERATION, -1);

        /**
         * відкриваємо базу і читаємо дані потрібні для побудови
         * dataForGraph_Ax - прискорення по осі х
         * dataForGraph_Aу - прискорення по осі у
         * dataForGraph_Wz - кут повороту навколо осі z
         */
        dbF = new DBFunctions(this);
        dbF.open();
        dataForGraph_Ax = dbF.getArrayForGraphics(DBHelper.KEY_T1_Ax, operation);
        dataForGraph_Ay = dbF.getArrayForGraphics(DBHelper.KEY_T1_Ay, operation);
        dataForGraph_Wz = dbF.getArrayForGraphics(DBHelper.KEY_T1_Wz, operation);
        int StepTime = dbF.getStepTime(operation);
        //lastIndexT1 = dbF.lastIndexTable1();
        dbF.close();

        Log.d("qqqqq", StepTime+"");

        //знаходимо головний екра активності
        graphLayout = (RelativeLayout) findViewById(R.id.activity_one_way);
        //Створюємо новий обєкт - графік
        mChart = new ScatterChart(this);
        //додаємо до головного вигляду
        graphLayout.addView(mChart, new AbsListView.LayoutParams
                (AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));
        //налаштовуэмо mChart
        Description des = new Description();
        des.setText("Way");
        mChart.setDescription(des);

        //включаємо виділення
        mChart.setHighlightPerDragEnabled(true);
        mChart.setTouchEnabled(true);

        //включаємо масштабування і перетягування
        mChart.setScaleEnabled(true);
        mChart.setDragEnabled(true);

        //сітка заднього фону
        mChart.setDrawGridBackground(false);

        //Вмикаю подвійний зум, щоб уникнути масштабування осі x і y окремо
        mChart.setPinchZoom(true);

        //Фон графіка
        mChart.setBackgroundColor(Color.LTGRAY);
        //настройка графіка закінчилася

        //попрацюємо з даними
        ScatterData data = new ScatterData();
        data.setValueTextColor(Color.WHITE);

        //додбавимо дані до графіку
        mChart.setData(data);

        //отримуємо обєкт легенди
        Legend l = mChart.getLegend();

        //Настроюємо легенду
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.WHITE);

        //Беремо з графіка всі мітки і малюємо їх, це для осі Х
        XAxis x1 = mChart.getXAxis();
        x1.setTextColor(Color.BLACK);

        //малювання сітки для цієї осі
        x1.setDrawGridLines(false);

        //Значить що ми не даємо діаграмі першу і останню точку
        x1.setAvoidFirstLastClipping(true);

        //Беремо з графіка всі мітки і малюємо їх, це для осі У
        YAxis y1 = mChart.getAxisLeft(); //повертає ліву вісь
        y1.setTextColor(Color.BLACK);
//        y1.setAxisMaximum(10f);        // максимальне значення по осі
//        y1.setAxisMinimum(0);         // мінімальне значення
        y1.setDrawZeroLine(true);
        y1.setZeroLineColor(Color.RED);
        y1.setDrawGridLines(true);      // сітка для цієї осі
        y1.setGridColor(Color.WHITE);

        YAxis y2 = mChart.getAxisRight(); //повертає праву вісь
        y2.setEnabled(false);             // виключаємо її
        ///Графік будується і відображається на екрані


        /**
         * контрольий приклад
         */
        float spead_axis_x, spead_axis_y;
        float way_axis_x, way_axis_y;
        ArrayList<Float> x = new ArrayList<>(dataForGraph_Ax.size()+1);
        ArrayList<Float> y = new ArrayList<>(dataForGraph_Ax.size()+1);


for(int i=0; i <dataForGraph_Ax.size()+1; i++){
    x.add(0f);
    y.add(0f);
}

        float spead_axis_x_previous = 0;
        float spead_axis_y_previous = 0;
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////
//        StepTime = 1000;
        float time = (float)StepTime/1000;

//        dataForGraph_Ax = new ArrayList<>(4);
//        dataForGraph_Ay = new ArrayList<>(4);
//        dataForGraph_Wz = new ArrayList<>(4);
//        for(int i=0; i<4;i++){
//            dataForGraph_Ax.add((double) 1);
//            dataForGraph_Ay.add((double) 1);
//        }
//        dataForGraph_Wz.add(0d);
//        dataForGraph_Wz.add(45d);
//        dataForGraph_Wz.add(90d);
//        dataForGraph_Wz.add((double)(90+45));

        way_x_min = way_x_max = way_y_min = way_y_max = 0;

        for(int i = 2500/StepTime; i < dataForGraph_Ax.size(); i++){
            Log.d("qqqqq", "x"+i+"="+x.get(i));
            Log.d("qqqqq", "y"+i+"="+y.get(i));

            Log.d("qqqqq", "Ax_"+i+"="+dataForGraph_Ax.get(i));
            Log.d("qqqqq", "Ay_"+i+"="+dataForGraph_Ay.get(i));

            spead_axis_x = (float) (spead_axis_x_previous + dataForGraph_Ax.get(i) * time);
            spead_axis_y = (float) (spead_axis_y_previous + dataForGraph_Ay.get(i) * time);
            Log.d("qqqqq", "spead_axis_x_previous_"+i+"="+spead_axis_x_previous);
            Log.d("qqqqq", "spead_axis_y_previous_"+i+"="+spead_axis_y_previous);
            Log.d("qqqqq", "spead_axis_x_"+i+"="+spead_axis_x);
            Log.d("qqqqq", "spead_axis_y_"+i+"="+spead_axis_y);

            way_axis_x =(float)( spead_axis_x_previous * time + dataForGraph_Ax.get(i)*Math.pow(time, 2) / 2);
            way_axis_y =(float)( spead_axis_y_previous * time + dataForGraph_Ay.get(i)*Math.pow(time, 2) / 2);
            Log.d("qqqqq", "way_axis_x_"+i+"="+way_axis_x);
            Log.d("qqqqq", "way_axis_y_"+i+"="+way_axis_y);

            float x_next =(float)(  way_axis_x * Math.cos(Math.toRadians( dataForGraph_Wz.get(i) )) + way_axis_y * Math.sin(Math.toRadians( dataForGraph_Wz.get(i) )) );
            float y_next =(float)(  way_axis_y * Math.cos(Math.toRadians( dataForGraph_Wz.get(i) )) - way_axis_x * Math.sin(Math.toRadians( dataForGraph_Wz.get(i) )) );
            Log.d("qqqqq", "Z_"+i+"="+dataForGraph_Wz.get(i));

            x.set( i+1, x.get(i) + x_next );
            y.set( i+1, y.get(i) + y_next );

            Log.d("qqqqq", "x"+(i+1)+"="+x.get(i+1));
            Log.d("qqqqq", "y"+(i+1)+"="+y.get(i+1));
            Log.d("qqqqq", "-------------------------------------");
            if((x.get(i) + x_next) < way_x_min){
                way_x_min = x.get(i) + x_next;
            }
            if((y.get(i) + y_next) < way_y_min){
                way_y_min = y.get(i) + y_next;
            }
            if((x.get(i) + x_next) > way_x_max){
                way_x_max = x.get(i) + x_next;
            }
            if((y.get(i) + y_next) > way_y_max){
                way_y_max = y.get(i) + y_next;
            }

            spead_axis_x_previous = spead_axis_x;
            spead_axis_y_previous = spead_axis_y;
        }



        //Додамо графіку дані
//        for(int i =2500/StepTime; i < x.size(); i++) {
//             addEntry( x.get(i), y.get(i));
//        }

    }

    //потрібно створити метод для додавання даних
    private void addEntry(float x, float y){
        ScatterData data = mChart.getData();
        if(data != null){
            ScatterDataSet set = (ScatterDataSet) data.getDataSetByIndex(0);
            if(set == null){
                set = createSet();
                data.addDataSet(set);
            }
            //Додаємо нове значення
            //data.addXValue
            data.addEntry(new Entry(x, y), 0);

            //робимо оновлення даних
            mChart.notifyDataSetChanged();
//            mChart.setVisibleXRange(10, 1);
//            mChart.setVisibleYRange(10, 1, YAxis.AxisDependency.LEFT);

            //прогортуємо на останній елемент
            //mChart.moveViewToX(data.getEntryCount());
        }
    }
//
    //метод створення
    private ScatterDataSet createSet(){
        ScatterDataSet set = new ScatterDataSet(null, "spl db");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        //forma
        set.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
        //rozmir znaczka
        set.setScatterShapeSize(10f);

        set.setColor(ColorTemplate.getHoloBlue());

        set.setValueTextColor(Color.argb(0,0,0,0));
        set.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return "" + value;
            }
        });
        set.setValueTextSize(10f);

        return set;
    }


}
