package splc.polsl.pl.client;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import splc.polsl.pl.client.utilities.Connector;
import splc.polsl.pl.client.utilities.ExpandableListViewAdapter;

public class MainActivity extends AppCompatActivity {

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
        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        ExpandableListViewAdapter adapter = new ExpandableListViewAdapter(MainActivity.this);
        expandableListView.setAdapter(adapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.test_connection:
                new Connector(this).execute(Connector.RequestType.TEST_CONNECTION);
                break;
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.exit:
                finish();
                break;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onStop() {
        MainActivity.setContextStatus(false);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        MainActivity.setContextStatus(false);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        MainActivity.setContextStatus(false);
        super.onPause();
    }

    @Override
    protected void onStart() {
        MainActivity.setContextStatus(true);
        super.onStart();
    }

    @Override
    protected void onResume() {
        MainActivity.setContextStatus(true);
        super.onResume();
    }
}
