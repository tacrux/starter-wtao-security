package pro.wtao.framework.security.constants;

/**
 * 鉴权模式
 */
public enum AccessValidateMode {
  LOCAL(0,"本地鉴权"),
  GATEWAY(1,"网关鉴权")
  ;
  final int code;
  final String explain;

  AccessValidateMode(int code, String explain) {
    this.code = code;
    this.explain = explain;
  }
}
