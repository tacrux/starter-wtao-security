package pro.wtao.framework.security.context;

import pro.wtao.framework.security.model.UserVariable;

/**
 * <pre>
 * <b>用户变量存储</b>
 * <b>Description:</b>
 * <b>Copyright:</b> Copyright 2022 360humi. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   		Date                    Author               	 Detail
 *   ----------------------------------------------------------------------
 *   1.0   2023/2/22 14:10    Wangtao     new file.
 * </pre>
 *
 * @author Wangtao
 * @since 2023/2/22
 */
public interface UserVariablesContext {
   UserVariable get(String username);
}
