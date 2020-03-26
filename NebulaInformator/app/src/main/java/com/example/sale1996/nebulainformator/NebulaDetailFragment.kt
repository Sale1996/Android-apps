package com.example.sale1996.nebulainformator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_nebula_detail.*


class NebulaDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nebula_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (activity?.intent != null){

            val nebulaTag = activity!!.intent.getStringExtra("nebula_tag") ?: "cats_eye_nebula"
            setNebulaDetails(nebulaTag)
        }

    }

    fun setNebulaDetails(nebulaTag : String){

        val nebulaImageID = resources.getIdentifier(nebulaTag, "drawable", activity!!.packageName)
        val nebulaTextFileID = resources.getIdentifier("$nebulaTag"+"_txt", "raw", activity!!.packageName)
        val fileText = resources.openRawResource(nebulaTextFileID).bufferedReader().readText()

        nebula_name.text = (nebulaTag.toString().replace("_", " "))
        nebula_detail_image.setImageResource(nebulaImageID)
        nebula_detail_desc.text = fileText

    }

}
