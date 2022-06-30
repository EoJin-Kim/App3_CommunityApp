package com.example.app3_communityapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.app3_communityapp.databinding.FragmentBoardMainBinding
import com.example.app3_communityapp.databinding.FragmentBoardWriteBinding


class BoardWriteFragment : Fragment() {


    lateinit var boardWriteFragmentBinding  : FragmentBoardWriteBinding

    var spinner_data = arrayOf("게시판1","게시판2","게시판3","게시판4")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        boardWriteFragmentBinding = FragmentBoardWriteBinding.inflate(inflater)
        boardWriteFragmentBinding.boardWriteToolbar.title = "게시글 작성"
        boardWriteFragmentBinding.boardWriteToolbar.inflateMenu(R.menu.board_write_menu)
        boardWriteFragmentBinding.boardWriteToolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.board_write_menu_camera ->{
                    true
                }
                R.id.board_write_menu_gallery ->{
                    true
                }
                R.id.board_write_menu_upload ->{
                    val act = activity as BoardMainActivity
                    act.fragmentRemoveBackStack("board_write")
                    act.fragmentController("board_read",true,true)
                    true
                }
                else -> false

            }
        }

        val spinnerAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,
                spinner_data)

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        boardWriteFragmentBinding.boardWriteType.adapter = spinnerAdapter


        return boardWriteFragmentBinding.root
    }

}