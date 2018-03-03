package splc.polsl.pl.client.utilities;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.ActionBar;

import splc.polsl.pl.client.MainActivity;


public class ExpandableListViewAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ActionBar actionBar;

    String [] groupNames = {
            "Pietro 0",
            "Pietro 1",
            "Pietro 2",
            "Pietro 3",
            "Pietro 4",
            "Pietro 5",
            "Pietro 6",
            "Pietro 7",
            "Pietro 8",
            "Pietro 9",
    };
    String [][] childNames={
            {"1","2","3","4","5","6","7","8","9","10"},
            {"100","101","102","103","104","105","106","107","108","109","110"},
            {"200","201","202","203","204","205","206","207","208","209","210"},
            {"300","301","302","303","304","305","306","307","308","309","310"},
            {"400","401","402","403","404","405","406","407","408","409","410"},
            {"500","501","502","503","504","505","506","507","508","509","510"},
            {"600","601","602","603","604","605","606","607","608","609","610"},
            {"700","701","702","703","704","705","706","707","708","709","710"},
            {"800","801","802","803","804","805","806","807","808","809","810"},
            {"900","901","902","903","904","905","906","907","908","909","910"}
    };


    public ExpandableListViewAdapter(Context context, ActionBar actionBar){
        this.context = context;
        this.actionBar = actionBar;
    }

    @Override
    public int getGroupCount() {
        return groupNames.length;
    }

    @Override
    public int getChildrenCount(int i) {
        return childNames.length;
    }

    @Override
    public Object getGroup(int i) {
        return groupNames[i];
    }

    @Override
    public Object getChild(int i, int i1) {
        return childNames[i][i1];
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        TextView textView = new TextView(context);
        textView.setText(groupNames[i]);
        textView.setPadding(100,0,0,0);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(30);
        return textView;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final TextView textView = new TextView(context);
        textView.setText(childNames[i][i1]);
        textView.setPadding(150,10,0,10);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(20);
        textView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //TODO: DODAC POBRANIE DANYCH JAK email, haslo, ip oraz host
                String serverCommand = "OPEN jankowalski@mail.pl haslo123 " + textView.getText().toString();
                new Connector(context).execute(serverCommand, "10.10.10.105", "1234");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        return textView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
