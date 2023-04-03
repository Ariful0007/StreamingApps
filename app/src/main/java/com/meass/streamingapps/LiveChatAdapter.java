package com.meass.streamingapps;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class LiveChatAdapter extends RecyclerView.Adapter<LiveChatAdapter.myView> {
    private List<LiveChatModel> data;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    private Animation topAnimation, bottomAnimation, startAnimation, endAnimation;
    private SharedPreferences onBoardingPreference;
    public LiveChatAdapter(List<LiveChatModel> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public LiveChatAdapter.myView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.textsign, parent, false);
        return new LiveChatAdapter.myView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LiveChatAdapter.myView holder, final int position) {
        holder.livechat.setText(data.get(position).getMessage());
        holder.fromwhom.setText(data.get(position).getFromwhom());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class myView extends RecyclerView.ViewHolder {
        CircleImageView img;
        TextView livechat,fromwhom,email,phone,officealsback;
        View v;
        RelativeLayout rellla;
        public myView(@NonNull View itemView) {

            super(itemView);
            livechat=itemView.findViewById(R.id.livechat);
            fromwhom=itemView.findViewById(R.id.fromwhom);
            officealsback=itemView.findViewById(R.id.officealsback);
            rellla=itemView.findViewById(R.id.rellla);


        }
    }
}

/*
extends FirebaseRecyclerAdapter<LiveChatModel, LiveChatAdapter.myviewholder>
{
String channelName,mytokn,new_1,uuid,host,roomID,appID,appSign,userID,userName,roomname,image,audience;
    public LiveChatAdapter(@NonNull FirebaseRecyclerOptions<LiveChatModel> options,String channelName,String mytokn,String new_1,String uuid,String host,String roomID,String appID,
                           String appSign,String userID,String userName,String roomname,String image,String audience)
    {
        super(options);
        this.channelName=channelName;
        this.mytokn=mytokn;
        this.new_1=new_1;
        this.uuid=uuid;
        this.host=host;
        this.roomID=roomID;
        this.appID=appID;
        this.appSign=appSign;
        this.userID=userID;
        this.userName=userName;
        this.roomname=roomname;
        this.image=image;
        this.audience=audience;

    }


    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull LiveChatModel model)
    {
        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("user");
        firebaseAuth= FirebaseAuth.getInstance();
        if (TextUtils.isEmpty(model.getUserid())) {
            holder.livechat.setText(model.getMessage());
            holder.fromwhom.setText(model.getFromwhom());
        }
        else {

            databaseReference.child(model.getUserid())
                            .get()
                                    .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DataSnapshot snapshot = task.getResult();
                                                if(snapshot.exists()){
                                                    String  offer_class1 = String.valueOf(snapshot.child("offer_class1").getValue());
                                                    String  official = String.valueOf(snapshot.child("official").getValue());

                                                    String  agency = String.valueOf(snapshot.child("agency").getValue());
                                                    String  diamond_seller = String.valueOf(snapshot.child("diamond_seller").getValue());
                                                    String  host = String.valueOf(snapshot.child("host").getValue());

                                                    if (offer_class1.equals("true")) {
                                                        holder.livechat.setText(model.getMessage());
                                                        holder.fromwhom.setText(model.getFromwhom());
                                                        holder.officealsback.setText("Offer class");
                                                    }
                                                    if (official.equals("true")) {
                                                        holder.livechat.setText(model.getMessage());
                                                        holder.fromwhom.setText(model.getFromwhom());
                                                        holder.officealsback.setText("Official");
                                                    }
                                                    if (agency.equals("true")) {
                                                        holder.livechat.setText(model.getMessage());
                                                        holder.fromwhom.setText(model.getFromwhom());
                                                        holder.officealsback.setText("Agency");
                                                    }
                                                    if (diamond_seller.equals("true")) {
                                                        holder.livechat.setText(model.getMessage());
                                                        holder.fromwhom.setText(model.getFromwhom());
                                                        holder.officealsback.setText("Diamond seller");
                                                    }
                                                    if (host.equals("true")) {
                                                        holder.livechat.setText(model.getMessage());
                                                        holder.fromwhom.setText(model.getFromwhom());
                                                        holder.officealsback.setText("Host");
                                                    }
                                                }
                                            }
                                        }
                                    });

        }

holder.rellla.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

Intent intent=new Intent(view.getContext(),ProfileDekhteHobe.class);
intent.putExtra("uuuid",""+model.getUid());
intent.putExtra("new_1",new_1);
intent.putExtra("uuid",uuid);
        intent.putExtra("host",host);
        intent.putExtra("roomID",roomID);
        intent.putExtra("appID",appID);
        intent.putExtra("appSign",appSign);
        intent.putExtra("userID",userID);
        intent.putExtra("userName",userName);
        intent.putExtra("roomname",roomname);
        intent.putExtra("image",image);
        intent.putExtra("audience",audience);
        intent.putExtra("token",mytokn);
        intent.putExtra("channelName",channelName);



        view.getContext().startActivity(intent);
        Toast.makeText(view.getContext(), ""+model.getUid(), Toast.LENGTH_SHORT).show();
    }
});



    }
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.textsign,parent,false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        CircleImageView img;
        TextView livechat,fromwhom,email,phone,officealsback;
        View v;
        RelativeLayout rellla;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            livechat=itemView.findViewById(R.id.livechat);
            fromwhom=itemView.findViewById(R.id.fromwhom);
            officealsback=itemView.findViewById(R.id.officealsback);
            rellla=itemView.findViewById(R.id.rellla);

        }
    }


}
 */
