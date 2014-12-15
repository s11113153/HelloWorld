package tw.com.mobilogics.demo;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import tw.com.mobilogics.demo.db.DBHelper;

/**
 * Created by xuyouren on 14/12/15.
 */
public class TargetActivity extends Activity implements View.OnClickListener {
  private Button mBtnCalculate;
  private EditText mEtInputVal;
  private TextView mTvMsg;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_target);
    mBtnCalculate = (Button) findViewById(R.id.btn_calculate);
    mEtInputVal = (EditText) findViewById(R.id.et_input_val);
    mBtnCalculate.setOnClickListener(this);
    mTvMsg = (TextView) findViewById(R.id.tv_msg);
  }
  @Override
  public void onClick(View v) {
    try {
      int val = Integer.parseInt(mEtInputVal.getText().toString()) * 9000;
      Cursor cursor = DBHelper.Table.getAllData(this, DBHelper.Table.Personal.tableName);
      int sum = 0;
      String msg = "";
      while (cursor.moveToNext()) {
        int calories = cursor.getInt(cursor.getColumnIndex(DBHelper.Table.Personal.calories));
        sum += calories;
      }
      cursor.close();
      if (sum > 0 && val > 0) {
        mTvMsg.setTextColor(Color.BLUE);
        msg = "You must add calories : " + String.valueOf(val - sum);
      } else if (sum > 0 && val < 0) {
        mTvMsg.setTextColor(Color.RED);
        int result = val * -1 + sum;
        msg = "You must loss calories : " + String.valueOf(result);
      }
      if (sum < 0 && val > 0) {
        mTvMsg.setTextColor(Color.BLUE);
        int result = sum * -1 + val;
        msg = "You must add calories : " + String.valueOf(result);
      } else if (sum < 0 && val < 0) {
        mTvMsg.setTextColor(Color.RED);
        int result = (val * -1) - (sum * -1);
        msg = "You must loss calories : " + String.valueOf(result);
      }
      mTvMsg.setText(msg);
    } catch (Exception e) {
      mEtInputVal.setText("");
      e.printStackTrace();
    }
  }
}
