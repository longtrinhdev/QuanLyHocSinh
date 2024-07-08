package com.example.collegemanager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.collegemanager.User.User;
import com.example.collegemanager.databinding.ActivityLoginBinding;
import com.example.collegemanager.databinding.ActivitySignupBinding;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private ActivitySignupBinding binding ;
    private int cnt =0;
    private int cnt1 =0;

    private int count =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.edtAccount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    binding.edtAccount.setTextColor(Color.parseColor("#000000"));

                }else {
                    binding.edtAccount.setTextColor(Color.parseColor("#aaaaaa"));
                }
            }
        });
        binding.edtPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    binding.edtPassword.setTextColor(Color.parseColor("#000000"));
                    binding.imgEyes.setColorFilter(ContextCompat.getColor(SignupActivity.this, R.color.black), PorterDuff.Mode.SRC_IN);

                }else {
                    binding.edtPassword.setTextColor(Color.parseColor("#aaaaaa"));
                    binding.imgEyes.setColorFilter(ContextCompat.getColor(SignupActivity.this, R.color.gray), PorterDuff.Mode.SRC_IN);

                }
            }
        });
        binding.edtPasswordA.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    binding.edtPasswordA.setTextColor(Color.parseColor("#000000"));
                    binding.imgEyese.setColorFilter(ContextCompat.getColor(SignupActivity.this, R.color.black), PorterDuff.Mode.SRC_IN);
                }else {
                    binding.edtPasswordA.setTextColor(Color.parseColor("#aaaaaa"));
                    binding.imgEyese.setColorFilter(ContextCompat.getColor(SignupActivity.this, R.color.black), PorterDuff.Mode.SRC_IN);
                }
            }
        });

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickSignup();
                count++;

            }
        });

        binding.imgEyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cnt % 2 ==0) {
                    binding.imgEyes.setImageResource(R.drawable.show);
                    binding.edtPassword.setTransformationMethod(null);
                }else {
                    binding.imgEyes.setImageResource(R.drawable.hide);
                    binding.edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                cnt++;
            }
        });

        binding.imgEyese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cnt1 % 2 ==0) {
                    binding.imgEyese.setImageResource(R.drawable.show);
                    binding.edtPasswordA.setTransformationMethod(null);
                }else {
                    binding.imgEyese.setImageResource(R.drawable.hide);
                    binding.edtPasswordA.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                cnt1++;
            }
        });

    }

    private void onClickSignup() {
        String hoTen = binding.edtName.getText().toString().trim();
        String email = binding.edtAccount.getText().toString().trim();
        String password = binding.edtPassword.getText().toString().trim();
        String passwordA = binding.edtPasswordA.getText().toString().trim();

        if(TextUtils.isEmpty(hoTen)  ||TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(passwordA)) {
            Toast.makeText(SignupActivity.this, "Hãy nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(SignupActivity.this, "Tài khoản email không đúng định dạng", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!passwordA.equals(password)) {
            Toast.makeText(SignupActivity.this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!(isCheckPassword(password) && isCheckPassword(passwordA))) {
            Toast.makeText(SignupActivity.this, "Mật khẩu phải chứa ít nhất 1 kí tự đặc biệt", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.progress.setVisibility(View.VISIBLE);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            binding.progress.setVisibility(View.GONE);
                            Toast.makeText(SignupActivity.this, "Đăng ký thành công",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                            // push lên fb
                            Bundle bundle = new Bundle();
                            bundle.putString("email", email);
                            bundle.putString("name", hoTen);
                            intent.putExtra("user", bundle);
                            pushData(email,hoTen, count);
                            //--end
                            startActivity(intent);
                            finishAffinity();

                        } else {
                            binding.progress.setVisibility(View.GONE);
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(SignupActivity.this, "Tài khoản đã tồn tại",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SignupActivity.this, "Đăng ký thất bại",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
    private boolean isCheckPassword(String password) {
        for(int i = 0 ; i< password.length(); i++) {
            if((password.charAt(i) < 'a' || password.charAt(i) > 'z' ) && (password.charAt(i) < '0' || password.charAt(i) > '9' )) {
                return true;
            }
        }
        return false;
    }

    private void pushData(String email, String hoTen, int cnt) {
        try {
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("User");
            User user  = new User(email, hoTen);
            String id = String.valueOf(cnt);
            myRef.child(id).setValue(user, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}