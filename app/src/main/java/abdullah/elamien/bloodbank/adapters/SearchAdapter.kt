package abdullah.elamien.bloodbank.adapters

import abdullah.elamien.bloodbank.R
import abdullah.elamien.bloodbank.databinding.ListItemSearchDonorsBinding
import abdullah.elamien.bloodbank.models.Donor
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
import androidx.recyclerview.widget.RecyclerView


class SearchAdapter
constructor(private val mContext: Context) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {
    private var mDonors: List<Donor>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val bindingView = ListItemSearchDonorsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(bindingView)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(mDonors!![position])
    }

    override fun getItemCount(): Int {
        return if (mDonors == null) 0 else mDonors!!.size
    }

    fun setDonors(donors: List<Donor>) {
        mDonors = donors
        notifyDataSetChanged()
    }

    inner class SearchViewHolder(private var mBinding: ListItemSearchDonorsBinding) : RecyclerView.ViewHolder(mBinding.root) {

        internal fun bind(donor: Donor) {
            mBinding.handler = this
            mBinding.donor = donor
            mBinding.imageUrl = donor.image_
            mBinding.executePendingBindings()
        }

        fun onCallButtonClick(view: View) {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:" + mDonors!![adapterPosition].phone!!)
            mContext.startActivity(intent)
        }

        fun onWhatsAppButtonClick(view: View) {
            try {
                val sendIntent = Intent("android.intent.action.MAIN")

                sendIntent.component = ComponentName("com.whatsapp", "com.whatsapp.Conversation")
                sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(mDonors!![adapterPosition].phone) + "@s.whatsapp.net")
                mContext.startActivity(sendIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(mContext, mContext.getString(R.string.whatsapp_error_msg), Toast.LENGTH_SHORT).show()
            }

        }

        fun onSMSButtonClick(view: View) {
            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + mDonors!![adapterPosition].phone!!))
            intent.putExtra("sms_body", mContext.getString(R.string.hi_donor_sms_first_part)
                    + mDonors!![adapterPosition].name + mContext.getString(R.string.hi_donor_sms_second_part))
            mContext.startActivity(intent)

        }
    }
}
