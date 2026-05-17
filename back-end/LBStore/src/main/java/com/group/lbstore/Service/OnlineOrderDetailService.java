package com.group.lbstore.Service;

import com.group.lbstore.Model.OnlineOrderDetail;
import com.group.lbstore.Repository.OnlineOrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OnlineOrderDetailService {

    @Autowired
    private OnlineOrderDetailRepository onlineOrderDetailRepository;

    public List<OnlineOrderDetail> findAll() {
        return onlineOrderDetailRepository.findAll();
    }

    public Optional<OnlineOrderDetail> findById(Long id) {
        return onlineOrderDetailRepository.findById(id);
    }

    public OnlineOrderDetail save(OnlineOrderDetail onlineOrderDetail) {
        return onlineOrderDetailRepository.save(onlineOrderDetail);
    }

    public void delete(Long id) {
        onlineOrderDetailRepository.deleteById(id);
    }
}