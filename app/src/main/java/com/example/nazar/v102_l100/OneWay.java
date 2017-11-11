package com.example.nazar.v102_l100;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class OneWay extends AppCompatActivity {
    ArrayList<XYValue> xyValueArray, xyValueArrayFiltr;


    String nameGraphics;
    int operation;
    DBFunctions dbF;
    ArrayList<Double> dataForGraph_Ax, dataForGraph_Ay, dataForGraph_Az,dataForGraph_Wz;

    private RelativeLayout graphLayout;
    private ScatterChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_way);

        /**
         * дані що приходять з бази даних
         * nameGraphics - для якої операції будуємо графік
         * operation - для якого досліду виконуємо дану побудову
         */
        Intent intent = getIntent();
        nameGraphics = intent.getStringExtra(AllGraphs.KEY_FOR_INTENT_GRAPH);
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
        //dataForGraph_Az = dbF.getArrayForGraphics(DBHelper.KEY_T1_Az, operation);
        dataForGraph_Wz = dbF.getArrayForGraphics(DBHelper.KEY_T1_Wz, operation);
        int StepTime = dbF.getStepTime(operation);
        dbF.close();


        //знаходимо головний екра активності
        graphLayout = (RelativeLayout) findViewById(R.id.activity_one_way);
        //Створюємо новий обєкт - графік
        mChart = new ScatterChart(this);
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
        x1.setDrawAxisLine(true);
        x1.setAxisLineColor(Color.BLACK);
        //малювання сітки для цієї осі
        x1.setDrawGridLines(false);

        //Значить що ми не даємо діаграмі першу і останню точку
        x1.setAvoidFirstLastClipping(true);

        //Беремо з графіка всі мітки і малюємо їх, це для осі У
        YAxis y1 = mChart.getAxisLeft(); //повертає ліву вісь
        y1.setTextColor(Color.BLACK);
        y1.setDrawZeroLine(true);
        y1.setZeroLineColor(Color.RED);
        y1.setDrawGridLines(true);      // сітка для цієї осі
        y1.setGridColor(Color.WHITE);

        YAxis y2 = mChart.getAxisRight(); //повертає праву вісь
        y2.setEnabled(false);             // виключаємо її
        ///Графік будується і відображається на екрані

        xyValueArray = new ArrayList<>();
        allPosition(StepTime, 2.5f, dataForGraph_Ax, dataForGraph_Ay, null, dataForGraph_Wz, xyValueArray);
//        float spead_axis_x, spead_axis_y;
//        float way_axis_x, way_axis_y;
//
//        float spead_axis_x_previous = 0;
//        float spead_axis_y_previous = 0;
//        float x_previous = 0;
//        float y_previous = 0;
///////////////////////////////////////////////////////////////////////////////////////////////////////////
//        int StepTime = 1000;
//        dataForGraph_Ax = new ArrayList<>(4);
//        dataForGraph_Ay = new ArrayList<>(4);
//        dataForGraph_Wz = new ArrayList<>(4);
//        for(int i=0; i<4;i++){
//            dataForGraph_Ax.add((double) 1);
//            dataForGraph_Ay.add((double) 1);
//        }
//        dataForGraph_Wz.add(0d);
//        dataForGraph_Wz.add(-45d);
//        dataForGraph_Wz.add(-90d);
//        dataForGraph_Wz.add((double)(-90-45));
//        xyValueArray.add(new XYValue(x.get(0), y.get(0)));
///////////////////////////////////////////////////////////////////////////////////////////////////////////

