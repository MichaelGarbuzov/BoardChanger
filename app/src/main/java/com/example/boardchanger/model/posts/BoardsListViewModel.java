package com.example.boardchanger.model.posts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.boardchanger.model.posts.Board;
import com.example.boardchanger.model.Model;
import com.example.boardchanger.model.users.User;

import java.util.ArrayList;
import java.util.List;

public class BoardsListViewModel extends ViewModel {
    LiveData<List<Board>> data;

    public BoardsListViewModel(){
        data = Model.instance.getAllBoards();
    }

    private Boolean isUserBoardsOnly;
    private MutableLiveData<List<Board>> boardsList = new MutableLiveData();

    public LiveData<List<Board>> getData() {

        if(isUserBoardsOnly) {
            List<Board> boards = data.getValue();
            List<Board> onlyUserBoard = new ArrayList<>();

            for(Board board : boards) {
                if (board.getUser().equals(User.getInstance().getEmail())) {
                    onlyUserBoard.add(board);
                }
            }

            boardsList.postValue(onlyUserBoard);
            return boardsList;
        }

        return data;
    }

    public void setIsUserBoardsOnly(Boolean isUserBoardsOnly) {
        this.isUserBoardsOnly = isUserBoardsOnly;
    }
}
