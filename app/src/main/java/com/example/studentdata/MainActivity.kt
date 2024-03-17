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
import kotlinx.coroutines.withContext

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
        binding.btnDeleteAll.setOnClickListener {
            GlobalScope.launch {
                database.studentDao().deleteAll()
            }
        }
        binding.updatebtn.setOnClickListener {
            updateData()
        }

    }
    private fun updateData() {
        val firstname=binding.etFirstName.text.toString()
        val lastname=binding.etLastName.text.toString()
        val rollno=binding.etRollNo.text.toString()
        if (firstname.isNotEmpty()&&lastname.isNotEmpty()&&rollno.isNotEmpty()){
            GlobalScope.launch (Dispatchers.IO){
                database.studentDao().update(firstname,lastname,rollno.toInt())
            }
            binding.etFirstName.text.clear()
            binding.etLastName.text.clear()
            binding.etRollNo.text.clear()
            Toast.makeText(this@MainActivity, "Successfully updated", Toast.LENGTH_SHORT).show()

        }else{
            Toast.makeText(this@MainActivity, "Please enter details", Toast.LENGTH_SHORT).show()
        }

    }

    private fun writeData(){
        val firstname=binding.etFirstName.text.toString()
        val lastname=binding.etLastName.text.toString()
        val rollno=binding.etRollNo.text.toString()
        if (firstname.isNotEmpty()&&lastname.isNotEmpty()&&rollno.isNotEmpty()){
            val student=Student(null,firstname,lastname,rollno.toInt())
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
    private suspend fun displayData(student: Student?){
//        withContext(Dispatchers.Main) {
//            binding.tvFirstName.text = student.firstName
//            binding.tvLastName.text = student.lastName
//            binding.tvRollNo.text = student.rollNo.toString()
//        }
        student?.let {
            withContext(Dispatchers.Main) {
                binding.tvFirstName.text = it.firstName
                binding.tvLastName.text = it.lastName
                binding.tvRollNo.text = it.rollNo.toString()
            }
        } ?: run {
            // Handle case where student is null, for example, clear text views
            withContext(Dispatchers.Main) {
                binding.tvFirstName.text = "No data"
                binding.tvLastName.text = "No data"
                binding.tvRollNo.text = "No data"
            }
        }
    }



    private fun readData(){
        val rollNo=binding.etRollNoRead.text.toString()
        if (rollNo.isNotEmpty()){
            var student:Student
            GlobalScope.launch(Dispatchers.IO) {
                student=database.studentDao().findByRoll(rollNo.toInt())
                displayData(student)
            }
        }

    }
}