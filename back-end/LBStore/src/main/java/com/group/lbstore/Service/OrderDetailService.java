package com.group.lbstore.Service;

import com.group.lbstore.Model.OrderDetail;
import com.group.lbstore.Repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public List<OrderDetail> findAll() {
        return orderDetailRepository.findAll();
    }

    public Optional<OrderDetail> findById(Long id) {
        return orderDetailRepository.findById(id);
    }

    public OrderDetail save(OrderDetail orderDetail) {
        return orderDetailRepository.save(orderDetail);
    }

    public void delete(Long id) {
        orderDetailRepository.deleteById(id);
    }
}