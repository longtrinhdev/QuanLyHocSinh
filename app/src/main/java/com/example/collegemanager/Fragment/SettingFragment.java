package com.example.collegemanager.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.collegemanager.LoginActivity;
import com.example.collegemanager.MainActivity;
import com.example.collegemanager.ManualActivity;
import com.example.collegemanager.R;
import com.example.collegemanager.databinding.SettingFragmentBinding;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;

public class SettingFragment extends Fragment {
    private SettingFragmentBinding binding;
    private MainActivity mMainActivity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SettingFragmentBinding.inflate(inflater, container, false);

        mMainActivity =(MainActivity) getActivity();

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainActivity.replaceFragment(new HomeFragment(), null);
                mMainActivity.binding.bottomNav.setItemSelected(R.id.Home, true);
            }
        });


        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder  builder = new AlertDialog.Builder(getContext())
                        .setTitle("Đăng xuất")
                        .setMessage("Bạn muốn đăng xuất?")
                        .setCancelable(false)
                        .setNegativeButton("Thoát", null)
                        .setPositiveButton("Tiếp tục", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                                firebaseAuth.signOut();
                                Toast.makeText(getContext(), "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                                if(getActivity()!= null) {
                                    getActivity().finishAffinity();
                                }
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        binding.btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainActivity.replaceFragment(new ChangePasswordFragment(), null);
            }
        });

        binding.txtUsername.setText(mMainActivity.getHoTen());
        binding.txtEmail.setText(mMainActivity.getEmail());



        // xem hướng dẫn
        binding.btnInstructApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ManualActivity.class));
            }
        });
        return binding.getRoot();
    }

}
