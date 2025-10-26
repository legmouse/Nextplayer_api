package kr.co.nextplayer.base.backend.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CorsFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		if (request.getHeader("Access-Control-Request-Method") != null
				&& "OPTIONS".equals(request.getMethod())) {
			// CORS "pre-flight" request
			response.addHeader("Access-Control-Allow-Methods", "GET, OPTIONS, POST, PUT, DELETE");
			response.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
			response.addHeader("Access-Control-Max-Age", "3600");
			
			System.err.println("CorsFilter was called->"+request.getMethod());
		}
		filterChain.doFilter(request, response);
	}
}

// example web.xml configuration

/*
 * <filter> <filter-name>cors</filter-name>
 * <filter-class>org.springframework.web
 * .servlet.support.CorsFilter</filter-class> </filter>
 * 
 * <filter-mapping> <filter-name>cors</filter-name>
 * <url-pattern>/*</url-pattern> </filter-mapping>
 */
