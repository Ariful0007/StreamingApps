package com.meass.streamingapps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class DetailsGiveActivity extends AppCompatActivity {
    ImageView profileimage,nid_font,nid_font_vack;
    StorageReference storageReference;

    FirebaseFirestore firebaseFirestore;
    UserSession session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_give);

        profileimage=findViewById(R.id.profileimage);
        nid_font=findViewById(R.id.nid_font);
        nid_font_vack=findViewById(R.id.nid_font_vack);



        Toolbar toolbar = findViewById(R.id.profile_toolbar);
        toolbar.setTitle("Details of Host");
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_myarrow);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_myarrow);
        getSupportActionBar().setElevation(10.0f);
        getSupportActionBar().setElevation(10.0f);
        /////imageview
        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(getApplicationContext())
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intent, 101);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });
        nid_font.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(getApplicationContext())
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intent, 102);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });
        nid_font_vack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(getApplicationContext())
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intent, 103);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });
        storageReference = FirebaseStorage.getInstance().getReference();

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        session=new UserSession(DetailsGiveActivity.this);
    }
    FirebaseAuth firebaseAuth;

    int flag=1,flag2=1,flag3=1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK) {
            videouri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), videouri);
                profileimage.setImageBitmap(bitmap);
                flag=2;
            } catch (IOException e) {
                e.printStackTrace();
            }



        }
        if (requestCode == 102 && resultCode == RESULT_OK) {
            videouri1 = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), videouri1);
                nid_font.setImageBitmap(bitmap);
                flag2=2;
            } catch (IOException e) {
                e.printStackTrace();
            }



        }
        if (requestCode == 103 && resultCode == RESULT_OK) {
            videouri2 = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), videouri2);
                nid_font_vack.setImageBitmap(bitmap);
                flag3=2;
            } catch (IOException e) {
                e.printStackTrace();
            }



        }

    }
    Uri videouri,videouri1,videouri2;

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(DetailsGiveActivity.this);
        builder.setTitle("Warning")
                .setMessage("You can not back from this activity?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        builder.show();
    }
    public String getExtension() {
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(getContentResolver().getType(videouri));
    }
    public void user(View view) {
        if (flag3==1||flag2==1||flag==1) {
            Toast.makeText(this, "Please select all images", Toast.LENGTH_SHORT).show();
        }
        else {
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setTitle("Media Uploader");
            pd.setCancelable(false);
            pd.show();

            final StorageReference uploader = storageReference.child("myvideos/" + System.currentTimeMillis() + "." );
            uploader.putFile(videouri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String one=uri.toString();


                                    Map<String, Object> user = new HashMap<>();
                                    user.put("image", one);
                                    user.put("nid", one);
                                    user.put("nid_back", one);
                                    user.put("uuid",firebaseAuth.getCurrentUser().getUid());
                                    firebaseFirestore.collection("RequestForHost")
                                            .document(firebaseAuth.getCurrentUser().getUid())
                                            .set(user)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Handler handler=new Handler();
                                                        handler.postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {

                                                                Toasty.success(getApplicationContext(), "Account Create  Successfully Done.", Toasty.LENGTH_SHORT, true).show();

                                                                firebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                                        if (task.isSuccessful()) {

                                                                            if (task.getResult().exists()) {


                                                                                String sessionname = task.getResult().getString("name");
                                                                                String sessionmobile = task.getResult().getString("number");
                                                                                String sessionphoto = task.getResult().getString("image");
                                                                                String sessionemail = task.getResult().getString("email");
                                                                                String sessionusername = task.getResult().getString("username");


                                                                                session.createLoginSession(sessionname,sessionemail,sessionmobile,sessionphoto,sessionusername);




                                                                                Intent loginSuccess = new Intent(DetailsGiveActivity.this, HomeActivity.class);

                                                                                startActivity(loginSuccess);
                                                                                finish();

                                                                            }
                                                                        } else {
                                                                            firebaseAuth.signOut();

                                                                            Toast.makeText(DetailsGiveActivity.this, "Login Error. Please try again.", Toast.LENGTH_SHORT).show();
                                                                        }

                                                                    }
                                                                });
                                                            }
                                                        },1000);
                                                    }
                                                }
                                            });


                                }
                            });
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            float per = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                            pd.setMessage("Uploaded :" + (int) per + "%");
                        }
                    });
        }
    }
}