package com.danil.sdfviewer.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.danil.sdfviewer.bean.SDFBean;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Part;

/**
 *
 * @author danil
 */
@MultipartConfig
@WebServlet(name = "SDFServlet", urlPatterns = {"/SDFServlet"})
public class SDFServlet extends HttpServlet {

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
            throws ServletException, IOException, Exception {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

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
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(SDFServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            Part filePart = request.getPart("file");        // Retrieves <input type="file" name="file">
            //String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            InputStream fileContent = filePart.getInputStream();
            SDFBean.generateJsonFile(fileContent);
            SDFBean.readJsonFile();

        } catch (Exception ex) {
            Logger.getLogger(SDFServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        //response.setContentType("application/json");
        //response.setCharacterEncoding("UTF-8");
        //String json = SDFBean.getJsonCompoundsList().get(0).toString();
       // response.getWriter().write(json);	
    /*  String ajaxUpdateResult = "";
        try {
            List items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);            
            for (FileItem item : items) {
                if (!item.isFormField()) {
                    String fileName = item.getName();
                    InputStream content = item.getInputStream();
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    // Do whatever with the content InputStream.
                    System.out.println(Streams.asString(content));
                    ajaxUpdateResult += "File " + fileName + " is successfully uploaded\n\r";
                }
            }
        } catch (FileUploadException e) {
            throw new ServletException("Parsing file upload failed.", e);
        }
        response.getWriter().print(ajaxUpdateResult);
      */
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
