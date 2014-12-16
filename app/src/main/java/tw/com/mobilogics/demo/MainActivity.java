package tw.com.mobilogics.demo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import tw.com.mobilogics.demo.Util.Date;
import tw.com.mobilogics.demo.db.DBHelper;


public class MainActivity
            extends RoboActivity
            implements View.OnClickListener {

  @InjectView(R.id.tv_different_value) TextView mTvDifferentValue;
  @InjectView(R.id.tv_get) TextView mTvGet;
  @InjectView(R.id.tv_consumer) TextView mTvConsumer;
  @InjectView(R.id.btn_eating) Button mBtnEating;
  @InjectView(R.id.btn_motion) Button mBtnMotion;
  @InjectView(R.id.btn_record) Button mBtnRecord;
  @InjectView(R.id.btn_bmi) Button mBtnBmi;
  @InjectView(R.id.btn_target) Button mbtnTarget;

  SharedPreferences mSharedPref;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initConfig();
    mSharedPref = getSharedPreferences("init", Context.MODE_PRIVATE);

    if (!mSharedPref.getBoolean("isInit", false)) {
      mSharedPref.edit().putBoolean("isInit", true).commit();
      DBHelper.Table.ClassIfication.Creator c = new DBHelper.Table.ClassIfication.Creator();
      c.insertData(this, "五穀類", 1);
      c.insertData(this,  "肉類",  2);
      c.insertData(this,  "魚類",  3);
      c.insertData(this,  "菜類",  4);
      DBHelper.Table.FoodItems.Creator creator = new DBHelper.Table.FoodItems.Creator();
      creator.insertData(this, "白飯", 1, 263);
      creator.insertData(this, "米粉(乾)", 1, 360);
      creator.insertData(this, "蛋麵(乾)", 1, 372);
      creator.insertData(this, "通心麵(乾)", 1, 386);
      creator.insertData(this, "義大利麵", 1, 363);
      creator.insertData(this, "麵線", 1, 330);
      creator.insertData(this, "粉絲", 1, 350);
      creator.insertData(this, "燕麥片", 1, 389);
      creator.insertData(this, "牛肉", 2, 114);
      creator.insertData(this, "漢堡牛排", 2, 360);
      creator.insertData(this, "鹹牛肉", 2, 240);
      creator.insertData(this, "雞肉", 2, 100);
      creator.insertData(this, "燒雞肉", 2, 195);
      creator.insertData(this, "白斬雞", 2, 198);
      creator.insertData(this, "豬肉", 2, 123);
      creator.insertData(this, "煎豬排", 2, 451);
      creator.insertData(this, "燒豬排", 2, 317);
      creator.insertData(this, "香腸", 2, 326);
      creator.insertData(this, "火腿", 2, 389);
      creator.insertData(this, "羊肉", 2, 198);
      creator.insertData(this, "羊排", 2, 355);
      creator.insertData(this, "魚類(通用)", 3,   100);
      creator.insertData(this, "柳葉魚", 3,   95);
      creator.insertData(this, "鱈魚", 3,   75);
      creator.insertData(this, "秋刀魚", 3,   240);
      creator.insertData(this, "蝦肉", 3,   90);
      creator.insertData(this, "蜆肉", 3,   50);
      creator.insertData(this, "蟹肉", 3,   90);
      creator.insertData(this, "菜類(通用)", 4,   18);
      creator.insertData(this, "蔥", 4,   47);
      creator.insertData(this, "洋蔥", 4,   35);
      creator.insertData(this, "大蒜", 4,   40);
      creator.insertData(this, "白菜", 4,   17);
      creator.insertData(this, "花椰菜(熟)", 4,   15);
      creator.insertData(this, "番茄(熟)", 4,   20);
      creator.insertData(this, "苦瓜(熟)", 4,   12);
      creator.insertData(this, "青椒(熟)", 4,   14);
      creator.insertData(this, "紅蘿蔔(熟)", 4,   37);
      creator.insertData(this, "白蘿蔔(熟)", 4,   20);
      creator.insertData(this, "蕃薯(熟)", 4,   115);
      creator.insertData(this, "生菜", 4,   14);
      creator.insertData(this, "菠菜", 4,   19);
      creator.insertData(this, "豆芽菜", 4,   20);
      creator.insertData(this, "番茄", 4,   14);
      creator.insertData(this, "絲瓜", 4,   17);
      creator.insertData(this, "芋頭", 4,   94);
      creator.insertData(this, "海帶", 4,   36);
      creator.insertData(this, "豆腐", 4,   160);
      creator.insertData(this, "雞蛋", 4,   80);
      creator.insertData(this, "蛋白", 4,   15);
      creator.insertData(this, "蛋黃", 4,   65);
      creator.insertData(this, "皮蛋", 4,   160);
    }
  }

  private void initConfig() {
    mBtnEating.setOnClickListener(this);
    mBtnMotion.setOnClickListener(this);
    mBtnRecord.setOnClickListener(this);
    mBtnBmi.setOnClickListener(this);
    mbtnTarget.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_eating :
        startActivity(new Intent(this, EatingDialog.class));
        break;
      case R.id.btn_motion :
        startActivity(new Intent(this, MotionDialog.class));
        break;
      case R.id.btn_record :
        startActivity(new Intent(this, RecordActivity.class));
        break;
      case R.id.btn_bmi :
        startActivity(new Intent(this, BMIActivity.class));
        break;
      case R.id.btn_target :
        startActivity(new Intent(this, TargetActivity.class));
        break;
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    showPersonal();
  }

  private void showPersonal() {
    int get = 0;
    int loss = 0;
    String time = Date.getDate().split(" ")[0];
    Cursor cursor  = DBHelper.Table.getAllData(this, DBHelper.Table.Personal.tableName);
    while (cursor.moveToNext()) {
      String tmpTime = cursor.getString(cursor.getColumnIndex(DBHelper.Table.Personal.time)).split(" ")[0];
      if (time.equals(tmpTime)) {
        int calories = cursor.getInt(cursor.getColumnIndex(DBHelper.Table.Personal.calories));
        if (calories > 0) get += calories;
        else loss += calories;
      }
    } cursor.close();
    if (get == 0 && loss == 0) return;
    mTvGet.setTextColor(Color.BLUE);
    mTvGet.setText("\t" + String.valueOf(get));
    mTvConsumer.setTextColor(Color.RED);
    mTvConsumer.setText(String.valueOf("\t" + loss * -1));
    int result = get - loss * -1;
    if (result >= 0) {
      mTvDifferentValue.setTextColor(Color.BLUE);
    } else {
      mTvDifferentValue.setTextColor(Color.RED);
    }
    mTvDifferentValue.setText(String.valueOf(result));
  }
}
