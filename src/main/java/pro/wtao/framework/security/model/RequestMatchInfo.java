package pro.wtao.framework.security.model;

import lombok.Data;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * <pre>
 * <b>请求匹配信息，包含请求头和路径</b>
 * <b>Description:</b>
 *
 * <b>Author:</b> tacrux
 * <b>Date:</b> 2022/9/8 10:47
 * <b>Copyright:</b> Copyright 2022 Wangtao. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   		Date                    Author               	 Detail
 *   ----------------------------------------------------------------------
 *   1.0   2022/10/8 17:28    Wangtao     new file.
 * </pre>
 *
 * @author Wangtao
 * @since 2022/10/8
 */
@Data
public
class RequestMatchInfo {
  private AntPathMatcher antPathMatcher = new AntPathMatcher(",");
  private String requestMethod;
  private String requestUri;

  public RequestMatchInfo(String requestMethod, String requestUri) {
    this.requestMethod = requestMethod;
    this.requestUri = requestUri;
  }

  public static RequestMatchInfo fromRequest(HttpServletRequest request) {
    return new RequestMatchInfo(request.getMethod(), request.getRequestURI());
  }

  public boolean match(UriGrantedAuthority uriGrantedAuthority) {
    return antPathMatcher.match(uriGrantedAuthority.getUri(), requestUri)
      && Objects.equals(uriGrantedAuthority.getMethod().name(), requestMethod);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RequestMatchInfo that = (RequestMatchInfo) o;
    return com.google.common.base.Objects.equal(requestMethod, that.requestMethod) && com.google.common.base.Objects.equal(requestUri, that.requestUri);
  }

  @Override
  public int hashCode() {
    return com.google.common.base.Objects.hashCode(requestMethod, requestUri);
  }
}
