package com.lazzlepazzle.meetingremainder

import android.app.*
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.lazzlepazzle.meetingremainder.Model.Note
import com.lazzlepazzle.meetingremainder.Model.ReminderBroadcast
import com.lazzlepazzle.meetingremainder.Model.SaveData
import java.text.SimpleDateFormat
import java.util.*

class FragmentRemind : Fragment(){
    companion object{val CHANNEL = "com.lazzlepazzle.meetingremainder"}

    private val ERROR = "Required field"

    private val REQUEST_CLIENT: Int = 1

    private val KEY_TITLE: String = "title"
    private val KEY_DATE: String = "date"
    private val KEY_TIME: String = "time"
    private val KEY_FULL_NAME: String = "full_name"
    private val KEY_EMAIL: String = "email"
    private val KEY_PHOTO: String = "photo"


    private var mEmail: String? = null
    private var mFullName: String? = null
    private var mPhotoByteArray: ByteArray? = null
    private var mDate: Calendar? = Calendar.getInstance()
    private var mDateTime: Calendar? = Calendar.getInstance()

    private lateinit var mTitleEditText: TextInputEditText
    private lateinit var mDateEditText: TextInputEditText
    private lateinit var mTimeEditText: TextInputEditText
    private lateinit var mFullNameEditText: TextInputEditText
    private lateinit var mEmailEditText: TextInputEditText
    private lateinit var mDateSetListener: DatePickerDialog.OnDateSetListener
    private lateinit var mTimeSetListener: TimePickerDialog.OnTimeSetListener

    private lateinit var mTitleInputLayout: TextInputLayout
    private lateinit var mDateInputLayout: TextInputLayout
    private lateinit var mFullNameInputLayout: TextInputLayout
    private lateinit var mEmailInputLayout: TextInputLayout

    lateinit var mSaveButton: Button
    lateinit var mBackButton: Button


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View = inflater.inflate(R.layout.reminder_fragment, container, false)
        val cal: Calendar = Calendar.getInstance()

        createNotificationChannel()

        mTitleEditText = v.findViewById(R.id.title_reminder_et)
        mDateEditText = v.findViewById(R.id.date_reminder_et)
        mTimeEditText = v.findViewById(R.id.time_reminder_et)
        mFullNameEditText = v.findViewById(R.id.full_name_reminder_et)
        mEmailEditText = v.findViewById(R.id.email_reminder_et)

        mSaveButton = v.findViewById(R.id.save_button)
        mBackButton = v.findViewById(R.id.back_button)

        mTitleInputLayout = v.findViewById(R.id.title_reminder_il)
        mDateInputLayout = v.findViewById(R.id.date_reminder_il)
        mFullNameInputLayout = v.findViewById(R.id.full_name_reminder_il)
        mEmailInputLayout = v.findViewById(R.id.email_reminder_il)


        if (activity!!.intent.extras != null){
            mBackButton.visibility = View.INVISIBLE
        }
//        if (savedInstanceState != null){
//            mTitleEditText.setText(savedInstanceState.getString(KEY_TITLE))
//            mDateEditText.setText(savedInstanceState.getString(KEY_DATE))
//            mTimeEditText.setText(savedInstanceState.getString(KEY_TIME))
//
//            mPhotoByteArray = savedInstanceState.getByteArray(KEY_PHOTO)
//        }
        mTimeEditText.setOnClickListener{
            val timePicker = TimePickerDialog(v.context,
                mTimeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true)
            timePicker.show()
        }
        mFullNameEditText.setOnClickListener{
            val intent = Intent(v.context, NoteListActivity::class.java)
            startActivityForResult(intent, REQUEST_CLIENT)
        }
        mEmailEditText.setOnClickListener{
            val intent = Intent(v.context, NoteListActivity::class.java)
            startActivityForResult(intent, REQUEST_CLIENT)
        }
        mDateEditText.setOnClickListener {
            val datePickerDialog: DatePickerDialog = DatePickerDialog(v.context,
                android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth,
                mDateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.show()
        }
        mDateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy")
                mDate!!.set(year, month, dayOfMonth)
                mDateEditText.setText(simpleDateFormat.format(mDate!!.time))
                mDateInputLayout.error = null
            }

        mTimeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            val simpleDateFormat = SimpleDateFormat("HH:mm")
            mDateTime!!.set(0,0,0,hourOfDay,minute)
            mTimeEditText.setText(simpleDateFormat.format(mDateTime!!.time))
        }
        mSaveButton.setOnClickListener{
            var flag = true

            if (mTitleEditText.text!!.isEmpty()){
                mTitleInputLayout.error = ERROR
                flag = false
            }else mTitleInputLayout.error = null
            if(mDateEditText.text!!.isEmpty()){
                mDateInputLayout.error = ERROR
                flag = false
            }else mDateInputLayout.error = null
            if (mFullNameEditText.text!!.isEmpty()){
                mFullNameInputLayout.error = ERROR
                mEmailInputLayout.error = ERROR
                flag = false
            }else{
                mFullNameInputLayout.error = null
                mEmailInputLayout.error = null
            }
            if (flag){
                val note: Note = Note(mTitleEditText.text.toString(),
                    mDateEditText.text.toString(),
                    mTimeEditText.text.toString(),
                    mFullNameEditText.text.toString(),
                    mEmailEditText.text.toString(),
                    mPhotoByteArray!!)

                val size = SaveData.saveNote(activity!!, note)
                createAlarm(v.context, createDate(), size, mFullNameEditText.text.toString())
                sendData(note, activity!!.intent.extras!=null)
            }
        }
        mBackButton.setOnClickListener{
            activity!!.setResult(2)
            activity!!.finish()
        }
        return v
    }
    private fun createAlarm(context: Context, time: Long, id : Int, fullName: String){
        val intent = Intent(context!!, ReminderBroadcast::class.java)
        intent.putExtra("ID", id)
        intent.putExtra("FULL_NAME", fullName)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

        val alarmManager: AlarmManager = activity!!.getSystemService(ALARM_SERVICE) as AlarmManager

        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent)
    }

    private fun createNotificationChannel(){
        val notificationManager = activity!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL, "Meeting reminder",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "Will remind you of the meeting in an hour"
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(false)
            notificationManager!!.createNotificationChannel(channel)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK){
            mEmail= data!!.getStringExtra("email")
            mFullName= data.getStringExtra("fullName")
            mPhotoByteArray = data.getByteArrayExtra("image")
            mEmailEditText.setText(mEmail)
            mFullNameEditText.setText(mFullName)
            mEmailInputLayout.error = null
            mFullNameInputLayout.error = null
        }
    }
//    override fun onSaveInstanceState(outState: Bundle) {
//        outState.putString(KEY_TITLE, mTitleEditText.text.toString())
//        outState.putString(KEY_DATE, mDateEditText.text.toString())
//        outState.putString(KEY_TIME, mTimeEditText.text.toString())
//        outState.putString(KEY_EMAIL, mEmailEditText.text.toString())
//        outState.putString(KEY_FULL_NAME, mFullNameEditText.text.toString())
//        outState.putByteArray(KEY_PHOTO, mPhotoByteArray)
//    }
    private fun sendData(note : Note, boolean: Boolean){
        if (boolean){
            var intent = Intent(context, ListRemindsActivity::class.java)
            intent = putNoteInIntent(intent,note)
            startActivity(intent)
            activity!!.finish()
        }else{
            var intent = Intent()
            intent = putNoteInIntent(intent,note)
            activity!!.setResult(1, intent)
            activity!!.finish()
        }

    }
    private fun putNoteInIntent(intent: Intent, note : Note):Intent{
        intent.putExtra("TITLE", note.title)
        intent.putExtra("DATE", note.date)
        intent.putExtra("TIME", note.time)
        intent.putExtra("FULL_NAME", note.fullNameClient)
        intent.putExtra("EMAIL", note.email)
        intent.putExtra("PHOTO", note.photo)
        return intent
    }

    private fun createDate():Long{
        if (mTimeEditText.text!!.isEmpty()){
            mDateTime!!.set(0,0,0,0,0)
        }
        val simpleDateFormat = Calendar.getInstance()
        simpleDateFormat.set(mDate!!.get(Calendar.YEAR),
            mDate!!.get(Calendar.MONTH),
            mDate!!.get(Calendar.DAY_OF_MONTH),
            mDateTime!!.get(Calendar.HOUR_OF_DAY),
            mDateTime!!.get(Calendar.MINUTE))
        return System.currentTimeMillis() + (simpleDateFormat.timeInMillis-(System.currentTimeMillis())-60*60*1000)
    }


}