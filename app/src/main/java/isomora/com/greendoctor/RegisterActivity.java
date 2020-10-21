package isomora.com.greendoctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    Button btn_signup;
    EditText edt_name, edt_email, edt_password;
    String name, email, password;
    FirebaseAuth mAuth;
    String id;
    CheckBox show_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btn_signup = findViewById(R.id.btn_signup);
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        edt_name = findViewById(R.id.edt_name);
        mAuth = FirebaseAuth.getInstance();
        show_password = findViewById(R.id.show_password);

        show_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    edt_password.setTransformationMethod(null);
                } else {
                    edt_password.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = edt_name.getText().toString().trim();
                email = edt_email.getText().toString().trim();
                password = edt_password.getText().toString().trim();

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                id = mAuth.getCurrentUser().getUid();
                                Model model = new Model(name, email, password, id);
                                DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Users");
                                mRef.child(id).setValue(model);
                                Toast.makeText(RegisterActivity.this, "Data Saved!", Toast.LENGTH_SHORT).show();

                            } else {
                                String message = task.getException().getMessage();
                                Toast.makeText(RegisterActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, "Empty Field!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}