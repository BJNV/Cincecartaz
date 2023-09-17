package pt.ulusofona.deisi.cm2223.g22004883_22007130.data

import android.content.res.AssetManager
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class Cinema(
    private val id: String,
    private val name: String,
    private val provider: String,
    private val latitude: String,
    private val longitude: String,
    private val address: String,
    private val postcode: String,
    private val county: String,
) {

    companion object {
        var cinemas = mutableListOf<Cinema>()
        fun getIDByName(name: String): String {
            cinemas.forEach { if (it.getName() == name) return it.getID() }
            return ""
        }

        fun readJsonFile(assets:AssetManager){
            CoroutineScope(Dispatchers.IO).launch{
                try {
                    val inputStream = assets.open("cinemas.json")
                    val json = inputStream.bufferedReader().use { it.readText() }
                    val output = JSONObject(json)
                    val jsonArray = output.getJSONArray("cinemas") as JSONArray
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val cinema = Cinema(
                            jsonObject.getString("cinema_id"),
                            jsonObject.getString("cinema_name"),
                            jsonObject.getString("cinema_provider"),
                            jsonObject.getString("latitude"),
                            jsonObject.getString("longitude"),
                            jsonObject.getString("address"),
                            jsonObject.getString("postcode"),
                            jsonObject.getString("county"),
                        )
                        cinemas.add(cinema)
                    }
                } catch (e: Exception) {
                    e.message?.let { Log.d("Exception a ler o ficheiro de cinemas", it) }
                }
            }
        }

    }

    fun getID(): String = id
    fun getName(): String = name
    fun getLatitude(): String = latitude
    fun getLongitude(): String = longitude

}