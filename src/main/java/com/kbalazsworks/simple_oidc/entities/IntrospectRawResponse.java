package com.kbalazsworks.simple_oidc.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Jacksonized
@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@ToString
@Getter
public class IntrospectRawResponse
{
    @JsonProperty("iss")
    private final String       iss;
    @JsonProperty("nbf")
    private final Integer      nbf;
    @JsonProperty("iat")
    private final Integer      iat;
    @JsonProperty("exp")
    private final Integer      exp;
    @JsonProperty("aud")
    private final List<String> aud;
    @JsonProperty("client_id")
    private final String       clientId;
    @JsonProperty("sub")
    private final String       sub;
    @JsonProperty("auth_time")
    private final Integer      authTime;
    @JsonProperty("idp")
    private final String       idp;
    @JsonProperty("amr")
    private final String       amr;
    @JsonProperty("sid")
    private final String       sid;
    @JsonProperty("jti")
    private final String       jti;
    @JsonProperty("active")
    private final Boolean      active;
    @JsonProperty("scope")
    private final String       scope;
}
