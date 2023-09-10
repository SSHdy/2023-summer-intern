package com.ryzw.housemanager.service.impl;

import com.ryzw.housemanager.entity.Property;
import com.ryzw.housemanager.mapper.PropertyMapper;
import com.ryzw.housemanager.service.IPropertyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mfl
 * @since 2019-10-17
 */
@Service
public class PropertyServiceImpl extends ServiceImpl<PropertyMapper, Property> implements IPropertyService {

}
