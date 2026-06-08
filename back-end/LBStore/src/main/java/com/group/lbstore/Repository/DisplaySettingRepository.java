package com.group.lbstore.Repository;

import com.group.lbstore.Model.DisplaySetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisplaySettingRepository extends JpaRepository<DisplaySetting, String> {
}
