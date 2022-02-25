package com.example.boardchanger.feed;

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
import com.example.boardchanger.model.Model;
import com.example.boardchanger.model.users.User;
import com.squareup.picasso.Picasso;

import java.util.Date;


public class profileFragment extends Fragment {
    ImageView profileImage;
    TextView userName,userEmail, memSince, memSinceData;

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
        User user = Model.instance.getUserByEmail( new Model.getUserByEmail() {
            @Override
            public void onComplete(User user) {
                Date dateLong = new Date(user.getUpdateDate()*1000);
                String date = dateLong.toString();
                userEmail.setText(user.getEmail());
                userName.setText(user.getName());
                memSinceData.setText(date);
                Picasso.get().load(user.getImageUrl()).into(profileImage);
            }

        });
        profileImage = view.findViewById(R.id.profile_image);
        userName = view.findViewById(R.id.profile_name);
        userEmail = view.findViewById(R.id.profile_email);
        memSince = view.findViewById(R.id.profile_member_since);
        memSinceData = view.findViewById(R.id.profile_member_since_data);
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