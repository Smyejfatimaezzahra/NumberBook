package ma.emsi.apiandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button call, sms, contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        call = findViewById(R.id.call);
        sms = findViewById(R.id.sms);
        contacts = findViewById(R.id.contacts);

        call.setOnClickListener(this);
        sms.setOnClickListener(this);
        contacts.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
       /* if(v == call){
            Intent appel = new Intent(Intent.ACTION_DIAL, Uri.parse(("tel:0548785544")) );

            startActivity(appel);
        }
        if(v == sms){
            Intent msg = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:075587545"));
            msg.putExtra("sms_body", "Salut tout le monde");
            startActivity(msg);
        }
        if(v == contacts){
            Map<String, String> map =  getNumber(this.getContentResolver());

            Set<String> keys = map.keySet();

            for(String key : keys){
                Log.d(key, map.get(key));
            }

        }*/
    }


    public HashMap<String, String> getNumber(ContentResolver cr) {
        HashMap<String, String> map = new HashMap<>();
        Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (phones.moveToNext()) {
            @SuppressLint("Range") String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            @SuppressLint("Range") String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            map.put(name, phoneNumber);
        }
        phones.close();// close cursor
        return map;
    }

}