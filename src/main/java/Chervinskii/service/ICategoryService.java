package Chervinskii.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import Chervinskii.dto.Category;

public interface ICategoryService {
    @GET("categories/{id}")
    Call<Category> getCategory(@Path("id") int id);

}
