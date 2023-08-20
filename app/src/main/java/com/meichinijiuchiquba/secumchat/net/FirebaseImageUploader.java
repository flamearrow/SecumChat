package com.meichinijiuchiquba.secumchat.net;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;

import javax.inject.Inject;

import static com.meichinijiuchiquba.secumchat.injection.FirebaseModule
        .FIREBASE_PROFILE_IMAGE_STOREAGE_BASE_PATH;

public class FirebaseImageUploader {
    public interface ImageUploadListener {
        void onLoadStarted();

        void onLoadSuccess(String imageUrl);

        void onLoadFailure();
    }

    private FirebaseStorage firebaseStorage;

    @Inject
    public FirebaseImageUploader(FirebaseStorage firebaseStorage) {
        this.firebaseStorage = firebaseStorage;
    }

    public void uploadImage(Bitmap image, String imageName, ImageUploadListener listener) {
        StorageReference reference = firebaseStorage.getReference
                (FIREBASE_PROFILE_IMAGE_STOREAGE_BASE_PATH).child(imageName + ".png");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);

        // Following https://firebase.google
        // .com/docs/storage/android/upload-files#get_a_download_url
        // this is so fucking confusing
        reference.putBytes(baos.toByteArray()).continueWithTask(
                task -> {
                    listener.onLoadStarted();
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();
                }
        ).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                listener.onLoadSuccess(downloadUri.toString());

            } else {
                listener.onLoadFailure();
            }
        });
    }

}
