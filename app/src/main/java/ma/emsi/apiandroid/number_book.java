package ma.emsi.apiandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import beans.Contact;

public class number_book extends AppCompatActivity implements View.OnClickListener  {
      MainActivity main;
      Button find;
      EditText text;
      RadioButton b1;
      RadioButton b2;
      PopupWindow popupWindow;
      ConstraintLayout c;
      String url="http://192.168.43.231:8080/numbook/";
      RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_book);
        main=new MainActivity();
        find=findViewById(R.id.find);
        find.setOnClickListener(this);
        text=findViewById(R.id.num);
        b1=findViewById(R.id.name);
        b2=findViewById(R.id.number);
        c=findViewById(R.id.c);
    }

    @Override
    public void onClick(View view) {
        String param = text.getText().toString();
        String key="";
        if(b1.isChecked())
            key="name";
        else
            key= "number";
        if(view==find){
            requestQueue = Volley.newRequestQueue(getApplicationContext());
            String finalKey = key;
            StringRequest request = new StringRequest(Request.Method.GET,
                   url+key+"/"+param, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                     Log.d("response",response);
                    Type type = new TypeToken<Contact>(){}.getType();
                    Contact c = new Gson().fromJson(response, type);
                    Popup(finalKey,c);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("TAG",error.toString());
                }
            });
            requestQueue.add(request);
        }
    }
     public  void Popup(String key,Contact ct){
         LayoutInflater layoutInflater = (LayoutInflater) number_book.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         View v = layoutInflater.inflate(R.layout.activity_main,null);
         TextView tv=v.findViewById(R.id.result);
         ImageView img=v.findViewById(R.id.close);
         Button call= v.findViewById(R.id.call);
         Button sms=v.findViewById(R.id.sms);
         Button contact= v.findViewById(R.id.contacts);
         if (key.equals("number"))tv.setText("name:"+" "+ct.getName());
         else tv.setText("number:"+" "+"0"+ct.getNumber());
         popupWindow = new PopupWindow(v, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
         popupWindow.showAtLocation(c, Gravity.CENTER, 0, 0);
         popupWindow.update(750,750);
         img.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 popupWindow.dismiss();
             }
         });

         call.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent appel = new Intent(Intent.ACTION_DIAL, Uri.parse(("tel:"+String.valueOf((ct.getNumber()))) ));

                 startActivity(appel);
             }
         });
         sms.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent msg = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+String.valueOf((ct.getNumber()))));
                 msg.putExtra("sms_body", "Salut tout le monde");
                 startActivity(msg);
             }
         });
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Uri addContactsUri = ContactsContract.Data.CONTENT_URI;
                Intent contacts = new Intent(Intent.ACTION_VIEW, addContactsUri);

                startActivity(contacts);*/
                Intent intent = new Intent(number_book.this, add_contact.class);
                startActivity(intent);
            }
        });
     }


}