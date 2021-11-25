package com.example.ecommercetemplate.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommercetemplate.Prevalent.Prevalent;
import com.example.ecommercetemplate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {

    private String check = "";
    private TextView pageTitle, titleQuestions;
    private EditText phoneNumber, question1, question2;
    private Button verifyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        check = getIntent().getStringExtra("check");

        pageTitle = findViewById(R.id.reset_password_txt);
        titleQuestions = findViewById(R.id.title_questions);
        phoneNumber = findViewById(R.id.find_phone_number);
        question1 = findViewById(R.id.question_1);
        question2 = findViewById(R.id.question_2);
        verifyButton = findViewById(R.id.verify_btn);

    }

    @Override
    protected void onStart() {
        super.onStart();
        phoneNumber.setVisibility(View.GONE);

        if (check.equals("settings"))
        {
            pageTitle.setText("Set your Security Questions");
            titleQuestions.setText("Choose and Set your Security Question");
            verifyButton.setText("Set");

            DisplayPreviousAnswers();

            verifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SetAnswers();
                }
            });
        }
        else if (check.equals("login"))
        {
            phoneNumber.setVisibility(View.VISIBLE);
            
            verifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VerifyUser();
                }
            });
        }
    }

    private void SetAnswers()
    {
        String answer1 = question1.getText().toString().toLowerCase();
        String answer2 = question2.getText().toString().toLowerCase();

        if (TextUtils.isEmpty(answer1) || TextUtils.isEmpty(answer2))
        {
            Toast.makeText(ResetPasswordActivity.this, "Answer to the question is required!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(Prevalent.currentOnlineUser.getPhone());

            HashMap<String, Object> userdataMap = new HashMap<>();
            userdataMap.put("answer1",answer1);
            userdataMap.put("answer2",answer2);

            ref.child("Security Questions").updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(ResetPasswordActivity.this, "Your security question has been set successfully!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(ResetPasswordActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    private void DisplayPreviousAnswers()
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(Prevalent.currentOnlineUser.getPhone());

        ref.child("Security Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    String answer1 = snapshot.child("answer1").getValue().toString();
                    String answer2 = snapshot.child("answer2").getValue().toString();

                    question1.setText(answer1);
                    question2.setText(answer2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void VerifyUser() {
        String phone = phoneNumber.getText().toString();
        String answer1 = question1.getText().toString().toLowerCase();
        String answer2 = question2.getText().toString().toLowerCase();

        if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(answer1) && !TextUtils.isEmpty(answer2))
        {

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(phone);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists())
                    {
                        String mPhone = snapshot.child("phone").getValue().toString();

                        if(snapshot.hasChild("Security Questions"))
                        {
                            String ans1 = snapshot.child("Security Questions").child("answer1").getValue().toString();
                            String ans2 = snapshot.child("Security Questions").child("answer2").getValue().toString();

                            if (!ans1.equals(answer1))
                            {
                                Toast.makeText(ResetPasswordActivity.this, "The first answer you entered is not correct!", Toast.LENGTH_SHORT).show();
                            }
                            else if (!ans2.equals(answer2))
                            {
                                Toast.makeText(ResetPasswordActivity.this, "The second answer you entered is not correct!", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                                builder.setTitle("Reset Password");

                                final EditText newPassword = new EditText(ResetPasswordActivity.this);
                                newPassword.setHint("Enter your new password");
                                builder.setView(newPassword);

                                builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (TextUtils.isEmpty(newPassword.getText().toString()))
                                        {
                                            Toast.makeText(ResetPasswordActivity.this, "Password is required!", Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            ref.child("password")
                                                    .setValue(newPassword.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful())
                                                            {
                                                                Toast.makeText(ResetPasswordActivity.this, "Your password has been changed successfully!", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                                                                startActivity(intent);
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });

                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.show();
                            }
                        }
                        else
                        {
                            Toast.makeText(ResetPasswordActivity.this, "You have not set your security questions!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(ResetPasswordActivity.this, "Phone Number was not found!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else
        {
            Toast.makeText(ResetPasswordActivity.this, "Please fill the fields!", Toast.LENGTH_SHORT).show();
        }

    }
}