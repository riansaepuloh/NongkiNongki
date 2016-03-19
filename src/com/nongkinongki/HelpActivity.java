package com.nongkinongki;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class HelpActivity extends Activity{
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		TextView textView = new TextView(this);
		textView.setText("Help Layout");
		setContentView(textView);
	}

}
