package com.example.boardchanger.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;
import android.window.SplashScreen;
import android.window.SplashScreenView;

import androidx.annotation.NonNull;

import com.example.boardchanger.model.posts.Board;
import com.example.boardchanger.model.users.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    FirebaseUser user = userAuth.getCurrentUser();
    DatabaseReference boardRef = FirebaseDatabase.getInstance().getReference().child("boards");

    public ModelFirebase() {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false).build();
        db.setFirestoreSettings(settings);
    }

    public void editBoard(Board board, Model.CompleteListener listener) {
        DocumentReference boardRef = db.collection(Board.COLLECTION_NAME).document(board.getId());
        boardRef.update(board.toJson()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    listener.onComplete();
                }
            }

        });
    }


    public void deleteBoard(Board board, Model.CompleteListener listener) {
        db.collection(Board.COLLECTION_NAME).document(board.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    listener.onComplete();
                }
                else{listener.onError();}
            }
        });
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
                    if (board != null && board.getDeleted() == false) {
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
                .document(board.getId())
                .set(json)
                .addOnSuccessListener(unused -> listener.onComplete())
                .addOnFailureListener(e -> listener.onComplete());

    }

    public void getBoardByID(String boardID, Model.getBoardByID listener) {
        db.collection(Board.COLLECTION_NAME).document(boardID).get()
                .addOnCompleteListener(task -> {
                    Board board = null;
                    if (task.isSuccessful() & task.getResult() != null) {
                        board = Board.create(task.getResult().getData());
                    }
                    listener.onComplete(board);
                });


    }

    public void saveImage(Bitmap imageBitmap, String imageName, String imageCat, Model.SaveImageListener listener) {

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
                        user = User.getInstance().create(task.getResult().getData());

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

    public void updateUser(Map<String, Object> userMap, Bitmap imageBitMap, Model.CompleteListener listener) {
        DocumentReference userRef = db.collection(User.COLLECTION_NAME).document(userMap.get("email").toString());
        final String email = user.getEmail();
        String oldPass = User.getInstance().getPassword();
        AuthCredential credential = EmailAuthProvider.getCredential(email, oldPass);
        authenticateUser(credential, () -> user.updatePassword(userMap.get("password").toString()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (imageBitMap != null) {
                    saveImage(imageBitMap, userMap.get("name") + ".jpg", "/user_pictures/", url -> {
                        userMap.put("imageUrl", url);
                        userRef.update(userMap).addOnCompleteListener(task1 -> listener.onComplete());
                    });
                } else {
                    userRef.update(userMap).addOnCompleteListener(task12 -> listener.onComplete());
                }
            }
        }));
    }

    private interface AuthenticateUserListener {
        void onSuccess();
    }

    private void authenticateUser(AuthCredential credential, AuthenticateUserListener listener) {
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    listener.onSuccess();
                }
            }
        });
    }
}
