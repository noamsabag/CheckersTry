package com.example.checkerstry

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.checkerstry.databinding.FragmentOfflineGameBinding
import com.example.checkerstry.databinding.FragmentOnlineGameBinding

class OfflineGameFragment : Fragment() {

    private var _binding: FragmentOfflineGameBinding?  = null
    private  val binding
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOfflineGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.passAndPlay.setOnClickListener{
            val intent = Intent(context, GameActivity::class.java)
            startActivity(intent)
        }

        binding.playAginstCpu.setOnClickListener{
            val intent = Intent(context, RoomsActivity::class.java)
            startActivity(intent)
        }
    }

}