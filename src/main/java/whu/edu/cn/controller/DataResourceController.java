package whu.edu.cn.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import whu.edu.cn.entity.Product;
import whu.edu.cn.entity.Result;
import whu.edu.cn.service.ProductService;

import java.util.List;

@Api(tags = "OGE-serviceCenter")
@RestController
@RequestMapping("/services")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DataResourceController {
    // 这个变量表示Coverage服务是否打开
    public boolean COVERAGE_SERVICES_IS_ON=true;

    @Autowired
    private ProductService productService;

    @GetMapping("/toggleCoverageAPI")
    @ApiOperation("开启/关闭CoverageAPI服务")
    private Result toggleCoverage(){
        COVERAGE_SERVICES_IS_ON=!COVERAGE_SERVICES_IS_ON;
        return Result.ok().success("操作成功！");
    }

    @GetMapping("/data-publish")
    @ApiOperation("查看可/已经发布的数据集")
    public Result<Object> getDataResourceProducts(){
        List<Product> allProducts = productService.getAllProducts();
        return Result.ok(allProducts);
    }

    @PutMapping("/data-publish/{id}")
    @ApiOperation("根据id将数据集发布")
    public Result publishById(@PathVariable Integer id){
       try {
            productService.publishProductById(id);
            return Result.ok().success("发布成功");
        }catch (Exception e){
           return Result.error(500,"服务器异常,或数据集不存在");
       }
    }

    @PutMapping("/data-retract/{id}")
    @ApiOperation("根据id将数据集撤回")
    public Result retractById(@PathVariable Integer id){
        try {
            productService.retractProductById(id);
            return Result.ok().success("撤回成功");
        }catch (Exception e){
            return Result.error(500,"服务器异常,或数据集不存在");
        }
    }

    @PutMapping("/data-publish/publishAll")
    @ApiOperation("发布所有数据集")
    public Result publishAll(){
        try {
            productService.publishAll();
            return Result.ok().success("发布成功");
        }catch (Exception e){
            return Result.error(500,"服务器异常,或数据集不存在");
        }
    }

    @PutMapping("/data-retract/retractAll")
    @ApiOperation("撤回所有数据集")
    public Result retractAll(){
        try {
            productService.publishAll();
            return Result.ok().success("撤回成功");
        }catch (Exception e){
            return Result.error(500,"服务器异常,或数据集不存在");
        }
    }

}
