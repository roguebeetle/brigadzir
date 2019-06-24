package org.mizgir.brigadzir;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Объявим переменные компонентов
    Button button;
    Button button2;
    TextView textView;
    EditText editText;
    String SearchText;


    //Переменная для работы с БД
    private DBHelper mDBHelper;
    private SQLiteDatabase mDb;
    Cursor crsr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);

        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(this);

        Log.d("brigadzir", "запуск");
        mDBHelper = new DBHelper(this);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }

        //Найдем компоненты в XML разметке

        mDBHelper.openDataBase();


        //Пропишем обработчик клика кнопки
            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.d("brigadzir", "начали");
                    EditText einText = findViewById(R.id.editText);
                    SearchText = einText.getText().toString();
                    try {
                        //@SuppressLint("Recycle")
                        crsr = mDb.rawQuery("SELECT * FROM objects WHERE Num_IVB='" +
                                SearchText + "' or Num_PPR1='" +
                                SearchText + "' or Num_PPR2='" +
                                SearchText + "' or Num_PPR3='" +
                                SearchText + "' or Num_TSP1='" +
                                SearchText + "' or Num_TSP2='" +
                                SearchText + "' or Num_TSP4='" +
                                SearchText + "' or Num_TSP5='" +
                                SearchText + "'", null, null);

                        crsr.moveToFirst();
                        Log.d("brigadzir", "извлекаем данные из курсора");
                        // извлекаем данные из курсора
                        String street = crsr.getString(2);
                        String ndom = crsr.getString(3);
                        String nkor = crsr.getString(4);
                        String jeu = crsr.getString(5);
                        String sys = crsr.getString(6);
                        String type = crsr.getString(13);
                        String ivb = crsr.getString(18);
                        String nppr1 = crsr.getString(20);
                        String nppr2 = crsr.getString(21);
                        String nppr3 = crsr.getString(22);
                        String du1 = crsr.getString(23);
                        String du2 = crsr.getString(24);
                        String du3 = crsr.getString(25);
                        String tsp1gvs = crsr.getString(28);
                        String tsp2gvs = crsr.getString(29);
                        String tsp1ot = crsr.getString(33);
                        String tsp2ot = crsr.getString(34);
                        String uchgvs = crsr.getString(39);
                        String uchot = crsr.getString(41);
                        String pov_s = crsr.getString(43);
                        String pov_f = crsr.getString(44);
                        String povtspgvs_s = crsr.getString(62);
                        String povtspgvs_f = crsr.getString(63);
                        String povtspot_s = crsr.getString(64);
                        String povtspot_f = crsr.getString(65);
                        crsr.close();
                        Log.d("brigadzir", "ГВС: " + tsp1gvs + " ОТ: " + tsp1ot);
                        textView.setText("ЖЭУ: " + jeu +
                                "\nАдрес: " + street + " " + ndom + " " + nkor + "" +
                                "\nТип учёта: " + sys + "" +
                                "\nТип прибора: " + type +
                                "\nИВБ: " + ivb +
                                "\nППР1 ГВС: " + nppr1 +
                                "\nППР2 ГВС: " + nppr2 +
                                "\nППР ОТ: " + nppr3 +
                                "\nППР1 ГВС ДУ: " + du1 +
                                "\nППР2 ГВС ДУ: " + du2 +
                                "\nППР ОТ ДУ: " + du3 +
                                "\n№ТСП1 ГВС: " + tsp1gvs +
                                "\n№ТСП2 ГВС: " + tsp2gvs +
                                "\n№ТСП1 ОТ: " + tsp1ot +
                                "\n№ТСП2 ОТ: " + tsp2ot +
                                "\nОТ НА УЧ С: " + uchgvs +
                                "\nГВС НА УЧ С: " + uchot +
                                "\nПОВ ИВБ: " + pov_s +
                                "\nСЛЕД ПОВ ИВБ: " + pov_f +
                                "\nПОВ ТСП ГВС НАЧ: " + povtspgvs_s +
                                "\nПОВ ТСП ГВС КОН: " + povtspgvs_f +
                                "\nПОВ ТСП ОТ НАЧ: " + povtspot_s +
                                "\nПОВ ТСП ОТ КОН: " + povtspot_f);

                    } catch (CursorIndexOutOfBoundsException e) {
                    }

                }

            });

    }

    //@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button2:
                Intent intent = new Intent(this, Tree.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}



