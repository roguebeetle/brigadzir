package org.mizgir.brigadzir;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.SimpleExpandableListAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AdapterHelper {


    private final String LOG_TAG = "myLogs";
    private static final String GET_JEU_LIST = "SELECT distinct Num_Jeu FROM objects ORDER BY Num_Jeu;";

    private DBHelper mDBHelper;
    private SQLiteDatabase mDb;

    private Cursor crsr;

    private final String ATTR_OBJECT_NAME = "objectName";
    private final String ATTR_ADDRESS_NAME = "addressName";


    // названия компаний (групп)
    private String[] groups;// = new String[] {"HTC", "Samsung", "LG"};

    // названия телефонов (элементов)
    String[] address;
    // коллекция для групп
    ArrayList<Map<String, String>> groupData;
    // коллекция для элементов одной группы
    ArrayList<Map<String, String>> childDataItem;
    // общая коллекция для коллекций элементов
    ArrayList<ArrayList<Map<String, String>>> childData;
    // в итоге получится childData = ArrayList<childDataItem>
    // список аттрибутов группы или элемента
    Map<String, String> m;

    Context ctx;

    AdapterHelper(Context _ctx) {
        ctx = _ctx;
    }

    SimpleExpandableListAdapter adapter;

    SimpleExpandableListAdapter getAdapter() {

        setJeu();

        // заполняем коллекцию групп из массива с названиями групп
        groupData = new ArrayList<Map<String, String>>();
        for (String group : groups) {
            // заполняем список аттрибутов для каждой группы
            m = new HashMap<String, String>();
            m.put(ATTR_OBJECT_NAME, group); // имя компании
            groupData.add(m);
        }

        // список аттрибутов групп для чтения
        String groupFrom[] = new String[]{ATTR_OBJECT_NAME};
        // список ID view-элементов, в которые будет помещены аттрибуты групп
        int groupTo[] = new int[]{android.R.id.text1};


        // создаем коллекцию для коллекций элементов
        childData = new ArrayList<ArrayList<Map<String, String>>>();
        for (int i = 0; i < groups.length; i++) {
            // создаем коллекцию элементов для первой группы
            childDataItem = new ArrayList<Map<String, String>>();
            // заполняем список аттрибутов для каждого элемента
            setAddress(groups[i]);
            for (String phone : address) {
                m = new HashMap<String, String>();
                m.put(ATTR_ADDRESS_NAME, phone); // название телефона
                Log.d(LOG_TAG, "item = " + phone);
                childDataItem.add(m);
            }

            childData.add(childDataItem);
        }

        // список аттрибутов элементов для чтения
        String childFrom[] = new String[]{ATTR_ADDRESS_NAME};
        // список ID view-элементов, в которые будет помещены аттрибуты элементов
        int childTo[] = new int[]{android.R.id.text1};

        adapter = new SimpleExpandableListAdapter(
                ctx,
                groupData,
                android.R.layout.simple_expandable_list_item_1,
                groupFrom,
                groupTo,
                childData,
                android.R.layout.simple_list_item_1,
                childFrom,
                childTo);

        return adapter;
    }


    String getGroupText(int groupPos) {
        return ((Map<String, String>) (adapter.getGroup(groupPos))).get(ATTR_OBJECT_NAME);
    }

    String getChildText(int groupPos, int childPos) {
        return ((Map<String, String>) (adapter.getChild(groupPos, childPos))).get(ATTR_ADDRESS_NAME);
    }

    String getGroupChildText(int groupPos, int childPos) {
        return getGroupText(groupPos) + " " + getChildText(groupPos, childPos);
    }

    String[] setJeu() {
        //-----------------------------------------------
        mDBHelper = new DBHelper(ctx);
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
        mDBHelper.openDataBase();
//------------------------------------------------
        crsr = mDb.rawQuery(GET_JEU_LIST, null, null);
        //Log.d(LOG_TAG, "rows count = " + crsr.getCount());

        crsr.moveToFirst();
        String[] str = new String[crsr.getCount()];
        for (int i = 0; crsr.isAfterLast() == false; i++) {

            // Log.d(LOG_TAG, "row = " + crsr.getString(0));
            str[i] = crsr.getString(0);
            crsr.moveToNext();
        }

        crsr.close();  // RIGHT: ensure resource is always recovered
        return groups = Arrays.copyOf(str, str.length);
    }

    String[] setAddress(String jeu) {
        //-----------------------------------------------
        mDBHelper = new DBHelper(ctx);
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
        mDBHelper.openDataBase();
//------------------------------------------------
        crsr = mDb.rawQuery("SELECT Adres_Doma, Num_Doma, Num_korp, Sistema, Data_Sled_Poverki_IVB, Data_Sled_Poverki_RSM, SL_POVERKA_TSP_GVS, SL_POVERKA_TSP_OT FROM objects Where Num_Jeu='" + jeu + "' ORDER BY Adres_Doma, Num_Doma, Num_korp, Sistema;", null, null);
        crsr.moveToFirst();
        String[] str = new String[crsr.getCount()];
        for (int i = 0; crsr.isAfterLast() == false; i++) {

            str[i] = (crsr.getString(0) + " " + crsr.getString(1) + " " + crsr.getString(2) + " " + crsr.getString(3)+"" +
                    "\n Поверка ИВБ: "+crsr.getString(4)+"\n Поверка РСМ: "+crsr.getString(5)+"\n Поверка ТСП ГВС: "+crsr.getString(6)+"\n Поверка ТСП ОТ: "+crsr.getString(7));
            crsr.moveToNext();
        }
        crsr.close();
//        for (int i = 0; i <str.length; i++) {
//            if (str[i] == "null") str[i]=".";
//        }

        return address = Arrays.copyOf(str, str.length);

    }
}