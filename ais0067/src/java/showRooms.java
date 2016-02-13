/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import static com.sun.org.apache.xalan.internal.lib.ExsltStrings.align;
import static com.sun.org.apache.xalan.internal.lib.ExsltStrings.padding;
import static com.sun.xml.internal.fastinfoset.alphabet.BuiltInRestrictedAlphabets.table;
import static java.awt.Color.blue;
import static java.awt.SystemColor.text;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import static javafx.scene.paint.Color.color;
import static javafx.scene.paint.Color.color;
import static javafx.scene.paint.Color.color;
import static javafx.scene.paint.Color.color;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author dskarpetis
 */
@WebServlet("/showRooms")
public class showRooms extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        response.setContentType("text/html;charset=UTF-8");
        
        String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        String db_URL = "jdbc:mysql://127.0.0.1:3306/anesis";
        String dbUSER = "anesisdba";
        String dbPASS = "12345";
  
        Connection dbConn = null;
        Statement dbStmt = null;  
        
        try (PrintWriter out = response.getWriter()) {
 //============================= sundesh me vash =============================//     
            try
            {
                Class.forName(JDBC_DRIVER);
                dbConn = DriverManager.getConnection(db_URL, dbUSER, dbPASS);
            }
            catch(Exception ex){
                //ex.printStackTrace();
                out.println("<h2>Δεν υπάρχει σύνδεση με τη βαση!!!<h2>");
                dbConn = null;
                boolean ans = false;
              } 
              
            if(dbConn != null)
            try {
//=============== epilogh eggrafwn stoixeiwn ston pinaka rooms ===============//                
                dbStmt =  dbConn.createStatement();
                String sql = "SELECT id, room, arrival, departure, total, name, "
                      + "pnumber FROM rooms";

                ResultSet dbRs = dbStmt.executeQuery(sql);

                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\">");
                out.println("<title>Hotel Anesis</title>");
                out.println("<style>");           
                out.println("table {");
                out.println("border-collapse: collapse; width: 100%;");
                out.println("}");
                out.println("table, td, th {");
                out.println("border: 3px solid black; height: 30px; background-color: #FFFFDB;}");
                out.println("</style>");		
                out.println("</head>");            
                out.println("<body>");            
                out.println("<div style='background-color:#D2D2D2; border-radius: 20px; box-shadow: 10px 10px 5px #797D92; padding: 5px 5px 20px 5px; width: 1000px; margin-left: auto; margin-right: auto; text-align: left;'>");
                out.println("<h1 style='color: blue; font-family:Comic Sans MS; text-shadow: 2px 2px 2px #888888;' > <center>Hotel Anesis</center></h1>");	             
                out.println("<form>");
                out.println("<table  style='width:100%; box-shadow: 8px 8px 5px #888888;'>");
                out.println("<centre>");
                out.println("<tr>");
                out.println("<td><center><b>A/A</b></center></td>");
                out.println("<td><center><b>Δωμάτιο</b></center></td>");
                out.println("<td><center><b>Άφιξη</b></center></td>");
                out.println("<td><center><b>Αναχώρηση</b></center></td>");
                out.println("<td><center><b>Πλήθος</b></center></td>");
                out.println("<td><center><b>Όνομα Επικ.</b></center></td>");
                out.println("<td><center><b>Τηλέφωνο</b></center></td>");
                out.println("</tr>");

                    while(dbRs.next())
                    {

                    int idcode = dbRs.getInt("id");
                    String nroom = dbRs.getString("room");
                    Date arr = dbRs.getDate("arrival");
                    Date dep = dbRs.getDate("departure");
                    int sum = dbRs.getInt("total");             
                    String fullname = dbRs.getString("name");
                    String phone = dbRs.getString("pnumber");
//========== metatroph format tou date apo YYYY-MM-dd se dd/MM/YYYY ===========// 
                    String a = new String("");                  
                    String b = new String("");
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/YYYY");
                    a = format.format(arr);
                    b = format.format(dep);

                    out.println("<tr>");
                    out.println("<td><center>"+ idcode +    "</center></td>");
                    out.println("<td><center>"+ nroom +     "</center></td>");
                    out.println("<td><center>"+ a +"</center></td>");
                    out.println("<td><center>"+ b +"</center></td>");
                    out.println("<td><center>"+ sum +       "</center></td>");
                    out.println("<td><center>"+ fullname +  "</center></td>");
                    out.println("<td><center>"+ phone +     "</center></td>");
                    out.println("</tr>");                               
                    }            
                out.println("</table>");
                out.println("<center>"); 
                out.println("<input type=button style='width:90px;height:30px; box-shadow: 3px 3px 3px #888888;'; onClick=\"parent.location='index.html'\" value='Επιστροφή'>");
                out.println("</center>"); 
                out.println("<br>");
                out.println("</div>");	
                out.println("</form>");
                out.println("</body>");
                out.println("</html>");            
            }
            
                
            catch(SQLException ex)  
            { 
           //System.out.println(ex.getMessage());
            }              
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
