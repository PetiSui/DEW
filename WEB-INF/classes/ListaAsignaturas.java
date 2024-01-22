

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
 * Servlet implementation class ListaAsignaturas
 */
@WebServlet("/ListaAsignaturas")
public class ListaAsignaturas extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListaAsignaturas() {
        super();
        // TODO Auto-generated constructor stub
    }
    
   
	public class Asignatura{
		 	public String asignatura;
			public String nota;
			
		public Asignatura(String a, String n) {
			this.asignatura = a;
			this.nota = n;
    	
		}
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		PrintWriter pw = response.getWriter();
		response.setContentType("text/html");
		String key = "";
		String dni = "";
		Cookie[] cookies = request.getCookies();
		
		for(Cookie c : cookies) {
			if(c.getName().equals("key")) {
				key = c.getValue();
				//pw.println("<p>La cookie " + c.getName() + " tiene el valor " + c.getValue() + "</p></br>");				
			}
			if(c.getName().equals("dni")) {
				dni = c.getValue();
			}
		}
		
//		pw.println("<html><head><title>Prueba</title></head>");
//		pw.println("<body><p>La key es: " + cookies[0].getValue());
//		pw.println("</p></body></html>");

		//HttpSession sesion = request.getSession();

        String url = "http://dew-jortaga1-2021.dsic.cloud:9090/CentroEducativo/alumnos/" + dni + "/asignaturas?key=" + key;
		
		BufferedReader reader = null;
		HttpURLConnection conexion = null;
		StringBuffer buffer = new StringBuffer();
		
		try { //Recoger info de asignaturas
			
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
		
		pw.println("<!DOCTYPE html>\n" + 
				"<html>\n" + 
				"    <head>\n" + 
				"        <meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\">\n" + 
				"        <title>Bienvenido</title>\n" + 
				"        <link rel=\"stylesheet\" href=\"./css/bootstrap.min.css\">\n" + 
				"        <link rel=\"stylesheet\" href=\"./welcome.css\">\n" + 
				"    </head>\n" + 
				"    <body background='/nol2021/img/background.jpg'>\n" + 
				"	<div class=\"container\">\n" + 
				"        <div class=\"row azul aumentar\" align=\"center\">    \n" + 
				"<h1>Alumno: " + dni + "</h1>" +
				"            <h1>Bienvenid@ a Notas Online</h1>\n" + 
				"            <p>Una aplicaci&oacute;n que cuesta m&aacute;s de lo que parece para conseguir menos de lo que cre&iacute;as... ¿¡Qu&eacute; m&aacute;s se puede pedir!?</p>\n" + 
				"        </div>\n" + 
				"\n" + 
				"        <div class=\"row\">\n" + 
				"\n" + 
				"        	<div class=\"col-7 row align-items-center\">\n" + 
				"        	<div align=\"center\">");
		
		for(Asignatura a : asigs) {
			
			pw.println("<a class=\"btn btn-primary \" href='/nol2021/InfoAsignatura?asig=" + a.asignatura + "' role=\"button\" style=\"display:block;width:200px\"" + "'>" + a.asignatura + "</a><br/>");
		}
		//pw.println("<p>" + asigs.toString() + "</p>");
		pw.println("</div>\n" + 
				"			</div>\n" + 
				"               \n" + 
				"                    <div class=\"col-5 lista\" align=\"left\">\n" + 
				"                        <h2>Grupo g01_labo_Miercoles</h2>\n" + 
				"                        <ol>\n" + 
				"                            <li>Abel Ferrer</li>\n" + 
				"                            <li>Edu Navarro</li>\n" + 
				"                            <li>Gabriel Sales</li>\n" + 
				"                            <li>Jorge Tarazona</li>\n" + 
				"                            <li>Marcos Alejos</li>\n" + 
				"                            <li>Sergio Lurbe</li>\n" + 
				"                        </ol>\n" + 
				"                    </div>\n" + 
				"                </div>\n" + 
				"\n" + 
				"        <div id=\"footer\" class=\"peque\" align=\"center\">\n" + 
				"            <hr>\n" + 
				"            <p>Trabajo en grupo realizado para la asignatura Desarrollo Web. Curso 2020-2021 (aka el curso del COVID-19, en toda la frente)</p>\n" + 
				"        </div>\n" + 
				"    </div>    \n" + 
				"	<script src=\"https://code.jquery.com/jquery-3.6.0.min.js\"></script>\n" + 
				"	<script src=\"https://unpkg.com/@popperjs/core@2\"></script>\n" + 
				"	<script src=\"./js/bootstrap.min.js\"></script>\n" + 
				"    </body>\n" + 
				"</html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	
}
