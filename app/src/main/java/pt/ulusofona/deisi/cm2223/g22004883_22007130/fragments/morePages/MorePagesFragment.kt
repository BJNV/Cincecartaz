package pt.ulusofona.deisi.cm2223.g22004883_22007130.fragments.morePages

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import pt.ulusofona.deisi.cm2223.g22004883_22007130.MainActivity
import pt.ulusofona.deisi.cm2223.g22004883_22007130.NavigationManager
import pt.ulusofona.deisi.cm2223.g22004883_22007130.R
import pt.ulusofona.deisi.cm2223.g22004883_22007130.animations.AnimationHandler
import pt.ulusofona.deisi.cm2223.g22004883_22007130.animations.startAnimation
import pt.ulusofona.deisi.cm2223.g22004883_22007130.databinding.FragmentMorePagesBinding


class MorePagesFragment : Fragment() {

    private lateinit var binding: FragmentMorePagesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_more_pages, container, false)
        binding = FragmentMorePagesBinding.bind(view)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        /*animacao*/
        val animation = AnimationUtils.loadAnimation(context,R.anim.button_explosion_anim).apply {
            duration = 750
            interpolator = AccelerateDecelerateInterpolator()
        }
        binding.goToExtraButton.setOnClickListener {
            AnimationHandler.noAnimation = true
            binding.goToExtraButton.isVisible = false
            binding.frameForExplosionAnimationExtra.isVisible = true
            /*animacao*/

            MainActivity.push(R.id.more_pages)
            binding.frameForExplosionAnimationExtra.startAnimation(animation){
                NavigationManager.goToExtraPage(parentFragmentManager)
            }
        }
        binding.goToMapButton.setOnClickListener {
            MainActivity.push(R.id.more_pages)
            NavigationManager.goToMapFragment(parentFragmentManager)
        }
    }

}