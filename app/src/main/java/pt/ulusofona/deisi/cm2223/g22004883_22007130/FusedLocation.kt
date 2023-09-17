package pt.ulusofona.deisi.cm2223.g22004883_22007130

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.google.android.gms.location.*

@SuppressLint("MissingPermission")
class FusedLocation private constructor(context: Context) : LocationCallback() {

    private val TAG = pt.ulusofona.deisi.cm2223.g22004883_22007130.FusedLocation::class.java.simpleName

    // Intervalos de tempo em que a localização é verificada, 20 segundos
    private val TIME_BETWEEN_UPDATES = 20 * 1000L

    // Este atributo será utilizado para acedermos à API da Fused Location
    private var client = FusedLocationProviderClient(context)

    // Configurar a precisão e os tempos entre atualizações da localização
    private var locationRequest = LocationRequest.create().apply {
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        interval = TIME_BETWEEN_UPDATES
    }

    init {

        // Instanciar o objeto que permite definir as configurações
        val locationSettingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .build()

        // Aplicar as configurações ao serviço de localização
        LocationServices.getSettingsClient(context)
            .checkLocationSettings(locationSettingsRequest)

        client.requestLocationUpdates(
            locationRequest,
            this, Looper.getMainLooper()
        )

    }

    // Este método é invocado sempre que a posição se alterar
    override fun onLocationResult(locationResult: LocationResult) {
        //Log.i(TAG, locationResult?.lastLocation.toString())
        val lastLocation = locationResult.lastLocation
        pt.ulusofona.deisi.cm2223.g22004883_22007130.FusedLocation.Companion.currentLatitude = lastLocation.latitude
        pt.ulusofona.deisi.cm2223.g22004883_22007130.FusedLocation.Companion.currentLongitude = lastLocation.longitude
        pt.ulusofona.deisi.cm2223.g22004883_22007130.FusedLocation.Companion.notifyListeners(
            locationResult
        )
    }



    companion object {
        // Se quisermos ter vários listeners isto tem de ser uma lista
        private var listener: pt.ulusofona.deisi.cm2223.g22004883_22007130.OnLocationChangedListener? = null
        private var instance: pt.ulusofona.deisi.cm2223.g22004883_22007130.FusedLocation? = null

        fun registerListener(listener: pt.ulusofona.deisi.cm2223.g22004883_22007130.OnLocationChangedListener) {
            pt.ulusofona.deisi.cm2223.g22004883_22007130.FusedLocation.Companion.listener = listener
        }

        var currentLatitude = 0.0
        var currentLongitude = 0.0

        fun unregisterListener() {
            pt.ulusofona.deisi.cm2223.g22004883_22007130.FusedLocation.Companion.listener = null
        }

        // Se tivermos vários listeners, temos de os notificar com um forEach
        fun notifyListeners(locationResult: LocationResult) {
            val location = locationResult.lastLocation
            pt.ulusofona.deisi.cm2223.g22004883_22007130.FusedLocation.Companion.listener?.onLocationChanged(location.latitude, location.longitude)
        }

        // Só teremos uma instância em execução
        fun start(context: Context) {
            pt.ulusofona.deisi.cm2223.g22004883_22007130.FusedLocation.Companion.instance =
                if (pt.ulusofona.deisi.cm2223.g22004883_22007130.FusedLocation.Companion.instance == null) pt.ulusofona.deisi.cm2223.g22004883_22007130.FusedLocation(
                    context
                )
                else pt.ulusofona.deisi.cm2223.g22004883_22007130.FusedLocation.Companion.instance
        }
    }


}