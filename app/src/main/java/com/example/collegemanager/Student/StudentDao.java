package com.example.collegemanager.Student;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface  StudentDao {

    @Insert
    void Insert(student student);

    @Query("SELECT * FROM  student")
    List<student> getList();

    @Delete
    void Delete(student student);

    @Update
    void update(student student);

    @Query("SELECT * FROM student WHERE maSV =:msv")
    List<student> isCheckStudent(String msv);

    @Query("SELECT*FROM student WHERE (maSV LIKE '%' || :msv || '%')")
    List<student> search(String msv);

    @Query("SELECT * FROM student ORDER BY (diemToan + diemVan + diemAnh) DESC")
    List<student> orderSort();

    @Query("SELECT * FROM student ORDER BY (diemToan + diemVan + diemAnh) ASC")
    List<student> orderSortReverse();


}
