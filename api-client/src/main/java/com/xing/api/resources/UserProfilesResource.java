/*
 * Copyright (С) 2015 XING AG (http://xing.com/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xing.api.resources;

import com.xing.api.CallSpec;
import com.xing.api.HttpError;
import com.xing.api.Resource;
import com.xing.api.XingApi;
import com.xing.api.internal.Experimental;
import com.xing.api.model.user.ProfileMessage;
import com.xing.api.model.user.XingUser;

import java.util.List;

/**
 * Represent the <a href="https://dev.xing.com/docs/resources#user-profiles">'User Profiles'</a> resource.
 * This resource provides methods which allow to access the {@linkplain XingUser user} profile information.
 *
 * @author serj.lotutovici
 * @author daniel.hartwich
 */
public class UserProfilesResource extends Resource {
    private static final String ME = "me";

    /**
     * Creates a resource instance. This should be the only constructor declared by child classes.
     *
     * @param api An instance of XingApi
     */
    protected UserProfilesResource(XingApi api) {
        super(api);
    }

    /**
     * Gets a list of particular {@linkplain XingUser users} profiles. The data returned by this call will be checked
     * and filtered on the basis of the privacy settings of each requested user.
     * <p>
     * Possible optional query parameters are:
     * <li><strong>fields</strong> - List of user attributes to return. If this parameter is not used,
     * the full user profile will be returned.</li>
     *
     * @param ids Id's of the requested {@linkplain XingUser user}.
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/get/users/:id">'Get user details' resource page</a>
     */
    public CallSpec<List<XingUser>, HttpError> getUsersById(List<String> ids) {
        return Resource.<List<XingUser>, HttpError>newGetSpec(api, "/v1/users/{ids}")
              .pathParam("ids", ids)
              .responseAs(list(XingUser.class, "users"))
              .build();
    }

    /**
     * Gets a particular {@linkplain XingUser user’s} profile. The data returned by this call will be checked and
     * filtered on the basis of the privacy settings of the requested user.
     * <p>
     * Possible optional query parameters are:
     * <li><strong>fields</strong> - List of user attributes to return. If this parameter is not used,
     * the full user profile will be returned.</li>
     *
     * @param id Id of the requested {@linkplain XingUser user}.
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/get/users/:id">'Get user details' resource page</a>
     */
    public CallSpec<XingUser, HttpError> getUserById(String id) {
        return Resource.<XingUser, HttpError>newGetSpec(api, "/v1/users/{id}")
              .pathParam("id", id)
              .responseAs(first(XingUser.class, "users"))
              .build();
    }

    /**
     * Gets the profile of the authorizing {@linkplain XingUser user} (The user who has granted access to the API
     * consumer).
     * <p>
     * Possible optional query parameters are:
     * <li><strong>fields</strong> - List of user attributes to return. If this parameter is not used,
     * the full user profile will be returned.</li>
     *
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/get/users/:id">'Get user details' resource page</a>
     */
    public CallSpec<XingUser, HttpError> getOwnProfile() {
        return getUserById(ME);
    }

    /**
     * Get the minimal profile information of the authorizing {@linkplain XingUser user}.
     * <p>
     * If more user details required consider using {@link UserProfilesResource#getOwnProfile()}.
     *
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/get/users/me/id_card">'Get your id card' resource page</a>
     */
    public CallSpec<XingUser, HttpError> getOwnIdCard() {
        return Resource.<XingUser, HttpError>newGetSpec(api, "/v1/users/me/id_card")
              .responseAs(single(XingUser.class, "id_card"))
              .build();
    }

