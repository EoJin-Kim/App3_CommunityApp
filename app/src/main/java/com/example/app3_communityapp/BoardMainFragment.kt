package com.example.app3_communityapp

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app3_communityapp.databinding.BoardMainRecyclerItemBinding
import com.example.app3_communityapp.databinding.FragmentBoardMainBinding
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import kotlin.concurrent.thread


class BoardMainFragment : Fragment() {

    lateinit var boardMainFragemntBinding : FragmentBoardMainBinding

    val contentIdxList = ArrayList<Int>()
    val contentWriterList = ArrayList<String>()
    val contentWriterDateList = ArrayList<String>()
    val contentSubjectList = ArrayList<String>()

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
                        getContentList(true)
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
        boardMainFragemntBinding.boardMainRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // 현재 화면에 보이는 항목 중제일 마직 항목의 인덱스
                val index1 = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

                // 리사이클러 뷰가 관리하는 항목의 총 개수
                val count1 = recyclerView.adapter?.itemCount

                Log.d("test","${index1+1} : ${count1}")
                if(index1 +1 ==count1){
                    act.nowPage+=1
                    getContentList(false)
                }
            }
        })

        getContentList(true)

        boardMainFragemntBinding.boardMainSwipe.setOnRefreshListener {
            // 새로고침 동안 해야하는 작업
            getContentList(true);

            // 작업 끝나면 false
            boardMainFragemntBinding.boardMainSwipe.isRefreshing = false
        }

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
            holder.boardMainItemNickname.text =  contentWriterList.get(position)
            holder.boardMainItemWriteDate.text = contentWriterDateList[position]
            holder.boardMainItemSubject.text =contentSubjectList[position]
        }

        override fun getItemCount(): Int {
            return contentIdxList.size
        }

        inner class ViewHolderClass(boardMainRecyclerItemBinding: BoardMainRecyclerItemBinding) :
            RecyclerView.ViewHolder(boardMainRecyclerItemBinding.root), View.OnClickListener {

            val boardMainItemNickname = boardMainRecyclerItemBinding.boardMainItemNickname
            val boardMainItemSubject = boardMainRecyclerItemBinding.boardMainItemSubject
            val boardMainItemWriteDate = boardMainRecyclerItemBinding.boardMainItemWriteDate

            override fun onClick(v: View?) {
                val act = activity as BoardMainActivity

                act.readContentIdx = contentIdxList[adapterPosition]

                act.fragmentController("board_read",true,true)
            }
        }
    }

    fun getContentList(clear:Boolean){
        if(clear){
            contentIdxList.clear()
            contentWriterList.clear()
            contentWriterDateList.clear()
            contentSubjectList.clear()

            val act = activity as BoardMainActivity
            act.nowPage = 1;
        }

        thread {
            val client = OkHttpClient()

            val site = "http://${ServerInfo.SERVER_IP}:8080/get_content_list"

            val act = activity as BoardMainActivity

            val builder1 = FormBody.Builder()
            builder1.add("content_board_idx", "${act.selectedBoardType}")
            builder1.add("page_num","${act.nowPage}")
            val formBody = builder1.build()

            val request = Request.Builder().url(site).post(formBody).build()
            val response = client.newCall(request).execute()

            if(response.isSuccessful){
                val resultText = response.body?.string()!!.trim()
                val root = JSONArray(resultText)

                for(i in 0 until root.length()){
                    val obj = root.getJSONObject(i)

                    contentIdxList.add(obj.getInt("content_idx"))
                    contentWriterList.add(obj.getString("content_nick_name"))
                    contentWriterDateList.add(obj.getString("content_write_date"))
                    contentSubjectList.add(obj.getString("content_subject"))

                }

                // 만약 가져온 것이 하나도 없다면 존재하지 않는 페이지 이므로 페이지를 하나 빼준다.
                if(root.length() ==0){
                    act.nowPage -=1;
                }
                act?.runOnUiThread {
                    boardMainFragemntBinding.boardMainRecycler.adapter?.notifyDataSetChanged()
                }

            }
        }
    }

}