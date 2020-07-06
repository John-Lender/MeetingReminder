package com.lazzlepazzle.meetingremainder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lazzlepazzle.meetingremainder.Model.ConvertImage
import com.lazzlepazzle.meetingremainder.Model.Note
import com.lazzlepazzle.meetingremainder.Model.NoteArray
import com.lazzlepazzle.meetingremainder.Model.SaveData

class ListRemindsFragment : Fragment(){
    val REQUEST_NEW_REMIND: Int = 1
    private lateinit var mRemindRecyclerView: RecyclerView
    private  var mAdapter: RemindAdapter? = null
    private lateinit var mCreateActionButton: FloatingActionButton

    private  var mData: NoteArray? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View = inflater.inflate(R.layout.list_reminds_fragment, container, false)
        mRemindRecyclerView = v.findViewById(R.id.list_reminders_rv)
        val layoutManager: LinearLayoutManager = LinearLayoutManager(activity)
        mRemindRecyclerView.layoutManager = layoutManager
        mCreateActionButton = v.findViewById(R.id.create_action_button)
        getData()
        val divider : DividerItemDecoration = DividerItemDecoration(mRemindRecyclerView.context, layoutManager.orientation)
        mRemindRecyclerView.addItemDecoration(divider)
        mCreateActionButton.setOnClickListener{
            val intent = Intent(v.context, MainActivity::class.java)
            startActivityForResult(intent, REQUEST_NEW_REMIND)
        }
        return v
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.i("LIST_REMINDS", "requestCode: $requestCode,resultCode: $resultCode")
        if (resultCode == REQUEST_NEW_REMIND) {
            val note: Note = Note(
                data!!.getStringExtra("TITLE"),
                data.getStringExtra("DATE"),
                data.getStringExtra("TIME"),
                data.getStringExtra("FULL_NAME"),
                data.getStringExtra("EMAIL"),
                data.getByteArrayExtra("PHOTO")
            )
            mData!!.items.add(0, note)
            mRemindRecyclerView.adapter!!.notifyItemInserted(0)
        }
    }
    private fun getData(){
        mData = SaveData.openNote(activity!!)
        Log.i("LIST_REMINDS", (mData==null).toString())
        if (mData == null){
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("FLAG", true)
            startActivity(intent)
            //Отработать случай если данный вернулись пустыми
        }else{
            updateUI()
        }
    }

    private fun updateUI(){
        Log.i("LIST_REMINDS", "update")
        if (mAdapter == null){
            mAdapter = RemindAdapter(mData!!)
            mRemindRecyclerView.adapter = mAdapter
        }else{
            mAdapter!!.notifyDataSetChanged()
        }
    }
    class RemindHolder : RecyclerView.ViewHolder {
        val mTitleTextView: TextView = itemView.findViewById(R.id.title_item_remind_tv)
        val mDateTextView: TextView = itemView.findViewById(R.id.date_item_remind_tv)
        val mTimeTextView: TextView = itemView.findViewById(R.id.time_item_remind_tv)
        val mFullNameTextView: TextView = itemView.findViewById(R.id.full_name_item_remind_tv)
        val mEmailTextView: TextView = itemView.findViewById(R.id.email_item_remind_tv)
        val mPhotoImageView: ImageView = itemView.findViewById(R.id.photo_item_remind_iv)
        lateinit var mNote: Note

        constructor(inflater: LayoutInflater, parent: ViewGroup) : super(inflater.inflate(R.layout.item_list_reminds_fragment, parent, false))

        fun bind(note: Note){
            mNote = note
            mTitleTextView.text = note.title
            mDateTextView.text = note.date
            mTimeTextView.text = note.time ?: ""
            mFullNameTextView.text = note.fullNameClient
            mEmailTextView.text = note.email
            mPhotoImageView.setImageBitmap(ConvertImage.ByteArrayToBitmap(note.photo))
        }

    }
    inner class RemindAdapter(notes: NoteArray) : RecyclerView.Adapter<ListRemindsFragment.RemindHolder>() {
        private val mNotes: NoteArray = notes
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RemindHolder {
            var inflater: LayoutInflater = LayoutInflater.from(activity)
            return RemindHolder(inflater, parent)
        }

        override fun getItemCount(): Int {
            return mNotes.items.size
        }

        override fun onBindViewHolder(holder: RemindHolder, position: Int) {
            val mNote: Note = mNotes.items[position]
            holder.bind(mNote)
        }

    }

}