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
import com.example.collegemanager.Student.StudentDatabase;
import com.example.collegemanager.Student.student;
import com.example.collegemanager.databinding.CreateFragmentBinding;

import java.util.List;
import com.example.collegemanager.Student.student;

public class CreateFragment extends Fragment {
    private CreateFragmentBinding binding;
    private MainActivity mMainActivity;
    private List<student> myList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = CreateFragmentBinding.inflate(inflater,container,false);

        mMainActivity =(MainActivity) getActivity();

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniUI();
            }
        });
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainActivity.replaceFragment(new HomeFragment(), null);
                mMainActivity.binding.bottomNav.setItemSelected(R.id.Home,true);
            }
        });

        return binding.getRoot();
    }

    private void iniUI() {
        String name = binding.edtName.getText().toString().trim();
        String MSV = binding.edtMSV.getText().toString().trim();
        String diem_toan = binding.edtMath.getText().toString().trim();
        String diem_van = binding.edtVan.getText().toString().trim();
        String diem_anh = binding.edtAnh.getText().toString().trim();


        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(MSV)|TextUtils.isEmpty(diem_toan)|TextUtils.isEmpty(diem_van)|TextUtils.isEmpty(diem_anh)) {
            Toast.makeText(getContext(),"Mời nhập thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        float diemToan = Float.parseFloat(binding.edtMath.getText().toString().trim());
        float diemVan = Float.parseFloat(binding.edtVan.getText().toString().trim());
        float diemAnh = Float.parseFloat(binding.edtAnh.getText().toString().trim());

        if( !(checkScore(diemToan) && checkScore(diemVan) && checkScore(diemAnh))) {
            Toast.makeText(getContext(),"Xin nhập điểm trong thang điểm 10", Toast.LENGTH_SHORT).show();
            return;
        }

        student obj = new student(name, MSV, diemToan, diemVan, diemAnh);
        myList = StudentDatabase.getInstanceStudent(getContext()).studentDao().isCheckStudent(MSV);
        for(student it: myList) {
            if(MSV.equals(it.getMaSV())) {
                Toast.makeText(getContext(), "Học sinh đã tồn tại", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        StudentDatabase.getInstanceStudent(getContext()).studentDao().Insert(obj);
        Toast.makeText(getContext(), "Thêm học sinh thành công", Toast.LENGTH_SHORT).show();
    }

    private boolean checkScore(float score) {
        return score >= 0 && score <= 10;
    }




}
