package wanted.budgetmanagement.domain.user.entity;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import wanted.budgetmanagement.exception.CustomException;
import wanted.budgetmanagement.exception.ErrorCode;
import wanted.budgetmanagement.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsManager implements UserDetailsManager {
    private final UserRepository userRepository;

    @Override
    public void createUser(UserDetails user) {

    }

    @Override
    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() ->
                new CustomException(ErrorCode.USER_NOT_FOUND));

        return CustomUserDetails.fromEntity(userEntity);
    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }
}
