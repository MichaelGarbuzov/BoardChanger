package com.example.boardchanger.model.posts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.boardchanger.model.posts.Board;
import com.example.boardchanger.model.Model;

import java.util.List;

public class BoardsListViewModel extends ViewModel {
    LiveData<List<Board>> data;

    public BoardsListViewModel(){
        data = Model.instance.getAllBoards();
    }

    public LiveData<List<Board>> getData() {
        return data;
    }
}
