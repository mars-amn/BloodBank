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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import abdullah.elamien.bloodbank.R;
import abdullah.elamien.bloodbank.databinding.ListItemShoutoutBinding;
import abdullah.elamien.bloodbank.models.Shoutout;

@SuppressWarnings("ALL")
public class ShoutoutAdapter extends RecyclerView.Adapter<ShoutoutAdapter.ShoutoutViewHolder> {
    private Context mContext;
    private List<Shoutout> mShoutouts;

    @Inject
    public ShoutoutAdapter(Context mContext) {
        this.mContext = mContext;
        this.mShoutouts = new ArrayList<>();
    }

    @NonNull
    @Override
    public ShoutoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemShoutoutBinding binding = ListItemShoutoutBinding.inflate(LayoutInflater.from(mContext), parent, false);
        return new ShoutoutViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoutoutViewHolder holder, int position) {
        holder.bind(mShoutouts.get(position));
    }

    @Override
    public int getItemCount() {
        return mShoutouts == null ? 0 : mShoutouts.size();
    }

    public void addShoutouts(List<Shoutout> shoutouts) {
        this.mShoutouts = shoutouts;
        notifyDataSetChanged();
    }

    public class ShoutoutViewHolder extends RecyclerView.ViewHolder {
        ListItemShoutoutBinding mBinding;

        public ShoutoutViewHolder(@NonNull ListItemShoutoutBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.setHandlers(this);
        }

        @SuppressWarnings("WeakerAccess")
        public void bind(Shoutout shoutout) {
            mBinding.setShoutout(shoutout);
            mBinding.setImageUrl(shoutout.getImage());
            mBinding.executePendingBindings();
        }

        public void onPhoneButtonClick(View view) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + mShoutouts.get(getAdapterPosition()).getPhone()));
            mContext.startActivity(intent);
        }

        public void onWhatsAppButtonClick(View view) {
            try {
                Intent sendIntent = new Intent("android.intent.action.MAIN");

                sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators("2" + mShoutouts.get(getAdapterPosition()).getPhone()) + "@s.whatsapp.net");
                mContext.startActivity(sendIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(mContext, mContext.getString(R.string.whatsapp_error_msg), Toast.LENGTH_SHORT).show();
            }
        }

        public void onSMSButtonClick(View view) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + mShoutouts.get(getAdapterPosition()).getPhone()));
            intent.putExtra("sms_body", mContext.getString(R.string.hi_share_shoutout_sms)
                    + mShoutouts.get(getAdapterPosition()).getName() + mContext.getString(R.string.can_help_share_shoutout_msg));
            mContext.startActivity(intent);
        }

        public void onShareButtonClick(View view) {
            ShareCompat.IntentBuilder.from((AppCompatActivity) mContext)
                    .setType("text/plain")
                    .setText(getSharedText())
                    .setChooserTitle(mContext.getString(R.string.share_title))
                    .startChooser();
        }

        private String getSharedText() {
            return mContext.getString(R.string.hey_there_share_shoutout_first_part) + mShoutouts.get(getAdapterPosition()).getName()
                    + mContext.getString(R.string.hey_thee_share_shoutout_second_part) +
                    mShoutouts.get(getAdapterPosition()).getPhone();
        }
    }
}
