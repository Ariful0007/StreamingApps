package com.meass.streamingapps;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.geniusforapp.fancydialog.FancyAlertDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.mikepenz.crossfadedrawerlayout.view.CrossfadeDrawerLayout;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private Toolbar mainToolbar;
    private String current_user_id;
    private BottomNavigationView mainBottomNav;
    private DrawerLayout mainDrawer;
    private ActionBarDrawerToggle mainToggle;
    private NavigationView mainNav;

    FrameLayout frameLayout;
    private TextView drawerName,appname;
    private CircleImageView drawerImage;
    FirebaseAuth firebaseAuth;
    //firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseFirestoreSettings settings;
    private DatabaseReference mUserRef;




    KProgressHUD kProgressHUD;
    Long tsLong = System.currentTimeMillis()/1000;
    String ts = tsLong.toString();

    private UserSession session;
    private HashMap<String, String> user;
    private String name, email, photo, mobile,username;
    private Drawer result;
    private CrossfadeDrawerLayout crossfadeDrawerLayout = null;
    FirebaseFirestore db;

    FirebaseUser firebaseUser;
    String user1;

    IProfile profile;
    TextView nameTv,emailTv;
    CircleImageView profileImage;
    TextView coinsT1v;
    CardView dailyCheckCard,luckySpinCard,aboutCard1,aboutCard,redeemCard,referCard,taskCard;

    String sessionname,sessionmobile,sessionphoto,sessionemail,sessionusername;
    int count,count1,count2,count3;
    String package_actove;
    String daily_bonus;
    String incomeType="Daily Task";
    int main_account;
    int count12,count123;
    int main_refer;
    String main_task ;



    private TextView tvemail,tvphone;
    private HashMap<String, String> uaser;
    FloatingActionButton dialogClose;

    ; Dialog mDialog;
    @RequiresApi(api = Build.VERSION_CODES.N)
    TextView myamount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("dfdfdf");
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(10.0f);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(10.0f);
        getSupportActionBar().setElevation(10.0f);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        session = new UserSession(getApplicationContext());
        myamount=findViewById(R.id.myamount);
        getValues();
        mainDrawer=findViewById(R.id.main_activity);
        mainNav = findViewById(R.id.main_nav);
        mainNav.setNavigationItemSelectedListener(this);

        mainToggle = new ActionBarDrawerToggle(this,mainDrawer,toolbar,R.string.open,R.string.close);
        mainDrawer.addDrawerListener(mainToggle);
        mainDrawer.addDrawerListener(mainToggle);
        mainToggle.setDrawerIndicatorEnabled(true);
        mainToggle.syncState();
        //////textview count
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        ////fragment show
        RequestManager glide = Glide.with(HomeActivity.this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        getList = new ArrayList<>();
        getDataAdapter1 = new NotificationAdapter(getList);
        firebaseFirestore = FirebaseFirestore.getInstance();
        documentReference =   firebaseFirestore.collection("Livestreams")
                .document();
        recyclerView = findViewById(R.id.blog_list_view);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);

        //recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
        recyclerView.setAdapter(getDataAdapter1);
        reciveData();
    }
    private StaggeredGridLayoutManager mLayoutManager;
    private void reciveData() {

        firebaseFirestore.collection("Livestreams").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange ds : queryDocumentSnapshots.getDocumentChanges()) {
                    if (ds.getType() == DocumentChange.Type.ADDED) {

                 /*String first;
                 first = ds.getDocument().getString("name");
                 Toast.makeText(MainActivity2.this, "" + first, Toast.LENGTH_SHORT).show();*/
                        LiveModel get = ds.getDocument().toObject(LiveModel.class);
                        getList.add(get);
                        getDataAdapter1.notifyDataSetChanged();
                    }

                }
            }
        });

    }
    DocumentReference documentReference;
    RecyclerView recyclerView;
    NotificationAdapter getDataAdapter1;
    List<LiveModel> getList;
    String url;


    KProgressHUD progressHUD;
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getValues() {
        //validating session


        try {
            //get User details if logged in
            session.isLoggedIn();
            user = session.getUserDetails();

            name = user.get(UserSession.KEY_NAME);
            email = user.get(UserSession.KEY_EMAIL);
            mobile = user.get(UserSession.KEY_MOBiLE);
            photo = user.get(UserSession.KEY_PHOTO);
            username=user.get(UserSession.Username);
            // Toast.makeText(this, ""+name, Toast.LENGTH_SHORT).show();
            DateFormat df = new SimpleDateFormat("h:mm:ss a");
            DateFormat df1 = new SimpleDateFormat("EEE, MMM d");
            String date = df.format(Calendar.getInstance().getTime());
            String date2 = df1.format(Calendar.getInstance().getTime());
            myamount.setText(name+"\n" +
                    ""+date2+" | "+date);

        }catch (Exception e) {
            //get User details if logged in
            session.isLoggedIn();
            user = session.getUserDetails();

            name = user.get(UserSession.KEY_NAME);
            email = user.get(UserSession.KEY_EMAIL);
            mobile = user.get(UserSession.KEY_MOBiLE);
            photo = user.get(UserSession.KEY_PHOTO);
            username=user.get(UserSession.Username);
            DateFormat df = new SimpleDateFormat("h:mm a");
            DateFormat df1 = new SimpleDateFormat("EEE, MMM d");
            String date = df.format(Calendar.getInstance().getTime());
            String date2 = df1.format(Calendar.getInstance().getTime());
            myamount.setText(name+"\n" +
                    ""+date2+" | "+date);
        }
        //Toast.makeText(this, ""+username, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onBackPressed() {
        final FancyAlertDialog.Builder alert = new FancyAlertDialog.Builder(HomeActivity.this)
                .setBackgroundColor(R.color.white)
                .setTextTitle("Exit")
                .setCancelable(false)
                .setTextSubTitle("Are you want to exit")
                .setBody("User is not stay at app when user click exit button.")
                .setPositiveButtonText("No")
                .setPositiveColor(R.color.toolbar)
                .setOnPositiveClicked(new FancyAlertDialog.OnPositiveClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog) {
                        dialog.dismiss();

                    }
                })
                .setNegativeButtonText("Exit")
                .setNegativeColor(R.color.colorPrimaryDark)
                .setOnNegativeClicked(new FancyAlertDialog.OnNegativeClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog) {
                        dialog.dismiss();
                        finishAffinity();

                    }
                })
                .setBodyGravity(FancyAlertDialog.TextGravity.CENTER)
                .setTitleGravity(FancyAlertDialog.TextGravity.CENTER)
                .setSubtitleGravity(FancyAlertDialog.TextGravity.CENTER)
                .setCancelable(false)
                .build();
        alert.show();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.startlive) {
            Intent intent=new Intent(getApplicationContext(),JoinAsHost.class);
            intent.putExtra("host",username);
            intent.putExtra("userName",username);
            intent.putExtra("roomname",username);
            intent.putExtra("image",photo);
            intent.putExtra("host",username);
            intent.putExtra("host",username);
            intent.putExtra("host",username);
            startActivity(intent);
            firebaseFirestore.collection("User_orHoist")
                    .document(firebaseAuth.getCurrentUser().getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().exists()) {
                                    String hostt=task.getResult().getString("hostt");
                                    if (hostt.equals("false")) {
                                        Toast.makeText(HomeActivity.this, "You are a simple user.", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        String op[]={"Start Live","End Live"};
                                        AlertDialog.Builder stsrt=new AlertDialog.Builder(HomeActivity.this);
                                        stsrt.setTitle("Options")
                                                .setItems(op, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        if (which==0) {
                                                            AlertDialog.Builder warning = new AlertDialog.Builder(HomeActivity.this)
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
                                                                            final KProgressHUD progressDialog=  KProgressHUD.create(HomeActivity.this)
                                                                                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                                                                    .setLabel("Uploading Data.....")
                                                                                    .setCancellable(false)
                                                                                    .setAnimationSpeed(2)
                                                                                    .setDimAmount(0.5f)
                                                                                    .show();
                                                                            // ToDO: delete all the notes created by the Anon user
                                                                            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                                                            Calendar cal = Calendar.getInstance();

                                                                            LiveModel liveModel =new LiveModel(username,photo,"yyy",firebaseAuth.getCurrentUser().getUid(),
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
                                                                                                                    Intent intent=new Intent(getApplicationContext(),StartLivevvve.class);
                                                                                                                    intent.putExtra("host",username);
                                                                                                                    intent.putExtra("userName",username);
                                                                                                                    intent.putExtra("roomname",username);
                                                                                                                    intent.putExtra("image",photo);
                                                                                                                    intent.putExtra("host",username);
                                                                                                                    intent.putExtra("host",username);
                                                                                                                    intent.putExtra("host",username);
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
                                                        else {
                                                            firebaseFirestore.collection("Livestreams")
                                                                    .document(firebaseAuth.getCurrentUser().getUid())
                                                                    .get()
                                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                            if (task.isSuccessful()) {
                                                                                if (task.getResult().exists()) {
                                                                                    firebaseFirestore.collection("Livestreams")
                                                                                            .document(firebaseAuth.getCurrentUser().getUid())
                                                                                            .delete()
                                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                    if (task.isSuccessful()) {
                                                                                                        Toast.makeText(HomeActivity.this, "Done", Toast.LENGTH_SHORT).show();
                                                                                                    }
                                                                                                }
                                                                                            });
                                                                                }
                                                                                else {
                                                                                    Toast.makeText(HomeActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                                                                                }

                                                                            }
                                                                            else {
                                                                                Toast.makeText(HomeActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                    });

                                                        }
                                                    }
                                                });
                                        stsrt.show();
                                    }
                                }
                                else {
                                    Toast.makeText(HomeActivity.this, "You are a simple user only.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(HomeActivity.this, "You are a simple user only.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


        }


        if (id==R.id.mypost) {
            AlertDialog.Builder warning = new AlertDialog.Builder(HomeActivity.this)
                    .setTitle("Logout")
                    .setMessage("Are you want to logout?")
                    .setPositiveButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();



                        }
                    }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // ToDO: delete all the notes created by the Anon user


                            firebaseAuth.signOut();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();


                        }
                    });

            warning.show();
        }
        if (id==R.id.bottom_home) {
            Toasty.success(getApplicationContext(),"You are home now",Toasty.LENGTH_SHORT,true).show();

        }
        if (id==R.id.ctreate) {
            startActivity(new Intent(getApplicationContext(),CreatChanelActivity.class));
            firebaseFirestore.collection("ApproveHost")
                    .document(firebaseAuth.getCurrentUser().getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().exists()) {
                                    String hostt=task.getResult().getString("hostt");
                                    if (hostt.toLowerCase().equals("false")) {
                                        Toast.makeText(HomeActivity.this, "Please wait for your host approval", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        startActivity(new Intent(getApplicationContext(),CreatChanelActivity.class));
                                    }
                                }
                                else {
                                    Toast.makeText(HomeActivity.this, "You are a simple user", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(HomeActivity.this, "You are a simple user", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        if (id==R.id.uiserr) {
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
        }


        return false;
    }
}