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
import androidx.fragment.app.FragmentTransaction;

import com.example.collegemanager.Student.StudentDatabase;
import com.example.collegemanager.databinding.UpdateFragmentBinding;
import  com.example.collegemanager.Student.student;

public class UpdateFragment extends Fragment{
    private UpdateFragmentBinding binding;
    private static final String ARG_STUDENT = "student";
    private HomeFragment homeFragment;
    private student student;

    // Phương thức tĩnh để tạo instance của UpdateFragment với dữ liệu student
    public static UpdateFragment newInstance(student student) {
        UpdateFragment fragment = new UpdateFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_STUDENT, student);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = UpdateFragmentBinding.inflate(inflater, container, false);

        if (getArguments() != null) {
            student = (student) getArguments().getSerializable(ARG_STUDENT);
            receiveDataFromFragmentHome(student);
        }
        homeFragment = new HomeFragment();

        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStudent();
            }
        });

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });


        return binding.getRoot();
    }

    public void receiveDataFromFragmentHome(student student) {
        if(student != null) {
            binding.edtName.setText(student.getHoTen());
            binding.edtMSV.setText(student.getMaSV());
            binding.edtMath.setText(String.valueOf(student.getDiemToan()));
            binding.edtVan.setText(String.valueOf(student.getDiemVan()));
            binding.edtAnh.setText(String.valueOf(student.getDiemAnh()));
        }
    }

    private void updateStudent() {
        String name = binding.edtName.getText().toString().trim();
        String maSV = binding.edtMSV.getText().toString().trim();
        String diemToan = binding.edtMath.getText().toString().trim();
        String diemVan = binding.edtVan.getText().toString().trim();
        String diemAnh = binding.edtAnh.getText().toString().trim();

        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(maSV) || TextUtils.isEmpty(diemToan) || TextUtils.isEmpty(diemVan)|| TextUtils.isEmpty(diemAnh)) {
            Toast.makeText(getContext(), "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        float toan = Float.parseFloat(diemToan);
        float van = Float.parseFloat(diemVan);
        float anh = Float.parseFloat(diemAnh);

        student.setHoTen(name);
        student.setMaSV(maSV);
        student.setDiemToan(toan);
        student.setDiemVan(van);
        student.setDiemAnh(anh);


        StudentDatabase.getInstanceStudent(getContext()).studentDao().update(student);
        Toast.makeText(getContext(), "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();



    }
}
