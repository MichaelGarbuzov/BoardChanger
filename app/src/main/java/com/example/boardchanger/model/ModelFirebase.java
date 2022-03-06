package com.example.boardchanger.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.Display;

import androidx.annotation.NonNull;

import com.example.boardchanger.model.posts.Board;
import com.example.boardchanger.model.users.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ModelFirebase {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseAuth userAuth = FirebaseAuth.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference userDb = FirebaseDatabase.getInstance().getReference("users");
    User user = null;

    public ModelFirebase() {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false).build();
        db.setFirestoreSettings(settings);
    }

    public void updateUserName(String newName) {
        DocumentReference userRef = db.collection(User.COLLECTION_NAME).document(userAuth.getCurrentUser().getEmail());
        userRef.update("name", newName);
    }

    public void updateUserPassword(String password) {
        userAuth.getCurrentUser().updatePassword(password);
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

    public void saveImage(Bitmap imageBitmap, String imageName,String imageCat, Model.SaveImageListener listener) {

        StorageReference storageRef = storage.getReference();

        StorageReference imgRef = storageRef.child(imageCat + imageName);

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


    public void getUserByEmail(Model.getUserByEmail listener) {
        String userEmail = userAuth.getCurrentUser().getEmail();
        db.collection(User.COLLECTION_NAME).document(userEmail).get()
                .addOnCompleteListener(task -> {
                    User user = null;
                    if (task.isSuccessful() & task.getResult() != null) {
                        user = User.create(task.getResult().getData());

                    }
                    listener.onComplete(user);
                });

    }

    public void addUser(User user, Model.CompleteListener listener) {
        Map<String, Object> json = user.toJson();

        db.collection(User.COLLECTION_NAME)
                .document(user.getEmail())
                .set(json)
                .addOnSuccessListener(unused -> listener.onComplete())
                .addOnFailureListener(e -> listener.onComplete());

    }

    public void update(Map<String, Object> userMap, Bitmap imageBitMap ,Model.CompleteListener listener) {
        DocumentReference userRef = db.collection(User.COLLECTION_NAME).document(userMap.get("email").toString());

        if(imageBitMap != null) {
            saveImage(imageBitMap, userMap.get("name") + ".jpg", "/user_pictures/", new Model.SaveImageListener() {
                @Override
                public void onComplete(String url) {
                    userMap.put("imageUrl", url);
                    userRef.update(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            listener.onComplete();
                        }
                    });
                }
            });
        } else {
            userRef.update(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    listener.onComplete();
                }
            });
        }
    }
}
