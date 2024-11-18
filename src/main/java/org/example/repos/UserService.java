package org.example.repos;


import org.example.model.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(String name, String password) {
        // Проверка, существует ли уже пользователь с таким именем
        if (userRepository.findByName(name).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }
        // Шифрование пароля
        String encryptedPassword = passwordEncoder.encode(password);

        // Создание нового пользователя
        User user = new User();
        user.setName(name);
        user.setPassword(encryptedPassword);  // Используем зашифрованный пароль

        // Сохранение пользователя в базе данных
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        System.out.println("Username: " + name);
        User user = userRepository.findByName(name)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getName()) // имя из БД
                .password(user.getPassword()) // зашифрованный пароль из БД
                .roles("USER") // роль пользователя
                .build();
    }
}
