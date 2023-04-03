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
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
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
import com.kaopiz.kprogresshud.KProgressHUD;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class CreatChanelActivity extends AppCompatActivity {
    ImageView profileimage,nid_font,nid_font_vack;
    StorageReference storageReference;

    FirebaseFirestore firebaseFirestore;
    UserSession session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_chanel);

        profileimage=findViewById(R.id.profileimage);

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
        storageReference = FirebaseStorage.getInstance().getReference();

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth= FirebaseAuth.getInstance();
        session=new UserSession(CreatChanelActivity.this);
        username=findViewById(R.id.username);
    }
    EditText username;
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


    }
    public String getExtension() {
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(getContentResolver().getType(videouri));
    }
    @Override
    public void onBackPressed() {
    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
    }
    Uri videouri,videouri1,videouri2;
    public void user(View view) {
        if (flag==1|| TextUtils.isEmpty(username.getText().toString())) {
            Toast.makeText(this, "Please selct channel anme and image", Toast.LENGTH_SHORT).show();
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
                                    user.put("name", username.getText().toString());
                                    user.put("image", one);
                                    user.put("uuid", firebaseAuth.getCurrentUser().getUid());
                                    firebaseFirestore.collection("Host_Channel")
                                            .document(firebaseAuth.getCurrentUser().getUid())
                                            .set(user)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        pd.dismiss();
                                                        Toast.makeText(CreatChanelActivity.this, "Done", Toast.LENGTH_SHORT).show();
                                                        AlertDialog.Builder warning = new AlertDialog.Builder(CreatChanelActivity.this)
                                                                .setTitle("Confirmation")
                                                                .setMessage("Do you want to start this live?")
                                                                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        dialog.dismiss();



                                                                    }
                                                                }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        final KProgressHUD progressDialog=  KProgressHUD.create(CreatChanelActivity.this)
                                                                                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                                                                .setLabel("Uploading Data.....")
                                                                                .setCancellable(false)
                                                                                .setAnimationSpeed(2)
                                                                                .setDimAmount(0.5f)
                                                                                .show();
                                                                        // ToDO: delete all the notes created by the Anon user
                                                                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                                                        Calendar cal = Calendar.getInstance();
                                                                        Toast.makeText(CreatChanelActivity.this, ""+firebaseAuth.getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();

                                                                        LiveModel liveModel =new LiveModel(username.getText().toString(),one,""+username.getText().toString(),firebaseAuth.getCurrentUser().getUid(),
                                                                                "99",""+dateFormat.format(cal.getTime()),"0");
                                                                        firebaseFirestore.collection("Livestreams")
                                                                                .document(firebaseAuth.getCurrentUser().getUid())
                                                                                .set(liveModel)
                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        if (task.isSuccessful()) {
                                                                                            Map<String, Object> user = new HashMap<>();
                                                                                            user.put("gift", "0");

                                                                                            firebaseFirestore.collection("GettingGift")
                                                                                                    .document(firebaseAuth.getCurrentUser().getUid())
                                                                                                    .set(user)
                                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                                            if (task.isSuccessful()) {
                                                                                                                progressDialog.dismiss();
                                                                                                                Toasty.success(getApplicationContext(),"Successfully started meeting",Toasty.LENGTH_SHORT,true).show();
                                                                                                                Intent intent=new Intent(getApplicationContext(),JoinAsHost.class);
                                                                                                                intent.putExtra("host",username.getText().toString());
                                                                                                                intent.putExtra("userName",username.getText().toString());
                                                                                                                intent.putExtra("roomname",username.getText().toString());
                                                                                                                intent.putExtra("image",one);
                                                                                                                intent.putExtra("host",username.getText().toString());
                                                                                                                intent.putExtra("host",username.getText().toString());
                                                                                                                intent.putExtra("host",username.getText().toString());
                                                                                                                startActivity(intent);

                                                                                                                return;
                                                                                                            }
                                                                                                        }
                                                                                                    });


                                                                                        }

                                                                                    }
                                                                                });





                                                                    }
                                                                });

                                                        warning.show();

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