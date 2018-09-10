package com.example.repositoy;

import com.example.Entity.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2018/8/20 0020.
 */
@Repository
public interface IUserInfoRepository extends JpaRepository<UserInfoEntity,Integer> {

    UserInfoEntity findUserInfoByName(String name);

}

