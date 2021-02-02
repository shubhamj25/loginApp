package com.example.loginapp.screens.home
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.loginapp.R
import com.example.loginapp.database.LoginEntity
import com.google.android.material.button.MaterialButton

class RecyclerViewAdaptor(private val userList: List<LoginEntity>):
    RecyclerView.Adapter<RecyclerViewAdaptor.ToDoItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoItemViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.user_record,
            parent, false)
        return ToDoItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ToDoItemViewHolder, position: Int) {
        val user=userList[position]
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
    }
}