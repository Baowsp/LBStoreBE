package com.group.lbstore.Service;

import com.group.lbstore.Model.Manager;
import com.group.lbstore.Service.ManagerService;
import com.group.lbstore.Repository.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerServiceImpl implements ManagerService {
    private final ManagerRepository managerRepository;

    public Manager createManager(Manager manager) {
        // TODO: Logic to ensure the employee exists and has MANAGEMENT department
        return managerRepository.save(manager);
    }

    public List<Manager> getAllManagers() {
        return managerRepository.findAll();
    }

    public Manager getManagerById(Long id) {
        return managerRepository.findById(id).orElseThrow(() -> new RuntimeException("Manager not found"));
    }
}