package com.example.boardchanger.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ModelFirebase {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void getAllBoards(Model.GetAllBoardsListener listener) {
        db.collection(Board.COLLECTION_NAME).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Board> list = new LinkedList<Board>();
                if(task.isSuccessful()){
                    QuerySnapshot querySnapshot = task.getResult();
                    for (QueryDocumentSnapshot doc : querySnapshot){
                        Map<String, Object> json = doc.getData();
                        Board board = Board.create(json);
                        if(board != null){
                            list.add(board);
                        }
                    }
                }
                listener.onComplete(list);
            }
        });
    }

    public void addBoard(Board board, Model.AddBoardListener listener) {
        Map<String, Object> json = board.toJson();


        db.collection(Board.COLLECTION_NAME)
                .document(board.getName())
                .set(json)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        listener.onComplete();
                    }
                })
                .addOnFailureListener(new OnFailureListener(){
                    @Override
                    public void onFailure(@NonNull Exception e){
                        listener.onComplete();
                    }
        });

    }

    public void getBoardByName(String boardName, Model.getBoardByName listener) {
        db.collection(Board.COLLECTION_NAME).document(boardName).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Board board = null;
                        if (task.isSuccessful() & task.getResult()!=null) {
                          board = Board.create(task.getResult().getData());

                        }
                        listener.onComplete(board);
                    }
                });


    }
}
