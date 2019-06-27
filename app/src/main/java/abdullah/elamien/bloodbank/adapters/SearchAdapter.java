package abdullah.elamien.bloodbank.adapters;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import javax.inject.Inject;

import abdullah.elamien.bloodbank.R;
import abdullah.elamien.bloodbank.databinding.ListItemSearchDonorsBinding;
import abdullah.elamien.bloodbank.models.Donor;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private Context mContext;
    private List<Donor> mDonors;

    @Inject
    public SearchAdapter(Context mContext, List<Donor> mDonors) {
        this.mContext = mContext;
        this.mDonors = mDonors;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemSearchDonorsBinding bindingView = ListItemSearchDonorsBinding.inflate(LayoutInflater.from(mContext), parent, false);
        return new SearchViewHolder(bindingView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.bind(mDonors.get(position));
    }

    @Override
    public int getItemCount() {
        return mDonors == null ? 0 : mDonors.size();
    }

    public void setDonors(List<Donor> donors) {
        mDonors = donors;
        notifyDataSetChanged();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {

        ListItemSearchDonorsBinding mBinding;

        public SearchViewHolder(@NonNull ListItemSearchDonorsBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bind(Donor donor) {
            mBinding.setHandler(this);
            mBinding.setDonor(donor);
            mBinding.setImageUrl(donor.getImage_());
            mBinding.executePendingBindings();
        }

        public void onCallButtonClick(View view) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + mDonors.get(getAdapterPosition()).getPhone()));
            mContext.startActivity(intent);
        }

        public void onWhatsAppButtonClick(View view) {
            try {
                Intent sendIntent = new Intent("android.intent.action.MAIN");

                sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(mDonors.get(getAdapterPosition()).getPhone()) + "@s.whatsapp.net");
                mContext.startActivity(sendIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(mContext, mContext.getString(R.string.whatsapp_error_msg), Toast.LENGTH_SHORT).show();
            }
        }

        public void onSMSButtonClick(View view) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + mDonors.get(getAdapterPosition()).getPhone()));
            intent.putExtra("sms_body", mContext.getString(R.string.hi_donor_sms_first_part)
                    + mDonors.get(getAdapterPosition()).getName() + mContext.getString(R.string.hi_donor_sms_second_part));
            mContext.startActivity(intent);

        }
    }
}
