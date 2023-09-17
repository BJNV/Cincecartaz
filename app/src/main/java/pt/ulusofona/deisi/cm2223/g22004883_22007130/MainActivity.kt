package pt.ulusofona.deisi.cm2223.g22004883_22007130

import ConnectivityUtil
import android.Manifest
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.core.view.isVisible
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2223.g22004883_22007130.R
import pt.ulusofona.deisi.cm2223.g22004883_22007130.databinding.ActivityMainBinding
import pt.ulusofona.deisi.cm2223.g22004883_22007130.databinding.MicTimerDialogBinding
import pt.ulusofona.deisi.cm2223.g22004883_22007130.animations.AnimationHandler
import pt.ulusofona.deisi.cm2223.g22004883_22007130.data.MovieRepository
import java.util.*

class MainActivity : AppCompatActivity(),NetworkStateListener {

    companion object{
        /*Este stack serve para sabermos qual o fragmento que se encontrava ativo antes de se pressionar
        * o onBack para conseguirmos dar highlight ao icon depois do onBackPressed, tambem serve para
        * as animaçoes.
        *
        * A lista serve para as animaçoes dos swipes right or left*/
        var fragementsInBottomNavigatorMenuStack: MutableList<Int> = mutableListOf()
        var fragmentsListId : List<Int> = listOf(R.id.home,R.id.movies,R.id.more_pages,R.id.registration)
        var trailerId = ""
        fun push(fragmentId : Int) {
            fragementsInBottomNavigatorMenuStack.add(fragementsInBottomNavigatorMenuStack.size,fragmentId)
        }

        fun pop() {
            fragementsInBottomNavigatorMenuStack.removeLast()
        }
    }

