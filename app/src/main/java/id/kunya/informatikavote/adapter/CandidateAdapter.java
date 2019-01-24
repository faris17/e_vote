package id.kunya.informatikavote.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import id.kunya.informatikavote.MainActivity;
import id.kunya.informatikavote.R;
import id.kunya.informatikavote.URLs;
import id.kunya.informatikavote.inf.RequstInterface;
import id.kunya.informatikavote.model.Candidates;
import id.kunya.informatikavote.model.User;
import id.kunya.informatikavote.utils.PreferencesHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by muhammad on 10/06/17.
 */

public class CandidateAdapter extends RecyclerView.Adapter<CandidateAdapter.ViewHolder> {

    private Retrofit retrofit;
    private RequstInterface request;
    private ProgressDialog progress;
    private String TAG="VOTE";
    private String status, voted;

    private List<Candidates> candidates;
    private Context context;
    private TextView selectedItem;
    private RecyclerView rv_candidate;

    public CandidateAdapter(String status, String voted, List<Candidates> candidates, Context context, TextView selectedItem, RecyclerView rv_candidate, ProgressDialog progress) {
        this.status = status;
        this.voted = voted;
        this.candidates = candidates;
        this.selectedItem = selectedItem;
        this.context = context;
        this.rv_candidate = rv_candidate;
        this.progress = progress;
    }

    @Override
    public CandidateAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()) .inflate(R.layout.item_list_candidate, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CandidateAdapter.ViewHolder viewHolder, final int i) {

        retrofit = new Retrofit.Builder()
                .baseUrl(URLs.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        request = retrofit.create(RequstInterface.class);

        final String getNIM=PreferencesHelper.getInstance(context.getApplicationContext()).getLoginID();

        setImageCandidate(viewHolder.img_ketua, viewHolder.img_wakil, i);

        if(status!=null){
            if(status.equals("Y")){
                if (voted.equals(candidates.get(i).getIdkandidat())) {
                    viewHolder.btn_vote.setBackgroundColor(Color.parseColor("#ffb82b"));
                    viewHolder.btn_vote.setText("VOTED!");
                    viewHolder.btn_vote.setEnabled(false);
                } else {
                    viewHolder.btn_vote.setBackgroundColor(Color.parseColor("#b9b9b9"));
                    viewHolder.btn_vote.setEnabled(false);
                }
            }
        }

        viewHolder.img_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfo(view, i);
            }
        });

        viewHolder.btn_vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<User> api = request.voting(candidates.get(i).getIdkandidat(), getNIM);
                progress.show();

                api.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        User user = response.body();

                        if (response.body().getSuccess().equals("1")) {
                            selectedItem.setText("V" + i);
                            if (selectedItem.getText().toString().equals("V" + i)) {
                                viewHolder.btn_vote.setText("Voted!");

                                for (int o = 0; o < candidates.size(); o++) {
                                    if (i == o) {
                                        rv_candidate.findViewHolderForAdapterPosition(o).itemView.findViewById(R.id.voted).setBackgroundColor(Color.parseColor("#ffb82b"));
                                        rv_candidate.findViewHolderForAdapterPosition(o).itemView.findViewById(R.id.voted).setEnabled(false);
                                    } else {
                                        rv_candidate.findViewHolderForAdapterPosition(o).itemView.findViewById(R.id.voted).setBackgroundColor(Color.parseColor("#b9b9b9"));
                                        rv_candidate.findViewHolderForAdapterPosition(o).itemView.findViewById(R.id.voted).setEnabled(false);
                                    }
                                }
                            }
                            Toast.makeText(context, "" + user.getMessage(), Toast.LENGTH_LONG).show();
                            Log.d(TAG, "Vote Info : " + user.getMessage());
                        }else{
                            Toast.makeText(context, "" + user.getMessage(), Toast.LENGTH_LONG).show();
                            Log.d(TAG, "Vote Info : " + user.getMessage());
                        }

                        progress.dismiss();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        t.printStackTrace();
                        progress.dismiss();
                    }
                });

            }
        });

    }

    private void setImageCandidate(ImageView ketua, ImageView wakil, int position){

        Glide.with(context).load(URLs.URL+"voting/assets/upload/" +candidates.get(position).getFotoprofilketua())
                .thumbnail(0.5f)
                .crossFade()
                .placeholder(R.drawable.calon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ketua);

        Glide.with(context).load(URLs.URL+"voting/assets/upload/" +candidates.get(position).getFotoprofilwakil())
                .thumbnail(0.5f)
                .crossFade()
                .placeholder(R.drawable.calon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(wakil);
    }

    private void showInfo(View view, int position) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder((Activity) view.getContext());
        View mView = LayoutInflater.from(context).inflate(R.layout.md_item_info, null);

        TextView tahun = mView.findViewById(R.id.tahun);
        TextView visi = mView.findViewById(R.id.visi);
        TextView misi = mView.findViewById(R.id.misi);
        ImageView foto_ketua = mView.findViewById(R.id.ketua);
        ImageView foto_wakil = mView.findViewById(R.id.wakil);
        TextView nim_ketua = mView.findViewById(R.id.nim_ketua);
        TextView nim_wakil = mView.findViewById(R.id.nim_wakil);
        TextView nama_ketua = mView.findViewById(R.id.nama_ketua);
        TextView nama_wakil = mView.findViewById(R.id.nama_wakil);
        TextView kembali = mView.findViewById(R.id.kembali);


        setImageCandidate(foto_ketua, foto_wakil, position);
        tahun.setText("Tahun : "+candidates.get(position).getTahun());
        visi.setText("Visi : "+candidates.get(position).getVisi());
        misi.setText("Misi : "+candidates.get(position).getMisi());
        nim_ketua.setText("\n"+candidates.get(position).getNim_calonketua());
        nim_wakil.setText("\n"+candidates.get(position).getNim_calonwakil());
        nama_ketua.setText(candidates.get(position).getNama_ketua());
        nama_wakil.setText(candidates.get(position).getNama_wakil());

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return candidates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView btn_vote;
        private ImageView img_ketua, img_wakil, img_info;
        public ViewHolder(View view) {
            super(view);

            img_ketua = view.findViewById(R.id.ketua);
            img_wakil = view.findViewById(R.id.wakil);
            btn_vote = view.findViewById(R.id.voted);
            img_info = view.findViewById(R.id.info);

        }
    }
}