package tw.com.mobilogics.demo;

import android.text.format.Time;
import android.util.Log;

/**
 * Created by xuyouren on 14/12/9.
 */
public class Timer extends Thread {

  private static final Timer time = new Timer();
  private boolean mIsRunnging = false;

  public static Timer getInstance() {
    return time;
  }
  public void doStart(TimerGet gat) {
    mTimerGet = gat;
    mIsRunnging = true;
    run();
  }
  public void doExit(TimerGet gat) {
    mTimerGet = gat;
    mIsRunnging = false;
  }

  static private TimerGet mTimerGet;

  interface TimerGet {
    int START = 0;
    int EXIT = 1;
    void get(int status, long time);
  }


  @Override
  public void run() {
    long startTime = System.currentTimeMillis();
    try {
      while (mIsRunnging) {
        callBack (
          TimerGet.START,
          (long) Math.rint((System.currentTimeMillis() - startTime) / 1000)
        );
        if (!mIsRunnging) break;
        Thread.sleep(50);
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    callBack (
      TimerGet.EXIT,
      (long) Math.rint((System.currentTimeMillis() - startTime) / 1000)
    );
  }

  private void callBack(int status, long time) {
    mTimerGet.get(status, time);
  }
}
