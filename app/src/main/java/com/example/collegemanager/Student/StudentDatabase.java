package com.example.collegemanager.Student;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {student.class},version = 1)
public abstract class StudentDatabase extends RoomDatabase {
    public static final String table_name = "student.db";
    public static StudentDatabase instance;
    public static synchronized StudentDatabase  getInstanceStudent(Context context) {

        if (instance == null){
            instance = Room.databaseBuilder(context,StudentDatabase.class,table_name)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance ;
    }
    public abstract StudentDao studentDao();
}