    lateinit var binding: ActivityMainBinding
    private var speechRecognizer: SpeechRecognizer? = null
    private val repository = MovieRepository.getInstance()
    private lateinit var networkListener: NetworkListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //iniciamos a app na pagina home
        NavigationManager.goToHomeFragment(supportFragmentManager)
        push(R.id.home)
        askLocationPermission()
        //ConnectivityUtil.registerNetworkListener(this, this)
        networkListener = NetworkListener(this)
        networkListener.startListening(this)
        if (!ConnectivityUtil.isOnline(this)) {
            binding.noWifi.isVisible = true
        }
    }


    override fun onStart() {
        super.onStart()
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            Log.d("nao tem permissoes de mic","")
            getMicPermission()
        }
        /*e necessario ca estar senao um icon aparece a toa no meio da barra, esse icon serve para
         espaçamento dos icones da bottoom navigator bar*/
        binding.bottomNavigator.background = null
        binding.bottomNavigator.menu[2].isEnabled = false //desativa o icon do bottom navigator do meio, pois ele serve apenas para separar os botoes
        binding.bottomNavigator.setOnItemSelectedListener { onClickNavigationItem(it) }
        binding.fab.setOnClickListener { micDialog() }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 1 && grantResults.isNotEmpty()) {
            Log.d("permission Granted","")
        }
    }

    private fun openSameScreen(id :Int) {
        if(fragementsInBottomNavigatorMenuStack.last() == id) {
            pop()
            supportFragmentManager.popBackStack()
            AnimationHandler.noAnimation = true
        }
    }

    private fun onClickNavigationItem(item: MenuItem): Boolean {
        openSameScreen(item.itemId)
        push(item.itemId)
        when (item.itemId) {
            R.id.movies -> NavigationManager.goToMoviesListFragment(supportFragmentManager)
            R.id.home -> NavigationManager.goToHomeFragment(supportFragmentManager)
            R.id.more_pages -> NavigationManager.goToMorePages(supportFragmentManager)
            R.id.registration -> NavigationManager.goToRegistrationFragment(supportFragmentManager)
        }
        return true
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            confirmExitAppDialog()
        } else {
            super.onBackPressed()
            pop()
            binding.bottomNavigator.menu.findItem(fragementsInBottomNavigatorMenuStack.last()).isChecked = true//highlight do icon
        }
    }

    private fun findActiveFragmentContext(): Context?{
        val listOfFragments = supportFragmentManager.fragments
        for (fragment in listOfFragments) {
            if(fragment != null && fragment.isVisible) {
                return fragment.context
            }
        }
        return null
    }

    private fun confirmExitAppDialog() {
        val builder = AlertDialog.Builder(findActiveFragmentContext())
        builder.setPositiveButton(R.string.yes) { _, _ ->
            finish()
        }
        builder.setNegativeButton(R.string.no) { _, _ -> }
        builder.setTitle(R.string.close_app)
        builder.setMessage(R.string.sure_close_app)
        builder.create().show()
    }

    private fun micDialog() {
        val builder = AlertDialog.Builder(findActiveFragmentContext())
        val inflater = LayoutInflater.from(findActiveFragmentContext())
        val dialogView = inflater.inflate(R.layout.mic_timer_dialog, null)
        //val speechView = dialogView.findViewById<TextView>(R.id.speech_receiver_textview)
        val bindingDialog: MicTimerDialogBinding = MicTimerDialogBinding.bind(dialogView)
        builder.setView(dialogView)
        builder.setPositiveButton("") { _, _ ->
            // Positive button clicked
        }
        builder.setNegativeButton("") { _, _ ->
            // Negative button clicked
        }
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogView.post{
            val layoutParams = dialogView.layoutParams
            layoutParams.height = 750// definir a altura do nosso dialog
            dialogView.layoutParams = layoutParams

        }

        speechRecorder(/*speechView,*/bindingDialog,dialog)
        /*var counter = 10
        counterTextView.text = "${getText(R.string.time)}:  $counter"

        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                if (counter > 0) {
                    counter--
                    counterTextView.text = "${getText(R.string.time)}:  $counter"
                    handler.postDelayed(this, 1000)
                } else {
                    dialog.dismiss()
                }
            }
        }
        handler.postDelayed(runnable, 1000)*/
        dialog.show()
    }

    private fun speechRecorder(/*speechView:TextView,*/bindingDialog: MicTimerDialogBinding,dialog:AlertDialog) {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        //speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
        speechRecognizer!!.setRecognitionListener(object: RecognitionListener {
            override fun onReadyForSpeech(p0: Bundle?) {
                Log.d("onReadyForSpeech",p0.toString())
                bindingDialog.speechReceiverTextview.text = ""
                bindingDialog.speechReceiverTextview.hint = ""
                /*speechView.text = ""
                speechView.hint = ""*/
            }

            override fun onBeginningOfSpeech() {
                //speechView.hint = getString(R.string.listening)
                bindingDialog.speechReceiverTextview.hint = getString(R.string.listening)
                Log.d("tou a oubir","")
            }

            override fun onRmsChanged(p0: Float) {
                Log.d("onRmsChanged",p0.toString())
            }

            override fun onBufferReceived(p0: ByteArray?) {
                Log.d("onBufferReceived",p0.toString())
            }

            override fun onEndOfSpeech() {
                Log.d("onEndOfSpeech","")
            }

            override fun onError(p0: Int) {
                Log.d("Erro",p0.toString())
                //speechView.hint = getString(R.string.reset)
                bindingDialog.speechReceiverTextview.hint = getString(R.string.reset)
                bindingDialog.resetMicBtn.setOnClickListener {
                    speechRecognizer!!.stopListening()
                    speechRecognizer!!.startListening(speechRecognizerIntent)
                }
            }

            override fun onResults(bundle: Bundle?) {
                val data = bundle?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                //speechView.text = data?.get(0)
                bindingDialog.speechReceiverTextview.text = data?.get(0)
                Log.d("oubi",data?.get(0).toString())
                bindingDialog.resetMicBtn.setOnClickListener {
                    speechRecognizer!!.stopListening()
                    speechRecognizer!!.startListening(speechRecognizerIntent)
                }
                bindingDialog.searchMicBtn.setOnClickListener {
                    searchMovieMic(data?.get(0).toString(),bindingDialog,dialog)
                }
            }

            override fun onPartialResults(p0: Bundle?) {
                Log.d("onPartialResults",p0.toString())
            }

            override fun onEvent(p0: Int, p1: Bundle?) {
                Log.d("onEvent",p0.toString())
            }
        })
        speechRecognizer!!.startListening(speechRecognizerIntent)

    }

    fun searchMovieMic(movieName : String,bindingDialog: MicTimerDialogBinding,dialog: AlertDialog) {
        repository.getMoviesSeenByUser {
            movies ->
            var canShowMovieNotFoundMessage = true
            movies.forEach { if(it.title.lowercase() == movieName.lowercase()) {
                pushMovieDetail(it.getImdbID(), dialog)
                canShowMovieNotFoundMessage = false
            } }
            if(canShowMovieNotFoundMessage){
                //dialogSpeechView.text = "Movie not found"
                bindingDialog.speechReceiverTextview.text = getString(R.string.mic_movie_not_found)
            }
        }
    }

    fun pushMovieDetail(movieId:String,dialog: AlertDialog) {
        dialog.dismiss()
        push(R.id.movies)
        NavigationManager.goToMovieDetailFragment(supportFragmentManager,movieId)
    }

    private fun getMicPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.RECORD_AUDIO), 1
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        networkListener.stopListening()
    }
    /*override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val lastFragmentOnRotate = savedInstanceState.getInt("lastFragmentOnRotate")
        if(lastFragmentOnRotate == R.id.movies) {
            push(lastFragmentOnRotate)
        }else{
            push(R.id.more_pages)//e preciso isto para nao rebentar no onBackPressed
        }
        when(lastFragmentOnRotate){
            R.id.movies -> NavigationManager.goToMoviesListFragment(supportFragmentManager)
            2131296532 -> NavigationManager.goToExtraPage(supportFragmentManager)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val currentFragmentName = supportFragmentManager.findFragmentById(R.id.frame)?.javaClass?.simpleName
        if(supportFragmentManager.findFragmentById(R.id.frame)?.javaClass?.simpleName == "ExtraFragment"){
            outState.putInt("lastFragmentOnRotate", 2131296532)
        }else {
            outState.putInt("lastFragmentOnRotate", fragementsInBottomNavigatorMenuStack.last())
        }
        super.onSaveInstanceState(outState)
    }*/

    private fun askLocationPermission() {
        permissionsBuilder(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION).build().send { result ->
            if (result.allGranted()) {
                Log.d("Iniciei o FusedLocation","")
                pt.ulusofona.deisi.cm2223.g22004883_22007130.FusedLocation.start(this)
                // Este if já cá estava antes, para garantir que ficamos no
                // ecrã em caso de ocorrer uma rotação
            } else {
                finish()
            }
        }
    }

    override fun onNetworkAvailable() {
        CoroutineScope(Dispatchers.Main).launch {
           binding.noWifi.isVisible = false
        }
        Log.d("Got Internet","")
    }

    override fun onNetworkLost() {
        CoroutineScope(Dispatchers.Main).launch {
            binding.noWifi.isVisible = true
        }
        Log.d("Internet lost","")
    }

    override fun onNetworkUnavailable() {
        Log.d("Internet unavailable","")
    }

}