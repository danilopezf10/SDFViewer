package com.danil.sdfviewer.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Part;
import com.danil.sdfviewer.bean.SDFBean;
import com.danil.sdfviewer.bean.InvalidSDFileException;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author danil
 */
@MultipartConfig
@WebServlet(name = "SDFServlet", urlPatterns = {"/SDFServlet"})
public class SDFServlet extends HttpServlet {

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

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String jsonArray = SDFBean.getCompoundsArray().toJSONString();
        response.getWriter().write(jsonArray);      //Sends back the json file as an array
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
            Part filePart = request.getPart("file");        //Retrieves <input type="file" name="file">
            InputStream fileContent = filePart.getInputStream();
            SDFBean.generateJsonFile(fileContent);

        } catch (ParseException | InvalidSDFileException ex) {
            Logger.getLogger(SDFServlet.class.getName()).log(Level.SEVERE, null, ex);
            try {
                JSONParser parser = new JSONParser();
                SDFBean.setCompoundsArray((JSONArray) parser.parse("[{\"ERROR\": \"" + "ERROR: " + ex.getMessage() + "\"}]"));
            } catch (ParseException ex1) {
                Logger.getLogger(SDFServlet.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }
}
