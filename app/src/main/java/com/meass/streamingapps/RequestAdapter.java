package com.meass.streamingapps;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.myView> {
    private List<UserRequestModel> data;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    private Animation topAnimation, bottomAnimation, startAnimation, endAnimation;
    private SharedPreferences onBoardingPreference;
    public RequestAdapter(List<UserRequestModel> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public RequestAdapter.myView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subbb, parent, false);
        return new RequestAdapter.myView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestAdapter.myView holder, final int position) {
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=firebaseFirestore=FirebaseFirestore.getInstance();
       try {
           holder.logout.setText(data.get(position).getStatus());
           holder.customer_name.setText("Channel Name : "+data.get(position).getName());
           holder.customer_number.setText("Request Time : "+data.get(position).getTime());
           String image22="https://firebasestorage.googleapis.com/v0/b/cash-money-express-ltd.appspot.com/o/profile_images%2Fo8Dnqf5LFodKSwocGQ4nKB7ZEkW2.jpg?alt=media&token=c22700e2-67ca-4497-8bf1-204ac83b6749";
           String  picture = data.get(position).getImage();
           if (picture.equals(image22)) {
               Picasso.get().load(R.drawable.app_logo).into(holder.main_image);
               //holder.coins.setText(data.get(position).getName());
           }
           else {
               Picasso.get().load(data.get(position).getImage()).into(holder.main_image);
               ///holder.coins.setText(data.get(position).getName());
           }

       }
       catch (Exception e) {
       }
       holder.card_view8.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (data.get(position).getStatus().toLowerCase().equals("pending")) {

                   Toast.makeText(v.getContext(), "Please wait host will accept your request", Toast.LENGTH_SHORT).show();
                   firebaseFirestore.collection("User2")
                           .document(firebaseAuth.getCurrentUser().getEmail())
                           .get()
                           .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                               @Override
                               public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                   if (task.isSuccessful()) {
                                       if (task.getResult().exists()) {
                                           String username=task.getResult().getString("username");
                                           String photo=task.getResult().getString("image");
                                           Intent intent=new Intent(v.getContext(),JoinAsUserActivity.class);
                                           intent.putExtra("host",data.get(position).getUid());
                                           intent.putExtra("userName",username);
                                           intent.putExtra("roomname",username);
                                           intent.putExtra("image",photo);
                                           intent.putExtra("host",data.get(position).getUid());
                                           v.getContext().startActivity(intent);
                                       }
                                   }
                               }
                           });


               }else
               {
                   AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
                   builder.setTitle("Join Meeeting")
                           .setMessage("Do you want to join this call?")
                           .setPositiveButton("Not Now", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   dialog.dismiss();

                               }
                           }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           dialog.dismiss();

                       }
                   }).create().show();
               }

           }
       });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class myView extends RecyclerView.ViewHolder {
        ImageView main_image;
        CircleImageView photo_round1;
        TextView coins,id_number,email,phone;
        View v;
        FrameLayout framelayout;
        CardView dailyCheckCard;
        TextView customer_name,customer_number,customer_area,logout,customer_area3,customer_area8;
        CardView card_view8;
        public myView(@NonNull View itemView) {

            super(itemView);

            customer_name=itemView.findViewById(R.id.customer_name);
            customer_number=itemView.findViewById(R.id.customer_number);
            logout=itemView.findViewById(R.id.logout);
            card_view8=itemView.findViewById(R.id.card_view8);
            main_image=itemView.findViewById(R.id.image);

        }
    }
}
