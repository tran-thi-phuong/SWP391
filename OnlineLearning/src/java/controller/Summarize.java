/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

//Servlet by default
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//for sending request to API
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//for model object
import model.Question;

//for handle json easily
import org.json.JSONArray;
import org.json.JSONObject;

//for get APIKEY in env file
import io.github.cdimascio.dotenv.Dotenv;

//for Data Access
import dal.QuestionDAO;
/**
 *
 * @author 84336
 */
@WebServlet(name = "Summarize", urlPatterns = {"/Summarize"})
public class Summarize extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    Dotenv dotenv = Dotenv.load();
    String API_KEY = dotenv.get("API_KEY");
    private final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + API_KEY;
    QuestionDAO questionDAO = new QuestionDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int questionId = Integer.parseInt(request.getParameter("questionId"));
        Question q = questionDAO.getQuestionById(questionId);
        String userInput = q.getContent();
        userInput += "Summarize it with simple words";
        String jsonResponse = callGenerativeAPI(userInput);
        String extractedText = extractTextFromJson(jsonResponse);
        extractedText = extractedText.replace("**", "");
        String sum = extractTextFromJson(extractedText);
    }

    private String callGenerativeAPI(String input) throws IOException {
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String jsonInputString = String.format("{\"contents\":[{\"parts\":[{\"text\":\"%s\"}]}]}", input);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] inputBytes = jsonInputString.getBytes("utf-8");
            os.write(inputBytes, 0, inputBytes.length);
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }

        return response.toString();
    }

    private static String extractTextFromJson(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray candidates = jsonObject.getJSONArray("candidates");
        JSONObject firstCandidate = candidates.getJSONObject(0);
        JSONObject content = firstCandidate.getJSONObject("content");
        JSONArray parts = content.getJSONArray("parts");
        String output = parts.getJSONObject(0).getString("text");
        return output;
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
