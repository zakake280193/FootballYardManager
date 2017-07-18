package ducnm.com.footballyardmanage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class YardActivity extends AppCompatActivity {
    ViewFlipper vf;

    TextView address, name, numberPhone;
    Button btnOrder;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yard);
        address = (TextView) findViewById(R.id.textView6);
        name = (TextView) findViewById(R.id.textView8);
        numberPhone = (TextView) findViewById(R.id.textView10);
        btnOrder =(Button) findViewById(R.id.btnOrder);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Intent i = getIntent();
        final Yard yard = (Yard)i.getSerializableExtra("yard");
        Toast.makeText(this, yard.getAddress(), Toast.LENGTH_SHORT).show();
                vf = (ViewFlipper) findViewById(R.id.viewFlipper);
        for(String currentKey : yard.getListImage().keySet()){
            vf.addView(loadImageViewFromURL(yard.getListImage().get(currentKey)));
        }

        address.setText(yard.getAddress());
        address.setText(yard.getYardOwner());
        address.setText(yard.getPhoneNumber());


        vf.setFlipInterval(3000);
        vf.setAutoStart(true);

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("users").child(yard.getIdOwner()).child("order").child(MainActivity.user.getUid()).setValue("ordered");
            }
        });
    }


    private ImageView loadImageViewFromURL(String imgUrl) {
        ImageView image = new ImageView(getApplicationContext());
        Picasso.with(this).load(imgUrl).into(image);
        return image;
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(YardActivity.this, SearchActivity.class);
        startActivity(intent);

    }

}
