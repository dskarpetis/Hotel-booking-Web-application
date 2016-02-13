/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import static java.awt.Event.INSERT;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.System.out;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.beans.binding.Bindings.select;
import static javafx.scene.input.KeyCode.K;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static org.apache.coyote.http11.Constants.a;

/**
 *
 * @author dskarpetis
 */

@WebServlet("/addRooms")
public class addRooms extends HttpServlet {

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
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");


        String JDBC_DRIVER = "com.mysql.jdbc.Driver"; 
        String db_URL = "jdbc:mysql:///anesis?useUnicode=true&characterEncoding=utf-8";
        String dbUSER = "anesisdba";
        String dbPASS = "12345";
        Connection dbConn = null;
        Statement dbStmt = null;
        
        java.util.Date date = new java.util.Date();
        
        try (PrintWriter out = response.getWriter()) { 

//======= diavasma stoixeiwn ths formsas addRooms.html =======================//
            String _room = request.getParameter("room");
            String _arrival=  request.getParameter("arrival");     
            String _departure=  request.getParameter("departure");
            String _total = request.getParameter("total");        
            String _name = request.getParameter("name");
            String _phone = request.getParameter("phone");

//==========================elegxos gia keno pedio============================//        
            if(_room == null || _room.isEmpty()) {
                response.sendRedirect("error1.html");
                    return;   
            }        
            if(_arrival == null || _arrival.isEmpty()) {
                response.sendRedirect("error1.html");
                    return;   
            }   
            if(_departure == null || _departure.isEmpty()) {
                response.sendRedirect("error1.html");
                    return;   
            }
            if(_total == null || _total.isEmpty()) {
                response.sendRedirect("error1.html");
                    return;   
            }
            if(_name == null || _name.isEmpty()) {
                response.sendRedirect("error1.html");
                    return;   
            }
            if(_phone == null || _phone.isEmpty()) {
                response.sendRedirect("error1.html");
                    return;   
            }        
//===== elegxos na to Format tou Date, an einai ths morfhs "yyyy-MM-dd" ======//        
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            format.setLenient(false);

            ParsePosition pos = new ParsePosition(0);
            format.parse(_arrival, pos);

            if (pos.getErrorIndex() >= 0) {
                response.sendRedirect("error2.html");  
                             return;
            } else if (pos.getIndex() != _arrival.length()) {
                System.out.println("Date parsed but not all input characters used!"
                     + " Decide if it's good or bad for you!");
            } else {
                System.out.println("Input is valid, parsed completely.");
            }       
//============================= elegxos gia room =============================//
            boolean contains = false;
            String [] R = new String [] {"K1","K2","K3","K4","K5","A1","A2","A3","A4","A5"};                     
            for(int i=0; i < R.length; i++){  
                if(R[i].equals(_room)){                    
                    contains = true;                                                
                }
            }
            if (contains == false) {
                response.sendRedirect("error.html");
                return;
            }          
//==================== metatroph String se Date ======================//                                        
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date1 = null;
            try {
                date1 = sdf.parse(_arrival);
            } catch (ParseException ex) {
                Logger.getLogger(addRooms.class.getName()).log(Level.SEVERE, null, ex);
            }
               java.util.Date date2 = null;
            try {
                date2 = sdf.parse(_departure);
            } catch (ParseException ex) {
                Logger.getLogger(addRooms.class.getName()).log(Level.SEVERE, null, ex);
            }
            
 //============ elegxos gia Date afiksis mikroterh ths current day ===========//             
            if(date1.compareTo(date)<0){
                response.sendRedirect("error3.html");
                return;
            }
//====== elegxos gia Date anaxwrisis  mikroterh i ish ths Date afiksis =======//             
            if(date2.compareTo(date1)<=0){
                //System.out.println("Date1 is mikrotero Date2");
                response.sendRedirect("error4.html");
                return;
            }
 //========================= elgxos gia plithos atomwn =======================//                    
            if(Integer.parseInt(_total)<1 || Integer.parseInt(_total)>5)
            {
                 response.sendRedirect("error5.html");  
                 return;
            }                
 //===================== elegxos gia arithimitka psifia ======================//             
            if (! _phone.matches("[0-9]+")) {
                response.sendRedirect("error6.html");
                return;
            } 

 //============================= sundesh me vash =============================//             
            try
            {                                    
                Class.forName(JDBC_DRIVER);
                dbConn = DriverManager.getConnection(db_URL, dbUSER, dbPASS);
            }
            catch(Exception ex)
            {
                out.println("<h2>Δεν υπάρχει σύνδεση με τη βαση!!!<h2>");
                dbConn = null;
                boolean ans = false;
            } 
                
                
            if(dbConn != null)
                try {               
                dbStmt =  dbConn.createStatement();
//=============== epilogh eggrafwn stoixeiwn ston pinaka rooms ===============//
                String sql = "SELECT id, room, arrival, departure, total, name, "
                + "pnumber FROM rooms";
                ResultSet dbRs = dbStmt.executeQuery(sql);
                while(dbRs.next())
                {

                    int idcode = dbRs.getInt("id");
                    String nroom = dbRs.getString("room");
                    Date arr = dbRs.getDate("arrival");
                    Date dep = dbRs.getDate("departure");
                    int sum = dbRs.getInt("total");             
                    String fullname = dbRs.getString("name");
                    String phone = dbRs.getString("pnumber");
//========== elegxos gia idia hmeromnia afikshs kai idio dwmatio =============//                     
                   DateFormat  form = new SimpleDateFormat("YYYY-MM-dd");
                   String aarr = form.format(arr);
                   if(_arrival.equals(aarr) && nroom.equals(_room)){       
                        response.sendRedirect("error7.html");
                        return;
                    }                    
                }   
 //=================== eisagwgh eggrafhs ston pinaka rooms ===================// 
                PreparedStatement pst = dbConn.prepareStatement("insert into rooms"
                + "(room,arrival,departure,total,name,pnumber) values(?,?,?,?,?,?)");
                
                pst.setString(1,_room);  
                pst.setString(2,_arrival);        
                pst.setString(3,_departure);  
                pst.setString(4,_total); 
                pst.setString(5,_name);  
                pst.setString(6,_phone);               
                
                int i = pst.executeUpdate();
                
                    if(i!=0){  
                        response.sendRedirect("success.html");                   
                        dbStmt.close();
                    }
                    else {  
                        response.sendRedirect("error.html");
                        dbStmt.close();
                    }                 
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
