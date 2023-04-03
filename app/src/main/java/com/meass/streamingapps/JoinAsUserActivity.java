package com.meass.streamingapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import io.agora.rtc2.ChannelMediaOptions;
import io.agora.rtc2.Constants;
import io.agora.rtc2.IRtcEngineEventHandler;
import io.agora.rtc2.RtcEngine;
import io.agora.rtc2.RtcEngineConfig;
import io.agora.rtc2.video.VideoCanvas;
public class JoinAsUserActivity extends AppCompatActivity {
    private static final int PERMISSION_REQ_ID = 22;
    private static final String[] REQUESTED_PERMISSIONS =
            {
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA
            };

    private boolean checkSelfPermission()
    {
        if (ContextCompat.checkSelfPermission(this, REQUESTED_PERMISSIONS[0]) !=  PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, REQUESTED_PERMISSIONS[1]) !=  PackageManager.PERMISSION_GRANTED)
        {
            return false;
        }
        return true;
    }
    void showMessage(String message) {
        runOnUiThread(() ->
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show());
    }
    // Fill the App ID of your project generated on Agora Console.
    private final String appId = "93fdcff4216a4454a1fb2e92a5bfc6b9";
    // Fill the channel name.
    private String channelName = "jj";
    // Fill the temp token generated on Agora Console.
    private String token = "007eJxTYFj2wuq4LEfXl7em5oULjcLitp1Oy/IsmRTNyLLyZ0PehdsKDJbGaSnJaWkmRoZmiSYmpiaJhmlJRqmWRommSWnJZkmWGq5aKQ2BjAwnVxxhYWSAQBCfiSEri4EBAB7iHwM=";
    // An integer that identifies the local user.
    private int uid = 0;
    private boolean isJoined = false;

    private RtcEngine agoraEngine;
    //SurfaceView to render local video in a Container.
    private SurfaceView localSurfaceView;
    //SurfaceView to render Remote video in a Container.
    private SurfaceView remoteSurfaceView;
    // A toggle switch to change the User role.
    private Switch audienceRole;


    String new_1;
    String host,roomID,appID,appSign,userID,userName,roomname,image,audience,uuid;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,databaseReference1,databaseReference2,databaseReference3,databaseReference4,
            databaseReference5,databaseReference6,databaseReference7,databaseReference8,databaseReference9;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    EditText input_messages;
    ImageButton send_message_btn;
    LinearLayout linear1,linear2,linear3,linear4,linear5,linear6,linear7,linear8;
    TextView name1,name2,name3,name4,name5,name6,name7,name8;
    ImageView real1,real2,real3,real4,real5,real6,real7,real8;
    String mychanel,mytokn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_as_user);
        audienceRole = (Switch) findViewById(R.id.switch1);
        // If all the permissions are granted, initialize the RtcEngine object and join a channel.
        if (!checkSelfPermission()) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, PERMISSION_REQ_ID);
        }
        setupVideoSDKEngine();
        ////joincall
        if (checkSelfPermission()) {
            ChannelMediaOptions options = new ChannelMediaOptions();
            // For Live Streaming, set the channel profile as LIVE_BROADCASTING.
            options.channelProfile = Constants.CHANNEL_PROFILE_LIVE_BROADCASTING;
            // Set the client role as BROADCASTER or AUDIENCE according to the scenario.
            if (audienceRole.isChecked()) { //Audience
                Toast.makeText(JoinAsUserActivity.this, "You can talk with host now.", Toast.LENGTH_SHORT).show();
                options.clientRoleType = Constants.CLIENT_ROLE_AUDIENCE;
                //
                //remote video







                FrameLayout container = findViewById(R.id.local_video_view_container);
                // Create a SurfaceView object and add it as a child to the FrameLayout.
                localSurfaceView = new SurfaceView(getBaseContext());
                container.addView(localSurfaceView);
                // Call setupLocalVideo with a VideoCanvas having uid set to 0.
                agoraEngine.setupRemoteVideo(new VideoCanvas(localSurfaceView, VideoCanvas.RENDER_MODE_HIDDEN, 0));
            } else { //Host
                Toast.makeText(JoinAsUserActivity.this, "You can talk with host now.", Toast.LENGTH_SHORT).show();
                options.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER;
                // Display LocalSurfaceView.
                FrameLayout container = findViewById(R.id.local_video_view_container);
                // Create a SurfaceView object and add it as a child to the FrameLayout.
                localSurfaceView = new SurfaceView(getBaseContext());
                container.addView(localSurfaceView);
                // Call setupLocalVideo with a VideoCanvas having uid set to 0.
                agoraEngine.setupLocalVideo(new VideoCanvas(localSurfaceView, VideoCanvas.RENDER_MODE_HIDDEN, 0));
                localSurfaceView.setVisibility(View.VISIBLE);
                // Start local preview.
                agoraEngine.startPreview();
            }
            audienceRole.setEnabled(false); // Disable the switch
            // Join the channel with a temp token.
            // You need to specify the user ID yourself, and ensure that it is unique in the channel.
            agoraEngine.joinChannel(token, channelName, uid, options);
        } else {
            Toast.makeText(getApplicationContext(), "Permissions was not granted", Toast.LENGTH_SHORT).show();
        }
        Button JoinButton=findViewById(R.id.JoinButton);
        JoinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission()) {
                    ChannelMediaOptions options = new ChannelMediaOptions();
                    // For Live Streaming, set the channel profile as LIVE_BROADCASTING.
                    options.channelProfile = Constants.CHANNEL_PROFILE_LIVE_BROADCASTING;
                    // Set the client role as BROADCASTER or AUDIENCE according to the scenario.
                    if (audienceRole.isChecked()) { //Audience
                        Toast.makeText(JoinAsUserActivity.this, "You can talk with host now.", Toast.LENGTH_SHORT).show();
                        options.clientRoleType = Constants.CLIENT_ROLE_AUDIENCE;
                        //
                        //remote video







                        FrameLayout container = findViewById(R.id.local_video_view_container);
                        // Create a SurfaceView object and add it as a child to the FrameLayout.
                        localSurfaceView = new SurfaceView(getBaseContext());
                        container.addView(localSurfaceView);
                        // Call setupLocalVideo with a VideoCanvas having uid set to 0.
                        agoraEngine.setupRemoteVideo(new VideoCanvas(localSurfaceView, VideoCanvas.RENDER_MODE_HIDDEN, 0));
                    } else { //Host
                        Toast.makeText(JoinAsUserActivity.this, "You can talk with host now.", Toast.LENGTH_SHORT).show();
                        options.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER;
                        // Display LocalSurfaceView.
                        FrameLayout container = findViewById(R.id.local_video_view_container);
                        // Create a SurfaceView object and add it as a child to the FrameLayout.
                        localSurfaceView = new SurfaceView(getBaseContext());
                        container.addView(localSurfaceView);
                        // Call setupLocalVideo with a VideoCanvas having uid set to 0.
                        agoraEngine.setupLocalVideo(new VideoCanvas(localSurfaceView, VideoCanvas.RENDER_MODE_HIDDEN, 0));
                        localSurfaceView.setVisibility(View.VISIBLE);
                        // Start local preview.
                        agoraEngine.startPreview();
                    }
                    audienceRole.setEnabled(false); // Disable the switch
                    // Join the channel with a temp token.
                    // You need to specify the user ID yourself, and ensure that it is unique in the channel.
                    agoraEngine.joinChannel(token, channelName, uid, options);
                } else {
                    Toast.makeText(getApplicationContext(), "Permissions was not granted", Toast.LENGTH_SHORT).show();
                }
            }
        });
    ////raw section
     LottieAnimationView animationView1=findViewById(R.id.animationView1);

        ///textview
        name1=findViewById(R.id.name1);
        name2=findViewById(R.id.name2);
        name3=findViewById(R.id.name3);
        name4=findViewById(R.id.name4);
        name5=findViewById(R.id.name5);
        name6=findViewById(R.id.name6);
        name7=findViewById(R.id.name7);
        name8=findViewById(R.id.name8);
        ///imageview
        real1=findViewById(R.id.real1);
        real2=findViewById(R.id.real2);
        real3=findViewById(R.id.real3);
        real4=findViewById(R.id.real4);
        real5=findViewById(R.id.real5);
        real6=findViewById(R.id.real6);
        real7=findViewById(R.id.real7);
        real8=findViewById(R.id.real8);





        /////
        linear1=findViewById(R.id.linear1);
        linear2=findViewById(R.id.linear2);
        linear3=findViewById(R.id.linear3);
        linear4=findViewById(R.id.linear4);
        linear5=findViewById(R.id.linear5);
        linear6=findViewById(R.id.linear6);
        linear7=findViewById(R.id.linear7);
        linear8=findViewById(R.id.linear8);


        input_messages=findViewById(R.id.input_messages);
        send_message_btn=findViewById(R.id.send_message_btn);


        Toolbar toolbar = findViewById(R.id.tollbar);

        toolbar.setTitle("Live");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setElevation(10.0f);

        /////
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();


        try {
            new_1="1";
            uuid=firebaseAuth.getCurrentUser().getUid();
            host=getIntent().getStringExtra("host");
            roomID="9899";
            appID="f50ae2f9b6f3433d9cac72a4e268e012";
            appSign="f50ae2f9b6f3433d9cac72a4e268e012";
            userID=firebaseAuth.getCurrentUser().getUid();
            userName=getIntent().getStringExtra("userName");
            roomname=getIntent().getStringExtra("roomname");
            image=getIntent().getStringExtra("image");
            audience="0";

        }catch (Exception e) {
            new_1="1";
            uuid=firebaseAuth.getCurrentUser().getUid();
            host=getIntent().getStringExtra("host");
            roomID="9899";
            appID="f50ae2f9b6f3433d9cac72a4e268e012";
            appSign="f50ae2f9b6f3433d9cac72a4e268e012";
            userID=firebaseAuth.getCurrentUser().getUid();
            userName=getIntent().getStringExtra("userName");
            roomname=userName;
            image=getIntent().getStringExtra("image");
            audience="0";
        }
        //Toast.makeText(this, ""+host, Toast.LENGTH_SHORT).show();
        ImageView hostimage=findViewById(R.id.hostimage);
        TextView histname=findViewById(R.id.histname);
        try {
            firebaseFirestore=FirebaseFirestore.getInstance();
            firebaseFirestore.collection("Host_Channel")
                    .document(host)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().exists()) {
                                   try {
                                       Picasso.get().load(task.getResult().getString("image")).into(hostimage);
                                       //Picasso.get().load(image).into(hostimage);
                                       histname.setText(task.getResult().getString("name"));
                                   }catch (Exception e) {
                                   }
                                }
                            }
                        }
                    });
            String image22="https://firebasestorage.googleapis.com/v0/b/cash-money-express-ltd.appspot.com/o/profile_images%2Fo8Dnqf5LFodKSwocGQ4nKB7ZEkW2.jpg?alt=media&token=c22700e2-67ca-4497-8bf1-204ac83b6749";
            String  picture = image;
            firebaseFirestore.collection("Users")
                    .document(firebaseAuth.getCurrentUser().getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().exists()) {
                                    try {
                                        name1.setText(task.getResult().getString("name"));
                                    }catch (Exception e) {
                                        name1.setText(task.getResult().getString("name"));
                                    }
                                }
                            }
                        }
                    });

        }catch (Exception e) {

        }
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseFirestore= FirebaseFirestore.getInstance();


