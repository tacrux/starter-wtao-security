package pro.wtao.framework.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Objects;

/**
 * <pre>
 * <b>接口权限</b>
 * <b>Description:</b>
 * <b>Copyright:</b> Copyright 2022 Wangtao. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   		Date                    Author               	 Detail
 *   ----------------------------------------------------------------------
 *   1.0   2022/10/8 11:30    Wangtao     new file.
 * </pre>
 *
 * @author Wangtao
 * @since 2022/10/8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class UriGrantedAuthority implements GrantedAuthority {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private RequestMethod method;
    private String uri;
    private String systemCode;


    @Override
    @JsonIgnore
    public String getAuthority() {
        return systemCode.concat(" ").concat(method.name()).concat(",").concat(uri);
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof UriGrantedAuthority) {
            return (this.method.equals(((UriGrantedAuthority) obj).method)
                    && (this.uri.equals(((UriGrantedAuthority) obj).uri))
                    &&(this.systemCode.equals(((UriGrantedAuthority) obj).systemCode)));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, uri, systemCode);
    }

    @Override
    public String toString() {
        return this.getAuthority();
    }


}
