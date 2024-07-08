package com.example.collegemanager.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.collegemanager.MainActivity;
import com.example.collegemanager.Student.student;
import com.example.collegemanager.databinding.FragmentshowbackgroundBinding;

public class ShowBackground extends Fragment {
    private FragmentshowbackgroundBinding binding;
    private static final String ARG_STUDENT = "student";
    private MainActivity mMainActivity;

    public static ShowBackground getInstance(student student) {
        ShowBackground fragment = new ShowBackground();
        Bundle args = new Bundle();
        args.putSerializable(ARG_STUDENT, student);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentshowbackgroundBinding.inflate(inflater, container, false);

        if(getArguments() != null) {
            student student = (student)getArguments().getSerializable(ARG_STUDENT);
            showInfor(student);
        }
        mMainActivity = (MainActivity) getActivity();

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainActivity.replaceFragment(new QueryFragment(), null);
            }
        });

        return binding.getRoot();
    }

    public void showInfor(student student) {
        if(student != null) {
            binding.txtTen.setText(student.getHoTen());
            binding.txtMSV.setText(student.getMaSV());
            double tbc = 1.0*(student.getDiemToan() + student.getDiemAnh() + student.getDiemVan()) /3;
            if(tbc >= 8.5) {
                binding.txtXepLoai.setText("Giỏi");
            }else if(tbc >=7 && tbc < 8.5) {
                binding.txtXepLoai.setText("Khá");

            }else if (tbc >=5 && tbc <7){
                binding.txtXepLoai.setText("TB");
            }else {
                binding.txtXepLoai.setText("Yếu");
            }
            binding.txtDiemToan.setText(String.valueOf(student.getDiemToan()));
            binding.txtDiemVan.setText(String.valueOf(student.getDiemVan()));
            binding.txtDiemAnh.setText(String.valueOf(student.getDiemAnh()));

        }

    }

}
