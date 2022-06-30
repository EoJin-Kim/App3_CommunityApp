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
import com.example.app3_communityapp.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    lateinit var loginFragmentBinding : FragmentLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        loginFragmentBinding = FragmentLoginBinding.inflate(inflater)

        loginFragmentBinding.loginToolbar.title = "로그인"

        loginFragmentBinding.loginJoinbtn.setOnClickListener {
            val act = activity as MainActivity
            act.fragmentController("join",true,true)
        }

        loginFragmentBinding.loginLoginbtn.setOnClickListener {

            val loginId = loginFragmentBinding.loginId.text.toString()
            val loginPw = loginFragmentBinding.loginPw.text.toString()
            val chk = loginFragmentBinding.loginAutologin.isChecked

            var loginAutoLogin = 0
            if(chk){
                loginAutoLogin = 1
            }else{
                loginAutoLogin = 0
            }

            if(loginId == null || loginId.length ==0){
                val dialoBuilder = AlertDialog.Builder(requireContext())
                dialoBuilder.setTitle("아이디 입력 오류")
                dialoBuilder.setMessage("아이드를 입력해 주세요")
                dialoBuilder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                    loginFragmentBinding.loginId.requestFocus()
                }
                dialoBuilder.show()
                return@setOnClickListener
            }

            if(loginPw == null || loginPw.length ==0){
                val dialoBuilder = AlertDialog.Builder(requireContext())
                dialoBuilder.setTitle("비밀번호 입력 오류")
                dialoBuilder.setMessage("비밀번호호입력해 주세요")
                dialoBuilder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                    loginFragmentBinding.loginPw.requestFocus()
                }
                dialoBuilder.show()
                return@setOnClickListener
            }

            Log.d("test","$loginId")
            Log.d("test","$loginPw")
            Log.d("test","$loginAutoLogin")

//            val boardMainIntent = Intent(requireContext(),BoardMainActivity::class.java)
//            startActivity(boardMainIntent)
//            activity?.finish()
        }

        return loginFragmentBinding.root
    }


}