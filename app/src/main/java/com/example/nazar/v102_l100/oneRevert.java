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
    ArrayList<Double> dataArray_Ax_oYXo, dataArray_Ay_oYXo, dataArray_Az_oYXo;
    ArrayList<Double> dataArray_Ax_oYXZo, dataArray_Ay_oYXZo, dataArray_Az_oYXZo;

    ArrayList<Double> A_oYXo, A_oYXo__x, A_oYXo__y;
    ArrayList<Double> V_oYXo, V_oYXo__x, V_oYXo__y, V_oYXo__xy;
    ArrayList<Double> S_oYXo, S_oYXo__x, S_oYXo__y, S_oYXo__xy;

    String answer;
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
        int StepTime = dbF.getStepTime(operation);
        dbF.close();

//        txt_1.setText("Vidbulysia zminy" + name_operation + "  " + operation);
        //powrot wokol OY, dataArray_Ay=const
        //powrot przeciw wskazowki zegarka
        // [x']   [x][cosQ,  0, -sinQ]   [ x*cosQ - z*sinQ ]
        // [y'] = [y][0,     1,     0] = [        y        ]
        // [z']   [z][sinQ,  0,  cosQ]   [ x*sinQ + z*cosQ ]
        // dataArray_Wy -> Q

//        Log.d("qqqqq", "3_3__" + Math.cos(Math.toRadians(60)) +"");
//        Log.d("qqqqq", "lenght_AX_1_Start_" + dataArray_Ax.size() +"");
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

//        Log.d("qqqqq", "lenght_AX_oYo_" + dataArray_Ax_oYo.size() +"");
//        for(int i = 0; i < dataArray_Ax_oYo.size(); i++){
//            Log.d("qqqqq", "AX_Start_"+i+"="+dataArray_Ax.get(i) + "{kat(oy)= }"+ dataArray_Wy.get(i) + "---AX_o_Y_o__"+i+"="+dataArray_Ax_oYo.get(i));
//            Log.d("qqqqq", "AY_Start_"+i+"="+dataArray_Ay.get(i) + "{kat(oy)= }"+ dataArray_Wy.get(i) + "---AY_o_Y_o__"+i+"="+dataArray_Ay_oYo.get(i));
//            Log.d("qqqqq", "AZ_Start_"+i+"="+dataArray_Az.get(i) + "{kat(oy)= }"+ dataArray_Wy.get(i) + "---AZ_o_Y_o__"+i+"="+dataArray_Az_oYo.get(i));
//            Log.d("qqqqq", "-----------------------------");
//        }


        ///part 2

        //powrot wokol OX, dataArray_Ax=const
        //powrot przeciw wskazowki zegarka
        // [x'']   [x'][1,    0,      0]   [         x         ]
        // [y''] = [y'][0,  cosP, -sinP] = [ y'*cosP - z'*sinP ]
        // [z'']   [z'][0,  sinP,  cosP]   [ y'*sinP + z'*cosP ]
        // dataArray_Wx -> P

//        Log.d("qqqqq", "3_3__" + Math.cos(Math.toRadians(60)) +"");
        Log.d("qqqqq", "lenght_AX_2_Start_" + dataArray_Ax_oYo.size() +"");
        dataArray_Ax_oYXo = new ArrayList<>();
        dataArray_Ay_oYXo = new ArrayList<>();
        dataArray_Az_oYXo = new ArrayList<>();
        for(int i = 0; i < dataArray_Ax_oYo.size(); i++){
            double tmp_Ax, tmp_Ay, tmp_Az;
            tmp_Ax = dataArray_Ax_oYo.get(i);
            tmp_Ay = dataArray_Ay_oYo.get(i)*Math.cos(Math.toRadians(dataArray_Wx.get(i))) - dataArray_Az_oYo.get(i)*Math.sin(Math.toRadians(dataArray_Wx.get(i)));
            tmp_Az = dataArray_Ay_oYo.get(i)*Math.sin(Math.toRadians(dataArray_Wx.get(i))) + dataArray_Az_oYo.get(i)*Math.cos(Math.toRadians(dataArray_Wx.get(i)));
//            Log.d("qqqqq", "i=" + i+"");
//            Log.d("qqqqq", "tmp_Ax=" + tmp_Ax+"");
//            Log.d("qqqqq", "tmp_Ay=" + tmp_Ay+"");
//            Log.d("qqqqq", "tmp_Az=" + tmp_Az+"");
//            Log.d("qqqqq", "-------------------");
            dataArray_Ax_oYXo.add(tmp_Ax);
            dataArray_Ay_oYXo.add(tmp_Ay);
            dataArray_Az_oYXo.add(tmp_Az);
        }

