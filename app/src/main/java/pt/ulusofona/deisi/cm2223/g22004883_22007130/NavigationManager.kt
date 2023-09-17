package pt.ulusofona.deisi.cm2223.g22004883_22007130

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import pt.ulusofona.deisi.cm2223.g22004883_22007130.animations.AnimationHandler
import pt.ulusofona.deisi.cm2223.g22004883_22007130.fragments.dashboard.HomeFragment
import pt.ulusofona.deisi.cm2223.g22004883_22007130.fragments.morePages.ExtraFragment
import pt.ulusofona.deisi.cm2223.g22004883_22007130.fragments.morePages.MapFragment
import pt.ulusofona.deisi.cm2223.g22004883_22007130.fragments.morePages.MorePagesFragment
import pt.ulusofona.deisi.cm2223.g22004883_22007130.fragments.moviesList.MovieDetailViewFragment
import pt.ulusofona.deisi.cm2223.g22004883_22007130.fragments.moviesList.MoviesListFragment
import pt.ulusofona.deisi.cm2223.g22004883_22007130.fragments.register.RegistrationFragment

object NavigationManager {

    private fun placeFragment(fm: FragmentManager, fragment: Fragment) {
        val animations = AnimationHandler.swipeRightWay()
        val transition = fm.beginTransaction()
        if(animations != null) {
            transition.setCustomAnimations(animations[0],animations[1])
        }
        transition.replace(R.id.frame, fragment)
        transition.addToBackStack(null)
        transition.commit()
    }

    fun goToHomeFragment(fm: FragmentManager) {
        placeFragment(fm, HomeFragment())
    }

    fun goToMoviesListFragment(fm: FragmentManager) {
        placeFragment(fm, MoviesListFragment())
    }

    fun goToRegistrationFragment(fm: FragmentManager) {
        placeFragment(fm, RegistrationFragment())
    }

    fun goToMorePages(fm: FragmentManager) {
        placeFragment(fm, MorePagesFragment())
    }

    fun goToExtraPage(fm: FragmentManager) {
        placeFragment(fm, ExtraFragment())
    }

    fun goToMovieDetailFragment(fm: FragmentManager, imdbID: String) {
        placeFragment(fm, MovieDetailViewFragment.newInstanceWithID(imdbID))
    }

    fun goToMapWithCinemaIdFragment(fm: FragmentManager, cinemaId: String) {
        placeFragment(fm, MapFragment.newInstance(cinemaId))
    }
    fun goToMapFragment(fm: FragmentManager) {
        placeFragment(fm, MapFragment())
    }

}