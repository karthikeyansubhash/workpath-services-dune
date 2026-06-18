
package com.hp.ws.cdm.security;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthenticationAttributes {

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;
    /**
     * The client identifier issued to the client during the registration process
     * 
     */
    @SerializedName("client_id")
    @Expose
    private String client_id;
    /**
     * The client secret
     * 
     */
    @SerializedName("client_secret")
    @Expose
    private String client_secret;
    /**
     * The client informs the authorization server of the desired grant type
     * 
     */
    @SerializedName("response_type")
    @Expose
    private String response_type;
    /**
     * The authorization and token endpoints allow the client to specify the scope of the access request
     * 
     */
    @SerializedName("scope")
    @Expose
    private String scope;
    /**
     * An opaque value used by the client to maintain state between the request and callback (can be used for CSRF token)
     * 
     */
    @SerializedName("state")
    @Expose
    private String state;
    /**
     * The redirection endpoint URI
     * 
     */
    @SerializedName("redirect_uri")
    @Expose
    private String redirect_uri;
    /**
     * The error code
     * 
     */
    @SerializedName("error")
    @Expose
    private String error;
    /**
     * Human-readable ASCII text providing additional information
     * 
     */
    @SerializedName("error_description")
    @Expose
    private String error_description;
    /**
     * Grant type identifier
     * 
     */
    @SerializedName("grant_type")
    @Expose
    private String grant_type;
    /**
     * The authorization code received from the authorization server
     * 
     */
    @SerializedName("code")
    @Expose
    private String code;
    /**
     * The type of the token issued by the server
     * 
     */
    @SerializedName("token_type")
    @Expose
    private String token_type;
    /**
     * Token type issued by the server
     * 
     */
    @SerializedName("issued_token_type")
    @Expose
    private String issued_token_type;
    /**
     * The actual client that requests to exchange for a device token
     * 
     */
    @SerializedName("subject_token")
    @Expose
    private String subject_token;
    /**
     * The subject_token type
     * 
     */
    @SerializedName("subject_token_type")
    @Expose
    private String subject_token_type;
    /**
     * The access token issued by the authorization server
     * 
     */
    @SerializedName("access_token")
    @Expose
    private String access_token;
    /**
     * The resource owner identity
     * 
     */
    @SerializedName("username")
    @Expose
    private String username;
    /**
     * The resource owner password
     * 
     */
    @SerializedName("password")
    @Expose
    private String password;
    /**
     * The lifetime in seconds of the token
     * 
     */
    @SerializedName("expires_in")
    @Expose
    private String expires_in;
    /**
     * The refresh token, which can be used to obtain new access tokens using the same authorization grant
     * 
     */
    @SerializedName("refresh_token")
    @Expose
    private String refresh_token;
    /**
     * Authentication agent identifier for credential validation
     * 
     */
    @SerializedName("agentId")
    @Expose
    private String agentId;

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    public String getVersion() {
        return version;
    }

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * The client identifier issued to the client during the registration process
     * 
     */
    public String getClient_id() {
        return client_id;
    }

    /**
     * The client identifier issued to the client during the registration process
     * 
     */
    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    /**
     * The client secret
     * 
     */
    public String getClient_secret() {
        return client_secret;
    }

    /**
     * The client secret
     * 
     */
    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    /**
     * The client informs the authorization server of the desired grant type
     * 
     */
    public String getResponse_type() {
        return response_type;
    }

    /**
     * The client informs the authorization server of the desired grant type
     * 
     */
    public void setResponse_type(String response_type) {
        this.response_type = response_type;
    }

    /**
     * The authorization and token endpoints allow the client to specify the scope of the access request
     * 
     */
    public String getScope() {
        return scope;
    }

    /**
     * The authorization and token endpoints allow the client to specify the scope of the access request
     * 
     */
    public void setScope(String scope) {
        this.scope = scope;
    }

    /**
     * An opaque value used by the client to maintain state between the request and callback (can be used for CSRF token)
     * 
     */
    public String getState() {
        return state;
    }

    /**
     * An opaque value used by the client to maintain state between the request and callback (can be used for CSRF token)
     * 
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * The redirection endpoint URI
     * 
     */
    public String getRedirect_uri() {
        return redirect_uri;
    }

    /**
     * The redirection endpoint URI
     * 
     */
    public void setRedirect_uri(String redirect_uri) {
        this.redirect_uri = redirect_uri;
    }

    /**
     * The error code
     * 
     */
    public String getError() {
        return error;
    }

    /**
     * The error code
     * 
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * Human-readable ASCII text providing additional information
     * 
     */
    public String getError_description() {
        return error_description;
    }

    /**
     * Human-readable ASCII text providing additional information
     * 
     */
    public void setError_description(String error_description) {
        this.error_description = error_description;
    }

    /**
     * Grant type identifier
     * 
     */
    public String getGrant_type() {
        return grant_type;
    }

    /**
     * Grant type identifier
     * 
     */
    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }

    /**
     * The authorization code received from the authorization server
     * 
     */
    public String getCode() {
        return code;
    }

    /**
     * The authorization code received from the authorization server
     * 
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * The type of the token issued by the server
     * 
     */
    public String getToken_type() {
        return token_type;
    }

    /**
     * The type of the token issued by the server
     * 
     */
    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    /**
     * Token type issued by the server
     * 
     */
    public String getIssued_token_type() {
        return issued_token_type;
    }

    /**
     * Token type issued by the server
     * 
     */
    public void setIssued_token_type(String issued_token_type) {
        this.issued_token_type = issued_token_type;
    }

    /**
     * The actual client that requests to exchange for a device token
     * 
     */
    public String getSubject_token() {
        return subject_token;
    }

    /**
     * The actual client that requests to exchange for a device token
     * 
     */
    public void setSubject_token(String subject_token) {
        this.subject_token = subject_token;
    }

    /**
     * The subject_token type
     * 
     */
    public String getSubject_token_type() {
        return subject_token_type;
    }

    /**
     * The subject_token type
     * 
     */
    public void setSubject_token_type(String subject_token_type) {
        this.subject_token_type = subject_token_type;
    }

    /**
     * The access token issued by the authorization server
     * 
     */
    public String getAccess_token() {
        return access_token;
    }

    /**
     * The access token issued by the authorization server
     * 
     */
    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    /**
     * The resource owner identity
     * 
     */
    public String getUsername() {
        return username;
    }

    /**
     * The resource owner identity
     * 
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * The resource owner password
     * 
     */
    public String getPassword() {
        return password;
    }

    /**
     * The resource owner password
     * 
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * The lifetime in seconds of the token
     * 
     */
    public String getExpires_in() {
        return expires_in;
    }

    /**
     * The lifetime in seconds of the token
     * 
     */
    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    /**
     * The refresh token, which can be used to obtain new access tokens using the same authorization grant
     * 
     */
    public String getRefresh_token() {
        return refresh_token;
    }

    /**
     * The refresh token, which can be used to obtain new access tokens using the same authorization grant
     * 
     */
    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    /**
     * Authentication agent identifier for credential validation
     * 
     */
    public String getAgentId() {
        return agentId;
    }

    /**
     * Authentication agent identifier for credential validation
     * 
     */
    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

}
