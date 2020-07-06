package com.lazzlepazzle.meetingremainder

import androidx.fragment.app.Fragment


class MainActivity : AbstractFragment() {

    override fun createFragment(): Fragment {
        return FragmentRemind()
    }
}
