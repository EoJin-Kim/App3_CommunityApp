package com.example.app3_communityapp

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app3_communityapp.databinding.FragmentBoardReadBinding


class BoardReadFragment : Fragment() {


    lateinit var boardReadFragmentBinding : FragmentBoardReadBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        boardReadFragmentBinding = FragmentBoardReadBinding.inflate(inflater)
        boardReadFragmentBinding.boardReadToolbar.title = "게시글읽기"

        //navbar 백 버튼 활성화
        val navIcon = requireContext().getDrawable(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        boardReadFragmentBinding.boardReadToolbar.navigationIcon = navIcon

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            boardReadFragmentBinding.boardReadToolbar.navigationIcon?.colorFilter = BlendModeColorFilter(
                Color.parseColor("#FFFFFF"),BlendMode.SRC_ATOP)
        }else{
            boardReadFragmentBinding.boardReadToolbar.navigationIcon?.setColorFilter(
                Color.parseColor("#FFFFFF"),PorterDuff.Mode.SRC_ATOP
            )
        }


        // 백 버튼 클릭스 뒤로가기기
       boardReadFragmentBinding.boardReadToolbar.setNavigationOnClickListener {
            val act = activity as BoardMainActivity
            act.fragmentRemoveBackStack("board_read")
        }

        boardReadFragmentBinding.boardReadToolbar.inflateMenu(R.menu.board_read_menu)

        boardReadFragmentBinding.boardReadToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.board_read_menu_modify -> {
                    val act = activity as BoardMainActivity
                    act.fragmentController("board_modify", true,true)
                    true
                }
                R.id.board_read_menu_delete -> {
                    val act = activity as BoardMainActivity
                    act.fragmentRemoveBackStack("board_read")
                    true
                }


                else -> false

            }
        }

        return boardReadFragmentBinding.root
    }

}