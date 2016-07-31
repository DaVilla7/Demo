package com.stevenhu.android.phone.ui;
import java.util.List;

import org.w3c.dom.Text;

import com.stevenhu.android.phone.ui.R;


//import android.R.integer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ContentAdapter extends ArrayAdapter<Content> {
        private int resourceId;
        public ContentAdapter(Context context,int textViewResourceId,List<Content>  objects){
        	super(context, textViewResourceId,objects);
        	resourceId = textViewResourceId;
        }
        
        public View getView(int position,View convertView,ViewGroup parent){
        	Content content = getItem(position);
        	View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        	ImageView contentImage = (ImageView) view.findViewById(R.id.list_image);
        	TextView    contentName = (TextView) view.findViewById(R.id.list_name);
        	TextView  contentMain = (TextView) view.findViewById(R.id.list_content);
        	contentImage.setImageResource(content.getImageID());
        	contentName.setText(content.getName());
        	contentMain.setText(content.getContent());
        	return view;
        	
        }
}
