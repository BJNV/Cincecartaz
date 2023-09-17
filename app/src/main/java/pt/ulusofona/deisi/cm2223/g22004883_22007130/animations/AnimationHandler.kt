package pt.ulusofona.deisi.cm2223.g22004883_22007130.animations

import pt.ulusofona.deisi.cm2223.g22004883_22007130.MainActivity
import pt.ulusofona.deisi.cm2223.g22004883_22007130.R

object AnimationHandler {
    var noAnimation = true

    fun swipeRightWay(): List<Int>? {
        if(noAnimation) {
            noAnimation = false
            return null
        }
        var getCurrentFragmentId = 0
        var getNextFragmentId = 0
        val fragmentsStack = MainActivity.fragementsInBottomNavigatorMenuStack
        val fragmentsListId = MainActivity.fragmentsListId
        var i = 0
        while (i < fragmentsListId.size) {
            if (fragmentsStack.size == 1) {
                return null
            }
            if (fragmentsListId[i] == fragmentsStack[fragmentsStack.size - 1]) {
                getNextFragmentId = i
            }
            if (fragmentsListId[i] == fragmentsStack[fragmentsStack.size - 2]) {
                getCurrentFragmentId = i
            }
            i++
        }
        return if (getNextFragmentId < getCurrentFragmentId) {
            listOf(R.anim.slide_in_left, R.anim.slide_out_right)
        } else if(getNextFragmentId > getCurrentFragmentId){
            listOf(R.anim.slide_in_right, R.anim.slide_out_left)
        } else{
            null
        }
    }
}