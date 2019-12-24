package com.example.myapplication.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.myapplication.R
import android.content.pm.PackageManager
import android.content.Context
import android.widget.*
import com.example.myapplication.*

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loadList()
        val root = inflater.inflate(R.layout.fragment_main, container, false)

        //val listView: LinearLayout = root.findViewById(R.id.ui_linearLayout)
        //val adapter: MyAdapter = MyAdapter(activity?.baseContext!!, pageViewModel.launchItems)

        //val textView: TextView = root.findViewById(R.id.section_label)
        //pageViewModel.text.observe(this, Observer<String> {
        //    textView.text = it
        //})
        return root
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    fun loadList() {
        // 面倒なのでここでそのまま画面に反映（ViewModelは使わない！）
        // 内部パッケージ一覧を取得、、、画面に出す
        val pm = activity?.packageManager
        if (pm === null) return
        val pckInfoList = pm.getInstalledPackages(PackageManager.GET_ACTIVITIES or PackageManager.GET_SERVICES)
        val launchItems: MutableList<LaunchItem> = mutableListOf()
        for (pckInfo in pckInfoList) {
            if (pm.getLaunchIntentForPackage(pckInfo.packageName) != null) {
                val packageName = pckInfo.packageName
                val className = pm.getLaunchIntentForPackage(pckInfo.packageName)!!.component!!.className + ""
                Log.i("起動可能なパッケージ名", packageName)
                Log.i("起動可能なクラス名", className)
                launchItems.add(LaunchItem(true, packageName, className))
            } else {
                Log.i("----------起動不可能なパッケージ名", pckInfo.packageName)
                //launchItems.add(LaunchItem(false, pckInfo.packageName, ""))
            }
        }
        pageViewModel.launchItems = launchItems
    }
}

class MyAdapter(context: Context,data: MutableList<LaunchItem>): BaseAdapter() {

    var context: Context = context
    val infrater:LayoutInflater = LayoutInflater.from(context)
    var _data: MutableList<LaunchItem> = data

    override fun getCount(): Int {
        return _data.size
    }

    override fun getItem(position: Int): Any {
        return _data[position]
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View? = convertView
        if (view != null) {
            view.findViewById<TextView>(R.id.textView).setText(_data[position].packageName)
            view.findViewById<TextView>(R.id.textView2).setText(_data[position].className)
        }
        else {
        }
        return view!!
    }
}




