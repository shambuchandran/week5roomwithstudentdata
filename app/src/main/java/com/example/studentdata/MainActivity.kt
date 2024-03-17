package com.example.studentdata

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.studentdata.databinding.ActivityMainBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database=AppDatabase.getDatabase(this)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnWriteData.setOnClickListener {
            writeData()
        }
        binding.btnReadData.setOnClickListener {
            readData()
        }

    }
    private fun writeData(){
        val firstname=binding.etFirstName.text.toString()
        val lastname=binding.etLastName.text.toString()
        val rollno=binding.etRollNo.text.toString()
        if (firstname.isNotEmpty()&&lastname.isNotEmpty()&&rollno.isNotEmpty()){
            val student=Student(0,firstname,lastname,rollno.toInt())
            GlobalScope.launch (Dispatchers.IO){
                database.studentDao().insert(student)
        }
            binding.etFirstName.text.clear()
            binding.etLastName.text.clear()
            binding.etRollNo.text.clear()
            Toast.makeText(this@MainActivity, "Successfully added", Toast.LENGTH_SHORT).show()

        }else{
            Toast.makeText(this@MainActivity, "Please enter details", Toast.LENGTH_SHORT).show()
        }

    }
    private fun displayData(student: Student){
            binding.tvFirstName.text=student.firstName
            binding.tvLastName.text=student.lastName
            binding.tvRollNo.text=student.rollNo.toString()

    }

    private fun readData(){
        val rollNo=binding.etRollNo.text.toString()
        if (rollNo.isNotEmpty()){
            var student:Student
            GlobalScope.launch(Dispatchers.IO) {
                student=database.studentDao().findByRoll(rollNo.toInt())
                displayData(student)
            }
        }

    }
}