package whu.edu.cn.service;

import org.springframework.stereotype.Service;
import whu.edu.cn.entity.NameDao;
import whu.edu.cn.entity.MeasurementName;
import whu.edu.cn.entity.Product;
import whu.edu.cn.entity.ProductCopy;
import whu.edu.cn.mapper.ProductMapper;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

/**
 * ProductCopy service class
 */
@Service
public class ProductService {
    @Resource
    ProductMapper productMapper;

    /**
     * 将产品的is_publish字段改为true
     * @param id
     */
    public void publishProductById(Integer id){
        productMapper.publishProductById(id);
    }
    /**
     * 将产品的is_publish字段改为false
     * @param id
     */
    public void retractProductById(Integer id){
        productMapper.retractProductById(id);
    }

    /**
     * Publish all data resource products
     */
    public void publishAll(){
        productMapper.pulishAll();
    }

    /**
     * Retract all data resource products
     */
    public void retractAll(){
        productMapper.pulishAll();
    }
    /**
     * Get all products info.
     *
     * @return
     */
    public List<Product> getAllProducts() {
        List<Product> products = productMapper.getAllProducts();

        products.forEach(product -> product.setMeasurements(productMapper.getMeasurements(product.getId())));
        return products;
    }

    /**
     * Get product measurements by product name.
     *
     * @param name
     * @return
     */
    public List<MeasurementName> getMeasurementsByName(String name){
        List<MeasurementName> measurements = productMapper.getMeasurementNamesByName(name);
        return measurements;
    }

    /**
     * Get raster product info by name.
     * @param ProductName
     * @return
     */
    public List<ProductCopy> getProductsByName(String ProductName){
        List<ProductCopy> productCopies =productMapper.getEOProductsByName(ProductName);
        productCopies.forEach(productCopy -> productCopy.setMeasurements(productMapper.getMeasurements(Integer.parseInt(productCopy.getProductKey()))));
        return productCopies;
    }

    /**
     * Get all product names.
     * @return
     */
    public List<NameDao> getAllProductNames(){
        return productMapper.getAllProductNames();
    }

    /**
     * Get product info by product name, space, time and measurements.
     *
     * @param ProductName
     * @param StartTime
     * @param EndTime
     * @param WKT
     * @return
     */
    public List<ProductCopy> getProductsByParams(String ProductName, Timestamp StartTime, Timestamp EndTime, String WKT) {
        List<ProductCopy> productCopies = productMapper.getProductsByParams(ProductName,StartTime,EndTime,WKT);
        productCopies.forEach(productCopy -> productCopy.setMeasurements(productMapper.getMeasurements(Integer.parseInt(productCopy.getProductKey()))));
        return productCopies;
    }

}
