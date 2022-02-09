package com.example.boardchanger.auth;

import static com.example.boardchanger.MyApplication.getContext;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boardchanger.R;
import com.example.boardchanger.feed.AddBoardFragment;
import com.example.boardchanger.feed.MainFeedActivity;
import com.example.boardchanger.model.Model;
import com.example.boardchanger.model.User;
import com.example.boardchanger.shared.ImageHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.InputStream;
import java.text.BreakIterator;


public class RegistrationActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText regEmail, regPwd, regName;
    String userEmail, userPassword, userName;
    Button regBtn;
    TextView regQn, regAddImage;
    ImageButton takeImage;
    ImageButton uploadImage;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    Bitmap imageBitmap;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_SELECTION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registration);

        regEmail = findViewById(R.id.RegistrationEmail);
        regPwd = findViewById(R.id.RegistrationPassword);
        regBtn = findViewById(R.id.RegistrationButton);
        regQn = findViewById(R.id.RegistrationQuestion);
        regName = findViewById(R.id.register_name);
        progressBar = findViewById(R.id.reg_progress_Bar);
        firebaseAuth = FirebaseAuth.getInstance();
        takeImage = findViewById(R.id.register_take_image_btn);
        uploadImage = findViewById(R.id.register_add_image_btn);
        regAddImage = findViewById(R.id.register_add_profile_image_tv);

        regQn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(login);
                finish();
            }
        });

        takeImage.setOnClickListener(v -> {
            ImageHandler.openCamera();
        });

        uploadImage.setOnClickListener(v -> {
            ImageHandler.openGallery();
        });

        regBtn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
                String email = regEmail.getText().toString().trim();
                String password = regPwd.getText().toString().trim();
                String name = regName.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    regEmail.setError("Email is Required.");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    regPwd.setError("Password is Required.");
                    return;
                }
                if (TextUtils.isEmpty(name)) {
                    regName.setError("Name is Required.");
                    return;
                }
                if (password.length() < 6) {
                    regPwd.setError("Password must be >= 6 Characters");
                    return;
                }


                progressBar.setVisibility(View.VISIBLE);

                // reg the user

                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userEmail = regEmail.getText().toString();
                            userName = regName.getText().toString();
                            userPassword = regPwd.getText().toString();
                            User user = new User(userEmail, userName, userPassword);
                            Model.instance.addUser(user, () -> {
                                Toast.makeText(RegistrationActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            });
                            Intent feed = new Intent(RegistrationActivity.this, MainFeedActivity.class);
                            startActivity(feed);
                            finish();
                        } else {
                            Toast.makeText(RegistrationActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
            }
        } else if (requestCode == REQUEST_IMAGE_SELECTION) {
            if (resultCode == RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                    imageBitmap = BitmapFactory.decodeStream(imageStream);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(RegistrationActivity.this, "Failed to select image", Toast.LENGTH_LONG).show();
                }
            }
        }

    }
}

