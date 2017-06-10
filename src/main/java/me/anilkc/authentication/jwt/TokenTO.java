package me.anilkc.authentication.jwt;

import java.util.Date;
import java.util.Map;

public class TokenTO {

  private String jwtToken;
  private String subjectId;
  private String subject;
  private String clientIpAddress;
  private String roles;
  private String browserFingerprintDigest;
  private String issuer;
  private Date issueDate;
  private Date expirationDate;
  private Date notBeforeDate;
  private Map<String, Object> clientStorage;
  private Map<String, Object> headerClaims;


  public String getJwtToken() {
    return jwtToken;
  }

  public void setJwtToken(String jwtToken) {
    this.jwtToken = jwtToken;
  }

  public String getSubjectId() {
    return subjectId;
  }

  public void setSubjectId(String subjectId) {
    this.subjectId = subjectId;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getClientIpAddress() {
    return clientIpAddress;
  }

  public void setClientIpAddress(String clientIpAddress) {
    this.clientIpAddress = clientIpAddress;
  }

  public String getRoles() {
    return roles;
  }

  public void setRoles(String roles) {
    this.roles = roles;
  }

  public String getBrowserFingerprintDigest() {
    return browserFingerprintDigest;
  }

  public void setBrowserFingerprintDigest(String browserFingerprintDigest) {
    this.browserFingerprintDigest = browserFingerprintDigest;
  }

  public String getIssuer() {
    return issuer;
  }

  public void setIssuer(String issuer) {
    this.issuer = issuer;
  }

  public Date getIssueDate() {
    return issueDate;
  }

  public void setIssueDate(Date issueDate) {
    this.issueDate = issueDate;
  }

  public Date getExpirationDate() {
    return expirationDate;
  }

  public void setExpirationDate(Date expirationDate) {
    this.expirationDate = expirationDate;
  }

  public Date getNotBeforeDate() {
    return notBeforeDate;
  }

  public void setNotBeforeDate(Date notBeforeDate) {
    this.notBeforeDate = notBeforeDate;
  }

  public Map<String, Object> getClientStorage() {
    return clientStorage;
  }

  public void setClientStorage(Map<String, Object> clientStorage) {
    this.clientStorage = clientStorage;
  }

  public Map<String, Object> getHeaderClaims() {
    return headerClaims;
  }

  public void setHeaderClaims(Map<String, Object> headerClaims) {
    this.headerClaims = headerClaims;
  }


}
