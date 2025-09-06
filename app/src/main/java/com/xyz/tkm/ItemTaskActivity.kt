package com.xyz.tkm
import com.xyz.tkm.HomeActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.xyz.tkm.adapter.TaskAdapter
import com.xyz.tkm.data.TaskDatabase
import com.xyz.tkm.model.Task
import com.xyz.tkm.viewmodel.TaskViewModel
import com.xyz.tkm.data.TaskDao
import kotlin.jvm.java

class ItemTaskActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private val taskViewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_task_template)

        //recyclerView = findViewById(R.id.recyclerViewTasks)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val taskDao = TaskDatabase.getDatabase(this).taskDao()
        val tasks = taskDao.getAllTasks()

        taskAdapter= TaskAdapter(
            taskList = mutableListOf(),
                 onItemClick = { task ->
                Toast.makeText(this, "Clicked: ${task.title}", Toast.LENGTH_SHORT).show()
            },
            onEditClick = { task -> showEditTaskDialog(task) },
            onDeleteClick = { task ->
                taskViewModel.delete(task)
                Toast.makeText(this, "Deleted: ${task.title}", Toast.LENGTH_SHORT).show()
            },
            onStatusChange = { updatedTask ->
                taskViewModel.updateTask(updatedTask)
            }
        )
            recyclerView.adapter = taskAdapter

        taskViewModel.allTasks.observe(this) { tasks ->
            taskAdapter.submitList(tasks)
        }
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
                taskViewModel.addTask(updatedTask)
                Toast.makeText(this, "Task Updated", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.setContentView(view)
        dialog.show()
    }
}
