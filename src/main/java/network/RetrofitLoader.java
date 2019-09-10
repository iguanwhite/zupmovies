package network;

import android.content.Context;

import android.util.Log;

import androidx.loader.content.AsyncTaskLoader;

import java.io.IOException;

public class RetrofitLoader extends AsyncTaskLoader<OmdbService.ResultWithDetail> {

    private final String mTitle;
    private OmdbService.ResultWithDetail mData;

    public RetrofitLoader(Context context, String title) {
        super(context);
        mTitle = title;
    }

    @Override
    public OmdbService.ResultWithDetail loadInBackground() {
        // get results from calling API
        try {
            OmdbService.Result result =  OmdbService.performSearch(mTitle);
            OmdbService.ResultWithDetail resultWithDetail = new OmdbService.ResultWithDetail(result);
            if(result.Search != null) {
                for(OmdbService.Movie movie: result.Search) {
                    resultWithDetail.addToList(OmdbService.getDetail(movie.imdbID));
                }
            }
            return  resultWithDetail;
        } catch(final IOException e) {
            Log.e("Error", "Error from api access", e);
        }
        return null;
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            deliverResult(mData);
        } else {
            forceLoad();
        }
    }


    @Override
    protected void onReset() {
        Log.d("Error", "onReset");
        super.onReset();
        mData = null;
    }

    @Override
    public void deliverResult(OmdbService.ResultWithDetail data) {
        if (isReset()) {
            return;
        }

        OmdbService.ResultWithDetail oldData = mData;
        mData = data;

        if (isStarted()) {
            super.deliverResult(data);
        }

    }
}
