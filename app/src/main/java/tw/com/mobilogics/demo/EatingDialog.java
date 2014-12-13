package tw.com.mobilogics.demo;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import tw.com.mobilogics.demo.db.DBHelper;
import tw.com.mobilogics.demo.Util.Date;

/**
 * Created by xuyouren on 14/12/2.
 */
public class EatingDialog extends RoboActivity
                          implements Spinner.OnItemSelectedListener,
                                     View.OnClickListener {

  private final String TAG = EatingDialog.class.getName();

  @InjectView(R.id.sp_classification) Spinner mSpClassIfication;
  @InjectView(R.id.sp_items) Spinner mSpItems;
  @InjectView(R.id.listView) ListView mListView;
  @InjectView(R.id.btn_join_list) Button mBtnJoinList;
  @InjectView(R.id.btn_motion) Button mBtnMotion;
  @InjectView(R.id.tv_result) TextView mTvResult;
  @InjectView(R.id.btn_save) Button mBtnSave;

  private static String[] srrClassIfication;
  private static int[] arrClassInication;

  private HashMap<String, Integer> mMap = new HashMap();
  private MyAdapter mAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setTitle(getString(R.string.eating_dialog_title));
    setClassIficationData();
    setContentView(R.layout.dialog_eating);
    initConfig();
    initClassIfication();
  }

  private void initConfig() {
    mSpClassIfication.setOnItemSelectedListener(this);
    mBtnJoinList.setOnClickListener(this);
    mBtnMotion.setOnClickListener(this);
    mBtnSave.setOnClickListener(this);
    mListView.setAdapter(mAdapter = new MyAdapter(this, mMap));
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_join_list :
        int position = mSpItems.getSelectedItemPosition();
        String item = mSpItems.getItemAtPosition(position).toString();
        String name = item.split(":")[0];
        int calories = Integer.parseInt(item.split("熱量")[1]);
        mMap.put(name, calories);
        mListView.setAdapter(new MyAdapter(this, mMap));
        doCalculateCalories();
        break;
      case R.id.btn_motion :
        startActivity(new Intent(this, MotionDialog.class));
        break;
      case R.id.btn_save :
        if (mMap.size() <= 0) {
          Toast.makeText(this, "Please join data to list", Toast.LENGTH_LONG).show();
          return;
        }
        DBHelper.Table.Personal.Creator creator = new DBHelper.Table.Personal.Creator(this);
        long i = creator.insertData(Date.getDate(), doCalculateCalories());
        String msg = (i <= 0) ? "Save Error" : "Save Successful";
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        mMap.clear();
        mAdapter.notifyDataSetChanged();
        break;
    }
  }

  private int doCalculateCalories() {
    int sum = 0;
    for (Map.Entry entry : mMap.entrySet()) {
      sum += (int)entry.getValue();
    }
    mTvResult.setText("熱量" + String.valueOf(sum));
    return sum;
  }

  @Override
  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    switch (parent.getId()) {
      case R.id.sp_classification :
        setSpItemsData(arrClassInication[position]);
        break;
      case R.id.sp_items :
        break;
    }
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {

  }

  private void setClassIficationData() {
    int i = 0;
    Cursor cursor = DBHelper.Table.getAllData(this, DBHelper.Table.ClassIfication.tableName);
    srrClassIfication = new String[cursor.getCount()];
    arrClassInication = new int[cursor.getCount()];
    while (cursor.moveToNext()) {
      srrClassIfication[i] = cursor.getString(DBHelper.Table.ClassIfication.index_name);
      arrClassInication[i] = cursor.getInt(DBHelper.Table.ClassIfication.index_group);
      i++;
    }
    cursor.close();
  }

  private void setSpItemsData(int group) {
    DBHelper.Table.FoodItems.Creator creator = new DBHelper.Table.FoodItems.Creator();
    Cursor cursor = creator.queryData(this, group);
    String tmpSrr[] = new String[cursor.getCount()];
    int i = 0;
    while (cursor.moveToNext()) {
      String name = cursor.getString(cursor.getColumnIndex(DBHelper.Table.FoodItems.name));
      int calories = cursor.getInt(cursor.getColumnIndex(DBHelper.Table.FoodItems.calories));
      tmpSrr[i] = name + ": 熱量" + calories;
      i++;
    }
    cursor.close();
    mSpItems.setAdapter(new ArrayAdapter (
      this, R.layout.spinner_item, tmpSrr)
    );
  }

  private void initClassIfication() {
    mSpClassIfication.setAdapter (new ArrayAdapter (
      this, R.layout.spinner_item, srrClassIfication
    ));
  }

}
