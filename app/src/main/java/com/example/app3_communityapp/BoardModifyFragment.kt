package com.example.app3_communityapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.app3_communityapp.databinding.FragmentBoardModifyBinding


class BoardModifyFragment : Fragment() {

    lateinit var boardModifyFragmentBinding : FragmentBoardModifyBinding

    val spinner_data = arrayOf("게시판1", "게시판2", "게시판3","게시판4")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        boardModifyFragmentBinding = FragmentBoardModifyBinding.inflate(inflater)

        boardModifyFragmentBinding.boardModifyToolbar.title = "글 수정"
        boardModifyFragmentBinding.boardModifyToolbar.inflateMenu(R.menu.board_modify_menu)
        boardModifyFragmentBinding.boardModifyToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.board_modify_menu_camera -> {
                    true
                }
                R.id.board_modify_menu_gallery -> {
                    true
                }
                R.id.board_modify_menu_upload -> {
                    val act = activity as BoardMainActivity
                    act.fragmentRemoveBackStack("board_modify")
                    true
                }

                else -> false
            }
        }

        val spinnerAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item
            ,spinner_data)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        boardModifyFragmentBinding.boardModifyType.adapter = spinnerAdapter


        return boardModifyFragmentBinding.root
    }


}