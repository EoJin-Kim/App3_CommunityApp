package com.example.app3_communityapp

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.example.app3_communityapp.databinding.FragmentBoardMainBinding
import com.example.app3_communityapp.databinding.FragmentBoardWriteBinding
import java.io.File


class BoardWriteFragment : Fragment() {


    lateinit var boardWriteFragmentBinding  : FragmentBoardWriteBinding

    var spinner_data = arrayOf("게시판1","게시판2","게시판3","게시판4")

    lateinit var contentUri : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val act = activity as BoardMainActivity

        boardWriteFragmentBinding = FragmentBoardWriteBinding.inflate(inflater)
        boardWriteFragmentBinding.boardWriteToolbar.title = "게시글 작성"
        boardWriteFragmentBinding.boardWriteToolbar.inflateMenu(R.menu.board_write_menu)
        boardWriteFragmentBinding.boardWriteToolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.board_write_menu_camera ->{
                    val filePath = requireContext().getExternalFilesDir(null).toString()

                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                    // 촬영한 사진이 저장될 파일 이름
                    val fileName = "/temp_${System.currentTimeMillis()}.jpg"
                    val picPath = "$filePath/$fileName"

                    val file = File(picPath)

                    contentUri = FileProvider.getUriForFile(requireContext(),"com.example.app3_communityapp.file_provider",file)


                    if (contentUri != null) {
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,contentUri)
                        startActivityForResult(cameraIntent,1)
                    }

                    true
                }
                R.id.board_write_menu_gallery ->{
                    val albumIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    albumIntent.type = "image/*"

                    val mimeType = arrayOf("image/*")
                    albumIntent.putExtra(Intent.EXTRA_MIME_TYPES,mimeType)
                    startActivityForResult(albumIntent,2)

                    true
                }
                R.id.board_write_menu_upload ->{
                    val act = activity as BoardMainActivity

                    val boardWriteSubject = boardWriteFragmentBinding.boardWriteSubject.text.toString()
                    val boardWriteText = boardWriteFragmentBinding.boardWriteText.text.toString()

                    if (boardWriteSubject == null || boardWriteSubject.length == 0) {
                        val dialogBuilder = AlertDialog.Builder(requireContext())
                        dialogBuilder.setTitle("제목 입력 오류")
                        dialogBuilder.setMessage("제목을 입력해 주세요")
                        dialogBuilder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                            boardWriteFragmentBinding.boardWriteSubject.requestFocus()
                        }
                        dialogBuilder.show()
                        return@setOnMenuItemClickListener  true
                    }

                    if (boardWriteText == null || boardWriteText.length == 0) {
                        val dialogBuilder = AlertDialog.Builder(requireContext())
                        dialogBuilder.setTitle("내용 입력 오류")
                        dialogBuilder.setMessage("내용 입력해 주세요")
                        dialogBuilder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                            boardWriteFragmentBinding.boardWriteText.requestFocus()
                        }
                        dialogBuilder.show()
                        return@setOnMenuItemClickListener  true
                    }

                    act.fragmentRemoveBackStack("board_write")
                    act.fragmentController("board_read",true,true)
                    true
                }
                else -> false

            }
        }

        val spinnerAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,
                act.boardNameList.drop(1))

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        boardWriteFragmentBinding.boardWriteType.adapter = spinnerAdapter

        if (act.selectedBoardType == 0) {
            boardWriteFragmentBinding.boardWriteType.setSelection(0)
        } else {
            boardWriteFragmentBinding.boardWriteType.setSelection(act.selectedBoardType -1)
        }


        return boardWriteFragmentBinding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> {
                if (resultCode == Activity.RESULT_OK) {
                    val bitmap = BitmapFactory.decodeFile(contentUri.path)
                    boardWriteFragmentBinding.boardWriteImage.setImageBitmap(bitmap)

                    val file = File(contentUri.path)
                    file.delete()
                }
            }

            2 -> {
                if (resultCode == Activity.RESULT_OK) {
                    // 선택한 이미지에 접근할 수 있는 uri
                    val uri = data?.data

                    if (uri != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            val source = ImageDecoder.createSource(activity?.contentResolver!!, uri)
                            val bitmap = ImageDecoder.decodeBitmap(source)
                            boardWriteFragmentBinding.boardWriteImage.setImageBitmap(bitmap)
                        } else {
                            val cursor = activity?.contentResolver?.query(uri,null,null,null,null)
                            if (cursor != null) {
                                cursor.moveToNext()
                                // 이미지 경로를 가져온다
                                val index = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                                val source = cursor.getString(index)
                                val bitmap = BitmapFactory.decodeFile(source)
                                boardWriteFragmentBinding.boardWriteImage.setImageBitmap(bitmap)
                            }
                        }
                    }
                }
            }
        }
    }

}