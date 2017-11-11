package com.example.nazar.v102_l100;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.RelativeLayout;

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

import java.util.ArrayList;

public class FiltrKalman extends AppCompatActivity {
    DBFunctions dbF;
    ArrayList<Double> dataForGraph;
    private RelativeLayout graphLayout;
    private LineChart mChart;

    double q,r1,r2,r3,r4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtr_kalman);

        dbF = new DBFunctions(this);
        dbF.open();
        dataForGraph = dbF.getArrayForGraphics(DBHelper.KEY_T1_Ax, 0);
        dbF.close();

        //знаходимо головний екра активності
        graphLayout = (RelativeLayout) findViewById(R.id.activity_filtr_kalman);
        //Створюємо новий обєкт - графік
        mChart = new LineChart(this);
        //додаємо до головного вигляду
        graphLayout.addView(mChart, new AbsListView.LayoutParams
                (AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));
        //налаштовуэмо mChart
        Description des = new Description();
        des.setText("Filtr Kalmana");
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
        //origin
        for(int i =0; i < dataForGraph.size(); i++) {
            addEntry(Float.valueOf(String.valueOf(dataForGraph.get(i))), 0);
        }

        //q=0.01; r1=0.001; r2=0.01; r3=0.1; r4=1; //rys 1.1
        //q=0.01; r1=1; r2=10; r3=20; r4=50; //rys 1.2

        //q=0.1; r1=0.001; r2=0.01; r3=0.1; r4=1; //rys 2.1
        //q=0.1; r1=1; r2=10; r3=20; r4=50; //rys 1.2

        //q=1; r1=0.001; r2=0.01; r3=0.1; r4=1; //rys 3.1
        //q=1; r1=1; r2=10; r3=20; r4=50; //rys 3.2

        //q=10; r1=0.001; r2=0.01; r3=0.1; r4=1; //rys 4.1
        //q=10; r1=1; r2=10; r3=20; r4=50; //rys 4.2

        q=1; r1=1; r2=7; r3=14; r4=20; //rys 5


        //Filtr1
        KalmanFilterSimple1D kalman1 = new KalmanFilterSimple1D(q,r1);
        kalman1.setArrayForFiltr(dataForGraph);
        ArrayList<Double> arrayFiltr1 = kalman1.getArrayFiltr();
        for(int i =0; i < dataForGraph.size(); i++) {
            addEntry((Float.valueOf(String.valueOf(arrayFiltr1.get(i)))), 1);
        }

        //Filtr2
        KalmanFilterSimple1D kalman2 = new KalmanFilterSimple1D(q,r2);
        kalman2.setArrayForFiltr(dataForGraph);
        ArrayList<Double> arrayFiltr2 = kalman2.getArrayFiltr();
        for(int i =0; i < dataForGraph.size(); i++) {
            addEntry((Float.valueOf(String.valueOf(arrayFiltr2.get(i)))), 2);
        }

        //Filtr3
        KalmanFilterSimple1D kalman3 = new KalmanFilterSimple1D(q,r3);
        kalman3.setArrayForFiltr(dataForGraph);
        ArrayList<Double> arrayFiltr3 = kalman3.getArrayFiltr();
        for(int i =0; i < dataForGraph.size(); i++) {
            addEntry((Float.valueOf(String.valueOf(arrayFiltr3.get(i)))), 3);
        }

        //Filtr4
        KalmanFilterSimple1D kalman4 = new KalmanFilterSimple1D(q,r4);
        kalman4.setArrayForFiltr(dataForGraph);
        ArrayList<Double> arrayFiltr4 = kalman4.getArrayFiltr();
        for(int i =0; i < dataForGraph.size(); i++) {
            addEntry((Float.valueOf(String.valueOf(arrayFiltr4.get(i)))), 4);
        }

    }

    //потрібно створити метод для додавання даних
    private void addEntry(float y, int index){
        LineData data = mChart.getData();
        if(data != null){
            LineDataSet set = (LineDataSet) data.getDataSetByIndex(index);
            if(set == null){
                set = createSet(index);
                data.addDataSet(set);
            }
            //Додаємо нове значення
            //data.addXValue
            /**
             * y - значення яке приходить в функцію, це або кут або прискорення
             * х - беремо кількість побудованих пунктів, а так як рахунок йде від 0 то зразу
             *      додаємо його в якості першого параметру
             *      index - порядковий номер прямої
             */
            data.addEntry(new Entry(set.getEntryCount(), y), index);

            //робимо оновлення даних
            mChart.notifyDataSetChanged();
            mChart.setVisibleXRange(25, 10);

            //прогортуємо на останній елемент
            mChart.moveViewToX(data.getEntryCount());
        }
    }

    //метод створення
    private LineDataSet createSet(int index){
        LineDataSet set;
        if (index == 0){
            set = new LineDataSet(null, "oryginal");
            set.setFillColor(ColorTemplate.getHoloBlue());
            set.setColor(ColorTemplate.getHoloBlue());
            set.setDrawCircles(true);
            set.setCircleColor(ColorTemplate.getHoloBlue());
            set.setValueTextColor(Color.WHITE);
            set.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return "" + value;
                }
            });
            set.setValueTextSize(10f);
        }else if(index == 1){
            set = new LineDataSet(null, "filtr q="+q+"; r="+r1+"; ");
            set.setColor(Color.RED);
            set.setFillColor(Color.RED);
            set.setValueTextColor(Color.TRANSPARENT);
            set.setDrawCircles(false);
        }else if(index == 2){
            set = new LineDataSet(null, "filtr q="+q+"; r="+r2+"; ");
            set.setColor(Color.GREEN);
            set.setFillColor(Color.GREEN);
            set.setValueTextColor(Color.TRANSPARENT);
            set.setDrawCircles(false);
        }else if(index == 3){
            set = new LineDataSet(null, "filtr q="+q+"; r="+r3+"; ");
            set.setColor(Color.YELLOW);
            set.setFillColor(Color.YELLOW);
            set.setValueTextColor(Color.TRANSPARENT);
            set.setDrawCircles(false);
        }else{
            set = new LineDataSet(null, "filtr q="+q+", r="+r4+"; ");
            set.setColor(Color.BLACK);
            set.setFillColor(Color.BLACK);
            set.setValueTextColor(Color.TRANSPARENT);
            set.setDrawCircles(false);
        }

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setLineWidth(2f);
        set.setFillAlpha(65);
        set.setHighLightColor(Color.rgb(244, 0, 0));

        return set;
    }


}
