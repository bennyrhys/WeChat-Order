package com.bennyrhys.wechat_order.controller;

import com.bennyrhys.wechat_order.VO.ProductInfoVO;
import com.bennyrhys.wechat_order.VO.ProductVO;
import com.bennyrhys.wechat_order.VO.ResultVO;
import com.bennyrhys.wechat_order.daoobject.ProductCategory;
import com.bennyrhys.wechat_order.daoobject.ProductInfo;
import com.bennyrhys.wechat_order.service.CategoryService;
import com.bennyrhys.wechat_order.service.ProductSerice;
import com.bennyrhys.wechat_order.utils.ResultVOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 买家商品
 * @Author bennyrhys
 * @Date 2020-06-27 19:30
 */
@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {
    
    @Autowired 
    private ProductSerice productSerice;

    @Autowired
    private CategoryService categoryService;
    
    @GetMapping("list")
    public ResultVO list() {
//        1. 查询所有上架商品
        List<ProductInfo> productInfoList = productSerice.findUpAll();
//        2. 查询类目（一次查清）
//        List<Integer> categoryTypeList = new ArrayList<>();
//        获取categoryType-传统方法
//        for (ProductInfo productInfo : productInfoList) {
//            categoryTypeList.add(productInfo.getCategoryType());
//        }
//        精简方法（java8 lambda）
        List<Integer> categoryTypeList = productInfoList.stream()
                .map(e -> e.getCategoryType())
                .collect(Collectors.toList());
        List<ProductCategory> productCategoryList = categoryService.findByCategoryTypeIn(categoryTypeList);
//        3. 数据拼装
        List<ProductVO> productVOList = new ArrayList<>();
        for (ProductCategory productCategory : productCategoryList) {
            ProductVO productVO = new ProductVO();
            productVO.setProductType(productCategory.getCategoryType());
            productVO.setProductName(productCategory.getCategoryName());

            List<ProductInfoVO> productInfoVOList = new ArrayList<>();
            for (ProductInfo productInfo : productInfoList) {
                if (productInfo.getCategoryType().equals(productCategory.getCategoryType())){
                    ProductInfoVO productInfoVO = new ProductInfoVO();
//                    每个重新赋值太慢 对象拷贝
                    BeanUtils.copyProperties(productInfo, productInfoVO);
                    productInfoVOList.add(productInfoVO);
                }
            }
            productVO.setProductInfoVOList(productInfoVOList);
            productVOList.add(productVO);
        }


//        【直接返回】
//        ResultVO<Object> resultVO = new ResultVO<>();
//        resultVO.setData(productVOList);
//        resultVO.setCode(0);
//        resultVO.setMsg("成功");

//        【测试 json 结构】
//        ResultVO<Object> vo = new ResultVO<>();
//        ProductVO productVO = new ProductVO();
//        ProductInfoVO productInfoVO = new ProductInfoVO();
//
//        vo.setCode(0);
//        vo.setMsg("成功");
//
//        productVO.setProductInfoVOList(Arrays.asList(productInfoVO));
//        vo.setData(Arrays.asList(productVO));

//      【封装返回数据】
        return ResultVOUtils.success(productVOList);
    }
}