////





        /////////messageing section
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        // Toast.makeText(this, ""+auth.getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();

        mymessage =(RecyclerView) findViewById(R.id.mymessage);
        mymessage.setLayoutManager(new LinearLayoutManager(this));

        /////////messageing section
        send_message_btn=findViewById(R.id.send_message_btn);
        send_message_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message=input_messages.getText().toString();
                if (TextUtils.isEmpty(message)) {
                    Toast.makeText(JoinAsUserActivity.this, "Enter Message", Toast.LENGTH_SHORT).show();


                }
                else {
                    long lo=System.currentTimeMillis()/1000;
                    LiveChatModel liveChatModel=new LiveChatModel(userName,uuid,image,roomname,""+lo,message,userName,auth.getCurrentUser().getEmail());

                    firebaseFirestore.collection("LiveMessgae")
                            .document(host)
                            .collection("List")
                            .document(""+lo)
                            .set(liveChatModel)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(JoinAsUserActivity.this, "Sent", Toast.LENGTH_SHORT).show();
                                        reloadMessage();
                                    }
                                }
                            });
                }
            }
        });
        //////retrive

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        // Toast.makeText(this, ""+auth.getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();

        mymessage =(RecyclerView) findViewById(R.id.mymessage);
        mymessage.setLayoutManager(new LinearLayoutManager(this));




        /////gift
        ImageView send_files_btn=findViewById(R.id.send_files_btn);
        send_files_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cohostshow();
            }
        });
        /////////calling
        if (!checkSelfPermission()) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, PERMISSION_REQ_ID);
        }
        String appSign = "f50ae2f9b6f3433d9cac72a4e268e012";
        String userID = firebaseAuth.getCurrentUser().getUid();
        String userName = getIntent().getStringExtra("userName");
        String roomID = "9899";
        String roomname = userName;

        setupVideoSDKEngine();

