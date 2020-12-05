//package com.antoniuswicaksana.project_pbp.UnitTestRegister;
//
//import android.app.ProgressDialog;
//import android.util.Log;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class RegisterService {
//
//    public void login(final RegisterView view, String email, String password){
//        final ProgressDialog progressDialog;
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("loading....");
//        progressDialog.setTitle("Register in Progress");
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progressDialog.show();
//
//        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if(task.isSuccessful()) {
//                    // send verification link
//                    firebaseUser = firebaseAuth.getCurrentUser();
//                    firebaseUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            Toast.makeText(RegisterActivity.this, "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.d("TAG", "onFailure: Email not sent " + e.getMessage());
//                        }
//                    });
//
//                    // SAVE USER TO DATABASE
//                    encryptedPW = encrypt(AESPassword, password);
//                    user.setPassword(encryptedPW);
//                    userID = firebaseUser.getUid();
//                    databaseReference.child(userID).setValue(user);
//
//                    // SAVE IMAGE TO STORAGE
////                    imageReferences = storageReference.child(userID).child("Images").child("Profile Pic"); //{userID}/Images/Profile Pic.jpg
////                    UploadTask uploadTask = imageReferences.putFile(imagePath);
//
//                    Toast.makeText(RegisterActivity.this, "User Has Been Saved", Toast.LENGTH_SHORT).show();
//                    firebaseAuth.signOut();
//                    onBackPressed();
//                } else {
//                    Toast.makeText(RegisterActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                }
//
//                progressDialog.dismiss();
//            }
//        });
//    }
//
//    public Boolean getValid(final LoginView view, String email, String password){
//        final Boolean[] bool = new Boolean[1];
//        login(view, email, password, new LoginCallback() {
//            @Override
//            public void onSuccess(boolean value, UserDAO user) {
//                bool[0] = true;
//            }
//            @Override
//            public void onError() {
//                bool[0] = false;
//            }
//        });
//        return bool[0];
//    }
//
//}
