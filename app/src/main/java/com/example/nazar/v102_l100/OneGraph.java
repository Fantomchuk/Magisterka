package com.example.nazar.v102_l100;

import android.content.Intent;
import android.graphics.Color;
import android.icu.text.DecimalFormat;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

public class OneGraph extends AppCompatActivity {

    String nameGraphics;
    Boolean isRunMyService;
    int operation;
    ArrayList<Double> dataForGraph, dataForGraph_end;
    int lastIndexT1;
    DBFunctions dbF;
    Handler h;

    private RelativeLayout graphLayout;
    private LineChart mChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_graph);

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
         * dataForGraph - масив чисел по потрібній осі
         * lastIndexT1 - останній індекс в досліді на цей момент
         */
        dbF = new DBFunctions(this);
        dbF.open();
        dataForGraph = dbF.getArrayForGraphics(nameGraphics, operation);
        lastIndexT1 = dbF.lastIndexTable1();

        //знаходимо головний екра активності
        graphLayout = (RelativeLayout) findViewById(R.id.activity_one_graph);
        //Створюємо новий обєкт - графік
        mChart = new LineChart(this);
        //додаємо до головного вигляду
        graphLayout.addView(mChart, new AbsListView.LayoutParams
                (AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));
        //налаштовуэмо mChart
        Description des = new Description();
        des.setText("");
        mChart.setDescription(des);

        //включаємо виділення
        mChart.setHighlightPerDragEnabled(true);
        mChart.setTouchEnabled(true);

        //включаємо масштабування і перетягування
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        //сітка задньогофону
        mChart.setDrawGridBackground(false);

        //Вмикаю подвійний зум, щоб уникнути масштабування осі x і y окремо
        mChart.setPinchZoom(true);

        //Фон графіка
        mChart.setBackgroundColor(Color.LTGRAY);

        //настройка графіка закінчилася
        //попрацюємо з даними
        LineData data = new LineData();
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
        //Додамо графіку дані

        for(int i =0; i < dataForGraph.size(); i++) {
            addEntry(Float.valueOf(String.valueOf(dataForGraph.get(i))));
        }
        if(isRunMyService) {
            dataForGraph_end = new ArrayList<>();
            h = new Handler();
            Thread t = new Thread(new Runnable() {
                public void run() {
                    h.post(changedataForGraph);
                }
            });
            t.start();
        }
    }

    Runnable changedataForGraph = new Runnable() {
        @Override
        public void run() {
            dataForGraph_end =  dbF.getArrayForGraphicsToAnd(nameGraphics, operation, lastIndexT1);
            if(dataForGraph_end.size() != 0) {
                for (int i = 0; i < dataForGraph_end.size(); i++) {
                    addEntry(Float.valueOf(String.valueOf(dataForGraph_end.get(i))));
                }
            }
            lastIndexT1 = dbF.lastIndexTable1();
            h.postDelayed(changedataForGraph, 1000);
        }
    };


    //потрібно створити метод для додавання даних
    private void addEntry(float y){
        LineData data = mChart.getData();
        if(data != null){
            LineDataSet set = (LineDataSet) data.getDataSetByIndex(0);
            if(set == null){
                set = createSet();
                data.addDataSet(set);
            }
            //Додаємо нове значення
            //data.addXValue
            /**
             * y - значення яке приходить в функцію, це або кут або прискорення
             * х - беремо кількість побудованих пунктів, а так як рахунок йде від 0 то зразу
             *      додаємо його в якості першого параметру
             */
            data.addEntry(new Entry(set.getEntryCount(), y), 0);

            //робимо оновлення даних
            mChart.notifyDataSetChanged();
            mChart.setVisibleXRange(25, 10);

            //прогортуємо на останній елемент
            mChart.moveViewToX(data.getEntryCount());
        }
    }

    //метод створення
    private LineDataSet createSet(){
        LineDataSet set = new LineDataSet(null, nameGraphics);
        set.setDrawCircles(true);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(ColorTemplate.getHoloBlue());
        set.setLineWidth(2f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 0, 0));
        set.setValueTextColor(Color.WHITE);
        set.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return "" + value;
            }
        });
        set.setValueTextSize(10f);

        return set;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbF.close();
        if(isRunMyService)
            h.removeCallbacks(changedataForGraph);
    }
}
