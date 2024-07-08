package com.example.collegemanager;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.collegemanager.databinding.ActivityLoginBinding;
import com.github.ybq.android.spinkit.style.Circle;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private int cnt = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickButtonLogin();
            }
        });

        binding.imgSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSignup();
            }
        });

        binding.txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickForgotPassword();

            }
        });

        binding.imgEyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cnt %2 == 0) {
                    binding.imgEyes.setImageResource(R.drawable.show);
                    binding.edtPassword.setTransformationMethod(null);
                }else {

                    binding.imgEyes.setImageResource(R.drawable.hide);
                    binding.edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                cnt ++;
            }
        });

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
                    binding.imgEyes.setColorFilter(ContextCompat.getColor(LoginActivity.this, R.color.black), PorterDuff.Mode.SRC_IN);

                }else {
                    binding.edtPassword.setTextColor(Color.parseColor("#aaaaaa"));
                    binding.imgEyes.setColorFilter(ContextCompat.getColor(LoginActivity.this, R.color.gray), PorterDuff.Mode.SRC_IN);
                }
            }
        });

    }

    private void onClickButtonLogin() {
        String email = binding.edtAccount.getText().toString().trim();
        String password = binding.edtPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)) {
            Toast.makeText(LoginActivity.this,"Bạn chưa nhập tài khoản", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this,"Bạn chưa nhập mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Không đúng định dạng email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(password.length() <8) {
            Toast.makeText(LoginActivity.this,"Mật khẩu phải lớn hơn 8 kí tự", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!isCheckPassword(password)) {
            Toast.makeText(LoginActivity.this,"Mật khẩu phải chứa ít nhất 1  kí tự đặc biệt", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.progress.setVisibility(View.VISIBLE);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            binding.progress.setVisibility(View.GONE);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("email", email);
                            intent.putExtra("user", bundle);
                            startActivity(intent);
                            finishAffinity();
                        } else {

                            binding.progress.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }

    private void onClickSignup() {
        startActivity(new Intent(LoginActivity.this, SignupActivity.class));
    }

    private void onClickForgotPassword() {
        View alertDialog = getLayoutInflater().inflate(R.layout.layout_dialog_password,null);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setCancelable(false);
        alert.setView(alertDialog);

        AlertDialog dialog = alert.create();
        dialog.show();

        EditText edtEmail = alertDialog.findViewById(R.id.edt_email);
        Button btnBack = alertDialog.findViewById(R.id.btnBack);
        Button btnReset = alertDialog.findViewById(R.id.btnReset);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email  = edtEmail.getText().toString().trim();

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(alert.getContext(), "Tài khoản email không đúng định dạng", Toast.LENGTH_SHORT).show();
                    return;
                }
                ActionResetPassword(email);
                dialog.dismiss();
            }
        });
    }

    private void ActionResetPassword(String email) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        binding.progress.setVisibility(View.VISIBLE);
        binding.progressBar.setIndeterminateDrawable(new FoldingCube());

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            binding.progress.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Gửi yêu cầu thành công", Toast.LENGTH_SHORT).show();

                        }else {
                            binding.progress.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Gửi yêu cầu thất bại", Toast.LENGTH_SHORT).show();
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

}