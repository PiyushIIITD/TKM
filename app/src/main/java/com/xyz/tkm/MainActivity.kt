package com.xyz.tkm

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.xyz.tkm.adapter.TaskAdapter
import com.xyz.tkm.model.Task
import com.xyz.tkm.viewmodel.TaskViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var addTaskBtn: ImageView
    private lateinit var taskAdapter: TaskAdapter
    private val taskViewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        recyclerView = findViewById(R.id.recyclerViewTasks)
        addTaskBtn = findViewById(R.id.addTaskBtn)

        // ✅ Initialize Task Adapter
        taskAdapter = TaskAdapter(
            taskList = mutableListOf(),
            onEditClick = { task -> showEditTaskDialog(task) },
            onDeleteClick = { task -> deleteTask(task) },
            onStatusChange = { update ->
                taskViewModel.insert(update) // ✅ Save status in DB
            },
            onItemClick = { task -> showTaskDetails(task) } // ✅ Show task details in dialog instead of new Activity
        )

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = taskAdapter
        }

        // ✅ Observe LiveData from ViewModel
        taskViewModel.allTasks.observe(this) { tasks ->
            taskAdapter.submitList(tasks) // ✅ Automatically refresh list
        }

        // ✅ Open add-task bottom sheet
        addTaskBtn.setOnClickListener {
            showAddTaskDialog()
        }
    }

    /**
     * ✅ Show Task Details in BottomSheet
     */
    private fun showTaskDetails(task: Task) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.dialogue_add_task, null)
        val titleInput = view.findViewById<EditText>(R.id.taskTitleInput)
        val descInput = view.findViewById<EditText>(R.id.taskDescInput)
        val saveBtn = view.findViewById<MaterialButton>(R.id.saveTaskBtn)

        titleInput.setText(task.title)
        descInput.setText(task.description)
        saveBtn.isEnabled = false // ❌ Disable save in details mode

        dialog.setContentView(view)
        dialog.show()
    }

    /**
     * ✅ Show Add Task BottomSheet Dialog
     */
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
                taskViewModel.insert(task)
                Toast.makeText(this, "Task Added", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.setContentView(view)
        dialog.show()
    }

    /**
     * ✅ Show Edit Task BottomSheet Dialog
     */
    private fun showEditTaskDialog(task: Task) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.dialogue_add_task, null)
        val titleInput = view.findViewById<EditText>(R.id.taskTitleInput)
        val descInput = view.findViewById<EditText>(R.id.taskDescInput)
        val saveBtn = view.findViewById<MaterialButton>(R.id.saveTaskBtn)

        // Pre-fill existing data
        titleInput.setText(task.title)
        descInput.setText(task.description)

        saveBtn.setOnClickListener {
            val title = titleInput.text.toString().trim()
            val desc = descInput.text.toString().trim()
            if (title.isNotEmpty()) {
                val updatedTask = Task(task.id, title, desc)
                taskViewModel.insert(updatedTask)
                Toast.makeText(this, "Task Updated", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.setContentView(view)
        dialog.show()
    }

    /**
     * ✅ Delete Task
     */
    private fun deleteTask(task: Task) {
        taskViewModel.delete(task)
        Toast.makeText(this, "Task Deleted", Toast.LENGTH_SHORT).show()
    }
}
