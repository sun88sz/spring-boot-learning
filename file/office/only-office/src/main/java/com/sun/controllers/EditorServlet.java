package com.sun.controllers;

import com.sun.entities.FileModel;
import com.sun.helpers.DocumentManager;
import com.sun.helpers.OfficeProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Controller
public class EditorServlet {

    @Autowired
    private OfficeProperties officeProperties;

    @RequestMapping("/EditorServlet")
    public String processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DocumentManager.Init(request, response);

        String fileName = request.getParameter("fileName");
        String fileExt = request.getParameter("fileExt");

        if (fileExt != null) {
            try {
                fileName = DocumentManager.CreateDemo(fileExt);
            } catch (Exception ex) {
                response.getWriter().write("Error: " + ex.getMessage());
            }
        }

        FileModel file = new FileModel(fileName);
        if ("embedded".equals(request.getParameter("mode")))
            file.InitDesktop();
        if ("view".equals(request.getParameter("mode")))
            file.editorConfig.mode = "view";

        if (DocumentManager.TokenEnabled()) {
            file.BuildToken();
        }

        request.setAttribute("file", file);
        request.setAttribute("docserviceApiUrl", officeProperties.getUrl().getApi());
//        request.getRequestDispatcher("editor.jsp").forward(request, response);

        return "editor";
    }

}
