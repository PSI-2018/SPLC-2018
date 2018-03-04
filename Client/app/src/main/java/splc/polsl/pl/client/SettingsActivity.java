package splc.polsl.pl.client;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    private EditText emailField;
    private EditText passwordField;
    private EditText serverIPField;
    private EditText portField;

    private SharedPreferences preferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        this.emailField = (EditText) findViewById(R.id.emailField);
        this.passwordField = (EditText) findViewById(R.id.passwordField);
        this.serverIPField = (EditText) findViewById(R.id.serverIPField);
        this.portField = (EditText) findViewById(R.id.portField);
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
        loadSettings();
        final Button button = (Button) findViewById(R.id.saveAuthorizationData);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                saveSettings();
            }
        });
    }

    private void loadSettings(){
        String email = this.preferences.getString("email","default e-mail");
        String password = this.preferences.getString("password", "default pasword");
        String serverIP = this.preferences.getString("serverIP", "192.168.0.1");
        Integer port = this.preferences.getInt("port",1234);

        this.emailField.setText(email);
        this.passwordField.setText(password);
        this.serverIPField.setText(serverIP);
        this.portField.setText(port.toString());
    }

    private void saveSettings(){
        String email = this.emailField.getText().toString();
        String password = this.passwordField.getText().toString();
        String serverIP = this.serverIPField.getText().toString();
        Integer port = Integer.parseInt(this.portField.getText().toString());

        Editor preferencesEditor = this.preferences.edit();
        preferencesEditor.putString("email",email);
        preferencesEditor.putString("password",password);
        preferencesEditor.putString("serverIP",serverIP);
        preferencesEditor.putInt("port",port);
        String message = preferencesEditor.commit() ? "Successfuly saved" : "Error while saving";
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
        super.finish();
    }

}
