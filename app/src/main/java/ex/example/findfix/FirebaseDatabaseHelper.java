package ex.example.findfix;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferencePhotos;
    private List<Photo> photos = new ArrayList<>();

    public interface DataStatus{
        void DataIsLoaded(List<Photo> photos, List<String> keys);
        void DataInserted();
        void DataUpdated();
        void DataIsDeleted();

    }
    public FirebaseDatabaseHelper() {
        mDatabase= FirebaseDatabase.getInstance();
        mReferencePhotos=mDatabase.getReference("photos");
    }
  public void readPhotos(final DataStatus dataStatus ){
     mReferencePhotos.addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
          photos.clear();
          List<String> keys = new ArrayList<>() ;
         for (DataSnapshot keyNode : dataSnapshot.getChildren()){
             keys.add(keyNode.getKey());
             Photo photo = keyNode.getValue(Photo.class);
             photos.add(photo);
         }
         dataStatus.DataIsLoaded(photos,keys);
         }

         @Override
         public void onCancelled(@NonNull DatabaseError databaseError) {

         }
     });
  }
  public void addPhoto(Photo photo,final DataStatus dataStatus){
       String key = mReferencePhotos.push().getKey();
       mReferencePhotos.child(key).setValue(photo)
               .addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void aVoid) {
                       dataStatus.DataInserted();
                   }
               });
  }
}
