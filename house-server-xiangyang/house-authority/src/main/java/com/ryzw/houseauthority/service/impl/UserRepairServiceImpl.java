package com.ryzw.houseauthority.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryzw.houseauthority.entity.UserRepair;
import com.ryzw.houseauthority.exception.ServiceException;
import com.ryzw.houseauthority.mapper.UserRepairMapper;
import com.ryzw.houseauthority.service.IUserRepairService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户维修权限表 服务实现类
 * </p>
 *
 * @author mfl
 * @since 2019-04-15
 */
@Service
@Slf4j
public class UserRepairServiceImpl extends ServiceImpl<UserRepairMapper, UserRepair> implements IUserRepairService {

    @Autowired
    UserRepairMapper userRepairMapper;

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 根据权限Id查询对应的用户
     *
     * @param repairId
     * @return
     */
    @Override
    public Set selectRepairList(Long repairId) {
        try {
            if (redisTemplate.hasKey("repair:right:" + repairId)) {
                Set members = redisTemplate.opsForSet().members("repair:right:" + repairId);
                return members;
            } else {
                Set<Object> repairSet = new HashSet<>();
                List<UserRepair> userRepairList = userRepairMapper.selectList(new QueryWrapper<UserRepair>().eq("repair_id", repairId));
                if (CollectionUtils.isNotEmpty(userRepairList)) {
                    repairSet = userRepairList.stream().map(UserRepair::getUserId).collect(Collectors.toSet());
                    redisTemplate.opsForSet().add("repair:right:" + repairId, repairSet.toArray(new Long[0]));
                }
                return repairSet;
            }
        } catch (Exception ex) {
            log.error("selectRepairListError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 删除用户
     *
     * @param userId
     * @return
     */
    @Override
    public boolean deleteRepairById(Long userId) {
        try {
            List<UserRepair> userRepairList = userRepairMapper.selectList(new QueryWrapper<UserRepair>().eq("user_id", userId));
            Set<Object> deletes = new HashSet<>();
            Object flag = null;
            if (CollectionUtils.isNotEmpty(userRepairList)) {
                for (UserRepair userRepair : userRepairList) {
                    //获取权限id
                    Long repairId = userRepair.getRepairId();
                    deletes.add(repairId);
                }
                //删除redis中拥有该权限中的用户
                flag = this.updateRepairStorage(userId, null, deletes);
            }
            //删除数据库中的用户
            userRepairMapper.delete(new QueryWrapper<UserRepair>().eq("user_id", userId));
            return flag.equals(1);
        } catch (Exception ex) {
            log.error("deleteRepairByIdError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 操作Redis
     *
     * @param userId
     * @param adds
     * @param deletes
     * @return
     */
    private Object updateRepairStorage(Long userId, Set<Object> adds, Set<Object> deletes) {
        return redisTemplate.opsForSet().getOperations().execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                if (CollectionUtils.isNotEmpty(deletes)) {
                    for (Object deleteObj : deletes) {
                        redisConnection.sRem(("repair:right:" + deleteObj).getBytes(), String.valueOf(userId).getBytes());
                    }
                }
                if (CollectionUtils.isNotEmpty(adds)) {
                    for (Object addObj : adds) {
                        redisConnection.sAdd(("repair:right:" + addObj).getBytes(), String.valueOf(userId).getBytes());
                    }
                }
                return 1;
            }
        });
    }
}