//        Log.d("qqqqq", "-----------------------------");
//        Log.d("qqqqq", "-----------------------------");
//        Log.d("qqqqq", "-----------------------------");
//        Log.d("qqqqq", "lenght_AX_oYXo_" + dataArray_Ax_oYXo.size() +"");
//        for(int i = 0; i < dataArray_Ax_oYo.size(); i++){
//            Log.d("qqqqq", "AX_Start_"+i+"="+dataArray_Ax.get(i) + "{kat(oy)= }"+ dataArray_Wy.get(i) + "---AX_o_Y_o__"+i+"="+dataArray_Ax_oYo.get(i)  + "{kat(ox)= }"+ dataArray_Wx.get(i) + "---AX_o_YZ_o__"+i+"="+dataArray_Ax_oYXo.get(i));
//            Log.d("qqqqq", "AY_Start_"+i+"="+dataArray_Ay.get(i) + "{kat(oy)= }"+ dataArray_Wy.get(i) + "---AY_o_Y_o__"+i+"="+dataArray_Ay_oYo.get(i)  + "{kat(ox)= }"+ dataArray_Wx.get(i) + "---AY_o_YZ_o__"+i+"="+dataArray_Ay_oYXo.get(i));
//            Log.d("qqqqq", "AZ_Start_"+i+"="+dataArray_Az.get(i) + "{kat(oy)= }"+ dataArray_Wy.get(i) + "---AZ_o_Y_o__"+i+"="+dataArray_Az_oYo.get(i)  + "{kat(ox)= }"+ dataArray_Wx.get(i) + "---AZ_o_YZ_o__"+i+"="+dataArray_Az_oYXo.get(i));
//            Log.d("qqqqq", "-----------------------------");
//        }


        ///part 3

        //powrot wokol OZ, dataArray_Az=const
        //powrot przeciw wskazowki zegarka
        // [x''']   [x''][cosK, -sinK, 0]   [  x''*cosK - y''*sinK ]
        // [y'''] = [y''][sinK,  cosK, 0] = [  x''*sinK + y''*cosK ]
        // [z''']   [z''][0,      0,   1]   [           z          ]
        // dataArray_Wz -> K

//        Log.d("qqqqq", "3_3__" + Math.cos(Math.toRadians(60)) +"");
        Log.d("qqqqq", "lenght_AX_3_Start_" + dataArray_Ax_oYXo.size() +"");
        dataArray_Ax_oYXZo = new ArrayList<>();
        dataArray_Ay_oYXZo = new ArrayList<>();
        dataArray_Az_oYXZo = new ArrayList<>();
        for(int i = 0; i < dataArray_Ax_oYo.size(); i++){
            double tmp_Ax, tmp_Ay, tmp_Az;
            tmp_Ax = dataArray_Ax_oYXo.get(i)*Math.cos(Math.toRadians(dataArray_Wz.get(i))) - dataArray_Ay_oYXo.get(i)*Math.sin(Math.toRadians(dataArray_Wz.get(i)));
            tmp_Ay = dataArray_Ax_oYXo.get(i)*Math.sin(Math.toRadians(dataArray_Wz.get(i))) + dataArray_Ay_oYXo.get(i)*Math.cos(Math.toRadians(dataArray_Wz.get(i)));
            tmp_Az = dataArray_Az_oYXo.get(i);
//            Log.d("qqqqq", "i=" + i+"");
//            Log.d("qqqqq", "tmp_Ax=" + tmp_Ax+"");
//            Log.d("qqqqq", "tmp_Ay=" + tmp_Ay+"");
//            Log.d("qqqqq", "tmp_Az=" + tmp_Az+"");
//            Log.d("qqqqq", "-------------------");
            dataArray_Ax_oYXZo.add(tmp_Ax);
            dataArray_Ay_oYXZo.add(tmp_Ay);
            dataArray_Az_oYXZo.add(tmp_Az);
        }

