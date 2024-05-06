// UserController.java
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    @GetMapping("/user/{resource}")
    public ResponseEntity<String> getResource(@PathVariable String resource, HttpServletRequest request) {
        // Check if the user making the request is authenticated and has access to the resource
        String role = (String) request.getAttribute("role");
        if (role != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader("access_info.txt"))) {
                String line;
                Map<Integer, String> accessMap = new HashMap<>();
                while ((line = reader.readLine()) != null) {
                    // Parse access information from the file
                    String[] parts = line.split(", ");
                    int userId = Integer.parseInt(parts[0].split(": ")[1]);
                    String endpoints = parts[1].split(": ")[1];
                    accessMap.put(userId, endpoints);
                }
                // Check if the user has access to the requested resource
                if (accessMap.containsKey(request.getUserId()) && accessMap.get(request.getUserId()).contains(resource)) {
                    return ResponseEntity.ok("Success: You have access to resource '" + resource + "'");
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Failure: You don't have access to resource '" + resource + "'");
                }
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while accessing resource");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You need to log in to access this resource");
        }
    }
}