//        float time = (float)StepTime/1000;
//        xyValueArray.add(new XYValue(x_previous, y_previous));
//
//        for(int i = (int)(2.5/time); i < dataForGraph_Ax.size(); i++){
//            //Log.d("qqqqq", "x"+i+"="+x.get(i));
//            //Log.d("qqqqq", "y"+i+"="+y.get(i));
//            //Log.d("qqqqq", "Ax_"+i+"="+dataForGraph_Ax.get(i));
//            //Log.d("qqqqq", "Ay_"+i+"="+dataForGraph_Ay.get(i));
//
//            spead_axis_x = (float) (spead_axis_x_previous + dataForGraph_Ax.get(i) * time);
//            spead_axis_y = (float) (spead_axis_y_previous + dataForGraph_Ay.get(i) * time);
//            //Log.d("qqqqq", "spead_axis_x_previous_"+i+"="+spead_axis_x_previous);
//            //Log.d("qqqqq", "spead_axis_y_previous_"+i+"="+spead_axis_y_previous);
//            //Log.d("qqqqq", "spead_axis_x_"+i+"="+spead_axis_x);
//            //Log.d("qqqqq", "spead_axis_y_"+i+"="+spead_axis_y);
//
//            way_axis_x =(float)( spead_axis_x_previous * time + dataForGraph_Ax.get(i)*Math.pow(time, 2) / 2);
//            way_axis_y =(float)( spead_axis_y_previous * time + dataForGraph_Ay.get(i)*Math.pow(time, 2) / 2);
//            //Log.d("qqqqq", "------------------way_axis_x_"+i+"="+way_axis_x);
//            //Log.d("qqqqq", "------------------way_axis_y_"+i+"="+way_axis_y);
//
//            float x_next =(float)(  way_axis_x * Math.cos(Math.toRadians( dataForGraph_Wz.get(i) )) + way_axis_y * Math.sin(Math.toRadians( dataForGraph_Wz.get(i) )) );
//            float y_next =(float)(  way_axis_y * Math.cos(Math.toRadians( dataForGraph_Wz.get(i) )) - way_axis_x * Math.sin(Math.toRadians( dataForGraph_Wz.get(i) )) );
//            //Log.d("qqqqq", "Z_"+i+"="+dataForGraph_Wz.get(i));
//
//            //Log.d("qqqqq", "x_next"+(i)+"="+x_next);
//            //Log.d("qqqqq", "y_next"+(i)+"="+y_next);
//
//            spead_axis_x_previous = spead_axis_x;
//            spead_axis_y_previous = spead_axis_y;
//            x_previous += x_next;
//            y_previous += y_next;
//
//            //Filtracia{   }
//            xyValueArray.add(new XYValue(x_previous, y_previous));
//        }

        //Додамо графіку дані
        xyValueArray = sortArray(xyValueArray);
        for(int i = 0;i <xyValueArray.size(); i++){
            float x = xyValueArray.get(i).getX();
            float y = xyValueArray.get(i).getY();
            addEntry( x, y, 0, ColorTemplate.getHoloBlue(), "położenia w moment czasu");
        }

        //filtr
        xyValueArrayFiltr = new ArrayList<>();
        KalmanFilterSimple1D kalmanX = new KalmanFilterSimple1D(1, 7);
        kalmanX.setArrayForFiltr(dataForGraph_Ax);
        ArrayList<Double> dataForGraphFiltr_Ax = kalmanX.getArrayFiltr();

        KalmanFilterSimple1D kalmanY = new KalmanFilterSimple1D(1, 7);
        kalmanY.setArrayForFiltr(dataForGraph_Ay);
        ArrayList<Double> dataForGraphFiltr_Ay = kalmanY.getArrayFiltr();
        allPosition(StepTime, 2.5f, dataForGraphFiltr_Ax, dataForGraphFiltr_Ay, null, dataForGraph_Wz, xyValueArrayFiltr);

        xyValueArrayFiltr = sortArray(xyValueArrayFiltr);
        for(int i = 0;i <xyValueArrayFiltr.size(); i++){
            float x = xyValueArrayFiltr.get(i).getX();
            float y = xyValueArrayFiltr.get(i).getY();
            addEntry( x, y, 1, Color.RED, "Filtr q=1, r=7");
        }

    }

    private void allPosition(int StepTime, float cutTime, ArrayList<Double> d_Ax, ArrayList<Double> d_Ay, ArrayList<Double> d_Az, ArrayList<Double> d_Wz, ArrayList<XYValue> xyVA){
        float spead_axis_x, spead_axis_y;
        float way_axis_x, way_axis_y;

        float spead_axis_x_previous = 0;
        float spead_axis_y_previous = 0;
        float x_previous = 0;
        float y_previous = 0;

        float time = (float)StepTime/1000;
        xyVA.add(new XYValue(x_previous, y_previous));

        for(int i = (int)(cutTime/time); i < d_Ax.size(); i++){
//            Log.d("qqqqq", "Ax_"+i+"="+d_Ax.get(i));
//            Log.d("qqqqq", "Ay_"+i+"="+d_Ay.get(i));

            spead_axis_x = (float) (spead_axis_x_previous + d_Ax.get(i) * time);
            spead_axis_y = (float) (spead_axis_y_previous + d_Ay.get(i) * time);
//            Log.d("qqqqq", "spead_axis_x_previous_"+i+"="+spead_axis_x_previous);
//            Log.d("qqqqq", "spead_axis_y_previous_"+i+"="+spead_axis_y_previous);
//            Log.d("qqqqq", "spead_axis_x_"+i+"="+spead_axis_x);
//            Log.d("qqqqq", "spead_axis_y_"+i+"="+spead_axis_y);

            way_axis_x =(float)( spead_axis_x_previous * time + d_Ax.get(i)*Math.pow(time, 2) / 2);
            way_axis_y =(float)( spead_axis_y_previous * time + d_Ay.get(i)*Math.pow(time, 2) / 2);
//            Log.d("qqqqq", "------------------way_axis_x_"+i+"="+way_axis_x);
//            Log.d("qqqqq", "------------------way_axis_y_"+i+"="+way_axis_y);

            float x_next =(float)(  way_axis_x * Math.cos(Math.toRadians( d_Wz.get(i) )) + way_axis_y * Math.sin(Math.toRadians( d_Wz.get(i) )) );
            float y_next =(float)(  way_axis_y * Math.cos(Math.toRadians( d_Wz.get(i) )) - way_axis_x * Math.sin(Math.toRadians( d_Wz.get(i) )) );
//            Log.d("qqqqq", "Z_"+i+"="+d_Wz.get(i));
//
//            Log.d("qqqqq", "x_next"+(i)+"="+x_next);
//            Log.d("qqqqq", "y_next"+(i)+"="+y_next);

            spead_axis_x_previous = spead_axis_x;
            spead_axis_y_previous = spead_axis_y;
            x_previous += x_next;
            y_previous += y_next;

            //Filtracia{   }
            xyVA.add(new XYValue(x_previous, y_previous));
        }
    }

    //потрібно створити метод для додавання даних
    private void addEntry(float x, float y, int index, int color, String lable){
        ScatterData data = mChart.getData();
        if(data != null){
            ScatterDataSet set = (ScatterDataSet) data.getDataSetByIndex(index);
            if(set == null){
                set = createSet(color, lable);
                data.addDataSet(set);
            }
            //Додаємо нове значення
            data.addEntry(new Entry(x, y), index);
//            mChart.setVisibleXRange(25, 10);

            //робимо оновлення даних
            mChart.notifyDataSetChanged();
        }
    }
