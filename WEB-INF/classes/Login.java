import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public String[] alus = {"12345678W", "23456387R", "34567891F", "93847525G", "37264096W"};
	public String[] profs = {"23456733H", "10293756L", "06374291A", "65748923M"};
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

    public class Usuario{
    	
    	public String dni;
    	public String password;
    	
    	public Usuario(String dni, String password) {
    		this.dni = dni;
    		this.password = password;
    	}

    }
    protected boolean checkLoginAlu(String dni) { //true ok, false mal
    	ArrayList <String> alus= new ArrayList<String>();
    	
    	
    	alus.add("12345678W");
    	alus.add("23456387R");
    	alus.add("34567891F");
    	alus.add("93847525G");
    	alus.add("37264096W");	
    	
    	if(alus.contains(dni)) {
    		return true;
    	}else {
    		return false;
    	}
    						
    }
    
    protected boolean checkLoginPro(String dni) { //true ok, false mal

    	ArrayList <String> profs= new ArrayList<String>();
    	
    	profs.add("23456733H");
    	profs.add("10293756L");
    	profs.add("06374291A");
    	profs.add("65748923M");
    	
    	if(profs.contains(dni)) {
    		return true;
    	}else {
    		return false;
    	}
    						
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		
		String dni = request.getParameter("dni");
		String password = request.getParameter("password");

		HttpSession sesion = request.getSession();


        String url = "http://dew-jortaga1-2021.dsic.cloud:9090/CentroEducativo/login";
		//String jsoncutre = "{ \"dni\":\""+ dni + "\",  \"password\":\"" + password + "\" }";
        
		Gson gson = new Gson();
		Usuario usuario = new Usuario(dni, password);
		String enviar = gson.toJson(usuario);
		
		BufferedReader reader = null;
		HttpURLConnection conexion = null;
		StringBuffer buffer = new StringBuffer();
		
		CookieManager cookieManager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
		CookieHandler.setDefault(cookieManager);
		
		try {
			
			URL u = new URL(url);				
			conexion = (HttpURLConnection) u.openConnection();
			conexion.setRequestProperty("Content-Type", "application/json");
			conexion.setRequestMethod("POST");
			conexion.setDoOutput(true);
			conexion.setDoInput(true);
			
			OutputStream stream = conexion.getOutputStream();
			stream.write(enviar.getBytes("UTF-8"));
			stream.close();
			
			String salida = null;
			reader = new BufferedReader(new InputStreamReader(conexion.getInputStream(), "utf-8"));
			
			while((salida = reader.readLine()) != null) {
				
				buffer.append(salida);
				
			}
			
		}
		catch( MalformedURLException e ) { e.printStackTrace(); }
		catch( IOException e ) { e.printStackTrace(); }
		
		finally {
				if(reader != null){
					try { 
							reader.close(); 
						}
					catch( IOException e ) { e.printStackTrace(); }
				}
				if(conexion != null) { 
						conexion.disconnect();
						}
		}
	
		String cookies = cookieManager.getCookieStore().getCookies().toString();
		String result[] =  { buffer.toString() , cookies};
		
		//Cookie cookie = new Cookie("key", result[0]);
		
		String[] arrayCookies = {buffer.toString(), cookies};

		Cookie cookie = new Cookie("key", arrayCookies[0]);
		Cookie cookie2 = new Cookie("dni", dni);
		
		//cookie.setMaxAge();
		response.addCookie(cookie);
		response.addCookie(cookie2);
	
		sesion.setAttribute("user", dni);
		
		//request.authenticate(response);
		
		if(!buffer.toString().equals("-1")) {
			if(checkLoginAlu(dni)) { //request.isUserInRole("rolalu") no va bien
				
//				RequestDispatcher rd = request.getRequestDispatcher("/ListaAsignaturas");
//				rd.forward(request, response);
				response.sendRedirect("/nol2021/ListaAsignaturas");
				
			}else
			if(checkLoginPro(dni)) {
				
//				RequestDispatcher rd = request.getRequestDispatcher("/ListaAsignaturas");
//				rd.forward(request, response);
				
				response.sendRedirect("/nol2021/AsigsProf");
				
			}else {
				//error
				RequestDispatcher rd = request.getRequestDispatcher("login.html");
				rd.forward(request, response);
			}
		}else {
			//error 

			RequestDispatcher rd = request.getRequestDispatcher("login.html");
			rd.forward(request, response);
		}
		
	}
	
}
