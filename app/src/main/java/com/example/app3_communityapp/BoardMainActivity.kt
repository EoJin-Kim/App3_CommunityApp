package com.example.app3_communityapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.app3_communityapp.databinding.ActivityBoardMainBinding

class BoardMainActivity : AppCompatActivity() {

    lateinit var boardMainActivityBinding : ActivityBoardMainBinding
    lateinit var currentFragment : Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        boardMainActivityBinding = ActivityBoardMainBinding.inflate(layoutInflater)
        setContentView(boardMainActivityBinding.root)

        fragmentController("board_main",false,false)
    }

    fun fragmentController(name: String, add: Boolean, animate: Boolean) {
        when (name) {
            "board_main" ->{
                currentFragment = BoardMainFragment()
            }
        }

        val trans = supportFragmentManager.beginTransaction()
        trans.replace(R.id.board_main_container,currentFragment)
        if (add == true) {
            trans.addToBackStack(name)
        }

        if (animate == true) {
            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        }

        trans.commit()
    }

}