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
    private Boolean isUserBoardsOnly;
    private LiveData<List<Board>> boardsList;

    public BoardsListViewModel(){
        data = Model.instance.getAllBoards();

        boardsList = Transformations.map(data, boards -> {
            if(isUserBoardsOnly) {
                List<Board> onlyUserBoard = new ArrayList<>();
                for (Board board : boards) {
                    if (board.getUser().equals(User.getInstance().getEmail())) {
                        onlyUserBoard.add(board);
                    }
                }
                return onlyUserBoard;
            } else {
                return boards;
            }
        });
    }


    public LiveData<List<Board>> getData() {
        return boardsList;
    }

    public void setIsUserBoardsOnly(Boolean isUserBoardsOnly) {
        this.isUserBoardsOnly = isUserBoardsOnly;
    }
}
