package com.example.app3_communityapp

import android.content.Context
import android.content.DialogInterface
import android.graphics.*
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.app3_communityapp.databinding.FragmentBoardReadBinding
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.net.URL
import kotlin.concurrent.thread


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


        thread {
            val client = OkHttpClient()

            val site = "http://${ServerInfo.SERVER_IP}:8080/get_content"

            val act = activity as BoardMainActivity

            val builder1 = FormBody.Builder()
            builder1.add("read_content_idx","${act.readContentIdx}")
            val formBody = builder1.build()

            val request = Request.Builder().url(site).post(formBody).build()
            val response = client.newCall(request).execute()

            if(response.isSuccessful){
                val resultText = response.body?.string()!!.trim()
                val obj = JSONObject(resultText)

                val contentWriterIdx = obj.getInt("content_writer_idx")

                act?.runOnUiThread {
                    boardReadFragmentBinding.boardReadSubject.text = obj.getString("content_subject")
                    boardReadFragmentBinding.boardReadWriter.text = obj.getString("content_nick_name")
                    boardReadFragmentBinding.boardReadWriteDate.text = obj.getString("content_write_date")
                    boardReadFragmentBinding.boardReadText.text = obj.getString("content_text")

                    val contentImage = obj.getString("content_image")
                    if(contentImage =="null"){
                        boardReadFragmentBinding.boardReadImage.visibility = View.GONE
                    }else{
                        thread {
                            val imageUrl = URL("http://${ServerInfo.SERVER_IP}:8080/upload/$contentImage")
                            val bitmap = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream())
                            act?.runOnUiThread {
                                boardReadFragmentBinding.boardReadImage.setImageBitmap(bitmap)
                            }
                        }
                    }

                    val pref = act.getSharedPreferences("login_data",Context.MODE_PRIVATE)
                    val loginUserIdx = pref.getInt("login_user_idx" ,-1)

                    if(loginUserIdx == contentWriterIdx){
                        boardReadFragmentBinding.boardReadToolbar.inflateMenu(R.menu.board_read_menu)
                        boardReadFragmentBinding.boardReadToolbar.setOnMenuItemClickListener {
                            when (it.itemId) {
                                R.id.board_read_menu_modify -> {
                                    val act = activity as BoardMainActivity
                                    act.fragmentController("board_modify", true,true)
                                    true
                                }
                                R.id.board_read_menu_delete -> {


                                    thread {
                                        val act = activity as BoardMainActivity

                                        val client = OkHttpClient()

                                        val site = "http://${ServerInfo.SERVER_IP}:8080/delete_content"

                                        val builder1 = FormBody.Builder()
                                        builder1.add("content_idx","${act.readContentIdx}")
                                        val formBody = builder1.build()

                                        val request = Request.Builder().url(site).post(formBody).build()
                                        val response = client.newCall(request).execute()

                                        if(response.isSuccessful){
                                            act.runOnUiThread {
                                                val dialogBuilder= AlertDialog.Builder(act!!)
                                                dialogBuilder.setTitle("글 삭제")
                                                dialogBuilder.setMessage("글이 삭제되었습니다")
                                                dialogBuilder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
//                                                    val act = activity as BoardMainActivity
                                                    act.fragmentRemoveBackStack("board_read")
                                                }
                                                dialogBuilder.show()
                                            }

                                        }
                                    }
                                    val act = activity as BoardMainActivity

                                    act.fragmentRemoveBackStack("board_read")
                                    true
                                }
                                else -> false

                            }
                        }
                    }


                }
            }

        }

        return boardReadFragmentBinding.root
    }

}