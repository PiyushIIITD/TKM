package com.xyz.tkm.adapter
import android.R.attr.description
import com.xyz.tkm.viewmodel.TaskViewModel
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.xyz.tkm.R
import com.xyz.tkm.model.Task

class TaskAdapter(
    private val onEditClick: (Task) -> Unit,
    private var taskList: MutableList<Task>,
    private val onDeleteClick: (Task) -> Unit,
    private val onItemClick: (Task) -> Unit,
    private val onStatusChange: (Task) -> Unit

    ) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {


    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskImage: ImageView = itemView.findViewById(R.id.taskImage)
        val taskTitle: TextView = itemView.findViewById(R.id.taskTitle)
        val taskDescription: TextView = itemView.findViewById(R.id.taskDescription)
        val editTask: ImageView = itemView.findViewById(R.id.editTask)
        val deleteTask: ImageView = itemView.findViewById(R.id.deleteTask)
        val statusBtn: ImageView = itemView.findViewById(R.id.taskStatusIcon)

        fun bind(task: Task) {
            taskTitle.text = task.title
            taskDescription.text = task.description
            itemView.setOnClickListener {
                onEditClick(task)
            }
            itemView.setOnClickListener {
                onItemClick(task)
            }
            deleteTask.setOnClickListener {
                onDeleteClick(task)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task_template, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount() = taskList.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.taskTitle.text = task.title
        holder.taskDescription.text = task.description
        val colorRes = when (task.status) {
            "Done" -> R.color.green
            "Incomplete" -> R.color.red
            else -> R.color.yellow
        }
        holder.statusBtn.setColorFilter(ContextCompat.getColor(holder.itemView.context, colorRes))

        // ✅ Edit task
        holder.editTask.setOnClickListener {
            onEditClick(task)
        }

        // ✅ Delete task
        holder.deleteTask.setOnClickListener {
            onDeleteClick(task)
        }

        // ✅ View task details or status
        holder.statusBtn.setOnClickListener { view->
        showStatusMenu(view , task)
        }
    }

    private fun showStatusMenu(view: View, task: Task) {
        val popup = PopupMenu(view.context, view)
        popup.menuInflater.inflate(R.menu.menu_task, popup.menu)

        popup.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.status_done -> task.status = "Done"
                R.id.status_incomplete -> task.status = "Incomplete"
                R.id.status_pending -> task.status = "Pending"
            }
            onStatusChange(task)
            notifyDataSetChanged()
            true
        }
        popup.show()
    }

    fun submitList(tasks: List<Task>) {
        taskList = tasks.toMutableList()
//        taskList.addAll(tasks)
        notifyDataSetChanged()
    }
}
