package com.xyz.tkm
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import android.text.Editable
import android.text.TextWatcher
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xyz.tkm.R
import com.xyz.tkm.adapter.TaskAdapter
import com.xyz.tkm.model.Task
import com.xyz.tkm.viewmodel.TaskViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.toString

class HomeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TaskAdapter
    private lateinit var searchBox: EditText
    private lateinit var addTaskBtn: ImageView
    private val taskViewModel: TaskViewModel by viewModels()
    private var allTasks = mutableListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        recyclerView = findViewById(R.id.recyclerViewTasks)
        searchBox = findViewById(R.id.searchBox)
        addTaskBtn = findViewById(R.id.addTaskBtn)

        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = TaskAdapter(
            taskList = mutableListOf(),
            onItemClick = { task -> showAddTaskDialog() },
            onEditClick = { task -> showEditTaskDialog(task) },
            onDeleteClick = { task ->
                taskViewModel.delete(task)
                Toast.makeText(this, "Deleted: ${task.title}", Toast.LENGTH_SHORT).show()
            },
            onStatusChange = { updatedTask ->
                taskViewModel.insert(updatedTask) // ✅ Save status in DB
            }
        )

        recyclerView.adapter = adapter

        // Observe tasks from DB
        taskViewModel.allTasks.observe(this) { tasks ->
//            allTasks = tasks.toMutableList()
            adapter.submitList(tasks)
        }

        // Search functionality
        searchBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val filtered = allTasks.filter { it.title.contains(s.toString(), ignoreCase = true) }
                adapter.submitList(filtered)
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Add Task Button
        addTaskBtn.setOnClickListener { showAddTaskDialog() }
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
                taskViewModel.insert(Task(0, title, desc))
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
}