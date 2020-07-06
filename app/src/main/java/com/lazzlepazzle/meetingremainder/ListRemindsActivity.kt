package com.lazzlepazzle.meetingremainder

import android.content.Intent
import androidx.fragment.app.Fragment





class ListRemindsActivity : AbstractFragment(){
    override fun createFragment(): Fragment {
        return ListRemindsFragment()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}