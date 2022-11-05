package pro.wtao.framework.security.component.matchInfoConverters;

import pro.wtao.framework.security.model.RequestMatchInfo;

import javax.servlet.http.HttpServletRequest;

public class DefaultMachInfoConverter implements MatchInfoConverter{
  @Override
  public RequestMatchInfo fromRequest(HttpServletRequest request) {
    return RequestMatchInfo.fromRequest(request);
  }
}
