package abdullah.elamien.bloodbank.adapters

import abdullah.elamien.bloodbank.R
import abdullah.elamien.bloodbank.databinding.ListItemShoutoutBinding
import abdullah.elamien.bloodbank.models.Shoutout
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.telephony.PhoneNumberUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class ShoutoutAdapter
constructor(private val mContext: Context) : RecyclerView.Adapter<ShoutoutAdapter.ShoutoutViewHolder>() {
    private var mShoutouts: List<Shoutout>? = null

    init {
        this.mShoutouts = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoutoutViewHolder {
        val binding = ListItemShoutoutBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return ShoutoutViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShoutoutViewHolder, position: Int) {
        holder.bind(mShoutouts!![position])
    }

    override fun getItemCount(): Int {
        return if (mShoutouts == null) 0 else mShoutouts!!.size
    }

    fun addShoutouts(shoutouts: List<Shoutout>) {
        this.mShoutouts = shoutouts
        notifyDataSetChanged()
    }

    inner class ShoutoutViewHolder(private var mBinding: ListItemShoutoutBinding) : RecyclerView.ViewHolder(mBinding.root) {

        private val sharedText: String
            get() = (mBinding.listItemShoutoutDate.context.getString(R.string.hey_there_share_shoutout_first_part) + mShoutouts!![adapterPosition].name
                    + mContext.getString(R.string.hey_thee_share_shoutout_second_part) +
                    mShoutouts!![adapterPosition].phone)

        init {
            mBinding.handlers = this
        }

        fun bind(shoutout: Shoutout) {
            mBinding.shoutout = shoutout
            mBinding.imageUrl = shoutout.image
            mBinding.executePendingBindings()
        }

        fun onPhoneButtonClick(view: View) {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:" + mShoutouts!![adapterPosition].phone!!)
            mContext.startActivity(intent)
        }

        fun onWhatsAppButtonClick(view: View) {
            try {
                val sendIntent = Intent("android.intent.action.MAIN")

                sendIntent.component = ComponentName("com.whatsapp", "com.whatsapp.Conversation")
                sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators("2" + mShoutouts!![adapterPosition].phone!!) + "@s.whatsapp.net")
                mContext.startActivity(sendIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(mContext, mContext.getString(R.string.whatsapp_error_msg), Toast.LENGTH_SHORT).show()
            }

        }

        fun onSMSButtonClick(view: View) {
            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + mShoutouts!![adapterPosition].phone!!))
            intent.putExtra("sms_body", mContext.getString(R.string.hi_share_shoutout_sms)
                    + mShoutouts!![adapterPosition].name + mContext.getString(R.string.can_help_share_shoutout_msg))
            mContext.startActivity(intent)
        }

        fun onShareButtonClick(view: View) {
            ShareCompat.IntentBuilder.from(mContext as AppCompatActivity)
                    .setType("text/plain")
                    .setText(sharedText)
                    .setChooserTitle(mContext.getString(R.string.share_title))
                    .startChooser()
        }
    }
}
