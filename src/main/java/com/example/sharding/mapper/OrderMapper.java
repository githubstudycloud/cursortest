package com.example.sharding.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.sharding.entity.Order;
import com.example.sharding.entity.vo.OrderDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单Mapper接口
 * 
 * @author example
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    /**
     * 多表联查：查询订单及其详情和用户信息
     */
    List<OrderDetailVO> selectOrderDetailWithUser(@Param("projectId") String projectId, 
                                                  @Param("orderNo") String orderNo);

    /**
     * 多表联查：根据用户ID查询订单详情
     */
    List<OrderDetailVO> selectOrderDetailByUserId(@Param("projectId") String projectId, 
                                                  @Param("userId") Long userId);

    /**
     * 多表联查：查询指定状态的订单详情
     */
    List<OrderDetailVO> selectOrderDetailByStatus(@Param("projectId") String projectId, 
                                                  @Param("status") Integer status);

    /**
     * 批量更新订单状态
     */
    int batchUpdateOrderStatus(@Param("projectId") String projectId,
                              @Param("orderIds") List<Long> orderIds,
                              @Param("newStatus") Integer newStatus);

    /**
     * 根据用户ID批量更新订单状态
     */
    int batchUpdateOrderStatusByUserId(@Param("projectId") String projectId,
                                      @Param("userId") Long userId,
                                      @Param("oldStatus") Integer oldStatus,
                                      @Param("newStatus") Integer newStatus);
}