package com.example.collegemanager.Student;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegemanager.IClickItembackground;
import com.example.collegemanager.R;

import java.util.List;

public class QueryAdapter extends RecyclerView.Adapter<QueryAdapter.QueryViewHolder> {
    private List<student> lst;
    private IClickItembackground iClickItembackground;

    public QueryAdapter(IClickItembackground iClickItembackground) {
        this.iClickItembackground = iClickItembackground;
    }

    @NonNull
    @Override
    public QueryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_list, parent, false);

        return new QueryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QueryViewHolder holder, int position) {
        student student = lst.get(position);
        if(student == null) {
            return;
        }
        holder.txtTen.setText(student.getHoTen());
        holder.txtToan.setText(String.valueOf(student.getDiemToan()));
        holder.txtVan.setText(String.valueOf(student.getDiemVan()));
        holder.txtAnh.setText(String.valueOf(student.getDiemAnh()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItembackground.actionIntent(student);
            }
        });


    }

    @Override
    public int getItemCount() {
        if(lst == null) {
            return  0;
        }
        return lst.size();
    }

    public void loadData(List<student> users) {
        this.lst = users;
    }

    public class QueryViewHolder extends RecyclerView.ViewHolder{
        private TextView txtTen , txtToan, txtVan, txtAnh;

        public QueryViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTen = itemView.findViewById(R.id.txtTen);
            txtToan = itemView.findViewById(R.id.txtToan);
            txtVan = itemView.findViewById(R.id.txtVan);
            txtAnh = itemView.findViewById(R.id.txtAnh);


        }
    }
}
