package com.group.lbstore.Repository;

import com.group.lbstore.Model.ChatRoom;
import com.group.lbstore.Model.ChatRoomStatus;
import com.group.lbstore.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, UUID> {
    
    Optional<ChatRoom> findByCustomerAndStatusNot(User customer, ChatRoomStatus status);

    @Query("SELECT c FROM ChatRoom c WHERE c.status = :waitingStatus OR c.status = :activeStatus ORDER BY c.lastMessageTime DESC")
    Page<ChatRoom> findRoomsForAdmin(
            @Param("waitingStatus") ChatRoomStatus waitingStatus, 
            @Param("activeStatus") ChatRoomStatus activeStatus, 
            Pageable pageable
    );

    boolean existsByEmployeeAndStatusAndIdNot(User employee, ChatRoomStatus status, UUID id);
}
