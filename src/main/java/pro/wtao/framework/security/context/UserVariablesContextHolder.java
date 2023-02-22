package pro.wtao.framework.security.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <pre>
 * <b>用户变量上下文持有者</b>
 * <b>Description:</b>
 * <b>Copyright:</b> Copyright 2022 360humi. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   		Date                    Author               	 Detail
 *   ----------------------------------------------------------------------
 *   1.0   2023/2/22 14:46    Wangtao     new file.
 * </pre>
 *
 * @author Wangtao
 * @since 2023/2/22
 */
@Component
public class UserVariablesContextHolder {

    private static UserVariablesContext userVariablesContext;

    public static UserVariablesContext getUserVariablesContext() {
        return userVariablesContext;
    }

    @Autowired
    public void setUserVariablesContext(UserVariablesContext userVariablesContext) {
        UserVariablesContextHolder.userVariablesContext = userVariablesContext;
    }
}
