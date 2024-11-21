package TTT.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) throws IOException {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                printError(request);
                return "error/404";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                printError(request);
                return "error/500";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                printError(request);
                return "error/403";
            } else if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
                printError(request);
                return "error/401";
            } else if (statusCode == HttpStatus.BAD_REQUEST.value()) {
                printError(request);
                return "error/400";
            }
        }
        return "error/generic";
    }

    private void printError(HttpServletRequest request) throws IOException {
        String s = request.getReader().readLine();

        while (s != null) {
            System.out.println(s);
            s = request.getReader().readLine();
        }
    }

}
