package com.example.loginapp.screens.chat

import android.annotation.SuppressLint
import android.app.Application
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.loginapp.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_chat_window.*
import kotlinx.android.synthetic.main.chat_item.view.*
import kotlinx.android.synthetic.main.chat_item_self.view.*


@Suppress("DEPRECATION")
class RecyclerViewAdaptor(
    private val messageList: ArrayList<ChatItem>,
    private val application: Application,
    private val sharedPreferences: SharedPreferences
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.chat_item, parent, false)
                ChatItemViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.chat_item_self, parent, false)
                ChatItemViewHolder2(view)
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = messageList[position]
        when (holder.itemViewType) {
            0 -> {
                val holder1 = ChatItemViewHolder(holder.itemView)
                holder1.chatMessage.text = item.message.toString()
                holder1.chatMessageAuthor.text =
                    item.author + "@" + item.postedOn.date + "-" + item.postedOn.month + "-" + (item.postedOn.year+1900).toString()
                holder1.itemView.deleteMessage.visibility=View.GONE
                //holder1.itemView.deleteMessage.setColorFilter(Color.BLACK)
//                holder1.itemView.deleteMessage.setOnClickListener {
//                    removeItem(holder.adapterPosition)
//                }
            }
            1 -> {
                val holder2 = ChatItemViewHolder2(holder.itemView)
                holder2.chatMessageSelf.text = item.message.toString()
                holder2.chatMessageAuthorSelf.text =
                    item.author + "@" + item.postedOn.date + "-" + item.postedOn.month + "-" + (item.postedOn.year+1900).toString()
                holder2.itemView.deleteMessage_self.setColorFilter(Color.BLACK)
                holder.itemView.deleteMessage_self.setOnClickListener {
                    removeItem(holder.adapterPosition)
                    deleteMsg(item.docId!!,item.messageId!!)
                }

            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val loggedInUser = sharedPreferences.getString(
            application.getString(R.string.sh_email),
            application.getString(R.string.sh_def_email)
        )
        return when (messageList[position].author) {
            loggedInUser -> 1
            else -> 0
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    inner class ChatItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chatMessage: TextView = itemView.findViewById(R.id.chatItem)
        val chatMessageAuthor: TextView = itemView.findViewById(R.id.chatItemAuthor)
    }

    inner class ChatItemViewHolder2(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chatMessageSelf: TextView = itemView.findViewById(R.id.chatItem_self)
        val chatMessageAuthorSelf: TextView = itemView.findViewById(R.id.chatItemAuthor_self)
    }

    private fun removeItem(position: Int) {
        messageList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, messageList.size)
    }

    private fun deleteMsg(docName: String,messageId:String){
        FirebaseFirestore.getInstance().collection("chats").document(docName)
            .collection("chats").document(messageId).delete().addOnSuccessListener {
                Log.i("QWERTY", "Message with id $messageId Deleted from$docName")
            }
    }

}