package com.meass.streamingapps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.agora.rtc2.IRtcEngineEventHandler;
import io.agora.rtc2.RtcEngine;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.animation.Animator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;
import io.agora.rtc2.ChannelMediaOptions;
import io.agora.rtc2.Constants;
import io.agora.rtc2.IRtcEngineEventHandler;
import io.agora.rtc2.RtcEngine;
import io.agora.rtc2.RtcEngineConfig;
import io.agora.rtc2.video.VideoCanvas;
import io.agora.rtc2.video.VideoEncoderConfiguration;

public class StreamingDemo extends AppCompatActivity {
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



    //////calling
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



    private final String appId = "f50ae2f9b6f3433d9cac72a4e268e012";
    // Fill the channel name.
    private String channelName ;
    // Fill the temp token generated on Agora Console.
    private String token;
    // An integer that identifies the local user.
    private int uid = 0;
    private boolean isJoined = false;

    private RtcEngine agoraEngine;
    //SurfaceView to render local video in a Container.
    private SurfaceView localSurfaceView;
    //SurfaceView to render Remote video in a Container.
    private SurfaceView remoteSurfaceView;
    int cohostflag=0;
    LottieAnimationView animationView1;




    ////
    private RtcEngine mRtcEngine; // Tutorial Step 1
    private final IRtcEngineEventHandler mRtcEventHandler1 = new IRtcEngineEventHandler() { // Tutorial Step 1

        /**
         * Occurs when a remote user (Communication)/host (Live Broadcast) leaves the channel.
         *
         * There are two reasons for users to become offline:
         *
         *     Leave the channel: When the user/host leaves the channel, the user/host sends a goodbye message. When this message is received, the SDK determines that the user/host leaves the channel.
         *     Drop offline: When no data packet of the user or host is received for a certain period of time (20 seconds for the communication profile, and more for the live broadcast profile), the SDK assumes that the user/host drops offline. A poor network connection may lead to false detections, so we recommend using the Agora RTM SDK for reliable offline detection.
         *
         * @param uid ID of the user or host who
         * leaves
         * the channel or goes offline.
         * @param reason Reason why the user goes offline:
         *
         *     USER_OFFLINE_QUIT(0): The user left the current channel.
         *     USER_OFFLINE_DROPPED(1): The SDK timed out and the user dropped offline because no data packet was received within a certain period of time. If a user quits the call and the message is not passed to the SDK (due to an unreliable channel), the SDK assumes the user dropped offline.
         *     USER_OFFLINE_BECOME_AUDIENCE(2): (Live broadcast only.) The client role switched from the host to the audience.
         */
        @Override
        public void onUserOffline(final int uid, final int reason) { // Tutorial Step 4
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onRemoteUserLeft(uid, reason);
                }
            });
        }

        /**
         * Occurs when a remote user stops/resumes sending the audio stream.
         * The SDK triggers this callback when the remote user stops or resumes sending the audio stream by calling the muteLocalAudioStream method.
         *
         * @param uid ID of the remote user.
         * @param muted Whether the remote user's audio stream is muted/unmuted:
         *
         *     true: Muted.
         *     false: Unmuted.
         */
        @Override
        public void onUserMuteAudio(final int uid, final boolean muted) { // Tutorial Step 6
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (muted) {
                    }
                    else {
                    }

                    onRemoteUserVoiceMuted(uid, muted);
                }
            });
        }
    };
    // Tutorial Step 4
    private void onRemoteUserLeft(int uid, int reason) {
        mRtcEngine.adjustAudioMixingVolume(0);
        mRtcEngine.adjustPlaybackSignalVolume(0);
        showLongToast(String.format(Locale.US, "user %d left %d", (uid & 0xFFFFFFFFL), reason));
        //View tipMsg = findViewById(R.id.quick_tips_when_use_agora_sdk); // optional UI
        //tipMsg.setVisibility(View.VISIBLE);
    }

    // Tutorial Step 6
    private void onRemoteUserVoiceMuted(int uid, boolean muted) {
        showLongToast(String.format(Locale.US, "user %d muted or unmuted %b", (uid & 0xFFFFFFFFL), muted));
    }
    public final void showLongToast(final String msg) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }
    String mychanel,mytokn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streaming_demo);
        ///
        try {
            mytokn="007eJxTYOCfa+16ln1D01Ev97ryPTOWqTikrEu6sOV0zLM9gVMXredWYEgzNUhMNUqzTDJLMzYxNk6xTE5MNjdKNEk1MrNINTA0+ndMMaUhkJHhyL1kBkYoBPGZGSorKxkYAFSaIG8=";
            token=mytokn;
            mychanel="yyy";
            channelName=mychanel;


        }catch (Exception e) {
            mytokn="007eJxTYOCfa+16ln1D01Ev97ryPTOWqTikrEu6sOV0zLM9gVMXredWYEgzNUhMNUqzTDJLMzYxNk6xTE5MNjdKNEk1MrNINTA0+ndMMaUhkJHhyL1kBkYoBPGZGSorKxkYAFSaIG8=";
            token=mytokn;
            mychanel="yyy";
            channelName=mychanel;
        }
        animationView1=findViewById(R.id.animationView1);

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
        Toast.makeText(this, ""+host, Toast.LENGTH_SHORT).show();
        ImageView hostimage=findViewById(R.id.hostimage);
        TextView histname=findViewById(R.id.histname);
        try {
            String image22="https://firebasestorage.googleapis.com/v0/b/cash-money-express-ltd.appspot.com/o/profile_images%2Fo8Dnqf5LFodKSwocGQ4nKB7ZEkW2.jpg?alt=media&token=c22700e2-67ca-4497-8bf1-204ac83b6749";
            String  picture = image;
            if (picture.equals(image22)) {
                Picasso.get().load(R.drawable.app_logo).into(hostimage);
                //holder.coins.setText(data.get(position).getName());
            }
            else {
                Picasso.get().load(image).into(hostimage);
                ///holder.coins.setText(data.get(position).getName());
            }
            //Picasso.get().load(image).into(hostimage);
            histname.setText(roomname);
        }catch (Exception e) {
            String image22="https://firebasestorage.googleapis.com/v0/b/cash-money-express-ltd.appspot.com/o/profile_images%2Fo8Dnqf5LFodKSwocGQ4nKB7ZEkW2.jpg?alt=media&token=c22700e2-67ca-4497-8bf1-204ac83b6749";
            String  picture = image;
            if (picture.equals(image22)) {
                Picasso.get().load(R.drawable.app_logo).into(hostimage);
                //holder.coins.setText(data.get(position).getName());
            }
            else {
                Picasso.get().load(image).into(hostimage);
                ///holder.coins.setText(data.get(position).getName());
            }
            //Picasso.get().load(image).into(hostimage);
            histname.setText(roomname);
        }
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();


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
                    Toast.makeText(StreamingDemo.this, "Enter Message", Toast.LENGTH_SHORT).show();


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
                                        Toast.makeText(StreamingDemo.this, "Sent", Toast.LENGTH_SHORT).show();
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

    void showMessage(String message) {
        runOnUiThread(() ->
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show());
    }
    private void setupVideoSDKEngine() {
        try {
            RtcEngineConfig config = new RtcEngineConfig();
            config.mContext = getBaseContext();
            config.mAppId = appId;
            config.mEventHandler = mRtcEventHandler;
            agoraEngine = RtcEngine.create(config);
            // By default, the video module is disabled, call enableVideo to enable it.
            agoraEngine.enableVideo();
            initAgoraEngine();
            mystsrta();
        } catch (Exception e) {
            showMessage(e.toString());
        }
    }
    private void initAgoraEngine() {
        try {
            mRtcEngine = RtcEngine.create(getBaseContext(),appID, mRtcEventHandler);
        } catch (Exception e) {
            // Log.e(LOG_TAG, Log.getStackTraceString(e));

            /// throw new RuntimeException("fatal error\n" + Log.getStackTraceString(e));
        }
        setupSession();
    }
    private void setupSession() {
        mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION);

        mRtcEngine.enableVideo();

        mRtcEngine.setVideoEncoderConfiguration(new VideoEncoderConfiguration(VideoEncoderConfiguration.VD_640x480, VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_30,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT));
    }
    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        // Listen for the remote host joining the channel to get the uid of the host.
        public void onUserJoined(int uid, int elapsed) {
            showMessage("Remote user joined " + uid);

            // Set the remote video view
            runOnUiThread(() -> setupRemoteVideo(uid));
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
    private void setupRemoteVideo(int uid) {
        FrameLayout container = findViewById(R.id.remote_video_view_container);
        remoteSurfaceView = new SurfaceView(getBaseContext());
        remoteSurfaceView.setZOrderMediaOverlay(true);
        container.addView(remoteSurfaceView);
        agoraEngine.setupRemoteVideo(new VideoCanvas(remoteSurfaceView, VideoCanvas.RENDER_MODE_FIT, uid));
        // Display RemoteSurfaceView.
        remoteSurfaceView.setVisibility(View.VISIBLE);
    }
    private void setupLocalVideo() {
        FrameLayout container = findViewById(R.id.local_video_view_container);
        // Create a SurfaceView object and add it as a child to the FrameLayout.
        localSurfaceView = new SurfaceView(getBaseContext());
        container.addView(localSurfaceView);
        // Pass the SurfaceView object to Agora so that it renders the local video.
        agoraEngine.setupLocalVideo(new VideoCanvas(localSurfaceView, VideoCanvas.RENDER_MODE_HIDDEN, 0));
    }
    public void joinChannel(View view) {
        if (checkSelfPermission()) {
            ChannelMediaOptions options = new ChannelMediaOptions();

            // For a Video call, set the channel profile as COMMUNICATION.
            options.channelProfile = Constants.CHANNEL_PROFILE_COMMUNICATION;
            // Set the client role as BROADCASTER or AUDIENCE according to the scenario.
            options.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER;
            // Display LocalSurfaceView.
            setupLocalVideo();
            localSurfaceView.setVisibility(View.VISIBLE);
            // Start local preview.
            agoraEngine.startPreview();
            // Join the channel with a temp token.
            // You need to specify the user ID yourself, and ensure that it is unique in the channel.
            agoraEngine.joinChannel(token, channelName, uid, options);
        } else {
            Toast.makeText(getApplicationContext(), "Permissions was not granted", Toast.LENGTH_SHORT).show();
        }
    }
    public void mystsrta() {
        if (checkSelfPermission()) {
            ChannelMediaOptions options = new ChannelMediaOptions();

            // For a Video call, set the channel profile as COMMUNICATION.
            options.channelProfile = Constants.CHANNEL_PROFILE_COMMUNICATION;
            // Set the client role as BROADCASTER or AUDIENCE according to the scenario.
            options.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER;
            // Display LocalSurfaceView.
            setupLocalVideo();
            localSurfaceView.setVisibility(View.VISIBLE);
            // Start local preview.
            agoraEngine.startPreview();
            mRtcEngine.adjustAudioMixingVolume(10);
            mRtcEngine.adjustPlaybackSignalVolume(10);
            mRtcEngine.muteLocalAudioStream(true);





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
    }
    private void showBottomSheetDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(StreamingDemo.this);






        bottomSheetDialog.show();
    }
    private void cohostshow() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(StreamingDemo.this);

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
            AlertDialog.Builder builder=new AlertDialog.Builder(StreamingDemo.this);
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
        AlertDialog.Builder builder=new AlertDialog.Builder(StreamingDemo.this);
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
}