import com.example.intranetredm.io.response.LoginResponse
import com.example.intranetredm.model.LoginRequest
import com.example.intranetredm.model.User
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
interface ApiService {
    @POST("login")
    fun postLogin(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("logout")
    fun postLogout(@Header(value = "Autorization") autHeader:String):
            Call<Void>

    companion object Factory {
        private const val BASE_URL = "http://192.168.101.77:3000/"

        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}
