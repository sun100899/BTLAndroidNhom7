package com.example.btlandroidnhom7.fragment;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.example.btlandroidnhom7.View.StartActivity;
import com.example.btlandroidnhom7.dialog.ProgressLoading;
import com.example.btlandroidnhom7.service.PicassoLoadingService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import androidx.annotation.NonNull;
import com.example.btlandroidnhom7.R;
import androidx.fragment.app.Fragment;

import java.io.FileNotFoundException;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class SettingsFragment extends Fragment implements View.OnClickListener {
    public final String STORAGE_PATH = "image/";
    public final int REQUEST_CODE_PICK_IMAGE = 1412;

    private DatabaseReference mUser;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;

    private RelativeLayout tvChangeUserName, tvChangePass, tvLogout;
    private RelativeLayout tvAboutUs, tvFeedback, tvRateApp, tvShare;
    private ImageView imgAvatar;
    private TextView tvUserName, tvUserEmail;
    private Toolbar toolbar;
    private Uri imgUri;
    public static Fragment newInstance() {
        SettingsFragment settingsFragment = new SettingsFragment();
        return settingsFragment;
    }

    @NonNull
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment, container, false);
        initView(view);
        ProgressLoading.show(getContext());
        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        uploadProfile();
        
        //Click Events
        imgAvatar.setOnClickListener(this);
        tvChangeUserName.setOnClickListener(this);
        tvChangePass.setOnClickListener(this);
        tvAboutUs.setOnClickListener(this);
        tvFeedback.setOnClickListener(this);
        tvRateApp.setOnClickListener(this);
        tvShare.setOnClickListener(this);
        tvLogout.setOnClickListener(this);

        return view;
    }

    private void uploadProfile() {
        String email = mAuth.getCurrentUser().getEmail();
        tvUserEmail.setText(email);
        displayUserName();
        displayAvatarImage();
        ProgressLoading.dismiss();
    }

    private void displayAvatarImage() {
        mUser.child("Users").child(mAuth.getCurrentUser().getUid()).child("AvatarUrl").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String avatarUrl = dataSnapshot.getValue(String.class);
                if(avatarUrl!=null) {
                    PicassoLoadingService picasso = new PicassoLoadingService();
                    picasso.loadImage(dataSnapshot.getValue(String.class), imgAvatar);
                } else {
                    imgAvatar.setImageResource(R.drawable.avatar);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initView(View view) {
        tvUserEmail = view.findViewById(R.id.tv_email_setting);
        tvUserName = view.findViewById(R.id.tv_username_setting);
        tvRateApp = view.findViewById(R.id.tv_rate_app_setting);
        tvChangeUserName = view.findViewById(R.id.tv_change_username);
        tvChangePass = view.findViewById(R.id.tv_change_passwd);
        tvAboutUs = view.findViewById(R.id.tv_about_us_setting);
        tvShare = view.findViewById(R.id.tv_share_setting);
        tvFeedback = view.findViewById(R.id.tv_feed_back_setting);
        tvLogout = view.findViewById(R.id.tv_logout_setting);
        imgAvatar = view.findViewById(R.id.img_avatar_setting);
        toolbar = view.findViewById(R.id.toolbar_settings);
        ((AppCompatActivity)getActivity()).getSupportActionBar();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_PICK_IMAGE) {
            if(resultCode == RESULT_OK && data != null && data.getData() != null) {
                imgUri = data.getData();
                new AlertDialog.Builder(getContext()).setMessage("Change your avatar?")
                        .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Bitmap bm = null;
                                try {
                                    bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imgUri);
                                } catch (FileNotFoundException e) {
                                    Log.i("PROFILE", e.getMessage());
                                } catch (IOException e) {
                                    Log.i("PROFILE", e.getMessage());
                                }
                                imgAvatar.setImageBitmap(bm);
                                uploadImageToServer();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        }
    }

    private void uploadImageToServer() {
        if(imgUri != null) {
            final ProgressDialog dialog = new ProgressDialog(getContext());
            dialog.setTitle("Uploading image");
            dialog.show();

            //Get the storage reference
            StorageReference ref = mStorageRef.child(STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(imgUri));

            //Add file to reference

            ref.putFile(imgUri).addOnSuccessListener(taskSnapshot -> {
                //Dimiss dialog when success
                dialog.dismiss();
                //Display success toast msg
                Toast.makeText(getContext(), "Image uploaded", Toast.LENGTH_SHORT).show();
                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!urlTask.isSuccessful());
                Uri downloadUrl = urlTask.getResult();
                String avatarUrl = downloadUrl.toString();

                //Save image avatar to database
                mUser.child("Users").child(mAuth.getCurrentUser().getUid()).child("AvatarUrl").setValue(avatarUrl);

            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            //Dimiss dialog when error
                            dialog.dismiss();
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //Show upload progress
                            double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            dialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        } else {
            Toast.makeText(getContext(), "Please select image", Toast.LENGTH_SHORT).show();
        }

    }

    public String getImageExt(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.img_avatar_setting:
                chooseImage();
                break;
            case R.id.tv_change_passwd:
                changePassword();
                break;
            case R.id.tv_change_username:
                changeUserName();
                break;
            case R.id.tv_about_us_setting:
                Toast.makeText(getContext(), "Nhom 07", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_share_setting:
                shareLink();
                break;
            case R.id.tv_rate_app_setting:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getActivity().getPackageName()));
                this.startActivity(intent);
                break;
            case R.id.tv_feed_back_setting:
                sendEmail();
                break;
            case R.id.tv_logout_setting:
                logOut();
                break;
        }

    }

    private void shareLink() {
        int applicationNameId = getContext().getApplicationInfo().labelRes;
        final String appPackageName = getContext().getPackageName();
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, getString(applicationNameId));
        String text = "Install this cool application: ";
        //String link = "https://play.google.com/store/apps/details?id=" + appPackageName;
        /*demo*/
        String link = "https://play.google.com/store/apps/details?id=" + appPackageName;
        i.putExtra(Intent.EXTRA_TEXT, text + " " + link);
        startActivity(Intent.createChooser(i, "Share link:"));
    }

    private void sendEmail() {
        Intent intentSendMail = new Intent(Intent.ACTION_SENDTO);
        intentSendMail.setType("text/plain");
        intentSendMail.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_title));
        intentSendMail.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_body));
        intentSendMail.setData(Uri.parse("mailto:vuthanhxuan99@gmail.com"));
        intentSendMail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intentSendMail, "Send mail..."));
    }

    private void changePassword() {
        String email = mAuth.getCurrentUser().getEmail();
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Snackbar.make(tvChangePass, "Success! " + "Please check new password in your email!", Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    private void changeUserName() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View promptsView = inflater.inflate(R.layout.dialog_change_name, null);
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(promptsView);
        final EditText newUserName = promptsView.findViewById(R.id.edt_change_name);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Change",
                        (dialog, id) -> {
                            String newName = newUserName.getText().toString();
                            mUser.child("Users").child(mAuth.getCurrentUser().getUid()).child("Name").setValue(newName);
                            displayUserName();
                        })
                .setNegativeButton("Cancel",
                        (dialog, id) -> dialog.cancel());
        alertDialogBuilder.show();
    }

    private void displayUserName() {
        mUser.child("Users")
                .child(mAuth.getCurrentUser().getUid())
                .child("Name")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.getValue(String.class);
                tvUserName.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select image"), REQUEST_CODE_PICK_IMAGE);
    }

    private void logOut() {
        new AlertDialog.Builder(getContext()).setMessage("You want to log out?")
                .setPositiveButton("Continue", (dialog, which) -> {
                    dialog.dismiss();
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getActivity(), StartActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();

    }

}
