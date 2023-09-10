package com.ryzw.housemanager.vo;

import lombok.Data;

/**
 * <p>
 *分页的基类
 * </p>
 *
 * @author mfl
 * @since 2019/3/4
 */
@Data
public class PageBasicVo<T> extends PageVo {

    private T id;
}
