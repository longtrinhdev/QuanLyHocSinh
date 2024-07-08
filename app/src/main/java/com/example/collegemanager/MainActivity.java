package com.example.collegemanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import com.example.collegemanager.Banner.Banner;
import com.example.collegemanager.Banner.BannerAdapter;
import com.example.collegemanager.Fragment.CreateFragment;
import com.example.collegemanager.Fragment.HomeFragment;
import com.example.collegemanager.Fragment.QueryFragment;
import com.example.collegemanager.Fragment.SettingFragment;
import com.example.collegemanager.Fragment.ShowBackground;
import com.example.collegemanager.Fragment.UpdateFragment;
import com.example.collegemanager.Student.student;
import com.example.collegemanager.User.User;
import com.example.collegemanager.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements HomeFragment.ISendData , QueryFragment.IClickItemBackground {

    public ActivityMainBinding binding;
    public static final int FRAGMENT_HOME =0;
    public static final int FRAGMENT_QUERY =1;
    public static final int FRAGMENT_CREATE =2;
    public static final int FRAGMENT_SETTING =3;

    public static  int CURRENT_FRAGMENT = FRAGMENT_HOME;

    private String hoTen, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle1 = getIntent().getBundleExtra("user");
        Bundle bundle2 = getIntent().getBundleExtra("user");
        Bundle bundle3 = getIntent().getBundleExtra("user");


        if(bundle1 != null) {
            email = bundle1.getString("email");
            hoTen = "Application";
        }
        if(bundle2 != null) {
            email = bundle2.getString("email");
            hoTen = bundle2.getString("name");
        }
        if(bundle3 != null) {
            email = bundle3.getString("email");
            hoTen = "Application";
        }


        getValue();
        replaceFragment(new HomeFragment(),null);
        binding.bottomNav.setItemSelected(R.id.Home, true);
        onClickBottomNavigation();

    }

    private void onClickBottomNavigation() {
        binding.bottomNav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {

                if(i == R.id.Home) {
                    if(CURRENT_FRAGMENT != FRAGMENT_HOME) {
                        replaceFragment(new HomeFragment(),null);
                        CURRENT_FRAGMENT = FRAGMENT_HOME;
                    }
                }else if(i == R.id.Query) {
                    if(CURRENT_FRAGMENT != FRAGMENT_QUERY) {
                        replaceFragment(new QueryFragment(),null);
                        CURRENT_FRAGMENT = FRAGMENT_QUERY;
                    }
                }else if(i == R.id.Insert) {
                    if(CURRENT_FRAGMENT != FRAGMENT_CREATE) {
                        replaceFragment(new CreateFragment(),null);
                        CURRENT_FRAGMENT = FRAGMENT_CREATE;
                    }
                }else if(i == R.id.Setting) {
                    if(CURRENT_FRAGMENT != FRAGMENT_SETTING) {
                        replaceFragment(new SettingFragment(),null);
                        CURRENT_FRAGMENT = FRAGMENT_SETTING;
                    }
                }
            }
        });
    }

    public void replaceFragment(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(tag == null) {
            fragmentTransaction.replace(R.id.frameLayout, fragment);
        }else {
            fragmentTransaction.replace(R.id.frameLayout, fragment,tag);
        }

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {
        if(CURRENT_FRAGMENT != FRAGMENT_HOME) {
            replaceFragment(new HomeFragment(),null);
            binding.bottomNav.setItemSelected(R.id.Home, true);
        }
        if(CURRENT_FRAGMENT == FRAGMENT_HOME) {
            super.onBackPressed();
        }

    }
    @Override
    public void senData(student student) {
        UpdateFragment fragment = (UpdateFragment) getSupportFragmentManager().findFragmentByTag("UPDATE_FRAGMENT_TAG");
        if (fragment != null) {
            fragment.receiveDataFromFragmentHome(student);
        }
    }

    @Override
    public void sendData(student student) {
        ShowBackground fragment = (ShowBackground) getSupportFragmentManager().findFragmentByTag("SHOW_FRAGMENT_TAG");
        if (fragment != null) {
            fragment.showInfor(student);
        }
    }

    public String getHoTen() {
        return hoTen;
    }

    public String getEmail() {
        return email;
    }


    private void getValue() {
        DatabaseReference myRef= FirebaseDatabase.getInstance().getReference("User");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<User> users = new ArrayList<>();
                for(DataSnapshot snapshot1: snapshot.getChildren()) {
                    User user = snapshot1.getValue(User.class);
                    users.add(user);
                }

                for(User user:users ){
                    if(email.equals(user.getEmail())) {
                        hoTen = user.getName();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Chưa cập nhật tên người dùng", Toast.LENGTH_SHORT).show();
            }
        });
    }

}