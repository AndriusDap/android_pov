package com.andrius.pov;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	
	private Button startButton;
	private EditText message;
	private Context context;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = (Context) this;
        startButton = (Button) findViewById(R.id.button1);
        message = (EditText) findViewById(R.id.editText1);
        
        startButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(context, PovViewer.class);
				i.putExtra("Message", message.getText().toString());
				context.startActivity(i);
			}
        	
        });
    }   
}