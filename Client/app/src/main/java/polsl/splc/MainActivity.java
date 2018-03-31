package polsl.splc;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private EditText userEmail;
    private EditText userPassword;
    private Button confirmButton;
    private Button startButton;
    private TextView result;
    boolean startEnabled = false;
    String host, port;
    ClientCommunication client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        userEmail = (EditText) findViewById(R.id.emailText);
        confirmButton = (Button) findViewById(R.id.confBtn);
        startButton = (Button) findViewById(R.id.startButton);
        result = (TextView) findViewById(R.id.resultText);
        startButton.setVisibility(View.INVISIBLE);
        userPassword = (EditText) findViewById(R.id.passwordsText);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startButton.setVisibility(View.INVISIBLE);
                    if ((userEmail.getText().length() > 0) && (userPassword.getText().length() > 0)) {
                        checkId(userEmail.getText().toString(), userPassword.getText().toString());
                    }else{
                        result.setText("Fill both fields");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            ;

        });
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveOn();
            }

            ;
        });
    }

    ;

    private void checkId(String mailGiven, String passwordGiven) throws IOException {
        host = Util.getProperty("host", getApplicationContext());
        port = Util.getProperty("port", getApplicationContext());
        client = new ClientCommunication(host, port);
        boolean loginResult = client.checkLogin(mailGiven, passwordGiven);
        if (loginResult) {
            startButton.setVisibility(View.VISIBLE);
            result.setText("Logged");
        } else {
            result.setText("Wrong credentials");
        }
    }

    private void moveOn() {
        Intent intent = new Intent(MainActivity.this, LoggedActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("email", userEmail.getText().toString());
        bundle.putString("password", userPassword.getText().toString());
        bundle.putString("host", host);
        bundle.putString("port", port);
        intent.putExtras(bundle); //Put your id to your next Intent
        startButton.setEnabled(false);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
