package com.example.boardchanger.model;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.boardchanger.MyApplication;

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
    public interface SaveImageListener{
        void onComplete(String url);
    }
    public void saveImage(Bitmap imageBitmap, String imageName,SaveImageListener listener ) {
        modelFirebase.saveImage(imageBitmap,imageName,listener);
    }

    public enum BoardListLoadingState{
        loading,
        loaded
    }

    MutableLiveData<BoardListLoadingState> boardListLoadingState = new MutableLiveData<BoardListLoadingState>();
    public LiveData<BoardListLoadingState> getBoardListLoadingState(){
        return boardListLoadingState;
    }

    MutableLiveData<List<Board>> boardsList = new MutableLiveData<List<Board>>();
    public LiveData<List<Board>> getAll(){
        if(boardsList.getValue() == null) { refreshBoardsList(); }
        return boardsList;
    }

    public void refreshBoardsList(){
        boardListLoadingState.setValue(BoardListLoadingState.loading);

        Long lastUpdateDate = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("BoardsLastUpdateDate",0);

        modelFirebase.getAllBoards(lastUpdateDate, new ModelFirebase.GetAllBoardsListener(){
            @Override
            public void onComplete(List<Board> list){
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Long lud = new Long(0);
                        for(Board board : list){
                            BoardChangerLocalDB.db.boardDao().insertAll(board);
                            if(lud < board.getUpdateDate()){ lud = board.getUpdateDate(); }
                        }
                        MyApplication.getContext().getSharedPreferences("TAG",Context.MODE_PRIVATE)
                                .edit().putLong("BoardsLastUpdateDate", lud).commit();

                        List<Board> bdList = BoardChangerLocalDB.db.boardDao().getAll();
                        boardsList.postValue(bdList);
                        boardListLoadingState.postValue(BoardListLoadingState.loaded);
                    }
                });
            }
        });
    }

    public interface  AddBoardListener{
        void onComplete();
    }

    public interface getBoardByName{
        void onComplete(Board board);
    }

    public Board getBoardByName(String boardName, getBoardByName listener){
        modelFirebase.getBoardByName(boardName, listener);
        return null;
    }

    public void addBoard(Board board, AddBoardListener listener){
        modelFirebase.addBoard(board, new AddBoardListener() {
            @Override
            public void onComplete() {
                refreshBoardsList();
                listener.onComplete();
            }
        });
    }
}
