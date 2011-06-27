/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.seam.social.facebook;

import javax.enterprise.inject.New;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.jboss.seam.social.facebook.model.UserJackson;
import org.jboss.seam.social.oauth.HttpResponse;
import org.jboss.seam.social.oauth.JsonMapper;
import org.jboss.seam.social.oauth.OAuth2ServiceScribe;
import org.jboss.seam.social.oauth.OAuthService;
import org.jboss.seam.social.oauth.OAuthServiceSettings;
import org.jboss.seam.social.oauth.RelatedTo;
import org.jboss.seam.social.oauth.RestVerb;
import org.jboss.seam.social.oauth.UserProfile;

/**
 * @author Antoine Sabot-Durand
 */

public class FacebookJackson extends OAuth2ServiceScribe implements Facebook {

    static final String USER_PROFILE_URL = "https://graph.facebook.com/me";
    static final String LOGO_URL = "https://d2l6uygi1pgnys.cloudfront.net/2-2-08/images/buttons/facebook_connect.png";
    public static final String TYPE = "Facebook";
    static final String STATUS_UPDATE_URL = "https://graph.facebook.com/me/feed";

    @Inject
    private Logger log;
    @Inject
    private JsonMapper jsonMapper;

    @Override
    @Inject
    public void setSettings(@RelatedTo(FacebookJackson.TYPE) OAuthServiceSettings settings) {
        super.setSettings(settings);

    }
    
    @Produces
    @RelatedTo(FacebookJackson.TYPE)
    protected OAuthService qualifiedFacebookProducer(@New FacebookJackson service) {
        return service;
    }
    
    /**
     *
     */
    private static final long serialVersionUID = -1388022067022793683L;

    /*
     * (non-Javadoc)
     *
     * @see org.jboss.seam.social.oauth.OAuthService#getServiceLogo()
     */
    @Override
    public String getServiceLogo() {
        return LOGO_URL;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jboss.seam.social.oauth.OAuthService#getUser()
     */
    @Override
    protected UserProfile getUser() {
       
            HttpResponse resp = sendSignedRequest(RestVerb.GET, USER_PROFILE_URL);
            return jsonMapper.readValue(resp, UserJackson.class);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jboss.seam.social.oauth.OAuthService#getType()
     */
    @Override
    public String getType() {
        return TYPE;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jboss.seam.social.oauth.HasStatus#updateStatus()
     */
    @Override
    public Object updateStatus() {
        return updateStatus(getStatus());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jboss.seam.social.oauth.HasStatus#updateStatus(java.lang.String)
     */
    @Override
    public Object updateStatus(String message) {
        HttpResponse resp = sendSignedRequest(RestVerb.POST, STATUS_UPDATE_URL, "message", message);
        log.debugf("Update staut is %s", message);
        setStatus("");
        log.debugf("Response is : %s",resp.getBody());
        return null;
    }

   
}