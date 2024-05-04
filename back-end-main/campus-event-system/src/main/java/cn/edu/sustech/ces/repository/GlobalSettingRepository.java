package cn.edu.sustech.ces.repository;

import cn.edu.sustech.ces.entity.GlobalSetting;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GlobalSettingRepository extends ListCrudRepository<GlobalSetting, String> {


}
