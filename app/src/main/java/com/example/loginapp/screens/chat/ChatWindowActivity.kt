package com.example.loginapp.screens.chat

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.loginapp.BaseActivity
import com.example.loginapp.R
import com.example.loginapp.getDailyQuote
import com.example.loginapp.showSnackBarOnTop
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_chat_window.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class ChatWindowActivity : BaseActivity() {
    private var messages: ArrayList<ChatItem>? = null
    private lateinit var loggedInAs: String
    private lateinit var chatWith: String
    private lateinit var chatWithEmail: String

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_window)
        initLateInitVarsFromBundle()
        //some layout initializations
        initUi()
        //listeners and Recycler View init
        setListeners()
        initializeRecyclerView()
        setObservers()
        updateRecyclerView()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initUi() {
        title = chatWith
        if (messages == null) {
            loadingMessage.visibility == View.VISIBLE
        }
        uiScope.launch {
            quote.text= getDailyQuote()
        }
        val outValue = TypedValue()
        theme.resolveAttribute(R.attr.themeName, outValue, true)
        if ("dark" != outValue.string) {
            chatWindowConstraintLayout.background =
                resources.getDrawable(R.drawable.chat_light_background)
            typedMessage.setBackgroundColor(resources.getColor(R.color.whiteSmoke))
            quote.setTextColor(Color.BLACK)
            quote.animate().alpha(1f).setDuration(1000).start()
        } else {
            chatWindowConstraintLayout.background =
                resources.getDrawable(R.drawable.chat_dark_background)
            quote.setTextColor(getColor(R.color.whiteSmoke))
            quote.animate().alpha(1f).setDuration(1000).start()
        }
        window.navigationBarColor = Color.BLACK
    }

    private fun initLateInitVarsFromBundle() {
        loggedInAs = getLoggedInUserEmail(sharedPreferences)
        chatWith = intent.extras?.getBundle(getString(R.string.bundleKey_Chat))
            ?.getString(getString(R.string.personName), getString(R.string.defaultUserName))
            .toString()
        chatWithEmail = intent.extras?.getBundle(getString(R.string.bundleKey_Chat))
            ?.getString(getString(R.string.personEmail), getString(R.string.defaultUserName))
            .toString()
    }


    private fun setObservers() {
        typedMessage.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length > 30) {
                    send.animate().alpha(0f).setDuration(500).start()
                    sendLongMessage.visibility = View.VISIBLE
                    sendLongMessage.animate().alpha(1f).setDuration(500).start()
                } else {
                    send.animate().alpha(1f).setDuration(500).start()
                    //sendLongMessage.animate().alpha(0f).setDuration(500).start()
                    sendLongMessage.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing
            }
        })
    }

    private fun initializeRecyclerView() {
        messages = arrayListOf()
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        layoutManager.stackFromEnd = true
        recyclerView.setHasFixedSize(true);
        recyclerView.isNestedScrollingEnabled = true
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = RecyclerViewAdaptor(messages!!, application, sharedPreferences)
    }

    private fun updateRecyclerView() {
        messages = getAllChatsOfUser(chatWithEmail, loggedInAs)
        (recyclerView.adapter as RecyclerViewAdaptor).notifyDataSetChanged()
        if ((recyclerView.adapter as RecyclerViewAdaptor).itemCount >= 1) {
            recyclerView.smoothScrollToPosition((recyclerView.adapter as RecyclerViewAdaptor).itemCount)
        }
    }

    private fun getLoggedInUserEmail(sharedPreferences: SharedPreferences): String {
        return sharedPreferences.getString(
            getString(R.string.sh_email),
            getString(R.string.sh_def_email)
        ).toString()
    }

    private fun setListeners() {
        sendLongMessage.setOnClickListener { sendProcess() }
        send.setOnClickListener { sendProcess() }
    }

    private fun sendProcess() {
        loadingMessage.visibility = View.VISIBLE
        uiScope.launch {
            withContext(Dispatchers.IO) {
                sendMessage(
                    ChatItem(
                        "sampleId",
                        "sampleDocId",
                        typedMessage.text.toString(),
                        loggedInAs,
                        Calendar.getInstance().time
                    ), loggedInAs, chatWithEmail
                )
            }
        }.invokeOnCompletion {
            typedMessage.text?.clear()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.chat_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.close -> {
                this.finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun sendMessage(
        chatItem: ChatItem,
        currentLoggedInUser: String,
        chattingWithEmail: String
    ) {
        val chat = HashMap<String, Any>()
        chat["message"] = chatItem.message.toString()
        chat["author"] = chatItem.author.toString()
        chat["postedOn"] = chatItem.postedOn
        chat["readByAuthor"] = true
        chat["readByRecipient"] = false
        var chatDocumentName =
            (currentLoggedInUser.hashCode() + chattingWithEmail.hashCode()).toString()
        sendChatToFirebase(chatDocumentName, chat)
    }

    private fun sendChatToFirebase(chatDocumentName: String, chat: HashMap<String, Any>) {
        FirebaseFirestore.getInstance().collection("chats")
            .document(chatDocumentName).collection("chats").add(chat)
            .addOnCompleteListener {
                FirebaseFirestore.getInstance().collection("chats")
                    .document(chatDocumentName).collection("chats").document(it.result.id).update(
                        mapOf("messageId" to it.result.id,"docId" to chatDocumentName)
                    ).addOnCompleteListener {
                        updateRecyclerView()
                        loadingMessage.visibility = View.INVISIBLE
                    }
            }.addOnFailureListener {
                showSnackBarOnTop(
                    chatWindowConstraintLayout,
                    this,
                    R.string.messageNotDeliverd,
                    R.color.deepRed
                )
                loadingMessage.visibility = View.INVISIBLE
            }
    }

    private fun getAllChatsOfUser(
        currentLoggedInUser: String,
        chattingWithEmail: String
    ): ArrayList<ChatItem>? {
        var fetchChatFromDocument =
            (currentLoggedInUser.hashCode() + chattingWithEmail.hashCode()).toString()
        return getAllChatsFromFirebase(fetchChatFromDocument)
    }

    private fun getAllChatsFromFirebase(fetchChatFromDocument: String): ArrayList<ChatItem> {
        val chatMessages = arrayListOf<ChatItem>()
        FirebaseFirestore.getInstance().collection("chats")
            .document(fetchChatFromDocument).collection("chats").orderBy("postedOn")
            .get()
            .addOnCompleteListener { it1 ->
                for (doc in it1.result.documents) {
                    chatMessages.add(
                        ChatItem(
                            doc["messageId"].toString(),
                            doc["docId"].toString(),
                            doc["message"].toString(),
                            doc["author"].toString(),
                            (doc["postedOn"] as Timestamp).toDate()
                        )
                    )
                }
                recyclerView.adapter = RecyclerViewAdaptor(
                    chatMessages,
                    application,
                    sharedPreferences
                )
                loadingMessage.visibility = View.GONE
                recyclerView.smoothScrollToPosition(chatMessages.size)
                if(chatMessages.size>0){
                    quote.animate().alpha(0f).start()
                }
            }
        return chatMessages
    }

    private fun markChatsAsRead(user: String, chattingWith: String) {
        val docName = (user.hashCode() + chattingWith.hashCode()).toString()
        FirebaseFirestore.getInstance().collection("chats").document(docName)
            .collection("chats").addSnapshotListener { value, _ ->
                for (i in value?.documents!!) {
                    i.reference.update(mapOf("readByRecipient" to true))
                }
            }
    }
}