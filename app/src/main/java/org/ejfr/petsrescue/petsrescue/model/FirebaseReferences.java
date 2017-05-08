package org.ejfr.petsrescue.petsrescue.model;

/**
 * Created by EmilioJosé on 27/04/2017.
 */

public class FirebaseReferences {

    public static final String DATABASE_NAME_REFERENCE = "petsrescue_db";
    public static final String PETS_REFERENCE ="pets";
    public static final String DATABASE_USER ="petsalert@petsrescue.org";
    public static final String DATABASE_PASSWORD = "p3tsr3scu3";
    public static final String FIREBASE_STORAGE_REFERENCE_NAME="petsrescue_images";
    public static final String FIREBASE_STORAGE_REFERENCE_PHOTOS="photos";

    /*
https://firebase.google.com/docs/storage/android/upload-files?hl=es-419


      // Create a storage reference from our app
StorageReference storageRef = storage.getReferenceFromUrl("gs://<your-bucket-name>");

// Create a reference to "mountains.jpg"
StorageReference mountainsRef = storageRef.child("mountains.jpg");

// Create a reference to 'images/mountains.jpg'
StorageReference mountainImagesRef = storageRef.child("images/mountains.jpg");

// While the file names are the same, the references point to different files
mountainsRef.getName().equals(mountainImagesRef.getName());    // true
mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false



     */
}
