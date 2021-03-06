package ducnm.com.footballyardmanage;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends Activity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    EditText txtReEmail, txtRePassword, txtRePasswordConfirm, txtName, txtNumberPhone;
    RadioButton radioCustomer, radioManager;
    Button btnRunRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        txtReEmail = (EditText) findViewById(R.id.txtReEmail);
        txtRePassword = (EditText) findViewById(R.id.txtRePassword);
        txtRePasswordConfirm = (EditText) findViewById(R.id.txtRePasswordConfirm);
        txtName = (EditText) findViewById(R.id.name);
        txtNumberPhone = (EditText) findViewById(R.id.numberPhone);

        radioCustomer = (RadioButton) findViewById(R.id.radioCustomer);
        radioManager = (RadioButton) findViewById(R.id.radioManager);

        btnRunRegister = (Button) findViewById(R.id.btnRunRegister);


        btnRunRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtReEmail.getText().toString();
                String password = txtRePassword.getText().toString();
                String passwordConfirm = txtRePasswordConfirm.getText().toString();
                String name = txtName.getText().toString();
                String numberPhone = txtNumberPhone.getText().toString();
                String role = "Customer";
                if(radioManager.isChecked()) role = "Manager";

                if(!isValidEmailAddress(email)){
                    Toast.makeText(RegisterActivity.this, "Please input correct email", Toast.LENGTH_SHORT).show();
                }else if(!password.equals(passwordConfirm)){
                    Toast.makeText(RegisterActivity.this, "Confirm password is not match", Toast.LENGTH_SHORT).show();
                }else if(name.equals("")){
                    Toast.makeText(RegisterActivity.this, "Please input name", Toast.LENGTH_SHORT).show();
                }else if(numberPhone.equals("")){
                    Toast.makeText(RegisterActivity.this, "Please input number phone", Toast.LENGTH_SHORT).show();
                }
                else{
                    createAccount(email, password, role, name, numberPhone);
                }
            }
        });

    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public void createAccount(String email, String password, final String role, final String name, final String numberPhone){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String userID = task.getResult().getUser().getUid();
                            mDatabase.child("users").child(userID).child("role").setValue(role);
                            mDatabase.child("users").child(userID).child("name").setValue(name);
                            mDatabase.child("users").child(userID).child("phoneNumber").setValue(numberPhone);
                            Toast.makeText(RegisterActivity.this, "Register success!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(RegisterActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
