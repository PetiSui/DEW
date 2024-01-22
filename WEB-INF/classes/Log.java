

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter implementation class Log
 */
@WebFilter(
		urlPatterns = { "/Log" }, 
		servletNames = { 
				"DetallesAsignatura", 
				"InfoAsignatura", 
				"ListaAsignaturas", 
				"Login"
		})
public class Log implements Filter {

    /**
     * Default constructor. 
     */
    public Log() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response; 
	   
		LocalDateTime fecha = LocalDateTime.now();
		
		//PrintWriter html = response.getWriter();
		
		FileWriter writer = null;
		BufferedWriter out = null;
		String user = null;
		
		
		
		try {
			
			File file = new File("/home/user/tomcat/bin/logNOL.txt");
			if(!file.exists()){  // Comprobar si existe el archivo en cuestión.
			   file.createNewFile(); // Crear el archivo.
			}
			writer = new FileWriter(file.getAbsolutePath(), true);
			out = new BufferedWriter(writer);
			
			String dni = "";
			
			Cookie[] cookies = httpServletRequest.getCookies();
			if (cookies != null){
				for(Cookie c : cookies) {
					if(c.getName().equals("dni")) {
						user = c.getValue();
					}
				}
			}else {
				user = "No Disponible";
			}
			
			//user = httpServletRequest.getParameter("user");	
			
			out.write(fecha.toString() + " " +  user + " " + httpServletRequest.getRemoteAddr() + " " + httpServletRequest.getServletPath().substring(1) + " acceso " +httpServletRequest.getMethod() + "\n");
			
		}catch(IOException e) {
			
		}finally {
	        try {	    
	            if (out != null)
	                out.close();
	            if (writer != null)
	                writer.close();
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
	        
		}
		// pass the request along the filter chain
		chain.doFilter(httpServletRequest, httpServletResponse);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
