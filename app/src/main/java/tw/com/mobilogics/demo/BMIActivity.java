package tw.com.mobilogics.demo;

import android.annotation.TargetApi;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

/**
 * Created by xuyouren on 14/12/13.
 */
public class BMIActivity extends RoboActivity implements View.OnClickListener {
  @InjectView(R.id.et_input_h) EditText mEtInputH;
  @InjectView(R.id.et_input_w) EditText mEtInputW;
  @InjectView(R.id.btn_calculate) Button mBtnCalculate;
  @InjectView(R.id.tv_result) TextView mTvResult;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_bmi);
    mBtnCalculate.setOnClickListener(this);
    Point point = new Point();
    getWindowManager().getDefaultDisplay().getSize(point);
    getWindow().getAttributes().width = point.x;
  }

  @Override
  public void onClick(View v) {
    try {
      double h = Double.parseDouble(mEtInputH.getText().toString());
      double w = Double.parseDouble(mEtInputW.getText().toString());
      if (HandleNull.getIsNull(h, w)) {
        showMsg();
        return;
      }
      h = Math.pow(h / 100d, 2);
      double bmi = w / h;
      String message = "你的BMI為 : " + String.valueOf(bmi).substring(0,4).concat("\n");
      if (bmi < 18.5) {
        message += "體重過輕";
      } else if (18.5 <= bmi && bmi < 24) {
        message += "健康體位";
      } else if (24 <= bmi && bmi < 27) {
        message += "過重";
      } else if (27 <= bmi && bmi < 30) {
        message += "輕度肥胖";
      } else if (30 <= bmi && bmi < 35) {
        message += "中度肥胖";
      } else if (bmi >= 35) {
        message += "重度肥胖";
      }
      setText(message).setVisibility(View.VISIBLE);
    } catch (NumberFormatException e) {
      showMsg();
      e.printStackTrace();
    }
  }

  private void showMsg() {
    Toast.makeText(this, "Input value is Invalid", Toast.LENGTH_LONG).show();
  }

  public TextView setText(String msg) {
    mTvResult.setText(msg);
    return mTvResult;
  }

  @TargetApi(19)
  public static class HandleNull {
    private static HandleNullImp mImp = new HandleNullImp() {
      @Override
      public boolean isNull(double height, double width) {
        if (height == 0) return true;
        if (width  == 0) return true;
        return false;
      }
    };
    public static boolean getIsNull(double height, double width) {
      return mImp.isNull(height, width);
    }
  }

  public interface HandleNullImp {
    public boolean isNull(double height, double width);
  }
}
