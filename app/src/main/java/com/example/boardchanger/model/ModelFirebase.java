package com.example.boardchanger.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ModelFirebase {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public ModelFirebase() {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false).build();
        db.setFirestoreSettings(settings);
    }

    public interface GetAllBoardsListener {
        void onComplete(List<Board> boardsList);
    }

    public void getAllBoards(Long lastUpdateDate, GetAllBoardsListener listener) {
        db.collection(Board.COLLECTION_NAME)
                .get().addOnCompleteListener(task -> {
            List<Board> list = new LinkedList<Board>();
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    Board board = Board.create(doc.getData());
                    if (board != null) {
                        list.add(board);
                    }
                }
            }
            listener.onComplete(list);
        });
    }

    public void addBoard(Board board, Model.AddBoardListener listener) {
        Map<String, Object> json = board.toJson();

        db.collection(Board.COLLECTION_NAME)
                .document(board.getName())
                .set(json)
                .addOnSuccessListener(unused -> listener.onComplete())
                .addOnFailureListener(e -> listener.onComplete());

    }

    public void getBoardByName(String boardName, Model.getBoardByName listener) {
        db.collection(Board.COLLECTION_NAME).document(boardName).get()
                .addOnCompleteListener(task -> {
                    Board board = null;
                    if (task.isSuccessful() & task.getResult() != null) {
                        board = Board.create(task.getResult().getData());

                    }
                    listener.onComplete(board);
                });


    }

    public void saveImage(Bitmap imageBitmap, String imageName, Model.SaveImageListener listener) {

        StorageReference storageRef = storage.getReference();

        StorageReference imgRef = storageRef.child("/board_pictures/" + imageName);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imgRef.putBytes(data);
        uploadTask.addOnFailureListener(exception -> {
            listener.onComplete(null);
        }).addOnSuccessListener(taskSnapshot -> {
            imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                Uri downloadUrl = uri;
                listener.onComplete(downloadUrl.toString());
            });

        });
    }
    public boolean isConnected(){
        FirebaseUser currUser = mAuth.getCurrentUser();
        return (currUser != null);
    }

}
