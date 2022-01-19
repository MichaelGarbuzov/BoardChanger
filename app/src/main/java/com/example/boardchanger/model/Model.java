package com.example.boardchanger.model;

import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {
    public final static Model instance = new Model();
    Executor executor =Executors.newFixedThreadPool(1);
    Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());
    ModelFirebase modelFirebase = new ModelFirebase();
    private Model(){
    }

    public interface GetAllBoardsListener{
        void onComplete(List<Board> boardsList);
    }
    public void getAllBoards(GetAllBoardsListener listener) {
        modelFirebase.getAllBoards(listener);
    }
    public interface getBoardByName{
        void onComplete(Board board);
    }
    public Board getBoardByName(String boardName, getBoardByName listener){
        modelFirebase.getBoardByName(boardName, listener);
        return null;
    }

    public interface  AddBoardListener{
        void onComplete();
    }
    public void addBoard(Board board, AddBoardListener listener){
        modelFirebase.addBoard(board, listener);
    }
}
