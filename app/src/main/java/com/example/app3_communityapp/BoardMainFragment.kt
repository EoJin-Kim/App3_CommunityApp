package com.example.app3_communityapp

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app3_communityapp.databinding.BoardMainRecyclerItemBinding
import com.example.app3_communityapp.databinding.FragmentBoardMainBinding


class BoardMainFragment : Fragment() {

    lateinit var boardMainFragemntBinding : FragmentBoardMainBinding
    val boardListData = arrayOf(
        "전체글","게시판1","게시판2","게시판3","게시판4",
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val act = activity as BoardMainActivity

        // Inflate the layout for this fragment
        boardMainFragemntBinding = FragmentBoardMainBinding.inflate(inflater)
        boardMainFragemntBinding.boardMainToolbar.title = act.boardNameList[act.selectedBoardType];

        boardMainFragemntBinding.boardMainToolbar.inflateMenu(R.menu.board_main_menu)
        boardMainFragemntBinding.boardMainToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.board_main_menu_board_list ->{

                    val act = activity as BoardMainActivity

                    val boardListBuilder = AlertDialog.Builder(requireContext())
                    boardListBuilder.setTitle("게시판 목록")
                    boardListBuilder.setNegativeButton("취소",null)
                    boardListBuilder.setItems(act.boardNameList.toTypedArray()){ dialogInterface: DialogInterface, i: Int ->
                        act.selectedBoardType = i
                        boardMainFragemntBinding.boardMainToolbar.title = act.boardNameList[act.selectedBoardType]
                    }
                    boardListBuilder.show()
                    true
                }

                R.id.board_main_menu_write -> {
                    val act = activity as BoardMainActivity
                    act.fragmentController("board_write",true,true)
                    true
                }

                else -> false
            }
            true
        }



        val boardMainRecyclerAdapter = BoardMainRecyclerAdapter()
        boardMainFragemntBinding.boardMainRecycler.adapter = boardMainRecyclerAdapter

        boardMainFragemntBinding.boardMainRecycler.layoutManager = LinearLayoutManager(requireContext())
        boardMainFragemntBinding.boardMainRecycler.addItemDecoration(DividerItemDecoration(requireContext(),1))

        return boardMainFragemntBinding.root
    }


    inner class BoardMainRecyclerAdapter : RecyclerView.Adapter<BoardMainRecyclerAdapter.ViewHolderClass>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
            val boardMainRecyclerItemBinding = BoardMainRecyclerItemBinding.inflate(layoutInflater)
            val holder = ViewHolderClass(boardMainRecyclerItemBinding)

            val layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            boardMainRecyclerItemBinding.root.layoutParams = layoutParams
            boardMainRecyclerItemBinding.root.setOnClickListener(holder)

            return holder

        }

        override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {

        }

        override fun getItemCount(): Int {
            return 10
        }

        inner class ViewHolderClass(boardMainRecyclerItemBinding: BoardMainRecyclerItemBinding) :
            RecyclerView.ViewHolder(boardMainRecyclerItemBinding.root), View.OnClickListener {
            override fun onClick(v: View?) {
                val act = activity as BoardMainActivity
                act.fragmentController("board_read",true,true)
            }
        }
    }
}