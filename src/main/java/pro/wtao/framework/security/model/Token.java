package pro.wtao.framework.security.model;

import lombok.Data;

import java.util.Map;

/**
 * <pre>
 * <b></b>
 * <b>Description:</b>
 * <b>Copyright:</b> Copyright 2022 Wangtao. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   		Date                    Author               	 Detail
 *   ----------------------------------------------------------------------
 *   1.0   2022/10/8 11:41    Wangtao     new file.
 * </pre>
 *
 * @author Wangtao
 * @since 2022/10/8
 */
@Data
public class Token {

    public static String BEARER_TYPE = "Bearer";

    private Map<String, Object> additionalInformation;

    private String tokenType = BEARER_TYPE;

    private long expiration;

    private String value;

    public Token(long expiration, String value) {
        this.expiration = expiration;
        this.value = value;
    }
}
