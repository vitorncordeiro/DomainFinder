package com.domainsugester.domain_finder.security;

import com.domainsugester.domain_finder.model.UserModel;
import com.domainsugester.domain_finder.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = delegate.loadUser(request);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String googleId = (String) attributes.get("sub");
        String name     = (String) attributes.get("name");
        String email    = (String) attributes.get("email");
        String picture  = (String) attributes.get("picture");

        UserModel user = userRepository.findByGoogleId(googleId)
                .map(existing -> updateExistingUser(existing, name, picture))
                .orElseGet(() -> createNewUser(googleId, name, email, picture));

        return new AuthenticatedUser(user, attributes);
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
        return userRepository.save(newUser);
    }

    private UserModel updateExistingUser(UserModel user, String name, String picture) {
        user.setName(name);
        user.setPictureUrl(picture);
        user.setLastLoginAt(Instant.now());
        return userRepository.save(user);
    }
}
