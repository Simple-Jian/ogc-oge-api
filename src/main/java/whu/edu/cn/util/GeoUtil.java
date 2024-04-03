package whu.edu.cn.util;

import org.springframework.stereotype.Component;
import whu.edu.cn.entity.ProductCopy;
import whu.edu.cn.entity.ProductDao;
import whu.edu.cn.entity.Measurement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GeoUtil {
    /**
     * Transform a spatial extent to WKT format.
     * @param minx 最小x
     * @param miny 最小y
     * @param maxx 最大x
     * @param maxy 最大y
     * @return 返回wkt
     */
    public String DoubleToWKT(double minx,double miny,double maxx,double maxy){
        return "POLYGON(("+minx+" "+miny+", "+minx+" "+maxy+", "+maxx+" "+maxy+", "+maxx+" "+miny+","+minx+" "+miny+"))";
    }

    /**
     * 根据crsCode获取对应的Href
     * @param crsCode 例如 EPSG：4326
     * @return href
     */
    public String getCRSHref(String crsCode){
        if(crsCode.equals("EPSG:4326")){
            return "http://www.opengis.net/def/crs/OGC/1.3/CRS84";
        }else{
            return null;
        }
    }

    public List<ProductDao> TransformProducts(List<ProductCopy> productCopyList){
        List<ProductDao> productDaos = new ArrayList<ProductDao>();
        for(ProductCopy productCopy : productCopyList){
            List<Measurement> measurements= productCopy.getMeasurements();
            if(measurements.size()>=1){
                for(Measurement measurement:measurements){
                    ProductDao productDao = new ProductDao();
                    productDao.setdType(measurement.getdType());
                    productDao.setMeasurementName(measurement.getMeasurementName());

                    productDao.setProductKey(productCopy.getProductKey());
                    productDao.setProductName(productCopy.getProductName());
                    productDao.setPlatformName(productCopy.getPlatformName());
                    productDao.setSensorName(productCopy.getSensorName());
                    productDao.setPhenomenonTime(productCopy.getPhenomenonTime());
                    productDao.setResultTime(productCopy.getResultTime());

                    productDao.setCrs(productCopy.getCrs());

                    productDao.setImagingLength(productCopy.getImagingLength());
                    productDao.setImagingWidth(productCopy.getImagingWidth());
                    productDao.setLowerLeftLong(productCopy.getLowerLeftLong());
                    productDao.setLowerLeftLat(productCopy.getLowerLeftLat());
                    productDao.setLowerRightLat(productCopy.getLowerRightLat());
                    productDao.setLowerRightLong(productCopy.getLowerRightLong());
                    productDao.setUpperRightLong(productCopy.getUpperRightLong());
                    productDao.setUpperRightLat(productCopy.getUpperRightLat());
                    productDao.setUpperLeftLong(productCopy.getUpperLeftLong());
                    productDao.setUpperLeftLat(productCopy.getUpperLeftLat());
                    productDaos.add(productDao);
                }
            }
        }
        System.out.println(productDaos.size());
        Map<String,List<ProductDao>> collect = productDaos.stream().collect(Collectors.groupingBy(e->fetchGroupKey(e)));
        List<ProductDao> productDaoReturn = new ArrayList<ProductDao>();
        for(List<ProductDao> productDaos1:collect.values()){
            productDaoReturn.add(productDaos1.get(0));
        }
        return productDaoReturn;
    }

    public static String fetchGroupKey(ProductDao productDao){
        return productDao.getPhenomenonTime()+"#"+productDao.getProductName()+"#"+productDao.getLowerLeftLong()+"#"+
                productDao.getLowerLeftLat()+"#"+productDao.getUpperRightLong()+"#"+productDao.getUpperRightLat()+"$"+productDao.getMeasurementName();
    }
}
