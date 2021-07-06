package com.example.loginapp.screens.home

import android.app.Application
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.loginapp.*
import com.example.loginapp.database.LoginDatabase
import com.example.loginapp.database.LoginEntity
import com.example.loginapp.screens.chat.ChatWindowActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecyclerViewAdaptor(
    private val userList: MutableList<LoginEntity>,
    private val application: Application,
    private val appCompatActivity: AppCompatActivity
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var db: LoginDatabase

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        db = getDatabaseInstance(application)
        return when (viewType) {
            0 -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.user_residential_layout,
                    parent, false
                )
                ToDoItemViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.user_commercial_layout,
                    parent, false
                )
                ToDoItemViewHolder2(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val user = userList[position]
        when (holder.itemViewType) {
            0 -> {
                val holder1 = ToDoItemViewHolder(holder.itemView)
                holder1.customerType.setBackgroundColor(Color.GRAY)
                holder1.firstName.text = user.firstName
                holder1.lname.text = user.lastName
                holder1.email.text = user.email
                holder1.phone.text = user.phone
                holder1.customerType.text = user.customerType
                holder1.deleteUserRecord.setOnClickListener {
                    removeItem(holder.adapterPosition)
                }

                holder1.viewChatResidential.setOnClickListener {
                    val intent =
                        Intent(application.applicationContext, ChatWindowActivity()::class.java)
                    val bundle = Bundle()
                    bundle.putString(
                        application.getString(R.string.personName),
                        user.firstName + user.lastName
                    )
                    bundle.putString(application.getString(R.string.personEmail), user.email)
                    intent.putExtra(application.getString(R.string.bundleKey_Chat), bundle)
                    holder1.itemView.context.startActivity(intent)
                }
            }
            1 -> {
                val holder2 = ToDoItemViewHolder2(holder.itemView)
                holder2.customerTypeCommercial.setBackgroundColor(
                    Color.parseColor(
                        application.getString(
                            R.string.windowsBlue
                        )
                    )
                )
                holder2.bName.text = user.businessName
                holder2.cinNumber.text = user.cin
                holder2.emailCommercial.text = user.email
                holder2.phoneCommercial.text = user.phone
                holder2.deleteUserRecordCommercial.setOnClickListener {
                    removeItem(holder.adapterPosition)
                }

                holder2.viewChatCommercial.setOnClickListener {
                    val intent =
                        Intent(application.applicationContext, ChatWindowActivity()::class.java)
                    val bundle = Bundle()
                    bundle.putString(application.getString(R.string.personName), user.businessName)
                    bundle.putString(application.getString(R.string.personEmail), user.email)
                    intent.putExtra(application.getString(R.string.bundleKey_Chat), bundle)
                    holder2.itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (userList[position].customerType) {
            application.getString(R.string.customerType_residential) -> 0
            application.getString(R.string.customerType_commercial) -> 1
            else -> -1
        }
    }


    override fun getItemCount(): Int {
        return userList.size
    }

    inner class ToDoItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val firstName: TextView = itemView.findViewById(R.id.fName_residential)
        val lname: TextView = itemView.findViewById(R.id.lname_residential)
        val email: TextView = itemView.findViewById(R.id.e_mail_residential)
        val phone: TextView = itemView.findViewById(R.id.mobile_residential)
        val customerType: MaterialButton = itemView.findViewById(R.id.customerTypeLabel_residential)
        val deleteUserRecord: MaterialButton = itemView.findViewById(R.id.deleteRecord_residential)
        val viewChatResidential: MaterialButton = itemView.findViewById(R.id.residential_Chat)


    }

    inner class ToDoItemViewHolder2(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bName: TextView = itemView.findViewById(R.id.businessName_commercial)
        val cinNumber: TextView = itemView.findViewById(R.id.cin_commercial)
        val emailCommercial: TextView = itemView.findViewById(R.id.e_mail_commercial)
        val phoneCommercial: TextView = itemView.findViewById(R.id.mobile_commercial)
        val customerTypeCommercial: MaterialButton =
            itemView.findViewById(R.id.customerTypeLabel_commercial)
        val deleteUserRecordCommercial: MaterialButton =
            itemView.findViewById(R.id.deleteRecord_commercial)
        val viewChatCommercial: MaterialButton = itemView.findViewById(R.id.commercial_Chat)
    }

    private fun removeItem(position: Int) {
        appCompatActivity.showDialog(
            { _, _ ->
                uiScope.launch {
                    deleteFromDB(userList[position].userId)
                    userList.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, userList.size)
                }
            },
            { dialog, _ -> dialog.cancel() },
            R.string.app_name,
            R.string.deleteRecordMessage,
            R.string.dialogYes,
            R.string.dialogNo
        )?.show()
    }

    private suspend fun deleteFromDB(id: Long) {
        withContext(Dispatchers.IO) {
            db.loginDatabaseDao.deleteById(id)
        }
    }

}