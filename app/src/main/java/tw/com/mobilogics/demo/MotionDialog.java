package tw.com.mobilogics.demo;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import tw.com.mobilogics.demo.Util.Date;
import tw.com.mobilogics.demo.db.DBHelper;

/**
 * Created by xuyouren on 14/12/4.
 */
public class MotionDialog extends RoboActivity
  implements View.OnClickListener {

  @InjectView(R.id.btn_save) Button mBtnSave;
  @InjectView(R.id.sp_sport) Spinner mSpSports;
  @InjectView(R.id.et_consumer) EditText mEtConsumer;
  @InjectView(R.id.listView) ListView mListView;
  @InjectView(R.id.btn_join_list) Button mBtnJoinList;
  @InjectView(R.id.tv_result) TextView mTvResult;
  @InjectView(R.id.btn_timer) Button mBtnTimer;
  @InjectView(R.id.tv_show_time) TextView mTvShowTime;

  private final String TAG = MotionDialog.class.getName();
  private static final String[] sports = { "走路", "跑步", "騎腳踏車", "游泳", "跳繩", "下樓梯", "上樓梯" };
  private static final double [] consumers = { 3.5, 9.5, 4.5, 7d, 10d, 3d, 8d };

  private static List<String> mList = new ArrayList();
  private MotionAdapter mAdapter;
  private boolean mIsTimer = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dialog_motion);
    initConfig();
    initSportsData();
  }

  private void initConfig() {
    mBtnSave.setOnClickListener(this);
    mBtnJoinList.setOnClickListener(this);
    mBtnTimer.setOnClickListener(this);
    mListView.setAdapter(mAdapter = new MotionAdapter(this));
  }

  private void initSportsData() {
    mSpSports.setAdapter(new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, sports));
  }


  private void setTvResult() {
    int sum = 0;
    Iterator<String> iterator = mList.iterator();
    while (iterator.hasNext()) {
      sum += Double.parseDouble(iterator.next().split(",")[1]);
    }if (sum <=0) return;
    mTvResult.setText(String.valueOf(sum));
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_join_list :
        try {
          int minute = Integer.parseInt(mEtConsumer.getText().toString());
          int index = mSpSports.getSelectedItemPosition();
          double consumer = consumers[index] * minute;
          mList.add(String.valueOf(sports[index] + ", " + consumer));
          mAdapter.notifyDataSetChanged();
          setTvResult();
        } catch (NumberFormatException e) {
          Toast.makeText(this, "format error", Toast.LENGTH_LONG).show();
        }
        break;
      case R.id.btn_save :
        DBHelper.Table.Personal.Creator creator = new DBHelper.Table.Personal.Creator(this);
        Iterator<String> iterator = mList.iterator();
        int sum = 0;
        while (iterator.hasNext()) {
          sum -= Double.parseDouble(iterator.next().split(",")[1]);
        }
        if (sum != 0) {
          creator.insertData(Date.getDate(), sum);
          Toast.makeText(this, "Save Successful", Toast.LENGTH_LONG).show();
          mList.clear();
          mAdapter.notifyDataSetChanged();
          mTvResult.setText("");
        }
        else Toast.makeText(this, "Please input data!!", Toast.LENGTH_LONG).show();
        break;
      case R.id.btn_timer :
        final Timer timer = Timer.getInstance();
        if (!mIsTimer) {
          new Thread(new Runnable() {
            @Override
            public void run() {
              timer.doStart(new Timer.TimerGet() {
                @Override
                public void get(int status, final long time) {
                  if (status == START) {
                    doMainThread(new Runnable() {
                      @Override
                      public void run() {
                        mTvShowTime.setText(String.valueOf(time) + "sec");
                      }
                    });
                  }
                }
              });
            }
          }).start();
          mBtnTimer.setText(getString(R.string.stop_timing));
        } else {
          timer.doExit(new Timer.TimerGet() {
            @Override
            public void get(int status, final long time) {
              if (status == EXIT) {
                doMainThread(new Runnable() {
                  @Override
                  public void run() {
                    mTvShowTime.setText("end : " + String.valueOf(time)+ "sec");
                  }
                });
              }
            }
          });
          mBtnTimer.setText(getString(R.string.start_timing));
        }
        mIsTimer = ! mIsTimer;
        break;
    }
  }


  private void doMainThread(Runnable runnable) {
    this.runOnUiThread(runnable);
  }


  public static class MotionAdapter extends BaseAdapter {
    private Context mContext;
    public MotionAdapter(Context context) {
      mContext = context;
    }

    @Override
    public int getCount() {
      return mList.size();
    }

    @Override
    public Object getItem(int position) {
      return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      View view = View.inflate(mContext, R.layout.spinner_item, null);
      TextView textView = (TextView) view.findViewById(android.R.id.text1);
      textView.setTextColor(Color.BLUE);
      textView.setText(mList.get(position));
      return view;
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
    Timer.getInstance().doExit(new Timer.TimerGet() {
      @Override
      public void get(int status, long time) {
      }
    });
  }
}