///////////////////////////////////////////mute and unmute







    }
    private void reloadMessage() {
        DocumentReference documentReference;
        RecyclerView recyclerView;
        LiveChatAdapter getDataAdapter1;
        List<LiveChatModel> getList;
        String url;

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        getList = new ArrayList<>();
        getDataAdapter1 = new LiveChatAdapter(getList);
        firebaseFirestore = FirebaseFirestore.getInstance();
        documentReference =    firebaseFirestore.collection("LiveMessgae")
                .document(host)
                .collection("List")
                .document();

        mymessage.setHasFixedSize(true);
        mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mymessage.setLayoutManager(mLayoutManager);
        mymessage.post(new Runnable() {
            @Override
            public void run() {
                // Call smooth scroll

            }
        });
        mymessage.smoothScrollToPosition(getDataAdapter1.getItemCount());
        //recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
        mymessage.setAdapter(getDataAdapter1);
        firebaseFirestore.collection("LiveMessgae")
                .document(host)
                .collection("List").orderBy("time", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange ds : queryDocumentSnapshots.getDocumentChanges()) {
                    if (ds.getType() == DocumentChange.Type.ADDED) {

                 /*String first;
                 first = ds.getDocument().getString("name");
                 Toast.makeText(MainActivity2.this, "" + first, Toast.LENGTH_SHORT).show();*/
                        LiveChatModel get = ds.getDocument().toObject(LiveChatModel.class);
                        getList.add(get);
                        getDataAdapter1.notifyDataSetChanged();
                    }

                }
            }
        });

    }
    StaggeredGridLayoutManager mLayoutManager;


    Runnable updater;
    DatabaseReference databaseReference65;
    FirebaseFirestore firebaseFirestore;
    private void check() {

    }


    int muteflag=1;
    DatabaseReference databaseReferenceadmincheeked;
    private void showBottomSheetDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(JoinAsUserActivity.this);






        bottomSheetDialog.show();
    }
    private void cohostshow() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(JoinAsUserActivity.this);

        bottomSheetDialog.setContentView(R.layout.giftlistt);
        ImageView myhostimage=(ImageView)bottomSheetDialog.findViewById(R.id.myhostimage);
        TextView myhostname=(TextView)bottomSheetDialog.findViewById(R.id.myhostname);
        try {
            String image22="https://firebasestorage.googleapis.com/v0/b/cash-money-express-ltd.appspot.com/o/profile_images%2Fo8Dnqf5LFodKSwocGQ4nKB7ZEkW2.jpg?alt=media&token=c22700e2-67ca-4497-8bf1-204ac83b6749";
            String  picture = image;
            if (picture.equals(image22)) {
                Picasso.get().load(R.drawable.app_logo).into(myhostimage);
                //holder.coins.setText(data.get(position).getName());
            }
            else {
                Picasso.get().load(image).into(myhostimage);
                ///holder.coins.setText(data.get(position).getName());
            }
            //Picasso.get().load(image).into(hostimage);
            myhostname.setText(roomname);
        }catch (Exception e) {
            String image22="https://firebasestorage.googleapis.com/v0/b/cash-money-express-ltd.appspot.com/o/profile_images%2Fo8Dnqf5LFodKSwocGQ4nKB7ZEkW2.jpg?alt=media&token=c22700e2-67ca-4497-8bf1-204ac83b6749";
            String  picture = image;
            if (picture.equals(image22)) {
                Picasso.get().load(R.drawable.app_logo).into(myhostimage);
                //holder.coins.setText(data.get(position).getName());
            }
            else {
                Picasso.get().load(image).into(myhostimage);
                ///holder.coins.setText(data.get(position).getName());
            }
            //Picasso.get().load(image).into(hostimage);
            myhostname.setText(roomname);
        }
        RecyclerView mymessage_rechost=(RecyclerView)bottomSheetDialog.findViewById(R.id.mymessage_rechost);
        DocumentReference documentReference;
        RecyclerView recyclerView;
        GiftAdapter getDataAdapter1;
        List<GiftModel> getList;
        String url;
        FirebaseFirestore firebaseFirestore;
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        getList = new ArrayList<>();
        getDataAdapter1 = new GiftAdapter(getList,host);
        firebaseFirestore = FirebaseFirestore.getInstance();
        documentReference =    firebaseFirestore.collection("Gifts")
                .document();

        mymessage_rechost.setHasFixedSize(true);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mymessage_rechost.setLayoutManager(mLayoutManager);

        //recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
        mymessage_rechost.setAdapter(getDataAdapter1);
        firebaseFirestore.collection("Gifts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange ds : queryDocumentSnapshots.getDocumentChanges()) {
                    if (ds.getType() == DocumentChange.Type.ADDED) {

                 /*String first;
                 first = ds.getDocument().getString("name");
                 Toast.makeText(MainActivity2.this, "" + first, Toast.LENGTH_SHORT).show();*/
                        GiftModel get = ds.getDocument().toObject(GiftModel.class);
                        getList.add(get);
                        getDataAdapter1.notifyDataSetChanged();
                    }

                }
            }
        });


        bottomSheetDialog.show();
    }


    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onStop() {
        super.onStop();

    }
    LiveChatAdapter mainAdapter;
    RecyclerView mymessage;
    private FirebaseAuth auth;
    private FirebaseUser user;
    int flag=0;
    @Override
    public boolean onNavigateUp() {
        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        return super.onNavigateUp();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menlu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int menuitem=item.getItemId();
        if (menuitem==R.id.emergency) {
            Toast.makeText(this, "Coming", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        }
        if (menuitem==R.id.covid) {
            DatabaseReference dd=firebaseDatabase.getReference().child(roomname).child(auth.getCurrentUser().getUid());
            AlertDialog.Builder builder=new AlertDialog.Builder(JoinAsUserActivity.this);
            builder.setTitle("Confirmation")
                    .setMessage("Do you want to exit from this live?")
                    .setPositiveButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();

                        }
                    }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    RtcEngine.destroy();
                    agoraEngine.leaveChannel();
                    showMessage("You left the channel");
                    // Stop remote video rendering.
                    if (remoteSurfaceView != null) remoteSurfaceView.setVisibility(View.GONE);
                    // Stop local video rendering.
                    if (localSurfaceView != null) localSurfaceView.setVisibility(View.GONE);
                    isJoined = false;
                    RtcEngine.destroy();

                }
            }).create().show();


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(JoinAsUserActivity.this);
        builder.setTitle("Confirmation")
                .setMessage("Do you want to exit from this live?")
                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                agoraEngine.leaveChannel();
                showMessage("You left the channel");
                // Stop remote video rendering.
                if (remoteSurfaceView != null) remoteSurfaceView.setVisibility(View.GONE);
                // Stop local video rendering.
                if (localSurfaceView != null) localSurfaceView.setVisibility(View.GONE);
                isJoined = false;
                RtcEngine.destroy();
            }
        }).create().show();

    }
    protected void onDestroy() {
        super.onDestroy();
        agoraEngine.stopPreview();
        agoraEngine.leaveChannel();

        // Destroy the engine in a sub-thread to avoid congestion
        new Thread(() -> {
            RtcEngine.destroy();
            agoraEngine = null;
        }).start();
    }
    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        // Listen for the remote host joining the channel to get the uid of the host.
        public void onUserJoined(int uid, int elapsed) {
            showMessage("Remote user joined " + uid);
            if (!audienceRole.isChecked()) return;
            // Set the remote video view
            runOnUiThread(() -> setupRemoteVideo(uid));
        }
        private void setupRemoteVideo(int uid) {
            FrameLayout container = findViewById(R.id.remote_video_view_container);
            remoteSurfaceView = new SurfaceView(getBaseContext());
            remoteSurfaceView.setZOrderMediaOverlay(true);
            container.addView(remoteSurfaceView);
            agoraEngine.setupRemoteVideo(new VideoCanvas(remoteSurfaceView, VideoCanvas.RENDER_MODE_FIT, uid));
            // Display RemoteSurfaceView.
            remoteSurfaceView.setVisibility(View.VISIBLE);
        }
        public void joinChannel(View view) {
            if (checkSelfPermission()) {
                ChannelMediaOptions options = new ChannelMediaOptions();
                // For Live Streaming, set the channel profile as LIVE_BROADCASTING.
                options.channelProfile = Constants.CHANNEL_PROFILE_LIVE_BROADCASTING;
                // Set the client role as BROADCASTER or AUDIENCE according to the scenario.
                if (audienceRole.isChecked()) { //Audience
                    options.clientRoleType = Constants.CLIENT_ROLE_AUDIENCE;
                } else { //Host
                    options.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER;
                    // Display LocalSurfaceView.
                    setupLocalVideo();
                    localSurfaceView.setVisibility(View.VISIBLE);
                    // Start local preview.
                    agoraEngine.startPreview();
                }
                audienceRole.setEnabled(false); // Disable the switch
                // Join the channel with a temp token.
                // You need to specify the user ID yourself, and ensure that it is unique in the channel.
                agoraEngine.joinChannel(token, channelName, uid, options);
            } else {
                Toast.makeText(getApplicationContext(), "Permissions was not granted", Toast.LENGTH_SHORT).show();
            }
        }
        public void leaveChannel(View view) {
            if (!isJoined) {
                showMessage("Join a channel first");
            } else {
                agoraEngine.leaveChannel();
                showMessage("You left the channel");
                // Stop remote video rendering.
                if (remoteSurfaceView != null) remoteSurfaceView.setVisibility(View.GONE);
                // Stop local video rendering.
                if (localSurfaceView != null) localSurfaceView.setVisibility(View.GONE);
                isJoined = false;
            }
            audienceRole.setEnabled(true); // Enable the switch
        }
        private void setupLocalVideo() {
            FrameLayout container = findViewById(R.id.local_video_view_container);
            // Create a SurfaceView object and add it as a child to the FrameLayout.
            localSurfaceView = new SurfaceView(getBaseContext());
            container.addView(localSurfaceView);
            // Call setupLocalVideo with a VideoCanvas having uid set to 0.
            agoraEngine.setupLocalVideo(new VideoCanvas(localSurfaceView, VideoCanvas.RENDER_MODE_HIDDEN, 0));
        }
        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
            isJoined = true;
            showMessage("Joined Channel " + channel);
        }

        @Override
        public void onUserOffline(int uid, int reason) {
            showMessage("Remote user offline " + uid + " " + reason);
            runOnUiThread(() -> remoteSurfaceView.setVisibility(View.GONE));
        }
    };
    private void setupVideoSDKEngine() {
        try {
            RtcEngineConfig config = new RtcEngineConfig();
            config.mContext = getBaseContext();
            config.mAppId = appId;
            config.mEventHandler = mRtcEventHandler;
            agoraEngine = RtcEngine.create(config);
            // By default, the video module is disabled, call enableVideo to enable it.
            agoraEngine.enableVideo();
        } catch (Exception e) {
            showMessage(e.toString());
        }
    }
}