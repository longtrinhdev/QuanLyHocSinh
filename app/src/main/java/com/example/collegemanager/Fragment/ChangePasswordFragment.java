package com.example.collegemanager.Fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.collegemanager.MainActivity;
import com.example.collegemanager.R;
import com.example.collegemanager.databinding.ChangepasswordFragmentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordFragment extends Fragment {
    private ChangepasswordFragmentBinding binding;
    private MainActivity mMainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ChangepasswordFragmentBinding.inflate(inflater, container, false);

        mMainActivity = (MainActivity)getActivity();
        binding.btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InitUi();
            }
        });

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainActivity.replaceFragment(new SettingFragment(), null);
            }
        });

        binding.txtUsername.setText(mMainActivity.getHoTen());
        binding.txtEmail.setText(mMainActivity.getEmail());

        return binding.getRoot();
    }

    private void InitUi() {
        String passwordOld = binding.edtPasswordOld.getText().toString().trim();
        String passwordNew = binding.edtPasswordNew.getText().toString().trim();
        String passwordNewAgain = binding.edtChpassAgain.getText().toString().trim();

        if(TextUtils.isEmpty(passwordOld) || TextUtils.isEmpty(passwordNew) ||TextUtils.isEmpty(passwordNewAgain) ) {
            Toast.makeText(getContext(), "Hãy nhập đầu đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!passwordNew.equals(passwordNewAgain)) {
            Toast.makeText(getContext(), "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        binding.progress.setVisibility(View.VISIBLE);
        assert user != null;
        user.updatePassword(passwordNew)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            binding.progress.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                        }else {
                            binding.progress.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Đổi mật khẩu không thành công!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
