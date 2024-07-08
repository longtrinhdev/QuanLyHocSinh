package com.example.collegemanager.Student;

import android.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegemanager.IClickItem;
import com.example.collegemanager.R;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    private List<student> lst;
    private final IClickItem iClickItem;

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item, parent, false);
        return new StudentViewHolder(view);
    }

    public StudentAdapter(IClickItem iClickItem) {
        this.iClickItem = iClickItem;
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        student student = lst.get(position);
        if(student == null) {
            return;
        }
        holder.txtName.setText(student.getHoTen());
        holder.txtMSV.setText(student.getMaSV());

        holder.imgBtnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // tạo một optionMenu
                PopupMenu popupMenu = new PopupMenu(v.getContext(), holder.imgBtnMore);
                popupMenu.inflate(R.menu.option);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if(item.getItemId() == R.id.update) {
                            iClickItem.actionIntent(student);
                            return true;
                        }
                        if(item.getItemId() == R.id.delete) {
                            iClickItem.showDialogDelete(student);
                            return true;
                        }
                        return  false;
                    }
                });
                popupMenu.show();

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Click vào biểu tượng dấu ba chấm để chọn chức năng", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        if(lst != null){
            return lst.size();
        }
        return 0;
    }
    public void setData(List<student> myList) {
        this.lst = myList;
        notifyDataSetChanged();
    }


    public class StudentViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgBtnMore;
        private TextView txtName, txtMSV;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBtnMore = itemView.findViewById(R.id.imgBtnMore);
            txtName = itemView.findViewById(R.id.txtHoten);
            txtMSV = itemView.findViewById(R.id.txtMSV);
        }
    }
}
