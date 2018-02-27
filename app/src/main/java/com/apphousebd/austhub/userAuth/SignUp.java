/*
 * Created by Asif Imtiaz Shaafi
 *     Email: a15shaafi.209@gmail.com
 *     Date: 2, 2018
 *
 * Copyright (c) 2018, AppHouseBD. All rights reserved.
 *
 * Last Modified on 2/27/18 1:41 PM
 * Modified By: shaafi
 */

package com.apphousebd.austhub.userAuth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.apphousebd.austhub.R;
import com.apphousebd.austhub.dataModel.UserModel;
import com.apphousebd.austhub.mainUi.activities.MainActivity;
import com.apphousebd.austhub.utilities.Constants;
import com.apphousebd.austhub.utilities.ManageImageFile;
import com.apphousebd.austhub.utilities.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import static com.apphousebd.austhub.utilities.ManageImageFile.REQUEST_CODE;

@SuppressWarnings("FieldCanBeLocal")
public class SignUp extends AppCompatActivity {

    public static final String USER_DETAILS = "user_details";

    //user name
    private TextInputLayout mUserNameLayout;
    private TextView mUserName;

    //user year
    private Spinner mYearSpinner;
    private int year = 0;

    //user semester
    private Spinner mSemesterSpinner;
    private int sem = 0;
    private ImageView mUplodedImage;
    private Uri imageUri = null;

    // user dept
    private Spinner mDeptSpinner;
    private String dept = "cse";

    //user section
    private Spinner mSectionSpinner;
    private String sec = "section_a";

