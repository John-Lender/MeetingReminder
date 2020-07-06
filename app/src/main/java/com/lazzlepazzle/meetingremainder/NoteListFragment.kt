package com.lazzlepazzle.meetingremainder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lazzlepazzle.meetingremainder.Model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoteListFragment : Fragment(){
    private lateinit var mNoteRecyclerView: RecyclerView
    private lateinit var mAdapter: NoteAdapter
    private lateinit var mRestService: RestService
    private lateinit var mFloatingButton: FloatingActionButton
    private var data: List<Result>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View = inflater.inflate(R.layout.recycler_veiw_activity, container, false)
        Log.d("NoteListFragment", "onCreateView")
        mRestService =
            ServiceGenerator.createService(RestService::class.java)//Нужно вынести в DataManager, чтобы зранить ссылку в единственном экземпляре
        mNoteRecyclerView = v.findViewById(R.id.crime_recycler_view)
        val layoutManager: LinearLayoutManager = LinearLayoutManager(activity)
        mNoteRecyclerView.layoutManager = layoutManager
        val divider : DividerItemDecoration = DividerItemDecoration(mNoteRecyclerView.context, layoutManager.orientation)
        mNoteRecyclerView.addItemDecoration(divider)
        mFloatingButton = v.findViewById(R.id.floating_action_button)
        mFloatingButton.setOnClickListener{
            setDate(1, Intent())
        }
        getData()
        return v
    }

    private fun getNotes(key: String, ref: String, results: Int): Call<NoteModel> {
        return mRestService.getNotes(key, ref, results)
    }
    private fun getData(){
        val call: Call<NoteModel> = getNotes("L9XT-8D3O-QUBD-7RP7", "caxtzzsi", 10)
        call.enqueue(object: Callback<NoteModel>{
            override fun onResponse(call: Call<NoteModel>, response: Response<NoteModel>) {
                if(response.code() == 200){
                    data = response.body()?.results
                    updateUI()
                    Log.i("RESULT_VALUE2", response.body().toString())
                }else if (response.code() == 404){
                    Log.e("INTERNET","Not correct key or ref")
                }else{
                    Log.e("INTERNET","something wrong")
                }
            }
            override fun onFailure(call: Call<NoteModel>, t: Throwable) {
                //Обработать ошибки Retrofit
            }
        })
    }
    fun setDate(resultCode: Int,intent: Intent){
        activity!!.setResult(resultCode, intent)
        activity!!.finish()
    }
    fun setToastMsg(text: String){
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
    }
    private fun updateUI(){
        mAdapter = NoteAdapter(data)
        mNoteRecyclerView.adapter = mAdapter
    }

inner  class NoteHolder :  RecyclerView.ViewHolder {
        val mClientNameTextView: TextView = itemView.findViewById(R.id.note_nameClient_tv)
        val mEmailTextView: TextView = itemView.findViewById(R.id.note_email_tv)
        val mClientImageView: ImageView = itemView.findViewById(R.id.note_image_client)
        val mLinearLayout: ConstraintLayout = itemView.findViewById(R.id.constraint_layout)
        lateinit var imageDate: DownloadImageClient

        lateinit var mNote: Result

        constructor(inflater: LayoutInflater, parent: ViewGroup) : super(inflater.inflate(R.layout.fragment_note, parent, false))
        fun bind(note: Result){
            mNote = note
            mClientNameTextView.text = mNote.nameClient
            mEmailTextView.text = mNote.email
            imageDate = DownloadImageClient(mClientImageView)
            imageDate.execute("https://loremflickr.com/640/360")//mNote.photo - использован другой ресурс для загрузки фото, т.к loremPixel не грузит фото вообше
            mLinearLayout.setOnClickListener{
                if (imageDate.bitmapData == null){
                    setToastMsg("Image not uploaded!")
                }else{
                    val intent: Intent = Intent()
                    intent.putExtra("email", mNote.email)
                    intent.putExtra("fullName", mNote.nameClient)
                    intent.putExtra("image", ConvertImage.decodeImageToByteArray(imageDate.bitmapData!!))
                    setDate(-1,intent)
                }
            }
        }
    }


    inner class NoteAdapter(notes: List<Result>?) : RecyclerView.Adapter<NoteHolder>() {
        private var mNotes : List<Result>? = notes

        override fun onBindViewHolder(noteHolder: NoteHolder, i: Int) {
            val note: Result = mNotes!![i]
            noteHolder.bind(note)
        }
        override fun onCreateViewHolder(viewGroup: ViewGroup, k: Int): NoteHolder {
            val inflater : LayoutInflater = LayoutInflater.from(activity)
            return NoteHolder(inflater, viewGroup)
        }

        override fun getItemCount(): Int {
            return mNotes!!.size
        }
    }

}
