package ir.radesh.basemodule.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class CustomSpinnerAdapter(context: Context, resource: Int, internal var list: List<String>) :
    ArrayAdapter<String>(context, resource, list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return super.getView(position, convertView, parent) as TextView
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return super.getDropDownView(position, convertView, parent) as TextView
    }

    override fun getCount(): Int {
        return list.size
    }
}