    private FirebaseUser mFirebaseUser;
    private ProgressDialog dialog;
    ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (mFirebaseUser == null)
            finish();
        else {

            dialog = new ProgressDialog(SignUp.this);
            dialog.setMessage("Loading...");
            dialog.setCancelable(false);


            setUpDeptSpinner();
            setUpYearSpinner();
            setUpSemesterSpinner();
            setSectionSpinner();
            setUpUserName_N_Email();

            //image
            mUplodedImage = findViewById(R.id.sign_up_user_img);
            mUplodedImage.setDrawingCacheEnabled(true);
            mUplodedImage.buildDrawingCache();

            if (mFirebaseUser.getPhotoUrl() != null && !TextUtils.isEmpty(mFirebaseUser.getPhotoUrl().toString())) {
                Picasso.with(SignUp.this)
                        .load(mFirebaseUser.getPhotoUrl())
                        .fit()
                        .into(mUplodedImage);
            }
        }
    }

    private void setUpDeptSpinner() {
        mDeptSpinner = findViewById(R.id.sign_up_user_dept);

        final String[] deptList = getResources().getStringArray(R.array.std_dept_value);


        List<String> years = Arrays.asList(getResources().getStringArray(R.array.std_dept_title));

        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mDeptSpinner.setAdapter(arrayAdapter);

        mDeptSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dept = deptList[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setUpYearSpinner() {
        mYearSpinner = findViewById(R.id.sign_up_user_year);


        List<String> years = Arrays.asList(getResources().getStringArray(R.array.std_year_title));

        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mYearSpinner.setAdapter(arrayAdapter);

        mYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                year = i + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setUpSemesterSpinner() {

        mSemesterSpinner = findViewById(R.id.sign_up_user_semester);

        List<String> years = Arrays.asList(getResources().getStringArray(R.array.std_sem_title));

        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSemesterSpinner.setAdapter(arrayAdapter);

        mSemesterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sem = i + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setSectionSpinner() {
        mSectionSpinner = findViewById(R.id.sign_up_user_section);

        final List<String> sections = Arrays.asList(getResources().getStringArray(R.array.std_section_title));
        final List<String> sectionValues = Arrays.asList(getResources().getStringArray(R.array.std_section_value));

        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sections);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSectionSpinner.setAdapter(arrayAdapter);

        mSectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sec = sectionValues.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpUserName_N_Email() {
        mUserName = findViewById(R.id.sign_up_user_name);
        if (mFirebaseUser != null && !TextUtils.isEmpty(mFirebaseUser.getDisplayName())) {
            mUserName.setText(mFirebaseUser.getDisplayName());
        }

        mUserNameLayout = findViewById(R.id.sign_up_user_name_layout);

    }

    public void performSignUp(View view) {

        final String name = mUserName.getText().toString().trim();

        if (validateInputs(name) && year != 0 && sem != 0) {

            dialog.show();

            // uploading the image
            UploadTask uploadTask;
            StorageReference reference = FirebaseStorage.getInstance().getReference(Constants.USER_PROFILE_IMAGE_STORAGE + mFirebaseUser.getUid());
            if (imageUri == null) {
                imageUri = Uri.parse("android.resource://com.apphousebd.austhub/mipmap/ic_launcher");
            }
            uploadTask = reference.putFile(imageUri);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    registerUser(name, "");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    if (downloadUrl != null)
                        registerUser(name, downloadUrl.toString());
                    else {
                        registerUser(name, "");
                    }
                }
            });
        }

    }

    private void registerUser(String name, String imgLink) {
        PreferenceManager.setDefaultValues(this, R.xml.settings, true);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        preferences.edit()
                .putString(getString(R.string.std_dept), dept)
                .putString(getString(R.string.std_year), String.valueOf(year))
                .putString(getString(R.string.std_sem), String.valueOf(sem))
                .putString(getString(R.string.std_sec), sec)
                .apply();


        String email;
        if (TextUtils.isEmpty(mFirebaseUser.getEmail())) {
            email = "";
        } else {
            email = mFirebaseUser.getEmail();
        }

        String phone;
        if (TextUtils.isEmpty(mFirebaseUser.getPhoneNumber())) {
            phone = "";
        } else {
            phone = mFirebaseUser.getPhoneNumber();
        }

        UserModel userModel = new UserModel(
                name, email, phone, mFirebaseUser.getUid(), imgLink, dept, String.valueOf(sem),
                String.valueOf(year), String.valueOf(sec)
        );

        Utils.saveUser(getApplicationContext(), userModel);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
        reference.child(mFirebaseUser.getUid()).setValue(userModel);

        if (Utils.getDataVersion(getApplicationContext()) == 0) {
            if (dialog.isShowing())
                dialog.dismiss();

            Intent data = new Intent();
            data.setAction(Constants.DOWNLOAD);
            String dept = userModel.getDept();
            data.setData(Uri.parse(dept));

            setResult(RESULT_OK, data);
            finish();
        } else {
            proceedToMain(userModel);

            if (dialog.isShowing())
                dialog.dismiss();

            Intent data = new Intent();
            data.setAction(Constants.DONE);
            setResult(RESULT_OK, data);
            finish();
        }
    }

    private void proceedToMain(UserModel model) {
        Intent mainIntent = new Intent(getBaseContext(), MainActivity.class);
        mainIntent.setAction("user");
        mainIntent.putExtra(getString(R.string.user), model);
        startActivity(mainIntent);
        finish();
    }

    private boolean validateInputs(String name) {

//        boolean emailOk;
        boolean nameOk;

        //checkin if the name and email is not emplty and valid

        if (TextUtils.isEmpty(mFirebaseUser.getDisplayName()) && TextUtils.isEmpty(name)) {
            mUserNameLayout.setError("Please Enter Your Name!");
            nameOk = false;
        } else {
            nameOk = true;
            mUserNameLayout.setError(null);
        }
//
//        if (TextUtils.isEmpty(email) || !email.contains("@") || !email.contains(".com")) {
//            mUserEmailLayout.setError("Please Enter Your Valid Email!");
//            emailOk = false;
//        } else {
//            emailOk = true;
//            mUserEmailLayout.setError(null);
//        }

        return nameOk;
    }

    public void finishActivity(View view) {
        finish();
    }

    public void getImage(View v) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_CODE);
    }


    //adding the image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            mUplodedImage.setImageURI(imageUri);

            Bitmap bitmap = ((BitmapDrawable) mUplodedImage.getDrawable()).getBitmap();
            ManageImageFile.saveImage(getBaseContext(), bitmap);
        }
    }
}
