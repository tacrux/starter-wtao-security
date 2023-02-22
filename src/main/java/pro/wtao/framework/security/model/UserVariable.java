package pro.wtao.framework.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;

/**
 * <pre>
 * <b>用户变量</b>
 * <ul>
 *     <b>Description:</b>
 *     <li>通常像username，id这种不可变信息放到{@link pro.wtao.framework.security.model.LoginUser},便于使用<li/>
 *     <li>邮箱，手机号通常是可以修改的，单独存储，便于修改后刷新这些变量（如果有缓存）<li/>
 * </ul>
 * <b>Copyright:</b> Copyright 2022 360humi. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   		Date                    Author               	 Detail
 *   ----------------------------------------------------------------------
 *   1.0   2023/2/22 13:56    Wangtao     new file.
 * </pre>
 *
 * @author Wangtao
 * @since 2023/2/22
 */

/**
 * 用户变量类，用于存储用户信息和授权信息的容器。
 */
@Data
@AllArgsConstructor
public class UserVariable {

    /**
     * 用户电话号码
     */
    private String phone;

    /**
     * 用户电子邮件地址
     */
    private String email;

    /**
     * 用户授权信息的集合
     */
    private Collection<UriGrantedAuthority> authorities;
}

