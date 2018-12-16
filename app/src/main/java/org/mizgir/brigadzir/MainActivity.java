package org.mizgir.brigadzir;

import android.annotation.SuppressLint;
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

public class MainActivity extends AppCompatActivity {

    //Объявим переменные компонентов
    Button button;
    TextView textView;
    EditText editText;
    String SearchText;


    //Переменная для работы с БД
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("brigadzir", "запуск");
        mDBHelper = new DatabaseHelper(this);

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

        textView = (TextView) findViewById(R.id.textView);
        button = (Button) findViewById(R.id.button);
        mDBHelper.openDataBase();


        //Пропишем обработчик клика кнопки
            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.d("brigadzir", "начали");
                    EditText einText = (EditText) findViewById(R.id.editText);
                    SearchText = einText.getText().toString();

                    try {
                        @SuppressLint("Recycle") Cursor cursor = mDb.rawQuery("SELECT * FROM objects WHERE Num_IVB='" +
                                SearchText + "' or Num_PPR1='" +
                                SearchText + "' or Num_PPR2='" +
                                SearchText + "' or Num_PPR3='" +
                                SearchText + "' or Num_TSP1='" +
                                SearchText + "' or Num_TSP2='" +
                                SearchText + "' or Num_TSP4='" +
                                SearchText + "' or Num_TSP5='" +
                                SearchText + "'", null, null);

                        cursor.moveToFirst();
                        Log.d("brigadzir", "извлекаем данные из курсора");
                        // извлекаем данные из курсора
                        String street = cursor.getString(2);
                        String ndom = cursor.getString(3);
                        String nkor = cursor.getString(4);
                        String jeu = cursor.getString(5);
                        String sys = cursor.getString(6);
                        String type = cursor.getString(13);
                        String ivb = cursor.getString(18);
                        String nppr1 = cursor.getString(20);
                        String nppr2 = cursor.getString(21);
                        String nppr3 = cursor.getString(22);
                        String du1 = cursor.getString(23);
                        String du2 = cursor.getString(24);
                        String du3 = cursor.getString(25);
                        String tsp1gvs = cursor.getString(28);
                        String tsp2gvs = cursor.getString(29);
                        String tsp1ot = cursor.getString(33);
                        String tsp2ot = cursor.getString(34);
                        String uchgvs = cursor.getString(39);
                        String uchot = cursor.getString(41);
                        String pov_s = cursor.getString(43);
                        String pov_f = cursor.getString(44);
                        String povtspgvs_s = cursor.getString(62);
                        String povtspgvs_f = cursor.getString(63);
                        String povtspot_s = cursor.getString(64);
                        String povtspot_f = cursor.getString(65);
                        cursor.close();
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
}



