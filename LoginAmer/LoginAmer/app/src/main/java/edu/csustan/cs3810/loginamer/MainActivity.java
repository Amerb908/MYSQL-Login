package edu.csustan.cs3810.loginamer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void login(View v) {

        // get id and password
        EditText inputID = findViewById(R.id.inputID);
        String id = inputID.getText().toString();
        EditText inputPassword = findViewById(R.id.inputPassword);
        String password = inputPassword.getText().toString();
        // run task in background
        Task task = new Task(this);
        task.execute(id, password);
    }
}
