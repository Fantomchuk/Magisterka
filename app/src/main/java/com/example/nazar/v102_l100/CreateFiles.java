package com.example.nazar.v102_l100;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CreateFiles extends AppCompatActivity {

    final String DIR_SD = "_Magister_Dziubak";
    int lastOperation;
    private ArrayList<MyOnePosition> positions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_files);

        DBFunctions dbF = new DBFunctions(this);
        dbF.open();
        lastOperation = dbF.lastOperationDataBase();
        positions = new ArrayList<>();
        positions = dbF.getAllPositions();
        dbF.close();

    }

    public void createF(View view) {
        //провіряємо чи є записи в базі даних
        if (positions.size() == 0 ){
            ((TextView) findViewById(R.id.tvInfo)).setText("Database is empty");
            return;
        }

        // провіряємо доступність зовнішньої памяті телефона
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            ((TextView) findViewById(R.id.tvInfo)).setText("Memory not available:" + Environment.getExternalStorageState());
            return;
        }
        // Отримуємо до неї шлях
        File sdPath = Environment.getExternalStorageDirectory();
        // добавлємо свою папку до шляху
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);

        //Якщо така папка існує, то видаляємо її повністю, і створюємо заново
        deletePath(sdPath);

        // створюємо свою папку
        sdPath.mkdirs();

        int index = 0;
        for(int i = 0; i <= lastOperation; i++) {
            // формуємо файл, який має шлях
            File sdFile = new File(sdPath, "positions_" + i +".txt");

            try {
                // відкриваємо потік на запис
                BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
                // записуємо

                bw.write("Ax, Ay, Az - Acceleration \n");
                bw.write("Wx, Wy, Wz - Position \n");
                bw.write("T - Time step in milliseconds \n");
                bw.write("O - Number of operations \n");
                bw.write("Ax|Ay|Az|Wx|Wy|Wz|T|O \n");

                while (positions.get(index).getOperation() == i){
                    bw.write(positions.get(index).getAx() + "|" + positions.get(index).getAy() + "|" +
                            positions.get(index).getAz() + "|" + positions.get(index).getWx() + "|" +
                            positions.get(index).getWy() + "|" + positions.get(index).getWz() + "|" +
                            positions.get(index).getStepTime() + "|" + positions.get(index).getOperation() + "\n");
                    index++;
                    if(index == positions.size()) {
                        break;
                    }
                }
                // закриваємо потік
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ((TextView) findViewById(R.id.tvInfo)).setText("Files created and recorded in:" + sdPath.getAbsolutePath());
    }

    public void deleteF(View view) {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            ((TextView) findViewById(R.id.tvInfo)).setText("Memory not available: "+ Environment.getExternalStorageState());
            return;
        }
        File sdPath = Environment.getExternalStorageDirectory();
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
        deletePath(sdPath);
        ((TextView) findViewById(R.id.tvInfo)).setText("All files have been deleted from the phone memory as well as the folder:" + sdPath.getAbsolutePath());

    }

    //для того, щоб видалити папку, потрібно видалити повністю все, що є в середині.
    //метод є рекурсивний, тому зітре все, навіть папки!
    public void deletePath(File file)
    {
        if(!file.exists())
            return;
        if(file.isDirectory())
        {
            for(File f : file.listFiles())
                deletePath(f);
            file.delete();
        }
        else
        {
            file.delete();
        }
    }
}
