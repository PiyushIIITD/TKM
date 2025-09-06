package com.xyz.tkm

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.xyz.tkm.adapter.TaskAdapter
import com.xyz.tkm.model.Task
import com.xyz.tkm.network.FirebaseHelper
import com.xyz.tkm.viewmodel.TaskViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var addTaskBtn: ImageView
    private lateinit var taskAdapter: TaskAdapter
    private val taskViewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val logoutBtn: ImageView = findViewById(R.id.logoutBtn)

        FirebaseHelper.fetchTasks { remoteTasks ->
            remoteTasks.forEach { taskViewModel.addTask(it) }
        }
        recyclerView = findViewById(R.id.recyclerViewTasks)
        addTaskBtn = findViewById(R.id.addTaskBtn)

        taskAdapter = TaskAdapter(
            taskList = mutableListOf(),
            onEditClick = { task -> showEditTaskDialog(task) },
            onDeleteClick = { task -> taskViewModel.delete(task) },
            onStatusChange = { task -> toggleTaskStatus(task) },
            onItemClick = { task -> showTaskDetails(task) }
        )

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = taskAdapter
        }
        logoutBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            lifecycleScope.launch {
                taskViewModel.clearAllTasks()
            }

            Toast.makeText(this, "Logged out successfully!", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }


        taskViewModel.allTasks.observe(this) { tasks ->
            taskAdapter.submitList(tasks)
        }
        addTaskBtn.setOnClickListener {
            showAddTaskDialog()
        }
    }

    private fun showTaskDetails(task: Task) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.dialogue_add_task, null)
        val titleInput = view.findViewById<EditText>(R.id.taskTitleInput)
        val descInput = view.findViewById<EditText>(R.id.taskDescInput)
        val saveBtn = view.findViewById<MaterialButton>(R.id.saveTaskBtn)

        titleInput.setText(task.title)
        descInput.setText(task.description)
        saveBtn.isEnabled = false

        dialog.setContentView(view)
        dialog.show()
    }

    private fun showAddTaskDialog() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.dialogue_add_task, null)
        val titleInput = view.findViewById<EditText>(R.id.taskTitleInput)
        val descInput = view.findViewById<EditText>(R.id.taskDescInput)
        val saveBtn = view.findViewById<MaterialButton>(R.id.saveTaskBtn)

        saveBtn.setOnClickListener {
            val title = titleInput.text.toString().trim()
            val desc = descInput.text.toString().trim()
            if (title.isNotEmpty()) {
                val task = Task(title = title, description = desc)
                taskViewModel.addTask(task)
                Toast.makeText(this, "Task Added", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.setContentView(view)
        dialog.show()
    }

    private fun showEditTaskDialog(task: Task) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.dialogue_add_task, null)
        val titleInput = view.findViewById<EditText>(R.id.taskTitleInput)
        val descInput = view.findViewById<EditText>(R.id.taskDescInput)
        val saveBtn = view.findViewById<MaterialButton>(R.id.saveTaskBtn)

        titleInput.setText(task.title)
        descInput.setText(task.description)

        saveBtn.setOnClickListener {
            val title = titleInput.text.toString().trim()
            val desc = descInput.text.toString().trim()
            if (title.isNotEmpty()) {
                val updatedTask = Task(task.Id, title, desc)
                taskViewModel.updateTask(updatedTask)
                Toast.makeText(this, "Task Updated", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.setContentView(view)
        dialog.show()
    }

    private fun toggleTaskStatus(task: Task) {
        val colorRes = when (task.status) {
            "Done" -> R.color.green
            "Incomplete" -> R.color.red
            else -> R.color.yellow
        }
        val updatedTask = task.copy(colorRes)
        taskViewModel.updateTask(updatedTask)
    }
}
