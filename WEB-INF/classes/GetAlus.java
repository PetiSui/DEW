import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class GetAlus
 */
@WebServlet("/GetAlus")
public class GetAlus extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetAlus() {
        super();
        // TODO Auto-generated constructor stub
    }

    public class Alumno{
	 	public String alumno;
		public String nota;

		
	public Alumno(String a, String n) {
		this.alumno = a;
		this.nota = n;
	
		}
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		String acronimo = request.getParameter("acronimo");
		
		Cookie[] cookies = request.getCookies();
		String key = "";
		
		for(Cookie c : cookies) {
			if(c.getName().equals("key")) {
				key = c.getValue();
			}
		}
				      
		String url = "http://dew-jortaga1-2021.dsic.cloud:9090/CentroEducativo/asignaturas/" + acronimo + "/alumnos?key=" + key;
		
		BufferedReader reader = null;
		HttpURLConnection conexion = null;
		StringBuffer buffer = new StringBuffer();
		
		try { //recoger info de alumno
			
			URL u = new URL(url);				
			conexion = (HttpURLConnection) u.openConnection();
			conexion.setRequestProperty("Content-Type", "application/json");
			conexion.setRequestMethod("GET");
			conexion.setDoOutput(true);
			conexion.setDoInput(true);
			
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
		
		String resultado = buffer.toString();
		
		Gson gson = new Gson();
		
		String json = gson.toJson(resultado);
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		PrintWriter pw = response.getWriter();
		
		pw.print(json);
		pw.flush();
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
