package Chervinskii.tests;

import Chervinskii.dto.Category;
import Chervinskii.dto.Product;
import Chervinskii.enums.CategoryType;
import Chervinskii.service.ICategoryService;
import Chervinskii.service.IProductService;
import Chervinskii.utils.PrettyLogger;
import Chervinskii.utils.RetrofitUtils;
import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import retrofit2.Response;
import retrofit2.Retrofit;
import java.io.IOException;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductTests {
    static Retrofit client;
    static IProductService productService;
    static ICategoryService categoryService;
    static int id_1,id_2,id_3;
    Faker faker = new Faker();
    Product product;
    Product productZeroPrice;
    Product productNoPrice;
    Product productIncorrectCategoryTitle;
    PrettyLogger prettyLogger = new PrettyLogger();

    @BeforeAll
    static void beforeAll(){
        client = RetrofitUtils.getRetrofit();
        productService = client.create(IProductService.class);
        categoryService = client.create(ICategoryService.class);
    }

    @BeforeEach
    void setUp() {
        product = new Product()
                .withTitle(faker.food().dish())
                .withPrice((int) ((Math.random()+1)*100))
                .withCategoryTitle(CategoryType.FOOD.getTitle());
        productZeroPrice = new Product()
                .withTitle(faker.food().dish())
                .withPrice(0)
                .withCategoryTitle(CategoryType.FOOD.getTitle());
        productNoPrice = new Product()
                .withTitle(faker.food().dish())
                .withCategoryTitle(CategoryType.FOOD.getTitle());
        productIncorrectCategoryTitle= new Product()
                .withTitle(faker.food().dish())
                .withCategoryTitle("");
    }

    @Order(1)
    @Test
    void getProductsTest() throws  IOException {
        Response<ArrayList<Product>> response = productService.getProducts().execute();
        assertThat(response.body(),is(notNullValue()));
        assertThat(response.body().get(0).getId(), is(notNullValue()));
    }

    @Order(2)
    @Test
    void postProductTest() throws  IOException {
        Response<Product> response = productService.createProduct(product).execute();
        id_1 = response.body().getId();
        assertThat(response.body().getTitle(), equalTo(product.getTitle()));
        assertThat(response.body().getPrice(), equalTo(product.getPrice()));
        assertThat(response.body().getCategoryTitle(),equalTo(product.getCategoryTitle()));
    }

    @Order(3)
    @Test
    void postProductIncorrectCategoryTitle() throws IOException {
        Response<Product> response = productService.createProduct(productIncorrectCategoryTitle).execute();
        assertThat(response.code(),equalTo(500));
    }

    @Order(4)
    @Test
    void postProductNoPriceTest() throws  IOException {
        Response<Product> response = productService.createProduct(productNoPrice).execute();
        id_2 = response.body().getId();
        prettyLogger.log(response.body().toString());
        assertThat(response.body().getPrice(), equalTo(0));
    }

    @Order(5)
    @Test
    void postProductZeroPriceTest() throws  IOException {
        Response<Product> response = productService.createProduct(productZeroPrice).execute();
        id_3 = response.body().getId();
        prettyLogger.log(response.body().toString());
        assertThat(response.body().getPrice(), equalTo(productZeroPrice.getPrice()));
    }

    @Order(6)
    @Test
    void postProductZeroIdTest() throws  IOException {
        productZeroPrice.setId(0);
        Response<Product> response = productService.createProduct(productZeroPrice).execute();
        assertThat(response.code(),equalTo(400));
    }

    @Order(7)
    @Test
    void postProductExistIdTest() throws  IOException {
        productZeroPrice.setId(id_1);
        Response<Product> response = productService.createProduct(productZeroPrice).execute();
        assertThat(response.code(),equalTo(400));
    }

    @Order(8)
    @Test
    void updateProductWithIdTest() throws IOException{
        productZeroPrice.setId(id_1);
        Response<Product> response = productService.putProduct(productZeroPrice).execute();
        assertThat(response.body().getPrice(), equalTo(productZeroPrice.getPrice()));
        assertThat(response.body().getId(), equalTo(id_1));
        assertThat(response.body().getTitle(), equalTo(productZeroPrice.getTitle()));
        assertThat(response.body().getCategoryTitle(),equalTo(productZeroPrice.getCategoryTitle()));
    }

    @Order(9)
    @Test
    void updateProductSomeIdTest() throws IOException {
        productZeroPrice.setId(1547879);
        Response<Product> response = productService.putProduct(productZeroPrice).execute();
        assertThat(response.code(),equalTo(400));
    }

    @Order(10)
    @Test
    void updateProductNoIdTest() throws IOException {
        Response<Product> response = productService.putProduct(product).execute();
        assertThat(response.code(),equalTo(400));
    }

    @Order(11)
    @Test
    void getProductTest() throws IOException {
        Response<Product> response = productService.getProduct(id_1).execute();
        assertThat(response.code(), equalTo(200));
    }

    @Order(12)
    @Test
    void getProductByNoExistIdTest() throws IOException {
        Response<Product> response = productService.getProduct(0).execute();
        assertThat(response.code(), equalTo(404));
    }

    @Order(13)
    @Test
    void deleteProduct_1() throws IOException {
        Response<ResponseBody> response = productService.deleteProduct(id_1).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.code(), equalTo(200));
    }

    @Order(14)
    @Test
    void deleteProduct_2() throws IOException {
        Response<ResponseBody> response = productService.deleteProduct(id_2).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.code(), equalTo(200));
    }

    @Order(15)
    @Test
    void deleteProduct_3() throws IOException {
        Response<ResponseBody> response = productService.deleteProduct(id_3).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.code(), equalTo(200));
    }

    @Order(16)
    @Test
    void deleteProductNoExistID() throws IOException {
        Response<ResponseBody> response = productService.deleteProduct(0).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
        assertThat(response.code(), equalTo(500));
    }

    @Order(17)
    @Test
    void getCategoryByIdTest() throws IOException {
        Integer id = CategoryType.FOOD.getId();
        Response<Category> response = categoryService
                .getCategory(id)
                .execute();
        prettyLogger.log(response.body().toString());
        assertThat(response.body().getTitle(), equalTo(CategoryType.FOOD.getTitle()));
        assertThat(response.body().getId(),equalTo(id));
    }

    @Order(18)
    @Test
    void getCategoryByNoExistIdTest() throws IOException {
        Integer id = 0;
        Response<Category> response = categoryService
                .getCategory(id)
                .execute();
        assertThat(response.code(), equalTo(404));
    }
}
