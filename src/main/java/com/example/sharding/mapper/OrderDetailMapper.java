package com.example.sharding.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.sharding.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单详情Mapper接口
 * 
 * @author example
 */
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {

    /**
     * 批量插入订单详情
     */
    int batchInsert(@Param("detailList") List<OrderDetail> detailList);

    /**
     * 批量更新商品数量
     */
    int batchUpdateQuantity(@Param("projectId") String projectId,
                           @Param("detailIds") List<Long> detailIds,
                           @Param("quantity") Integer quantity);

    /**
     * 根据订单ID批量更新价格
     */
    int batchUpdatePriceByOrderId(@Param("projectId") String projectId,
                                 @Param("orderId") Long orderId,
                                 @Param("discountRate") BigDecimal discountRate);

    /**
     * 根据商品ID批量删除订单详情
     */
    int batchDeleteByProductId(@Param("projectId") String projectId,
                              @Param("productIds") List<Long> productIds);
}