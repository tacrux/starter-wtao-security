package pro.wtao.framework.security.context;

import pro.wtao.framework.security.model.LoginUser;
/**
 * <pre>
 * <b></b>
 * <b>Description:</b>
 * <b>Copyright:</b> Copyright 2022 Wangtao. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   		Date                    Author               	 Detail
 *   ----------------------------------------------------------------------
 *   1.0   2022/10/8 11:37    Wangtao     new file.
 * </pre>
 *
 * @author Wangtao
 * @since 2022/10/8
 */
public interface OnlineUserContext {

    /**
     * <pre>
     *	加载用户信息
     * </pre>
     * @return
     */
    LoginUser get(String jti);

    void put(LoginUser loginUser);

    /**
     * 更新缓存中的用户信息
     * @param loginUser
     */
    void update(LoginUser loginUser);

    /**
     * <pre>
     *	删除
     * </pre>
     * @param jti
     */
    void remove(String jti);

}

