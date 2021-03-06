package tw.com.mobilogics.demo;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuyouren on 14/12/3.
 */
public class EatingAdapter extends BaseAdapter {

  private HashMap<String, Integer> mMap;
  private Context mContext;

  private String[] tmpName;
  private int[] tmpValue;

  private int[] quantity;

  public EatingAdapter(
    Context context, HashMap<String,
    Integer> map,
    HashMap<String, Integer> quantityMap
  ) {
    mMap = map;
    mContext = context;
    tmpName = new String[mMap.size()];
    tmpValue = new int[mMap.size()];
    quantity = new int[quantityMap.size()];
    int i = 0;
    for (Map.Entry entry : mMap.entrySet()) {
      tmpName[i] = entry.getKey().toString();
      tmpValue[i] = (int)entry.getValue();
      i++;
    }
    i = 0;
    for (Map.Entry entry : quantityMap.entrySet()) {
      quantity[i] = (int)entry.getValue();
      i++;
    }
  }

  @Override
  public int getCount() {
    return mMap.size();
  }

  @Override
  public Object getItem(int position) {
    return tmpName[position];
  }

  @Override
  public long getItemId(int position) {
    return position;
  }
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View view = View.inflate(mContext, android.R.layout.simple_list_item_1, null);
    TextView textView = (TextView) view.findViewById(android.R.id.text1);
    textView.setTextColor(Color.BLUE);
    textView.setText(tmpName[position] + quantity[position]+ "份," + tmpValue[position]);
    return view;
  }
}