//        Log.d("qqqqq", "-----------------------------");
//        Log.d("qqqqq", "-----------------------------");
//        Log.d("qqqqq", "-----------------------------");
//        Log.d("qqqqq", "lenght_AX_oYXo_" + dataArray_Ax_oYXZo.size() +"");
//        for(int i = 0; i < dataArray_Ax_oYXZo.size(); i++){
//            Log.d("qqqqq", "AX_Start_"+i+"="+dataArray_Ax.get(i) + "{kat(oz)= }"+ dataArray_Wz.get(i) + "---AX_o_YXZ_o__"+i+"="+dataArray_Ax_oYXZo.get(i)  );
//            Log.d("qqqqq", "AY_Start_"+i+"="+dataArray_Ay.get(i) + "{kat(oz)= }"+ dataArray_Wz.get(i) + "---AY_o_YXZ_o__"+i+"="+dataArray_Ay_oYXZo.get(i)  );
//            Log.d("qqqqq", "AZ_Start_"+i+"="+dataArray_Az.get(i) + "{kat(oz)= }"+ dataArray_Wz.get(i) + "---AZ_o_YXZ_o__"+i+"="+dataArray_Az_oYXZo.get(i)  );
//            Log.d("qqqqq", "-----------------------------");
//        }

        //po obrotach

        //dataArray_Ax_oYXZo, dataArray_Ay_oYXZo, dataArray_Az_oYXZo - przespirz.
        // przyspierzenia w płaszczyznie


        A_oYXo = new ArrayList<>(); A_oYXo__x = new ArrayList<>(); A_oYXo__y = new ArrayList<>();
        V_oYXo = new ArrayList<>(); V_oYXo__x = new ArrayList<>(); V_oYXo__y = new ArrayList<>(); V_oYXo__xy = new ArrayList<>();
        S_oYXo = new ArrayList<>(); S_oYXo__x = new ArrayList<>(); S_oYXo__y = new ArrayList<>(); S_oYXo__xy = new ArrayList<>();
        double time = (double)StepTime/1000;

        for(int i = 0; i < dataArray_Ax_oYXZo.size(); i++){
            double tmp_a;
            tmp_a = Math.sqrt( Math.pow(dataArray_Ax_oYXZo.get(i), 2) + Math.pow(dataArray_Ay_oYXZo.get(i), 2) );
            A_oYXo.add(tmp_a);
            A_oYXo__x.add( dataArray_Ax_oYXZo.get(i) );
            A_oYXo__y.add( dataArray_Ay_oYXZo.get(i) );
        }
        for(int i = 0; i < (int)(3/time); i++){
            A_oYXo.set(i, (double) 0); A_oYXo__x.set(i, (double) 0); A_oYXo__y.set(i, (double) 0);
        }

        /*
        filtracia 1.

        int elemets = dataArray_Ax_oYXZo.size();
        double sum_acc_x = 0;
        double sum_acc_y = 0;
        int count = 0;
        for(int i = (int)(3/time); i < elemets; i++){
            count = count + 1;
            sum_acc_x = sum_acc_x + A_oYXo__x.get(i);
            sum_acc_y = sum_acc_y + A_oYXo__y.get(i);
        }
        double delta_x = sum_acc_x/count;
        double delta_y = sum_acc_y/count;

        Log.d("qqqqq", "count=" + count);
        Log.d("qqqqq", "sum_acc_x=" + sum_acc_x);
        Log.d("qqqqq", "sum_acc_y=" + sum_acc_y);
        Log.d("qqqqq", "delta_x=" + delta_x);
        Log.d("qqqqq", "delta_y=" + delta_y);

//        2478( 20ms )
//        delta_x_1=-7.724919112772673E-4
//        delta_y_1=-2.0027909143313777E-4

//        2478( 20ms )
//        delta_x_2= 9.600290113382426E-4
//        delta_y_2= -4.911981332914299E-4

//        3759( 20ms )
//        delta_x_2= 2.6137925771948443E-4
//        delta_y_2= 9.151073332992411E-5
*/
        double delta_x = (-7.724919112772673E-4 + 9.600290113382426E-4 + 2.613792577948443E-4)/3;
        double delta_y = (-2.027909143313777E-4 - 4.911981332914299E-4 + 9.151073332992411E-5)/3;
        for(int i = (int)(3/time); i < dataArray_Ax_oYXZo.size(); i++){
           double tmp_x = A_oYXo__x.get(i) - delta_x;
            A_oYXo__x.set(i, tmp_x);

            double tmp_y = A_oYXo__y.get(i) - delta_y;
            A_oYXo__y.set(i, tmp_y);

//            Log.d("qqqqq", "A_oYXo__x=\t" + A_oYXo__x.get(i));
//            Log.d("qqqqq", "A_oYXo__y=\t" + A_oYXo__y.get(i));
//            Log.d("qqqqq", "------");
        }


        /*
koly pryskorennia vid 0 do 0.03 to pryjmajem stan spokoju, dlia cogo maje buty 25povturen ( pry 20ms)
        int nol_x = 0;
        int nolnol_odyn_x = 0;
        int nolnol_dwa_x = 0;
        int nolnol_try_x = 0;
        int wyszcze_x = 0;
        for(int i = (int)(3/time); i < dataArray_Ax_oYXZo.size(); i++){
            if ( Math.abs( A_oYXo__x.get(i) ) < 0.01 ) nol_x = nol_x + 1;
            if ( 0.01 <= Math.abs( A_oYXo__x.get(i) ) && Math.abs( A_oYXo__x.get(i) ) < 0.02 ) nolnol_odyn_x = nolnol_odyn_x + 1;
            if ( 0.02 <= Math.abs( A_oYXo__x.get(i) ) && Math.abs( A_oYXo__x.get(i) ) < 0.03 ) nolnol_dwa_x = nolnol_dwa_x + 1;
            if ( 0.03 <= Math.abs( A_oYXo__x.get(i) ) && Math.abs( A_oYXo__x.get(i) ) < 0.04 ) nolnol_try_x = nolnol_try_x + 1;
            if ( 0.04 <= Math.abs( A_oYXo__x.get(i) )  ) wyszcze_x = wyszcze_x + 1;
        }
        int nol_y = 0;
        int nolnol_odyn_y = 0;
        int nolnol_dwa_y = 0;
        int nolnol_try_y = 0;
        int wyszcze_y = 0;
        for(int i = (int)(3/time); i < dataArray_Ax_oYXZo.size(); i++){
            if ( Math.abs( A_oYXo__y.get(i) ) < 0.01 ) nol_y = nol_y + 1;
            if ( 0.01 <= Math.abs( A_oYXo__y.get(i) ) && Math.abs( A_oYXo__y.get(i) ) < 0.02 ) nolnol_odyn_y = nolnol_odyn_y + 1;
            if ( 0.02 <= Math.abs( A_oYXo__y.get(i) ) && Math.abs( A_oYXo__y.get(i) ) < 0.03 ) nolnol_dwa_y = nolnol_dwa_y + 1;
            if ( 0.03 <= Math.abs( A_oYXo__y.get(i) ) && Math.abs( A_oYXo__y.get(i) ) < 0.04 ) nolnol_try_y = nolnol_try_y + 1;
            if ( 0.04 <= Math.abs( A_oYXo__y.get(i) )  ) wyszcze_y = wyszcze_y + 1;
        }
        int allNol_x = nol_x + nolnol_odyn_x + nolnol_dwa_x + nolnol_try_x + wyszcze_x;
        Log.d("qqqqq", "x=\t" + allNol_x + "   procent = " + allNol_x*100/allNol_x);
        Log.d("qqqqq", "nol_x=\t" + nol_x + "   procent = " + nol_x*100/allNol_x);
        Log.d("qqqqq", "nolnol_odyn_x=\t" + nolnol_odyn_x+ "   procent = " + nolnol_odyn_x*100/allNol_x);
        Log.d("qqqqq", "nolnol_dwa_x=\t" + nolnol_dwa_x+ "   procent = " + nolnol_dwa_x*100/allNol_x);
        Log.d("qqqqq", "nolnol_try_x=\t" + nolnol_try_x+ "   procent = " + nolnol_try_x*100/allNol_x);
        Log.d("qqqqq", "wyszcze_x=\t" + wyszcze_x+ "   procent = " + wyszcze_x*100/allNol_x);
        Log.d("qqqqq", "------");

        int allNol_y = (nol_y + nolnol_odyn_y + nolnol_dwa_y + nolnol_try_y + wyszcze_y);
        Log.d("qqqqq", "y=\t" + allNol_y+ "   procent = " + allNol_y*100/allNol_y);
        Log.d("qqqqq", "nol_y=\t" + nol_y+"   procent = " + nol_y*100/allNol_y);
        Log.d("qqqqq", "nolnol_odyn_y=\t" + nolnol_odyn_y + "   procent = " + nolnol_odyn_y*100/allNol_y);
        Log.d("qqqqq", "nolnol_dwa_y=\t" + nolnol_dwa_y + "   procent = " + nolnol_dwa_y*100/allNol_y);
        Log.d("qqqqq", "nolnol_try_y=\t" + nolnol_try_y + "   procent = " + nolnol_try_y*100/allNol_y);
        Log.d("qqqqq", "wyszcze_y=\t" + wyszcze_y+"   procent = " + wyszcze_y*100/allNol_y);
        Log.d("qqqqq", "------");

//        com.example.nazar.v102_l100 D/qqqqq: x=	3609   procent = 100
//        com.example.nazar.v102_l100 D/qqqqq: nol_x=	1814   procent = 50
//        com.example.nazar.v102_l100 D/qqqqq: nolnol_odyn_x=	1052   procent = 29
//        com.example.nazar.v102_l100 D/qqqqq: nolnol_dwa_x=	375   procent = 10       <0.03
//        com.example.nazar.v102_l100 D/qqqqq: nolnol_try_x=	190   procent = 5
//        com.example.nazar.v102_l100 D/qqqqq: wyszcze_x=	178   procent = 4
//        com.example.nazar.v102_l100 D/qqqqq: ------
//        com.example.nazar.v102_l100 D/qqqqq: y=	3609   procent = 100
//        com.example.nazar.v102_l100 D/qqqqq: nol_y=	1674   procent = 46
//        com.example.nazar.v102_l100 D/qqqqq: nolnol_odyn_y=	990   procent = 27
//        com.example.nazar.v102_l100 D/qqqqq: nolnol_dwa_y=	643   procent = 17       <0.03
//        com.example.nazar.v102_l100 D/qqqqq: nolnol_try_y=	141   procent = 3
//        com.example.nazar.v102_l100 D/qqqqq: wyszcze_y=	161   procent = 4
//        com.example.nazar.v102_l100 D/qqqqq: ------
*/

        V_oYXo__x.add((double) 0);
        V_oYXo__y.add((double) 0);
        int countPos_vX = 0;
        int countPos_vY = 0;
        int countPos_not_null = 0;
        //V_oYXo__xy.add((double) 0);
        for(int i = 0; i < A_oYXo__x.size() - 1; i++) {

            double tmp_v__x;
            double v_poc__x;
            boolean spok_x = false;
            if ( Math.abs(A_oYXo__x.get(i)) < 0.03 ){
                if( (i+24) < A_oYXo__x.size() ) {
                    int count = 0;
                    for (int jj = 0; jj < 25; jj++) {
                        if (Math.abs(A_oYXo__x.get(i + jj)) < 0.03) {
                            count = count + 1;
                        } else {
                            count = 0;
                            break;
                        }
                    }
                    if (count == 25) {
                        spok_x = true;
                    } else {
                        if (i - 24 >= 0) {
                            int countB = 0;
                            for (int jj = 0; jj < 25; jj++) {
                                if (Math.abs(A_oYXo__x.get(i - jj)) < 0.03) {
                                    countB = countB + 1;
                                } else {
                                    countB = 0;
                                    break;
                                }
                            }
                            if (countB == 25) {
                                spok_x = true;
                            }
                        }
                    }
                }else{
                    if (i - 24 >= 0) {
                        int countB = 0;
                        for (int jj = 0; jj < 25; jj++) {
                            if (Math.abs(A_oYXo__x.get(i - jj)) < 0.03) {
                                countB = countB + 1;
                            } else {
                                countB = 0;
                                break;
                            }
                        }
                        if (countB == 25) {
                            spok_x = true;
                        }
                    }
                }
            }

            if (i == 0) {
                v_poc__x = 0;
            } else {
                v_poc__x = V_oYXo__x.get(i - 1);
            }

            if(spok_x && (Math.abs(A_oYXo__x.get(i)) < 0.03)){
                tmp_v__x = 0;
            }else{
                countPos_vX = countPos_vX + 1 ;
                tmp_v__x = v_poc__x + time * A_oYXo__x.get(i) + time * ((A_oYXo__x.get(i + 1) - A_oYXo__x.get(i)) / 2);
            }



//////////////OY
            double tmp_v__y;
            double v_poc__y;
            boolean spok_y = false;

            if ( Math.abs(A_oYXo__y.get(i)) < 0.03 ){
                if( (i+24) < A_oYXo__y.size() ) {
                    int count = 0;
                    for (int jj = 0; jj < 25; jj++) {
                        if (Math.abs(A_oYXo__y.get(i + jj)) < 0.03) {
                            count = count + 1;
                        } else {
                            count = 0;
                            break;
                        }
                    }
                    if (count == 25) {
                        spok_y = true;
                    } else {
                        if (i - 24 >= 0) {
                            int countB = 0;
                            for (int jj = 0; jj < 25; jj++) {
                                if (Math.abs(A_oYXo__y.get(i - jj)) < 0.03) {
                                    countB = countB + 1;
                                } else {
                                    countB = 0;
                                    break;
                                }
                            }
                            if (countB == 25) {
                                spok_y = true;
                            }
                        }
                    }
                }else{
                    if (i - 24 >= 0) {
                        int countB = 0;
                        for (int jj = 0; jj < 25; jj++) {
                            if (Math.abs(A_oYXo__y.get(i - jj)) < 0.03) {
                                countB = countB + 1;
                            } else {
                                countB = 0;
                                break;
                            }
                        }
                        if (countB == 25) {
                            spok_y = true;
                        }
                    }
                }
            }
            if (i == 0) {
                v_poc__y = 0;
            } else {
                v_poc__y = V_oYXo__y.get(i - 1);
            }

            if(spok_y && (Math.abs(A_oYXo__y.get(i)) < 0.03)){
                tmp_v__y = 0;
            }else{
                countPos_vY = countPos_vY + 1 ;
                tmp_v__y = v_poc__y + time * A_oYXo__y.get(i) + time * ((A_oYXo__y.get(i + 1) - A_oYXo__y.get(i)) / 2);
            }

            if(tmp_v__x == 0 || tmp_v__y == 0){
                V_oYXo__x.add( (double) 0 );
                V_oYXo__y.add( (double) 0 );
            }else{
                countPos_not_null = countPos_not_null + 1;
                V_oYXo__x.add(tmp_v__x);
                V_oYXo__y.add(tmp_v__y);
            }

           //Log.d("qqqqq","V_oYXo__x=\t" + roundDouble(V_oYXo__x.get(i), 4)+  "\t\t\t\t) V_oYXo__y=\t\t\t\t" + roundDouble(V_oYXo__y.get(i), 4) );
        }
        Log.d("qqqqq", "countPos_vX = "+countPos_vX);
        Log.d("qqqqq", "countPos_vY = "+countPos_vY);
        Log.d("qqqqq", "countPos_not_null = "+countPos_not_null);

        for(int i = 0; i < A_oYXo.size(); i++){
            double tmp_v__xy;
            tmp_v__xy = Math.sqrt( Math.pow(V_oYXo__x.get(i), 2) + Math.pow(V_oYXo__y.get(i), 2) );
            V_oYXo__xy.add(tmp_v__xy);
//            Log.d("qqqqq", i + ") V_oYXo__x=\t" + roundDouble(V_oYXo__x.get(i), 4)+  ") V_oYXo__y=\t" + roundDouble(V_oYXo__y.get(i), 4)+ ") V_oYXo__xy=\t" + roundDouble(V_oYXo__xy.get(i), 4));
//            Log.d("qqqqq", "---------------");
        }





