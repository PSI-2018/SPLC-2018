package splc.polsl.pl.client;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;
import android.support.v7.app.ActionBar;

import splc.polsl.pl.client.utilities.ExpandableListViewAdapter;

public class MainActivity extends AppCompatActivity {

    ExpandableListView expandableListView;
    public static boolean contextStatus;

    private static void setContextStatus(boolean contextStatus){
        MainActivity.contextStatus = contextStatus;
    }

    public static boolean isContextAvailable(){
        return MainActivity.contextStatus;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = (ActionBar) getSupportActionBar();
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        ExpandableListViewAdapter adapter = new ExpandableListViewAdapter(MainActivity.this, actionBar);
        expandableListView.setAdapter(adapter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        MainActivity.setContextStatus(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainActivity.setContextStatus(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MainActivity.setContextStatus(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        MainActivity.setContextStatus(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainActivity.setContextStatus(true);
    }
}
