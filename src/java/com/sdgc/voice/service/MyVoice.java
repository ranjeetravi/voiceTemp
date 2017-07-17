/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdgc.voice.service;

import com.sdgc.voice.util.FileUtil;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *
 * @author ranjeetr
 */
@WebServlet(name = "MyVoice", urlPatterns = {"/MyVoice"})
@MultipartConfig
public class MyVoice extends HttpServlet {

	private static final long serialVersionUID = 2402083450516061414L;

	/**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String outputStr = "";
        try {
        	response.setContentType("text/html;charset=UTF-8");
            
           
            String email = (String) request.getParameter("email");
            
            if (email != null && !email.isEmpty()) {
              outputStr=  new FileUtil().writeAudioFiles(request.getParts(), email);
               // voiceAuthDTO = AuthBOFactory.getAuthBO().train(request, userInfo, deviceInfo);
               
            } else {
                // Invalid request  
                outputStr="Request not formatted correctly";
               
            }
            out.write(outputStr);
        }catch(Exception ex)  { 
            System.err.println("Exception:::"+ex);//new JsonUtil().createResponseJSON(voiceAuthDTO);
            
        
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
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
     * Handles the HTTP
     * <code>POST</code> method.
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
