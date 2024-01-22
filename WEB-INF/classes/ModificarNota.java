

import java.io.IOException;
import java.io.OutputStream;
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
 * Servlet implementation class ModificarNota
 */
@WebServlet("/ModificarNota")
public class ModificarNota extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ModificarNota() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		Cookie[] cookies = request.getCookies();
		String key = "";
		
		for(Cookie c : cookies) {
			if(c.getName().equals("key")) {
				key = c.getValue();
			}
		}
		
		String acronimo = (String) request.getParameter("acronimo");
		String alumno = request.getParameter("alumno");
		String nota = request.getParameter("nota");
		
    	String urlaux = "http://dew-jortaga1-2021.dsic.cloud:9090/CentroEducativo/alumnos/" + alumno + "/asignaturas/" + acronimo + "?key=" + key;

 		try {
    	URL url = new URL(urlaux);
    	HttpURLConnection conexion = null;
    	conexion = (HttpURLConnection) url.openConnection();
		conexion.setRequestProperty("Content-Type", "application/json");
		//conexion.setRequestProperty("Accept", "application/json");
		conexion.setRequestMethod("PUT");
		conexion.setDoOutput(true);
		conexion.setDoInput(true);
		
		OutputStream stream = conexion.getOutputStream();
		
		Gson gson = new Gson();
		
		String json = gson.toJson(nota);
		
		stream.write(json.getBytes("UTF-8"));
		stream.flush();
		stream.close();
		
		if(conexion.getResponseCode() != 200) {
			throw new RuntimeException("ERROR");
		}
		
		
 		}catch( MalformedURLException e ) { e.printStackTrace(); }
		catch( IOException e ) { e.printStackTrace(); }
		
 		
	}

}
