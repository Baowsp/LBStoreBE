package com.group.lbstore.Repository;

import com.group.lbstore.Model.OnlineOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OnlineOrderDetailRepository extends JpaRepository<OnlineOrderDetail, Long> {
    List<OnlineOrderDetail> findByOnlineOrderId(UUID onlineOrderId);
}