//
    //метод створення
    private ScatterDataSet createSet(int color, String lable){
        ScatterDataSet set = new ScatterDataSet(null, lable);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
        set.setScatterShapeSize(10f);
        set.setColor(color);

        set.setValueTextColor(Color.TRANSPARENT);
        set.setValueTextSize(10f);
        return set;
    }

    private ArrayList<XYValue> sortArray(ArrayList<XYValue> array){
        int factor = Integer.parseInt(String.valueOf(Math.round(Math.pow(array.size(),2))));
        int m = array.size()-1;
        int count = 0;
        ////Log.d("qqqqq", "sortArray: Sorting the XYArray.");

        while(true){
            m--;
            if(m <= 0){
                m = array.size() - 1;
            }
            ////Log.d("qqqqq", "sortArray: m = " + m);
            try{
                //print out the y entrys so we know what the order looks like
                ////Log.d(TAG, "sortArray: Order:");
                //for(int n = 0;n < array.size();n++){
                ////Log.d(TAG, "sortArray: " + array.get(n).getY());
                //}
                float tempY = array.get(m-1).getY();
                float tempX = array.get(m-1).getX();
                if(tempX > array.get(m).getX() ){
                    array.get(m-1).setY(array.get(m).getY());
                    array.get(m).setY(tempY);
                    array.get(m-1).setX(array.get(m).getX());
                    array.get(m).setX(tempX);
                }
                else if(tempY == array.get(m).getY()){
                    count++;
                    ////Log.d("qqqqq", "sortArray: count = " + count);
                }

                else if(array.get(m).getX() > array.get(m-1).getX()){
                    count++;
                    ////Log.d("qqqqq", "sortArray: count = " + count);
                }
                //break when factorial is done
                if(count == factor ){
                    break;
                }
            }catch(ArrayIndexOutOfBoundsException e){
                Log.e("qqqqq", "sortArray: ArrayIndexOutOfBoundsException. Need more than 1 data point to create Plot." +
                        e.getMessage());
                break;
            }
        }
        return array;
    }

    private class XYValue {
        private float x;
        private float y;

        XYValue(float x, float y) {
            this.x = x;
            this.y = y;
        }

        float getX() {
            return x;
        }

        void setX(float x) {
            this.x = x;
        }

        float getY() {
            return y;
        }

        void setY(float y) {
            this.y = y;
        }
    }
}
