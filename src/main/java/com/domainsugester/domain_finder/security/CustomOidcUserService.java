package com.domainsugester.domain_finder.security;

import com.domainsugester.domain_finder.model.UserModel;
import com.domainsugester.domain_finder.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {

    private final UserRepository userRepository;

    @Override
    public OidcUser loadUser(OidcUserRequest request) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(request);

        String googleId = oidcUser.getSubject();
        String name     = oidcUser.getFullName();
        String email    = oidcUser.getEmail();
        String picture  = oidcUser.getPicture();

        UserModel user = userRepository.findByGoogleId(googleId)
                .map(existing -> updateExistingUser(existing, name, picture))
                .orElseGet(() -> createNewUser(googleId, name, email, picture));

        return new AuthenticatedUser(user, oidcUser);
    }

    private UserModel createNewUser(String googleId, String name, String email, String picture) {
        UserModel newUser = UserModel.builder()
                .googleId(googleId)
                .name(name)
                .email(email)
                .pictureUrl(picture)
                .createdAt(Instant.now())
                .lastLoginAt(Instant.now())
                .build();

        var save = userRepository.save(newUser);
        return save;
    }

    private UserModel updateExistingUser(UserModel user, String name, String picture) {
        user.setName(name);
        user.setPictureUrl(picture);
        user.setLastLoginAt(Instant.now());
        return userRepository.save(user);
    }
}
