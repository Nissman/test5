package Chervinskii.utils;

import Chervinskii.db.model.Products;
import Chervinskii.dto.Product;
import Chervinskii.enums.CategoryType;
import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import Chervinskii.db.dao.CategoriesMapper;
import Chervinskii.db.dao.ProductsMapper;
import Chervinskii.db.model.Categories;
import Chervinskii.db.model.CategoriesExample;
import Chervinskii.db.model.ProductsExample;

import java.io.IOException;
import java.util.List;

@UtilityClass
public class DbUtils {
    private static  String resource = "mybatisConfig.xml";
    static Faker faker = new Faker();
    private static SqlSession getSqlSession() throws IOException {
        SqlSessionFactory sqlSessionFactory;
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream(resource));
        return sqlSessionFactory.openSession(true);
    }
    @SneakyThrows
    public static CategoriesMapper getCategoriesMapper(){
        return getSqlSession().getMapper(CategoriesMapper.class);
    }
    @SneakyThrows
    public static ProductsMapper getProductsMapper() {
        return getSqlSession().getMapper(ProductsMapper.class);
    }

    public static void createNewCategory(CategoriesMapper categoriesMapper) {
        Categories newCategory = new Categories();
        newCategory.setTitle(faker.animal().name());
        categoriesMapper.insert(newCategory);
    }

    public static Integer countCategories(CategoriesMapper categoriesMapper) {
        long categoriesCount = categoriesMapper.countByExample(new CategoriesExample());
        return Math.toIntExact(categoriesCount);
    }

    public static Integer countProducts(ProductsMapper productsMapper) {
        long products = productsMapper.countByExample(new ProductsExample());
        return Math.toIntExact(products);
    }

    public static String selectProductTitleByID(ProductsMapper productsMapper, int id){
        Products products =  productsMapper.selectByPrimaryKey((long) id);
        return products.getTitle();
    }

    public static int selectProductPriceByID(ProductsMapper productsMapper, int id){
        int price;
        Products products =  productsMapper.selectByPrimaryKey((long) id);
        return products.getPrice();
    }

    public static void updateProductById (Product product, ProductsMapper productsMapper){
        Products p = new Products();
        p.setId((long) product.getId());
        p.setTitle(product.getTitle());
        p.setPrice(product.getPrice());
        p.setCategory_id((long)CategoryType.FOOD.getId());
        productsMapper.updateByPrimaryKeySelective(p);
    }
    public static void deleteProductById(int id, ProductsMapper productsMapper){
        productsMapper.deleteByPrimaryKey((long) id);
    }

    public static String getCategoryTitleFromBdByID(int id, CategoriesMapper categoriesMapper){
        return categoriesMapper.selectByPrimaryKey(id).getTitle();
    }
}
