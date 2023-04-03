package com.meass.streamingapps;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.myView> {
    private List<LiveModel> data;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    private Animation topAnimation, bottomAnimation, startAnimation, endAnimation;
    private SharedPreferences onBoardingPreference;
    public NotificationAdapter(List<LiveModel> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public NotificationAdapter.myView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.live_itemm, parent, false);
        return new NotificationAdapter.myView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.myView holder, final int position) {
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=firebaseFirestore=FirebaseFirestore.getInstance();
        String image22="https://firebasestorage.googleapis.com/v0/b/cash-money-express-ltd.appspot.com/o/profile_images%2Fo8Dnqf5LFodKSwocGQ4nKB7ZEkW2.jpg?alt=media&token=c22700e2-67ca-4497-8bf1-204ac83b6749";
        try {


            String  picture = data.get(position).getImage();
            if (picture.equals(image22)) {
                Glide.with(holder.photo_round1.getContext())
                        .load(R.drawable.app_logo)
                        .placeholder(R.drawable.app_logo)
                        .circleCrop()
                        .error(R.drawable.error)
                        .into(holder.photo_round1);
            }
            else {
                Glide.with(holder.photo_round1.getContext())
                        .load(picture)
                        .placeholder(R.drawable.app_logo)
                        .circleCrop()
                        .error(R.drawable.error)
                        .into(holder.photo_round1);
            }

        }catch (Exception e) {
            String  picture = data.get(position).getImage();
            if (picture.equals(image22)) {
                Glide.with(holder.photo_round1.getContext())
                        .load(R.drawable.app_logo)
                        .placeholder(R.drawable.app_logo)
                        .circleCrop()
                        .error(R.drawable.error)
                        .into(holder.photo_round1);
            }
            else {
                Glide.with(holder.photo_round1.getContext())
                        .load(picture)
                        .placeholder(R.drawable.app_logo)
                        .circleCrop()
                        .error(R.drawable.error)
                        .into(holder.photo_round1);
            }
        }

        try {
            String  picture = data.get(position).getImage();
            if (picture.equals(image22)) {
                Picasso.get().load(R.drawable.app_logo).into(holder.main_image);
                holder.coins.setText(data.get(position).getName());
            }
            else {
                Picasso.get().load(data.get(position).getImage()).into(holder.main_image);
                holder.coins.setText(data.get(position).getName());
            }

        }catch (Exception e) {
            String  picture = data.get(position).getImage();
            if (picture.equals(image22)) {
                Picasso.get().load(R.drawable.app_logo).into(holder.main_image);
                holder.coins.setText(data.get(position).getName());
            }
            else {
                Picasso.get().load(data.get(position).getImage()).into(holder.main_image);
                holder.coins.setText(data.get(position).getName());
            }
        }
        holder.framelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
                builder.setTitle("Confirmation")
                        .setMessage("Do you want to join this call?")
                        .setPositiveButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        firebaseFirestore.collection("UserJoinRequest_user")
                                .document(firebaseAuth.getCurrentUser().getUid())
                                .collection("List")
                                .document(data.get(position).getUid())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            if (task.getResult().exists()) {
                                                v.getContext().startActivity(new Intent(v.getContext(),UserJoinRequestList.class));
                                            }
                                            else {
                                                final KProgressHUD progressDialog=  KProgressHUD.create(v.getContext())
                                                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                                        .setLabel("Uploading  Data.....")
                                                        .setCancellable(false)
                                                        .setAnimationSpeed(2)
                                                        .setDimAmount(0.5f)
                                                        .show();
                                                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                                Calendar cal = Calendar.getInstance();
                                                UserRequestModel userRequestModel=new UserRequestModel(data.get(position).getName(),
                                                        data.get(position).getImage(),data.get(position).getChancelname(),
                                                        data.get(position).getUid(),data.get(position).getRoomID(),""+dateFormat.format(cal.getTime()),
                                                        data.get(position).getAudience(),firebaseAuth.getCurrentUser().getUid(),"Pending");
                                                firebaseFirestore.collection("UserJoinRequest_user")
                                                        .document(firebaseAuth.getCurrentUser().getUid())
                                                        .collection("List")
                                                        .document(data.get(position).getUid())
                                                        .set(userRequestModel)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    firebaseFirestore.collection("UserJoinRequest_user")
                                                                            .document(data.get(position).getUid())
                                                                            .collection("List")
                                                                            .document(firebaseAuth.getCurrentUser().getUid())
                                                                            .set(userRequestModel)
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if (task.isSuccessful()) {
                                                                                        progressDialog.dismiss();
                                                                                        Toasty.success(v.getContext(),"Request gone to host",Toasty.LENGTH_SHORT,true).show();
                                                                                        v.getContext().startActivity(new Intent(v.getContext(),UserJoinRequestList.class));
                                                                                        return;

                                                                                    }
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    }
                                });


                    }
                }).create().show();
            }
        });
        holder.dailyCheckCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
                builder.setTitle("Confirmation")
                        .setMessage("Do you want to join this call?")
                        .setPositiveButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        final KProgressHUD progressDialog=  KProgressHUD.create(v.getContext())
                                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                .setLabel("Uploading  Data.....")
                                .setCancellable(false)
                                .setAnimationSpeed(2)
                                .setDimAmount(0.5f)
                                .show();
                        UserRequestModel userRequestModel=new UserRequestModel(data.get(position).getName(),
                                data.get(position).getImage(),data.get(position).getChancelname(),
                                data.get(position).getUid(),data.get(position).getRoomID(),data.get(position).getTime(),
                                data.get(position).getAudience(),firebaseAuth.getCurrentUser().getUid(),"Pending");
                        firebaseFirestore.collection("UserJoinRequest_user")
                                .document(firebaseAuth.getCurrentUser().getUid())
                                .collection("List")
                                .document(data.get(position).getUid())
                                .set(userRequestModel)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            firebaseFirestore.collection("UserJoinRequest_user")
                                                    .document(data.get(position).getUid())
                                                    .collection("List")
                                                    .document(firebaseAuth.getCurrentUser().getUid())
                                                    .set(userRequestModel)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                progressDialog.dismiss();
                                                                Toasty.success(v.getContext(),"Request gone to host",Toasty.LENGTH_SHORT,true).show();
                                                                v.getContext().startActivity(new Intent(v.getContext(),UserJoinRequestList.class));
                                                                return;

                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });

                    }
                }).create().show();

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
        public myView(@NonNull View itemView) {

            super(itemView);
            main_image=itemView.findViewById(R.id.main_image);
            photo_round1=itemView.findViewById(R.id.photo_round1);
            coins=itemView.findViewById(R.id.coins);
            framelayout=itemView.findViewById(R.id.framelayout);
            dailyCheckCard=itemView.findViewById(R.id.dailyCheckCard);


        }
    }
}
