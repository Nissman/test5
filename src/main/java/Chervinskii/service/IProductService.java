package Chervinskii.service;

import Chervinskii.dto.Product;
import retrofit2.Call;
import okhttp3.ResponseBody;
import retrofit2.http.*;

import java.util.ArrayList;

public interface IProductService {
    @GET("products")
    Call<ArrayList<Product>> getProducts();

    @GET("products/{id}")
    Call<Product> getProduct(@Path("id") Integer id);

    @PUT("products")
    Call<Product> putProduct(@Body Product product);

    @POST("products")
    Call<Product> createProduct(@Body Product product);

    @DELETE("products/{id}")
    Call<ResponseBody> deleteProduct(@Path("id") Integer id);
}
