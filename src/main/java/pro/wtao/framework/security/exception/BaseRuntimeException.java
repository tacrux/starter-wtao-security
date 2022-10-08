package pro.wtao.framework.security.exception;

/**
 * <pre>
 * <b>基础运行时异常</b>
 * <b>Description:</b>
 * <b>Copyright:</b> Copyright 2022 Wangtao. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   		Date                    Author               	 Detail
 *   ----------------------------------------------------------------------
 *   1.0   2022/9/30 16:33    Wangtao     new file.
 * </pre>
 *
 * @author Wangtao
 * @since 2022/9/30
 */
public class BaseRuntimeException extends RuntimeException {
    public BaseRuntimeException(String message) {
        super(message);
    }
}
