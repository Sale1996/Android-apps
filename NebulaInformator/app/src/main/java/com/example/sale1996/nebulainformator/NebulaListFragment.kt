package com.example.sale1996.nebulainformator

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TableRow
import kotlinx.android.synthetic.main.fragment_nebula_list.*


class NebulaListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nebula_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        for (i in 0 until nebula_table.childCount){
            val row = nebula_table.getChildAt(i) as TableRow
            for (j in 0 until row.childCount){
                val button = row.getChildAt(j) as ImageButton
                button.setOnClickListener{
                    goToNebulaInfo(it)
                }
            }
        }
    }


    private fun goToNebulaInfo(view : View){

        val button : ImageButton = view as ImageButton
        val tag: String = button.tag.toString()

        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
            // do in orientation portrait
            val detailIntent = Intent(activity, NebulaDetailActivity::class.java)
            detailIntent.putExtra("nebula_tag", tag)
            startActivity(detailIntent)

        }
        else{

            // do in landscape mode
            val nebulaDetailsFragment = fragmentManager!!.findFragmentById(R.id.nebula_detail_fragment) as NebulaDetailFragment
            nebulaDetailsFragment.setNebulaDetails(tag)
        }

    }

}
