package com.example.studentdata

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StudentDao {

   @Query("SELECT * FROM student_table")
   fun  getAll():List<Student>

   @Query("SELECT * FROM student_table WHERE rollNo LIKE :roll LIMIT 1")
   suspend fun findByRoll(roll:Int):Student

   @Insert(onConflict= OnConflictStrategy.IGNORE)
   suspend fun insert(student: Student)

   @Delete
   suspend fun delete(student: Student)
   @Query("UPDATE student_table SET firstName=:firstname,lastName=:lastname WHERE rollNo LIKE :roll")
   suspend fun update(firstname:String,lastname:String,roll: Int)

   @Query("DELETE FROM student_table")
   suspend fun deleteAll()
}