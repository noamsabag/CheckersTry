package com.example.checkerstry

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.checkerstry.databinding.FragmentMainMenuBinding
import com.example.checkerstry.databinding.FragmentOfflineGameBinding

class MainMenuFragment : Fragment() {

    private var _binding: FragmentMainMenuBinding?  = null
    private  val binding
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.quickMatch.setOnClickListener{
            val intent = Intent(context, RoomsActivity::class.java)
            startActivity(intent)
        }

        binding.analysys.setOnClickListener{
            val intent = Intent(context, GameHistoryActivity::class.java)
            startActivity(intent)
        }

        binding.playAginstCpu.setOnClickListener{
            val intent = Intent(context, GameActivity::class.java)
            startActivity(intent)
        }

    }

}