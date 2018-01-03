package pl.edu.agh.designpatterns.alexabeaconapp

import android.app.Activity
import android.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_beaconspreferences.*
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.elem_beaconarea.view.*


/**
 * Created by motek on 03.01.18.
 */

class MyAdapter(var beaconAreas : MutableList<BeaconArea>,var recyclerView: RecyclerView, var myAdapterButtonsListner: MyAdapterButtonsListner) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    interface MyAdapterButtonsListner {
        fun editButtonOnClick(poition: Int)
        fun deleteButtonOnClick(poition: Int)
    }

    override fun getItemCount(): Int {
        return beaconAreas.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
        val beaconArea = beaconAreas.get(position)
        holder!!.areaTagTV.text = beaconArea.tag
        holder.areaRadiusTV.text = beaconArea.radius.toString()
        holder.areaImgIV.setImageResource(beaconArea.image)

        holder.editButton.setOnClickListener {
            myAdapterButtonsListner.editButtonOnClick(position)
        }

        holder.deleteButton.setOnClickListener {
            myAdapterButtonsListner.deleteButtonOnClick(position)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.elem_beaconarea,parent,false)
        return MyViewHolder(view)
    }


    class MyViewHolder(v:View) : RecyclerView.ViewHolder(v) {
        val areaTagTV = v.area_tag_tv
        val areaRadiusTV = v.area_radius_tv
        val areaImgIV = v.area_image_iv
        val editButton = v.button
        val deleteButton = v.button2
    }
}

class BeaconsPreferencesActivity : Activity(), EditBeaconAreaDialog.NoticeDialogListener, MyAdapter.MyAdapterButtonsListner {
    override fun editButtonOnClick(poition: Int) {
        val elem = areasList.get(poition)
        val newFragment = EditBeaconAreaDialog()
        newFragment.areaTag = elem.tag
        newFragment.areaRadius = elem.radius
        newFragment.areaImage = elem.image
        newFragment.elemIndex = poition
        newFragment.show(this@BeaconsPreferencesActivity.fragmentManager,"sth345")
    }

    override fun deleteButtonOnClick(poition: Int) {
        areasList.removeAt(poition)
        mAdapter!!.notifyDataSetChanged()
    }

    var areasList = mutableListOf<BeaconArea>()

    override fun onDialogPositiveClick(editBeaconAreaDialog: EditBeaconAreaDialog) {
        if(editBeaconAreaDialog.elemIndex < 0) areasList.add(BeaconArea(editBeaconAreaDialog.areaTag!!, editBeaconAreaDialog.areaRadius!!, editBeaconAreaDialog.areaImage!!))
        else {
            areasList.removeAt(editBeaconAreaDialog.elemIndex)
            areasList.add(editBeaconAreaDialog.elemIndex,BeaconArea(editBeaconAreaDialog.areaTag!!, editBeaconAreaDialog.areaRadius!!, editBeaconAreaDialog.areaImage!!))
            mAdapter!!.notifyDataSetChanged()
        }
    }

    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beaconspreferences)
        mRecyclerView = beaconslist_rv
        mLayoutManager = LinearLayoutManager(this)
        mRecyclerView!!.layoutManager = mLayoutManager

        val tmpList = mutableListOf<BeaconArea>()
        tmpList.add(BeaconArea("desk",5.0,R.drawable.ic_launcher_foreground))
        tmpList.add(BeaconArea("chair",2.0,R.drawable.ic_launcher_foreground))
        tmpList.add(BeaconArea("desk",9.0,R.drawable.ic_launcher_foreground))
        tmpList.add(BeaconArea("chair",4.0,R.drawable.ic_launcher_foreground))
        tmpList.add(BeaconArea("desk",99.0,R.drawable.ic_launcher_foreground))
        tmpList.add(BeaconArea("chair",3.0,R.drawable.ic_launcher_foreground))

        areasList = tmpList
        mAdapter = MyAdapter(areasList, mRecyclerView!!, this)
        mRecyclerView!!.adapter = mAdapter
        addarea_fb.setOnClickListener {
            val newFragment = EditBeaconAreaDialog()
            newFragment.show(this@BeaconsPreferencesActivity.fragmentManager,"sth123")

        }
    }
}