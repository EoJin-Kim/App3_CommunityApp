package com.example.app3_communityapp

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.app3_communityapp.databinding.FragmentNickNameBinding


class NickNameFragment : Fragment() {

    lateinit var nickNameFragmentBinding : FragmentNickNameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        nickNameFragmentBinding = FragmentNickNameBinding.inflate(inflater)

        nickNameFragmentBinding.nicknameToolbar.title = "닉네임 임력"

        nickNameFragmentBinding.nicknameJoinBtn.setOnClickListener {
            val nickNameNickName = nickNameFragmentBinding.nicknameNickname.text.toString()
            if (nickNameNickName == null || nickNameNickName.length == 0) {
                val dialogBuilder = AlertDialog.Builder(requireContext())
                dialogBuilder.setTitle("넥네임 입력 오류")
                dialogBuilder.setMessage("닉네임을 입력해주세요")
                dialogBuilder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                    nickNameFragmentBinding.nicknameNickname.requestFocus()
                }
                dialogBuilder.show()
                return@setOnClickListener
            }

            val act = activity as MainActivity
            act.userNickname = nickNameNickName

            Log.d("test","${act.userId}")
            Log.d("test","${act.userPw}")
            Log.d("test","${act.userNickname}")

            val mainIntent = Intent(requireContext(),MainActivity::class.java)
            startActivity(mainIntent)
            activity?.finish()
        }

        return nickNameFragmentBinding.root
    }
}