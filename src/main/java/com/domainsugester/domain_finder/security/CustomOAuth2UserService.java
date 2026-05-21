package com.domainsugester.domain_finder.security;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {

        // 1. Let Spring fetch the user attributes from Google
        OAuth2User oAuth2User = delegate.loadUser(request);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 2. Extract the fields we care about
        String googleId = (String) attributes.get("sub");
        String name     = (String) attributes.get("name");
        String email    = (String) attributes.get("email");
        String picture  = (String) attributes.get("picture");

        // 3. Find or create the user in MongoDB
        User user = userRepository.findByGoogleId(googleId)
                .map(existing -> updateExistingUser(existing, name, picture))
                .orElseGet(() -> createNewUser(googleId, name, email, picture));

        // 4. Return our custom principal
        return new AuthenticatedUser(user, attributes);
    }

    private User createNewUser(String googleId, String name, String email, String picture) {
        User newUser = User.builder()
                .googleId(googleId)
                .name(name)
                .email(email)
                .pictureUrl(picture)
                .createdAt(Instant.now())
                .lastLoginAt(Instant.now())
                .build();
        return userRepository.save(newUser);
    }

    private User updateExistingUser(User user, String name, String picture) {
        user.setName(name);
        user.setPictureUrl(picture);
        user.setLastLoginAt(Instant.now());
        return userRepository.save(user);
    }
}
