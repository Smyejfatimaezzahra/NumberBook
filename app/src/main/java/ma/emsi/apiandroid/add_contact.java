package ma.emsi.apiandroid;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class add_contact extends AppCompatActivity {
    private EditText nameEdt, phoneEdt, emailEdt;
    private Button addContactEdt;
    RequestQueue requestQueue;
    String url="http://192.168.43.231:8080/numbook/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        nameEdt = findViewById(R.id.idEdtName);
        phoneEdt = findViewById(R.id.idEdtPhoneNumber);
        addContactEdt = findViewById(R.id.idBtnAddContact);
        JSONObject jsonObject = new JSONObject();
        // on below line we are adding on click listener for our button.
        addContactEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // on below line we are getting text from our edit text.
                String name = nameEdt.getText().toString();
                String phone = phoneEdt.getText().toString();
                // on below line we are making a text validation.
                if (TextUtils.isEmpty(name)  && TextUtils.isEmpty(phone)) {
                    Toast.makeText(add_contact.this, "Please enter the data in all fields. ", Toast.LENGTH_SHORT).show();
                } else {
                    // calling a method to add contact.
                    addContact(name, phone);
                    requestQueue = Volley.newRequestQueue(getApplicationContext());
                    try {
                        jsonObject.put("name", name);
                        jsonObject.put("number", phone);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                  /*  StringRequest request = new StringRequest(Request.Method.POST,
                            url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                       @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("Accept", "application/json");
                            headers.put("Content-Type", "application/json; charset=UTF-8");

                            return headers;
                        }


                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            HashMap<String, String> params = new HashMap<String, String>();
                            params.put("name", name);
                            params.put("number", phone);

                            return params;
                        }
                    };*/
                    JsonObjectRequest jsObjRequest =  new JsonObjectRequest(url, jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("RESPONSE", response.toString());
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if(error.getMessage()!=null){
                                Log.d("RESPONSE", error.getMessage());
                            }
                        }
                    });
                    requestQueue.add(jsObjRequest);
                }
            }
        });
    }
    private void addContact(String name,  String phone) {
        // in this method we are calling an intent and passing data to that
        // intent for adding a new contact.
        Intent contactIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
        contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
        contactIntent
                .putExtra(ContactsContract.Intents.Insert.NAME, name)
                .putExtra(ContactsContract.Intents.Insert.PHONE, phone);
        startActivityForResult(contactIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // in on activity result method.
        if (requestCode == 1) {
            // we are checking if the request code is 1
            if (resultCode == Activity.RESULT_OK) {
                // if the result is ok we are displaying a toast message.
                Toast.makeText(this, "Contact has been added.", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(add_contact.this, MainActivity.class);
                startActivity(i);
            }
            // else we are displaying a message as contact addition has cancelled.
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled Added Contact",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}