package pt.ulusofona.deisi.cm2223.g22004883_22007130.fragments.register

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2223.g22004883_22007130.*
import pt.ulusofona.deisi.cm2223.g22004883_22007130.adapters.AutoCompleteAdapter
import pt.ulusofona.deisi.cm2223.g22004883_22007130.data.*
import pt.ulusofona.deisi.cm2223.g22004883_22007130.data.Cinema
import pt.ulusofona.deisi.cm2223.g22004883_22007130.databinding.FragmentRegistrationBinding
import java.text.SimpleDateFormat
import java.util.*

class RegistrationFragment : Fragment() {
    /*Estas vars servem para quando se vai para a galeria das fotos e quando voltarmos termos um
    * backup do que o user ja introduziu para que nao perca a informacao ja introduzida*/
    companion object {
        lateinit var lastDateBeforeOpenPhotoIntent: String
        var lastItemPosBeforeOpenPhotoIntent: Int = 0
        var bool = false
        private const val READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 1
    }

    private lateinit var binding: FragmentRegistrationBinding
    private val photos = mutableListOf<String>()
    //private val movieNames = MovieRoom.getAllMovieTitles()
    private var openPhotoIntent = false
    private val repository = MovieRepository.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_registration, container, false)
        binding = FragmentRegistrationBinding.bind(view)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStart() {
        super.onStart()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding.photosRegistration.text =
            photos.size.toString() + " " + getString(R.string.selected_photos)
        /*Se o user abriu o foto intent ele introduz as vars guardadas antes de ter entrado no intent*/
        if (openPhotoIntent) {
            openPhotoIntent = false
            bool = true
            binding.dateSeenRegistration.text = lastDateBeforeOpenPhotoIntent
            binding.cinemaNameRegistration.onItemSelectedListener = SpinnerActivity(
                lastItemPosBeforeOpenPhotoIntent, bool
            )
        } else {
            val adapter = AutoCompleteAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                emptyList(),
                repository
            )
            binding.filmNameRegistration.setAdapter(adapter)
            binding.filmNameRegistration.setOnItemClickListener() { parent, view, position, id ->
                val selectedItem = parent.getItemAtPosition(position) as MovieRoom
                binding.filmNameRegistration.setText(selectedItem.title)
            }
            binding.filmNameRegistration.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    adapter.filter.filter(p0)
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })
            updateLabel(Calendar.getInstance())
            validateCinema()
            onClickDate()
            onClickPhotos()
            binding.registrationButton.setOnClickListener { onRegisterPressed() }
        }
    }

    private fun onRegisterPressed() {
        var movie: MovieRoom? = null
        val title = binding.filmNameRegistration.text.toString()
        repository.getMovieByTitle(title) {
            if (it.isSuccess) {
                CoroutineScope(Dispatchers.Main).launch {
                    movie = it.getOrNull()
                    validateForm(movie)
                }
            } else {
                CoroutineScope(Dispatchers.Main).launch { binding.filmNameRegistration.error = getString(R.string.invalid_title) }
            }
        }
    }

    private fun validateForm(movie: MovieRoom?) {
        var validForm = validateRating()
        if (validForm) {
            validForm = validateDate()
        } else {
            validateDate()
            validForm = false
        }
        if (!validForm || movie == null) return
        repository.getMovieByTitle(movie!!.title) {
            if (it.isSuccess) {
                CoroutineScope(Dispatchers.Main).launch {
                    formRegister(movie!!)
                }
            } else {
                Log.i("Erro", "")
            }
        }
    }

    /*private fun validateTitle(): MovieRoom? {
        var movieRoom: MovieRoom? = null
        val title = binding.filmNameRegistration.text.toString()
        movieRoom?.title?.let {
            repository.getMovieByTitle(it) {

            }
        }
        for (m in MovieRoom.movieRooms) {
            if (m.title == title) {
                movieRoom = m
                break
            }
        }
        if (movieRoom == null) {
            binding.filmNameRegistration.error = getString(R.string.invalid_title)
        }
        return movieRoom
    }*/

    private fun validateCinema() {
        //val cinemaNames: MutableList<String> = mutableListOf()
        //Cinema.cinemas.forEach { cinemaNames.add(it.getName()) }
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.Cinemas,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.cinemaNameRegistration.adapter = adapter
        }
        binding.cinemaNameRegistration.onItemSelectedListener = SpinnerActivity(
            lastItemPosBeforeOpenPhotoIntent, bool
        )
        binding.cinemaNameRegistration.setSelection(getClosestCinema())
    }
    fun getClosestCinema() : Int {
        val currentLatitude = pt.ulusofona.deisi.cm2223.g22004883_22007130.FusedLocation.currentLatitude
        val currentLongitude = pt.ulusofona.deisi.cm2223.g22004883_22007130.FusedLocation.currentLongitude
        val cinemas = Cinema.cinemas
        var closestCinema = 1000000.0
        var cinemaName = ""
        cinemas.forEach {
            val distance = calculateDistance(currentLatitude,currentLongitude,it.getLatitude().toDouble(),it.getLongitude().toDouble())
            Log.d("Cinema:${it.getName()}","Distancia:$distance")
            if (closestCinema > distance) {
                closestCinema = distance
                cinemaName = it.getName()
            }
        }
        return getClosestCinema(cinemaName)
    }

    fun getClosestCinema(name:String) :Int {
        return when {
            name == "Fórum Montijo" -> 0
            name == "Cinema Ideal" -> 1
            name == "Cinema City Campo Pequeno" -> 2
            name == "El Corte Inglês" -> 3
            name == "Cinema NOS Centro Comercial Colombo" -> 4
            name == "Cinema NOS Alvaláxia" -> 5
            name == "Cinema São Jorge" -> 6
            name == "Cinema do Parque" -> 7
            name == "UCI Cinemas Ubbo" -> 8
            name == "Cinema City Alegro Alfragide" -> 9
            name == "Cinema City Almada Fórum" -> 10
            else -> 0
        }
    }


    fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadius = 6371 // Radius of the Earth in kilometers
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        val distance = earthRadius * c
        return distance
    }

    private fun validateRating(): Boolean {
        val regex = Regex("^(10(\\.0)?|[1-9](\\.[0-9])?)\$")
        if (!regex.containsMatchIn(binding.ratingRegistration.text)) {
            binding.ratingRegistration.error = getString(R.string.invalid_rate)
            return false
        }
        return true
    }

    private fun validateDate(): Boolean {
        val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.UK)
        val selectedDate = sdf.parse(binding.dateSeenRegistration.text.toString())
        if (binding.dateSeenRegistration.text.toString() == sdf.format(Calendar.getInstance().time)) {
            binding.dateSeenRegistration.error = null
            return true
        }
        if (Date().before(selectedDate)) {
            binding.dateSeenRegistration.error = getString(R.string.invalid_date)
            return false
        }
        binding.dateSeenRegistration.error = null
        return true
    }

    private fun onClickDate() {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            updateLabel(calendar)
        }
        binding.dateSeenRegistration.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                datePicker,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun updateLabel(calendar: Calendar) {
        val myFormat = "yyyy/MM/dd"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        binding.dateSeenRegistration.text = sdf.format(calendar.time)
        lastDateBeforeOpenPhotoIntent = sdf.format(calendar.time)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, proceed with accessing the gallery
                    showGallery()
                } else {
                    // Permission denied, handle accordingly (e.g., show an error message)
                    Toast.makeText(context,getString(R.string.gallery_access_denied),Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun onClickPhotos() {

        binding.photosRegistration.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // Permission is granted, proceed with accessing the gallery
                showGallery()
            } else {
                // Permission is not granted, request it from the user
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE
                )
            }
        }
    }
    fun showGallery(){
            openPhotoIntent = true
            photos.clear()
            val intent = Intent()
            intent.type = "image/*"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Pictures"), 1)
    }

    @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data?.clipData != null) {
                val x = data.clipData!!.itemCount
                var i = 0
                while (i < x) {
                    val bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        data.clipData!!.getItemAt(i).uri
                    )
                    photos.add(PhotosHandler.bitmapToString(bitmap))
                    i++
                }
            } else if (data?.data != null) {
                val imageURL = data.data
                val uri = Uri.parse(imageURL.toString())
                val bitmap =
                    MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
                photos.add(PhotosHandler.bitmapToString(bitmap))
            }
        }
    }

    private fun formRegister(movie: MovieRoom) {
        val cinemaID = Cinema.getIDByName(binding.cinemaNameRegistration.selectedItem.toString())
        movie.setCinemaID(cinemaID)
        Log.d("cinema",cinemaID.toString())
        movie.setUserRating(binding.ratingRegistration.text.toString())
        movie.setUserSeenDate(binding.dateSeenRegistration.text.toString())
        val mutableList = mutableListOf<String>()
        photos.forEach { mutableList.add(it.toString()) }
        movie.setUserPhoto(mutableList)
        movie.setUserObservations(binding.observationsRegistration.text.toString())
        CoroutineScope(Dispatchers.IO).launch {
            //repository.downloadMovies(movie.getImdbID())
        }
        repository.insertMovieInDatabase(movie)
        //insertInDatabase(movie)
        Toast.makeText(
            activity as Context,
            getString(R.string.registration_success),
            LENGTH_LONG
        ).show()
        clearForm()
        MainActivity.push(R.id.movies)
        /*val bottomNavigator = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigator)
        bottomNavigator.menu.findItem(MainActivity.fragementsStack.last()).isChecked = true*/
        (requireActivity() as MainActivity).binding.bottomNavigator.menu.findItem(MainActivity.fragementsInBottomNavigatorMenuStack.last()).isChecked =
            true//highlight navigator item
        NavigationManager.goToMoviesListFragment(parentFragmentManager)
    }

    private fun clearForm() {
        binding.filmNameRegistration.text.clear()
        binding.ratingRegistration.text.clear()
        photos.clear()
        binding.photosRegistration.text =
            photos.size.toString() + " " + getString(R.string.selected_photos)
        binding.observationsRegistration.text.clear()
        lastDateBeforeOpenPhotoIntent = ""
        lastItemPosBeforeOpenPhotoIntent = 0
        bool = false
        openPhotoIntent = false
    }



}