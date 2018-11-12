package tmall.filter;

import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class BackServletFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String contextPath = request.getServletContext().getContextPath();
        String uri = request.getRequestURI(); //获得访问URI
        uri = StringUtils.remove(uri, contextPath); //截取URI访问路径部分，如 admin_category_list
        if (uri.startsWith("/admin_")) {
            String servletPath = StringUtils.substringBetween(uri, "_", "_") + "Servlet"; //categoryServlet
            String method = StringUtils.substringAfterLast(uri, "_"); // list
            request.setAttribute("method", method);
            req.getRequestDispatcher("/" + servletPath).forward(request, response); // 服务端跳转，算同一次请求，数据可以放 request 里传递
            return;
        }

        chain.doFilter(request, response);
    }

    public void destroy() {
    }

    public void init(FilterConfig arg0) {

    }
}
