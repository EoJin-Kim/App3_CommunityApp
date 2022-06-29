package com.example.app3_communityapp

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        boardMainFragemntBinding = FragmentBoardMainBinding.inflate(inflater)
        boardMainFragemntBinding.boardMainToolbar.title = "게시판이름"

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

            }
        }
    }
}