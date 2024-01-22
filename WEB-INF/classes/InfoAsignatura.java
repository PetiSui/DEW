

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
 * Servlet implementation class InfoAsignatura
 */
@WebServlet("/InfoAsignatura")
public class InfoAsignatura extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InfoAsignatura() {
        super();
        // TODO Auto-generated constructor stub
    }
    public class Alumno{
	 	public String dni;
		public String nombre;
		public String apellidos;
		
	public Alumno(String d, String n, String a) {
		this.dni = d;
		this.nombre = n;
		this.apellidos = a;
	
		}
    }
	public class Asignatura{
	 	public String asignatura;
		public String nota;
		
	public Asignatura(String a, String n) {
		this.asignatura = a;
		this.nota = n;
	
		}
	}
	 
	public String GetNota(String nota) {			
		
		
		if(nota.equals("")) {
			return "Por calificar"; 
		}
		return nota;
		
	}
	
	public String GetImgRuta(String dni){
		
		int indice = (int)(60 * Math.random());
		int dni2 = Integer.parseInt(dni.substring(0,1));
		String res;
		
		if(dni2 < 4) {//hombre
			res = "/nol2021/img/h/" + indice + ".png";
		}else { //mujer
			res = "/nol2021/img/m/" + indice + ".png";
		}
		
		return res;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		PrintWriter pw = response.getWriter();
		response.setContentType("text/html");
		String key = "";
		String dni = "";
		Cookie[] cookies = request.getCookies();
		
		String asigAmostrar = request.getParameter("asig");
		
		for(Cookie c : cookies) {
			if(c.getName().equals("key")) {
				key = c.getValue();
				//pw.println("<p>La cookie " + c.getName() + " tiene el valor " + c.getValue() + "</p></br>");				
			}
			if(c.getName().equals("dni")) {
				dni = c.getValue();
			}
		}
		
		//String ruta = GetImgRuta(dni);
		
//		pw.println("<html><head><title>Prueba</title></head>");
//		pw.println("<body><p>La key es: " + cookies[0].getValue());
//		pw.println("</p></body></html>");

		HttpSession sesion = request.getSession();

        String url = "http://dew-jortaga1-2021.dsic.cloud:9090/CentroEducativo/alumnos/" + dni + "?key=" + key;
		
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
		
		String resultado = buffer.toString(); //de alumnos
		
		url = "http://dew-jortaga1-2021.dsic.cloud:9090/CentroEducativo/alumnos/" + dni + "/asignaturas?key=" + key;
		reader = null;
		conexion = null;
		buffer = new StringBuffer();
		
		try { //Recoger info de asignaturas del alumno
			
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
		
		String resultado2 = buffer.toString(); //de asignaturas	
		
		
		Gson gson = new Gson();

		Alumno alum = gson.fromJson(resultado,Alumno.class);
		
		Asignatura[] asigs = gson.fromJson(resultado2,Asignatura[].class);
		
		pw.write("<!DOCTYPE html>\n" + 
				"<html>\n" + 
				"<head>\n" + 
				"<meta charset=\"UTF-8\">\n" + 
				"<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n" + 
				"<title>Nota Alumno</title>\n" + 
				"<link rel=\"stylesheet\" href=\"./css/bootstrap.min.css\">\n" + 
 				"</head>\n" + 
				"<body background='/nol2021/img/background.jpg'>\n" + 
				"\n" + 
				"<div class=\"container\">\n" + 
				"	<div class=\"row\">\n" + 
				"		<div class=\"col-12\" align=\"center\">\n"
		
				+"<h2>" + alum.apellidos + ", " + alum.nombre + " (" + alum.dni + ")" + "</h2>" + 
				"		</div>\n" + 
				"	</div></br>\n" + 
				"	<div class=\"row\">\n" + 
				"		<div class=\"col-3\" id=\"foto\"> <img src='img/"+ dni.toString() + ".png'" +" alt='fotoUsuario'></div>\n" + 
				"		<div class=\"col-6\">\n" + 
				"<h3>Matriculad@ en [" );
				
				for(int i = 0; i < asigs.length; i++) {
					if(i == asigs.length - 1) {
						pw.write(asigs[i].asignatura + "]");
					}else {
						pw.write(asigs[i].asignatura + ", ");
					}
				}
				
				pw.write("</h3></br>"+
				"		<p>Lorem ipsum dolor sit amet consectetur adipisicing elit. \n" + 
				"		Excepturi atque provident temporibus amet, nihil deleniti, harum perspiciatis mollitia enim laborum ut. \n" + 
				"		Voluptatem veritatis, rerum eius quaerat velit nobis saepe odio.\n" + 
				"		</p></br></br>\n" + 
				"		</div>\n" + 
				"	</div>\n"); 
				
				for(int i = 0; i < asigs.length; i++) {
					if(asigs[i].asignatura.equals(asigAmostrar)) {
					pw.write("<div class=\"row\">\n" + 
							"		<div class=\"col-12\" align=\"center\">\n" + 
							"<p><strong>Asignatura: </strong>"+ asigs[i].asignatura + ". <strong>Nota: </strong>" + GetNota(asigs[i].nota) + "</p></br>" +
							"		</div>\n" + 
							"	</div></br>");
					}
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
		doGet(request, response);
	}

}
