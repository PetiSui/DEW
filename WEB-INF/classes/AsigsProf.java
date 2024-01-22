

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
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

/**
 * Servlet implementation class AsigsProf
 */
@WebServlet("/AsigsProf")
public class AsigsProf extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AsigsProf() {
        super();
        // TODO Auto-generated constructor stub
    }

    public class Asignatura{
	 	public String acronimo;
		public String creditos;
		public String cuatrimestre;
		public String curso;
		public String nombre;
		
	public Asignatura(String acr, String cre, String cua, String cur, String nom) {
		this.acronimo = acr;
		this.creditos = cre;
		this.cuatrimestre = cua;
		this.curso = cur;
		this.nombre = nom;
	}
}
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
HttpSession sesion = request.getSession();
		
		String key = "";
		String dni = "";
		Cookie[] cookies = request.getCookies();
		
		for(Cookie c : cookies) {
			if(c.getName().equals("key")) {
				key = c.getValue();			
			}
			if(c.getName().equals("dni")) {
				dni = c.getValue();
			}
		}

        String url = "http://dew-jortaga1-2021.dsic.cloud:9090/CentroEducativo/profesores/" + dni + "/asignaturas?key=" + key;
		
		BufferedReader reader = null;
		HttpURLConnection conexion = null;
		StringBuffer buffer = new StringBuffer();
		
		try { //recoger las asignaturas del profesor
			
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
		
		Asignatura[] asigs = gson.fromJson(resultado,Asignatura[].class);
		
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		
		pw.println("<!DOCTYPE html>\n" + 
				"<html>\n" + 
				"<head>\n" + 
				"<meta charset=\"UTF-8\">\n" + 
				"<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n" + 
				"<meta http-equiv=\"Content-Type\" content=\"text/html\">" +
				"<title>Asignaturas Profesor</title>\n" + 
				"<link rel=\"stylesheet\" href=\"./css/bootstrap.min.css\">\n" + 
				"</head>\n" + 
				"<body background='/nol2021/img/background.jpg'>\n" + 
				"<div class=\"container-fluid\">\n" + 
				"	<div class=\"row\">\n" + 
				"		<div class=\"col-9\" align=\"center\">\n" + 
				"		</div>		\n" + 
				"		<div class=\"col-3\" align=\"right\">\n" + 
				"			<a class=\"btn btn-primary\" href=\"/nol2021/CerrarSesion\" role=\"button\" style=\"width:150px\">Cerrar Sesión</a>\n" + 
				"		</div>\n" + 
				"	</div>\n" + 
				"	<div class=\"col-12\" align=\"center\">\n" +
				"		<h1 style=\"font-size:48px;color:black;\">Profesor (" + dni + ") - Asignaturas:</h1>\n" +
				"	</div>" +
				"</div><br/>\n" + 
				"<div class=\"container-fluid\" align=\"center\">");
		
		for(Asignatura a : asigs) {
			
			pw.println("<div class=\"row border border-dark w-75 p-3 shadow p-3 mb-5 bg-white rounded\">\n");
			
			pw.println("<a class=\"btn btn-primary\" href='listaAlu.html?acronimo=" + a.acronimo + "' role=\"button\" style=\"width:200px\" '>" + a.acronimo + "</a>");
			pw.println("<p align=\"left\" style=\"color:grey;font-size:11px;\">(Haga click para ver los detalles)</p><br/>");
			
			pw.println("<p><code>Nombre: </code>" + a.nombre + "</p><br/>");
			pw.println("<p><code>Créditos: </code>" + a.creditos + "</p><br/>");
			pw.println("<p><code>Curso: </code>" + a.curso + "</p><br/>");
			pw.println("<p><code>Cuatrimestre: </code>" + a.cuatrimestre + "</p>");
			
			pw.println("</div><br/>");
			
		}
		
		pw.write("</div>\n" + 
				"	<script src=\"https://code.jquery.com/jquery-3.6.0.min.js\"></script>\n" + 
				"	<script src=\"https://unpkg.com/@popperjs/core@2\"></script>\n" + 
				"	<script src=\"./js/bootstrap.min.js\"></script>\n" + 
				"</body>\n" + 
				"</html>");
	
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
	}

}
