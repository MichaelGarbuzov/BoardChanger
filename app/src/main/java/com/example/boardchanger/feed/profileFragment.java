package com.example.boardchanger.feed;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.boardchanger.R;
import com.example.boardchanger.auth.LoginActivity;
import com.example.boardchanger.model.Model;
import com.example.boardchanger.model.users.User;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;


public class profileFragment extends Fragment {
    ImageView profileImage;
    TextView userName;
    TextView userEmail;

    public profileFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        String userProfileData = profileFragmentArgs.fromBundle(getArguments()).getUserProfileData();
        User user = Model.instance.getUserByEmail(userProfileData, new Model.getUserByEmail(){
            @Override
            public void onComplete(User user){
                userEmail.setText(user.getEmail());
                userName.setText(user.getName());
                Picasso.get().load(user.getImageUrl()).into(profileImage);
            }

    });
        profileImage = view.findViewById(R.id.profile_image);
        userName = view.findViewById(R.id.profile_name);
        userEmail = view.findViewById(R.id.profile_email);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (!super.onOptionsItemSelected(item)) {
            switch (item.getItemId()) {
                case R.id.profile_menu_boards:
                    return true;
                case R.id.profile_menu_edit:
                    return true;
            }
        }
        return true;
    }
}