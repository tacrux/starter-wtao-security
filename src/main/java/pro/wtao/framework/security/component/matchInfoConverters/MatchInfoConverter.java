package pro.wtao.framework.security.component.matchInfoConverters;

import pro.wtao.framework.security.model.RequestMatchInfo;

import javax.servlet.http.HttpServletRequest;

/**
 *  定义从request构建匹配信息的逻辑
 */
public interface MatchInfoConverter {
 RequestMatchInfo fromRequest(HttpServletRequest request);
}