    /**
     * Gets the list of users that belong directly to the given list of email addresses. The {@linkplain XingUser
     * users} will be returned in the same order as the corresponding email addresses. If addresses are invalid or no
     * user was found, the user will be returned as {@code null}.
     * <p>
     * Possible optional query parameters are:
     * <li><strong>hash_function</strong> - Hash emails values using the specified function. Currently only
     * <strong>MD5</strong> is supported.</li>
     * <li><strong>user_fields</strong> - List of user attributes to return in user objects. If this
     * parameter is not set, only the {@linkplain XingUser user} ID will be set.</li>
     *
     * @param emails List of email addresses to search for.
     * @return A {@linkplain CallSpec callSpec} object ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/get/users/find_by_emails">'Find users by email address' resource page</a>
     */
    public CallSpec<List<XingUser>, HttpError> findUsersByEmail(List<String> emails) {
        return Resource.<List<XingUser>, HttpError>newGetSpec(api, "/v1/users/find_by_emails")
              .responseAs(list(single(XingUser.class, "user"), "results", "items"))
              .queryParam("emails", emails)
              .build();
    }

    /**
     * Returns the list of users found in accordance with the given list of keywords.
     * <p>
     * This call is EXPERIMENTAL and is not suited for a production environments as it
     * may be missing some functionality, and both input and output interfaces are subject to change. Only certain
     * consumers are granted access to experimental calls. When in doubt, please contact the API team.
     * <p>
     * Possible optional query parameters are:
     * <li><strong>limit</strong> - Restricts the number of profile visits to be returned. This must be a positive
     * number. Default: 10, Maximum: 100.</li>
     * <li><strong>offset</strong> - Offset. This must be a positive number. Default: 0.</li>
     * <li><strong>user_fields</strong> - List of user attributes to return in nested user objects. If this parameter
     * is
     * not used, only the ID will be returned.</li>
     *
     * @param keywords A String representing the keywords to search for, if this is empty the search will
     * return and empty set
     * @return A {@linkplain CallSpec callSpec} object ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/get/users/find">'Find users by keywords' resource page</a>
     */
    @Experimental
    public CallSpec<List<XingUser>, HttpError> findUsersByKeyword(String keywords) {
        return Resource.<List<XingUser>, HttpError>newGetSpec(api, "/v1/users/find")
              .responseAs(list(single(XingUser.class, "user"), "users", "items"))
              .queryParam("keywords", keywords)
              .build();
    }

    /**
     * Returns the recent profile message for the {@linkplain XingUser user} with the given ID.
     *
     * @return A {@linkplain CallSpec callSpec} object ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/get/users/:user_id/profile_message">'Get user profile message' resource
     * page</a>
     */
    public CallSpec<ProfileMessage, HttpError> getUserProfileMessage(String userId) {
        return Resource.<ProfileMessage, HttpError>newGetSpec(api, "/v1/{user_id}/profile_message")
              .pathParam("user_id", userId)
              .responseAs(single(ProfileMessage.class, "profile_message"))
              .build();
    }

    /**
     * Returns the recent profile message for the authorizing {@linkplain XingUser user}.
     *
     * @return A {@linkplain CallSpec callSpec} object ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/get/users/:user_id/profile_message">'Get user profile message' resource
     * page</a>
     */
    public CallSpec<ProfileMessage, HttpError> getOwnProfileMessage() {
        return getUserProfileMessage(ME);
    }

    /**
     * Returns the legal information for the {@linkplain XingUser user} with the given ID.
     *
     * @param userId The ID of the user from whom to retrieve the legal information.
     * @return A {@linkplain CallSpec callSpec} object ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/get/users/:user_id/legal_information">'Get legal information of a user'
     * resource page</a>
     */
    public CallSpec<String, HttpError> getUserLegalInformation(String userId) {
        return Resource.<String, HttpError>newGetSpec(api, "/v1/users/{user_id}/legal_information")
              .pathParam("user_id", userId)
              .responseAs(single(String.class, "legal_information", "content"))
              .build();
    }

    /**
     * Returns the legal information for the authorizing {@linkplain XingUser user}.
     *
     * @return A {@linkplain CallSpec callSpec} object ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/get/users/:user_id/legal_information">'Get legal information of a user'
     * resource page</a>
     */
    public CallSpec<String, HttpError> getOwnLegalInformation() {
        return getUserLegalInformation(ME);
    }
}