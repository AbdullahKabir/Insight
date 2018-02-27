/*
 * Created by Asif Imtiaz Shaafi
 *     Email: a15shaafi.209@gmail.com
 *     Date: 2, 2018
 *
 * Copyright (c) 2018, AppHouseBD. All rights reserved.
 *
 * Last Modified on 2/27/18 1:33 PM
 * Modified By: shaafi
 */

package com.apphousebd.austhub.userAuth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.apphousebd.austhub.R;
import com.apphousebd.austhub.dataModel.UserModel;
import com.apphousebd.austhub.mainUi.activities.MainActivity;
import com.apphousebd.austhub.utilities.Constants;
import com.apphousebd.austhub.utilities.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.apphousebd.austhub.utilities.ManageImageFile.REQUEST_CODE;
import static com.apphousebd.austhub.utilities.ManageImageFile.getSavedImg;

public class UserProfileActivity extends AppCompatActivity {

    @BindView(R.id.user_img)
    CircleImageView userProImg;
    @BindView(R.id.user_name_edit_text)
    EditText mUserNameEt;
    @BindView(R.id.user_name_layout)
    TextInputLayout mUserNameLayout;
    @BindView(R.id.user_cancel_btn)
    Button mCancelBtn;
    @BindView(R.id.user_save_btn)
    Button mSaveBtn;

    private ProgressDialog dialog;

    private UserModel mUserModel;
    Uri image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        ButterKnife.bind(this);

        mUserModel = Utils.getUserModel(getApplicationContext());

        if (mUserModel == null)
            finish();

        String name = mUserModel.getName();
        mUserNameEt.setText(name);

        dialog = new ProgressDialog(UserProfileActivity.this);
        dialog.setMessage("Updating...");
        dialog.setCancelable(false);

        //loading image with picasso
        File file = getSavedImg(getBaseContext());
        if (!TextUtils.isEmpty(mUserModel.getImgLink())) {
            Picasso.with(getBaseContext())
                    .load(mUserModel.getImgLink())
                    .fit()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(userProImg);
        } else if (file.length() != 0) {
            Log.e("manage image", "getFilePath: " + file);
            Picasso.with(getBaseContext())
                    .load(file)
                    .fit()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(userProImg);
        } else {
            Picasso.with(getBaseContext())
                    .load(R.mipmap.ic_launcher)
                    .fit()
                    .into(userProImg);
        }
    }

    @OnClick(R.id.user_save_btn)
    public void saveUserInfo()
    {
        dialog.show();

        mUserNameLayout.setError(null);
        final String name = mUserNameEt.getText().toString();

        if (TextUtils.isEmpty(name)){
            mUserNameLayout.setError("Please Enter Your Name");
            if (dialog.isShowing())
                dialog.dismiss();

            return;
        }
        // uploading the image
        UploadTask uploadTask;
        StorageReference reference = FirebaseStorage.getInstance().getReference(Constants.USER_PROFILE_IMAGE_STORAGE + mUserModel.getuId());
        if (image == null) {

            updateUserInfo(name, mUserModel.getImgLink());
            return;
        }
        uploadTask = reference.putFile(image);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                updateUserInfo(name, mUserModel.getImgLink());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                if (downloadUrl != null)
                    updateUserInfo(name, downloadUrl.toString());
                else {
                    updateUserInfo(name, mUserModel.getImgLink());
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        returnToMain();
    }

    private void returnToMain() {

        if (dialog != null && dialog.isShowing())
            dialog.dismiss();

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void updateUserInfo(String name, String imgLink) {

        mUserModel.setName(name);
        mUserModel.setImgLink(imgLink);

        Utils.saveUser(getApplicationContext(), mUserModel);
        Map<String, Object> postValues = mUserModel.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + mUserModel.getuId() + "/", postValues);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_USER_DATABASE);
        reference.updateChildren(childUpdates);

        returnToMain();
    }

    @OnClick(R.id.user_img)
    public void getImage(View v) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_CODE);
    }


    //adding the image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            image = data.getData();
            Picasso.with(getBaseContext())
                    .load(image)
                    .into(userProImg);
        }
    }

    @OnClick(R.id.user_cancel_btn)
    public void cancel()
    {
        returnToMain();
    }
}
