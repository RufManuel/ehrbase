/*
 * Copyright (c) 2019 vitasystems GmbH and Hannover Medical School.
 *
 * This file is part of project EHRbase
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ehrbase.tenant;

import org.apache.commons.codec.binary.Base64;
import org.ehrbase.api.tenant.TenantAuthentication;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import com.auth0.jwt.JWT;
import com.auth0.jwt.impl.JWTParser;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Payload;

public class DefaultTenantAuthentication extends AbstractAuthenticationToken implements TenantAuthentication<String> {
  private static final long serialVersionUID = -187707458684929521L;
  public static final String TENANT_CLAIM = "tnt";
  
  public static <T> DefaultTenantAuthentication of(TenantAuthentication<T> auth) {
    return new DefaultTenantAuthentication(auth.getAuthentication().toString());
  }
  
  private final String tenantId;
  private final DecodedJWT token;
  private final String raw;
  private final Payload payload;
  
  public DefaultTenantAuthentication(String token) {
    super(null);
    this.raw = token;
    this.token = JWT.decode(token);
    this.payload = new JWTParser().parsePayload(new String(Base64.decodeBase64(this.token.getPayload())));
    this.tenantId = payload.getClaim(TENANT_CLAIM).asString();
  }
  
  public String getTenantId() {
    return tenantId;
  }
  
  @Override
  public Object getCredentials() {
    return token;
  }

  @Override
  public Object getPrincipal() {
    return token;
  }

  @Override
  public String getAuthentication() {
    return raw;
  }
}
