package com.stevenhu.android.phone.ui;

import com.stevenhu.android.phone.ui.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.style.SuperscriptSpan;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class ListSecondContent extends Activity {
    protected void onCreate(Bundle savedInstanceState){
    	super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	setContentView(R.layout.list_to_content);
    	Intent intent = getIntent();
    	String name = intent.getStringExtra("listName");
    	String content = intent.getStringExtra("listContent");
    	int imageId = intent.getIntExtra("ImageId", R.drawable.f);
    	 TextView nameTextView = (TextView)findViewById(R.id.list_to_second_name);
    	 TextView contentTextView = (TextView)findViewById(R.id.list_to_second_content);
    	 nameTextView.setText( name);
    	 contentTextView.setText(content);
    	 ImageView ImageView = (ImageView) findViewById(R.id.list_to_second_image);
    	 ImageView.setBackgroundResource(imageId);
    }
}
