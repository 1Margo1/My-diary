package com.example.demo1;
import javafx.scene.Parent;
import java.io.IOException;

public interface PageLoader {
    Parent loadPage(String fxmlPath) throws IOException;
}
//factory