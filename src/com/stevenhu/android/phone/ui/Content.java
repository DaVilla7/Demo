package com.stevenhu.android.phone.ui;

public class Content {
       private String  name;
       private int ImageID;
       private String content;
       public Content(String name,int ImageId,String content){
    	   this.name = name;
    	   this.ImageID = ImageId;
    	   this.content = content;
       }
       
       public String getName(){
    	   return name;
       }
       
       public int getImageID(){
    	   return ImageID;
       }
       
       public String getContent(){
    	   return content;
       }
}
