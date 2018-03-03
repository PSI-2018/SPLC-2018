package splc.polsl.pl.client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import splc.polsl.pl.client.model.ExpandableListViewAdapter;

public class MainActivity extends AppCompatActivity {

    ExpandableListView expandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);

        ExpandableListViewAdapter adapter = new ExpandableListViewAdapter(MainActivity.this, this);
        expandableListView.setAdapter(adapter);
        getSupportActionBar().setTitle("Client (connected)");
    }
}
