package network;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



public class OmdbService {
    private static final String API_URL = "http://www.omdbapi.com";
    private static final String API_KEY = "4575b7d4";
    private static Omdbapi sOmdbApi;

    public static class ResultWithDetail {
        private List<Structure> movieDetailList;
        private String totalResults;
        private String Response;

        public ResultWithDetail(Result result) {
            this.totalResults = result.totalResults;
            this.Response = result.Response;
            movieDetailList = new ArrayList<>();
        }

        public void addToList(Structure detail) {
            movieDetailList.add(detail);
        }

        public List<Structure> getMovieDetailList() {
            return movieDetailList;
        }

        public String getTotalResults() {
            return totalResults;
        }

        public String getResponse() {
            return Response;
        }
    }

    public static class Result {
        public List<Movie> Search;
        public String totalResults;
        public String Response;

        @Override
        public String toString() {
            return "Result{" +
                    "Search=" + Search +
                    ", totalResults='" + totalResults + '\'' +
                    ", Response='" + Response + '\'' +
                    '}';
        }

    }

    public static class Movie {
        public String Title;
        public String Year;
        public String imdbID;
        public String Type;
        public String Poster;

        @Override
        public String toString() {
            return "\nMovie{" +
                    "Title='" + Title + '\'' +
                    ", Year='" + Year + '\'' +
                    ", imdbID='" + imdbID + '\'' +
                    ", Type='" + Type + '\'' +
                    ", Poster='" + Poster + '\'' +
                    '}';
        }

    }

    public static class Structure implements Parcelable {
        public String Title;
        public String Year;
        public String Rated;
        public String Released;
        public String Runtime;
        public String Genre;
        public String Director;
        public String Writer;
        public String Actors;
        public String Plot;
        public String Language;
        public String Country;
        public String Awards;
        public String Poster;
        public String Metascore;
        public String imdbRating;
        public String imdbVotes;
        public String imdbID;
        public String Type;
        public String Response;

        @Override
        public String toString() {
            return "Detail{" +
                    "Title='" + Title + '\'' +
                    ", Year='" + Year + '\'' +
                    ", Rated='" + Rated + '\'' +
                    ", Released='" + Released + '\'' +
                    ", Runtime='" + Runtime + '\'' +
                    ", Genre='" + Genre + '\'' +
                    ", Director='" + Director + '\'' +
                    ", Writer='" + Writer + '\'' +
                    ", Actors='" + Actors + '\'' +
                    ", Plot='" + Plot + '\'' +
                    ", Language='" + Language + '\'' +
                    ", Country='" + Country + '\'' +
                    ", Awards='" + Awards + '\'' +
                    ", Poster='" + Poster + '\'' +
                    ", Metascore='" + Metascore + '\'' +
                    ", imdbRating='" + imdbRating + '\'' +
                    ", imdbVotes='" + imdbVotes + '\'' +
                    ", imdbID='" + imdbID + '\'' +
                    ", Type='" + Type + '\'' +
                    ", Response='" + Response + '\'' +
                    '}';
        }

        protected Structure(Parcel in) {
            Title = in.readString();
            Year = in.readString();
            Rated = in.readString();
            Released = in.readString();
            Runtime = in.readString();
            Genre = in.readString();
            Director = in.readString();
            Writer = in.readString();
            Actors = in.readString();
            Plot = in.readString();
            Language = in.readString();
            Country = in.readString();
            Awards = in.readString();
            Poster = in.readString();
            Metascore = in.readString();
            imdbRating = in.readString();
            imdbVotes = in.readString();
            imdbID = in.readString();
            Type = in.readString();
            Response = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(Title);
            dest.writeString(Year);
            dest.writeString(Rated);
            dest.writeString(Released);
            dest.writeString(Runtime);
            dest.writeString(Genre);
            dest.writeString(Director);
            dest.writeString(Writer);
            dest.writeString(Actors);
            dest.writeString(Plot);
            dest.writeString(Language);
            dest.writeString(Country);
            dest.writeString(Awards);
            dest.writeString(Poster);
            dest.writeString(Metascore);
            dest.writeString(imdbRating);
            dest.writeString(imdbVotes);
            dest.writeString(imdbID);
            dest.writeString(Type);
            dest.writeString(Response);
        }

        @SuppressWarnings("unused")
        public static final Creator<Structure> CREATOR = new Creator<Structure>() {
            @Override
            public Structure createFromParcel(Parcel in) {
                return new Structure(in);
            }

            @Override
            public Structure[] newArray(int size) {
                return new Structure[size];
            }
        };
    }

    public interface Omdbapi {
        @GET("?type=movie")
        Call<Result> Result(
                @Query("s") String Title);

        @GET("?plot=full")
        Call<Structure> Structure(
                @Query("i") String ImdbId);
    }

    private static void setsOmdbApi() {
        if (sOmdbApi == null) {
            OkHttpClient.Builder httpClient =
                    new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    HttpUrl originalHttpUrl = original.url();

                    HttpUrl url = originalHttpUrl.newBuilder()
                            .addQueryParameter("apikey", API_KEY)
                            .build();

                    Request.Builder requestBuilder = original.newBuilder()
                            .url(url);

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            sOmdbApi = retrofit.create(Omdbapi.class);
        }
    }

    public static Result performSearch(String title) throws IOException {
        setsOmdbApi();

        Call<Result> call = sOmdbApi.Result(title);

        return call.execute().body();
    }

    public static Structure getDetail(String imdbId) throws IOException {
        setsOmdbApi();

        Call<Structure> call = sOmdbApi.Structure(imdbId);

        return call.execute().body();
    }
}
