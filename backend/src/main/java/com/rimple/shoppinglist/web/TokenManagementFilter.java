package com.rimple.shoppinglist.web;

import com.rimple.shoppinglist.model.ShoppingListItem;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Component
@Order(1)
/**
 * Simple token generator to use for a UUID key for a demo app
 * (shopping lists are assigned an ID, and until the client destroys the token
 * (the back-end writes it in a cookie) or destroys the list.
 *
 * The front-end passes it as a cookie each time it performs a request.
 * This filter hits before the controllers so it generates the UUID token
 * if it is not present in the cookie.
 *
 * This is not a scalable example, just something for some demos. You really
 * need real security via Cognito for a robust solution, and to track the
 * lists by the user's Cognito ID or user id, at least.
 */
public class TokenManagementFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
       HttpServletRequest req = (HttpServletRequest) request;
       HttpServletResponse res = (HttpServletResponse) response;

       // do we have a shoppingListId UUID cookie?
       Optional<Cookie> optionalItem =
               Arrays.stream(req.getCookies())
                       .filter(cookie -> cookie.getName().equals("shoppingListId"))
                       .findFirst();
       String uuid;

       // if not, create it
       if (optionalItem.isEmpty()) {
            uuid = UUID.randomUUID().toString();
           res.addCookie(new Cookie("shoppingListId", uuid.toString()));
       } else {
           uuid = optionalItem.get().toString();
       }

       // set as request attribute for controllers
       req.setAttribute("shoppingListId", uuid);
    }
}
