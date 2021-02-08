package com.example.loginapp.screens.home
import android.app.Application
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
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
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var db : LoginDatabase

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        db= getDatabaseInstance(application)
        return when (viewType) {
            0 -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.user_record,
                        parent, false)
                ToDoItemViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.user_commercial_layout,
                        parent, false)
                ToDoItemViewHolder2(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val user=userList[position]
        when(holder.itemViewType){
            0->{
                val holder1=ToDoItemViewHolder(holder.itemView)
                holder1.customerType.setBackgroundColor(Color.GRAY)
                holder1.firstName.text=user.firstName
                holder1.lname.text=user.lastName
                holder1.email.text=user.email
                holder1.phone.text=user.phone
                holder1.customerType.text=user.customerType
                holder1.deleteUserRecord.setOnClickListener {
                    removeItem(holder.adapterPosition)
                }
            }
            1->{
                val holder2=ToDoItemViewHolder2(holder.itemView)
                holder2.customerTypeCommercial.setBackgroundColor(Color.parseColor("#1E90FF"))
                holder2.bName.text=user.businessName
                holder2.cinNumber.text=user.cin
                holder2.emailCommercial.text=user.email
                holder2.phoneCommercial.text=user.phone
                holder2.deleteUserRecordCommercial.setOnClickListener {
                    removeItem(holder.adapterPosition)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(userList[position].customerType){
            application.getString(R.string.customerType_residential) -> 0
            application.getString(R.string.customerType_commercial) -> 1
            else -> -1
        }
    }


    override fun getItemCount(): Int {
        return userList.size
    }

    inner class ToDoItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val firstName: TextView = itemView.findViewById(R.id.fName_residential)
        val lname: TextView =itemView.findViewById(R.id.lname_residential)
        val email: TextView =itemView.findViewById(R.id.e_mail_residential)
        val phone: TextView =itemView.findViewById(R.id.mobile_residential)
        val customerType:MaterialButton=itemView.findViewById(R.id.customerTypeLabel_residential)
        val deleteUserRecord:MaterialButton=itemView.findViewById(R.id.deleteRecord_residential)
    }

    inner class ToDoItemViewHolder2(itemView: View): RecyclerView.ViewHolder(itemView) {
        val bName:TextView= itemView.findViewById(R.id.businessName_commercial)
        val cinNumber:TextView=itemView.findViewById(R.id.cin_commercial)
        val emailCommercial: TextView =itemView.findViewById(R.id.e_mail_commercial)
        val phoneCommercial: TextView =itemView.findViewById(R.id.mobile_commercial)
        val customerTypeCommercial:MaterialButton=itemView.findViewById(R.id.customerTypeLabel_commercial)
        val deleteUserRecordCommercial:MaterialButton=itemView.findViewById(R.id.deleteRecord_commercial)
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