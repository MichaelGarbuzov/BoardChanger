package com.example.boardchanger.feed;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boardchanger.R;
import com.example.boardchanger.model.Model;
import com.example.boardchanger.model.users.User;
import com.example.boardchanger.shared.ImageHandler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class editProfileFragment extends Fragment {
    EditText userName, userPassword;
    ImageView userImage, previewImage;
    Button saveBtn;
    Bitmap imageBitmap;
    ProgressBar progressBar;
    ImageButton camBtn, galleryBtn;
    String imageCat = "/user_pictures/";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_SELECTION =2;


    public editProfileFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        User user = Model.instance.getUserByEmail( new Model.getUserByEmail() {
            @Override
            public void onComplete(User user) {
                Picasso.get().load(user.getImageUrl()).into(userImage);
            }

        });
        userName = view.findViewById(R.id.edit_profile_name);
        userPassword = view.findViewById(R.id.edit_profile_password);
        userImage = view.findViewById(R.id.edit_profile_image);
        camBtn = view.findViewById(R.id.edit_profile_take_image);
        galleryBtn = view.findViewById(R.id.edit_profile_add_image);
        saveBtn = view.findViewById(R.id.edit_profile_save_btn);
        progressBar = view.findViewById(R.id.edit_profile_progressbar);
        previewImage = view.findViewById(R.id.edit_profile_image_preview);
        progressBar.setVisibility(View.GONE);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        camBtn.setOnClickListener(v->{
            Intent intent = ImageHandler.openCamera();
            startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);

        });

        galleryBtn.setOnClickListener(v->{
            Intent intent = ImageHandler.openGallery();
            startActivityForResult(intent,REQUEST_IMAGE_SELECTION);
        });

        return view;
    }
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_CAPTURE){
            if(resultCode == Activity.RESULT_OK){
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                previewImage.setImageBitmap(imageBitmap);
            }
        }else if(requestCode == REQUEST_IMAGE_SELECTION){
            if(resultCode == RESULT_OK){
                try{
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                    imageBitmap = BitmapFactory.decodeStream(imageStream);
                    previewImage.setImageBitmap(imageBitmap);
                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"Failed to select image",Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    public void save() {
        progressBar.setVisibility(View.VISIBLE);
        saveBtn.setEnabled(false);
        camBtn.setEnabled(false);
        galleryBtn.setEnabled(false);
        String name = userName.getText().toString();
        String password = userPassword.getText().toString();

        Map<String, Object> userMap = User.getInstance().toJson();

        if(!name.isEmpty()) {
          userMap.put("name", name);
        }

        if(!password.isEmpty()) {
            userMap.put("password", password);
        }

        Fragment thisFragment = this;
        Model.instance.updateUser(userMap, imageBitmap,new Model.CompleteListener() {
            @Override
            public void onComplete() {
                NavHostFragment.findNavController(thisFragment).popBackStack();
            }
        });
    }
}