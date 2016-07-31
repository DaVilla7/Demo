//package com.stevenhu.android.phone.ui;
//
//import org.w3c.dom.Text;
//import java.util.List;
//import com.stevenhu.android.phone.ui.R;
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//public class LeftMenuContentAdapter extends ArrayAdapter<LeftMenuContent> {
//	  private int resourceId;
//      public LeftMenuContentAdapter(Context context,int textViewResourceId,List<LeftMenuContent>  objects){
//      	super(context, textViewResourceId,objects);
//      	resourceId = textViewResourceId;
//      	
//      	public final View getView(int position,View convertView,ViewGroup parent){
//        	LeftMenuContent content1 = getItem(position);
//        	View view1 = LayoutInflater.from(getContext()).inflate(resourceId, null);
//        	ImageView contenLeftMenutImage = (ImageView) view1.findViewById(R.id.left_menu_list_image);
//        	TextView    contentLeftMenuText = (TextView) view1.findViewById(R.id.left_menu_list_content);
//        	
//        	contenLeftMenutImage.setImageResource(content1.getImageID());
//        	contentLeftMenuText.setText(content1.getNme());
//        	
//        	return view1;
//        	
//        }
//}
//}