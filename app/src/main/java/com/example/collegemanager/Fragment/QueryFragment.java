package com.example.collegemanager.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegemanager.IClickItembackground;
import com.example.collegemanager.MainActivity;
import com.example.collegemanager.R;
import com.example.collegemanager.Student.QueryAdapter;
import com.example.collegemanager.Student.StudentDatabase;
import com.example.collegemanager.Student.student;
import com.example.collegemanager.databinding.QueryFragmentBinding;

import java.util.ArrayList;
import java.util.List;

public class QueryFragment extends Fragment {
    private QueryFragmentBinding binding;
    private QueryAdapter adapter;
    private List<student> myList;
    private MainActivity mMainActivity;
    public IClickItemBackground iClickItemBackground;
    public interface  IClickItemBackground{
        void sendData(student student);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = QueryFragmentBinding.inflate(inflater, container, false);

        myList = new ArrayList<>();
        adapter = new QueryAdapter(new IClickItembackground() {
            @Override
            public void actionIntent(student student) {

                MainActivity mainActivity = (MainActivity)getActivity();
                assert mainActivity != null;
                ShowBackground fragment = ShowBackground.getInstance(student);
                mainActivity.replaceFragment(fragment,"SHOW_FRAGMENT_TAG");
                iClickItemBackground.sendData(student);
            }
        });
        mMainActivity =(MainActivity) getActivity();
        showItemInList();

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainActivity.replaceFragment(new HomeFragment(), null);
                mMainActivity.binding.bottomNav.setItemSelected(R.id.Home, true);
            }
        });


        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        iClickItemBackground = (IClickItemBackground) getActivity();
    }

    private void showItemInList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.rcvList.setLayoutManager(linearLayoutManager);
        myList = StudentDatabase.getInstanceStudent(getContext()).studentDao().orderSort();
        adapter.loadData(myList);
        binding.rcvList.setAdapter(adapter);
    }


}
