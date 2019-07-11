package abdullah.elamien.bloodbank.models

import android.widget.ImageView

import androidx.databinding.BindingAdapter

import com.bumptech.glide.Glide

class Donor {

    var name: String? = null
    var phone: String? = null
    var image_: String? = null
    var address: String? = null
    var bloodType: String? = null
    var bio: String? = null
    var userId: String? = null
    var age: Int = 0

    companion object {

        @BindingAdapter("donorImage")
        @JvmStatic
        fun loadDonorImage(view: ImageView, imageUrl: String) {
            Glide.with(view.context)
                    .load(imageUrl)
                    .into(view)
        }
    }
}
