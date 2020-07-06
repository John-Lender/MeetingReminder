package com.lazzlepazzle.meetingremainder

import androidx.fragment.app.Fragment

class NoteListActivity : AbstractFragment(){
    override fun createFragment(): Fragment {
        return NoteListFragment()
    }
}