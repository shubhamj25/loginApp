package com.example.loginapp.screens.home
import android.app.Application
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.loginapp.R
import com.example.loginapp.database.LoginDatabase
import com.example.loginapp.database.LoginEntity
import com.example.loginapp.getDatabaseInstance
import com.example.loginapp.uiScope
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.*

class RecyclerViewAdaptor(private val userList: MutableList<LoginEntity>,private val application: Application):
    RecyclerView.Adapter<RecyclerViewAdaptor.ToDoItemViewHolder>() {
    lateinit var db : LoginDatabase
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoItemViewHolder {
        db= getDatabaseInstance(application)
        val view=LayoutInflater.from(parent.context).inflate(R.layout.user_record,
            parent, false)
        return ToDoItemViewHolder(view)
    }
    override fun onBindViewHolder(holder: ToDoItemViewHolder, position: Int) {
        val user=userList[position]
        setRecyclerItemView(user,holder)
    }

    private fun setRecyclerItemView(user: LoginEntity, holder: ToDoItemViewHolder) {
        if(user.firstName.isEmpty()){
            holder.firstName.height=0
        }
        if(user.lastName.isEmpty()){
            holder.lname.height=0
        }
        if(user.businessName.isEmpty()){
            holder.businessName.height=0
        }
        if(user.cin.isEmpty()){
            holder.cin.height=0
        }
        if(user.customerType == "Commercial"){
            holder.customerType.setBackgroundColor(Color.parseColor("#1E90FF"))
        }
        else{
            holder.customerType.setBackgroundColor(Color.GRAY)
        }
        holder.businessName.text=user.businessName
        holder.cin.text=user.cin
        holder.firstName.text=user.firstName
        holder.lname.text=user.lastName
        holder.email.text=user.email
        holder.phone.text=user.phone
        holder.customerType.text=user.customerType
        holder.deleteUserRecord.findViewById<MaterialButton>(R.id.deleteRecord).setOnClickListener {
            removeItem(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class ToDoItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val businessName:TextView= itemView.findViewById(R.id.businessName_commercial)
        val cin:TextView=itemView.findViewById(R.id.cin_commercial)
        val firstName: TextView = itemView.findViewById(R.id.fName)
        val lname: TextView =itemView.findViewById(R.id.lname)
        val email: TextView =itemView.findViewById(R.id.e_mail)
        val phone: TextView =itemView.findViewById(R.id.mobile)
        val customerType:MaterialButton=itemView.findViewById(R.id.customerTypeLabel)
        val deleteUserRecord:MaterialButton=itemView.findViewById(R.id.deleteRecord)
    }

    private fun removeItem(position: Int) {
        uiScope.launch {
            deleteFromDB(userList[position].userId)
            userList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, userList.size)
        }
    }

    private suspend fun deleteFromDB(id: Long){
        withContext(Dispatchers.IO){
            db.loginDatabaseDao.deleteById(id)
        }
    }
}