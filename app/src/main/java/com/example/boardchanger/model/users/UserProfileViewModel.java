package com.example.boardchanger.model.users;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.boardchanger.model.Model;

public class UserProfileViewModel extends ViewModel {
    LiveData<User> data;
}