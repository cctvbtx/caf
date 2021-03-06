package com.creativearmy.template;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.os.Handler;
import android.os.Message;

import com.creativearmy.sdk.APIConnection;
import com.creativearmy.sdk.JSONObject;


public class i000Activity extends Activity implements OnClickListener {

    // Activity template, make a copy of this file, and change screen
    // ID to iXXX, and XXX shall be larger than 100. IDs lower than 100
    // are reserved for system use
    // Search globally for ID i072 to see how to add appropriate entries
    // at different locations when creating new page.

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.i000activity);

        Button mock_input_click = (Button)findViewById(R.id.INPUT);
        mock_input_click.setOnClickListener(this);

        Button chat_button = (Button)findViewById(R.id.CHAT_BUTTON);
        chat_button.setOnClickListener(this);

        TextView mock_output = (TextView) findViewById(R.id.OUTPUT);
        mock_output = (TextView) findViewById(R.id.OUTPUT);

        mock_output.setText(this.getLocalClassName());
    }

    @Override
    protected void onPause() {
        super.onPause();

        APIConnection.removeHandler(handler);
    }

    @Override
    protected void onResume() {
        super.onResume();

        APIConnection.registerHandler(handler);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.INPUT) {

            JSONObject data = new JSONObject();
            data.xput("obj","test");
            data.xput("act","input1");
            data.xput("data", "click");
            mock_capture_input(data);
        }

        if (v.getId() == R.id.CHAT_BUTTON) {

            EditText login_name = (EditText) findViewById(R.id.CHAT_LOGIN_NAME);

            JSONObject data = new JSONObject();
            data.xput("obj","person");
            data.xput("act","get");
            data.xput("login_name", login_name.getText().toString());
            APIConnection.send(data);
        }
    }

    private TextView mock_output;

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {

        mock_output = (TextView) findViewById(R.id.OUTPUT);

        if (msg.what == APIConnection.responseProperty) {

            JSONObject jo = (JSONObject) msg.obj;

            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // {"obj":"associate","act":"mock","to_login_name":"ISTUDIO_ACCOUNT","data":{"obj":"test","act":"output1","data":"blah"}}
            if (jo.s("obj").equals("test") && jo.s("act").equals("output1")) {
                //output.setText(jo.optJSONArray("data").optJSONObject(5).optString("show"));
                //output.setText(jo.a("data").o(5).s("show"));
                //output.setText(jo.optString("data"));
                mock_output.setText(jo.s("data"));
            }

            if (jo.s("obj").equals("person") && jo.s("act").equals("get")) {

                APIConnection.user_data.xput("chat_person", jo.s("pid"));
                APIConnection.user_data.xput("chat_mode", "chat");

                Intent intent = new Intent(i000Activity.this, i052Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            if (jo.optString("obj").equals("associate") && jo.optString("act").equals("mock")) {
                mock_output.setText("mock resp "+System.currentTimeMillis());
            }

            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        }
        }
    };
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void mock_capture_input(JSONObject data) {
        JSONObject req = new JSONObject();
        req.xput("obj", "associate");
        req.xput("act", "mock");
        req.xput("to_login_name", MyApplication.TOOLBOX_ACCOUNT);
        req.xput("data", data);
        APIConnection.send(req);
    }
}
