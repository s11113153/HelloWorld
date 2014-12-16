package tw.com.mobilogics.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
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
  private AlertDialog.Builder mBuilder;

  // 彈出恭喜畫面時間
  private static final int SETTING_TIME_DURATION = 3;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_target);
    mBtnCalculate = (Button) findViewById(R.id.btn_calculate);
    mEtInputVal = (EditText) findViewById(R.id.et_input_val);
    mBtnCalculate.setOnClickListener(this);
    mTvMsg = (TextView) findViewById(R.id.tv_msg);
    mEtInputVal.setText("-1");
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

      if (val == 0) {
        showErrorMsg("請輸入正確體重");
        return;
      }
      if (sum > 0 && val > 0) {
        mTvMsg.setTextColor(Color.BLUE);
        int result = val - sum;
        if (result < 0) {
          msg = "你已達成";
          showCongratulation();
        }
        else msg = "你需要增加的卡路里為 : " + String.valueOf(result);
      } else if (sum > 0 && val < 0) {
        mTvMsg.setTextColor(Color.RED);
        int result = val * -1 + sum;
        msg = "你需要減少的卡路里為 : " + String.valueOf(result);
      }
      if (sum < 0 && val > 0) {
        mTvMsg.setTextColor(Color.BLUE);
        int result = sum * -1 + val;
        msg = "你需要增加的卡路里為 : " + String.valueOf(result);
      } else if (sum < 0 && val < 0) {
        mTvMsg.setTextColor(Color.RED);
        int result = (val * -1) - (sum * -1);
        if (result < 0) {
          msg = "你已達成目標";
          showCongratulation();
        }
        else msg = "你需要減少的卡路里為 : " + String.valueOf(result);
      }

      mTvMsg.setText(msg);
    } catch (Exception e) {
      mEtInputVal.setText("");
      showErrorMsg("輕輸入體重目標");
      e.printStackTrace();
    }
  }

  private void showErrorMsg(String msg) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
  }


  private void showCongratulation() {
    View view = getLayoutInflater().inflate(R.layout.toast, null);
    mBuilder = new AlertDialog.Builder(this);
    mBuilder.setView(view);
    final Dialog dialog = mBuilder.show();
    final Timer timer = Timer.getInstance();
    new Thread(new Runnable() {
      @Override
      public void run() {
        timer.doStart(new Timer.TimerGet() {
          @Override
          public void get(int status, long time) {
            if (status == START) {
              if (time >= SETTING_TIME_DURATION) {
                dialog.dismiss();
                timer.doExit(new Timer.TimerGet() {
                  @Override
                  public void get(int status, long time) {
                    //do nothing
                  }
                });
              }
            }
          }
        });
      }
    }).start();
  }
}
