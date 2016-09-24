package com.vitasoy.catint.vitasoy.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.vitasoy.catint.vitasoy.R;
import com.vitasoy.catint.vitasoy.repo.TorrentInfo;
import com.vitasoy.catint.vitasoy.repo.TorrentPage;

import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Subscriber;

/**
 * Created by yodazone on 16/9/5.
 * {@link android.support.v7.widget.RecyclerView.Adapter}
 */
public class SearchResultListAdapter extends RecyclerView.Adapter {

    private List<TorrentInfo> mResultList;
    private TorrentPage mResult;

    private Subscriber<? super Map<String, String>> subscriber;

    public SearchResultListAdapter() {
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_list, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        if (subscriber != null && mResultList.size() > 5 && position > mResultList.size() - 5 && lastEmitQuest == nextExpectedPage - 1) {
//            Map<String, String> request = new HashMap<>();
//            request.put("name", "*");
//            request.put("number", String.valueOf(nextExpectedPage));
//            lastEmitQuest = nextExpectedPage;
//            subscriber.onNext(request);
//            Log.d("SearchResultListAdapter", "onUpdate" + Thread.currentThread().toString());
//        }
        if (holder instanceof MainHolder) {
            MainHolder mainHolder = (MainHolder) holder;
            mainHolder.name.setText(mResultList.get(position).getName());
            mainHolder.size.setText(mResultList.get(position).getSize());
            mainHolder.seed.setText(mResultList.get(position).getSeed());
            mainHolder.leech.setText(mResultList.get(position).getLeech());
            mainHolder.ages.setText(mResultList.get(position).getAges());
            mainHolder.magnet.setTag(mResultList.get(position).getMagnet());
            mainHolder.extention.setText(FormatToTag(mResultList.get(position).getExtension()));
            mainHolder.resolution.setText(FormatToTag(mResultList.get(position).getResolution()));
            mainHolder.vcode.setText(FormatToTag(mResultList.get(position).getVcode()));
            mainHolder.acode.setText(FormatToTag(mResultList.get(position).getAcode()));
            mainHolder.quality.setText(FormatToTag(mResultList.get(position).getQuality()));
        }
    }

    private String FormatToTag(String s) {
        if (s == null || s.equals(""))
            return "";
        return " " + s + " ";
    }

    public void setSubscriber(Subscriber<? super Map<String, String>> subscriber) {
        this.subscriber = subscriber;
    }

    public void setResultData(TorrentPage result) {
        mResult = result;
        mResultList = result.getContent();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mResultList == null ? 0 : mResultList.size();
    }

    public static class MainHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.name)
        TextView name;
        @InjectView(R.id.size)
        TextView size;
        @InjectView(R.id.seed)
        TextView seed;
        @InjectView(R.id.leech)
        TextView leech;
        @InjectView(R.id.ages)
        TextView ages;
        @InjectView(R.id.magnet)
        ImageButton magnet;
        @InjectView(R.id.extention)
        TextView extention;
        @InjectView(R.id.vcode)
        TextView vcode;
        @InjectView(R.id.acode)
        TextView acode;
        @InjectView(R.id.resolution)
        TextView resolution;
        @InjectView(R.id.quality)
        TextView quality;

        public MainHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }

    }
}