//        S_oYXo__x.add((double) 0);
//        S_oYXo__y.add((double) 0);
        S_oYXo__xy.add((double) 0);
        for(int i = 0; i < V_oYXo__xy.size() - 1; i++){;
//
//            double tmp_s__x;
//            tmp_s__x = time*V_oYXo__x.get(i) + time*((V_oYXo__x.get(i+1) - V_oYXo__x.get(i))/2);
//            S_oYXo__x.add(tmp_s__x);
//
//            double tmp_s__y;
//            tmp_s__y = time*V_oYXo__y.get(i) + time*((V_oYXo__y.get(i+1) - V_oYXo__y.get(i))/2);
//            S_oYXo__y.add(tmp_s__y);

            double tmp_s__xy;
            tmp_s__xy = time*V_oYXo__xy.get(i) + time*((V_oYXo__xy.get(i+1) - V_oYXo__xy.get(i))/2);
            S_oYXo__xy.add(tmp_s__xy);
        }

//        double all_way = 0;
//        double all_s__x_y = 0;
        double all_way__xy = 0;
        for(int i = 0; i < S_oYXo__xy.size(); i++){
//            Log.d("qqqqq", i + ") all_way=\t\t" + roundDouble(all_way, 4));
//            Log.d("qqqqq", i + ") all_way__xy=\t\t" + roundDouble(all_way__xy, 4));
//            Log.d("qqqqq", i + ") all_s__x_y=\t\t" + roundDouble(all_s__x_y, 4));
//            Log.d("qqqqq", "-------------------------");

//            all_way = all_way + S_oYXo.get(i);
            all_way__xy = all_way__xy + S_oYXo__xy.get(i);

//            double tmp_s__x_y ;
//            tmp_s__x_y = Math.sqrt( Math.pow(S_oYXo__x.get(i), 2) + Math.pow(S_oYXo__y.get(i), 2) );
//            all_s__x_y = all_s__x_y + tmp_s__x_y;
        }
        Log.d("qqqqq","all_way__xy=\t\t" + roundDouble(all_way__xy, 4));

        txt_1.setText("all_way__xy=\t\t" + roundDouble(all_way__xy, 4) );
    }
    private String roundDouble(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        if( (double)tmp / factor > 0 ){
            return " " + (double) tmp / factor;
        }else{
            return ""+(double) tmp / factor;
        }
    }
}
