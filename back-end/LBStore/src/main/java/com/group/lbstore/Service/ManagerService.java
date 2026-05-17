package com.group.lbstore.Service;
import com.group.lbstore.Model.Manager;
import java.util.List;
public interface ManagerService {
    Manager createManager(Manager manager);
    List<Manager> getAllManagers();
    Manager getManagerById(Long id);
}