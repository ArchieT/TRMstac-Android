package pl.edu.platinum.archiet.trmstac;

import static pl.edu.platinum.archiet.trmstac.TableHeaders.ADDR_COLUMN;
import static pl.edu.platinum.archiet.trmstac.TableHeaders.DOST_COLUMN;
import static pl.edu.platinum.archiet.trmstac.TableHeaders.NUMER_COLUMN;
import static pl.edu.platinum.archiet.trmstac.TableHeaders.WOLN_COLUMN;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


/**
 * Created by mf on 14.07.16.
 */
public class ListViewAdapter extends BaseAdapter {
    Activity activity;
    TextView txtAddr;
    TextView txtDost;
    TextView txtNum;
    TextView txtWoln;

    public ListViewAdapter(Activity activity) {
        super();
        this.activity = activity;
    }

    @Override
    public int getCount() {
        int ourint;
        ourint = (int) Mobile.GiveAllStaLen();
        return ourint;
    }

    @Override
    public Object getItem(int position) {
        long poss = position;
        return Mobile.GiveASpSTRTUP(poss);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        long poss = position;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.colmn_row, null);
            txtNum = (TextView) convertView.findViewById(R.id.numsta);
            txtAddr = (TextView) convertView.findViewById(R.id.addrsta);
            txtDost = (TextView) convertView.findViewById(R.id.dostrow);
            txtWoln = (TextView) convertView.findViewById(R.id.wolrow);
        }
        Mobile.GiveByInt a = Mobile.GiveASpSTRTUP(poss);
        txtNum.setText(a.Give(Mobile.IDXNUM));
        txtAddr.setText(a.Give(Mobile.IDXADDR));
        txtDost.setText(a.Give(Mobile.IDXROW));
        txtWoln.setText(a.Give(Mobile.IDXWOL));
        return convertView;
    }
}
