// AdminController.java
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.io.FileWriter;
import java.io.IOException;

@RestController
public class AdminController {

    @PostMapping("/admin/addUser")
    public ResponseEntity<String> addUser(@RequestBody AccessRequest accessRequest) {
        // Check if the user making the request is an admin
        if ("admin".equals(accessRequest.getRole())) {
            try (FileWriter writer = new FileWriter("access_info.txt", true)) {
                // Append access information to the file
                writer.write("User ID: " + accessRequest.getUserId() + ", Endpoints: " + accessRequest.getEndpoints() + "\n");
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while adding access");
            }
            return ResponseEntity.ok("Access added successfully");
        } else {
            // Return error message for non-admin users
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admins can access this endpoint");
        }
    }
}
