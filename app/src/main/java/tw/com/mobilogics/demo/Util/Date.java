package tw.com.mobilogics.demo.Util;

import java.text.SimpleDateFormat;

/**
 * Created by xuyouren on 14/12/9.
 */
public class Date {

  private Date() {
  }

  public static String getDate() {
    return new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new java.util.Date());
  }
}
