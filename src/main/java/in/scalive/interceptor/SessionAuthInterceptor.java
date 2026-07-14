package in.scalive.interceptor;

import java.io.PrintWriter;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
@Component
public class SessionAuthInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession(false);
		System.out.println("Path :" + request.getRequestURI());
		System.out.println("Method :" + request.getMethod());
		System.out.println("session presemt  :" + (session != null));
		if (session != null) {
			System.out.println("session id :" + session.getId());
			System.out.println("user id: " + session.getAttribute("userId"));
		}
		if (session == null || session.getAttribute("userId") == null) {
			response.setStatus(401);
			response.setContentType("application/json");
			PrintWriter pw = response.getWriter();
			pw.write("{\"error\":\"please login first\"}");
			return false;
		}
		Long userId = (Long) session.getAttribute("userId");
		String userRole = (String) session.getAttribute("userRole");

		request.setAttribute("userRole", userRole);
		request.setAttribute("userId", userId);

		String path = request.getRequestURI();
		String method = request.getMethod();
		if(path.equals("/api/categories")) {
			if(!method.equals("GET") && !userRole.equals("ADMIN")) {
				response.setStatus(403);
				response.setContentType("application/json");
				PrintWriter pw = response.getWriter();
				pw.write("{\"error\":\"Admin is required\"}");
				return false;
			}
		}
		return true;
	}

}
