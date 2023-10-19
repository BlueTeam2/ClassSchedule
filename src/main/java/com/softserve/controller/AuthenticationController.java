package com.softserve.controller;

import com.softserve.dto.AuthenticationRequestDTO;
import com.softserve.dto.AuthenticationResponseDTO;
import com.softserve.dto.MessageDTO;
import com.softserve.dto.RegistrationRequestDTO;
import com.softserve.entity.User;
import com.softserve.mapper.UserMapper;
import com.softserve.security.jwt.JwtTokenProvider;
import com.softserve.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.softserve.service.impl.UserServiceImpl.PASSWORD_FOR_SOCIAL_USER;

@RestController
@RequestMapping("/auth")
@Api(tags = "Authentication API")
@Slf4j
@PropertySource({"classpath:cors.properties"})
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final UserMapper userMapper;
    @Value("${backend.url}")
    private String url;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
                                    UserService userService, UserMapper userMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/sign-in")
    @ApiOperation(value = "Get credentials  for login")
    public ResponseEntity<Object> signIn(@RequestBody AuthenticationRequestDTO requestDto) {
        log.info("Enter into signIn method with user email {}", requestDto.getEmail());
        User user = userService.findSocialUser(requestDto.getEmail()).orElseThrow(() ->
                new BadCredentialsException("Invalid password or email")
        );
        if (user.getPassword().equals(PASSWORD_FOR_SOCIAL_USER)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageDTO("You registered via social network. Please, sign in via social network."));
        }
        String username = requestDto.getEmail();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));
        String token = jwtTokenProvider.createToken(username, user.getRole().toString());

        return ResponseEntity.ok(new AuthenticationResponseDTO(username, token));
    }

    @PostMapping("/sign-up")
    @ApiOperation(value = "Get credentials for registration")
    public ResponseEntity<MessageDTO> signUp(@RequestBody RegistrationRequestDTO registrationDTO) {
        log.info("Enter into signUp method with user email {}", registrationDTO.getEmail());
        User user = userMapper.toCreateUser(registrationDTO);
        User createUser = userService.registration(user);
        String message = "You have successfully registered. Please, check Your email '" + createUser.getEmail() + "' to activate profile.";
        MessageDTO messageDTO = new MessageDTO(message);

        return ResponseEntity.status(HttpStatus.CREATED).body(messageDTO);
    }

    @PutMapping("/activation-account")
    @ApiOperation(value = "Update token after activation successfully account")
    public ResponseEntity<MessageDTO> activationAccount(@RequestParam("token") String token) {
        log.info("Enter into activationAccount method");
        User user = userService.findByToken(token);
        user.setToken(null);
        userService.update(user);

        return ResponseEntity.status(HttpStatus.OK).body(new MessageDTO("You successfully activated Your account."));
    }

    @PutMapping("/reset-password")
    @ApiOperation(value = "Reset password by email")
    public ResponseEntity<MessageDTO> resetPassword(@RequestParam("email") String email) {
        log.info("Enter into resetPassword method  with email:{}", email);
        userService.resetPassword(email);

        return ResponseEntity.ok().body(new MessageDTO("Check Your email, please. A new password has been sent to Your email."));
    }

    @PostMapping("/sign-out")
    @ApiOperation(value = "Making the logout")
    public void signOut(HttpServletRequest rq, HttpServletResponse rs) {
        log.info("Enter into signOut method");
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(rq, rs, null);
    }

    @GetMapping("/social/login-success")
    @ApiOperation(value = "Get token after successful sign in via social network")
    public ResponseEntity<MessageDTO> getLoginInfo(@RequestParam("token") String token) {
        log.info("Enter into getLoginInfo method");
        return ResponseEntity.ok().body(new MessageDTO(token));
    }

    @GetMapping("/google")
    public ResponseEntity<String> getGoogleSignIn(HttpServletResponse response) throws IOException {
        response.sendRedirect(url + "oauth_login/google");
        return ResponseEntity.ok().body("Ok");
    }
}
