package com.example.collegemanager.Fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import com.example.collegemanager.Banner.Banner;
import com.example.collegemanager.Banner.BannerAdapter;
import com.example.collegemanager.IClickItem;
import com.example.collegemanager.MainActivity;
import com.example.collegemanager.R;
import com.example.collegemanager.Student.StudentAdapter;
import com.example.collegemanager.Student.StudentDatabase;
import com.example.collegemanager.databinding.HomeFragmentBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import com.example.collegemanager.Student.student;

public class HomeFragment extends Fragment {

    private HomeFragmentBinding binding;

    private StudentAdapter adapter;
    private List<student> myList;

    public ISendData iSendData;
    private MainActivity mainActivity;

    public interface ISendData {
        void senData(student student);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = HomeFragmentBinding.inflate(inflater, container, false);

        initBanner();
        autoTextViewSearch();
        myList = new ArrayList<>();
        mainActivity = (MainActivity)getActivity();
        adapter = new StudentAdapter(new IClickItem() {
            @Override
            public void actionIntent(student student) {
                assert mainActivity != null;
                UpdateFragment fragment = UpdateFragment.newInstance(student);
                mainActivity.replaceFragment(fragment,"UPDATE_FRAGMENT_TAG");
                iSendData.senData(student);
            }
            @Override
            public void showDialogDelete(student student) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                        .setTitle("Xóa học sinh")
                        .setMessage("Bạn chắc chắn muốn xóa học sinh này?")
                        .setCancelable(false)
                        .setNegativeButton("Quay lại", null)
                        .setPositiveButton("Tiếp tục", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                StudentDatabase.getInstanceStudent(getContext()).studentDao().Delete(student);
                                Toast.makeText(getContext(), "Xóa Học sinh thành công!", Toast.LENGTH_SHORT).show();
                                loadData();
                            }
                        });
                AlertDialog  dialog = builder.create();
                dialog.show();

            }

        });

        binding.autoCompleteSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    searchStudent();
                }
                return true;
            }
        });

        showItemOnRecyclerView();
        binding.txtName.setText(mainActivity.getHoTen());
        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        iSendData = (ISendData) getActivity();
    }

    private void initBanner() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Banners");
        binding.progressBar.setVisibility(View.VISIBLE);
        ArrayList<Banner> lst = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for(DataSnapshot issue:snapshot.getChildren()) {
                        Banner items = issue.getValue(Banner.class);
                        if (items != null) {
                            lst.add(items);
                        }

                    }
                    if (getActivity() != null && isAdded()) {
                        Banners(lst);
                    }
                    binding.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });

    }

    private void Banners(ArrayList<Banner> lst) {
        binding.viewpager2.setAdapter(new BannerAdapter(lst,binding.viewpager2, getContext()));
        binding.viewpager2.setClipChildren(false);
        binding.viewpager2.setClipToPadding(false);
        binding.viewpager2.setOffscreenPageLimit(3);
        binding.viewpager2.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        binding.viewpager2.setPageTransformer(compositePageTransformer);


    }
    private void autoTextViewSearch() {
        String[] arrays = getResources().getStringArray(R.array.automatic);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.layout_custom_complete, R.id.txtComplete, arrays);
        binding.autoCompleteSearch.setAdapter(adapter);
    }

    private void showItemOnRecyclerView() {
        myList = StudentDatabase.getInstanceStudent(getContext()).studentDao().getList();
        adapter.setData(myList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false);
        binding.rcvItems.setLayoutManager(linearLayoutManager);
        binding.rcvItems.setAdapter(adapter);
    }

    private void searchStudent() {
        String student = binding.autoCompleteSearch.getText().toString().trim();
        List<student> lst = new ArrayList<>();
        if(TextUtils.isEmpty(student)) {
            Toast.makeText(getContext(),"Mời nhập thông tin",Toast.LENGTH_SHORT).show();
            return;
        }

        if(student.equals("Học sinh có điểm cao nhất")) {

            myList = StudentDatabase.getInstanceStudent(getContext()).studentDao().orderSort();
            lst.add(myList.get(0));
            adapter.setData(lst);
            hideSoftKeyboard();
        }else if(student.equals("Học sinh có điểm thấp nhất")) {
            myList = StudentDatabase.getInstanceStudent(getContext()).studentDao().orderSortReverse();
            lst.add(myList.get(0));
            adapter.setData(lst);
            hideSoftKeyboard();

        }else {
            myList = StudentDatabase.getInstanceStudent(getContext()).studentDao().search(student);
            adapter.setData(myList);
            hideSoftKeyboard();
        }
    }

    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(requireView().getWindowToken(), 0);
        }
    }

    private void loadData() {
        myList = StudentDatabase.getInstanceStudent(getContext()).studentDao().getList();
        adapter.setData(myList);
    }



}
