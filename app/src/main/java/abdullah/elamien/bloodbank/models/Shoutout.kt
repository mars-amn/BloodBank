package abdullah.elamien.bloodbank.models

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat

class Shoutout {
    var name: String? = null
    var email: String? = null
    var phone: String? = null
    var specificAddress: String? = null
    var description: String? = null
    var city: String? = null
    var bloodType: String? = null
    var image: String? = null
    private var date: Timestamp? = null

    fun getDate(): String {
        val timePosted = date!!.toDate()
        val df = SimpleDateFormat("dd MMM yyyy")
        return df.format(timePosted)
    }

    fun setDate(date: Timestamp) {
        this.date = date
    }

    companion object {
        @BindingAdapter("imageUrl")
        @JvmStatic
        fun loadShoutoutImage(view: ImageView, imageUrl: String) {
            Glide.with(view.context).load(imageUrl)
                    .into(view)
        }
    }
}
