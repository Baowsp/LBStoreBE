package com.group.lbstore.Service;

import com.group.lbstore.DTO.DeliveryEmployeeRequest;
import com.group.lbstore.Model.*;
import com.group.lbstore.Repository.DeliveryEmployeeRepository;
import com.group.lbstore.Repository.EmployeeRepository;
import com.group.lbstore.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryEmployeeServiceImpl implements DeliveryEmployeeService {

    private final DeliveryEmployeeRepository deliveryEmployeeRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public DeliveryEmployee create(DeliveryEmployee deliveryEmployee) {
        return deliveryEmployeeRepository.save(deliveryEmployee);
    }

    /**
     * Tạo mới nhân viên giao hàng hoàn toàn từ form (không cần link Employee sẵn có).
     * Flow: DeliveryEmployeeRequest → User → Employee → DeliveryEmployee (1 transaction)
     */
    @Override
    @Transactional
    public DeliveryEmployee createWithInfo(DeliveryEmployeeRequest req) {
        // 1. Tự sinh email nếu không nhập
        String email = (req.getEmail() != null && !req.getEmail().isBlank())
                ? req.getEmail().trim().toLowerCase()
                : "shipper." + UUID.randomUUID().toString().substring(0, 8) + "@lbstore.local";

        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email " + email + " đã tồn tại trong hệ thống.");
        }

        // 2. Tạo User
        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode("Shipper@123"))   // Mật khẩu mặc định
                .fullName(req.getFullName())
                .phoneNumber(req.getPhoneNumber())
                .role(UserRole.DELIVERY_EMPLOYEE)
                .isActive(true)
                .build();
        userRepository.save(user);

        // 3. Tạo Employee
        String empCode = "SHP-" + System.currentTimeMillis() % 100000;
        Employee employee = Employee.builder()
                .user(user)
                .employeeCode(empCode)
                .department(Department.DELIVERY)
                .position("Nhân viên giao hàng")
                .hireDate(LocalDate.now())
                .isActive(true)
                .build();
        employeeRepository.save(employee);

        // 4. Tạo DeliveryEmployee
        DeliveryStatus status;
        try {
            status = (req.getStatus() != null) ? DeliveryStatus.valueOf(req.getStatus()) : DeliveryStatus.OFFLINE;
        } catch (IllegalArgumentException e) {
            status = DeliveryStatus.OFFLINE;
        }

        VehicleType vehicleType;
        try {
            vehicleType = VehicleType.valueOf(req.getVehicleType());
        } catch (Exception e) {
            vehicleType = VehicleType.MOTORBIKE;
        }

        DeliveryEmployee shipper = DeliveryEmployee.builder()
                .employee(employee)
                .vehicleType(vehicleType)
                .licensePlate(req.getLicensePlate().trim().toUpperCase())
                .drivingLicense(req.getDrivingLicense().trim())
                .status(status)
                .build();

        return deliveryEmployeeRepository.save(shipper);
    }

    @Override
    public List<DeliveryEmployee> findAll(DeliveryStatus status) {
        return status != null ? deliveryEmployeeRepository.findByStatus(status) : deliveryEmployeeRepository.findAll();
    }

    @Override
    public DeliveryEmployee findById(Long id) {
        return deliveryEmployeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên giao hàng ID: " + id));
    }

    @Override
    public void updateLocation(Long id, Double latitude, Double longitude) {
        DeliveryEmployee employee = findById(id);
        employee.setCurrentLatitude(latitude);
        employee.setCurrentLongitude(longitude);
        deliveryEmployeeRepository.save(employee);
    }

    @Override
    public DeliveryEmployee updateStatus(Long id, DeliveryStatus status) {
        DeliveryEmployee employee = findById(id);
        employee.setStatus(status);
        return deliveryEmployeeRepository.save(employee);
    }

    @Override
    public DeliveryEmployee update(Long id, DeliveryEmployee data) {
        DeliveryEmployee employee = findById(id);
        if (data.getVehicleType() != null) employee.setVehicleType(data.getVehicleType());
        if (data.getLicensePlate() != null) employee.setLicensePlate(data.getLicensePlate());
        if (data.getDrivingLicense() != null) employee.setDrivingLicense(data.getDrivingLicense());
        if (data.getStatus() != null) employee.setStatus(data.getStatus());
        return deliveryEmployeeRepository.save(employee);
    }

    @Override
    public void delete(Long id) {
        DeliveryEmployee employee = findById(id);
        deliveryEmployeeRepository.delete(employee);
    }
}