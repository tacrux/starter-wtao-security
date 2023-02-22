package pro.wtao.framework.security.exception;

/**
 * <pre>
 * <b>不支持的操作</b>
 * <b>Description:</b>
 * <b>Copyright:</b> Copyright 2022 360humi. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   		Date                    Author               	 Detail
 *   ----------------------------------------------------------------------
 *   1.0   2023/2/22 14:40    Wangtao     new file.
 * </pre>
 *
 * @author Wangtao
 * @since 2023/2/22
 */
public class OperateNotSupportException extends BaseRuntimeException{

    public OperateNotSupportException(String message) {
        super(message);
    }
}
