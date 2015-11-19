package freelance.android.erick.demoapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import freelance.android.erick.demoapplication.R;
import freelance.android.erick.demoapplication.common.API;
import freelance.android.erick.demoapplication.model.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonPush;
    private EditText edittextPush;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initButtons();
        initEdittexts();
    }

    private void initButtons() {
        buttonPush = (Button) findViewById(R.id.buttonPush);

        buttonPush.setOnClickListener(this);
    }

    private void initEdittexts() {
        edittextPush = (EditText) findViewById(R.id.edittextPush);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonPush: {
                API.asyncAddMessage(String.valueOf(edittextPush.getText()), new API.CallbackResponse() {
                    @Override
                    public void onSuccess(final Response model) {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, model.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onError(final Response error) {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                edittextPush.setText("");
            }
        }
    }
}
