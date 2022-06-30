package com.example.app3_communityapp

import android.content.Context
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
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.concurrent.thread


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

//            Log.d("test","$loginId")
//            Log.d("test","$loginPw")
//            Log.d("test","$loginAutoLogin")

            thread {
                val client =OkHttpClient()

                val site = "http://219.248.58.57:8080/login_user"

                val builder1 = FormBody.Builder()
                builder1.add("user_id",loginId)
                builder1.add("user_pw",loginPw)
                builder1.add("user_autologin","$loginAutoLogin")
                val formBody = builder1.build()

                val request = Request.Builder().url(site).post(formBody).build()
                val response = client.newCall(request).execute()

                if(response.isSuccessful){
                    // 응답 결과를 추출한다.
                    val result_test = response.body?.string()!!.trim()
//                    Log.d("test",result_test)

                    if(result_test == "FAIL"){
                        activity?.runOnUiThread {
                            val dialogBuilder = AlertDialog.Builder(requireContext())
                            dialogBuilder.setTitle("로그인 실패")
                            dialogBuilder.setMessage("아이디나 비밀번호가 잘못되었습니다")
                            dialogBuilder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                                loginFragmentBinding.loginId.setText("")
                                loginFragmentBinding.loginPw.setText("")
                                loginFragmentBinding.loginAutologin.isChecked= false
                                loginFragmentBinding.loginAutologin.requestFocus()
                            }
                            dialogBuilder.show()
                        }
                    }
                    else{
                        activity?.runOnUiThread {
                            val dialogBuilder = AlertDialog.Builder(requireContext())
                            dialogBuilder.setTitle("로그인 성공")
                            dialogBuilder.setMessage("로그인에 성공하였습니다")
                            dialogBuilder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->

                                // 사용자 정보를 preferences에 저장한다
                                val pref = activity?.getSharedPreferences("login_data",Context.MODE_PRIVATE)
                                val editor = pref?.edit()

                                editor?.putInt("login_user_idx",Integer.parseInt(result_test))
                                editor?.putInt("login_auto_login",loginAutoLogin)
                                editor?.commit()

                                val boardMainIntent = Intent(requireContext(),BoardMainActivity::class.java)
                                startActivity(boardMainIntent)
                                activity?.finish()

                            }
                            dialogBuilder.show()
                        }
                    }
                }else{
                    activity?.runOnUiThread {
                        val dialogBuilder = AlertDialog.Builder(requireContext())
                        dialogBuilder.setTitle("로그인 오류")
                        dialogBuilder.setMessage("로그인 오류가 발생하였습니다")
                        dialogBuilder.setPositiveButton("확인",null)
                        dialogBuilder.show()

                    }
                }
            }

//            val boardMainIntent = Intent(requireContext(),BoardMainActivity::class.java)
//            startActivity(boardMainIntent)
//            activity?.finish()
        }

        return loginFragmentBinding.root
    }


}