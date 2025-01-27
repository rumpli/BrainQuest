package ch.quizinno.brainquest.shells;

import ch.quizinno.brainquest.entities.User;
import ch.quizinno.brainquest.services.UserService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.Optional;
import java.util.Scanner;

/**
 * Shell component for creating a new user
 */
// Spring annotation to indicate that this class is a shell component.
@ShellComponent
public class CreateUser {
    /**
     * User service
     */
    private final UserService userService;

    /**
     * Constructor
     *
     * @param userService User service
     */
    public CreateUser(UserService userService) {
        this.userService = userService;
    }

    /**
     * Create a new user
     *
     * @return a message indicating the result of the operation
     */
    // Spring annotation to indicate that this method is a shell method.
    @ShellMethod("Create a new user")
    public String createUser() {
        // Create a scanner object for user input
        Scanner scanner = new Scanner(System.in);

        // Prompt for username
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        // Check that the user does not already exist
        Optional<User> userOpt = userService.getUserByUsername(username);
        if (!userOpt.isEmpty()) {
            return "User " + username + " already exists";
        }

        // Prompt for password
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        // Create a new user
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        userService.saveUser(user);

        return "User " + username + " created";
    }

    /**
     * Change password
     *
     * @return a message indicating the result of the operation
     */
    @ShellMethod("Change password")
    public String changePassword() {
        // Create a scanner object for user input
        Scanner scanner = new Scanner(System.in);

        // Prompt for username
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        // Initialize user
        User user = new User();
        // Check if user exists
        Optional<User> userOpt = userService.getUserByUsername(username);
        if (userOpt.isEmpty()) {
            return "User " + username + " does not exist";
        } else {
            // user must not be type Optional<User>
            user = userOpt.get();
        }

        // Prompt for password
        System.out.print("Enter new password: ");
        String password = scanner.nextLine();

        // Change password
        user.setPassword(password);
        userService.saveUser(user);

        return "Password for user " + username + " changed";
    }
}
