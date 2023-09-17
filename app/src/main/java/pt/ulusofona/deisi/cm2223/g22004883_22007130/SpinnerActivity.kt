package pt.ulusofona.deisi.cm2223.g22004883_22007130

import android.app.Activity
import android.view.View
import android.widget.AdapterView
import pt.ulusofona.deisi.cm2223.g22004883_22007130.fragments.register.RegistrationFragment

class SpinnerActivity(private var selectedItemPos: Int,val useParameterItem: Boolean): Activity(), AdapterView.OnItemSelectedListener {

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        if(useParameterItem){
            parent?.getItemAtPosition(selectedItemPos)
            RegistrationFragment.bool = false
        }else{
            parent?.getItemAtPosition(pos)
            RegistrationFragment.lastItemPosBeforeOpenPhotoIntent = pos
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}