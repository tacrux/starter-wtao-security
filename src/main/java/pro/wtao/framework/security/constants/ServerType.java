package pro.wtao.framework.security.constants;

public enum ServerType {
  GATEWAY(0,"网关"),
  RESOURCE_SERVER(1,"资源服务器"),
  AUTHORIZATION_SERVER(2,"授权服务器")
  ;
  final int code;
  final String explain;

  ServerType(int code, String explain) {
    this.code = code;
    this.explain = explain;
  }
}
