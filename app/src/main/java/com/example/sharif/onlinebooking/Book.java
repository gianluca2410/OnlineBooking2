package com.example.sharif.onlinebooking;

/**
 * Created by SHARIF on 8/15/2018.
 */

 public class Book {
     String B_name,A_name,B_page,B_words,B_price,B_edition,B_code,image;

     public Book(String a,String b,String c,String d,String e,String f,String g, String image){
          this.B_name=a;
          this.A_name=b;
          this.B_page=c;
          this.B_words=d;
          this.B_price=e;
          this.B_edition=f;
          this.B_code=g;
          this.image=image;
     }
     public String getB_name(){
         return B_name;
     }
     public  String getA_name(){
         return A_name;
     }
    public  String  getB_page(){
         return B_page;
    }
    public  String  getB_words(){
        return  B_words;
    }
    public  String   getB_price(){
        return B_price;
    }
    public  String   getB_edition(){
       return B_edition;
    }
    public  String   getB_code(){
        return B_code;
    }

    public String getImage() {
        return image;
    }
}
