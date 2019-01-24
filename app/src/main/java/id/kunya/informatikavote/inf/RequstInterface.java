package id.kunya.informatikavote.inf;

import java.util.List;
import java.util.Map;

import id.kunya.informatikavote.model.Candidates;
import id.kunya.informatikavote.model.Data;
import id.kunya.informatikavote.model.Posts;
import id.kunya.informatikavote.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by muhammad on 12/12/18.
 */

public interface RequstInterface {

    @FormUrlEncoded
    @POST("apivote/api.php")
    Call<User> signIn(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("apivote/cek_voting.php")
    Call<User> checkVote(@Field("nim") String nim);

    @FormUrlEncoded
    @POST("apivote/voting.php")
    Call<User> voting(@Field("idkandidat") String idkandidat, @Field("nim") String nim);

    @GET("apivote/datakandidat.php")
    Call<Data> getCandidate();

    @GET("wp-json/wp/v2/posts")
    Call<List<Posts>> getKategoriId(@QueryMap Map<String, String> options);

    @GET("wp-json/wp/v2/media/{taskId}")
    Call<Posts> getImage(@Path("taskId") String taskId);

    @GET("wp-json/wp/v2/posts?categories=4&per_page=5")
    Call<List<Posts>> getPopular();

    @GET("wp-json/wp/v2/posts")
    Call<List<Posts>> getGeneral(@Query("categories") String id);

}
