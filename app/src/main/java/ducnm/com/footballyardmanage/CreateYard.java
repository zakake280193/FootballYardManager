package ducnm.com.footballyardmanage;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class CreateYard extends Activity {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    DatabaseReference mDatabase;

    Button bt;
    FloatingActionButton fb;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    ListView lv;
    ArrayList<String> listURL;
    ArrayAdapter adapter;
    String imageName;
    Yard yard;


    FrameLayout progressBarHolder;
    int REQUEST_CODE_IMAGE = 1;
    EditText txtAddressYard, txtYardOwner, txtPhoneNumber, txtDescription;
    Spinner spinnerArea;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && null != data) {
            new MyTask(data).execute();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_yard);

        yard = new Yard();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        bt = (Button) findViewById(R.id.button2);
        fb = (FloatingActionButton) findViewById(R.id.fab);
        lv = (ListView) findViewById(R.id.listView);
        txtAddressYard = (EditText) findViewById(R.id.txtAddressYard);
        txtYardOwner = (EditText) findViewById(R.id.txtYardOwner);
        txtPhoneNumber = (EditText) findViewById(R.id.txtPhoneNumber);
        txtDescription = (EditText) findViewById(R.id.txtDescription);
        spinnerArea = (Spinner) findViewById(R.id.spinnerArea);
        listURL = new ArrayList<String>();
        adapter = new ArrayAdapter(CreateYard.this, android.R.layout.simple_list_item_1,listURL);
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
//        vf = (ViewFlipper) findViewById(R.id.viewFlipper);
//        vf.addView(loadImageViewFromURL("https://firebasestorage.googleapis.com/v0/b/footballyardmanage.appspot.com/o/412081c7e322ba34b9badcc39499f854-d9pv688.jpg?alt=media&token=2c146819-6bc9-4302-a83d-652f642453cd"));
//        vf.addView(loadImageViewFromURL("https://firebasestorage.googleapis.com/v0/b/footballyardmanage.appspot.com/o/IMG_20170214_015957.jpg?alt=media&token=2fde9e6a-0089-45f4-b7de-c7b7b52c86c8"));
//        vf.addView(loadImageViewFromURL("https://firebasestorage.googleapis.com/v0/b/footballyardmanage.appspot.com/o/IMG_20170214_015957.jpg?alt=media&token=2fde9e6a-0089-45f4-b7de-c7b7b52c86c8"));
//        vf.setFlipInterval(3000);
//        vf.setAutoStart(true);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, REQUEST_CODE_IMAGE);
            }
        });
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createYard();
            }
        });
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {
        Intent data;

        public MyTask(Intent data) {
            this.data = data;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bt.setEnabled(false);
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
            bt.setEnabled(true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                    uploadImage(data);
                    TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void uploadImage(Intent data){
        System.out.println("Call method");
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
        File f = new File(picturePath);
        imageName = f.getName();

        StorageReference mountainsRef = storageRef.child(imageName);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data2 = baos.toByteArray();
        UploadTask uploadTask = mountainsRef.putBytes(data2);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                System.out.println("On success");
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Toast.makeText(CreateYard.this, taskSnapshot.getMetadata().getName() , Toast.LENGTH_SHORT).show();
                listURL.add(imageName);
                lv.setAdapter(adapter);
                yard.setListImage(imageName.replace(".jpg",""), downloadUrl.toString());
            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

            }
        });

    }

    private ImageView loadImageViewFromURL(String imgUrl) {
        ImageView image = new ImageView(getApplicationContext());
        Picasso.with(this).load(imgUrl).into(image);
        return image;
    }
    public void createYard(){
        yard.setAddress(txtAddressYard.getText().toString());
        yard.setYardOwner(txtYardOwner.getText().toString());
        yard.setPhoneNumber(txtPhoneNumber.getText().toString());
        yard.setDescription(txtDescription.getText().toString());
        yard.setArea(spinnerArea.getSelectedItem().toString());
        yard.setIdOwner(MainActivity.user.getUid());
        String mGroupId = mDatabase.push().getKey();
        yard.setId(mGroupId);
        mDatabase.child("Yard").child(mGroupId).setValue(yard);

        Intent intent = new Intent(CreateYard.this, SearchActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CreateYard.this, SearchActivity.class);
        startActivity(intent);

    }
}
