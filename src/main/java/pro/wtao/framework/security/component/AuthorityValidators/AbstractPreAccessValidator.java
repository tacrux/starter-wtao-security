package pro.wtao.framework.security.component.AuthorityValidators;

import org.springframework.beans.factory.annotation.Autowired;
import pro.wtao.framework.security.component.matchInfoConverters.MatchInfoConverter;
import pro.wtao.framework.security.model.RequestMatchInfo;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;

/**
 * <pre>
 * <b>预制的权限校验</b>
 * <b>Description:</b>
 * <b>Copyright:</b> Copyright 2022 Wangtao. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   		Date                    Author               	 Detail
 *   ----------------------------------------------------------------------
 *   1.0   2022/10/8 17:53    Wangtao     new file.
 * </pre>
 *
 * @author Wangtao
 * @since 2022/10/8
 */
public abstract class AbstractPreAccessValidator implements AccessValidator {
  @Autowired
  private MatchInfoConverter matchInfoConverter;

  /**
   * <pre>
   * 	是否支持
   * </pre>
   *
   * @param annotation
   * @return
   */
  @Override
  public abstract boolean isSupport(Annotation annotation);

  protected RequestMatchInfo getRequestMatchInfo(HttpServletRequest request) {
    return matchInfoConverter.fromRequest(request);
  }
}
